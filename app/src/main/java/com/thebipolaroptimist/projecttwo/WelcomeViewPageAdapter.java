package com.thebipolaroptimist.projecttwo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class WelcomeViewPageAdapter extends FragmentStatePagerAdapter {
    private Fragment[] mLayouts;

    public WelcomeViewPageAdapter(FragmentManager manager, Fragment[] layouts) {
        super(manager);
        mLayouts = layouts;
    }

    @Override
    public Fragment getItem(int position) {
        return mLayouts[position];
    }

    @Override
    public int getCount() {
        return mLayouts.length;
    }
}