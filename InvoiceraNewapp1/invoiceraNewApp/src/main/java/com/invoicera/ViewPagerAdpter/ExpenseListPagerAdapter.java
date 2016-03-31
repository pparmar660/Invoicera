package com.invoicera.ViewPagerAdpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import com.invoicera.Fragment.ExpenseList;
import com.invoicera.Fragment.InvoiceList;
import com.invoicera.ViewPagerFragment.EstimateListPagerFragment;
import com.invoicera.ViewPagerFragment.ExpenseListPagerFragment;

import java.util.ArrayList;

/**
 * Created by Parvesh on 31/8/15.
 */
public class ExpenseListPagerAdapter  extends FragmentPagerAdapter {

    ArrayList<ExpenseListPagerFragment> fragmentList = new ArrayList<>();


    public ExpenseListPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return fragmentList.get(arg0);
    }

    public void add(ExpenseListPagerFragment frg) {
        // TODO Auto-generated method stub

        fragmentList.add(frg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ExpenseList.content.length;
    }



}
