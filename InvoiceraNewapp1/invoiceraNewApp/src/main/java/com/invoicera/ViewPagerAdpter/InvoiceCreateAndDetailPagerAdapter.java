package com.invoicera.ViewPagerAdpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class InvoiceCreateAndDetailPagerAdapter extends FragmentPagerAdapter {

	ArrayList<Fragment> list = new ArrayList<Fragment>();

	public InvoiceCreateAndDetailPagerAdapter(FragmentManager fm) {
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