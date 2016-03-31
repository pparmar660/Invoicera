package com.invoicera.androidapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Parvesh on 31/8/15.
 */
public class Expense_Filter extends BaseActivity implements View.OnClickListener, WebApiResult {


    String amountStatusArray[] = {"Paid", "Invoiced", "Unbilled"};


    TextView fromET;


    String StatusArray[] = {"Active", "Archived", "Deleted"};
    TextView ApplyTV;
    ContentValues values;

    Calendar myCalendar;
    TextView amountStatusTV, clientTV, dateTV, statusTV, amountRangeTv;
    //  ListView clientListView;
    DatabaseHelper db;
    //ArrayList<ClientAttribute> clientList;
    LinearLayout amountStatusLiner, amountRangeLinear, clientLiner, dateLiner, statusLiner;
    TextView clearTv;
    ImageView backImg;
    RadioGroup clientRadioGroup, statusRadioGroup, amountStatusRadioGroup;
    //ImageView clearDate;
    int[] selectedImg = {R.drawable.status_selected,R.drawable.amount_white, R.drawable.client_selected, R.drawable.active_selected, R.drawable.calender_selected};
    String amount_status = "";
    EditText amountFromET, amountToET;
    TextView currentamountStTv;
    boolean isClientListCalled = false;

    int[] unSelectedImg = {R.drawable.status,R.drawable.amount_grey, R.drawable.client, R.drawable.active, R.drawable.calender};
    DatePickerDialog.OnDateSetListener date;

