package com.invoicera.ViewPagerFragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.CustomView.NoScrollListView;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.Fragment.EstimatePreviewCreateFragment;
import com.invoicera.Fragment.HomePageFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.ResultFromChildFragment;
import com.invoicera.InterFace.UpdateChargeAndTax;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.ChargesAndTaxListAdapter;
import com.invoicera.ListViewAdpter.ItemListAdapter;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Utility.Utils;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.CreateEstimateDetail;
import com.invoicera.androidapp.CreateItem;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.androidapp.SelectClient;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Parvesh on 30/7/15.
 */
public class EstimateCreateEditPagerFragment extends EstimatePreviewCreateFragment implements
        View.OnClickListener, WebApiResult, RadioGroup.OnCheckedChangeListener, UpdateChargeAndTax {

    private NoScrollListView chargesListView;
    RelativeLayout AddItemView, createEstimateNumberVew;
    // public static ArrayList<AdditionalCharge> selectedAdditionalChargeList;
    public FragmentChanger fragmentChanger;
    transient Context myContext;
    ItemListAdapter itemListAdapter;

    ArrayList<ItemModel> itemList = new ArrayList<ItemModel>();
    LinearLayout itemLiner;
    TableRow totalAfterDiscountLinear;
    public String estimate_no = "", estimate_date = "", estimate_title = "", selectQuery, schedule_on = "", estimate_note = "",
            term_condition = "", clientOrganization = "", clientName, clientId, clientAdd, clientCurrency;
    RelativeLayout discountOnTotalLinear;
    TextView addCharges, estimateNoTV, subtotalValueTV, itemTotalTaxTV, totalTV, totalAfterDiscountTV,
            netBalanceTV, estimate_dateTV, sendTV, saveTV;
    String discount_type = "Fixed", send_mail = "0";
    RadioGroup discountRadioGroup;
    ContentValues values;
    DatabaseHelper db;
    RadioButton flatButton, percentButton;
    public TextView clientTV, selectItemTV, grossTotalTV;
    /*    boolean isDiscountOnTotal, isTaxOnTotal, isPartialPayment, isTitleShow;*/
    transient Intent i;
    double totalItemTaxValue = 0, SubTotalValue, discount,  itemsTotalAmount, totalAfterDiscount, netBalance, grossTotal;
    boolean isDiscountFlat = true, isItemEditable = true;
    EditText discountEt;

    Cursor cursor;
    String estimateId;

    WebApiResult webApiResult;
    Calendar myCalendar;


    ChargesAndTaxListAdapter additionalChargesAdapter;
    boolean isEditing = false;


    int editItemPosition = 0;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.create_estimate, container, false);

        //
        isEditing = false;
        Bundle argument = getArguments();

        if (argument.getString(Constant.KEY_ESTIMATE_ID) != null) {
            if (!argument.getString(Constant.KEY_ESTIMATE_ID).isEmpty() && !argument.getString(Constant.KEY_ESTIMATE_ID).equalsIgnoreCase("null")) {
                estimateId = argument.getString(Constant.KEY_ESTIMATE_ID);
                isEditing = true;
            }
        }




        chargesListView = (NoScrollListView) view.findViewById(R.id.chargeslistView);

        selectItemTV = (TextView) view.findViewById(R.id.item_add);
        AddItemView = (RelativeLayout) view.findViewById(R.id.addItemLinear);
        AddItemView.setVisibility(View.GONE);
        createEstimateNumberVew = (RelativeLayout) view
                .findViewById(R.id.creteNumber);
        db = new DatabaseHelper(context);
        // selectedAdditionalChargeList = new ArrayList<>();
        itemListAdapter = new ItemListAdapter((Activity) context);

        estimateNoTV = (TextView) view.findViewById(R.id.number);
        estimate_dateTV = (TextView) view.findViewById(R.id.estimate_date);
        clientTV = (TextView) view.findViewById(R.id.selectClientView);
        itemList = new ArrayList<>();
        netBalanceTV = (TextView) view.findViewById(R.id.netBalance);
        webApiResult = this;


        additionalChargesAdapter = new ChargesAndTaxListAdapter(context, Constant.POP_UP.ADDITIONAL_CHARGES, Constant.SERVICE_TYPE.GET_ADDITIONAL_CHARGE, this);
        chargesListView = (NoScrollListView) view.findViewById(R.id.chargeslistView);
        chargesListView.setAdapter(additionalChargesAdapter);
        // ---
        addCharges = (TextView) view.findViewById(R.id.addCharges);
        sendTV = (TextView) view.findViewById(R.id.send);
        saveTV = (TextView) view.findViewById(R.id.save);
        sendTV.setOnClickListener(this);
        saveTV.setOnClickListener(this);
        clientTV.setOnClickListener(this);
        selectItemTV.setOnClickListener(this);
        AddItemView.setOnClickListener(this);
        addCharges.setOnClickListener(this);
        createEstimateNumberVew.setOnClickListener(this);
        itemLiner = (LinearLayout) view.findViewById(R.id.linear_item);
        itemLiner.setVisibility(View.GONE);
        totalAfterDiscountLinear = (TableRow) view.findViewById(R.id.total_after_discountLinear);
        discountRadioGroup = (RadioGroup) view.findViewById(R.id.discount);
        discountRadioGroup.setOnCheckedChangeListener(this);
        discountEt = (EditText) view.findViewById(R.id.dicountValue);
        subtotalValueTV = (TextView) view.findViewById(R.id.subtotalValue);
        itemTotalTaxTV = (TextView) view.findViewById(R.id.total_tax_on_item);
        totalTV = (TextView) view.findViewById(R.id.total);
        totalAfterDiscountTV = (TextView) view.findViewById(R.id.total_after_discount);
        discountEt = (EditText) view.findViewById(R.id.discountValue);


        grossTotalTV = (TextView) view.findViewById(R.id.grossTotal);


        myCalendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        estimate_dateTV.setText(sdf.format(myCalendar.getTime()));
        estimate_date = estimate_dateTV.getText().toString();
        flatButton = (RadioButton) view.findViewById(R.id.flat);
        percentButton = (RadioButton) view.findViewById(R.id.percent);
        discountOnTotalLinear = (RelativeLayout) view.findViewById(R.id.discountOnTotalLinear);


        discountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
