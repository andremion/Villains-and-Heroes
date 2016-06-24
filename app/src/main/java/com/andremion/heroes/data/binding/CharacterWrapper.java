package com.andremion.heroes.data.binding;

import android.database.Cursor;

import com.andremion.heroes.data.DataContract.Character;

public class CharacterWrapper {

    public static CharacterWrapper wrap(Cursor data) {
        return new CharacterWrapper(data);
    }

    private Cursor mData;

    private CharacterWrapper(Cursor data) {
        mData = data;
    }

    public String getName() {
        return mData.getString(mData.getColumnIndex(Character.COLUMN_NAME));
    }

    public String getImage() {
        return mData.getString(mData.getColumnIndex(Character.COLUMN_IMAGE));
    }

    public String getDescription() {
        return mData.getString(mData.getColumnIndex(Character.COLUMN_DESCRIPTION));
    }

    public String getDetail() {
        return mData.getString(mData.getColumnIndex(Character.COLUMN_DETAIL));
    }

    public String getWiki() {
        return mData.getString(mData.getColumnIndex(Character.COLUMN_WIKI));
    }

    public String getComicLink() {
        return mData.getString(mData.getColumnIndex(Character.COLUMN_COMIC_LINK));
    }

}