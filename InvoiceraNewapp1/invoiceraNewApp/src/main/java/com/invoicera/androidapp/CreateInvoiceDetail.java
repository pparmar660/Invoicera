package com.invoicera.androidapp;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CreateInvoiceDetail extends BaseActivity implements WebApiResult, View.OnClickListener, PopUpResult {


    EditText invoice_noET, invoice_titleET, invoice_noteET, term_conditionET;

    int requestCode;
    TextView late_feeSelectTV, late_feeNameTV, invoice_dateTV, schedual_onTV, due_dateTV;

    ImageView save, back;
    DatePickerDialog.OnDateSetListener datePicker;
    CustomPopup customPopUp;
    Calendar myCalendar;
    DatabaseHelper db;
    String lateFeeId = "", lateFeeName = "";
    ContentValues values;
    int dateFor = 1;
    WebRequest request;
    boolean isEditing = false;


    ArrayList<HashMap<String, String>> lateFeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_invoice_detail);

        lateFeeList = new ArrayList<>();
        Intent intent = getIntent();
        db = new DatabaseHelper(context);


        invoice_noET = (EditText) findViewById(R.id.number);
        invoice_dateTV = (TextView) findViewById(R.id.invoice_date);
        invoice_titleET = (EditText) findViewById(R.id.invoiceTitle);
        schedual_onTV = (TextView) findViewById(R.id.scheduled_on);
        late_feeSelectTV = (TextView) findViewById(R.id.latefeeSelect);
        late_feeNameTV = (TextView) findViewById(R.id.late_fee_value);
        save = (ImageView) findViewById(R.id.save);
        due_dateTV = (TextView) findViewById(R.id.due_date);
        invoice_noteET = (EditText) findViewById(R.id.invoiceNote);
        term_conditionET = (EditText) findViewById(R.id.invoiceTermAndCondition);
        back = (ImageView) findViewById(R.id.back);
        myCalendar = Calendar.getInstance();
        //   itemModelsList=new ArrayList<>();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        invoice_dateTV.setText(sdf.format(myCalendar.getTime()));


        if (intent.getStringExtra(Constant.KEY_NO) != null)
            invoice_noET.setText(intent.getStringExtra(Constant.KEY_NO));

        if (intent.getStringExtra(Constant.KEY_TITLE) != null)
            invoice_titleET.setText(intent.getStringExtra(Constant.KEY_TITLE));

        if (intent.getStringExtra(Constant.KEY_DATE) != null)
            invoice_dateTV.setText(intent.getStringExtra(Constant.KEY_DATE));

        if (intent.getStringExtra(Constant.KEY_SCHEDUAL_DATE) != null) {


            schedual_onTV.setText(intent.getStringExtra(Constant.KEY_SCHEDUAL_DATE));
        }
        if (intent.getStringExtra(Constant.KEY_DUE_DATE) != null)
            due_dateTV.setText(intent.getStringExtra(Constant.KEY_DUE_DATE));

        if (intent.getStringExtra(Constant.KEY_NOTE) != null)
            invoice_noteET.setText(intent.getStringExtra(Constant.KEY_NOTE));

        if (intent.getStringExtra(Constant.KEY_LATE_FEE) != null)
            late_feeSelectTV.setText(intent.getStringExtra(Constant.KEY_LATE_FEE));

        if (intent.getStringExtra(Constant.KEY_TermsAndCondition) != null)
            term_conditionET.setText(intent.getStringExtra(Constant.KEY_TermsAndCondition));

        if (intent.getStringExtra(Constant.KEY_LATE_FEE_NAME) != null)
            late_feeNameTV.setText(intent.getStringExtra(Constant.KEY_LATE_FEE_NAME));

        if (intent.getStringExtra(Constant.KEY_TermsAndCondition) != null)
            lateFeeId = intent.getStringExtra(Constant.KEY_LATE_FEE_ID);

        if (intent.getBooleanExtra(Constant.KEY_EDITING, false))
            isEditing = intent.getBooleanExtra(Constant.KEY_EDITING, false);


        requestCode = intent.getIntExtra(Constant.KEY_REQUEST, 0);


        save.setOnClickListener(this);
        back.setOnClickListener(this);
        invoice_dateTV.setOnClickListener(this);
        schedual_onTV.setOnClickListener(this);
        due_dateTV.setOnClickListener(this);
        late_feeSelectTV.setOnClickListener(this);
        late_feeNameTV.setOnClickListener(this);


        datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //  view.setMinDate(System.currentTimeMillis() - 1000);
                updateLabel();
            }

        };


    }


    @Override
    public void onClick(View v) {
        DatePickerDialog dialog;

        switch (v.getId())

        {
            case R.id.save:

                if (!Validation.hasText(invoice_noET, Constant.ErrorMessage_invoiceNumber))
                    return;
                Intent intent = new Intent();
                intent.putExtra(Constant.KEY_NO, invoice_noET.getText().toString());
                intent.putExtra(Constant.KEY_DATE, invoice_dateTV.getText().toString());
                intent.putExtra(Constant.KEY_TITLE, invoice_titleET.getText().toString());
                intent.putExtra(Constant.KEY_DUE_DATE, due_dateTV.getText().toString());
                intent.putExtra(Constant.KEY_SCHEDUAL_DATE, schedual_onTV.getText().toString());
                intent.putExtra(Constant.KEY_NOTE, invoice_noteET.getText().toString());
                intent.putExtra(Constant.KEY_TermsAndCondition, term_conditionET.getText().toString());
                intent.putExtra(Constant.KEY_LATE_FEE_ID, lateFeeId);
                intent.putExtra(Constant.KEY_LATE_FEE_NAME, lateFeeName);

                setResult(requestCode, intent);

                finish();

                break;

            case R.id.back:
                finish();

                break;


            case R.id.scheduled_on:
                dateFor = 3;
                if (!schedual_onTV.getText().toString().equalsIgnoreCase("0000-00-00") || !schedual_onTV.getText().toString().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        myCalendar.setTime(sdf.parse(schedual_onTV.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dialog = new DatePickerDialog(this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
                break;

            case R.id.due_date:
                if (!due_dateTV.getText().toString().equalsIgnoreCase("0000-00-00") || !due_dateTV.getText().toString().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {


                        myCalendar.setTime(sdf.parse(due_dateTV.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dateFor = 2;
                dialog = new DatePickerDialog(this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                //dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
                break;

            case R.id.invoice_date:
                if (!invoice_dateTV.getText().toString().equalsIgnoreCase("0000-00-00") || !invoice_dateTV.getText().toString().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        myCalendar.setTime(sdf.parse(invoice_dateTV.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dateFor = 1;
                dialog = new DatePickerDialog(this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                // dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
                break;


            case R.id.latefeeSelect:
            case R.id.late_fee_value:

                if (isEditing)
                    return;

                if (due_dateTV.getText().toString().isEmpty()) {
                    global.showAlert("Please select due date before applying late fee", context);
                    return;
                }
                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_LateFee;

                Cursor cursor = db.getRecords(selectQuery);

                boolean isShowProgressBar = false;
                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    lateFeeList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            HashMap<String, String> map;
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("late_fees").getJSONArray("late_fee").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("late_fees").getJSONArray("late_fee").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("name"));

                                lateFeeList.add(map);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        customPopUp = new CustomPopup((Activity) context, lateFeeList, Constant.POP_UP.LATE_FEE, true, this, "Select Late fee");
                        customPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                JSONObject obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "listGblLateFee");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_LIST, Constant.token, this, isShowProgressBar);
                request.execute();


                break;

        }


    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        switch (dateFor) {
            case 1:
                invoice_dateTV.setText(sdf.format(myCalendar.getTime()));

                due_dateTV.setText("");
                schedual_onTV.setText("");
                break;

            case 2:
                if (CheckDates(invoice_dateTV.getText().toString(), sdf.format(myCalendar.getTime()).toString()))
                    due_dateTV.setText(sdf.format(myCalendar.getTime()));
                else {
                    global.showAlert("Due date should be after Invoice date", context);
                    due_dateTV.setText("");
                }


                break;
            case 3:
                if (CheckDates(invoice_dateTV.getText().toString(), sdf.format(myCalendar.getTime()).toString())) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE,1);

                    if (CheckDates(dateFormat.format(cal.getTime()), sdf.format(myCalendar.getTime()).toString()))
                        schedual_onTV.setText(sdf.format(myCalendar.getTime()));
                    else global.showAlert("Schedule date should be after current date", context);
                } else {
                    global.showAlert("Schedule date should be after Invoice date", context);
                    schedual_onTV.setText("");
                }

                break;


        }


    }


    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {
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


        String selectQuery;
        Cursor cursor;
        switch (type) {

            case GET_LIST:

                lateFeeList = new ArrayList<>();
                HashMap<String, String> map;
                db.ClearTable(DatabaseHelper.Table_LateFee);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_LateFee, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_LateFee;

                cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    lateFeeList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("late_fees").getJSONArray("late_fee").length(); k++) {
                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("late_fees").getJSONArray("late_fee").getJSONObject(k);
                                map.put(Constant.KEY_ID, chargeObj.getString("id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("name"));

                                lateFeeList.add(map);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    boolean updatePopUp = false;

                    if (customPopUp != null)
                        if (customPopUp.isShowing())
                            updatePopUp = true;

                    if (updatePopUp)
                        customPopUp.updateList(false, lateFeeList);
                    else {

                        customPopUp = new CustomPopup((Activity) context, lateFeeList, Constant.POP_UP.LATE_FEE, false, this, "" +
                                "Select Late Fee");
                        customPopUp.show();

                    }

                }

                break;

        }
    }


    @Override
    public void getPopUpResult(Constant.POP_UP type, int pos, boolean clear) {


        switch (type) {
            case LATE_FEE:
                request.cancel(true);
                if (clear) {
                    late_feeNameTV.setText("");
                    lateFeeId = "";
                    lateFeeName = "";
                    return;
                }

                late_feeNameTV.setText(lateFeeList.get(pos).get(Constant.KEY_NAME));
                lateFeeId = lateFeeList.get(pos).get(Constant.KEY_ID);
                lateFeeName = lateFeeList.get(pos).get(Constant.KEY_NAME);

                break;

        }

    }

    public boolean CheckDates(String startDate, String endDate) {
        Log.e("dates ", startDate + "," + endDate);

        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = true;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }


}

