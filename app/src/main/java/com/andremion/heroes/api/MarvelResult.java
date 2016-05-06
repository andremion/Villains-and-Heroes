package com.andremion.heroes.api;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MarvelResult<T> {

    private int mTotal;
    private List<T> mEntries;

    public MarvelResult() {
        mTotal = 0;
        mEntries = new ArrayList<>();
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public int getCount() {
        return mEntries.size();
    }

    public List<T> getEntries() {
        return mEntries;
    }

    public void setEntries(@NonNull List<T> entries) {
        mEntries = entries;
    }
}
