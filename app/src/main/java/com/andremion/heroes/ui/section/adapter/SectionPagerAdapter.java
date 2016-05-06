package com.andremion.heroes.ui.section.adapter;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.andremion.heroes.data.DataContract;
import com.andremion.heroes.ui.section.SectionItemFragment;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private final String mType;
    private final Cursor mData;

    public SectionPagerAdapter(FragmentManager fm, String type, Cursor data) {
        super(fm);
        mType = type;
        mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        mData.moveToPosition(position);
        return SectionItemFragment.newInstance(mType, position,
                mData.getLong(mData.getColumnIndex(DataContract.Section.COLUMN_DATA_ID)));
    }

    @Override
    public int getCount() {
        return mData.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        mData.moveToPosition(position);
        return mData.getString(mData.getColumnIndex(DataContract.Section.COLUMN_NAME));
    }
}
