package com.invoicera.androidapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.CustomView.CustomPopup;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.Fragment.BaseFragment;
import com.invoicera.Fragment.ExpenseList;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.PopUpResult;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Utility.Validation;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Parvesh on 20/10/15.
 */
public class ExpenseCreateEdit extends BaseFragment implements OnClickListener, WebApiResult, RadioGroup.OnCheckedChangeListener, PopUpResult {

    TextView categoryTv, dateTv, staffTv, tax1Tv, tax2Tv, frequencyTv, utilityTv, recurringEndDateTv, assignToProjectTV, assignToClientTV, saveTV;
    EditText amountEt, vendorEt, expenseNoteEt;
    RadioGroup assignRadioGroup;
    CheckBox recurringChk;
    boolean isShowProgressBar = false;
    boolean isEditing = false;
    String selectQuery;

    Cursor cursor;
    CustomPopup myPopUp;
    String[] createFrequencyArray = {"Weekly", "2 Weeks", "3 Weeks", "4 Weeks", "Monthly", "Quarterly", "Half Yearly", "11 Month", "Yearly", "2 Years", "3 Years"};
    ArrayAdapter<String> createFrequencyAdapter;

    String[] utilityArray = {"Forever", "End Date"};
    ArrayAdapter<String> utilityAdapter;

    ArrayList<HashMap<String, String>> taxList;
    ArrayList<HashMap<String, String>> categoryList;
    ArrayList<HashMap<String, String>> staffList;
    ArrayList<HashMap<String, String>> projectList;

    ArrayList<HashMap<String, String>> clientList;
    WebRequest request;
    ContentValues values;
    String TaxId1 = "", TaxId2 = "";
    String staffId = "", projectId = "", clientId = "", categoryId = "", date = "", expenseId = "", selectedFrequency = "", endDate = "", utility = "1";

