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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.invoicera.Fragment.HomePageFragment;
import com.invoicera.Fragment.InvoicePreviewCreateFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.ResultFromChildFragment;
import com.invoicera.InterFace.UpdateChargeAndTax;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.ChargesAndTaxListAdapter;
import com.invoicera.ListViewAdpter.ItemListAdapter;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Utility.Utils;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.CreateInvoiceDetail;
import com.invoicera.androidapp.CreateItem;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.androidapp.SelectPaymentGateWay;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class InvoiceCreateEditPagerFragment extends InvoicePreviewCreateFragment implements
        OnClickListener, WebApiResult, RadioGroup.OnCheckedChangeListener, UpdateChargeAndTax {

    private NoScrollListView chargesListView, taxListView;
    RelativeLayout AddItemView, createInvoiceNumberVew;
    // public static ArrayList<AdditionalCharge> selectedAdditionalChargeList;
    public FragmentChanger fragmentChanger;
    transient Context myContext;
    boolean isPartialPaid = false;
    ItemListAdapter itemListAdapter;
    transient Bundle argument;
    transient ResultFromChildFragment fromChildFragment;
    ArrayList<ItemModel> itemList = new ArrayList<ItemModel>();
    LinearLayout itemLiner;
    TableRow taxOnTotalLinear, outStandingLinear, lateFeeLinear, allowPartialPaymentLinear, totalAfterDiscountLinear;
    public String invoice_no = "", invoice_date = "", invoice_title = "", selectQuery, schedule_on = "", late_feeId = "", due_date = "", invoice_note = "", term_condition = "", lateFeeName = "", clientOrganization = "", clientName, clientId, clientAdd, clientCurrency;
    LinearLayout discountOnTotalLinear;
    TextView addCharges, invoiceNoTV, lateFeeTV, outStandingTV, subtotalValueTV, itemTotalTaxTV, totalTV, totalAfterDiscountTV, addTax, netBalanceTV, invoice_dateTV, sendTV, saveTV, amountPaidTV;
    String discount_type = "Fixed", send_mail = "0";
    RadioGroup discountRadioGroup;
    ContentValues values;
    DatabaseHelper db;
    RadioButton flatButton, percentButton;
    public TextView clientTV, selectItemTV, grossTotalTV, paymentGatewayTextView, duedaysTV;
    /*    boolean isDiscountOnTotal, isTaxOnTotal, isPartialPayment, isTitleShow;*/
    transient Intent i;
    double totalItemTaxValue = 0, SubTotalValue, discount, outstandingAmount = 0, itemsTotalAmount, totalAfterDiscount, netBalance, grossTotal, totalPaid = 0, lateFeeAmount = 0;
    boolean isDiscountFlat = true, isItemEditable = true;
    EditText discountEt;
    CheckBox AllowPartialPaymentCheckBox;
    ArrayList<HashMap<String, String>> selectedPaymentGatewayListList;
    Cursor cursor;
    String invoiceId;

    WebApiResult webApiResult;
    Calendar myCalendar;

    HashMap<Integer, Integer> selectedTax;
    ChargesAndTaxListAdapter additionalChargesAdapter, taxListAdapter;
    boolean isEditing = false;
    TableRow amountPaidRow;

    int editItemPosition = 0;


/*
    "code":"200",
            "estimate_no":"ram262",
            "UserCompany_Terms":"vnvnvnv",
            "Invoice_Notes":"bvcbvvb",
            "discountOnTotal":"No",
            "taxOnTotal":"No",
            "isAddCredit":"No",
            "isPartialAmount":"N",
            "isTitleShowing":"No"
*/


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.create_invoice, container, false);

        //
        isEditing = false;
        Bundle argument = getArguments();

        if (argument.getString(Constant.KEY_INVOICE_ID) != null) {
            if (!argument.getString(Constant.KEY_INVOICE_ID).isEmpty() && !argument.getString(Constant.KEY_INVOICE_ID).equalsIgnoreCase("null")) {
                invoiceId = argument.getString(Constant.KEY_INVOICE_ID);
                isEditing = true;
            }
        }


        chargesListView = (NoScrollListView) view.findViewById(R.id.chargeslistView);
        taxListView = (NoScrollListView) view.findViewById(R.id.taxlistView);
        selectItemTV = (TextView) view.findViewById(R.id.item_add);
        AddItemView = (RelativeLayout) view.findViewById(R.id.addItemLinear);
        AddItemView.setVisibility(View.GONE);
        createInvoiceNumberVew = (RelativeLayout) view
                .findViewById(R.id.creteNumber);
        db = new DatabaseHelper(context);
        // selectedAdditionalChargeList = new ArrayList<>();
        itemListAdapter = new ItemListAdapter((Activity) context);

        invoiceNoTV = (TextView) view.findViewById(R.id.number);
        invoice_dateTV = (TextView) view.findViewById(R.id.invoice_date);
        clientTV = (TextView) view.findViewById(R.id.selectClientView);
        itemList = new ArrayList<>();
        netBalanceTV = (TextView) view.findViewById(R.id.netBalance);
        webApiResult = this;
        duedaysTV = (TextView) view.findViewById(R.id.dueDays);
        selectedPaymentGatewayListList = new ArrayList<>();
        additionalChargesAdapter = new ChargesAndTaxListAdapter(context, Constant.POP_UP.ADDITIONAL_CHARGES, Constant.SERVICE_TYPE.GET_ADDITIONAL_CHARGE, this);
        taxListAdapter = new ChargesAndTaxListAdapter(context, Constant.POP_UP.TAX, Constant.SERVICE_TYPE.GET_TAX_LIST, this);
        taxListView.setAdapter(taxListAdapter);
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
        createInvoiceNumberVew.setOnClickListener(this);
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
        addTax = (TextView) view.findViewById(R.id.addTax);
        addTax.setOnClickListener(this);
        selectedTax = new HashMap<>();
        grossTotalTV = (TextView) view.findViewById(R.id.grossTotal);
        AllowPartialPaymentCheckBox = (CheckBox) view.findViewById(R.id.allowPartialPayment);
        paymentGatewayTextView = (TextView) view.findViewById(R.id.paymentGateWay);
        paymentGatewayTextView.setOnClickListener(this);

        myCalendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        invoice_dateTV.setText(sdf.format(myCalendar.getTime()));
        invoice_date = invoice_dateTV.getText().toString();
        flatButton = (RadioButton) view.findViewById(R.id.flat);
        percentButton = (RadioButton) view.findViewById(R.id.percent);
        discountOnTotalLinear = (LinearLayout) view.findViewById(R.id.discountOnTotalLinear);
        allowPartialPaymentLinear = (TableRow) view.findViewById(R.id.allowPartialPaymentLinear);
        taxOnTotalLinear = (TableRow) view.findViewById(R.id.taxOnTotalLinear);
        amountPaidRow = (TableRow) view.findViewById(R.id.amountPaidLiner);
        amountPaidTV = (TextView) view.findViewById(R.id.amountPaid);
        lateFeeLinear = (TableRow) view.findViewById(R.id.lateFeeLinear);
        lateFeeTV = (TextView) view.findViewById(R.id.lateFee);
        outStandingTV = (TextView) view.findViewById(R.id.outStanding);
        outStandingLinear = (TableRow) view.findViewById(R.id.outStandingLinear);


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
                            total = Double.parseDouble(totalTV.getText().toString());
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
                + " From " + DatabaseHelper.Table_InvoiceCreateSettings;

        cursor = db.getRecords(selectQuery);
        JSONObject object = null;
        if (cursor.moveToFirst() && cursor.getCount() > 0)
            for (int i = 0; i < cursor.getCount(); i++) {

                try {
                    object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                    invoice_no = "";
                    invoice_note = object.getString("Invoice_Notes");
                    invoiceNoTV.setText(invoice_no);

                    if (object.getString("discountOnTotal").equalsIgnoreCase("No")) {
                        discountOnTotalLinear.setVisibility(View.GONE);
                        totalAfterDiscountLinear.setVisibility(View.GONE);
                    } else {
                        discountOnTotalLinear.setVisibility(View.VISIBLE);


                        totalAfterDiscountLinear.setVisibility(View.VISIBLE);
                    }


                    if (object.getString("taxOnTotal").equalsIgnoreCase("No"))
                        taxOnTotalLinear.setVisibility(View.GONE);

                    else taxOnTotalLinear.setVisibility(View.VISIBLE);


                    if (object.getString("isPartialAmount").equalsIgnoreCase("No"))
                        allowPartialPaymentLinear.setVisibility(View.GONE);
                    else allowPartialPaymentLinear.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        //------------------------------------------------------------

        if (!isEditing) {
            // get data from web ---------------------------------------------------------


            JSONObject obj = new JSONObject();

            try {
                obj.put(Constant.KEY_METHOD, "getInvoiceSetting");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            WebRequest request = new WebRequest(context, obj, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_SETTING, Constant.token, this, true);
            request.execute();

            //-----------------------------------------------------------------------------------------
        }
        // else
        //    setDataForEditing(invoiceId);

        // come from client list module
        Home.toolbarText.setText("Invoice");

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
    public void onResume() {
        super.onResume();
        Home.toolbarText.setText("Invoice");
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
        boolean canSendInvoice = false;
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

                i = new Intent(context, com.invoicera.androidapp.SelectClient.class);
                getActivity().startActivityForResult(i,
                        Constant.requestCodeSelectClient);

                break;

            case R.id.item_add:

            case R.id.addItemLinear:

                if (!isItemEditable) {
                    global.showAlert("Invoice is paid you can not add more item", context);
                    return;

                }


                myContext = getActivity().getApplicationContext();

                i = new Intent(myContext, CreateItem.class);
                if (clientCurrency != null) {
                    i.putExtra(Constant.KEY_CLIENT_CURRENCY, clientCurrency);
                    i.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateInvoiceItem);
                    getActivity().startActivityForResult(i,
                            Constant.requestCodeCreateInvoiceItem);
                    selectItemTV.setVisibility(View.GONE);

                    AddItemView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Please select client", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.creteNumber:
                Intent intent = new Intent(context, CreateInvoiceDetail.class);
                intent.putExtra(Constant.KEY_NO, invoice_no);
                intent.putExtra(Constant.KEY_DATE, invoice_date);
                intent.putExtra(Constant.KEY_TITLE, invoice_title);
                intent.putExtra(Constant.KEY_DUE_DATE, due_date);
                intent.putExtra(Constant.KEY_SCHEDUAL_DATE, schedule_on);
                intent.putExtra(Constant.KEY_NOTE, invoice_note);
                intent.putExtra(Constant.KEY_TermsAndCondition, term_condition);
                intent.putExtra(Constant.KEY_LATE_FEE_ID, late_feeId);
                intent.putExtra(Constant.KEY_LATE_FEE_NAME, lateFeeName);
                intent.putExtra(Constant.KEY_EDITING, isEditing);
                intent.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateInvoiceDetail);
                getActivity().startActivityForResult(intent, Constant.requestCodeCreateInvoiceDetail);
                break;
            case R.id.addCharges:
                if (itemList.isEmpty()) {
                    global.showAlert("Please select item before applying additional charges", context);
                    return;

                }
                additionalChargesAdapter.add(new HashMap<String, String>());

                break;

            case R.id.addTax:
                if (itemList.isEmpty()) {
                    global.showAlert("Please select item before applying additional charges", context);
                    return;

                }
                taxListAdapter.add(new HashMap<String, String>());

                break;

            case R.id.paymentGateWay:
                myContext = getActivity().getApplicationContext();
                i = new Intent(myContext, SelectPaymentGateWay.class);
                i.putExtra(Constant.KEY_PAYMENT_GATEWAY, selectedPaymentGatewayListList);
                getActivity().startActivityForResult(i,
                        Constant.requestCodeSelectPaymentGateway);

                break;


            case R.id.save:
                canSendInvoice = false;
                send_mail = "0";
                if (clientId != null)
                    if (!clientId.isEmpty())
                        canSendInvoice = true;

                if (canSendInvoice)
                    createInvoice();
                else global.showAlert("Please select client", context);
                break;
            case R.id.send:

                canSendInvoice = false;
                send_mail = "1";
                if (clientId != null)
                    if (!clientId.isEmpty())
                        canSendInvoice = true;
                if (canSendInvoice)
                    createInvoice();
                else global.showAlert("Please select client", context);
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == Constant.requestCodeCreateInvoiceItem) {
            ItemModel model = (ItemModel) data.getParcelableExtra(Constant.KEY_ITEM);
            if (data.hasExtra(Constant.KEY_EDITING)) {

                if (data.getBooleanExtra(Constant.KEY_EDITING, false))
                    itemList.set(editItemPosition, model);
                else itemList.add(model);


            } else itemList.add(model);
            updateIem();
/*

            itemLiner.setVisibility(View.VISIBLE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemRow = inflater.inflate(R.layout.create_invoice_item_row, null);
            itemLiner.addView(itemRow);*/
        }

        if (requestCode == Constant.requestCodeCreateInvoiceDetail) {
            invoice_no = data.getStringExtra(Constant.KEY_NO);

            invoice_date = data.getStringExtra(Constant.KEY_DATE);

            invoice_title = data.getStringExtra(Constant.KEY_TITLE);
            invoice_note = data.getStringExtra(Constant.KEY_NOTE);
            due_date = data.getStringExtra(Constant.KEY_DUE_DATE);
            late_feeId = data.getStringExtra(Constant.KEY_LATE_FEE_ID);
            term_condition = data.getStringExtra(Constant.KEY_TermsAndCondition);
            schedule_on = data.getStringExtra(Constant.KEY_SCHEDUAL_DATE);
            lateFeeName = data.getStringExtra(Constant.KEY_LATE_FEE_NAME);
            invoiceNoTV.setText(invoice_no);
            invoice_dateTV.setText(invoice_date);

            duedaysTV.setVisibility(View.GONE);
            if (!due_date.isEmpty()) {
                duedaysTV.setVisibility(View.VISIBLE);
                duedaysTV.setText(get_count_of_days(invoice_date, due_date));


            }

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

        if (requestCode == Constant.requestCodeSelectPaymentGateway) {

            selectedPaymentGatewayListList = (ArrayList<HashMap<String, String>>) data.getSerializableExtra(Constant.KEY_PAYMENT_GATEWAY);

        }
    }

/*    public void getChildFragment(Constant.FRAGMENT_RESULT type, HashMap<String, String> result) {

        switch (type) {


            case CLIENT:
                getView().findViewById(R.id.mainframe).setVisibility(View.GONE);
                clientAdd = result.get(Constant.KEY_ADDRESS);
                clientName = result.get(Constant.KEY_CLIENT_NAME);
                clientId = result.get(Constant.KEY_CLIENT_ID);
                clientCurrency = result.get(Constant.KEY_CLIENT_CURRENCY);
                clientTV.setText(clientName);
                totalAfterDiscountTV.setText(Utils.FloatToStringLimits(totalAfterDiscount));
                resetAllView();

                break;
        }

    }*/

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
            crossImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isItemEditable|| isPartialPaid)
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
            itemRow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isItemEditable|| isPartialPaid)
                        return;
                    myContext = getActivity().getApplicationContext();

                    Intent i = new Intent(myContext, CreateItem.class);
                    if (clientCurrency != null) {
                        i.putExtra(Constant.KEY_CLIENT_CURRENCY, clientCurrency);
                        i.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateInvoiceItem);

                        i.putExtra(Constant.KEY_ITEM, itemList.get(Integer.parseInt(v.getTag().toString())));
                        selectItemTV.setVisibility(View.GONE);

                        editItemPosition = Integer.parseInt(v.getTag().toString());
                        AddItemView.setVisibility(View.VISIBLE);

                        getActivity().startActivityForResult(i,
                                Constant.requestCodeCreateInvoiceItem);
                    } else {
                        Toast.makeText(context, "Please select client", Toast.LENGTH_SHORT).show();

                    }


                }
            });


            itemLiner.addView(itemRow);


        }


        subtotalValueTV.setText(clientCurrency+" "+global.setLength(Utils.FloatToStringLimits(SubTotalValue), Constant.defultLengthOfText));


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

        netBalanceTV.setText(clientCurrency+" "+global.setLength(Utils.FloatToStringLimits(netBalance), Constant.defultLengthOfText));


        // add tax


        grossTotal = netBalance;

        if (!taxListAdapter.selectedItemList.isEmpty()) {
            for (int i = 0; i < taxListAdapter.selectedItemList.size(); i++) {
                if (taxListAdapter.selectedItemList.get(i).get(Constant.KEY_NAME) != null)
                    if (!taxListAdapter.selectedItemList.get(i).get(Constant.KEY_NAME).isEmpty()) {

                        try {


                            grossTotal = grossTotal + (netBalance * (Double.parseDouble(taxListAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE).replaceAll(",", ""))) / 100);


                      /*      if (taxListAdapter.selectedItemList.get(i).get(Constant.KEY_TYPE).equalsIgnoreCase("Normal")) {
                                grossTotal = grossTotal + (netBalance * (Double.parseDouble(taxListAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE).replaceAll(",", ""))) / 100);


                            } else {


                                grossTotal = grossTotal + (Double.parseDouble(taxListAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE).replaceAll(",", "")));
                            }*/
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }

            }


        }

        grossTotalTV.setText(global.setLength(Utils.FloatToStringLimits(grossTotal), Constant.defultLengthOfText));

        lateFeeLinear.setVisibility(View.GONE);
        if (lateFeeAmount > 0) {

            lateFeeLinear.setVisibility(View.VISIBLE);
            lateFeeTV.setText(global.setLength(Utils.FloatToStringLimits(lateFeeAmount), Constant.defultLengthOfText));

        }

        amountPaidRow.setVisibility(View.GONE);

        if (totalPaid > 0) {
            amountPaidRow.setVisibility(View.VISIBLE);
            amountPaidTV.setText(global.setLength(Utils.FloatToStringLimits(totalPaid), Constant.defultLengthOfText));

        }
        outstandingAmount = grossTotal + lateFeeAmount - totalPaid;
        outStandingLinear.setVisibility(View.GONE);

        if (isEditing) {
            outStandingLinear.setVisibility(View.VISIBLE);
            outStandingTV.setText(global.setLength(Utils.FloatToStringLimits(outstandingAmount), Constant.defultLengthOfText));
        }

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

                //add  additional charges
