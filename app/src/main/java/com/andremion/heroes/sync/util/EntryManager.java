package com.andremion.heroes.sync.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncStats;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andremion.heroes.api.MarvelException;
import com.andremion.heroes.api.MarvelResult;

import java.io.IOException;

public class EntryManager {

    private static final String LOG_TAG = EntryManager.class.getSimpleName();

    private final SharedPreferences mPrefs;
    private String mTotalKey;
    private String mFetchedKey;
    private String mDoneKey;
    private SyncStats mSyncStats;

    public EntryManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public EntryManager setTotalKey(String totalKey) {
        mTotalKey = totalKey;
        return this;
    }

    public EntryManager setFetchedKey(String fetchedKey) {
        mFetchedKey = fetchedKey;
        return this;
    }

    public EntryManager setDoneKey(String doneKey) {
        mDoneKey = doneKey;
        return this;
    }

    public EntryManager setSyncStats(SyncStats syncStats) {
        mSyncStats = syncStats;
        return this;
    }

    private void ensureFields(FetchInterface callback) {
        if (mTotalKey == null
                || mFetchedKey == null
                || mDoneKey == null
                || mSyncStats == null
                || callback == null) {
            throw new IllegalArgumentException();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public <T> void sync(@NonNull FetchInterface<T> callback) throws IOException, MarvelException {

        ensureFields(callback);

        if (!mPrefs.getBoolean(mDoneKey, false)) {

            int total = mPrefs.getInt(mTotalKey, 0);
            int fetched = mPrefs.getInt(mFetchedKey, -1);
            int offset = fetched == -1 ? 0 : fetched;

            while (fetched < total) {

                // Fetch entries
                MarvelResult<T> result = callback.onFetchEntries(offset);

                // Put total into preferences
                if (total == 0 && result.getTotal() > 0) {
                    total = result.getTotal();
                    mPrefs.edit().putInt(mTotalKey, total).commit();
                }

                // Insert entries into database
                if (result.getCount() > 0) {
                    callback.onInsertEntries(result, mSyncStats);
                }

                // Update fetched count
                if (fetched == -1) {
                    fetched = result.getCount();
                } else {
                    fetched += result.getCount();
                }
                mPrefs.edit().putInt(mFetchedKey, fetched).commit();

                Log.d(LOG_TAG, String.format("Fetched %d of %d entries...", fetched, total));

                // Increase offset
                offset = fetched;
            }

            mPrefs.edit().putBoolean(mDoneKey, true).commit();
        }
    }

    public interface FetchInterface<T> {

        MarvelResult<T> onFetchEntries(int offset) throws IOException, MarvelException;

        void onInsertEntries(MarvelResult<T> result, SyncStats syncStats);
    }

    @SuppressLint("DefaultLocale")
    public static class Keys {
        private static final String TOTAL_CHARACTER = "total_characters";
        private static final String TOTAL_COMICS = "total_comics";
        private static final String TOTAL_SERIES = "total_series";
        private static final String FETCHED_CHARACTER = "fetched_characters";
        private static final String FETCHED_COMICS = "fetched_comics";
        private static final String FETCHED_SERIES = "fetched_series";
        private static final String DONE_CHARACTERS = "done_characters";
        private static final String DONE_COMICS = "done_comics";
        private static final String DONE_SERIES = "done_series";

        public static String getCharactersTotalKey() {
            return TOTAL_CHARACTER;
        }

        public static String getCharactersFetchedKey() {
            return FETCHED_CHARACTER;
        }

        public static String getCharactersDoneKey() {
            return DONE_CHARACTERS;
        }

        public static String getComicsTotalKey(long characterId) {
            return String.format(TOTAL_COMICS + "(%d)", characterId);
        }

        public static String getComicsFetchedKey(long characterId) {
            return String.format(FETCHED_COMICS + "(%d)", characterId);
        }

        public static String getComicsDoneKey(long characterId) {
            return String.format(DONE_COMICS + "(%d)", characterId);
        }

        public static String getSeriesTotalKey(long characterId) {
            return String.format(TOTAL_SERIES + "(%d)", characterId);
        }

        public static String getSeriesFetchedKey(long characterId) {
            return String.format(FETCHED_SERIES + "(%d)", characterId);
        }

        public static String getSeriesDoneKey(long characterId) {
            return String.format(DONE_SERIES + "(%d)", characterId);
        }

    }

}
