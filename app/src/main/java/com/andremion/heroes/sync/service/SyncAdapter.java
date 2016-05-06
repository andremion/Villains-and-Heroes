package com.andremion.heroes.sync.service;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.content.SyncStats;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelException;
import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.ComicVO;
import com.andremion.heroes.api.data.SeriesVO;
import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.data.DataContract.Comic;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.data.DataContract.Series;
import com.andremion.heroes.sync.util.EntryManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handle the transfer of data between a server and an app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String ACTION_SYNC_STATUS = SyncAdapter.class.getPackage().getName() + ".action.SYNC_STATUS";
    public static final String EXTRA_SYNCING = SyncAdapter.class.getPackage().getName() + ".extra.SYNCING";
    public static final String EXTRA_ERROR = SyncAdapter.class.getPackage().getName() + ".extra.ERROR";
    public static final String KEY_SYNCING = SyncAdapter.class.getSimpleName() + ".SYNCING";

    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();

    //
    private final ContentResolver mContentResolver;

    //
    private final LocalBroadcastManager mBroadcastManager;

    //
    private final SharedPreferences mPrefs;

    //
    private final MarvelApi mMarvelApi;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        //
        mContentResolver = context.getContentResolver();
        //
        mBroadcastManager = LocalBroadcastManager.getInstance(context);
        //
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //
        mMarvelApi = MarvelApi.getInstance();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Intent status = new Intent(ACTION_SYNC_STATUS);

        Log.v(LOG_TAG, "Beginning syncing");
        mBroadcastManager.sendBroadcast(new Intent(status).putExtra(EXTRA_SYNCING, true));
        mPrefs.edit().putBoolean(KEY_SYNCING, true).commit();

        try {

            EntryManager entryManager;

            if (extras != null && extras.containsKey(Section.COLUMN_CHARACTER)) {
                final long characterId = extras.getLong(Section.COLUMN_CHARACTER);

                Log.d(LOG_TAG, "Fetching comics...");
                entryManager = new EntryManager(getContext())
                        .setTotalKey(EntryManager.Keys.getComicsTotalKey(characterId))
                        .setFetchedKey(EntryManager.Keys.getComicsFetchedKey(characterId))
                        .setDoneKey(EntryManager.Keys.getComicsDoneKey(characterId))
                        .setSyncStats(syncResult.stats);
                entryManager.sync(new EntryManager.FetchInterface<ComicVO>() {
                    @Override
                    public MarvelResult<ComicVO> onFetchEntries(int offset) throws IOException, MarvelException {
                        return mMarvelApi.listComics(characterId, offset);
                    }

                    @Override
                    public void onInsertEntries(MarvelResult<ComicVO> result, SyncStats syncStats) {
                        insertComicsIntoDatabase(result.getEntries(), syncStats);
                        syncStats.numEntries += result.getCount();
                    }
                });

                Log.d(LOG_TAG, "Fetching series...");
                entryManager = new EntryManager(getContext())
                        .setTotalKey(EntryManager.Keys.getSeriesTotalKey(characterId))
                        .setFetchedKey(EntryManager.Keys.getSeriesFetchedKey(characterId))
                        .setDoneKey(EntryManager.Keys.getSeriesDoneKey(characterId))
                        .setSyncStats(syncResult.stats);
                entryManager.sync(new EntryManager.FetchInterface<SeriesVO>() {
                    @Override
                    public MarvelResult<SeriesVO> onFetchEntries(int offset) throws IOException, MarvelException {
                        return mMarvelApi.listSeries(characterId, offset);
                    }

                    @Override
                    public void onInsertEntries(MarvelResult<SeriesVO> result, SyncStats syncStats) {
                        insertSeriesIntoDatabase(result.getEntries(), syncStats);
                        syncStats.numEntries += result.getCount();
                    }
                });

            } else {

                Log.d(LOG_TAG, "Fetching characters...");
                entryManager = new EntryManager(getContext())
                        .setTotalKey(EntryManager.Keys.getCharactersTotalKey())
                        .setFetchedKey(EntryManager.Keys.getCharactersFetchedKey())
                        .setDoneKey(EntryManager.Keys.getCharactersDoneKey())
                        .setSyncStats(syncResult.stats);
                entryManager.sync(new EntryManager.FetchInterface<CharacterVO>() {
                    @Override
                    public MarvelResult<CharacterVO> onFetchEntries(int offset) throws IOException, MarvelException {
                        return mMarvelApi.listCharacters(offset);
                    }

                    @Override
                    public void onInsertEntries(MarvelResult<CharacterVO> result, SyncStats syncStats) {
                        insertCharactersIntoDatabase(result.getEntries(), syncStats);
                        syncStats.numEntries += result.getCount();
                    }
                });

            }

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            syncResult.stats.numIoExceptions++;
            status.putExtra(EXTRA_ERROR, e);
        } catch (MarvelException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            syncResult.stats.numParseExceptions++;
            status.putExtra(EXTRA_ERROR, e);
        }

        mBroadcastManager.sendBroadcast(new Intent(status).putExtra(EXTRA_SYNCING, false));
        mPrefs.edit().putBoolean(KEY_SYNCING, false).commit();
        Log.v(LOG_TAG, "Syncing complete. " + syncResult.toString());
    }

    private void insertCharactersIntoDatabase(List<CharacterVO> entries, SyncStats syncStats) {
        List<ContentValues> characterValuesList = new ArrayList<>();
        List<ContentValues> sectionValuesList = new ArrayList<>();
        for (CharacterVO character : entries) {
            ContentValues values = new ContentValues();
            values.put(Character._ID, character.getId());
            values.put(Character.COLUMN_NAME, character.getName());
            values.put(Character.COLUMN_DESCRIPTION, character.getDescription());
            values.put(Character.COLUMN_THUMBNAIL, character.getThumbnail());
            values.put(Character.COLUMN_IMAGE, character.getImage());
            values.put(Character.COLUMN_DETAIL, character.getDetail());
            values.put(Character.COLUMN_WIKI, character.getWiki());
            values.put(Character.COLUMN_COMIC_LINK, character.getComicLink());
            characterValuesList.add(values);
            for (ComicVO comic : character.getComics()) {
                values = new ContentValues();
                values.put(Section.COLUMN_TYPE, Section.TYPE_COMIC);
                values.put(Section.COLUMN_CHARACTER, character.getId());
                values.put(Section.COLUMN_DATA_ID, comic.getId());
                values.put(Section.COLUMN_NAME, comic.getTitle());
                sectionValuesList.add(values);
            }
            for (SeriesVO series : character.getSeries()) {
                values = new ContentValues();
                values.put(Section.COLUMN_TYPE, Section.TYPE_SERIES);
                values.put(Section.COLUMN_CHARACTER, character.getId());
                values.put(Section.COLUMN_DATA_ID, series.getId());
                values.put(Section.COLUMN_NAME, series.getTitle());
                sectionValuesList.add(values);
            }
        }

        ContentValues[] characterValues = new ContentValues[characterValuesList.size()];
        int charactersInserted = mContentResolver.bulkInsert(Character.CONTENT_URI, characterValuesList.toArray(characterValues));
        int skippedEntries = characterValues.length - charactersInserted;
        if (skippedEntries > 0) {
            Log.w(LOG_TAG, skippedEntries + " Characters skipped");
            syncStats.numSkippedEntries += charactersInserted;
        }

        ContentValues[] sectionValues = new ContentValues[sectionValuesList.size()];
        int sectionsInserted = mContentResolver.bulkInsert(Section.CONTENT_URI, sectionValuesList.toArray(sectionValues));
        int skippedSections = characterValues.length - sectionsInserted;
        if (skippedSections > 0) {
            Log.w(LOG_TAG, skippedSections + " Sections skipped");
            syncStats.numSkippedEntries += sectionsInserted;
        }
    }

    private void insertComicsIntoDatabase(List<ComicVO> entries, SyncStats syncStats) {
        List<ContentValues> valuesList = new ArrayList<>();
        for (ComicVO comic : entries) {
            ContentValues values = new ContentValues();
            values.put(Comic._ID, comic.getId());
            values.put(Comic.COLUMN_TITLE, comic.getTitle());
            values.put(Comic.COLUMN_THUMBNAIL, comic.getThumbnail());
            values.put(Comic.COLUMN_IMAGE, comic.getImage());
            valuesList.add(values);
        }
        ContentValues[] values = new ContentValues[valuesList.size()];
        int rows = mContentResolver.bulkInsert(Comic.CONTENT_URI, valuesList.toArray(values));
        int skipped = values.length - rows;
        if (skipped > 0) {
            Log.w(LOG_TAG, skipped + " Comic skipped");
            syncStats.numSkippedEntries += skipped;
        }
    }

    private void insertSeriesIntoDatabase(List<SeriesVO> entries, SyncStats syncStats) {
        List<ContentValues> valuesList = new ArrayList<>();
        for (SeriesVO series : entries) {
            ContentValues values = new ContentValues();
            values.put(Series._ID, series.getId());
            values.put(Series.COLUMN_TITLE, series.getTitle());
            values.put(Series.COLUMN_THUMBNAIL, series.getThumbnail());
            values.put(Series.COLUMN_IMAGE, series.getImage());
            valuesList.add(values);
        }
        ContentValues[] values = new ContentValues[valuesList.size()];
        int rows = mContentResolver.bulkInsert(Series.CONTENT_URI, valuesList.toArray(values));
        int skipped = values.length - rows;
        if (skipped > 0) {
            Log.w(LOG_TAG, skipped + " Series skipped");
            syncStats.numSkippedEntries += skipped;
        }
    }

}