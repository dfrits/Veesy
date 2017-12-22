package de.veesy.introduction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by dfritsch on 22.12.2017.
 * veesy.de
 * hs-augsburg
 */

class PagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    PagerAdapter(FragmentManager fm, List<Fragment> data) {
        super(fm);
        fragments = data;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
