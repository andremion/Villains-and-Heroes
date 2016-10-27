package com.andremion.heroes.api;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MarvelResult<T> {

    private int mOffset;
    private int mTotal;
    private List<T> mEntries;
    private String mAttribution;

    public MarvelResult() {
        mOffset = 0;
        mTotal = 0;
        mEntries = new ArrayList<>();
        mAttribution = "";
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
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

    public String getAttribution() {
        return mAttribution;
    }

    public void setAttribution(@NonNull String attribution) {
        mAttribution = attribution;
    }
}
