package com.andremion.heroes.data;

import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.data.DataContract.Comic;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.data.DataContract.Series;

import java.util.HashMap;
import java.util.Map;

import static com.andremion.heroes.data.DataContract.*;

public class DataProvider extends android.content.ContentProvider {

    private static final String NUMBER = "/#";
    private static final String TEXT = "/*";

    private static final int CHARACTER = 100;
    private static final int CHARACTER_ID = 101;
    private static final int CHARACTER_SUGGEST = 102;
    private static final int SECTION = 200;
    private static final int SECTION_ID = 201;
    private static final int COMIC = 300;
    private static final int COMIC_ID = 301;
    private static final int SERIES = 400;
    private static final int SERIES_ID = 401;
    private static final int STORIES = 500;
    private static final int STORIES_ID = 501;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sComicQueryBuilder = buildComicQueryBuilder();
    private static final SQLiteQueryBuilder sSeriesQueryBuilder = buildSeriesQueryBuilder();
    private static final SQLiteQueryBuilder sStoriesQueryBuilder = buildStoriesQueryBuilder();
    private DatabaseHelper mDatabaseHelper;

    public DataProvider() {
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        matcher.addURI(authority, Character.URI_PATH, CHARACTER);
        matcher.addURI(authority, Character.URI_PATH + NUMBER, CHARACTER_ID);
        matcher.addURI(authority, Character.URI_PATH + TEXT, CHARACTER_SUGGEST);
        matcher.addURI(authority, Character.URI_PATH + TEXT + TEXT, CHARACTER_SUGGEST);
        matcher.addURI(authority, Section.URI_PATH, SECTION);
        matcher.addURI(authority, Section.URI_PATH + NUMBER, SECTION_ID);
        matcher.addURI(authority, Comic.URI_PATH, COMIC);
        matcher.addURI(authority, Comic.URI_PATH + NUMBER, COMIC_ID);
        matcher.addURI(authority, Series.URI_PATH, SERIES);
        matcher.addURI(authority, Series.URI_PATH + NUMBER, SERIES_ID);
        matcher.addURI(authority, Story.URI_PATH, STORIES);
        matcher.addURI(authority, Story.URI_PATH + NUMBER, STORIES_ID);
        return matcher;
    }

    private static SQLiteQueryBuilder buildComicQueryBuilder() {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setDistinct(true);
        queryBuilder.setTables(Section.ENTITY_NAME +
                " LEFT JOIN " + Comic.ENTITY_NAME +
                " ON " + Section.ENTITY_NAME + "." + Section.COLUMN_DATA_ID +
                " = " + Comic.ENTITY_NAME + "." + Comic._ID
        );
        Map<String, String> projectionMap = new HashMap<>();
        projectionMap.put(Section.ENTITY_NAME + "." + Section._ID, Section.ENTITY_NAME + "." + Section._ID);
        projectionMap.put(Section.COLUMN_DATA_ID, Section.COLUMN_DATA_ID);
        projectionMap.put(Section.COLUMN_TYPE, Section.COLUMN_TYPE);
        projectionMap.put(Section.COLUMN_CHARACTER, Section.COLUMN_CHARACTER);
        projectionMap.put(Section.COLUMN_NAME, Section.COLUMN_NAME);
        projectionMap.put(Comic.COLUMN_TITLE, Comic.COLUMN_TITLE);
        projectionMap.put(Comic.COLUMN_THUMBNAIL, Comic.COLUMN_THUMBNAIL);
        projectionMap.put(Comic.COLUMN_IMAGE, Comic.COLUMN_IMAGE);
        queryBuilder.setProjectionMap(projectionMap);
        return queryBuilder;
    }

    private static SQLiteQueryBuilder buildSeriesQueryBuilder() {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setDistinct(true);
        queryBuilder.setTables(Section.ENTITY_NAME +
                " LEFT JOIN " + Series.ENTITY_NAME +
                " ON " + Section.ENTITY_NAME + "." + Section.COLUMN_DATA_ID +
                " = " + Series.ENTITY_NAME + "." + Series._ID
        );
        Map<String, String> projectionMap = new HashMap<>();
        projectionMap.put(Section.ENTITY_NAME + "." + Section._ID, Section.ENTITY_NAME + "." + Section._ID);
        projectionMap.put(Section.COLUMN_DATA_ID, Section.COLUMN_DATA_ID);
        projectionMap.put(Section.COLUMN_TYPE, Section.COLUMN_TYPE);
        projectionMap.put(Section.COLUMN_CHARACTER, Section.COLUMN_CHARACTER);
        projectionMap.put(Section.COLUMN_NAME, Section.COLUMN_NAME);
        projectionMap.put(Series.COLUMN_TITLE, Series.COLUMN_TITLE);
        projectionMap.put(Series.COLUMN_THUMBNAIL, Series.COLUMN_THUMBNAIL);
        projectionMap.put(Series.COLUMN_IMAGE, Series.COLUMN_IMAGE);
        queryBuilder.setProjectionMap(projectionMap);
        return queryBuilder;
    }

