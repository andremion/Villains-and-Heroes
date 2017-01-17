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

package com.andremion.heroes.ui.home;

import android.support.annotation.NonNull;
import android.view.View;

import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelCallback;
import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.json.CharacterDataWrapper;
import com.andremion.heroes.api.util.DataParser;
import com.andremion.heroes.ui.AbsPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends AbsPresenter<MainContract.View> implements MainContract.Actions {

    private final MarvelApi mMarvelApi;
    private final List<CharacterVO> mEntries;
    private String mAttribution;
    private boolean mHasMore;

    public MainPresenter(@NonNull MarvelApi marvelApi) {
        mMarvelApi = marvelApi;
        mEntries = new ArrayList<>();
    }

    @Override
    public void initScreen() {
        if (mEntries.isEmpty()) {
            loadCharacters(0);
        } else {
            mView.showResult(mEntries);
            mView.showAttribution(mAttribution);
            mView.stopProgress(mHasMore);
        }
    }

    @Override
    public void loadCharacters(int offset) {
        mView.showProgress();
        mMarvelApi.listCharacters(offset, new MarvelCallback<CharacterDataWrapper>() {

            @Override
            public void onResult(CharacterDataWrapper data) {
                MarvelResult<CharacterVO> result = DataParser.parse(data);
                mEntries.addAll(result.getEntries());
                mAttribution = result.getAttribution();
                int offset = result.getOffset();
                int count = result.getCount();
                int total = result.getTotal();
                mHasMore = total > offset + count;
                if (!isViewAttached()) {
                    return;
                }
                mView.showResult(mEntries);
                mView.showAttribution(mAttribution);
                mView.stopProgress(mHasMore);
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                mView.showError(e);
                mView.stopProgress(mHasMore);
            }
        });
    }

    @Override
    public void characterClick(@NonNull View heroView, @NonNull CharacterVO character) {
        mView.openCharacter(heroView, character);
    }

    @Override
    public void searchClick() {
        mView.openSearch();
    }

    @Override
    public void refresh() {
        mEntries.clear();
        loadCharacters(0);
    }

}
