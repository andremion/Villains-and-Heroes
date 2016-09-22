package com.andremion.heroes.ui.section;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.andremion.heroes.R;
import com.andremion.heroes.data.DataContract.Section;
import com.andremion.heroes.databinding.ActivitySectionBinding;
import com.andremion.heroes.sync.service.SyncAdapter;
import com.andremion.heroes.ui.section.adapter.SectionPagerAdapter;


public class SectionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_TYPE = SectionActivity.class.getPackage().getName() + ".extra.TYPE";
    public static final String EXTRA_CHARACTER = SectionActivity.class.getPackage().getName() + ".extra.CHARACTER";
    public static final String EXTRA_POSITION = SectionActivity.class.getPackage().getName() + ".extra.POSITION";

    private SharedPreferences mPrefs;

    private String mType;
    private long mCharacter;
    private int mPosition;
    private ViewPager mViewPager;
    private ActivitySectionBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_section);
        mBinding.setPresenter(this);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mType = getIntent().getExtras().getString(EXTRA_TYPE);
        mCharacter = getIntent().getExtras().getLong(EXTRA_CHARACTER);
        mPosition = getIntent().getExtras().getInt(EXTRA_POSITION);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Section.CONTENT_URI,
                null,
                Section.COLUMN_TYPE + "=? AND " + Section.COLUMN_CHARACTER + "=?",
                new String[]{mType, String.valueOf(mCharacter)},
                Section.COLUMN_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mViewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager(), mType, data));
        mViewPager.setCurrentItem(mPosition, false);
        mBinding.setAttribution(mPrefs.getString(SyncAdapter.KEY_ATTRIBUTION, null));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void onCloseClick(){
        supportFinishAfterTransition();
    }

}
