/*
 * Copyright (c) 2017. André Mion
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

package com.andremion.heroes.ui.search;

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

public class SearchPresenter extends AbsPresenter<SearchContract.View> implements SearchContract.Actions {

    private final MarvelApi mMarvelApi;
    private final List<CharacterVO> mEntries;

    public SearchPresenter() {
        mMarvelApi = MarvelApi.getInstance();
        mEntries = new ArrayList<>();
    }

    @Override
    public void attachView(@NonNull SearchContract.View view) {
        super.attachView(view);
        if (mEntries.isEmpty()) {
            loadCharacters();
        } else {
            mView.showResult(mEntries);
            mView.stopProgress();
        }
    }

    @Override
    public void loadCharacters() {
        mView.showProgress();
        mMarvelApi.listCharacters(0, new MarvelCallback<CharacterDataWrapper>() {

            @Override
            public void onResult(CharacterDataWrapper data) {
                MarvelResult<CharacterVO> result = DataParser.parse(data);
                mEntries.clear();
                mEntries.addAll(result.getEntries());
                if (!isViewAttached()) {
                    return;
                }
                mView.showResult(mEntries);
                mView.stopProgress();
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                mView.showError(e);
                mView.stopProgress();
            }
        });
    }

    @Override
    public void searchCharacters(String query) {
        mView.showProgress();
        mMarvelApi.searchCharacters(query, new MarvelCallback<CharacterDataWrapper>() {

            @Override
            public void onResult(CharacterDataWrapper data) {
                MarvelResult<CharacterVO> result = DataParser.parse(data);
                mEntries.clear();
                mEntries.addAll(result.getEntries());
                if (!isViewAttached()) {
                    return;
                }
                mView.showResult(mEntries);
                mView.stopProgress();
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                mView.showError(e);
                mView.stopProgress();
            }
        });
    }

    @Override
    public void characterClick(@NonNull View heroView, @NonNull CharacterVO character) {
        mView.openCharacter(heroView, character);
    }

}
