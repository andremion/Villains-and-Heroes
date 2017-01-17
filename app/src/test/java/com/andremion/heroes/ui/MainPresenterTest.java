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

package com.andremion.heroes.ui;

import android.view.View;

import com.andremion.heroes.api.MarvelApi;
import com.andremion.heroes.api.MarvelCallback;
import com.andremion.heroes.api.MarvelResult;
import com.andremion.heroes.api.data.CharacterVO;
import com.andremion.heroes.api.json.CharacterDataWrapper;
import com.andremion.heroes.api.util.DataParser;
import com.andremion.heroes.ui.home.MainContract;
import com.andremion.heroes.ui.home.MainPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataParser.class)
public class MainPresenterTest {

    @Mock
    private MarvelApi mMarvelApi;

    @Mock
    private MainContract.View mView;

    @Captor
    private ArgumentCaptor<MarvelCallback<CharacterDataWrapper>> mListCharactersCallback;

    @InjectMocks
    private MainPresenter mPresenter;

    @Before
    public void setUp() {
        mockStatic(DataParser.class);
        mPresenter.attachView(mView);
    }

    @Test
    public void initScreen_ResultEntries() {

        final int OFFSET = 0;
        final int TOTAL = MarvelApi.MAX_FETCH_LIMIT;
        final List<CharacterVO> ENTRIES = Arrays.asList(mock(CharacterVO.class), mock(CharacterVO.class));
        final String ATTRIBUTION = "";
        final boolean HAS_MORE = true;

        MarvelResult<CharacterVO> result = new MarvelResult<>();
        result.setOffset(OFFSET);
        result.setTotal(TOTAL);
        result.setEntries(ENTRIES);
        result.setAttribution(ATTRIBUTION);

        CharacterDataWrapper dataWrapper = any(CharacterDataWrapper.class);
        when(DataParser.parse(dataWrapper)).thenReturn(result);

        mPresenter.initScreen();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showProgress();

        verify(mMarvelApi).listCharacters(eq(OFFSET), mListCharactersCallback.capture());
        mListCharactersCallback.getValue().onResult(dataWrapper);

        inOrder.verify(mView).showResult(ENTRIES);
        inOrder.verify(mView).showAttribution(ATTRIBUTION);
        inOrder.verify(mView).stopProgress(HAS_MORE);

    }

    @Test
    public void initScreen_ResultCached() {

        final int OFFSET = 0;
        final int TOTAL = MarvelApi.MAX_FETCH_LIMIT;
        final List<CharacterVO> ENTRIES = Arrays.asList(mock(CharacterVO.class), mock(CharacterVO.class));
        final String ATTRIBUTION = "";
        final boolean HAS_MORE = true;

        MarvelResult<CharacterVO> result = new MarvelResult<>();
        result.setOffset(OFFSET);
        result.setTotal(TOTAL);
        result.setEntries(ENTRIES);
        result.setAttribution(ATTRIBUTION);

        CharacterDataWrapper dataWrapper = any(CharacterDataWrapper.class);
        when(DataParser.parse(dataWrapper)).thenReturn(result);

        mPresenter.initScreen();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showProgress();

        verify(mMarvelApi).listCharacters(eq(OFFSET), mListCharactersCallback.capture());
        mListCharactersCallback.getValue().onResult(dataWrapper);

        inOrder.verify(mView).showResult(ENTRIES);
        inOrder.verify(mView).showAttribution(ATTRIBUTION);
        inOrder.verify(mView).stopProgress(HAS_MORE);

        mPresenter.initScreen();

        inOrder.verify(mView).showResult(ENTRIES);
        inOrder.verify(mView).showAttribution(ATTRIBUTION);
        inOrder.verify(mView).stopProgress(HAS_MORE);

    }

    @Test
    public void initScreen_ResultDetached() {

        final int OFFSET = 0;
        final int TOTAL = MarvelApi.MAX_FETCH_LIMIT;
        final List<CharacterVO> ENTRIES = Arrays.asList(mock(CharacterVO.class), mock(CharacterVO.class));
        final String ATTRIBUTION = "";

        MarvelResult<CharacterVO> result = new MarvelResult<>();
        result.setOffset(OFFSET);
        result.setTotal(TOTAL);
        result.setEntries(ENTRIES);
        result.setAttribution(ATTRIBUTION);

        CharacterDataWrapper dataWrapper = any(CharacterDataWrapper.class);
        when(DataParser.parse(dataWrapper)).thenReturn(result);

        mPresenter.initScreen();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showProgress();

        mPresenter.detachView();

        verify(mMarvelApi).listCharacters(eq(OFFSET), mListCharactersCallback.capture());
        mListCharactersCallback.getValue().onResult(dataWrapper);

    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void initScreen_Error() {

        final int OFFSET = 0;
        final boolean HAS_MORE = false;

        Throwable error = mock(Throwable.class);

        mPresenter.initScreen();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showProgress();

        verify(mMarvelApi).listCharacters(eq(OFFSET), mListCharactersCallback.capture());
        mListCharactersCallback.getValue().onError(error);

        inOrder.verify(mView).showError(error);
        inOrder.verify(mView).stopProgress(HAS_MORE);

    }

    @Test
    public void initScreen_ErrorDetached() {

        final int OFFSET = 0;

        Throwable error = mock(Throwable.class);

        mPresenter.initScreen();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showProgress();

        mPresenter.detachView();

        verify(mMarvelApi).listCharacters(eq(OFFSET), mListCharactersCallback.capture());
        mListCharactersCallback.getValue().onError(error);

    }

    @Test
    public void characterClick() {

        View heroView = mock(View.class);
        CharacterVO character = mock(CharacterVO.class);

        mPresenter.characterClick(heroView, character);

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).openCharacter(heroView, character);

    }

    @Test
    public void searchClick() {

        mPresenter.searchClick();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).openSearch();

    }

    @Test
    public void refresh() {

        mPresenter.refresh();

        final int OFFSET = 0;
        final int TOTAL = MarvelApi.MAX_FETCH_LIMIT;
        final List<CharacterVO> ENTRIES = Arrays.asList(mock(CharacterVO.class), mock(CharacterVO.class));
        final String ATTRIBUTION = "";
        final boolean HAS_MORE = true;

        MarvelResult<CharacterVO> result = new MarvelResult<>();
        result.setOffset(OFFSET);
        result.setTotal(TOTAL);
        result.setEntries(ENTRIES);
        result.setAttribution(ATTRIBUTION);

        CharacterDataWrapper dataWrapper = any(CharacterDataWrapper.class);
        when(DataParser.parse(dataWrapper)).thenReturn(result);

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showProgress();

        verify(mMarvelApi).listCharacters(eq(OFFSET), mListCharactersCallback.capture());
        mListCharactersCallback.getValue().onResult(dataWrapper);

        inOrder.verify(mView).showResult(ENTRIES);
        inOrder.verify(mView).showAttribution(ATTRIBUTION);
        inOrder.verify(mView).stopProgress(HAS_MORE);

    }

}
