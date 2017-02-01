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

package com.andremion.heroes.ui.section.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andremion.heroes.R;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.databinding.PageItemSectionBinding;
import com.andremion.heroes.ui.binding.ImageLoadingListener;
import com.andremion.heroes.ui.util.PagerSharedElementCallback;

import java.util.List;

class SectionPagerAdapter extends PagerAdapter {

    private final FragmentActivity mActivity;
    private final LayoutInflater mInflater;
    private final int mType;
    private final List<SectionVO> mItems;
    private final PagerSharedElementCallback mSharedElementCallback;
    private final String mImageTransitionName;
    private int mInitialPosition;

    SectionPagerAdapter(@NonNull FragmentActivity activity, @SectionVO.Type int type, @NonNull List<SectionVO> items, @NonNull PagerSharedElementCallback sharedElementCallback) {
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mType = type;
        mItems = items;
        mSharedElementCallback = sharedElementCallback;
        mImageTransitionName = activity.getString(R.string.transition_section_image);
    }

    void setInitialPosition(int position) {
        mInitialPosition = position;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PageItemSectionBinding binding = DataBindingUtil.inflate(mInflater, R.layout.page_item_section, container, false);
        SectionVO data = mItems.get(position);
        onViewBound(binding, position, data);
        container.addView(binding.getRoot());
        return binding;
    }

    private void onViewBound(PageItemSectionBinding binding, int position, SectionVO data) {
        String imageTransitionName = mImageTransitionName + mType + position;
        binding.setSection(data);
        binding.setImageTransition(imageTransitionName);
        binding.setImageListener(new ImageLoadingCallback(position));
    }

    private void startPostponedEnterTransition(int position) {
        if (position == mInitialPosition) {
            mActivity.supportStartPostponedEnterTransition();
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof PageItemSectionBinding) {
            mSharedElementCallback.setSharedElementViews(((PageItemSectionBinding) object).image);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof PageItemSectionBinding
                && view.equals(((PageItemSectionBinding) object).getRoot());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = ((PageItemSectionBinding) object).getRoot();
        container.removeView(view);
    }

    private class ImageLoadingCallback extends ImageLoadingListener {

        final int mPosition;

        ImageLoadingCallback(int position) {
            mPosition = position;
        }

        @Override
        public void onSuccess() {
            startPostponedEnterTransition(mPosition);
        }

        @Override
        public void onFailed(@NonNull Exception e) {
            startPostponedEnterTransition(mPosition);
        }

    }

}