    private static SQLiteQueryBuilder buildStoriesQueryBuilder() {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setDistinct(true);
        queryBuilder.setTables(Section.ENTITY_NAME +
                " LEFT JOIN " + Story.ENTITY_NAME +
                " ON " + Section.ENTITY_NAME + "." + Section.COLUMN_DATA_ID +
                " = " + Story.ENTITY_NAME + "." + Story._ID
        );
        Map<String, String> projectionMap = new HashMap<>();
        projectionMap.put(Section.ENTITY_NAME + "." + Section._ID, Section.ENTITY_NAME + "." + Section._ID);
        projectionMap.put(Section.COLUMN_DATA_ID, Section.COLUMN_DATA_ID);
        projectionMap.put(Section.COLUMN_TYPE, Section.COLUMN_TYPE);
        projectionMap.put(Section.COLUMN_CHARACTER, Section.COLUMN_CHARACTER);
        projectionMap.put(Section.COLUMN_NAME, Section.COLUMN_NAME);
        projectionMap.put(Story.COLUMN_TITLE, Story.COLUMN_TITLE);
        projectionMap.put(Story.COLUMN_THUMBNAIL, Story.COLUMN_THUMBNAIL);
        projectionMap.put(Story.COLUMN_IMAGE, Story.COLUMN_IMAGE);
        queryBuilder.setProjectionMap(projectionMap);
        return queryBuilder;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case CHARACTER:

                int limit = getOffset(uri) + getLimit(uri);
                cursor = db.query(Character.ENTITY_NAME,
                        projection, selection, selectionArgs, null, null,
                        sortOrder, limit > 0 ? String.valueOf(limit) : null);

                break;
            case CHARACTER_ID:

                cursor = db.query(Character.ENTITY_NAME,
                        projection, Character._ID + " = " + ContentUris.parseId(uri), null, null, null, sortOrder);

                break;
            case CHARACTER_SUGGEST:

                String query = uri.getLastPathSegment();
                String limitArg = uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT);

                if (SearchManager.SUGGEST_URI_PATH_QUERY.equals(query)) {

                    cursor = db.query(Character.ENTITY_NAME,
                            projection, selection, selectionArgs, null, null,
                            sortOrder, limitArg);

                } else {

                    cursor = db.query(Character.ENTITY_NAME,
                            projection, Character.COLUMN_NAME + " LIKE ?", new String[]{query + "%"}, null, null,
                            sortOrder, limitArg);

                }

                cursor = getCharacterSuggestCursor(cursor);

                break;
            case SECTION:

                String type = selectionArgs != null && selectionArgs.length > 0 ? selectionArgs[0] : null;

                // Fetch from cache
                if (Section.TYPE_COMIC.equals(type)) {
                    cursor = sComicQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                    if (cursor != null) {
                        //noinspection ConstantConditions
                        cursor.setNotificationUri(getContext().getContentResolver(), Comic.CONTENT_URI);
                    }
                    return cursor;
                } else if (Section.TYPE_SERIES.equals(type)) {
                    cursor = sSeriesQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                    if (cursor != null) {
                        //noinspection ConstantConditions
                        cursor.setNotificationUri(getContext().getContentResolver(), Series.CONTENT_URI);
                    }
                    return cursor;
                } else if (Section.TYPE_STORIES.equals(type)) {
                    cursor = sStoriesQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                    if (cursor != null) {
                        //noinspection ConstantConditions
                        cursor.setNotificationUri(getContext().getContentResolver(), Story.CONTENT_URI);
                    }
                    return cursor;
                }

                break;
            case COMIC_ID:

                cursor = sComicQueryBuilder.query(db, projection,
                        Section.COLUMN_TYPE + " = ? AND " +
                                Section.COLUMN_DATA_ID + " = " + ContentUris.parseId(uri),
                        new String[]{Section.TYPE_COMIC}, null, null, sortOrder);

                break;

            case SERIES_ID:

                cursor = sSeriesQueryBuilder.query(db, projection,
                        Section.COLUMN_TYPE + " = ? AND " +
                                Section.COLUMN_DATA_ID + " = " + ContentUris.parseId(uri),
                        new String[]{Section.TYPE_SERIES}, null, null, sortOrder);

                break;

            case STORIES_ID:

                cursor = sStoriesQueryBuilder.query(db, projection,
                        Section.COLUMN_TYPE + " = ? AND " +
                                Section.COLUMN_DATA_ID + " = " + ContentUris.parseId(uri),
                        new String[]{Section.TYPE_STORIES}, null, null, sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri");
        }

        if (cursor != null) {
            //noinspection ConstantConditions
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    private Cursor getCharacterSuggestCursor(Cursor cursor) {

        if (cursor != null) {
            MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                    BaseColumns._ID,
                    SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.SUGGEST_COLUMN_ICON_1,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA}
            );
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(Character._ID));
                matrixCursor.newRow()
                        .add(id)
                        .add(cursor.getString(cursor.getColumnIndex(Character.COLUMN_NAME)))
                        .add(cursor.getString(cursor.getColumnIndex(Character.COLUMN_THUMBNAIL)))
                        .add(Character.buildUri(id));
            }
            cursor.close();
            cursor = matrixCursor;
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        String entityName = getEntityName(uri);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = db.insert(entityName, null, values);
        if (id > 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        String entityName = getEntityName(uri);
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long id = db.insert(entityName, null, value);
                if (id > 0) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String getEntityName(@NonNull Uri uri) {
        String entityName;
        switch (sUriMatcher.match(uri)) {
            case CHARACTER:
                entityName = Character.ENTITY_NAME;
                break;
            case SECTION:
                entityName = Section.ENTITY_NAME;
                break;
            case COMIC:
                entityName = Comic.ENTITY_NAME;
                break;
            case SERIES:
                entityName = Series.ENTITY_NAME;
                break;
            case STORIES:
                entityName = Story.ENTITY_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        return entityName;
    }
}
