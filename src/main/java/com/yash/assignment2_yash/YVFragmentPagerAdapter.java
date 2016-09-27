package com.yash.assignment2_yash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by yash on 16/09/16.
 */
public class YVFragmentPagerAdapter extends FragmentPagerAdapter {

    private int pagesCount;

    public YVFragmentPagerAdapter(FragmentManager fragmentManager, int pagesCount) {
        super(fragmentManager);
        this.pagesCount = pagesCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt("currentPage", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return pagesCount;
    }
}
