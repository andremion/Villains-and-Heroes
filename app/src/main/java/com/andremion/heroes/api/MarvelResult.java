/*
 * Copyright (c) 2016. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
