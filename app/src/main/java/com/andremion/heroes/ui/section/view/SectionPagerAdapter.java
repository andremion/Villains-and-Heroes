package com.andremion.heroes.ui.section.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.andremion.heroes.api.data.SectionVO;

import java.util.List;

public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private int mType;
    private final List<SectionVO> mItems;
    private final SparseArray<Fragment> mFragments;

    public SectionPagerAdapter(FragmentManager fm, @SectionVO.Type int type, List<SectionVO> items) {
        super(fm);
        mType = type;
        mItems = items;
        mFragments = new SparseArray<>();
    }

    public Fragment getFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return SectionItemFragment.newInstance(mType, mItems.get(position), position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragments.remove(position);
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
