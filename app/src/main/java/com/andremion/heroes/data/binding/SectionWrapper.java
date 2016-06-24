package com.andremion.heroes.data.binding;

import android.database.Cursor;

import com.andremion.heroes.data.DataContract.Section;

public class SectionWrapper {

    public static SectionWrapper wrap(Cursor data) {
        return new SectionWrapper(data);
    }

    private Cursor mData;

    private SectionWrapper(Cursor data) {
        mData = data;
    }

    public String getName() {
        return mData.getString(mData.getColumnIndex(Section.COLUMN_NAME));
    }

}