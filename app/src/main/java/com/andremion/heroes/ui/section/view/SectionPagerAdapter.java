package com.andremion.heroes.ui.section.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.andremion.heroes.api.data.SectionVO;

import java.util.List;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private final List<SectionVO> mItems;

    public SectionPagerAdapter(FragmentManager fm, List<SectionVO> items) {
        super(fm);
        mItems = items;
    }

    @Override
    public Fragment getItem(int position) {
        return SectionItemFragment.newInstance(mItems.get(position), position);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).getTitle();
    }
}
