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

package com.andremion.heroes.ui.section.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.andremion.heroes.R;
import com.andremion.heroes.api.data.SectionVO;
import com.andremion.heroes.databinding.ActivitySectionBinding;
import com.andremion.heroes.ui.section.SectionContract;
import com.andremion.heroes.ui.section.SectionPresenter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class SectionActivity extends AppCompatActivity implements SectionContract.View {

    private static final String EXTRA_ATTRIBUTION = SectionActivity.class.getPackage().getName() + ".extra.ATTRIBUTION";
    private static final String EXTRA_ENTRIES = SectionActivity.class.getPackage().getName() + ".extra.ENTRIES";
    private static final String EXTRA_POSITION = SectionActivity.class.getPackage().getName() + ".extra.POSITION";
    private static final String EXTRA_TYPE = SectionActivity.class.getPackage().getName() + ".extra.TYPE";
    public static final int EXTRA_NOT_FOUND = -1;

    private final SharedElementCallback mSharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            View sharedElement = findSharedElement();
            if (sharedElement != null) {
                names.clear();
                names.add(ViewCompat.getTransitionName(sharedElement));
                sharedElements.clear();
                sharedElements.put(ViewCompat.getTransitionName(sharedElement), sharedElement);
            }
        }

        private View findSharedElement() {
            if (mAdapter == null) {
                return null;
            }
            Fragment fragment = mAdapter.getFragment(mViewPager.getCurrentItem());
            if (fragment == null) {
                return null;
            }
            View view = fragment.getView();
            if (view == null) {
                return null;
            }
            return view.findViewById(R.id.image);
        }
    };

    private ViewPager mViewPager;
    private SectionPagerAdapter mAdapter;
    private SectionPresenter mPresenter;
    private int mType;

    public static void start(Activity activity, @SectionVO.Type int type, View heroView, String attribution, List<SectionVO> entries, int position) {

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,
                        heroView, ViewCompat.getTransitionName(heroView));
        Intent intent = new Intent(activity, SectionActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_ATTRIBUTION, attribution);
        intent.putExtra(EXTRA_ENTRIES, (Serializable) entries);
        intent.putExtra(EXTRA_POSITION, position);

        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static int getType(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.hasExtra(EXTRA_TYPE)) {
            return data.getIntExtra(EXTRA_TYPE, EXTRA_NOT_FOUND);
        }
        return EXTRA_NOT_FOUND;
    }

    public static int getPosition(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.hasExtra(EXTRA_POSITION)) {
            return data.getIntExtra(EXTRA_POSITION, EXTRA_NOT_FOUND);
        }
        return EXTRA_NOT_FOUND;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_section);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        mType = getIntent().getExtras().getInt(EXTRA_TYPE);
        String attribution = getIntent().getStringExtra(EXTRA_ATTRIBUTION);
        //noinspection unchecked
        List<SectionVO> entries = (List<SectionVO>) getIntent().getExtras().get(EXTRA_ENTRIES);
        assert entries != null;
        int position = getIntent().getExtras().getInt(EXTRA_POSITION);

        if (savedInstanceState == null) {
            mPresenter = new SectionPresenter(entries, position);
        } else {
            mPresenter = (SectionPresenter) getLastCustomNonConfigurationInstance();
        }
        mPresenter.attachView(this);

        binding.setAttribution(attribution);
        binding.setPresenter(mPresenter);

        // Postpone transition until section thumbnail is loaded by image loader.
        supportPostponeEnterTransition();
    }

    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showItems(List<SectionVO> entries, int position) {
        mViewPager.setAdapter(mAdapter = new SectionPagerAdapter(getSupportFragmentManager(), mType, entries));
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void close() {
        setResultAndFinish();
    }

    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(EXTRA_TYPE, mType);
        data.putExtra(EXTRA_POSITION, mViewPager.getCurrentItem());
        setResult(RESULT_OK, data);
        setEnterSharedElementCallback(mSharedElementCallback);
        supportFinishAfterTransition();
    }
}
