package com.andremion.heroes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.andremion.heroes.data.DataContract.Character;
import com.andremion.heroes.data.DataContract.Comic;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.data.DataContract.Series;

import static com.andremion.heroes.data.DataContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "heroes.db";
    private static final int DATABASE_VERSION = 9;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_CHARACTER_TABLE = "CREATE TABLE " + Character.ENTITY_NAME + " (" +
                Character._ID + " INTEGER, " +
                Character.COLUMN_NAME + " TEXT NOT NULL, " +
                Character.COLUMN_DESCRIPTION + " TEXT, " +
                Character.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
                Character.COLUMN_IMAGE + " TEXT NOT NULL, " +
                Character.COLUMN_DETAIL + " TEXT, " +
                Character.COLUMN_WIKI + " TEXT, " +
                Character.COLUMN_COMIC_LINK + " TEXT, " +
                "PRIMARY KEY (" + Character._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SECTION_TABLE = "CREATE TABLE " + Section.ENTITY_NAME + " (" +
                Section._ID + " INTEGER PRIMARY KEY, " +
                Section.COLUMN_TYPE + " TEXT NOT NULL, " +
                Section.COLUMN_CHARACTER + " INTEGER NOT NULL, " +
                Section.COLUMN_DATA_ID + " INTEGER NOT NULL, " +
                Section.COLUMN_NAME + " TEXT NOT NULL, " +
                "UNIQUE (" +
                Section.COLUMN_TYPE + ", " +
                Section.COLUMN_CHARACTER + ", " +
                Section.COLUMN_DATA_ID +
                ") ON CONFLICT REPLACE, " +
                "FOREIGN KEY (" + Section.COLUMN_CHARACTER + ") REFERENCES " + Character.ENTITY_NAME + " (" + Character._ID + "));";

        final String SQL_CREATE_COMIC_TABLE = "CREATE TABLE " + Comic.ENTITY_NAME + " (" +
                Comic._ID + " INTEGER, " +
                Comic.COLUMN_TITLE + " TEXT NOT NULL, " +
                Comic.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
                Comic.COLUMN_IMAGE + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + Comic._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SERIES_TABLE = "CREATE TABLE " + Series.ENTITY_NAME + " (" +
                Series._ID + " INTEGER, " +
                Series.COLUMN_TITLE + " TEXT NOT NULL, " +
                Series.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
                Series.COLUMN_IMAGE + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + Series._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_STORIES_TABLE = "CREATE TABLE " + Story.ENTITY_NAME + " (" +
                Story._ID + " INTEGER, " +
                Story.COLUMN_TITLE + " TEXT NOT NULL, " +
                Story.COLUMN_THUMBNAIL + " TEXT, " +
                Story.COLUMN_IMAGE + " TEXT, " +
                "PRIMARY KEY (" + Story._ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_CHARACTER_TABLE);
        db.execSQL(SQL_CREATE_SECTION_TABLE);
        db.execSQL(SQL_CREATE_COMIC_TABLE);
        db.execSQL(SQL_CREATE_SERIES_TABLE);
        db.execSQL(SQL_CREATE_STORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Story.ENTITY_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Series.ENTITY_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Comic.ENTITY_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Section.ENTITY_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Character.ENTITY_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

}
