package com.invoicera.androidapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.invoicera.Database.DatabaseHelper;

public class BaseActivity extends Activity{
		Global global;
	Context context;
	DatabaseHelper db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		global=(Global)this.getApplicationContext();
		context=this;
		db=new DatabaseHelper(context);


	}

	


}

