package com.andremion.heroes.ui.section.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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


public class SectionActivity extends AppCompatActivity implements SectionContract.View {

    private static final String EXTRA_ATTRIBUTION = SectionActivity.class.getPackage().getName() + ".extra.ATTRIBUTION";
    private static final String EXTRA_ENTRIES = SectionActivity.class.getPackage().getName() + ".extra.ENTRIES";
    private static final String EXTRA_POSITION = SectionActivity.class.getPackage().getName() + ".extra.POSITION";

    private ViewPager mViewPager;
    private SectionPresenter mPresenter;

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
        mViewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager(), entries));
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void close() {
        supportFinishAfterTransition();
    }
}
