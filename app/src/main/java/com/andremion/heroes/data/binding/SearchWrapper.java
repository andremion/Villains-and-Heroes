package com.andremion.heroes.data.binding;

import android.app.SearchManager;
import android.database.Cursor;

public class SearchWrapper {

    public static SearchWrapper wrap(Cursor data) {
        return new SearchWrapper(data);
    }

    private Cursor mData;

    private SearchWrapper(Cursor data) {
        mData = data;
    }

    public String getText() {
        return mData.getString(mData.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
    }

    public String getImage() {
        return mData.getString(mData.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1));
    }

}