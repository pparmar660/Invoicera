package com.invoicera.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Preferences;

public class Splash extends BaseActivity {
    Preferences pref;
    public boolean isLogin=false;
    String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        context = this;
        pref = new Preferences(getApplicationContext());
        // move to another
        /*Intent intent = new Intent(context, Home.class);
        startActivity(intent);
		finish();*/

        // -----------------------
        // get screen size

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        global.screenHeight = displaymetrics.heightPixels;
        global.screenWidth = displaymetrics.widthPixels;
        isLogin=pref.getBoolean(Constant.KEY_USER_LOGED);
        token=pref.getString("MyToken");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Intent intent;
                //Log.i("token",pref.getString("token"));




                    if (isLogin) {
                        intent = new Intent(context, Home.class);
                        Constant.token =token;
                        startActivity(intent);
                        finish();
                        return;
                    }


                intent = new Intent(context, Login.class);
                startActivity(intent);
                finish();

            }
        };
        handler.postDelayed(r, 3000);

    }

}
