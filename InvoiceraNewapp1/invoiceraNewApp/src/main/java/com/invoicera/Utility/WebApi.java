package com.invoicera.Utility;

import android.content.Context;

public class WebApi {

	Context mContext;

	public WebApi(Context context) {
		mContext = context;
	}

	public String getSignInApi(String username, String password) {
		return "";

		// mContext.getString(R.string.Request_SignIn) + "username=" +
		// username.trim() + "&password=" + password.trim();
	}

}