    ArrayList<FrameLayout> frames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_filter);

        Intent intent = getIntent();

        if (intent != null)
            if (intent.getStringExtra(Constant.KEY_AMOUNT_STATUS) != null) {
                //   if (!intent.getStringExtra(Constant.KEY_INVOICE_STATUS).equalsIgnoreCase("Recent"))
                amount_status = intent.getStringExtra(Constant.KEY_AMOUNT_STATUS);

                //   System.out.println("Look:"+estimateStatus);

            }

        //  clientList = new ArrayList<>();
        amountFromET = (EditText) findViewById(R.id.amountFrom);
        amountToET = (EditText) findViewById(R.id.amountTo);
        clearTv = (TextView) findViewById(R.id.clear);
        backImg = (ImageView) findViewById(R.id.back);
        ApplyTV = (TextView) findViewById(R.id.apply);
        amountStatusTV = (TextView) findViewById(R.id.amountStatus);
        amountRangeTv = (TextView) findViewById(R.id.amountRangeTv);
        clientTV = (TextView) findViewById(R.id.client);
        statusTV = (TextView) findViewById(R.id.main_status);
        dateTV = (TextView) findViewById(R.id.date);
       // clearDate = (ImageView) findViewById(R.id.clearDate);
        currentamountStTv = (TextView) findViewById(R.id.amount_st_tv);
        amountStatusLiner = (LinearLayout) findViewById(R.id.amount_st_liner);
        amountRangeLinear = (LinearLayout) findViewById(R.id.amount_liner);
        clientRadioGroup = (RadioGroup) findViewById(R.id.clientRadioGroup);
        amountStatusRadioGroup = (RadioGroup) findViewById(R.id.amount_st_group);
        statusRadioGroup = (RadioGroup) findViewById(R.id.st_group);
        fromET = (TextView) findViewById(R.id.from);
      //  toEt = (TextView) findViewById(R.id.to);
        clientLiner = (LinearLayout) findViewById(R.id.client_list_liner);
        dateLiner = (LinearLayout) findViewById(R.id.date_liner);
        statusLiner = (LinearLayout) findViewById(R.id.status_liner);
        amountStatusTV.setOnClickListener(this);
        db = new DatabaseHelper(context);
        clientTV.setOnClickListener(this);
        //clearDate.setOnClickListener(this);
        statusTV.setOnClickListener(this);
        dateTV.setOnClickListener(this);
        ApplyTV.setOnClickListener(this);
        amountRangeTv.setOnClickListener(this);

        //
        frames = new ArrayList<>();
        frames.add((FrameLayout) findViewById(R.id.amountFrame));
        frames.add((FrameLayout) findViewById(R.id.amountRange));
        frames.add((FrameLayout) findViewById(R.id.clientframe));
        frames.add((FrameLayout) findViewById(R.id.activeframe));
        frames.add((FrameLayout) findViewById(R.id.dateframe));


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

        fromET.setOnClickListener(this);
    //    toEt.setOnClickListener(this);
        setSelectedPos(R.id.amountStatus);
        backImg.setOnClickListener(this);
        clearTv.setOnClickListener(this);

        getClientList(false);

    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
      //  if (isFromDate)
            fromET.setText(sdf.format(myCalendar.getTime()));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.amountStatus:
                setSelectedPos(v.getId());
                break;
            case R.id.amountRangeTv:
                setSelectedPos(v.getId());
                break;
            case R.id.client:
                setSelectedPos(v.getId());
                settClientList(true);

                break;
            case R.id.main_status:
                setSelectedPos(v.getId());

                break;
            case R.id.date:
                setSelectedPos(v.getId());

                break;
            case R.id.from:
             //   isFromDate = true;
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;


            case R.id.clear:

                int selectedid = clientRadioGroup.getCheckedRadioButtonId();
                if (selectedid > 0) {
                    clientRadioGroup.clearCheck();
                }

                selectedid = amountStatusRadioGroup.getCheckedRadioButtonId();
                if (selectedid > 0) {
                    amountStatusRadioGroup.clearCheck();
                }

                selectedid = statusRadioGroup.getCheckedRadioButtonId();
                if (selectedid > 0) {
                    statusRadioGroup.clearCheck();
                }

                fromET.setText("");
               // toEt.setText("");
                amountToET.setText("");
                amountFromET.setText("");

                break;
            case R.id.back:
                finish();
                break;

            case R.id.apply:
                System.out.println("Click");

                int radioButtonID = statusRadioGroup.getCheckedRadioButtonId();
                View radioButton = statusRadioGroup.findViewById(radioButtonID);
                int idx = statusRadioGroup.indexOfChild(radioButton);

                String status = "";
                if (idx >= 0)
                    status = StatusArray[idx];


                radioButtonID = amountStatusRadioGroup.getCheckedRadioButtonId();
                radioButton = amountStatusRadioGroup.findViewById(radioButtonID);
                idx = amountStatusRadioGroup.indexOfChild(radioButton);

                if (idx >= 0)
                    amount_status = amountStatusArray[idx];


                String clientId = clientRadioGroup.getCheckedRadioButtonId() + "";

                if (clientId.equalsIgnoreCase("-1"))
                    clientId = "";

                String fromDate = fromET.getText().toString();
              //  String todate = toEt.getText().toString();


                //  System.out.println(estimateStatus + "," + status + "," + clientId + "," + fromDate + "," + todate);

                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constant.KEY_CLIENT_ID, clientId);
                returnIntent.putExtra(Constant.KEY_STATUS, status);
                returnIntent.putExtra(Constant.KEY_AMOUNT_STATUS, amount_status);
                returnIntent.putExtra(Constant.KEY_DATE_FROM, fromDate);
                returnIntent.putExtra(Constant.KEY_AMOUNT_FROM,amountFromET.getText().toString());
                returnIntent.putExtra(Constant.KEY_AMOUNT_TO,amountToET.getText().toString());

                // returnIntent.putExtra(Constant.KEY_DATE_TO, todate);
                setResult(Constant.requestCodeExpenseFilter, returnIntent);
                finish();
                break;




        }


    }


    public void setSelectedPos(int id) {


        List<TextView> textViews = new ArrayList<>();
        textViews.add(amountStatusTV);
        textViews.add(amountRangeTv);
        textViews.add(clientTV);
        textViews.add(statusTV);
        textViews.add(dateTV);
        List<LinearLayout> linearLayouts = new ArrayList<>();
        linearLayouts.add(amountStatusLiner);
        linearLayouts.add(amountRangeLinear);
        linearLayouts.add(clientLiner);
        linearLayouts.add(statusLiner);
        linearLayouts.add(dateLiner);


        for (int i = 0; i < textViews.size(); i++) {

            Drawable drawable;
            if (textViews.get(i).getId() == id) {

                frames.get(i).setBackgroundColor(getResources().getColor(R.color.orange2));

                linearLayouts.get(i).setVisibility(View.VISIBLE);


                // if invoice list not recent then not set invoice status
                if (!amount_status.equalsIgnoreCase("Recent")) {
                    amountStatusRadioGroup.setVisibility(View.GONE);
                    currentamountStTv.setVisibility(View.VISIBLE);
                    currentamountStTv.setText(amount_status);
                } else {

                    amountStatusRadioGroup.setVisibility(View.VISIBLE);
                    currentamountStTv.setVisibility(View.GONE);

                }
                drawable = getResources().getDrawable(selectedImg[i]);
                textViews.get(i).setTextColor(getResources().getColor(R.color.white));

            } else {

                frames.get(i).setBackground(getResources().getDrawable(R.drawable.un_selected_filter_background));
                linearLayouts.get(i).setVisibility(View.GONE);
                drawable = getResources().getDrawable(unSelectedImg[i]);

                textViews.get(i).setTextColor(Color.parseColor("#999999"));


            }


            textViews.get(i).setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        }


    }


    private void getClientList(boolean showProgress) {


        try {
            JSONObject obj = new JSONObject();

            obj.put(Constant.KEY_METHOD, "listClient");
            WebRequest request = new WebRequest(context, obj,
                    Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_LIST, Constant.token, this, showProgress);
            request.execute();
        } catch (JSONException e) {


            Log.v("Json Exception", e.getMessage());
        }


    }


    private void settClientList(boolean sendRequest) {


        int selectedClientId = clientRadioGroup.getCheckedRadioButtonId();
        clientRadioGroup.removeAllViews();

        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.TAble_ClientList;

        Cursor cursor = db.getRecords(selectQuery);
        //    clientList = new ArrayList<>();

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {

                try {

                    JSONObject object = new JSONObject(cursor.getString(cursor
                            .getColumnIndex(DatabaseHelper.JSON_DATA)));


                    for (int j = 0; j < object.getJSONObject("clients").getJSONArray("client").length(); j++) {


                        //  clientList.add(attribute);
                        int padVal = (int) global.convertDpToPixel(10, context);
                        RadioButton button = new RadioButton(context);
                        button.setId(Integer.parseInt(object.getJSONObject("clients").getJSONArray("client").getJSONObject(j).getString("client_id")));
                        button.setText(object.getJSONObject("clients").getJSONArray("client").getJSONObject(j).getString("name"));

                        button.setButtonDrawable(new StateListDrawable());
                        // button.setButtonDrawable(R.drawable.custom_radiobuton);
                        button.setPadding(padVal, padVal, padVal, padVal);

                        Drawable drawable = getResources().getDrawable(R.drawable.custom_radiobuton);
                        button.setCompoundDrawablePadding(padVal);

                        button.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                        button.setTextColor(Color.parseColor("#3E3E3E"));
                        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);


                        button.setLayoutParams(new RadioGroup.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        if (selectedClientId > 0) {


                            if (selectedClientId == Integer.parseInt(object.getJSONObject("clients").getJSONArray("client").getJSONObject(j).getString("client_id")))
                                button.setChecked(true);
                        }
                        clientRadioGroup.addView(button);


                    }


                } catch (JSONException e) {


                }
            }

            if (selectedClientId > 0)
                clientRadioGroup.findViewById(selectedClientId).setSelected(true);
            if (sendRequest)
                getClientList(false);
        } else {
            if (sendRequest)
                getClientList(true);

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


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (type) {

            case GET_LIST:

                int selectedClientId = clientRadioGroup.getCheckedRadioButtonId();
                db.ClearTable(DatabaseHelper.TAble_ClientList);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                db.insert(DatabaseHelper.TAble_ClientList, values);

                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.TAble_ClientList;

                Cursor cursor = db.getRecords(selectQuery);
                //    clientList = new ArrayList<>();


                if (cursor.moveToFirst() && cursor.getCount() > 0)
                    settClientList(false);
    /*                for (int i = 0; i < cursor.getCount(); i++) {

                        try {

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));


                            for (int j = 0; j < object.getJSONObject("clients").getJSONArray("client").length(); j++) {


                                RadioButton button = new RadioButton(context);
                                button.setId(Integer.parseInt(object.getJSONObject("clients").getJSONArray("client").getJSONObject(j).getString("client_id")));
                                button.setText(object.getJSONObject("clients").getJSONArray("client").getJSONObject(j).getString("name"));
                                button.setButtonDrawable(R.drawable.custom_radiobuton);
                                button.setTextColor(Color.parseColor("#3E3E3E"));
                                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                int padVal = (int) global.convertDpToPixel(10, context);
                                button.setPadding(padVal, padVal, padVal, padVal);
                                button.setLayoutParams(new RadioGroup.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                clientRadioGroup.addView(button);


                            }


                        } catch (JSONException e) {


                        }
                    }*/
                if (selectedClientId > 0) {
                    //if (clientRadioGroup!=null)

                    // clientRadioGroup.findViewById(selectedClientId).setSelected(true);
                }
                isClientListCalled = true;
                break;

        }
    }


}
