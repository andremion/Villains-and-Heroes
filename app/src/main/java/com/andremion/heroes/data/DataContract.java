package com.andremion.heroes.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {

    static final String CONTENT_AUTHORITY = "com.andremion.heroes.data.provider";
    private static final Uri BASE_CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(CONTENT_AUTHORITY).build();
    private static final String QUERY_PARAMETER_OFFSET = "offset";
    private static final String QUERY_PARAMETER_LIMIT = "limit";

    static int getOffset(Uri uri) {
        try {
            return Integer.parseInt(uri.getQueryParameter(DataContract.QUERY_PARAMETER_OFFSET));
        } catch (Exception e) {
            return 0;
        }
    }

    static int getLimit(Uri uri) {
        try {
            return Integer.parseInt(uri.getQueryParameter(DataContract.QUERY_PARAMETER_LIMIT));
        } catch (Exception e) {
            return 0;
        }
    }

    public static final class Character implements BaseColumns {

        public static final String ENTITY_NAME = Character.class.getSimpleName();
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DETAIL = "detail";
        public static final String COLUMN_WIKI = "wiki";
        public static final String COLUMN_COMIC_LINK = "comiclink";

        public static final String URI_PATH = ENTITY_NAME.toLowerCase();

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(URI_PATH).build();

        public static Uri buildUri(long id) {
            return ContentUris.appendId(CONTENT_URI.buildUpon(), id).build();
        }

        public static Uri buildUri(int offset) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(QUERY_PARAMETER_OFFSET, String.valueOf(offset))
                    .build();
        }

        public static Uri buildUri(int offset, int limit) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(QUERY_PARAMETER_OFFSET, String.valueOf(offset))
                    .appendQueryParameter(QUERY_PARAMETER_LIMIT, String.valueOf(limit))
                    .build();
        }
    }

    public static final class Section implements BaseColumns {

        public static final String TYPE_COMIC = "comic";
        public static final String TYPE_SERIES = "series";
        public static final String TYPE_STORIES = "stories";

        public static final String ENTITY_NAME = Section.class.getSimpleName();
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_CHARACTER = "character";
        public static final String COLUMN_DATA_ID = "data_id";
        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_IMAGE = "image";

        public static final String URI_PATH = ENTITY_NAME.toLowerCase();

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(URI_PATH).build();

        public static Uri buildUri(long id) {
            return ContentUris.appendId(CONTENT_URI.buildUpon(), id).build();
        }
    }

    public static final class Comic implements BaseColumns {

        public static final String ENTITY_NAME = Comic.class.getSimpleName();
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_IMAGE = "image";

        public static final String URI_PATH = ENTITY_NAME.toLowerCase();

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(URI_PATH).build();

        public static Uri buildUri(long id) {
            return ContentUris.appendId(CONTENT_URI.buildUpon(), id).build();
        }
    }

    public static final class Series implements BaseColumns {

        public static final String ENTITY_NAME = Series.class.getSimpleName();
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_IMAGE = "image";

        public static final String URI_PATH = ENTITY_NAME.toLowerCase();

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(URI_PATH).build();

        public static Uri buildUri(long id) {
            return ContentUris.appendId(CONTENT_URI.buildUpon(), id).build();
        }
    }

    public static final class Story implements BaseColumns {

        public static final String ENTITY_NAME = Story.class.getSimpleName();
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_IMAGE = "image";

        public static final String URI_PATH = ENTITY_NAME.toLowerCase();

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(URI_PATH).build();

        public static Uri buildUri(long id) {
            return ContentUris.appendId(CONTENT_URI.buildUpon(), id).build();
        }
    }

}