    DatePickerDialog.OnDateSetListener datePicker;
    boolean isValueSelected = false;
    Calendar myCalendar;
    String dateFor = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.expense_create_edit, container, false);
        Home.toolbarText.setText("Create Expense");

        //
        isEditing = false;
        Bundle argument = getArguments();

        if (argument.getString(Constant.KEY_EXPENSE_ID) != null) {
            if (!argument.getString(Constant.KEY_EXPENSE_ID).isEmpty() && !argument.getString(Constant.KEY_EXPENSE_ID).equalsIgnoreCase("null")) {
                expenseId = argument.getString(Constant.KEY_EXPENSE_ID);
                isEditing = true;
                Home.toolbarText.setText("Edit Expense");
            }
        }

        categoryTv = (TextView) view.findViewById(R.id.category1);
        dateTv = (TextView) view.findViewById(R.id.date);
        staffTv = (TextView) view.findViewById(R.id.staff);
        tax1Tv = (TextView) view.findViewById(R.id.tax1);
        tax2Tv = (TextView) view.findViewById(R.id.tax2);
        amountEt = (EditText) view.findViewById(R.id.amount);
        vendorEt = (EditText) view.findViewById(R.id.vendorName);
        expenseNoteEt = (EditText) view.findViewById(R.id.expenseNote);
        saveTV = (TextView) view.findViewById(R.id.save);
        assignRadioGroup = (RadioGroup) view.findViewById(R.id.assignGroup);
        recurringChk = (CheckBox) view.findViewById(R.id.recurringChk);
        recurringEndDateTv = (TextView) view.findViewById(R.id.recurringEndDate);
        utilityTv = (TextView) view.findViewById(R.id.recurringUtility);
        frequencyTv = (TextView) view.findViewById(R.id.recurringFrequency);
        assignToClientTV = (TextView) view.findViewById(R.id.assignToClient);
        assignToProjectTV = (TextView) view.findViewById(R.id.assignToProject);
        assignToClientTV.setVisibility(View.GONE);
        assignToProjectTV.setVisibility(View.VISIBLE);
        myCalendar = Calendar.getInstance();

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTv.setText(sdf.format(myCalendar.getTime()));
        createFrequencyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, createFrequencyArray);

        utilityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, utilityArray);
        frequencyTv.setOnClickListener(this);

        categoryList = new ArrayList<>();
        taxList = new ArrayList<>();
        staffList = new ArrayList<>();
        saveTV.setOnClickListener(this);
        assignToClientTV.setOnClickListener(this);
        assignToProjectTV.setOnClickListener(this);
        categoryTv.setOnClickListener(this);
        dateTv.setOnClickListener(this);
        staffTv.setOnClickListener(this);
        tax2Tv.setOnClickListener(this);
        tax1Tv.setOnClickListener(this);
        utilityTv.setOnClickListener(this);
        recurringEndDateTv.setOnClickListener(this);
        assignRadioGroup.setOnCheckedChangeListener(this);
        recurringChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    frequencyTv.setVisibility(View.VISIBLE);
                    utilityTv.setVisibility(View.VISIBLE);
                    recurringEndDateTv.setVisibility(View.VISIBLE);

                } else {
                    frequencyTv.setVisibility(View.GONE);
                    utilityTv.setVisibility(View.GONE);
                    recurringEndDateTv.setVisibility(View.GONE);
                }

            }
        });

        datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                view.setMinDate(System.currentTimeMillis() - 1000);
                updateLabel();
            }

        };

        recurringChk.setChecked(false);
        recurringEndDateTv.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isEditing) {


            JSONObject obj = new JSONObject();
            try {
                obj.put(Constant.KEY_METHOD, "getExpense");
                obj.put("expense_id", expenseId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            request = new WebRequest(context, obj,
                    Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_EXPENSE, Constant.token, this, true);
            request.execute();
        }


    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (dateFor.equalsIgnoreCase("expenseDate"))
            dateTv.setText(sdf.format(myCalendar.getTime()));
        if (dateFor.equalsIgnoreCase("EndDate"))
            recurringEndDateTv.setText(sdf.format(myCalendar.getTime()));


    }


    @Override
    public void onClick(View v) {
        JSONObject obj;
        DatePickerDialog dialog;

        HashMap<String, String> map;
        switch (v.getId()) {

            case R.id.save:

                sendDataToServer();

                break;

            case R.id.category1:
                isShowProgressBar = false;

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CategoryList;
                cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    categoryList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONArray("catagory").length(); k++) {

                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONArray("catagory").getJSONObject(k);

                                // if (chargeObj.getString("type").equalsIgnoreCase("Normal")) {

                                map.put(Constant.KEY_ID, chargeObj.getString("category_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("name"));
                                map.put(Constant.KEY_TYPE, chargeObj.getString("type"));

                                categoryList.add(map);
                                //  }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();
                        myPopUp = new CustomPopup((Activity) context, categoryList, Constant.POP_UP.CATEGORY_LIST, true, this, "Select Category");
                        myPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "getCategoryList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.CATEGORY_LIST, Constant.token, this, isShowProgressBar);
                request.execute();
                break;

            case R.id.date:

                if (!dateTv.getText().toString().equalsIgnoreCase("0000-00-00") || !dateTv.getText().toString().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        myCalendar.setTime(sdf.parse(dateTv.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dialog = new DatePickerDialog(context, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dateFor = "expenseDate";
                dialog.show();

                break;
            case R.id.recurringUtility:
                new AlertDialog.Builder(context).setTitle("Recurring")
                        .setAdapter(utilityAdapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                utilityTv.setText("Utility : " + utilityArray[i].toString());
                                if (i == 1) {
                                    utility = "1";
                                    recurringEndDateTv.setVisibility(View.VISIBLE);
                                } else {
                                    recurringEndDateTv.setVisibility(View.GONE);
                                    recurringEndDateTv.setText("");
                                    utility = "0";
                                }

                            }
                        }).show();
                break;
            case R.id.recurringEndDate:


                if (!recurringEndDateTv.getText().toString().equalsIgnoreCase("0000-00-00") || !recurringEndDateTv.getText().toString().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        myCalendar.setTime(sdf.parse(recurringEndDateTv.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dialog = new DatePickerDialog(context, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dateFor = "EndDate";
                dialog.show();


                break;

            case R.id.staff:
                isShowProgressBar = false;

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_StaffList;
                cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    staffList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("staff_users").getJSONArray("staff").length(); k++) {

                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("staff_users").getJSONArray("staff").getJSONObject(k);

                                // if (chargeObj.getString("type").equalsIgnoreCase("Normal")) {

                                map.put(Constant.KEY_ID, chargeObj.getString("staff_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("name"));


                                staffList.add(map);
                                //  }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();
                        myPopUp = new CustomPopup((Activity) context, staffList, Constant.POP_UP.STAFF_LIST, true, this, "Select Staff");
                        myPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "getStaffList");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.STAFF_LIST, Constant.token, this, isShowProgressBar);
                request.execute();
                break;
            case R.id.recurringFrequency:

                new AlertDialog.Builder(context).setTitle("Frequency")
                        .setAdapter(createFrequencyAdapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                selectedFrequency = createFrequencyArray[i].toString();
                                frequencyTv.setText("Frequency : " + createFrequencyArray[i].toString());
                                dialog.dismiss();
                            }
                        }).show();

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
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_TAX_LIST1, Constant.token, this, isShowProgressBar);
                request.execute();
                break;
            case R.id.tax2:
                isShowProgressBar = false;

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_TaxList;

                cursor = db.getRecords(selectQuery);

                Log.e("cusor length ", cursor.getCount() + "");

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    taxList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {

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
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_TAX_LIST2, Constant.token, this, isShowProgressBar);
                request.execute();

                break;

            case R.id.assignToProject:

                isShowProgressBar = false;

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_ProjectList;


                cursor = db.getRecords(selectQuery);
                projectList = new ArrayList<>();

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));


                            JSONArray projectsArray = object.getJSONObject("result").getJSONObject("projects").getJSONArray("project");

                            for (int j = 0; j < projectsArray.length(); j++) {

                                map = new HashMap<>();
                                map.put(Constant.KEY_NAME, projectsArray.getJSONObject(j).getString("project_name"));

                                map.put(Constant.KEY_ID, projectsArray.getJSONObject(j).getString("project_id"));
                                projectList.add(map);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();
                        myPopUp = new CustomPopup((Activity) context, projectList, Constant.POP_UP.PROJECT_LIST, true, this, "Select Project List");
                        myPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "listProjectForExpense");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_PROJECT_LIST, Constant.token, this, isShowProgressBar);
                request.execute();
                break;
            case R.id.assignToClient:


                isShowProgressBar = false;

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.TAble_ClientList;


                cursor = db.getRecords(selectQuery);
                clientList = new ArrayList<>();

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));


                            JSONArray clientsArray = object.getJSONObject("clients").getJSONArray("client");

                            for (int j = 0; j < clientsArray.length(); j++) {

                                map = new HashMap<>();
                                map.put(Constant.KEY_NAME, clientsArray.getJSONObject(j).getString("organization"));

                                map.put(Constant.KEY_ID, clientsArray.getJSONObject(j).getString("client_id"));
                                clientList.add(map);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                myPopUp.dismiss();
                        myPopUp = new CustomPopup((Activity) context, clientList, Constant.POP_UP.CLIENT_LIST, true, this, "Select Client List");
                        myPopUp.show();
                    }
                } else isShowProgressBar = true;
                //-------------------call to server----------------------------------------------

                obj = new JSONObject();
                try {
                    obj.put(Constant.KEY_METHOD, "listClient");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_CLIENT_LIST, Constant.token, this, isShowProgressBar);
                request.execute();
                break;


        }

    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {
        if (result == null)
            return;
        db.ClearTable(DatabaseHelper.Table_CreateOfflineExpense);

        try {
            if (!result.getString("code").equalsIgnoreCase("200")) {
                if (result.has("message"))
                    Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_SHORT).show();
                return;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map;

        boolean updatePopUp = false;
        switch (type) {
            case GET_EXPENSE:
                setDataForEditing(result);
                break;

            case CREATE:
                fragmentChanger.onFragmentReplace(new ExpenseList(), new Bundle());

                break;
            case GET_PROJECT_LIST:

                db.ClearTable(DatabaseHelper.Table_ProjectList);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_ProjectList, values);
                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_ProjectList;

                cursor = db.getRecords(selectQuery);


                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    projectList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));


                            JSONArray projectsArray = object.getJSONObject("result").getJSONObject("projects").getJSONArray("project");

                            for (int j = 0; j < projectsArray.length(); j++) {

                                map = new HashMap<>();
                                map.put(Constant.KEY_NAME, projectsArray.getJSONObject(j).getString("project_name"));

                                map.put(Constant.KEY_ID, projectsArray.getJSONObject(j).getString("project_id"));
                                projectList.add(map);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, projectList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, projectList, Constant.POP_UP.PROJECT_LIST, false, this, "Select Project");
                            myPopUp.show();

                        }


                    }
                }


                break;

            case GET_CLIENT_LIST:

                db.ClearTable(DatabaseHelper.TAble_ClientList);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.TAble_ClientList, values);
                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.TAble_ClientList;

                cursor = db.getRecords(selectQuery);


                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    projectList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));


                            JSONArray clientsArray = object.getJSONObject("clients").getJSONArray("client");

                            for (int j = 0; j < clientsArray.length(); j++) {

                                map = new HashMap<>();
                                map.put(Constant.KEY_NAME, clientsArray.getJSONObject(j).getString("organization"));

                                map.put(Constant.KEY_ID, clientsArray.getJSONObject(j).getString("client_id"));
                                clientList.add(map);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, clientList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, clientList, Constant.POP_UP.CLIENT_LIST, false, this, "Select Client");
                            myPopUp.show();

                        }


                    }
                }


                break;

            case CATEGORY_LIST:
                db.ClearTable(DatabaseHelper.Table_CategoryList);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_CategoryList, values);
                selectQuery = "select " + DatabaseHelper.JSON_DATA + " From " + DatabaseHelper.Table_CategoryList;
                cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    categoryList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONArray("catagory").length(); k++) {

                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONArray("catagory").getJSONObject(k);

                                // if (chargeObj.getString("type").equalsIgnoreCase("Normal")) {

                                map.put(Constant.KEY_ID, chargeObj.getString("category_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("name"));
                                map.put(Constant.KEY_TYPE, chargeObj.getString("type"));

                                categoryList.add(map);
                                //  }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (myPopUp != null)
                            if (myPopUp.isShowing())
                                updatePopUp = true;


                        if (updatePopUp)
                            myPopUp.updateList(false, categoryList);
                        else {

                            myPopUp = new CustomPopup((Activity) context, categoryList, Constant.POP_UP.CATEGORY_LIST, false, this, "Select Category");
                            myPopUp.show();

                        }
                    }
                }
                break;

            case STAFF_LIST:
                db.ClearTable(DatabaseHelper.Table_StaffList);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_StaffList, values);
                selectQuery = "select " + DatabaseHelper.JSON_DATA + " From " + DatabaseHelper.Table_StaffList;
                cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    staffList = new ArrayList<>();


                    for (int i = 0; i < cursor.getCount(); i++) {


                        try {

                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("staff_users").getJSONArray("staff").length(); k++) {

                                map = new HashMap<>();
                                JSONObject chargeObj = object.getJSONObject("staff_users").getJSONArray("staff").getJSONObject(k);

                                // if (chargeObj.getString("type").equalsIgnoreCase("Normal")) {

                                map.put(Constant.KEY_ID, chargeObj.getString("staff_id"));
                                map.put(Constant.KEY_NAME, chargeObj.getString("name"));


                                staffList.add(map);
                                //  }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (myPopUp != null)
                        if (myPopUp.isShowing())
                            updatePopUp = true;


                    if (updatePopUp)
                        myPopUp.updateList(false, staffList);
                    else {

                        myPopUp = new CustomPopup((Activity) context, staffList, Constant.POP_UP.STAFF_LIST, false, this, "Select Category");
                        myPopUp.show();

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
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.project) {
            assignToProjectTV.setVisibility(View.VISIBLE);
            assignToClientTV.setVisibility(View.GONE);
        } else {
            assignToProjectTV.setVisibility(View.GONE);
            assignToClientTV.setVisibility(View.VISIBLE);


        }

    }

    @Override
    public void getPopUpResult(Constant.POP_UP type, int pos, boolean clear) {
        if (request != null)
            request.cancel(true);
        switch (type) {
            case STAFF_LIST:
                if (clear) {
                    staffTv.setText("Staff: ");
                    staffId = "";
                    return;

                }
                staffTv.setText("Staff: " + staffList.get(pos).get(Constant.KEY_NAME));
                staffId = staffList.get(pos).get(Constant.KEY_ID);
                break;

            case CATEGORY_LIST:
                if (clear) {

                    categoryTv.setText("Category: ");
                    categoryId = "";
                    return;

                }

                categoryTv.setText("Category: " + categoryList.get(pos).get(Constant.KEY_NAME));
                categoryId = categoryList.get(pos).get(Constant.KEY_ID);
                break;

            case TAX_LIST1:
                if (clear) {

                    tax1Tv.setText("Apply Tax 1");
                    TaxId1 = "";
                    tax2Tv.setText("Apply Tax 2");
                    TaxId2 = "";
                    return;
                }
                if (TaxId1.isEmpty())
                    TaxId1 = taxList.get(pos).get(Constant.KEY_ID).toString();
                else {

                    if (taxList.get(pos).get(Constant.KEY_ID).toString().equalsIgnoreCase(TaxId2)) {
                        global.showAlert("This tax has already been selected", context);
                        return;
                    }
                }

                tax1Tv.setText(taxList.get(pos).get(Constant.KEY_NAME) + "(" + taxList.get(pos).get(Constant.KEY_VALUE).toString() + "%)");
                TaxId1 = taxList.get(pos).get(Constant.KEY_ID).toString();

                break;


            case TAX_LIST2:


                if (clear) {

                    tax2Tv.setText("ApplyT ax 2");
                    TaxId2 = "";
                    return;
                }

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

                tax2Tv.setText(taxList.get(pos).get(Constant.KEY_NAME) + "(" + taxList.get(pos).get(Constant.KEY_VALUE).toString() + "%)");
                TaxId2 = taxList.get(pos).get(Constant.KEY_ID).toString();
                break;
            case PROJECT_LIST:

                if (clear) {

                    projectId = "";
                    assignToProjectTV.setText("Project : ");
                    return;
                }
                projectId = projectList.get(pos).get(Constant.KEY_ID);
                assignToProjectTV.setText("Project : " + projectList.get(pos).get(Constant.KEY_NAME));

                break;


            case CLIENT_LIST:

                if (clear) {

                    clientId = "";
                    assignToClientTV.setText("Client : ");
                    return;
                }
                clientId = clientList.get(pos).get(Constant.KEY_ID);
                assignToClientTV.setText("Client : " + clientList.get(pos).get(Constant.KEY_NAME));

                break;

        }
    }


    public void sendDataToServer() {


        if (categoryId.isEmpty()) {
            Toast.makeText(context, "Please select category", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Validation.hasText(amountEt, "Please enter amount"))
            return;

        try {
            JSONObject createExpenseObj = new JSONObject();
            createExpenseObj.put("staff_id", staffId);
            if (!isEditing)
                createExpenseObj.put("method", "createExpense");
            else {
                createExpenseObj.put("expense_id", expenseId);

                createExpenseObj.put("method", "updateExpense");
            }

            createExpenseObj.put("notes", expenseNoteEt.getText().toString());
            createExpenseObj.put("expense_category_id", categoryId);

            createExpenseObj.put("tax1", TaxId1);
            createExpenseObj.put("tax2", TaxId2);
            createExpenseObj.put("date", dateTv.getText().toString());
            createExpenseObj.put("amount", amountEt.getText().toString());
            createExpenseObj.put("vendor", vendorEt.getText().toString());
            createExpenseObj.put("date", dateTv.getText());
            if (recurringChk.isChecked())
                createExpenseObj.put("is_expense_recurring","1");
            else
                createExpenseObj.put("is_expense_recurring","0");

            createExpenseObj.put("recurring_frequency", getFrequency(selectedFrequency));
            createExpenseObj.put("until", utility);
            if (!recurringEndDateTv.getText().equals("End Date : "))
                createExpenseObj.put("recurring_end_date", recurringEndDateTv.getText());
            else createExpenseObj.put("recurring_end_date", "");

            // }
/*
            int radioButtonID = assignRadioGroup.getCheckedRadioButtonId();
            View radioButton = assignRadioGroup.findViewById(radioButtonID);*/
            createExpenseObj.put("project_id", projectId);
            createExpenseObj.put("client_id", clientId);


            JSONArray crateExpenseMainArray = new JSONArray();
            JSONObject createExpenseMainObject = new JSONObject();
            values = new ContentValues();
            values.put(DatabaseHelper.JSON_DATA, createExpenseObj.toString());


            if (!isEditing) {
                values.put(DatabaseHelper.TYPE, Constant.KEY_CREATE);
                db.insert(DatabaseHelper.Table_CreateOfflineExpense, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineExpense + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_CREATE + "'";
            } else {
                values.put(DatabaseHelper.TYPE, Constant.KEY_EDIT);
                db.insert(DatabaseHelper.Table_CreateOfflineExpense, values);


                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineExpense + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_EDIT + "'";
            }

            cursor = db.getRecords(selectQuery);
            JSONObject object = null;
            JSONArray createExpenseArray = new JSONArray();
            if (cursor.moveToFirst() && cursor.getCount() > 0)
                for (int i = 0; i < cursor.getCount(); i++) {

                    try {
                        object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                        createExpenseArray.put(i, object);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            if (!isEditing)
                createExpenseMainObject.put("createExpense", createExpenseArray);
            else {
                createExpenseMainObject.put("updateExpense", createExpenseArray);
            }


            if (global.isNetworkAvailable()) {
                WebRequest request = new WebRequest(context, createExpenseMainObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.CREATE, Constant.token, this, true);
                request.execute();
            } else {

                FragmentManager fm = getActivity().getSupportFragmentManager();

                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                global.showAlert("Expense is saved and it will be update when connect to internet", context);
                //fragmentChanger.onFragmentReplace(new HomePageFragment(), new Bundle());

            }


            //   int idx = assignRadioGroup.indexOfChild(radioButton);

/*
            switch (idx) {
                case R.id.project:
                    createExpenseObj.put("project_id", projectId);
                    break;
                case R.id.client:
                    createExpenseObj.put("client_id", clientId);
                    break;

            }
*/


        } catch (Exception e)

        {
            e.printStackTrace();

        }
    }


    public void setDataForEditing(JSONObject jsonResult) {
        Log.e(" expense ", jsonResult.toString());

        try {
            JSONObject jsonObject = jsonResult.getJSONObject("expense");
            expenseId = jsonObject.getString("expense_id");
            clientId = jsonObject.getString("client_id");
            categoryId = jsonObject.getString("category");
            projectId = jsonObject.getString("project_id");
            assignToProjectTV.setText("Project : " + jsonObject.getString("project_name"));
            categoryId = jsonObject.getString("expense_category_id");
            categoryTv.setText("Category : " + jsonObject.getString("expense_category_name"));
            vendorEt.setText(jsonObject.getString("vendor"));

            dateTv.setText(MyDateFormat.GetDate(jsonObject.getString("date")));
            amountEt.setText(jsonObject.getString("amount"));
            expenseNoteEt.setText(jsonObject.getString("notes"));
            TaxId1 = jsonObject.getString("tax1");
            TaxId2 = jsonObject.getString("tax2");
            tax1Tv.setText(jsonObject.getString("tax1Name"));
            tax2Tv.setText(jsonObject.getString("tax2Name"));
            if (jsonObject.getString("is_expense_recurring").equalsIgnoreCase("0"))

                recurringChk.setChecked(false);

            else
                recurringChk.setChecked(true);

            if (!jsonObject.getString("recurring_end_date").equalsIgnoreCase("0000-00-00 00:00:00"))
                recurringEndDateTv.setText(MyDateFormat.GetDate(jsonObject.getString("recurring_end_date")));
            if (jsonObject.getString("assign_to_project").equalsIgnoreCase("1"))
                assignRadioGroup.check(R.id.project);


            if (jsonObject.getString("assign_to_client").equalsIgnoreCase("1"))
                assignRadioGroup.check(R.id.client);
            staffId = jsonObject.getString("staff_id");

            if (jsonObject.getString("until").equalsIgnoreCase("1")) {
                utility = "1";
                recurringEndDateTv.setVisibility(View.VISIBLE);
                utilityTv.setText("Utility : " + utilityArray[1].toString());
            } else {
                recurringEndDateTv.setVisibility(View.GONE);
                recurringEndDateTv.setText("");
                utility = "0";
                utilityTv.setText("Utility : " + utilityArray[0].toString());
            }

            frequencyTv.setText("Frequency : " + getFrequencyFromDay(jsonObject.getString("recurring_frequency")));
            assignToClientTV.setText("Client : " + jsonObject.getString("client_name"));
            if(jsonObject.has("staff_name"))
                staffTv.setText("Staff : "+jsonObject.has("staff_name"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public String getFrequency(String recurring_frequency) {

        //{"Weekly", "2 Weeks", "3 Weeks", "4 Weeks", "Monthly", "Quarterly", "Half Yearly", "11 Month", "Yearly", "2 Years", "3 Years"};

        String freqstr = "";
        if (recurring_frequency.equalsIgnoreCase("None")) {
            return "0";
        } else if (recurring_frequency.equalsIgnoreCase("Weekly")) {
            return "7";
        } else if (recurring_frequency.equalsIgnoreCase("2 Weeks")) {
            return "14";
        } else if (recurring_frequency.equalsIgnoreCase("3 Weeks")) {
            return "21";
        } else if (recurring_frequency.equalsIgnoreCase("4 Weeks")) {
            return "28";
        } else if (recurring_frequency.equalsIgnoreCase("Monthly")) {
            return "30";
        } else if (recurring_frequency.equalsIgnoreCase("Quarterly")) {
            return "60";
        } else if (recurring_frequency.equalsIgnoreCase("Half Yearly")) {
            return "180";
        } else if (recurring_frequency.equalsIgnoreCase("11 Month")) {
            return "330";
        } else if (recurring_frequency.equalsIgnoreCase("Yearly")) {
            return "365";
        } else if (recurring_frequency.equalsIgnoreCase("2 Years")) {
            return "730";
        } else if (recurring_frequency.equalsIgnoreCase("3 Years")) {
            return "1095";
        } else {
            return "";
        }

    }

    public String getFrequencyFromDay(String recurring_frequency) {

        //{"Weekly", "2 Weeks", "3 Weeks", "4 Weeks", "Monthly", "Quarterly", "Half Yearly", "11 Month", "Yearly", "2 Years", "3 Years"};

        String freqstr = "";
        if (recurring_frequency.equalsIgnoreCase("0")) {
            return "None";
        } else if (recurring_frequency.equalsIgnoreCase("7")) {
            return "Weekly";
        } else if (recurring_frequency.equalsIgnoreCase("14")) {
            return "2 Weeks";
        } else if (recurring_frequency.equalsIgnoreCase("21")) {
            return "3 Weeks";
        } else if (recurring_frequency.equalsIgnoreCase("28")) {
            return "4 Weeks";
        } else if (recurring_frequency.equalsIgnoreCase("30")) {
            return "Monthly";
        } else if (recurring_frequency.equalsIgnoreCase("60")) {
            return "Quarterly";
        } else if (recurring_frequency.equalsIgnoreCase("180")) {
            return "Half Yearly";
        } else if (recurring_frequency.equalsIgnoreCase("330")) {
            return "11 Month";
        } else if (recurring_frequency.equalsIgnoreCase("365")) {
            return "Yearly";
        } else if (recurring_frequency.equalsIgnoreCase("730")) {
            return "2 Years";
        } else if (recurring_frequency.equalsIgnoreCase("1095")) {
            return "3 Years";
        } else {
            return "";
        }

    }
}


