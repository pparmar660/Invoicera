package com.invoicera.androidapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.CustomView.CustomPopup;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.PopUpResult;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Utility.Validation;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Parvesh on 12/10/15.
 */
public class AutoBillingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, WebApiResult, PopUpResult {
    // UI Variable
    LinearLayout ly_AutoBill;
    ImageView img_Back;
    CheckBox chk_Own, chk_Client;
    EditText et_FirstName, et_LastName, et_CardName, et_Address, et_City, et_State, et_Zip, et_CardNo, et_Cvv, et_Expiry;
    Button btn_Save;
    String countryId, paymentGatewayId;
    TextView select_Payment, select_Country, select_CardType, clearTv;

    // Other Variable
    String bill_option = "Client";
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener myDate;
    String selectQuery;
    WebRequest request;
    String myFormat = "yyyy-MM";
    SimpleDateFormat sdf;// = new SimpleDateFormat(myFormat, Locale.US);
    Cursor cursor;
    ArrayList<HashMap<String, String>> paymentGatewayList;
    ArrayList<HashMap<String, String>> creditCardList;
    ArrayList<HashMap<String, String>> countryList;
    CustomPopup myPopUp;
    public Comparator<HashMap<String, String>> mapComparator;
    boolean isShowProgressBar = false, updatePopUp;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_billing);
        //   hideSoftKeyboard();
        ly_AutoBill = (LinearLayout) findViewById(R.id.ly_AutoBill);

        img_Back = (ImageView) findViewById(R.id.img_back);
        clearTv = (TextView) findViewById(R.id.clear);
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        chk_Own = (CheckBox) findViewById(R.id.chk_Own);
        chk_Client = (CheckBox) findViewById(R.id.chk_Client);

        select_Payment = (TextView) findViewById(R.id.paymentGateway);
        et_FirstName = (EditText) findViewById(R.id.et_FirstName);
        et_LastName = (EditText) findViewById(R.id.et_LastName);
        et_CardName = (EditText) findViewById(R.id.et_CardName);
        et_Address = (EditText) findViewById(R.id.et_Address);
        select_Country = (TextView) findViewById(R.id.et_Country);
        et_City = (EditText) findViewById(R.id.et_City);
        et_State = (EditText) findViewById(R.id.et_State);
        et_Zip = (EditText) findViewById(R.id.et_Zip);
        select_CardType = (TextView) findViewById(R.id.et_CardType);
        et_CardNo = (EditText) findViewById(R.id.et_CardNo);
        et_Cvv = (EditText) findViewById(R.id.et_Cvv);
        et_Expiry = (EditText) findViewById(R.id.et_Expiry);

        btn_Save = (Button) findViewById(R.id.btn_Save);


        Intent intent = getIntent();

        if (intent.getStringExtra("detail_option") != null) {
            System.out.println(intent.getStringExtra("detail_option"));

            if (intent.getStringExtra("detail_option").equals("btnMeFill")) {
                chk_Own.setChecked(true);
                bill_option = "btnMeFill";
                ly_AutoBill.setVisibility(View.VISIBLE);
                chk_Client.setChecked(false);
            } else {
                chk_Client.setChecked(true);
                chk_Own.setChecked(false);
                bill_option = "btnClientFill";
                ly_AutoBill.setVisibility(View.GONE);
            }
        }
        if (intent.getStringExtra("payment_gateway") != null)
            select_Payment.setText(intent.getStringExtra("payment_gateway"));
        if (intent.getStringExtra("first_name") != null)
            et_FirstName.setText(intent.getStringExtra("first_name"));
        if (intent.getStringExtra("last_name") != null)
            et_LastName.setText(intent.getStringExtra("last_name"));
        if (intent.getStringExtra("name_on_card") != null)
            et_CardName.setText(intent.getStringExtra("name_on_card"));
        if (intent.getStringExtra("address") != null)
            et_Address.setText(intent.getStringExtra("address"));
        if (intent.getStringExtra("country") != null)
            select_Country.setText(intent.getStringExtra("country"));
        if (intent.getStringExtra("city") != null)
            et_City.setText(intent.getStringExtra("city"));
        if (intent.getStringExtra("state") != null)
            et_State.setText(intent.getStringExtra("state"));
        if (intent.getStringExtra("zip") != null)
            et_Zip.setText(intent.getStringExtra("zip"));
        if (intent.getStringExtra("card_type") != null)
            select_CardType.setText(intent.getStringExtra("card_type"));
        if (intent.getStringExtra("card_number") != null)
            et_CardNo.setText(intent.getStringExtra("card_number"));
        if (intent.getStringExtra("cvv") != null)
            et_Cvv.setText(intent.getStringExtra("cvv"));


        img_Back.setOnClickListener(this);
        btn_Save.setOnClickListener(this);
        clearTv.setOnClickListener(this);
        et_Expiry.setOnClickListener(this);

        chk_Own.setOnCheckedChangeListener(this);
        chk_Client.setOnCheckedChangeListener(this);
        select_Country.setOnClickListener(this);
        select_Payment.setOnClickListener(this);
        select_CardType.setOnClickListener(this);

        myCalendar = Calendar.getInstance();

        try {
            if (intent.getStringExtra("expiry_year") != null)

                if (!intent.getStringExtra("expiry_year").toString().isEmpty())

                    try {
                        myCalendar.set(Calendar.YEAR, Integer.parseInt(intent.getStringExtra("expiry_year")));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            if (intent.getStringExtra("expiry_month") != null) {
                try {if (!intent.getStringExtra("expiry_month").isEmpty())
                    myCalendar.set(Calendar.MONTH, Integer.parseInt(intent.getStringExtra("expiry_month")) - 1);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                et_Expiry.setText(sdf.format(myCalendar.getTime()));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        myDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateFields();
            }

        };
    }

    private void updateFields() {

        et_Expiry.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        JSONObject obj = new JSONObject();
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.clear:
                select_Payment.setText("");
                et_FirstName.setText("");
                et_LastName.setText("");
                et_CardName.setText("");
                et_Address.setText("");
                select_Country.setText("");
                et_City.setText("");
                et_State.setText("");
                et_Zip.setText("");
                select_CardType.setText("");
                et_CardNo.setText("");
                et_Cvv.setText("");
                et_Expiry.setText("");
                break;

            case R.id.img_back:
                if (select_Payment.getText().toString().isEmpty())
                    intent.putExtra("fromBack", true);
                else intent.putExtra("fromBack", false);
                setResult(Constant.requestCodeAutoBill, intent);
                finish();
                break;

            case R.id.btn_Save:
                cal = Calendar.getInstance();
                if (chk_Own.isChecked()) {
                    if (checkValidation()) {
                        intent.putExtra("fromBack", false);
                        intent.putExtra("detail_option", bill_option);
                        intent.putExtra("payment_gateway", select_Payment.getText().toString());
                        intent.putExtra("first_name", et_FirstName.getText().toString());
                        intent.putExtra("last_name", et_LastName.getText().toString());
                        intent.putExtra("name_on_card", et_CardName.getText().toString());
                        intent.putExtra("address", et_Address.getText().toString());
                        intent.putExtra("country", select_Country.getText().toString());
                        intent.putExtra("city", et_City.getText().toString());
                        intent.putExtra("state", et_State.getText().toString());
                        intent.putExtra("zip", et_Zip.getText().toString());
                        intent.putExtra("card_type", select_CardType.getText().toString());
                        intent.putExtra("card_number", et_CardNo.getText().toString());
                        intent.putExtra("cvv", et_Cvv.getText().toString());
                        intent.putExtra("expiry_year", myCalendar.get(Calendar.YEAR) + "");
                        intent.putExtra("expiry_month", (myCalendar.get(Calendar.MONTH) + 1) + "");
                        setResult(Constant.requestCodeAutoBill, intent);
                        finish();
                    }
                } else {
                    if (checkPayment()) {
                        intent.putExtra("fromBack", false);
                        intent.putExtra("detail_option", bill_option);
                        intent.putExtra("payment_gateway", select_Payment.getText().toString());
                        setResult(Constant.requestCodeAutoBill, intent);
                        finish();
                    }
                }

                break;

            case R.id.et_Expiry:
                new DatePickerDialog(this, myDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.et_Country:

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_ListCountry;

                cursor = db.getRecords(selectQuery);
                countryList = new ArrayList<>();


                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    showCountryList();

                } else {


                    try {
                        obj.put(Constant.KEY_METHOD, "listCountry");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    request = new WebRequest(context, obj,
                            Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_COUNTRY_LIST, Constant.token, this, true);
                    request.execute();
                }

                break;
            case R.id.paymentGateway:

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_PaymentGateways;
                cursor = db.getRecords(selectQuery);
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    paymentGatewayList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        isShowProgressBar = false;
                        try {
                            HashMap<String, String> map;
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONArray("PaymentGateway").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONArray("PaymentGateway").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("PaymentGatewayName"));

                                if (chargeObj.getString("PaymentGatewayCategory").equalsIgnoreCase("AutoBill"))
                                    paymentGatewayList.add(map);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();


                        myPopUp = new CustomPopup((Activity) context, paymentGatewayList, Constant.POP_UP.PAYMENT_GATEWAY, true, this, "Payment method");

                        myPopUp.show();
                    }
                } else isShowProgressBar = true;


                try {
                    obj.put(Constant.KEY_METHOD, "getUserGblPaymentGateways");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_PAYMENT_LIST, Constant.token, this, isShowProgressBar);
                request.execute();

                break;

            case R.id.et_CardType:
                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreditCardType;
                cursor = db.getRecords(selectQuery);
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    isShowProgressBar = false;
                    creditCardList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            HashMap<String, String> map;

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONArray("CreditCardType").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONArray("CreditCardType").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("fkCreditCardID"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("CreditCardTitle"));


                                creditCardList.add(map);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();


                        myPopUp = new CustomPopup((Activity) context, creditCardList, Constant.POP_UP.CREDIT_CARD_TYPE, true, this, "Select Credit Card");

                        myPopUp.show();
                    }
                } else isShowProgressBar = true;


                try {
                    obj.put(Constant.KEY_METHOD, "listRecurringCardType");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_CREDIT_CARD_TYPE, Constant.token, this, isShowProgressBar);
                request.execute();

                break;

        }

    }

    private boolean checkPayment() {
        boolean payment_return = true;
        if (select_Payment.getText().toString().trim().isEmpty()) {
            global.showAlert("Please select payment gateway", context);
            //select_Payment.setError("Please Select payment gateway");
            return false;
        }
        return payment_return;
    }

    public void showCountryList() {


        selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_ListCountry;

        cursor = db.getRecords(selectQuery);
        countryList = new ArrayList<>();
        if (cursor.moveToFirst() && cursor.getCount() > 0)

        {
            for (int i = 0; i < cursor.getCount(); i++) {

                try {
                    HashMap<String, String> map;

                    JSONObject object = new JSONObject(cursor.getString(cursor
                            .getColumnIndex(DatabaseHelper.JSON_DATA)));

                    for (int k = 0; k < object.getJSONArray("countryList").length(); k++) {
                        map = new HashMap<>();
                        JSONObject chargeObj = object.getJSONArray("countryList").getJSONObject(k);
                        map.put(Constant.KEY_ID, chargeObj.getString("id"));
                        map.put(Constant.KEY_CountryCode, chargeObj.getString("CountryCode"));
                        map.put(Constant.KEY_CountryName, chargeObj.getString("CountryName"));


                        countryList.add(map);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (myPopUp != null)
                    if (myPopUp.isShowing())
                        myPopUp.dismiss();


                mapComparator = new Comparator<HashMap<String, String>>() {
                    public int compare(HashMap<String, String> m1, HashMap<String, String> m2) {
                        return m1.get(Constant.KEY_CountryName).compareTo(m2.get(Constant.KEY_CountryName));
                    }
                };

                Collections.sort(countryList, mapComparator);


                myPopUp = new CustomPopup((Activity) context, countryList, Constant.POP_UP.COUNTRY, false, this, "Select Country");

                myPopUp.show();
            }
        }
    }


    private boolean
    checkValidation() {
        boolean valid_return = true;

        if (select_Payment.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Please Select payment gateway", Toast.LENGTH_SHORT).show();

            return false;
        }
        if (!Validation.hasText(et_FirstName, "Please enter first name"))
            return false;
        if (!Validation.hasText(et_LastName, "Please enter last name"))
            return false;
        if (!Validation.hasText(et_CardName, "Please enter name on card"))
            return false;
        if (!Validation.hasText(et_Address, "Please enter address"))
            return false;
        if (select_Country.getText().toString().trim().isEmpty()) {
            select_Country.setError("Please Select country");
            return false;
        }
        if (!Validation.hasText(et_City, "Please enter city"))
            return false;
        if (!Validation.hasText(et_State, "Please enter state"))
            return false;
        if (!Validation.hasText(et_Zip, "Please enter zip code"))
            return false;
        if (select_CardType.getText().toString().trim().isEmpty()) {
            Toast.makeText(context, "Please Select card type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Validation.hasText(et_CardNo, "Please enter card number")) {

            return false;
        }
        if (et_CardNo.getText().toString().length() < 13 || et_CardNo.getText().toString().length() > 16) {
            et_CardNo.setError("Card Number must be of 13-16 digits.");
            return false;
        }
        if (!Validation.hasText(et_Cvv, "Please enter cvv number")) {
            return false;
        }
        if (et_Cvv.getText().toString().trim().length() < 3) {
            et_Cvv.setError("Verification Code must be of 3 or 4 digits.");
            return false;
        }
        if (!Validation.hasText(et_Expiry, "Please select expiry date")) {
            return false;
        }


        if (myCalendar.getTimeInMillis() < (cal.getTimeInMillis() - 1000 * 60 * 60 * 24)) {
            Toast.makeText(context, "Expiry date cannot be less than current date.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return valid_return;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chk_Own:
                if (chk_Own.isChecked()) {
                    chk_Client.setChecked(false);
                    ly_AutoBill.setVisibility(View.VISIBLE);
                    bill_option = "btnMeFill";
                }
                if (!chk_Own.isChecked()) {
                    chk_Client.setChecked(true);
                    ly_AutoBill.setVisibility(View.GONE);
                    bill_option = "btnClientFill";
                }
                break;

            case R.id.chk_Client:
                if (chk_Client.isChecked()) {
                    chk_Own.setChecked(false);
                    ly_AutoBill.setVisibility(View.GONE);
                    bill_option = "btnClientFill";
                }

                if (!chk_Client.isChecked()) {
                    chk_Own.setChecked(true);
                    ly_AutoBill.setVisibility(View.VISIBLE);
                    bill_option = "btnMeFill";
                }
                break;
        }

    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {

        HashMap<String, String> map = new HashMap<>();
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
        ;

        ContentValues values = null;
        switch (type) {

            case GET_COUNTRY_LIST:
                db.ClearTable(DatabaseHelper.Table_ListCountry);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_ListCountry, values);
                showCountryList();

                break;

            case GET_PAYMENT_LIST:
                db.ClearTable(DatabaseHelper.Table_PaymentGateways);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_PaymentGateways, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_PaymentGateways;
                cursor = db.getRecords(selectQuery);


                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    paymentGatewayList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {


                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONArray("PaymentGateway").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONArray("PaymentGateway").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("PaymentGatewayName"));

                                if (chargeObj.getString("PaymentGatewayCategory").equalsIgnoreCase("AutoBill"))
                                    paymentGatewayList.add(map);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, paymentGatewayList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, paymentGatewayList, Constant.POP_UP.PAYMENT_GATEWAY, false, this, "Select Payment method");
                            myPopUp.show();

                        }
                    }
                }
                break;

            case GET_CREDIT_CARD_TYPE:
                db.ClearTable(DatabaseHelper.Table_CreditCardType);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_CreditCardType, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreditCardType;
                cursor = db.getRecords(selectQuery);
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    creditCardList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONArray("CreditCardType").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONArray("CreditCardType").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("fkCreditCardID"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("CreditCardTitle"));


                                creditCardList.add(map);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, creditCardList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, creditCardList, Constant.POP_UP.CREDIT_CARD_TYPE, false, this, "Select Credit Card");
                            myPopUp.show();

                        }
                    }
                }
                break;
        }
    }

    @Override
    public void getPopUpResult(Constant.POP_UP type, int pos, boolean clear) {

        if (clear) {
            if (request != null)
                request.cancel(true);
            return;
        }

        switch (type) {
            case COUNTRY:
                select_Country.setText(countryList.get(pos).get("CountryName"));
                countryId = countryList.get(pos).get("id");
                if (request != null)
                    request.cancel(true);
                break;

            case PAYMENT_GATEWAY:
                paymentGatewayId = paymentGatewayList.get(pos).get("id");
                select_Payment.setText(paymentGatewayList.get(pos).get(Constant.KEY_NAME));
                if (request != null)
                    request.cancel(true);
                break;

            case CREDIT_CARD_TYPE:
                select_CardType.setText(creditCardList.get(pos).get(Constant.KEY_NAME));
                if (request != null)
                    request.cancel(true);
                break;

        }
    }

 /*   public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }*/
}
