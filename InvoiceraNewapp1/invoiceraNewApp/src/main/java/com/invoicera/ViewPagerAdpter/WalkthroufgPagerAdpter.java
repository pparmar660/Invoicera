package com.invoicera.ViewPagerAdpter;

import java.util.ArrayList;

import com.invoicera.ViewPagerFragment.WalkthroufgPagerFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WalkthroufgPagerAdpter extends FragmentPagerAdapter {

    public  ArrayList<WalkthroufgPagerFragment> fragmentList;

    public WalkthroufgPagerAdpter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<WalkthroufgPagerFragment>();
    }

    public void add(WalkthroufgPagerFragment fragment) {
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
        // return TestFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