/*

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

                netBalanceTV.setText(Utils.FloatToStringLimits(netBalance));
*/
                updateIem();
                break;

            case TAX:
                updateIem();

/*                grossTotal = netBalance;

                if (!taxListAdapter.selectedItemList.isEmpty()) {
                    for (int i = 0; i < taxListAdapter.selectedItemList.size(); i++) {
                        if (taxListAdapter.selectedItemList.get(i).get(Constant.KEY_NAME) != null)
                            if (!taxListAdapter.selectedItemList.get(i).get(Constant.KEY_NAME).isEmpty()) {

                                try
                                {


                                    grossTotal = grossTotal + (netBalance * (Double.parseDouble(taxListAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE).replaceAll(",", ""))) / 100);


                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }

                    }


                }

                grossTotalTV.setText(Utils.FloatToStringLimits(grossTotal));*/
                break;


        }


    }


    private void resetAllView() {


        itemList = new ArrayList<>();
        discountEt.setText(0 + "");
        additionalChargesAdapter.selectedItemList = new ArrayList<>();
        taxListAdapter.selectedItemList = new ArrayList<>();
        updateIem();


    }


    public void setDataForEditing(String invoiceId) {


        if (db == null)
            return;

        JSONObject object = null;
        //---------------Load data from local
        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_InvoicePreviewData + " WHERE " + DatabaseHelper.ID + " ='" + invoiceId + "'";
        Cursor cursor = db.getRecords(selectQuery);


        if (cursor.moveToFirst() && cursor.getCount() > 0) {


            //totalItem = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {

                try {

                    object = (new JSONObject
                            (cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)))).getJSONObject("invoice");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
        if (object == null)
            return;


        try {
            invoice_no = object.getString("number");
            invoice_title = object.getString("invoice_title");
            invoice_note = object.getString("notes");
            term_condition = object.getString("terms");
            invoice_date = MyDateFormat.GetDate(object.getString("date"));

            if (!object.getString("invoice_due_date").equalsIgnoreCase("0000-00-00 00:00:00"))
                due_date = MyDateFormat.GetDate(object.getString("invoice_due_date"));
            else due_date = "";

            duedaysTV.setVisibility(View.GONE);
            if (!due_date.isEmpty()) {
                duedaysTV.setVisibility(View.VISIBLE);
                duedaysTV.setText(get_count_of_days(invoice_date, due_date));


            }


            if (!object.getString("invoice_schedule_date").equalsIgnoreCase("0000-00-00 00:00:00"))
                schedule_on = MyDateFormat.GetDate(object.getString("invoice_schedule_date"));
            else schedule_on = "";

            late_feeId = object.getString("late_fee_id");
            if (object.has("late_fee_name"))
                lateFeeName = object.getString("late_fee_name");

            lateFeeAmount = 0;
            if (!object.getString("late_fee").isEmpty())
                lateFeeAmount = Double.parseDouble(object.getString("late_fee").replaceAll(",", ""));


            invoiceNoTV.setText(invoice_no);
            invoice_dateTV.setText(invoice_date);
            isItemEditable = true;

            if (object.getString("invoice_status").equalsIgnoreCase("Paid"))
                isItemEditable = false;

            isPartialPaid = false;

            if (object.getString("invoice_status").equalsIgnoreCase("Partial-Paid"))

                isPartialPaid = true;


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

            if (object.getString("InvoiceWiseDiscountType").equalsIgnoreCase("Percent")) {

                isDiscountFlat = false;
                flatButton.setChecked(false);
                percentButton.setChecked(true);

            }

            discountEt.setText(object.getString("InvoiceWiseDiscountValue"));


            // addtional value

            additionalChargesAdapter.selectedItemList = new ArrayList<>();
            for (int i = 0; i < object.getJSONObject("additional_charges").getJSONArray("charge").length(); i++) {

                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.KEY_NAME, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("name"));
                map.put(Constant.KEY_TYPE, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("type"));
                map.put(Constant.KEY_VALUE, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("amount"));
                additionalChargesAdapter.add(map);

            }
            //   additionalChargesAdapter.notifyDataSetChanged();

            //set tax

            taxListAdapter.selectedItemList = new ArrayList<>();
            for (int i = 0; i < object.getJSONObject("taxes").getJSONArray("tax").length(); i++) {

                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.KEY_NAME, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("name"));
                map.put(Constant.KEY_TYPE, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("type"));
                map.put(Constant.KEY_VALUE, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("amount"));
                map.put(Constant.KEY_ID, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("TaxId"));


                taxListAdapter.add(map);

            }
            taxListAdapter.notifyDataSetChanged();

            AllowPartialPaymentCheckBox.setChecked(false);
            if (!object.getString("IsPartialPayment").equalsIgnoreCase("N"))
                AllowPartialPaymentCheckBox.setChecked(true);

            if (!itemList.isEmpty()) {
                selectItemTV.setVisibility(View.GONE);
                AddItemView.setVisibility(View.VISIBLE);

            }
            totalPaid = 0;
            if (object.has("total_paid")) {

                if (!object.getString("total_paid").isEmpty()) {
                    try {
                        totalPaid = Double.parseDouble(object.getString("total_paid").replaceAll(",", ""));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //payment method
            selectedPaymentGatewayListList = new ArrayList<>();
            if (object.has("payment_method")) {

                JSONArray paymentArray = object.getJSONArray("payment_method");
                for (int i = 0;
                     i < paymentArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.KEY_ID, paymentArray.getString(i));
                    selectedPaymentGatewayListList.add(map);

                }


            }


            updateIem();
            ;

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }    // create invoice remote call to web and sending data

    @SuppressWarnings("rawtypes")
    private void createInvoice() {

        try {
            JSONObject CreateInvoice = new JSONObject();

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

            JSONArray taxarray = new JSONArray();

            for (int i = 0; i < taxListAdapter.selectedItemList.size(); i++) {


                taxarray.put(taxListAdapter.selectedItemList.get(i).get(Constant.KEY_ID));
            }

            JSONObject tax = new JSONObject();
            tax.put("tax_ids", taxarray);
            JSONArray paymentsgatewayarray = new JSONArray();
            String isPartialPayment;

            for (int i = 0; i < selectedPaymentGatewayListList.size(); i++) {


                paymentsgatewayarray.put(selectedPaymentGatewayListList.get(i).get(Constant.KEY_ID));
            }
            if (AllowPartialPaymentCheckBox.isChecked()) {
                isPartialPayment = "Y";
            } else {
                isPartialPayment = "N";
            }
            CreateInvoice.put("isPartialPayment", isPartialPayment);
            JSONObject client = new JSONObject();

            client.put("client_id", clientId);

            JSONObject Jdiscount = new JSONObject();
            Jdiscount.put("type", discount_type);
            Jdiscount.put("amount", discountEt.getText().toString());

            CreateInvoice.put("payment_gateway_ids", paymentsgatewayarray);
            CreateInvoice.put("client", client);
            CreateInvoice.put("discount", Jdiscount);


            CreateInvoice.put("send_mail", send_mail);

            CreateInvoice.put("invoice_title", invoice_title);

            //CreateInvoice.put(GlobalVariables.TAG_CREATED_BY, ConstantList.userId);

            CreateInvoice.put("number", invoice_no);
            CreateInvoice.put("date", invoice_date);
            CreateInvoice.put("due_date", due_date);
            CreateInvoice.put("late_fee", late_feeId);
            CreateInvoice.put("schedule_date", schedule_on);
            CreateInvoice.put("payment_term", "");

            CreateInvoice.put("status", "");
            CreateInvoice.put("notes", invoice_note);
            CreateInvoice.put("terms", term_condition);
            CreateInvoice.put("items", item);
            CreateInvoice.put("taxes", tax);
            CreateInvoice.put("additional_charges", charge);

            if (!isEditing) {
                CreateInvoice.put("method", "createInvoice");
            } else {
                CreateInvoice.put("invoice_id", invoiceId);
                CreateInvoice.put("method", "updateInvoice");
            }


            JSONObject CreateInvoiceMainObj = new JSONObject();
            JSONArray CreateInvoiceArray = new JSONArray();


            //save request in database

            values = new ContentValues();
            values.put(DatabaseHelper.JSON_DATA, CreateInvoice.toString());

            if (!isEditing) {
                values.put(DatabaseHelper.TYPE, Constant.KEY_CREATE);
                db.insert(DatabaseHelper.Table_CreateOfflineInvoice, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineInvoice + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_CREATE + "'";
            } else {
                values.put(DatabaseHelper.TYPE, Constant.KEY_EDIT);
                db.insert(DatabaseHelper.Table_CreateOfflineInvoice, values);


                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineInvoice + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_EDIT + "'";
            }


            cursor = db.getRecords(selectQuery);
            JSONObject object = null;
            if (cursor.moveToFirst() && cursor.getCount() > 0)
                for (int i = 0; i < cursor.getCount(); i++) {

                    try {
                        object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                        CreateInvoiceArray.put(i, object);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            if (!isEditing)
                CreateInvoiceMainObj.put("createInvoice", CreateInvoiceArray);
            else {
                CreateInvoiceMainObj.put("updateInvoice", CreateInvoiceArray);
            }


            if (global.isNetworkAvailable()) {
                WebRequest request = new WebRequest(context, CreateInvoiceMainObj, Constant.invoicelistURL, Constant.SERVICE_TYPE.CREATE, Constant.token, this, true);
                request.execute();
            } else {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                global.showAlert("Invoice is saved and it will be update when connect to internet", context);
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
                db.ClearTable(DatabaseHelper.Table_CreateOfflineInvoice);


            if (!result.getString("code").equalsIgnoreCase("200")) {
                if (type == Constant.SERVICE_TYPE.CREATE)
                    db.ClearTable(DatabaseHelper.Table_CreateOfflineInvoice);

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
                db.ClearTable(DatabaseHelper.Table_InvoiceCreateSettings);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_InvoiceCreateSettings, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_InvoiceCreateSettings;

                cursor = db.getRecords(selectQuery);
                object = null;
                if (cursor.moveToFirst() && cursor.getCount() > 0)
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                            invoice_no = object.getString("invoice_no");
                            invoice_note = object.getString("Invoice_Notes");
                            term_condition = object.getString("UserCompany_Terms");
                            invoiceNoTV.setText(invoice_no);


                            if (object.getString("discountOnTotal").equalsIgnoreCase("No")) {
                                discountOnTotalLinear.setVisibility(View.GONE);
                                totalAfterDiscountLinear.setVisibility(View.GONE);
                            } else {
                                discountOnTotalLinear.setVisibility(View.VISIBLE);


                                totalAfterDiscountLinear.setVisibility(View.VISIBLE);
                            }


                            if (object.getString("taxOnTotal").equalsIgnoreCase("No"))
                                taxOnTotalLinear.setVisibility(View.GONE);

                            else taxOnTotalLinear.setVisibility(View.VISIBLE);


                            if (object.getString("isPartialAmount").equalsIgnoreCase("No"))
                                allowPartialPaymentLinear.setVisibility(View.GONE);
                            else allowPartialPaymentLinear.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                break;


            case CREATE:
                Log.e("**************1", "*******");
                db.ClearTable(DatabaseHelper.Table_InvoicePreviewData);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                try {
                    invoiceId = result.getString("invoice_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                values.put(DatabaseHelper.ID, invoiceId);
                db.insert(DatabaseHelper.Table_InvoicePreviewData, values);

                Bundle arguments = new Bundle();
                //    System.out.println("Id1:" + adapter.estimateList.get(position).getInvoice_id());
                arguments.putString(Constant.KEY_INVOICE_ID, invoiceId);
                arguments.putInt(Constant.KEY_POSITION, 1);
/*                try {
                   // Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    global.showAlert( result.getString("message").toString(),context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                if (global.isNetworkAvailable()) {


                    fragmentChanger.onFragmentReplaceWithBackStack(new InvoicePreviewCreateFragment(), Constant.InvoicePreviewCreateFragmentTag, arguments);
                }


          /*      String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_InvoicePreviewData + " WHERE " + DatabaseHelper.ID + " ='" + estimateId + "'";

                Cursor cursor = db.getRecords(selectQuery);


                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    Log.e("**************3", "*******");

                    //totalItem = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {

                            object = (new JSONObject
                                    (cursor.getString(cursor.getColumnIndex(db.JSON_DATA)))).getJSONObject("invoice");
                            InvoicePreviewPagerFragment fragment = (InvoicePreviewPagerFragment) pagerAdapter.getItem(1);
                            pager.setCurrentItem(1);
                            fragment.setData(object);
                            Log.e("**************4", "*******");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }

*/


                break;


        }
    }

    public String get_count_of_days(String Created_date_String, String Expire_date_String) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;

        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int c_year = 0, c_month = 0, c_day = 0;

        if (Created_convertedDate.after(todayWithZeroTime)) {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }


            /*Calendar today_cal = Calendar.getInstance();
            int today_year = today_cal.get(Calendar.YEAR);
            int today = today_cal.get(Calendar.MONTH);
            int today_day = today_cal.get(Calendar.DAY_OF_MONTH);
            */


        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        if (dayCount < 0)
            return (" Overdue in " + (-1 * (int) dayCount) + " Days");

        return ("Due in " + (int) dayCount + " Days");
    }


}




