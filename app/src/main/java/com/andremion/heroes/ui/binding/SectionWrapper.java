package com.andremion.heroes.ui.binding;

import android.database.Cursor;

import com.andremion.heroes.data.DataContract.Section;

public class SectionWrapper {

    public static SectionWrapper wrap(Cursor data) {
        return new SectionWrapper(data);
    }

    protected Cursor mData;

    protected SectionWrapper(Cursor data) {
        mData = data;
    }

    public String getName() {
        return mData.getString(mData.getColumnIndex(Section.COLUMN_NAME));
    }

    public String getTitle() {
        return mData.getString(mData.getColumnIndex(Section.COLUMN_TITLE));
    }

    public String getThumbnail() {
        return mData.getString(mData.getColumnIndex(Section.COLUMN_THUMBNAIL));
    }

    public String getImage() {
        return mData.getString(mData.getColumnIndex(Section.COLUMN_IMAGE));
    }

}