package com.andremion.heroes.ui.section;

import android.support.annotation.NonNull;

import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.ui.AbsPresenter;

import java.util.List;

public class SectionPresenter extends AbsPresenter<SectionContract.View> implements SectionContract.Actions {

    private final List<SectionVO> mEntries;
    private final int mInitialPosition;

    public SectionPresenter(@NonNull List<SectionVO> entries, int initialPosition) {
        mEntries = entries;
        mInitialPosition = initialPosition;
    }

    @Override
    public void attachView(@NonNull SectionContract.View view) {
        super.attachView(view);
        mView.showItems(mEntries, mInitialPosition);
    }

    @Override
    public void closeClick() {
        mView.close();
    }

}
