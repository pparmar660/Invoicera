package com.invoicera.ViewPagerAdpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Parvesh on 30/9/15.
 */
public class RecurringCreateAndDetailPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list = new ArrayList<Fragment>();

    public RecurringCreateAndDetailPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    public void add(Fragment frg) {

        list.add(frg);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }
}
