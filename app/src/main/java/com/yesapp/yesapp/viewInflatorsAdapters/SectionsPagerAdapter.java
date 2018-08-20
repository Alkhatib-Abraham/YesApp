package com.yesapp.yesapp.viewInflatorsAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yesapp.yesapp.fragments.Fragment1;
import com.yesapp.yesapp.fragments.Fragment2;
import com.yesapp.yesapp.fragments.Fragment3;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment1 fragment1 = new Fragment1();
                return fragment1;
            case 1:
                Fragment2 fragment2 = new Fragment2();
                return fragment2;
            case 2:
                Fragment3 fragment3 = new Fragment3();
                return fragment3;

            default:

                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }


    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "FEED";
            case 1:
                return "REQUESTS";
            case 2:
                return "FRIENDS";

            default:

                return null;


        }

    }
}