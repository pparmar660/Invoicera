package com.invoicera.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.androidapp.Global;
import com.invoicera.listener.FragmentChanger;

public class BaseFragment extends Fragment {

	public static Global global;

	protected Context context;
	public  DatabaseHelper db;
	public  FragmentChanger fragmentChanger;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		global = (Global) context.getApplicationContext();
		db=new DatabaseHelper(context);



	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragmentChanger=(FragmentChanger)getActivity();
	}
}
