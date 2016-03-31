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
import android.util.Log;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Preferences;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Parvesh on 31/7/15.
 */
public class GetDataFromServerOnLogin extends Service implements WebApiResult {


    DatabaseHelper db;
    ContentValues values;
    String selectQuery;
    Cursor cursor;
    WebRequest getData;
    Preferences pref;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DatabaseHelper(this);
        pref = new Preferences(this);
        values = new ContentValues();
        Constant.token = pref.getString("MyToken");

        if (isNetworkAvailable()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("method", "listCreateInvoiceFieldsData");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Constant.token != null) {
                getData = new WebRequest(this, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, false);
                getData.execute();
            }


        }


        return Service.START_STICKY;
    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {

        switch (type) {
            case GET_DATA:


                try {
                    if (result == null)
                        return;
                    if (!result.getString("code").equalsIgnoreCase("200"))
                        return;

                    JSONObject clientObject = result.getJSONObject("clients");
                    JSONObject taxesObject = result.getJSONObject("taxes");
                    JSONArray currencyArray = result.getJSONArray("Currency");

                    //---------------------------------------------------------


                    JSONObject lateFeeObject = result.getJSONObject("late_fees");
                    JSONArray PaymentGateWayArray = result.getJSONArray("PaymentGateway");
                    JSONObject productsObject = result.getJSONObject("products");
                    JSONObject servicesObject = result.getJSONObject("services");
                    JSONObject additionalChargesObject = result.getJSONObject("additional_charges");


                    // insert these value in database
                    JSONObject mainObject = new JSONObject();


                    // add client

                    mainObject = new JSONObject();
                    mainObject.put("code", "200");
                    mainObject.put("clients", clientObject);
                    db.ClearTable(DatabaseHelper.TAble_ClientList);
                    values = new ContentValues();
                    values.put(DatabaseHelper.JSON_DATA, mainObject.toString());
                    db.insert(DatabaseHelper.TAble_ClientList, values);


                    // add tax value
                    mainObject = new JSONObject();
                    mainObject.put("code", "200");
                    mainObject.put("taxes", taxesObject);
                    db.ClearTable(DatabaseHelper.Table_TaxList);
                    values = new ContentValues();
                    values.put(DatabaseHelper.JSON_DATA, mainObject.toString());
                    db.insert(DatabaseHelper.Table_TaxList, values);

                    // add currency list

                    mainObject = new JSONObject();
                    mainObject.put("code", "200");
                    mainObject.put("Currency", currencyArray);
                    db.ClearTable(DatabaseHelper.Table_CurrencyList);
                    values = new ContentValues();
                    values.put(DatabaseHelper.JSON_DATA, mainObject.toString());
                    db.insert(DatabaseHelper.Table_CurrencyList, values);

                    // add late fee
                    mainObject = new JSONObject();
                    mainObject.put("code", "200");
                    mainObject.put("late_fees", lateFeeObject);
                    db.ClearTable(DatabaseHelper.Table_LateFee);
                    values = new ContentValues();
                    values.put(DatabaseHelper.JSON_DATA, mainObject.toString());
                    db.insert(DatabaseHelper.Table_LateFee, values);
                    //

                    //add payment Gateway
                    mainObject = new JSONObject();
                    mainObject.put("code", "200");
                    mainObject.put("PaymentGateway", PaymentGateWayArray);
                    db.ClearTable(DatabaseHelper.Table_PaymentGateways);
                    values = new ContentValues();
                    values.put(DatabaseHelper.JSON_DATA, mainObject.toString());
                    db.insert(DatabaseHelper.Table_PaymentGateways, values);

                    //

                    //add product and services object
                    mainObject = new JSONObject();
                    mainObject.put("code", "200");
                    mainObject.put("products", productsObject);
                    mainObject.put("services", servicesObject);
                    db.ClearTable(DatabaseHelper.Table_ProductAndServices);
                    values = new ContentValues();
                    values.put(DatabaseHelper.JSON_DATA, mainObject.toString());

                    Log.e("RESult:",mainObject.toString());
                    db.insert(DatabaseHelper.Table_ProductAndServices, values);
                    //

                    //add additional charges
                    mainObject = new JSONObject();
                    mainObject.put("code", "200");
                    mainObject.put("additional_charges", additionalChargesObject);

                    db.ClearTable(DatabaseHelper.Table_AdditionalCharges);
                    values = new ContentValues();
                    values.put(DatabaseHelper.JSON_DATA, mainObject.toString());
                    db.insert(DatabaseHelper.Table_AdditionalCharges, values);

                    //--------------------


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

