package com.invoicera.androidapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.CustomView.CustomPopup;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.SERVICE_TYPE;
import com.invoicera.InterFace.PopUpResult;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Utility.Utils;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.model.ItemModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateItem extends BaseActivity implements WebApiResult,
        OnClickListener, PopUpResult, RadioGroup.OnCheckedChangeListener {


    ItemModel itemModel;
    EditText quantityEt, unitCostEt, descriptionEt, discountEt, convertRateEditText;
    RadioGroup discountRadioGroup;
    ImageView save;
    DatabaseHelper db;
    CustomPopup myPopUp;
    double unitCostBasePrice = 0d;
    RadioGroup productAndServicesRadioGroup;
    TextView selectedItemTV, currencyConversionTV, totalValueTV, tax1TV, tax2TV;
    boolean isProductSelected = true;
    ArrayList<HashMap<String, String>> productList, servicesList, taxList;
    DownloadData currencyConversionRequest;
    HashMap<String, String> map;
    boolean isShowProgressBar = false;
    ContentValues values;
    String selectQuery, client_currency, itemCurrency, TaxId1 = "", TaxId2 = "", tax2Type = "", tax1Name = "", tax2Name = "";
    Cursor cursor;
    JSONObject obj;
    WebRequest request;
    ProgressBar CurrencyProgressBar;
    LinearLayout currencyConversionLiner;
    int requestCode;
    double quantity = 0;
    double unitCost = 0.0d, discountAmount = 0.0d, tax1Amount = 0.0d, tax2Amount = 0.0d, tax1value = 0d, tax2value = 0d;
    boolean isDiscountFlat = true;
    ImageView back;
    LinearLayout detilLinear;


    boolean isItemSelected = false;
    boolean isEditing = false;

    RadioButton productRadioButton, servicesRadioButton, flatDiscountRadioButton, percentDiscountRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);
        Intent intent = getIntent();

        if (intent.getStringExtra(Constant.KEY_CLIENT_CURRENCY) != null) {
            client_currency = intent.getStringExtra(Constant.KEY_CLIENT_CURRENCY);
            requestCode = intent.getIntExtra(Constant.KEY_REQUEST, 0);
        }

        isEditing = false;
        if (intent.getParcelableExtra(Constant.KEY_ITEM) != null) {
            itemModel = (ItemModel) getIntent().getParcelableExtra(Constant.KEY_ITEM);
            isEditing = true;
        }


        save = (ImageView) findViewById(R.id.save);
        save.setOnClickListener(this);
        productList = new ArrayList<>();
        discountRadioGroup = (RadioGroup) findViewById(R.id.discountGroup);
        currencyConversionTV = (TextView) findViewById(R.id.currencyConversion);
        CurrencyProgressBar = (ProgressBar) findViewById(R.id.progressCurrency);
        totalValueTV = (TextView) findViewById(R.id.totalvalue);
        productRadioButton = (RadioButton) findViewById(R.id.productButton);
        servicesRadioButton = (RadioButton) findViewById(R.id.serviceButton);

        flatDiscountRadioButton = (RadioButton) findViewById(R.id.flat);
        percentDiscountRadioButton = (RadioButton) findViewById(R.id.percent);


        db = new DatabaseHelper(context);
        quantityEt = (EditText) findViewById(R.id.quantity);
        unitCostEt = (EditText) findViewById(R.id.unitCost);
        descriptionEt = (EditText) findViewById(R.id.description);
        discountEt = (EditText) findViewById(R.id.dicountValue);
        tax1TV = (TextView) findViewById(R.id.tax1);
        tax2TV = (TextView) findViewById(R.id.tax2);
        currencyConversionLiner = (LinearLayout) findViewById(R.id.currecyConversionLiner);
        currencyConversionLiner.setVisibility(View.GONE);
        detilLinear = (LinearLayout) findViewById(R.id.detailLinear);
        productAndServicesRadioGroup = (RadioGroup) findViewById(R.id.itemType);
        selectedItemTV = (TextView) findViewById(R.id.selectItem);
        convertRateEditText = (EditText) findViewById(R.id.convertRateEt);
        productAndServicesRadioGroup.setOnCheckedChangeListener(this);
        tax1TV.setOnClickListener(this);
        tax2TV.setOnClickListener(this);
        selectedItemTV.setOnClickListener(this);
        discountRadioGroup.setOnCheckedChangeListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        detilLinear.setVisibility(View.GONE);

        convertRateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double theResultF = 1.0d;
                Double unitec = 1.0d;
                try {
                    unitec = unitCostBasePrice;

                    if (convertRateEditText.getText().toString().isEmpty())
                        theResultF = 1.0d;
                    else
                        theResultF = Double.parseDouble(convertRateEditText.getText().toString());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                unitCostEt.setText(Utils.FloatToStringLimits(unitec * theResultF).replace(",", ""));
                setTotal();
            }
        });

        quantityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                setTotal();
            }
        });


        unitCostEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTotal();
            }
        });

        discountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (!isDiscountFlat) {

                    try {
                        if (!discountEt.getText().toString().isEmpty())

                            if (Double.parseDouble(discountEt.getText().toString()) > 100) {
                                global.showAlert("Discount % should not be greater than 100", context);
                                discountEt.setText("0.0");

                            }


                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {

                    double total = unitCost * quantity;


                    try {
                        //total = Double.parseDouble(totalValueTV.getText().toString());
                        if (!discountEt.getText().toString().isEmpty())
                            if (total - Double.parseDouble(discountEt.getText().toString()) < 0) {
                                global.showAlert("Discount  should not be greater than total", context);
                                discountEt.setText("0.0");
                            }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }


                setTotal();


            }
        });

        if (isEditing)
            setDataOfEditingItem();
    }


    @Override
    public void getWebResult(SERVICE_TYPE type, JSONObject result) {
        if (result == null)
            return;
        try {
            if (!result.getString("code").equalsIgnoreCase("200")) {
                if (result.has("message"))
                    Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_SHORT).show();
                return;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean updatePopUp = false;
        switch (type) {

            case GET_PRODUCT_LIST:
                isProductSelected = true;

                db.ClearTable(DatabaseHelper.Table_ProductAndServices);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_ProductAndServices, values);
                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_ProductAndServices;

                cursor = db.getRecords(selectQuery);


                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    productList = new ArrayList<>();
                    servicesList = new
                            ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("products").getJSONArray("product").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("products").getJSONArray("product").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("product_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("product_name"));
                                map.put(Constant.KEY_UNITCOST, chargeObj.getString("unit_cost"));
                                map.put(Constant.KEY_QUANTITY, chargeObj.getString("quantity"));
                                map.put(Constant.KEY_DESCRIPTION, chargeObj.getString("description"));
                                map.put(Constant.KEY_DATE, chargeObj.getString("date"));
                                map.put(Constant.KEY_STATUS, chargeObj.getString("status"));
                                map.put(Constant.KEY_PRODUCT_TAX_VALUE1, chargeObj.getString("product_tax_value1"));
                                map.put(Constant.KEY_PRODUCT_TAX_VALUE2, chargeObj.getString("product_tax_value2"));
                                map.put(Constant.KEY_PRODUCT_CURRENCY_ID, chargeObj.getString("product_currency_id"));
                                map.put(Constant.KEY_PRODUCT_CURRENCY_NAME, chargeObj.getString("product_currency_name"));
                                map.put(Constant.KEY_PRODUCT_TAX_ONE_NAME, chargeObj.getString("product_tax_one_name"));
                                map.put(Constant.KEY_PRODUCT_TAX_TWO_NAME, chargeObj.getString("product_tax_two_name"));
                                map.put(Constant.KEY_INVENTORY_CURRENT_STOCK, chargeObj.getString("inventory_current_stock"));
                                map.put(Constant.KEY_TRACK_INVENTORY, chargeObj.getString("track_inventory"));
                                map.put(Constant.KEY_PRODUCT_TAXID1, chargeObj.getString("product_taxID1"));
                                map.put(Constant.KEY_PRODUCT_TAXID2, chargeObj.getString("product_taxID2"));
                                map.put(Constant.KEY_CREATED_BY, chargeObj.getString("created_by"));


                                productList.add(map);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, productList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, productList, Constant.POP_UP.PRODUCT_LIST, false, this, "Select Product");
                            myPopUp.show();

                        }


                    }
                }
                break;


            case GET_SERVICE_LIST:


                db.ClearTable(DatabaseHelper.Table_ProductAndServices);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_ProductAndServices, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_ProductAndServices;

                cursor = db.getRecords(selectQuery);

                isShowProgressBar = false;
                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    servicesList = new ArrayList<>();

                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {


                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("services").getJSONArray("service").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("services").getJSONArray("service").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("service_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("service_name"));
                                map.put(Constant.KEY_UNITCOST, chargeObj.getString("unit_cost"));
                                map.put(Constant.KEY_QUANTITY, chargeObj.getString("quantity"));
                                map.put(Constant.KEY_DESCRIPTION, chargeObj.getString("description"));
                                map.put(Constant.KEY_DATE, chargeObj.getString("date"));

                                map.put(Constant.KEY_STATUS, chargeObj.getString("status"));
                                map.put(Constant.KEY_SERVICE_TAX_VALUE1, chargeObj.getString("service_tax_value1"));
                                map.put(Constant.KEY_SERVICE_TAX_VALUE2, chargeObj.getString("service_tax_value2"));
                                map.put(Constant.KEY_SERVICE_CURRENCY_ID, chargeObj.getString("service_currency_id"));
                                map.put(Constant.KEY_SERVICE_CURRENCY_NAME, chargeObj.getString("service_currency_name"));
                                map.put(Constant.KEY_SERVICE_TAX_ONE_NAME, chargeObj.getString("service_tax_one_name"));

                                map.put(Constant.KEY_SERVICE_TAX_TWO_NAME, chargeObj.getString("service_tax_two_name"));
                                map.put(Constant.KEY_INVENTORY_CURRENT_STOCK, chargeObj.getString("inventory_current_stock"));
                                map.put(Constant.KEY_TRACK_INVENTORY, chargeObj.getString("track_inventory"));
                                map.put(Constant.KEY_SERVICE_TAXID1, chargeObj.getString("service_taxID1"));
                                map.put(Constant.KEY_SERVICE_TAXID2, chargeObj.getString("service_taxID2"));
                                map.put(Constant.KEY_CREATED_BY, chargeObj.getString("created_by"));


                                servicesList.add(map);
                            }


                            if (myPopUp != null)
                                if (myPopUp.isShowing())
                                    updatePopUp = true;


                            if (updatePopUp)
                                myPopUp.updateList(false, servicesList);
                            else {

                                myPopUp = new CustomPopup((Activity) context, servicesList, Constant.POP_UP.SERVICE_LIST, false, this, "Select Service");
                                myPopUp.show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }


                break;
            case GET_TAX_LIST1:

                db.ClearTable(DatabaseHelper.Table_TaxList);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_TaxList, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_TaxList;

                cursor = db.getRecords(selectQuery);

                Log.e("cusor length ", cursor.getCount() + "");

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    taxList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            HashMap<String, String> map;

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("taxes").getJSONArray("tax").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(k);

                                if (chargeObj.getString("type").equalsIgnoreCase("Normal")) {
                                    map.put(Constant.KEY_ID, chargeObj.getString("id"));
                                    map.put(Constant.KEY_NAME, chargeObj.getString("name"));
                                    map.put(Constant.KEY_TYPE, chargeObj.getString("type"));
                                    map.put(Constant.KEY_VALUE, chargeObj.getString("value"));
                                    taxList.add(map);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, taxList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, taxList, Constant.POP_UP.TAX_LIST1, false, this, "Select Tax");
                            myPopUp.show();

                        }
                    }
                }

                break;


            case GET_TAX_LIST2:

                db.ClearTable(DatabaseHelper.Table_TaxList);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_TaxList, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_TaxList;

                cursor = db.getRecords(selectQuery);

                Log.e("cusor length ", cursor.getCount() + "");

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    taxList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            HashMap<String, String> map;

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("taxes").getJSONArray("tax").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("id"));

                                map.put(Constant.KEY_TYPE, chargeObj.getString("type"));
                                map.put(Constant.KEY_VALUE, chargeObj.getString("value"));
                                if (chargeObj.getString("type").equalsIgnoreCase("Compound")) {
                                    map.put(Constant.KEY_NAME, "#" + chargeObj.getString("name"));

                                } else {
                                    map.put(Constant.KEY_NAME, chargeObj.getString("name"));
                                }
                                taxList.add(map);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, taxList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, taxList, Constant.POP_UP.TAX_LIST2, false, this, "Select Tax");
                            myPopUp.show();

                        }
                    }
                }
                break;
        }
    }

    @Override
    public void
    onClick(View v) {
        // TODO Auto-generated method stub
        isShowProgressBar = false;
        switch (v.getId()) {
            case R.id.save:
                if (!isItemSelected) {
                    finish();
                    return;

                }

                if ((quantity * unitCost) - discountAmount < 0) {
                    if (!discountEt.getText().toString().isEmpty())
                        if (Double.parseDouble(discountEt.getText().toString()) > 0) {
                            discountEt.setText("0");
                            setTotal();

                        }
                    global.showAlert("Total amount should not be less than zero", context);
                    return;

                }

                itemModel = new ItemModel();

                itemModel.setQuantity(quantity + "");
                itemModel.setUnit_cost(unitCost + "");
                itemModel.setDescription(descriptionEt.getText().toString());
                itemModel.setDiscountAmount(discountAmount + "");
                itemModel.setName(selectedItemTV.getText().toString());
                itemModel.setTax1_name(tax1Name);
                itemModel.setTax2_name(tax2Name);
                // itemModel.setTotalTAx((tax1Amount + tax2Amount) + "");
                itemModel.setTax1Type("Normal");
                itemModel.setTax2Type(tax2Type);
                if (isDiscountFlat)
                    itemModel.setDiscount_type("Fixed");
                else itemModel.setDiscount_type("Percent");
                itemModel.setTotalValue(((quantity * unitCost) - discountAmount) + "");

                if (isProductSelected)
                    itemModel.setType("Product");
                else
                    itemModel.setType("Service");
                itemModel.setTax1Id(TaxId1);
                itemModel.setTax2Id(TaxId2);
                itemModel.setTax1_percent(tax1value + "");
                itemModel.setTax2_percent(tax2value + "");

                String value = discountEt.getText().toString();
                itemModel.setDiscountValue(value);

                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_ITEM, itemModel);
                intent.putExtra(Constant.KEY_EDITING, isEditing);
                setResult(requestCode, intent);
                finish();


                break;

            case R.id.selectItem:

                currencyConversionLiner.setVisibility(View.GONE);
                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_ProductAndServices;

                cursor = db.getRecords(selectQuery);

                Log.e("cusor length ", cursor.getCount() + "");

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    productList = new ArrayList<>();
                    servicesList = new ArrayList<>();

                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            HashMap<String, String> map;

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("products").getJSONArray("product").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("products").getJSONArray("product").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("product_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("product_name"));
                                map.put(Constant.KEY_UNITCOST, chargeObj.getString("unit_cost"));
                                map.put(Constant.KEY_QUANTITY, chargeObj.getString("quantity"));
                                map.put(Constant.KEY_DESCRIPTION, chargeObj.getString("description"));
                                map.put(Constant.KEY_DATE, chargeObj.getString("date"));
                                map.put(Constant.KEY_STATUS, chargeObj.getString("status"));
                                map.put(Constant.KEY_PRODUCT_TAX_VALUE1, chargeObj.getString("product_tax_value1"));
                                map.put(Constant.KEY_PRODUCT_TAX_VALUE2, chargeObj.getString("product_tax_value2"));

                                //    map.put(Constant.KEY_PRODUCT_TAX_VALUE1, "0");
                                // map.put(Constant.KEY_PRODUCT_TAX_VALUE2, "0");
                                map.put(Constant.KEY_PRODUCT_CURRENCY_ID, chargeObj.getString("product_currency_id"));
                                map.put(Constant.KEY_PRODUCT_CURRENCY_NAME, chargeObj.getString("product_currency_name"));
                                map.put(Constant.KEY_PRODUCT_TAX_ONE_NAME, chargeObj.getString("product_tax_one_name"));
                                map.put(Constant.KEY_PRODUCT_TAX_TWO_NAME, chargeObj.getString("product_tax_two_name"));
                                map.put(Constant.KEY_INVENTORY_CURRENT_STOCK, chargeObj.getString("inventory_current_stock"));
                                map.put(Constant.KEY_TRACK_INVENTORY, chargeObj.getString("track_inventory"));
                                map.put(Constant.KEY_PRODUCT_TAXID1, chargeObj.getString("product_taxID1"));
                                map.put(Constant.KEY_PRODUCT_TAXID2, chargeObj.getString("product_taxID2"));
                                map.put(Constant.KEY_CREATED_BY, chargeObj.getString("created_by"));


                                productList.add(map);
                            }

                            for (int k = 0; k < object.getJSONObject("services").getJSONArray("service").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("services").getJSONArray("service").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("service_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("service_name"));
                                map.put(Constant.KEY_UNITCOST, chargeObj.getString("unit_cost"));
                                map.put(Constant.KEY_QUANTITY, chargeObj.getString("quantity"));
                                map.put(Constant.KEY_DESCRIPTION, chargeObj.getString("description"));
                                map.put(Constant.KEY_DATE, chargeObj.getString("date"));

                                map.put(Constant.KEY_STATUS, chargeObj.getString("status"));

                                map.put(Constant.KEY_SERVICE_TAX_VALUE1, chargeObj.getString("service_tax_value1"));
                                map.put(Constant.KEY_SERVICE_TAX_VALUE2, chargeObj.getString("service_tax_value2"));

                                //   map.put(Constant.KEY_SERVICE_TAX_VALUE1,"0");
                                //   map.put(Constant.KEY_SERVICE_TAX_VALUE2, "0");

                                map.put(Constant.KEY_SERVICE_CURRENCY_ID, chargeObj.getString("service_currency_id"));
                                map.put(Constant.KEY_SERVICE_CURRENCY_NAME, chargeObj.getString("service_currency_name"));
                                map.put(Constant.KEY_SERVICE_TAX_ONE_NAME, chargeObj.getString("service_tax_one_name"));

                                map.put(Constant.KEY_SERVICE_TAX_TWO_NAME, chargeObj.getString("service_tax_two_name"));
                                map.put(Constant.KEY_INVENTORY_CURRENT_STOCK, chargeObj.getString("inventory_current_stock"));
                                map.put(Constant.KEY_TRACK_INVENTORY, chargeObj.getString("track_inventory"));
                                map.put(Constant.KEY_SERVICE_TAXID1, chargeObj.getString("service_taxID1"));
                                map.put(Constant.KEY_SERVICE_TAXID2, chargeObj.getString("service_taxID2"));
                                map.put(Constant.KEY_CREATED_BY, chargeObj.getString("created_by"));

                                servicesList.add(map);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();

                        if (isProductSelected)
                            myPopUp = new CustomPopup((Activity) context, productList, Constant.POP_UP.PRODUCT_LIST, true, this, "Select Product");
                        else
                            myPopUp = new CustomPopup((Activity) context, servicesList, Constant.POP_UP.SERVICE_LIST, true, this, "Select Service");
                        myPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "listGblProduct");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isProductSelected)
                    request = new WebRequest(context, obj,
                            Constant.invoicelistURL, SERVICE_TYPE.GET_PRODUCT_LIST, Constant.token, this, isShowProgressBar);
                else
                    request = new WebRequest(context, obj,
                            Constant.invoicelistURL, SERVICE_TYPE.GET_SERVICE_LIST, Constant.token, this, isShowProgressBar);
                request.execute();
                break;


            case R.id.tax1:
                isShowProgressBar = false;

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_TaxList;

                cursor = db.getRecords(selectQuery);

                Log.e("cusor length ", cursor.getCount() + "");

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    taxList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            HashMap<String, String> map;

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("taxes").getJSONArray("tax").length(); k++) {

                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(k);

                                if (chargeObj.getString("type").equalsIgnoreCase("Normal")) {

                                    map.put(Constant.KEY_ID, chargeObj.getString("id"));
                                    map.put(Constant.KEY_NAME, chargeObj.getString("name"));
                                    map.put(Constant.KEY_TYPE, chargeObj.getString("type"));
                                    map.put(Constant.KEY_VALUE, chargeObj.getString("value"));
                                    taxList.add(map);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();
                        myPopUp = new CustomPopup((Activity) context, taxList, Constant.POP_UP.TAX_LIST1, true, this, "Select Tax");
                        myPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "listGblTax");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, SERVICE_TYPE.GET_TAX_LIST1, Constant.token, this, isShowProgressBar);
                request.execute();
                break;

            case R.id.tax2:

              /*  if (TaxId1.isEmpty())
                {
                    global.showAlert("Please apply tax 1 before it",context);
                    return;

                }*/

                isShowProgressBar = false;

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_TaxList;

                cursor = db.getRecords(selectQuery);

                Log.e("cusor length ", cursor.getCount() + "");

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    taxList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            HashMap<String, String> map;

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("taxes").getJSONArray("tax").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("id"));

                                map.put(Constant.KEY_TYPE, chargeObj.getString("type"));
                                map.put(Constant.KEY_VALUE, chargeObj.getString("value"));
                                if (chargeObj.getString("type").equalsIgnoreCase("Compound")) {
                                    map.put(Constant.KEY_NAME, "#" + chargeObj.getString("name"));

                                } else {
                                    map.put(Constant.KEY_NAME, chargeObj.getString("name"));
                                }
                                taxList.add(map);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();
                        myPopUp = new CustomPopup((Activity) context, taxList, Constant.POP_UP.TAX_LIST2, true, this, "Select Tax");
                        myPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "listGblTax");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, SERVICE_TYPE.GET_TAX_LIST2, Constant.token, this, isShowProgressBar);
                request.execute();
                break;
            case R.id.back:
                finish();
                break;

            default:
                break;


        }

    }


    @Override
    public void getPopUpResult(Constant.POP_UP type, int pos, boolean clear) {
        if (request != null)
            request.cancel(true);
        quantity = 1;

        switch (type) {
            case PRODUCT_LIST:

                if (clear) {
                    isProductSelected = true;
                    selectedItemTV.setText("");
                    quantityEt.setText("");
                    unitCostEt.setText("");
                    descriptionEt.setText("");
                    tax1TV.setText("Apply Tax 1");
                    tax2TV.setText("Apply Tax 2");
                    TaxId1 = "";
                    tax1Name = "";
                    tax2Name = "";
                    TaxId2 = "";
                    quantity = 0;
                    discountEt.setText(0 + "");
                    unitCost = 0;
                    tax1value = 0;
                    tax2value = 0;
                    discountAmount = 0;
                    setTotal();
                    isItemSelected = false;
                    detilLinear.setVisibility(View.GONE);
                    return;
                }
                detilLinear.setVisibility(View.VISIBLE);

                quantity = 1;
                unitCost = 0;
                tax1value = 0;
                tax2value = 0;
                discountAmount = 0;
                discountEt.setText(0 + "");
                isItemSelected = true;
                itemCurrency = productList.get(pos).get(Constant.KEY_PRODUCT_CURRENCY_NAME);
                if (itemCurrency == null) {

                    Toast.makeText(context, "Product currency is not valid", Toast.LENGTH_SHORT).show();
                    return;

                }


                Log.e("product list", productList.get(pos).toString());
                selectedItemTV.setText(productList.get(pos).get(Constant.KEY_NAME));
                itemCurrency = productList.get(pos).get(Constant.KEY_PRODUCT_CURRENCY_NAME);


                try {
                    quantity = (int) Float.parseFloat(productList.get(pos).get(Constant.KEY_QUANTITY).replaceAll(",", ""));//Integer.parseInt(productList.get(pos).get(Constant.KEY_QUANTITY).replaceAll(",", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                quantityEt.setText(quantity + "");
                try {
                    unitCostBasePrice = Double.parseDouble(productList.get(pos).get(Constant.KEY_UNITCOST));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                unitCostEt.setText(productList.get(pos).get(Constant.KEY_UNITCOST));
                descriptionEt.setText(productList.get(pos).get(Constant.KEY_DESCRIPTION));


                if (!client_currency.equalsIgnoreCase(productList.get(pos).get(Constant.KEY_PRODUCT_CURRENCY_NAME))) {
                    if (global.isNetworkAvailable()) {
                        currencyConversionRequest = new DownloadData();
                        currencyConversionRequest.execute();
                    } else {
                        global.showAlert("Client currency is " + client_currency + " and it is not match with product currency " + productList.get(pos).get(Constant.KEY_PRODUCT_CURRENCY_NAME) + " ,so please convert it manually ", context);
                        currencyConversionLiner.setVisibility(View.VISIBLE);
                        CurrencyProgressBar.setVisibility(View.GONE);
                    }
                } else {

                    currencyConversionLiner.setVisibility(View.GONE);
                }


                try {
                    TaxId1 = productList.get(pos).get(Constant.KEY_PRODUCT_TAXID1);
                    if (!TaxId1.isEmpty()) {
                        tax1value = Double.parseDouble(productList.get(pos).get(Constant.KEY_PRODUCT_TAX_VALUE1).toString().replaceAll(",", ""));
                        tax1TV.setText(productList.get(pos).get(Constant.KEY_PRODUCT_TAX_ONE_NAME) + "(" + productList.get(pos).get(Constant.KEY_PRODUCT_TAX_VALUE1) + "%)");
                        tax1Name = productList.get(pos).get(Constant.KEY_PRODUCT_TAX_ONE_NAME);
                    } else {
                        tax1TV.setText("Apply Tax 1");
                        tax1Name = "";
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                try {

                    TaxId2 = productList.get(pos).get(Constant.KEY_PRODUCT_TAXID2);
                    if (!TaxId2.isEmpty()) {

                        tax2value = Double.parseDouble(productList.get(pos).get(Constant.KEY_PRODUCT_TAX_VALUE2).toString().replaceAll(",", ""));
                        tax2Name = productList.get(pos).get(Constant.KEY_PRODUCT_TAX_TWO_NAME);
                        tax2TV.setText(productList.get(pos).get(Constant.KEY_PRODUCT_TAX_TWO_NAME) + "(" + productList.get(pos).get(Constant.KEY_PRODUCT_TAX_VALUE2) + "%)");
                    } else {
                        tax2TV.setText("Apply Tax 2");
                        tax2Name = "";
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                setTotal();

                break;
            case SERVICE_LIST:
                if (clear) {
                    isProductSelected = false;
                    selectedItemTV.setText("");
                    quantityEt.setText("");
                    unitCostEt.setText("");
                    descriptionEt.setText("");
                    tax1TV.setText("Apply Tax 1");
                    tax2TV.setText("Apply Tax 2");

                    discountEt.setText(0 + "");
                    TaxId1 = "";
                    TaxId2 = "";
                    tax2Name = "";
                    tax1Name = "";
                    quantity = 0;
                    unitCost = 0;
                    tax1value = 0;
                    tax2value = 0;
                    discountAmount = 0;
                    setTotal();
                    isItemSelected = false;
                    detilLinear.setVisibility(View.GONE);
                    return;
                }
                detilLinear.setVisibility(View.VISIBLE);
                quantity = 1;
                unitCost = 0;
                tax1value = 0;
                tax2value = 0;
                discountAmount = 0;
                discountEt.setText(0 + "");
                isItemSelected = true;
                itemCurrency = servicesList.get(pos).get(Constant.KEY_SERVICE_CURRENCY_NAME);
                if (itemCurrency == null) {

                    Toast.makeText(context, "Services currency is not valid", Toast.LENGTH_SHORT).show();
                    return;

                }
                Log.e("service list", servicesList.get(pos).toString());
                quantity = 1;
                try {
                    quantity = Integer.parseInt(servicesList.get(pos).get(Constant.KEY_QUANTITY).replaceAll(",", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                selectedItemTV.setText(servicesList.get(pos).get(Constant.KEY_NAME));
                quantityEt.setText(quantity + "");
                try {
                    unitCostBasePrice = Double.parseDouble(servicesList.get(pos).get(Constant.KEY_UNITCOST));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                unitCostEt.setText(servicesList.get(pos).get(Constant.KEY_UNITCOST));
                descriptionEt.setText(servicesList.get(pos).get(Constant.KEY_DESCRIPTION));


                if (!client_currency.equalsIgnoreCase(servicesList.get(pos).get(Constant.KEY_SERVICE_CURRENCY_NAME))) {
                    if (global.isNetworkAvailable()) {
                        currencyConversionRequest = new DownloadData();
                        currencyConversionRequest.execute();
                    } else {

                        global.showAlert("Client currency is " + client_currency + " and it is not match with service currency " + productList.get(pos).get(Constant.KEY_PRODUCT_CURRENCY_NAME) + " ,so please convert it manually ", context);
                        currencyConversionLiner.setVisibility(View.VISIBLE);
                        CurrencyProgressBar.setVisibility(View.GONE);
                    }
                } else
                    currencyConversionLiner.setVisibility(View.GONE);

                try {


                    TaxId1 = servicesList.get(pos).get(Constant.KEY_SERVICE_TAXID1);
                    if (!TaxId1.isEmpty()) {
                        tax1value = Double.parseDouble(servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_VALUE1).toString().replaceAll(",", ""));
                        tax1TV.setText(servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_ONE_NAME) + "(" + servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_VALUE1) + "%)");
                        tax1Name = servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_ONE_NAME);
                    } else {
                        tax1TV.setText("Apply Tax 1");
                        tax1Name = "";
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                try {

                    TaxId2 = servicesList.get(pos).get(Constant.KEY_SERVICE_TAXID2);
                    if (!TaxId2.isEmpty()) {
                        tax2value = Double.parseDouble(servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_VALUE2).toString().replaceAll(",", ""));
                        tax2TV.setText(servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_TWO_NAME) + "(" + servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_VALUE2) + "%)");
                        tax2Name = servicesList.get(pos).get(Constant.KEY_SERVICE_TAX_TWO_NAME);
                    } else {
                        tax2TV.setText("Apply Tax 2");
                        tax2Name = "";
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                setTotal();
                break;
            case TAX_LIST1:


                if (clear) {

                    tax1TV.setText("Apply Tax 1");
                    TaxId1 = "";

                    tax1value = 0;
                    setTotal();

                    // also clear second tax

                    tax2TV.setText("Apply Tax 2");
                    TaxId2 = "";
                    tax2Name = "";
                    tax1Name = "";
                    tax2value = 0;
                    tax2Type = "";
                    setTotal();

                    return;
                }
                tax1value = 0;

                if (TaxId1.isEmpty())
                    TaxId1 = taxList.get(pos).get(Constant.KEY_ID).toString();
                else {

                    if (taxList.get(pos).get(Constant.KEY_ID).toString().equalsIgnoreCase(TaxId2)) {
                        global.showAlert("This tax has already been selected", context);
                        return;
                    }
                }

                tax1TV.setText(taxList.get(pos).get(Constant.KEY_NAME) + "(" + taxList.get(pos).get(Constant.KEY_VALUE).toString() + "%)");
                tax1Name = taxList.get(pos).get(Constant.KEY_NAME);
                tax1value = Double.parseDouble(taxList.get(pos).get(Constant.KEY_VALUE).toString().replaceAll(",", ""));
                TaxId1 = taxList.get(pos).get(Constant.KEY_ID).toString();
                setTotal();

                break;


            case TAX_LIST2:


                if (clear) {

                    tax2TV.setText("Apply Tax 2");
                    TaxId2 = "";

                    tax2value = 0;
                    tax2Name = "";
                    tax2Type = "";
                    setTotal();
                    return;
                }
                tax2value = 0;

                if (TaxId1.isEmpty()) {
                    global.showAlert("Please select tax1", context);
                    return;
                } else {

                    if (TaxId1.equalsIgnoreCase(taxList.get(pos).get(Constant.KEY_ID).toString())) {
                        global.showAlert("This tax has already been selected", context);
                        return;
                    }
                }


                if (taxList.get(pos).get(Constant.KEY_TYPE).toString().equalsIgnoreCase("Compound")) {
                    if (TaxId1.isEmpty()) {
                        global.showAlert("Please apply tax 1 before compound tax", context);
                        return;
                    }
                }

                tax2TV.setText(taxList.get(pos).get(Constant.KEY_NAME) + "(" + taxList.get(pos).get(Constant.KEY_VALUE).toString() + "%)");
                tax2value = Double.parseDouble(taxList.get(pos).get(Constant.KEY_VALUE).toString().replaceAll(",", ""));
                TaxId2 = taxList.get(pos).get(Constant.KEY_ID).toString();
                tax2Type = taxList.get(pos).get(Constant.KEY_TYPE);
                tax2Name = taxList.get(pos).get(Constant.KEY_NAME);
                setTotal();
                break;


        }

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


        boolean isShowProgressBar = false;
        switch (checkedId) {


            case R.id.productButton:

                isProductSelected = true;
                quantity = 0;
                unitCost = 0;
                tax1value = 0;
                tax2value = 0;
                discountAmount = 0;
                selectedItemTV.setText("Select product");
                quantityEt.setText("");
                unitCostEt.setText("");
                descriptionEt.setText("");
                discountEt.setText("0.0");
                tax1TV.setText("Apply Tax 1");
                tax2TV.setText("Apply Tax 2");
                if (currencyConversionRequest != null)
                    currencyConversionRequest.cancel(true);

                if (request != null)
                    request.cancel(true);

                detilLinear.setVisibility(View.GONE);
                break;

            case R.id.serviceButton:

                selectedItemTV.setText("Select Service");
                if (request != null)
                    request.cancel(true);
                if (currencyConversionRequest != null)
                    currencyConversionRequest.cancel(true);

                quantity = 0;
                unitCost = 0;
                tax1value = 0;
                tax2value = 0;
                discountEt.setText("0.0");
                discountAmount = 0;
                quantityEt.setText("");
                unitCostEt.setText("");
                descriptionEt.setText("");
                tax1TV.setText("Apply Tax 1");
                tax2TV.setText("Apply Tax 2");

                isProductSelected = false;

                detilLinear.setVisibility(View.GONE);
                break;
            case R.id.flat:
                isDiscountFlat = true;
                discountEt.setText("0.0");
                break;
            case R.id.percent:
                isDiscountFlat = false;
                discountEt.setText("0.0");
                break;

        }


    }

    class DownloadData extends AsyncTask<Void, Integer, String> {

        ProgressDialog pd = null;

        @Override
        protected String doInBackground(Void... params) {

            String s;
            String theResult = "";
            Log.e("Currency *** ", itemCurrency + "," + client_currency);

            try {          //  Log.e("conversion 21:","---------");


                s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"
                        + itemCurrency
                        + client_currency

                        + "%22)"

                        + "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
                //  Log.e("conversion 22:","---------");


                JSONObject jObj;
                jObj = new JSONObject(s);
                theResult = jObj.getJSONObject("query")
                        .getJSONObject("results").getJSONObject("rate")
                        .getString("Rate");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return theResult;
        }


        @Override
        protected void onPostExecute(String theResult) {
            super.onPostExecute(theResult);

            //Log.e("conversion 3:",theResult+"");
            if (pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }

            CurrencyProgressBar.setVisibility(View.GONE);
            convertRateEditText.setText(theResult);
            currencyConversionTV.setText(client_currency + " = 1 "
                    + itemCurrency);


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            CurrencyProgressBar.setVisibility(View.GONE);
            if (pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            Log.e("conversion 1:", "---------");
            CurrencyProgressBar.setVisibility(View.VISIBLE);
            currencyConversionLiner.setVisibility(View.VISIBLE);

            pd = new ProgressDialog(context);
            pd.setTitle("Converting...");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }
    }

    public String getJson(String url) throws ClientProtocolException,
            IOException {
        StringBuilder build = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                content));
        String con;
        while ((con = reader.readLine()) != null) {
            build.append(con);
        }
        return build.toString();
    }

    public void setTotal() {


        try {
            if (!unitCostEt.getText().toString().isEmpty())
                unitCost = Double.parseDouble(unitCostEt.getText().toString().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            if (!quantityEt.getText().toString().isEmpty())
                quantity = Double.parseDouble(quantityEt.getText().toString().replaceAll(",", ""));
           else quantity=1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        if (!discountEt.getText().toString().isEmpty()) {
            if (!isDiscountFlat) {
                discountAmount = (unitCost * quantity) * Double.parseDouble(discountEt.getText().toString().replaceAll(",", "")) / 100;
            } else {

                discountAmount = Double.parseDouble(discountEt.getText().toString().replaceAll(",", ""));
            }
        } else discountAmount = 0;

        tax1Amount = ((unitCost * quantity) - discountAmount) * tax1value / 100;
        tax2Amount = ((unitCost * quantity) - discountAmount) * tax2value / 100;

        if (tax2Type.equalsIgnoreCase("Compound"))
            tax2Amount = (((unitCost * quantity) - discountAmount) + tax1Amount) * tax2value / 100;

        double total = ((unitCost * quantity) - discountAmount) + tax1Amount + tax2Amount;
        totalValueTV.setText(Utils.FloatToStringLimits(total + ""));


        if (total < 0) {
            if (!discountEt.getText().toString().isEmpty())
                if (Double.parseDouble(discountEt.getText().toString()) > 0) {
                    global.showAlert("Discount  should not be greater than total", context);
                    discountEt.setText("0");
                    setTotal();
                }
        }

        Log.e("Tax value", tax1value + "," + tax2value);
        Log.e("all attribute ", "UnitCost:" + unitCost + ",quantity:" + quantity + ",discountAmount:" + discountAmount + ",tax1Amount:" + tax1Amount + ",tax2Amount:" + tax2Amount);
    }

    public void setDataOfEditingItem() {


        if (itemModel.getType().equalsIgnoreCase("Service")) {
            isProductSelected = false;
            productRadioButton.setChecked(false);
            servicesRadioButton.setChecked(true);

        } else {

            isProductSelected = true;

            productRadioButton.setChecked(true);
            servicesRadioButton.setChecked(false);
        }
        isItemSelected = true;

        selectedItemTV.setText(itemModel.getName());
        quantityEt.setText(itemModel.getQuantity());
        unitCostEt.setText(itemModel.getUnit_cost());
        descriptionEt.setText(itemModel.getDescription());

        if (itemModel.getDiscount_type().equalsIgnoreCase("Fixed")) {
            isDiscountFlat = true;
            flatDiscountRadioButton.setChecked(true);
            percentDiscountRadioButton.setChecked(false);

        } else {

            flatDiscountRadioButton.setChecked(false);
            percentDiscountRadioButton.setChecked(true);
            isDiscountFlat = false;
        }
        discountEt.setText(itemModel.getDiscountValue());
        if (!itemModel.getTax1_name().isEmpty()) {
            tax1TV.setText(itemModel.getTax1_name() + "(" + itemModel.getTax1_percent() + " %)");
            TaxId1 = itemModel.getTax1Id();
            tax1Name = itemModel.getTax1_name();

            try {
                tax1value = Double.parseDouble(itemModel.getTax1_percent());
            } catch (NumberFormatException e) {
                e.printStackTrace();

            }
        } else {

            tax1TV.setText("Apply Tax 1");
            TaxId1 = "";
            tax1value = 0;
        }

        if (!itemModel.getTax2_name().isEmpty()) {
            if (tax2Type.equalsIgnoreCase("Compound"))
                tax2TV.setText("#" + itemModel.getTax2_name() + "(" + itemModel.getTax2_percent() + " %)");
            else
                tax2TV.setText(itemModel.getTax2_name() + "(" + itemModel.getTax2_percent() + " %)");
            TaxId2 = itemModel.getTax2Id();
            tax2Name = itemModel.getTax2_name();
            tax2Type = itemModel.getTax2Type();
            try {
                tax2value = Double.parseDouble(itemModel.getTax2_percent());
            } catch (NumberFormatException e) {
                e.printStackTrace();

            }
        } else {

            tax2TV.setText("Apply Tax 2");
            TaxId2 = "";
            tax2value = 0;
        }


        totalValueTV.setText(itemModel.getTotalValue());
        detilLinear.setVisibility(View.VISIBLE);


        setTotal();

/*
        itemModel.setTax1_name(tax1TV.getText().toString());
        itemModel.setTax2_name(tax2TV.getText().toString());
        itemModel.setTotalTAx((tax1Amount + tax2Amount) + "");
        if (isDiscountFlat)
            itemModel.setDiscount_type("Fixed");
        else itemModel.setDiscount_type("Percent");
        itemModel.setTotalValue(((quantity * unitCost) - discountAmount) + "");

        if (isProductSelected)
            itemModel.setType("Product");
        else
            itemModel.setType("Service");
        itemModel.setTax1Id(TaxId1);
        itemModel.setTax2Id(TaxId2);
        itemModel.setTax1_percent(tax1Amount + "");
        itemModel.setTax2_percent(tax2Amount + "");

        String value = discountEt.getText().toString();
        itemModel.setDiscountValue(value);

        Intent intent = new Intent();


        intent.putExtra(Constant.KEY_ITEM, itemModel);
        intent.putExtra(Constant.KEY_EDITING, isEditing);

        setResult(requestCode, intent);
*/


    }

}