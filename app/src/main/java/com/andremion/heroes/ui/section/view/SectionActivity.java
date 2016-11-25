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

    private final SharedElementCallback mSharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (!mSharedElementCallbackEnabled) {
                return;
            }
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
    private boolean mSharedElementCallbackEnabled = false;

    public static void start(Activity activity, View heroView, String attribution, List<SectionVO> entries, int position) {

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,
                        heroView, ViewCompat.getTransitionName(heroView));
        Intent intent = new Intent(activity, SectionActivity.class);
        intent.putExtra(EXTRA_ATTRIBUTION, attribution);
        intent.putExtra(EXTRA_ENTRIES, (Serializable) entries);
        intent.putExtra(EXTRA_POSITION, position);

        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static int getPosition(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.hasExtra(EXTRA_POSITION)) {
            return data.getIntExtra(EXTRA_POSITION, 0);
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_section);
        mViewPager = (ViewPager) findViewById(R.id.pager);

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

        supportPostponeEnterTransition();
        setEnterSharedElementCallback(mSharedElementCallback);
    }

    @Override
    public void onBackPressed() {
        setResultAndFinish();
    }

    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(EXTRA_POSITION, mViewPager.getCurrentItem());
        setResult(RESULT_OK, data);
        mSharedElementCallbackEnabled = true;
        supportFinishAfterTransition();
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
        mViewPager.setAdapter(mAdapter = new SectionPagerAdapter(getSupportFragmentManager(), entries));
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void close() {
        supportFinishAfterTransition();
    }
}
