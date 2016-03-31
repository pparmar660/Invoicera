package com.invoicera.ViewPagerAdpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.invoicera.Fragment.InvoiceList;
import com.invoicera.ViewPagerFragment.InvoiceListPagerFragment;

import java.util.ArrayList;

public class InvoiceListPagerAdapter extends FragmentPagerAdapter {

	ArrayList<InvoiceListPagerFragment> fragmentList = new ArrayList<InvoiceListPagerFragment>();


	public InvoiceListPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragmentList.get(arg0);
	}

	public void add(InvoiceListPagerFragment frg) {
		// TODO Auto-generated method stub

		fragmentList.add(frg);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return InvoiceList.content.length;
	}

	

}