/*
                if (discountEt.getText().toString().isEmpty())
                    return;
                if (totalTV.getText().toString().isEmpty())
                    return;*/
                if (!discountEt.getText().toString().isEmpty() && !totalTV.getText().toString().isEmpty()) {
                    if (!isDiscountFlat) {


                        try {
                            if (Double.parseDouble(discountEt.getText().toString()) > 100) {
                                global.showAlert("Discount % should not be greater than 100", context);
                                discountEt.setText("0.0");

                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {

                        double total = 0;


                        try {
                            total = Double.parseDouble(totalTV.getText().toString().replaceAll(",",""));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                        try {
                            if (total - Double.parseDouble(discountEt.getText().toString()) < 0 && total > 0) {
                                global.showAlert("Discount  should not be greater than total", context);
                                discountEt.setText("0.0");
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                }

                updateIem();


            }
        });

        //-----------------------------------------------------------

        selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_EstimateCreateSettings;

        cursor = db.getRecords(selectQuery);
        JSONObject object = null;
        if (cursor.moveToFirst() && cursor.getCount() > 0)
            for (int i = 0; i < cursor.getCount(); i++) {

                try {
                    object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                    estimate_no = "";
                    estimate_note = object.getString("Invoice_Notes");
                    estimateNoTV.setText(estimate_no);

                    if (object.getString("discountOnTotal").equalsIgnoreCase("No")) {
                        discountOnTotalLinear.setVisibility(View.GONE);
                        totalAfterDiscountLinear.setVisibility(View.GONE);
                    } else {
                        discountOnTotalLinear.setVisibility(View.VISIBLE);


                        totalAfterDiscountLinear.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        //------------------------------------------------------------

        if (!isEditing) {
            // get data from web ---------------------------------------------------------


            JSONObject obj = new JSONObject();

            try {
                obj.put(Constant.KEY_METHOD, "getEstimateSetting");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            WebRequest request = new WebRequest(context, obj, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_SETTING, Constant.token, this, true);
            request.execute();

            //-----------------------------------------------------------------------------------------
        }

        Home.toolbarText.setText("Estimate");

        if (argument.getString(Constant.KEY_CLIENT_ID) != null) {

            clientAdd = argument.getString(Constant.KEY_ADDRESS);  //map.get(Constant.KEY_ADDRESS);
            clientName = argument.getString(Constant.KEY_CLIENT_NAME);
            clientOrganization = argument.getString(Constant.KEY_ORGANIZATION);
            clientId = argument.getString(Constant.KEY_CLIENT_ID);
            clientCurrency = argument.getString(Constant.KEY_CLIENT_CURRENCY);
            clientTV.setText(clientOrganization);
            totalAfterDiscountTV.setText(Utils.FloatToStringLimits(totalAfterDiscount));


        }

        //

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments=getArguments();
        if(arguments.get(Constant.KEY_CLIENT_ID)!=null){
            clientAdd = arguments.getString(Constant.KEY_ADDRESS);
            clientName = arguments.getString(Constant.KEY_CLIENT_NAME);
            clientOrganization = arguments.getString(Constant.KEY_ORGANIZATION);
            clientId = arguments.getString(Constant.KEY_CLIENT_ID);
            clientCurrency =arguments.getString(Constant.KEY_CLIENT_CURRENCY);
            clientTV.setText(clientOrganization);
            totalAfterDiscountTV.setText(Utils.FloatToStringLimits(totalAfterDiscount));
            resetAllView();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentChanger = (FragmentChanger) getActivity();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        boolean canSendEstimate = false;
        switch (v.getId()) {
            case R.id.selectClientView:

                if (isEditing) {
                    return;

                }

              /*  SelectClient myf = new SelectClient();
                itemList = new ArrayList<>();
                updateIem();
                getChildFragmentManager().beginTransaction().replace(R.id.mainframe, myf).commit();
                getView().findViewById(R.id.mainframe).setVisibility(View.VISIBLE);*/

                i = new Intent(context,
                        SelectClient.class);
                getActivity().startActivityForResult(i,
                        Constant.requestCodeSelectClient);

                break;

            case R.id.item_add:

            case R.id.addItemLinear:


                myContext = getActivity().getApplicationContext();

                i = new Intent(myContext, CreateItem.class);
                if (clientCurrency != null) {
                    i.putExtra(Constant.KEY_CLIENT_CURRENCY, clientCurrency);
                    i.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateEstimateItem);
                    getActivity().startActivityForResult(i,
                            Constant.requestCodeCreateEstimateItem);
                    selectItemTV.setVisibility(View.GONE);

                    AddItemView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Please select client", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.creteNumber:
                Intent intent = new Intent(context, CreateEstimateDetail.class);
                intent.putExtra(Constant.KEY_NO, estimate_no);
                intent.putExtra(Constant.KEY_DATE, estimate_date);
                intent.putExtra(Constant.KEY_TITLE, estimate_title);

                intent.putExtra(Constant.KEY_SCHEDUAL_DATE, schedule_on);
                intent.putExtra(Constant.KEY_NOTE, estimate_note);
                intent.putExtra(Constant.KEY_TermsAndCondition, term_condition);

                intent.putExtra(Constant.KEY_EDITING, isEditing);
                intent.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateEstimateDetail);
                getActivity().startActivityForResult(intent, Constant.requestCodeCreateEstimateDetail);
                break;
            case R.id.addCharges:
                if (itemList.isEmpty()) {
                    global.showAlert("Please select item before applying additional charges", context);
                    return;

                }
                additionalChargesAdapter.add(new HashMap<String, String>());

                break;


            case R.id.save:
                canSendEstimate = false;
                send_mail = "0";
                if (clientId != null)
                    if (!clientId.isEmpty())
                        canSendEstimate = true;

                if (canSendEstimate)
                    createEstimate();
                else global.showAlert("Please select client", context);
                break;
            case R.id.send:

                canSendEstimate = false;
                send_mail = "1";
                if (clientId != null)
                    if (!clientId.isEmpty())
                        canSendEstimate = true;
                if (canSendEstimate)
                    createEstimate();
                else global.showAlert("Please select client", context);
                break;

            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == Constant.requestCodeCreateEstimateItem) {
            ItemModel model = (ItemModel) data.getParcelableExtra(Constant.KEY_ITEM);
            if (data.hasExtra(Constant.KEY_EDITING)) {

                if (data.getBooleanExtra(Constant.KEY_EDITING, false))
                    itemList.set(editItemPosition, model);
                else itemList.add(model);


            } else itemList.add(model);
            updateIem();

        }

        if (requestCode == Constant.requestCodeCreateEstimateDetail) {
            estimate_no = data.getStringExtra(Constant.KEY_NO);

            estimate_date = data.getStringExtra(Constant.KEY_DATE);

            estimate_title = data.getStringExtra(Constant.KEY_TITLE);
            estimate_note = data.getStringExtra(Constant.KEY_NOTE);

            term_condition = data.getStringExtra(Constant.KEY_TermsAndCondition);
            schedule_on = data.getStringExtra(Constant.KEY_SCHEDUAL_DATE);

            estimateNoTV.setText(estimate_no);
            estimate_dateTV.setText(estimate_date);

        }

        if (requestCode == Constant.requestCodeSelectClient) {

            HashMap<String, String> map = (HashMap) data.getSerializableExtra(Constant.KEY_CLIENT);
            if (map != null) {


                clientAdd = map.get(Constant.KEY_ADDRESS);
                clientName = map.get(Constant.KEY_CLIENT_NAME);
                clientOrganization = map.get(Constant.KEY_ORGANIZATION);
                clientId = map.get(Constant.KEY_CLIENT_ID);
                clientCurrency = map.get(Constant.KEY_CLIENT_CURRENCY);
                clientTV.setText(clientOrganization);
                totalAfterDiscountTV.setText(Utils.FloatToStringLimits(totalAfterDiscount));
                resetAllView();
            }
        }


    }


    public void updateIem() {

        itemLiner.removeAllViews();
        ItemModel model;
        SubTotalValue = 0;
        totalItemTaxValue = 0;
        itemsTotalAmount = 0;
        totalAfterDiscount = 0;
        discount = 0;
        netBalance = 0;

        if (itemList.isEmpty()) {

            itemLiner.setVisibility(View.GONE);
            selectItemTV.setVisibility(View.VISIBLE);
            AddItemView.setVisibility(View.GONE);
            subtotalValueTV.setText(Utils.FloatToStringLimits(SubTotalValue));
            itemTotalTaxTV.setText(Utils.FloatToStringLimits(totalItemTaxValue));
            totalTV.setText(Utils.FloatToStringLimits(itemsTotalAmount));
            netBalanceTV.setText(Utils.FloatToStringLimits(netBalance));
            grossTotalTV.setText(0 + "");
            return;
        }

        for (int i = 0; i < itemList.size(); i++)

        {
            model = itemList.get(i);
            itemLiner.setVisibility(View.VISIBLE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemRow = inflater.inflate(R.layout.create_item_row, null);

            ImageView crossImage = (ImageView) itemRow.findViewById(R.id.cross);
            crossImage.setTag(i);
            crossImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isItemEditable)
                        return;

                    int pos = (int) v.getTag();
                    itemList.remove(pos);
                    updateIem();
                }
            });

            TextView itemName = (TextView) itemRow.findViewById(R.id.itemName);
            TextView qtyAndCost = (TextView) itemRow.findViewById(R.id.qty_uc);
            TextView discountValue = (TextView) itemRow.findViewById(R.id.discount);
            TextView totalValue = (TextView) itemRow.findViewById(R.id.total);
            itemName.setText(model.getName());


            qtyAndCost.setText("QTY : " + global.setLength(Utils.FloatToStringLimits(Double.parseDouble(model.getQuantity().replaceAll(",", ""))), Constant.defultLengthOfText) + "\nUnit Cost : " + global.setLength(Utils.FloatToStringLimits(Double.parseDouble(model.getUnit_cost().replaceAll(",", ""))), Constant.defultLengthOfText));

            if (model.getDiscount_type().equalsIgnoreCase("Percent")) {
                discountValue.setText("Discount : " + global.setLength(model.getDiscountValue(), Constant.defultLengthOfText) + "%");
            } else
                discountValue.setText("Discount : " + global.setLength(model.getDiscountValue(), Constant.defultLengthOfText));

            totalValue.setText(clientCurrency + " " + global.setLength(Utils.FloatToStringLimits(model.getTotalValue()), Constant.defultLengthOfText));

            try {
                double qty = 0, unitCost = 0, discountAmount = 0, tax1Value = 0, tax2Value = 0;

                qty = 0;
                unitCost = 0;
                discountAmount = 0;
                tax1Value = 0;
                tax2Value = 0;
                if (!model.getQuantity().toString().isEmpty())
                    qty = Double.parseDouble(model.getQuantity().toString().replaceAll(",", ""));

                if (!model.getUnit_cost().toString().isEmpty())
                    unitCost = Double.parseDouble(model.getUnit_cost().toString().replaceAll(",", ""));

                if (!model.getDiscountAmount().toString().isEmpty())
                    discountAmount = Double.parseDouble(model.getDiscountAmount().toString().replaceAll(",", ""));

                if (!model.getTax1_percent().isEmpty())
                    tax1Value = Double.parseDouble(model.getTax1_percent());

                if (!model.getTax2_percent().isEmpty())
                    tax2Value = Double.parseDouble(model.getTax2_percent());


                if (!model.getTax1Id().isEmpty()) {


                    totalItemTaxValue = totalItemTaxValue + (((qty * unitCost) - discountAmount) * tax1Value / 100);

                }

                if (!model.getTax2Id().isEmpty()) {

                    if (model.getTax2Type().equalsIgnoreCase("Compound"))
                        totalItemTaxValue = totalItemTaxValue + (((((qty * unitCost) - discountAmount) * tax1Value / 100) + ((qty * unitCost) - discountAmount)) * tax2Value / 100);
                    else
                        totalItemTaxValue = totalItemTaxValue + (((qty * unitCost) - discountAmount) * tax2Value / 100);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                SubTotalValue = SubTotalValue + Double.parseDouble(model.getTotalValue().replaceAll(",", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            itemRow.setTag(i);
            itemRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isItemEditable)
                        return;
                    myContext = getActivity().getApplicationContext();

                    Intent i = new Intent(myContext, CreateItem.class);
                    if (clientCurrency != null) {
                        i.putExtra(Constant.KEY_CLIENT_CURRENCY, clientCurrency);
                        i.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateEstimateItem);

                        i.putExtra(Constant.KEY_ITEM, itemList.get(Integer.parseInt(v.getTag().toString())));
                        selectItemTV.setVisibility(View.GONE);

                        editItemPosition = Integer.parseInt(v.getTag().toString());
                        AddItemView.setVisibility(View.VISIBLE);

                        getActivity().startActivityForResult(i,
                                Constant.requestCodeCreateEstimateItem);
                    } else {
                        Toast.makeText(context, "Please select client", Toast.LENGTH_SHORT).show();

                    }


                }
            });


            itemLiner.addView(itemRow);


        }


        subtotalValueTV.setText(global.setLength(Utils.FloatToStringLimits(SubTotalValue), Constant.defultLengthOfText));


        itemTotalTaxTV.setText(global.setLength(Utils.FloatToStringLimits(totalItemTaxValue), Constant.defultLengthOfText));

        itemsTotalAmount = (SubTotalValue + totalItemTaxValue);


        totalTV.setText(global.setLength(Utils.FloatToStringLimits(itemsTotalAmount), Constant.defultLengthOfText));

        if (isDiscountFlat) {
            try {

                if (!discountEt.getText().toString().isEmpty())
                    discount = Double.parseDouble(discountEt.getText().toString().replaceAll(",", ""));
                else discount = 0;
            } catch (NumberFormatException e) {
                discount = 0;
                e.printStackTrace();
            }
        } else {
            try {

                if (!discountEt.getText().toString().isEmpty())
                    discount = (itemsTotalAmount) * Double.parseDouble(discountEt.getText().toString().replaceAll(",", "")
                    ) / 100;
                else discount = 0;
            } catch (NumberFormatException e) {
                discount = 0;
                e.printStackTrace();
            }


        }
        totalAfterDiscount = itemsTotalAmount - discount;

        if (totalAfterDiscount < 0) {

            discountEt.setText("0");
            global.showAlert("Discount  should not be greater than total ", context);
            updateIem();
        }

        totalAfterDiscountTV.setText(global.setLength(Utils.FloatToStringLimits(totalAfterDiscount), Constant.defultLengthOfText));


        if (discount == 0)
            totalAfterDiscountLinear.setVisibility(View.GONE);
        else
            totalAfterDiscountLinear.setVisibility(View.VISIBLE);

        netBalance = totalAfterDiscount;


        //add  additional charges

        if (!additionalChargesAdapter.selectedItemList.isEmpty())
            for (int i = 0; i < additionalChargesAdapter.selectedItemList.size(); i++) {

                if (additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_NAME) != null)
                    if (!additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_NAME).isEmpty()) {


                        try {
                            if (!additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_TYPE).equalsIgnoreCase("Fixed")) {


                                netBalance = netBalance + (itemsTotalAmount * (Double.parseDouble(additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE).replaceAll(",", ""))) / 100);


                            } else {

                                netBalance = netBalance + Double.parseDouble(additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE).replaceAll(",", ""));
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                    }

            }

        netBalanceTV.setText(global.setLength(Utils.FloatToStringLimits(netBalance), Constant.defultLengthOfText));


        // add tax


        grossTotal = netBalance;


        grossTotalTV.setText(global.setLength(Utils.FloatToStringLimits(grossTotal), Constant.defultLengthOfText));


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.flat:
                isDiscountFlat = true;
                discountEt.setText("0.0");
                discount_type = "Fixed";
                break;
            case R.id.percent:
                discount_type = "Percent";
                isDiscountFlat = false;
                discountEt.setText("0.0");
                break;

        }

    }


    @Override
    public void UpdateChargeAndTaxValue(Constant.POP_UP type) {
        /*updateIem();*/

        netBalance = totalAfterDiscount;
        switch (type) {


            case ADDITIONAL_CHARGES:
                updateIem();
                break;


        }


    }


    private void resetAllView() {


        itemList = new ArrayList<>();
        discountEt.setText(0 + "");
        additionalChargesAdapter.selectedItemList = new ArrayList<>();

        updateIem();


    }


    public void setDataForEditing(String estimateId) {


        if (db == null)
            return;

        JSONObject object = null;
        //---------------Load data from local
        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_EstimatePreviewData + " WHERE " + DatabaseHelper.ID + " ='" + estimateId + "'";
        Cursor cursor = db.getRecords(selectQuery);


        if (cursor.moveToFirst() && cursor.getCount() > 0) {


            //totalItem = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {

                try {

                    object = (new JSONObject
                            (cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)))).getJSONObject("estimate");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
        if (object == null)
            return;


        try {
            estimate_no = object.getString("number");
            estimate_title = object.getString("estimate_title");
            estimate_note = object.getString("notes");
            term_condition = object.getString("terms");
            estimate_date = MyDateFormat.GetDate(object.getString("date"));


            if (!object.getString("estimate_schedule_date").equalsIgnoreCase("0000-00-00 00:00:00"))
                schedule_on = MyDateFormat.GetDate(object.getString("estimate_schedule_date"));
            else schedule_on = "";


            estimateNoTV.setText(estimate_no);
            estimate_dateTV.setText(estimate_date);
            isItemEditable = true;


            clientAdd = object.getJSONObject("client").getString("address");

            clientCurrency = object.getJSONObject("client").getString("currency");

            clientId = object.getJSONObject("client").getString("client_id");
            clientName = object.getJSONObject("client").getString("client_name");
            if (object.getJSONObject("client").has("organization"))
                clientOrganization = object.getJSONObject("client").getString("organization");
            clientTV.setText(clientName);
            itemList = new ArrayList<>();


            //set item

            for (int i = 0; i < object.getJSONObject("items").getJSONArray("item").length(); i++) {

                JSONObject itemObj = object.getJSONObject("items").getJSONArray("item").getJSONObject(i);
                ItemModel model = new ItemModel();
                model.setId(itemObj.getString("item_id"));
                model.setType(itemObj.getString("type"));
                model.setName(itemObj.getString("name"));
                model.setDescription(itemObj.getString("description"));
                model.setUnit_cost(itemObj.getString("unit_cost"));
                model.setQuantity(itemObj.getString("quantity"));
                model.setDiscountValue(itemObj.getString("discount"));
                model.setDiscount_type(itemObj.getString("discount_type"));
                model.setTax1_name(itemObj.getString("tax1_name"));
                model.setTax2_name(itemObj.getString("tax2_name"));
                model.setTax1_percent(itemObj.getString("tax1_percent"));
                model.setTax2_percent(itemObj.getString("tax2_percent"));
                model.setTax1Id(itemObj.getString("tax1_id"));
                model.setTax2Id(itemObj.getString("tax2_id"));
                model.setTax1Type(itemObj.getString("tax1_type"));
                model.setTax2Type(itemObj.getString("tax2_type"));


                model.setTotalValue(itemObj.getString("total_priceValue"));
                model.setDiscountAmount(itemObj.getString("total_discountValue"));

                double tax1 = 0, tax2 = 0;

                try {
                    tax1 = Double.parseDouble(itemObj.getString("tax1_value").replaceAll(",", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    tax2 = Double.parseDouble(itemObj.getString("tax2_value").replaceAll(",", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // model.setTotalTAx((tax1 + tax2) + "");
                itemList.add(model);
            }

            isDiscountFlat = true;

            if (object.getString("EstimateWiseDiscountType").equalsIgnoreCase("Percent")) {

                isDiscountFlat = false;
                flatButton.setChecked(false);
                percentButton.setChecked(true);

            }

            discountEt.setText(object.getString("EstimateWiseDiscountValue"));


            // addtional value

            additionalChargesAdapter.selectedItemList = new ArrayList<>();
            for (int i = 0; i < object.getJSONObject("additional_charges").getJSONArray("charge").length(); i++) {

                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.KEY_NAME, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("name"));
                map.put(Constant.KEY_TYPE, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("type"));
                map.put(Constant.KEY_VALUE, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("amount"));
                additionalChargesAdapter.add(map);

            }
          //  additionalChargesAdapter.notifyDataSetChanged();


            if (!itemList.isEmpty()) {
                selectItemTV.setVisibility(View.GONE);
                AddItemView.setVisibility(View.VISIBLE);

            }
            updateIem();
            ;

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("rawtypes")
    private void createEstimate() {

        try {
            JSONObject CreateEstimate = new JSONObject();

            JSONObject obj1 = null;
            JSONArray itemsarray = new JSONArray();
            try {
                for (int i = 0; i < itemList.size(); i++) {
                    obj1 = new JSONObject();
                    ItemModel item = itemList.get(i);

                    obj1.put("name", item.getName());
                    obj1.put("type", item.getType());
                    obj1.put("description", item.getDescription());

                    try {
                        obj1.put("quantity",
                                Double.parseDouble(item.getQuantity().replaceAll(",", "")) + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        obj1.put("quantity", item.getQuantity());
                    }

                    try {
                        obj1.put("unit_cost", Double.parseDouble(item.getUnit_cost().replaceAll(",", "")) + "");
                    } catch (Exception Ex) {
                        obj1.put("unit_cost", item.getUnit_cost());
                    }
                    obj1.put("discount", item.getDiscountValue());
                    obj1.put("discount_type", item.getDiscount_type());
                    obj1.put("tax1_id", item.getTax1Id());
                    obj1.put("tax2_id", item.getTax2Id());
                    itemsarray.put(obj1);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONObject item = new JSONObject();
            item.put("item", itemsarray);

            JSONArray chargesarray = new JSONArray();


            for (int i = 0; i < additionalChargesAdapter.selectedItemList.size(); i++) {
                obj1 = new JSONObject();
                obj1.put("name", additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_NAME));
                obj1.put("type", additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_TYPE));
                obj1.put("amount", additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE));
                obj1.put("id", additionalChargesAdapter.selectedItemList.get(i).get(Constant.KEY_ID));
                chargesarray.put(obj1);

            }
            JSONObject charge = new JSONObject();
            charge.put("additional_charge", chargesarray);


            JSONArray paymentsgatewayarray = new JSONArray();
            String isPartialPayment;


            JSONObject client = new JSONObject();

            client.put("client_id", clientId);

            JSONObject Jdiscount = new JSONObject();
            Jdiscount.put("type", discount_type);
            Jdiscount.put("amount", discountEt.getText().toString());

            CreateEstimate.put("payment_gateway_ids", paymentsgatewayarray);
            CreateEstimate.put("client", client);
            CreateEstimate.put("discount", Jdiscount);


            CreateEstimate.put("send_mail", send_mail);

            CreateEstimate.put("estimate_title", estimate_title);

            //CreateEstimate.put(GlobalVariables.TAG_CREATED_BY, ConstantList.userId);

            CreateEstimate.put("number", estimate_no);
            CreateEstimate.put("date", estimate_date);

            CreateEstimate.put("schedule_date", schedule_on);
            CreateEstimate.put("payment_term", "");

            CreateEstimate.put("status", "");
            CreateEstimate.put("notes", estimate_note);
            CreateEstimate.put("terms", term_condition);
            CreateEstimate.put("items", item);

            CreateEstimate.put("additional_charges", charge);

            if (!isEditing) {
                CreateEstimate.put("method", "createEstimate");
            } else {
                CreateEstimate.put("estimate_id", estimateId);
                CreateEstimate.put("method", "updateEstimate");
            }


            JSONObject CreateEstimateMainObj = new JSONObject();
            JSONArray CreateEstimateArray = new JSONArray();


            //save request in database

            values = new ContentValues();
            values.put(DatabaseHelper.JSON_DATA, CreateEstimate.toString());

            if (!isEditing) {
                values.put(DatabaseHelper.TYPE, Constant.KEY_CREATE);
                db.insert(DatabaseHelper.Table_CreateOfflineEstimate, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineEstimate + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_CREATE + "'";
            } else {
                values.put(DatabaseHelper.TYPE, Constant.KEY_EDIT);
                db.insert(DatabaseHelper.Table_CreateOfflineEstimate, values);


                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineEstimate + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_EDIT + "'";
            }


            cursor = db.getRecords(selectQuery);
            JSONObject object = null;
            if (cursor.moveToFirst() && cursor.getCount() > 0)
                for (int i = 0; i < cursor.getCount(); i++) {

                    try {
                        object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                        CreateEstimateArray.put(i, object);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            if (!isEditing)
                CreateEstimateMainObj.put("createEstimate", CreateEstimateArray);
            else {
                CreateEstimateMainObj.put("updateEstimate", CreateEstimateArray);
            }


            if (global.isNetworkAvailable()) {
                WebRequest request = new WebRequest(context, CreateEstimateMainObj, Constant.invoicelistURL,
                        Constant.SERVICE_TYPE.CREATE, Constant.token, this, true);
                request.execute();
            } else {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                global.showAlert("Estimate is saved and it will be update when connect to internet", context);
                fragmentChanger.onFragmentReplace(new HomePageFragment(), new Bundle());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {
/*
        Home.progressBarHome.setVisibility(View.GONE);*/
        if (result == null)
            return;
        try {
            if (type == Constant.SERVICE_TYPE.CREATE)
                db.ClearTable(DatabaseHelper.Table_CreateOfflineEstimate);


            if (!result.getString("code").equalsIgnoreCase("200")) {
                if (type == Constant.SERVICE_TYPE.CREATE)
                    db.ClearTable(DatabaseHelper.Table_CreateOfflineEstimate);

                if (result.has("message")) {
                    Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_SHORT).show();

                    return;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject object;
        boolean updatePopUp = false;
        switch (type) {

            case GET_SETTING:
                db.ClearTable(DatabaseHelper.Table_EstimateCreateSettings);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_EstimateCreateSettings, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_EstimateCreateSettings;

                cursor = db.getRecords(selectQuery);
                object = null;
                if (cursor.moveToFirst() && cursor.getCount() > 0)
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                            estimate_no = object.getString("estimate_no");
                            estimate_note = object.getString("Invoice_Notes");
                            term_condition = object.getString("UserCompany_Terms");
                            estimateNoTV.setText(estimate_no);


                            if (object.getString("discountOnTotal").equalsIgnoreCase("No")) {
                                discountOnTotalLinear.setVisibility(View.GONE);
                                totalAfterDiscountLinear.setVisibility(View.GONE);
                            } else {
                                discountOnTotalLinear.setVisibility(View.VISIBLE);


                                totalAfterDiscountLinear.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                break;


            case CREATE:
                Log.e("**************1", "*******");
                db.ClearTable(DatabaseHelper.Table_EstimatePreviewData);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                try {
                    estimateId = result.getJSONObject("estimate").getString("estimate_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                values.put(DatabaseHelper.ID, estimateId);
                db.insert(DatabaseHelper.Table_EstimatePreviewData, values);

                Bundle arguments = new Bundle();

                arguments.putString(Constant.KEY_ESTIMATE_ID, estimateId);
                arguments.putInt(Constant.KEY_POSITION, 1);

                if (global.isNetworkAvailable()) {


                    fragmentChanger.onFragmentReplaceWithBackStack(new EstimatePreviewCreateFragment(), Constant.EstimatePreviewCreateFragmentTag, arguments);
                }


                break;


        }
    }


}




