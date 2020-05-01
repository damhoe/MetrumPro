package com.stho.metrum;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

/*
  see:
    http://codetheory.in/android-swipe-views-with-tabs/
    https://www.bignerdranch.com/blog/viewpager-without-fragments/
 */

public class MetrumPagerAdapter extends FragmentPagerAdapter {

    MetrumPagerAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new RotaryFragment();

            case 1:
                return new ClickFragment();

            case 2:
                return new BeatsFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}


