package com.invoicera.GlobalData;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

	public  SharedPreferences sharedPref;
	Editor edit;
	public static String MyPREFERENCES = "MyPrefs";
	

	public Preferences(Context con) {
		sharedPref = con.getSharedPreferences(MyPREFERENCES, 0);
		edit = sharedPref.edit();
	}

	public void setString(String key,String value) {
		edit.putString(key, value);
		edit.commit();
	}	

	public String getString(String key) {
		return sharedPref.getString(key, null);
	}

	public void setBoolean(String key,Boolean value) {
		edit.putBoolean(key, value);
		edit.commit();
	}	

	public Boolean getBoolean(String key) {
		return sharedPref.getBoolean(key, false);
	}

	public void setclear() {
		edit.clear();
		edit.commit();
	}





}
