package com.invoicera.ViewPagerAdpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GraphPagerAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

	String content[] = { "INVOICES", "ESTIMATES", "EXPENSES" };

	public static int centerX = 100, centerY = 100, radiusOfOuter = 15,
			radiusOfInner = 10, outerCircleStork = 10, innerCircleStork = 20,
			innerCircleInterval = 10, innerCirclePhase = 2,
			radiusOfCircleOnArc = 10, arcTextSize = 24, centerTextSize = 50,
			hintCircleRadius = 8, hintCircleTextSize = 10, gabBetweenHint = 10;

	public static float gapAngle = 10;

	public GraphPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);

	}

	public void add(Fragment fragment) {


		fragmentList.add(fragment);
 		notifyDataSetChanged();
	}

    public void clear(){
        fragmentList.clear();
        notifyDataSetChanged();
    }

	// Returns total number of pages
	@Override
	public int getCount() {
		return fragmentList.size();

	}

	// Returns the fragment to display for that page
	@Override
	public Fragment getItem(int position) {
		// switch (position) {

		return fragmentList.get(position);

	}

	// Returns the page title for the top indicator
	@Override
	public CharSequence getPageTitle(int position) {
		return content[position];
	}


	@Override
	public int getItemPosition(Object object) {

		return POSITION_NONE;

/*		Fragment fragment = (Fragment)object;

		String title = fragment.get;
		int position = titles.indexOf(title);

		if (position >= 0) {
			return position;
		} else {
			return POSITION_NONE;
		}*/
	}
}
