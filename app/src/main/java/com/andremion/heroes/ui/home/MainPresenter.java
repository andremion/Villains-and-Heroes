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
        if (!mView.showInfoDialog()) {
            if (mEntries.isEmpty()) {
                loadCharacters(0);
            } else {
                mView.showResult(mEntries);
                mView.showAttribution(mAttribution);
                mView.stopProgress(mHasMore);
            }
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
