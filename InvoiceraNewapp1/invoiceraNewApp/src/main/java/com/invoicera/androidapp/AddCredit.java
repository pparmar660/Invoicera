package com.invoicera.androidapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Utility.Utils;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Parvesh on 19/8/15.
 */
public class AddCredit extends BaseActivity implements View.OnClickListener, WebApiResult {

    String clientId, currency, paymentMethod, company_id;
    EditText amountEt, noteEt;
    TextView creditTv, outStandingTv, dateTv, clientNameTv, paymentMethodTv;
    Calendar myCalendar;
    ProgressBar progressBar;
    boolean showProgressBar = true;
    ArrayList<String> paymentMethodList;
    ImageView backImage;

    CheckBox sendMailCheckBox;

    double outSandingAmount = 0, creditAmount = 0;

    TextView saveTv, cancelTv;

    DatePickerDialog.OnDateSetListener date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_credit);
        outStandingTv = (TextView) findViewById(R.id.outStanding);
        clientNameTv = (TextView) findViewById(R.id.clientName);
        creditTv = (TextView) findViewById(R.id.credit);
        dateTv = (TextView) findViewById(R.id.date);
        amountEt = (EditText) findViewById(R.id.amount);
        noteEt = (EditText) findViewById(R.id.note);
        this.paymentMethodTv = (TextView) findViewById(R.id.paymentMethod);
        progressBar = (ProgressBar) findViewById(R.id.progress_addCredit);
        myCalendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTv.setText(sdf.format(myCalendar.getTime()));
        showProgressBar = true;
        paymentMethodList = new ArrayList<>();
        paymentMethodTv.setOnClickListener(this);
        backImage = (ImageView) findViewById(R.id.back);
        backImage.setOnClickListener(this);
        saveTv = (TextView) findViewById(R.id.save);
        cancelTv = (TextView) findViewById(R.id.cancel);
        sendMailCheckBox=(CheckBox)findViewById(R.id.isSendMail);

        saveTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);


        if (getIntent().getStringExtra(Constant.KEY_CLIENT_ID) != null)
            clientId = getIntent().getStringExtra(Constant.KEY_CLIENT_ID);

        if (getIntent().getStringExtra(Constant.KEY_OUTSTANDING) != null)
            try {
                outSandingAmount = Double.parseDouble(getIntent().getStringExtra(Constant.KEY_OUTSTANDING).replaceAll(",", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        if (getIntent().getStringExtra(Constant.KEY_CREDIT) != null)
            try {
                creditAmount = Double.parseDouble(getIntent().getStringExtra(Constant.KEY_CREDIT).replaceAll(",", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        if (getIntent().getStringExtra(Constant.KEY_CURRENCY) != null)

            currency = getIntent().getStringExtra(Constant.KEY_CURRENCY);

        if (getIntent().getStringExtra(Constant.KEY_COMPANY_ID) != null)
            company_id = getIntent().getStringExtra(Constant.KEY_COMPANY_ID);


        if (getIntent().getStringExtra(Constant.KEY_CLIENT_NAME) != null)

            clientNameTv.setText(getIntent().getStringExtra(Constant.KEY_CLIENT_NAME));

        outStandingTv.setText(currency + " " + global.setLength(outSandingAmount + "", 15));
        creditTv.setText(currency + " " + global.setLength(creditAmount + "", 15));
        amountEt.setText(Utils.round(outSandingAmount) + "");


        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_PaymentMethod;

        Cursor cursor = db.getRecords(selectQuery);

        if (cursor.moveToFirst() && cursor.getCount() > 0) {

            paymentMethodList = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {


                try {
                    showProgressBar = false;

                    JSONObject object = new JSONObject(cursor.getString(cursor
                            .getColumnIndex(DatabaseHelper.JSON_DATA)));

                    for (int k = 0; k < object.getJSONObject("payment_modes").getJSONArray("payment_mode").length(); k++) {

                        paymentMethodList.add(object.getJSONObject("payment_modes").getJSONArray("payment_mode").getString(k));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


        if (!paymentMethodList.isEmpty()) {
            paymentMethodTv.setText("Payment Method : " + paymentMethodList.get(0));
            paymentMethod=paymentMethodList.get(0);
        }

        JSONObject paymentMethod = null;
        try {
            paymentMethod = new JSONObject();
            paymentMethod.put("method", "listPaymentMethod");

            WebRequest request = new WebRequest(context, paymentMethod, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_LIST, Constant.token, this, showProgressBar);
            request.execute();
            progressBar.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateTv.setOnClickListener(this);


    }


    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTv.setText( sdf.format(myCalendar.getTime()));


        }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.paymentMethod:


                ArrayAdapter<String> adapter_tax = new ArrayAdapter<String>(
                        AddCredit.this, R.layout.simplelist_center_text,
                        paymentMethodList);
                if (paymentMethodList.size() > 0) {

                    new AlertDialog.Builder(AddCredit.this)
                            .setTitle("Payment Method")
                            .setAdapter(adapter_tax,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method
                                            // stub
                                            paymentMethod = paymentMethodList.get(which);
                                            paymentMethodTv.setText("Payment Method : " + paymentMethodList
                                                    .get(which).toString());
                                        }
                                    }).show();
                } else {
                    //methodET.setHint(GlobalVariables.no_op);
                }


                break;

            case R.id.back:

                finish();
                break;

            case R.id.cancel:
                finish();
                break;
            case R.id.save:

                SendDataToServer();
                break;

            case R.id.date:

                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;


        }

    }

    public void SendDataToServer() {

        try {
            JSONObject invoiceOfflinePayment = new JSONObject();

            JSONObject invoice = new JSONObject();
            invoice.put("client_id", clientId);
            invoice.put("comment", "");
            invoice.put("method",paymentMethod);
            invoice.put("amount", Utils.FloatToStringLimits(amountEt.getText().toString()));
            invoice.put("fkClientUserCompanyID", company_id);
            invoice.put("date", dateTv.getText());
            invoice.put("notes", noteEt.getText().toString());
            if (sendMailCheckBox.isChecked())
            invoice.put("client_notification", "1");
            else  invoice.put("client_notification","0");
            invoiceOfflinePayment.put("credit", invoice);
            invoiceOfflinePayment.put("method", "addCredit");

            WebRequest request = new WebRequest(context, invoiceOfflinePayment, Constant.invoicelistURL, Constant.SERVICE_TYPE.SEND_DATA, Constant.token, this, true);
            request.execute();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {


        progressBar.setVisibility(View.GONE);


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

        switch (type) {

            case GET_LIST:

                db.ClearTable(DatabaseHelper.Table_PaymentMethod);
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                db.insert(DatabaseHelper.Table_PaymentMethod, values);

                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_PaymentMethod;

                Cursor cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    paymentMethodList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {


                        try {


                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("payment_modes").getJSONArray("payment_mode").length(); k++) {

                                paymentMethodList.add(object.getJSONObject("payment_modes").getJSONArray("payment_mode").getString(k));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (!paymentMethodList.isEmpty())
                    paymentMethodTv.setText("Payment Method : " + paymentMethodList.get(0));


                break;

            case  SEND_DATA:
                //global.showAlert("Credit has been added",context);
                finish();
                break;


        }


    }


}
