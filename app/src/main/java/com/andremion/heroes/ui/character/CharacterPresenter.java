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

package com.andremion.heroes.ui.character;

import android.support.annotation.NonNull;
import android.view.View;

import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelCallback;
import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.api.json.SectionDataWrapper;
import com.andremion.heroes.api.util.DataParser;
import com.andremion.heroes.ui.AbsPresenter;

import java.util.ArrayList;
import java.util.List;

public class CharacterPresenter extends AbsPresenter<CharacterContract.View> implements CharacterContract.Actions {

    private final MarvelApi mMarvelApi;
    private final CharacterVO mCharacter;
    private final List<SectionVO> mComicList;
    private final List<SectionVO> mSeriesList;
    private final List<SectionVO> mStoryList;
    private final List<SectionVO> mEventList;
    private String mAttribution;

    public CharacterPresenter(@NonNull CharacterVO character) {
        mMarvelApi = MarvelApi.getInstance();
        mCharacter = character;
        mComicList = new ArrayList<>();
        mSeriesList = new ArrayList<>();
        mStoryList = new ArrayList<>();
        mEventList = new ArrayList<>();
    }

    @Override
    public void attachView(@NonNull CharacterContract.View view) {
        super.attachView(view);
        mView.showAttribution(mAttribution);
        mView.showCharacter(mCharacter);
        if (mComicList.isEmpty()) {
            mView.showComics(mCharacter.getComics());
            loadComics(mCharacter.getId(), 0);
        } else {
            mView.showComics(mComicList);
        }
        if (mSeriesList.isEmpty()) {
            mView.showSeries(mCharacter.getSeries());
            loadSeries(mCharacter.getId(), 0);
        } else {
            mView.showSeries(mSeriesList);
        }
        if (mStoryList.isEmpty()) {
            mView.showStories(mCharacter.getStories());
            loadStories(mCharacter.getId(), 0);
        } else {
            mView.showStories(mStoryList);
        }
        if (mEventList.isEmpty()) {
            mView.showEvents(mCharacter.getEvents());
            loadEvents(mCharacter.getId(), 0);
        } else {
            mView.showEvents(mEventList);
        }
    }

    @Override
    public void loadComics(long characterId, int offset) {
        mMarvelApi.listComics(characterId, offset, new MarvelCallback<SectionDataWrapper>() {

            @Override
            public void onResult(SectionDataWrapper data) {
                MarvelResult<SectionVO> result = DataParser.parse(data);
                mComicList.addAll(result.getEntries());
                mAttribution = result.getAttribution();
                if (!isViewAttached()) {
                    return;
                }
                mView.showComics(mComicList);
                mView.showAttribution(mAttribution);
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                mView.showError(e);
            }
        });
    }

    @Override
    public void loadSeries(long characterId, int offset) {
        mMarvelApi.listSeries(characterId, offset, new MarvelCallback<SectionDataWrapper>() {

            @Override
            public void onResult(SectionDataWrapper data) {
                MarvelResult<SectionVO> result = DataParser.parse(data);
                mSeriesList.addAll(result.getEntries());
                mAttribution = result.getAttribution();
                if (!isViewAttached()) {
                    return;
                }
                mView.showSeries(mSeriesList);
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                mView.showError(e);
            }
        });
    }

    @Override
    public void loadStories(long characterId, int offset) {
        mMarvelApi.listStories(characterId, offset, new MarvelCallback<SectionDataWrapper>() {

            @Override
            public void onResult(SectionDataWrapper data) {
                MarvelResult<SectionVO> result = DataParser.parse(data);
                mStoryList.addAll(result.getEntries());
                mAttribution = result.getAttribution();
                if (!isViewAttached()) {
                    return;
                }
                mView.showStories(mStoryList);
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                mView.showError(e);
            }
        });
    }

    @Override
    public void loadEvents(long characterId, int offset) {
        mMarvelApi.listEvents(characterId, offset, new MarvelCallback<SectionDataWrapper>() {

            @Override
            public void onResult(SectionDataWrapper data) {
                MarvelResult<SectionVO> result = DataParser.parse(data);
                mEventList.addAll(result.getEntries());
                mAttribution = result.getAttribution();
                if (!isViewAttached()) {
                    return;
                }
                mView.showEvents(mEventList);
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                mView.showError(e);
            }
        });
    }

    @Override
    public void sectionClick(@SectionVO.Type int type, @NonNull View heroView, @NonNull List<SectionVO> entries, int position) {
        mView.openSection(type, heroView, mAttribution, entries, position);
    }

    @Override
    public void linkClick(@NonNull String url) {
        mView.openLink(url);
    }

    @Override
    public void searchClick() {
        mView.openSearch();
    }

}
