package com.invoicera.BackgroundServices;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Preferences;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Parvesh on 10/7/15.
 */
public class SendDataToServer extends Service implements WebApiResult {


    DatabaseHelper db;
    ContentValues values;
    String selectQuery;
    Cursor cursor;
    WebRequest sendRequest;
    Preferences pref;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (isNetworkAvailable()) {

            JSONObject CreateMainObj = new JSONObject();
            JSONArray CreateArray = new JSONArray();
            db = new DatabaseHelper(this);
            pref = new Preferences(this);
            Constant.token = pref.getString("MyToken");

            selectQuery = "Select " + DatabaseHelper.JSON_DATA
                    + " From " + DatabaseHelper.Table_CreateOfflineInvoice + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_CREATE + "'";

            cursor = db.getRecords(selectQuery);
            JSONObject object = null;
            if (cursor.moveToFirst() && cursor.getCount() > 0)
//                for (int i = 0; i < cursor.getCount(); i++) {
                do {
                    try {
                        object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                        CreateArray.put(object);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());


            // }


            if (CreateArray.length() > 0) {

                try {
                    CreateMainObj.put("createInvoice", CreateArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                sendRequest = new WebRequest(this, CreateMainObj, Constant.invoicelistURL, Constant.SERVICE_TYPE.CREATE, Constant.token, this, false);
                sendRequest.execute();

            }

            //   send estimate offline ******************************************************************************

            selectQuery = "Select " + DatabaseHelper.JSON_DATA
                    + " From " + DatabaseHelper.Table_CreateOfflineEstimate + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_CREATE + "'";

            cursor = db.getRecords(selectQuery);
            object = null;
            CreateArray = new JSONArray();
            CreateMainObj = new JSONObject();
            if (cursor.moveToFirst() && cursor.getCount() > 0)
                //    for (int i = 0; i < cursor.getCount(); i++) {

                do {
                    try {
                        object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                        CreateArray.put(object);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());


            //       }

            if (CreateArray.length() > 0) {

                try {
                    CreateMainObj.put("createEstimate", CreateArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                sendRequest = new WebRequest(this, CreateMainObj, Constant.invoicelistURL, Constant.SERVICE_TYPE.CREATE_ESTIMATE_OFFLINE, Constant.token, this, false);
                sendRequest.execute();


            }


            // send expense to server

            selectQuery = "Select " + DatabaseHelper.JSON_DATA
                    + " From " + DatabaseHelper.Table_CreateOfflineRecurring + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_CREATE + "'";


            cursor = db.getRecords(selectQuery);

            object = null;
            CreateArray = new JSONArray();
            CreateMainObj = new JSONObject();

            if (cursor.moveToFirst() && cursor.getCount() > 0)
                do {

                    try {
                        object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                        CreateArray.put(object);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } while (cursor.moveToNext());

            if (CreateArray.length() > 0) {

                try {
                    CreateMainObj.put("createRecurringBase", CreateArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                sendRequest = new WebRequest(this, CreateMainObj, Constant.invoicelistURL, Constant.SERVICE_TYPE.CREATE_RECURRING_OFFLINE, Constant.token, this, false);
                sendRequest.execute();


            }


        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {

        switch (type) {
            case CREATE:
                db.ClearTable(DatabaseHelper.Table_CreateOfflineInvoice);
                break;
            case CREATE_ESTIMATE_OFFLINE:
                db.ClearTable(DatabaseHelper.Table_CreateOfflineEstimate);
                break;
            case CREATE_RECURRING_OFFLINE:
                db.ClearTable(DatabaseHelper.Table_CreateOfflineRecurring);
                break;

        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
