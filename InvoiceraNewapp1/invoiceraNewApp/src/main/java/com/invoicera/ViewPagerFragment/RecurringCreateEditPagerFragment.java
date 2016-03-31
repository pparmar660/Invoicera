package com.invoicera.ViewPagerFragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.invoicera.Fragment.RecurringList;
import com.invoicera.Fragment.RecurringPreviewCreateFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.ResultFromChildFragment;
import com.invoicera.InterFace.UpdateChargeAndTax;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.ChargesAndTaxListAdapter;
import com.invoicera.ListViewAdpter.ItemListAdapter;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Utility.Utils;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.AutoBillingActivity;
import com.invoicera.androidapp.CreateItem;
import com.invoicera.androidapp.CreateRecurringDetail;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.androidapp.SelectPaymentGateWay;
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
 * Created by Parvesh on 30/9/15.
 */
public class RecurringCreateEditPagerFragment extends RecurringPreviewCreateFragment implements
        View.OnClickListener, WebApiResult, RadioGroup.OnCheckedChangeListener, UpdateChargeAndTax {

    private NoScrollListView chargesListView, taxListView;
    RelativeLayout AddItemView, createRecurringNumberVew;
    boolean isCheckedFromSetEdit = false;
    public FragmentChanger fragmentChanger;
    transient Context myContext;
    ItemListAdapter itemListAdapter;
    transient ResultFromChildFragment fromChildFragment;
    ArrayList<ItemModel> itemList = new ArrayList<ItemModel>();
    LinearLayout itemLiner;
    TableRow taxOnTotalLinear, lateFeeLinear, allowPartialPaymentLinear, totalAfterDiscountLinear;
    public String recurring_no = "", recurring_date = "", recurring_title = "", selectQuery, schedule_on = "", late_feeId = "",
            recurring_note = "", term_condition = "", latefeeDayAfter = "", lateFeeName = "", clientOrganization = "", clientName, clientId, clientAdd, clientCurrency, creditAmount = "";
    RelativeLayout discountOnTotalLinear;
    TextView addCharges, recurringNoTV, lateFeeTV, subtotalValueTV, itemTotalTaxTV, totalTV, totalAfterDiscountTV, addTax,
            netBalanceTV, recurring_dateTV, sendTV, saveTV, amountPaidTV, txt_CreditAmnt, txt_EditAutoBill;
    String discount_type = "Fixed", send_mail = "0";
    RadioGroup discountRadioGroup;
    ContentValues values;
    DatabaseHelper db;
    RadioButton flatButton, percentButton;
    public TextView clientTV, selectItemTV, grossTotalTV, paymentGatewayTextView, duedaysTV, txt_CreateFrequency;
    transient Intent i;
    double totalItemTaxValue = 0, SubTotalValue, discount, outstandingAmount = 0, itemsTotalAmount, totalAfterDiscount, netBalance, grossTotal, totalPaid = 0, lateFeeAmount = 0;
    boolean isDiscountFlat = true, isItemEditable = true;
    EditText discountEt, etCreateOccurrence;
    CheckBox AllowPartialPaymentCheckBox, chk_AutoCredit, chk_AutoBilling;
    ArrayList<HashMap<String, String>> selectedPaymentGatewayListList;
    Cursor cursor;
    String recurringId, isAutoCredit = "Yes";

    WebApiResult webApiResult;
    Calendar myCalendar;

    HashMap<Integer, Integer> selectedTax;
    ChargesAndTaxListAdapter additionalChargesAdapter, taxListAdapter;
    boolean isEditing = false;
    TableRow amountPaidRow;
    boolean isCallingFromEdit=false;

    int editItemPosition = 0;
    String[] createFrequencyArray = {"Weekly", "2 Weeks", "3 Weeks", "4 Weeks", "Monthly", "Quarterly", "Half Yearly", "11 Month", "Yearly", "2 Years", "3 Years"};
    ArrayAdapter<String> createFrequencyAdapter;

    String selectedButton = "", payment_gateway = "", first_name = "", last_name = "", name_on_card = "", address = "", country = "", city = "",
            state = "", zip = "", card_type = "", card_number = "", cvv = "", expiry_year = "", expiry_month = "";
    RelativeLayout ly_autoCredit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_recurring, container, false);
        myContext = getActivity();

        isEditing = false;
        Bundle argument = getArguments();

        if (argument.getString(Constant.KEY_RECURRING_ID) != null) {
            if (!argument.getString(Constant.KEY_RECURRING_ID).isEmpty() && !argument.getString(Constant.KEY_RECURRING_ID).equalsIgnoreCase("null")) {
                recurringId = argument.getString(Constant.KEY_RECURRING_ID);
                isEditing = true;

            }
        }

        chargesListView = (NoScrollListView) view.findViewById(R.id.chargeslistView);
        taxListView = (NoScrollListView) view.findViewById(R.id.taxlistView);
        selectItemTV = (TextView) view.findViewById(R.id.item_add);
        AddItemView = (RelativeLayout) view.findViewById(R.id.addItemLinear);
        AddItemView.setVisibility(View.GONE);
        createRecurringNumberVew = (RelativeLayout) view.findViewById(R.id.creteNumber);
        db = new DatabaseHelper(context);
        // selectedAdditionalChargeList = new ArrayList<>();
        itemListAdapter = new ItemListAdapter((Activity) context);

        txt_CreateFrequency = (TextView) view.findViewById(R.id.txt_CreateFrequency);
        createFrequencyAdapter = new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, createFrequencyArray);

        txt_CreateFrequency.setOnClickListener(this);
        txt_CreateFrequency.setText(createFrequencyArray[0]);

        txt_EditAutoBill = (TextView) view.findViewById(R.id.txt_editAutoBill);
        txt_EditAutoBill.setOnClickListener(this);


        etCreateOccurrence = (EditText) view.findViewById(R.id.et_CreateOccurrence);

        ly_autoCredit = (RelativeLayout) view.findViewById(R.id.ly_autoCredit);

        recurringNoTV = (TextView) view.findViewById(R.id.number);
        recurring_dateTV = (TextView) view.findViewById(R.id.recurring_date);
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
        createRecurringNumberVew.setOnClickListener(this);
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
        chk_AutoBilling = (CheckBox) view.findViewById(R.id.chk_AutoBilling);
        chk_AutoCredit = (CheckBox) view.findViewById(R.id.chk_AutoCredit);

        if (chk_AutoBilling.isChecked()) {
            txt_EditAutoBill.setVisibility(View.VISIBLE);
        } else {
            txt_EditAutoBill.setVisibility(View.GONE);
        }

        txt_CreditAmnt = (TextView) view.findViewById(R.id.txt_CreditAmnt);

        paymentGatewayTextView = (TextView) view.findViewById(R.id.paymentGateWay);
        paymentGatewayTextView.setOnClickListener(this);


        myCalendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        recurring_dateTV.setText(sdf.format(myCalendar.getTime()));
        recurring_date = recurring_dateTV.getText().toString();
        flatButton = (RadioButton) view.findViewById(R.id.flat);
        percentButton = (RadioButton) view.findViewById(R.id.percent);
        discountOnTotalLinear = (RelativeLayout) view.findViewById(R.id.discountOnTotalLinear);
        allowPartialPaymentLinear = (TableRow) view.findViewById(R.id.allowPartialPaymentLinear);
        taxOnTotalLinear = (TableRow) view.findViewById(R.id.taxOnTotalLinear);
        amountPaidRow = (TableRow) view.findViewById(R.id.amountPaidLiner);
        amountPaidTV = (TextView) view.findViewById(R.id.amountPaid);
        lateFeeLinear = (TableRow) view.findViewById(R.id.lateFeeLinear);
        lateFeeTV = (TextView) view.findViewById(R.id.lateFee);
        /*outStandingTV = (TextView) view.findViewById(R.id.outStanding);
        outStandingLinear = (TableRow) view.findViewById(R.id.outStandingLinear);*/

        if (isEditing)
            sendTV.setVisibility(View.GONE);
        else sendTV.setVisibility(View.VISIBLE);


        discountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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
                            total = Double.parseDouble(totalTV.getText().toString().replaceAll(",", ""));
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

        selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_RecurringCreateSettings;

        cursor = db.getRecords(selectQuery);
        JSONObject object = null;
        if (cursor.moveToFirst() && cursor.getCount() > 0)
            for (int i = 0; i < cursor.getCount(); i++) {

                try {
                    object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                    setSetting(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        if (!isEditing) {
            JSONObject obj = new JSONObject();

            try {
                obj.put(Constant.KEY_METHOD, "getRecurringSetting");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            WebRequest request = new WebRequest(context, obj, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_SETTING, Constant.token, this, true);
            request.execute();
        }
        taxListAdapter.isFromRecurring = true;
        Home.toolbarText.setText("Recurring");

        if (argument.getString(Constant.KEY_CLIENT_ID) != null) {

            clientAdd = argument.getString(Constant.KEY_ADDRESS);  //map.get(Constant.KEY_ADDRESS);
            clientName = argument.getString(Constant.KEY_CLIENT_NAME);
            clientOrganization = argument.getString(Constant.KEY_ORGANIZATION);
            clientId = argument.getString(Constant.KEY_CLIENT_ID);
            clientCurrency = argument.getString(Constant.KEY_CLIENT_CURRENCY);
            clientTV.setText(clientOrganization);
            creditAmount = argument.getString("credit_amount");
            txt_CreditAmnt.setText("Credit Amount : " + creditAmount);
            totalAfterDiscountTV.setText(Utils.FloatToStringLimits(totalAfterDiscount));
        }


        chk_AutoBilling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isCallingFromEdit){

                   isCallingFromEdit=false;
                   return;
               }


                autoBilling();
            }
        });

        return view;
    }

    private void autoBilling() {

        if (chk_AutoBilling.isChecked()) {
            txt_EditAutoBill.setVisibility(View.VISIBLE);

            if (isCheckedFromSetEdit) {
                isCheckedFromSetEdit = false;
                return;
            }
            Intent returnIntent = new Intent(context, AutoBillingActivity.class);
            returnIntent.putExtra("detail_option", selectedButton);
            returnIntent.putExtra("payment_gateway", payment_gateway);
            returnIntent.putExtra("first_name", first_name);
            returnIntent.putExtra("last_name", last_name);
            returnIntent.putExtra("name_on_card", name_on_card);
            returnIntent.putExtra("address", address);
            returnIntent.putExtra("country", country);
            returnIntent.putExtra("city", city);
            returnIntent.putExtra("state", state);
            returnIntent.putExtra("zip", zip);
            returnIntent.putExtra("card_type", card_type);
            returnIntent.putExtra("card_number", card_number);
            returnIntent.putExtra("cvv", cvv);
            returnIntent.putExtra("expiry_year", expiry_year);
            returnIntent.putExtra("expiry_month", expiry_month);
            getActivity().startActivityForResult(returnIntent, Constant.requestCodeAutoBill);
        } else {
            chk_AutoBilling.setText("Auto Billing");
            txt_EditAutoBill.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Home.toolbarText.setText("Recurring");
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
        boolean canSendRecurring = false;
        switch (v.getId()) {
            case R.id.txt_editAutoBill:

                Intent returnIntent = new Intent(context, AutoBillingActivity.class);
                returnIntent.putExtra("detail_option", selectedButton);
                returnIntent.putExtra("payment_gateway", payment_gateway);
                returnIntent.putExtra("first_name", first_name);
                returnIntent.putExtra("last_name", last_name);
                returnIntent.putExtra("name_on_card", name_on_card);
                returnIntent.putExtra("address", address);
                returnIntent.putExtra("country", country);
                returnIntent.putExtra("city", city);
                returnIntent.putExtra("state", state);
                returnIntent.putExtra("zip", zip);
                returnIntent.putExtra("card_type", card_type);
                returnIntent.putExtra("card_number", card_number);
                returnIntent.putExtra("cvv", cvv);
                returnIntent.putExtra("expiry_year", expiry_year);
                returnIntent.putExtra("expiry_month", expiry_month);
                getActivity().startActivityForResult(returnIntent, Constant.requestCodeAutoBill);
                break;

            case R.id.txt_CreateFrequency:
                new AlertDialog.Builder(myContext).setTitle("Frequency")
                        .setAdapter(createFrequencyAdapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                txt_CreateFrequency.setText(createFrequencyArray[i].toString());
                            }
                        }).show();

                break;

            case R.id.selectClientView:

                if (isEditing) {
                    return;
                }

                i = new Intent(context, com.invoicera.androidapp.SelectClient.class);
                getActivity().startActivityForResult(i,
                        Constant.requestCodeSelectClient);

                break;

            case R.id.item_add:

            case R.id.addItemLinear:

                if (!isItemEditable) {
                    global.showAlert("Recurring is paid you can not add more item", context);
                    return;
                }

                myContext = getActivity().getApplicationContext();

                i = new Intent(myContext, CreateItem.class);
                if (clientCurrency != null) {
                    i.putExtra(Constant.KEY_CLIENT_CURRENCY, clientCurrency);
                    i.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateRecurringItem);
                    getActivity().startActivityForResult(i,
                            Constant.requestCodeCreateRecurringItem);
                    selectItemTV.setVisibility(View.GONE);

                    AddItemView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Please select client", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.creteNumber:
                Intent intent = new Intent(context, CreateRecurringDetail.class);
                intent.putExtra(Constant.KEY_NO, recurring_no);
                intent.putExtra(Constant.KEY_DATE, recurring_date);
                intent.putExtra(Constant.KEY_TITLE, recurring_title);
                //intent.putExtra(Constant.KEY_DUE_DATE, due_date);
                intent.putExtra(Constant.KEY_SCHEDUAL_DATE, schedule_on);
                intent.putExtra(Constant.KEY_NOTE, recurring_note);
                intent.putExtra(Constant.KEY_TermsAndCondition, term_condition);
                intent.putExtra(Constant.KEY_LateFee, latefeeDayAfter);
                intent.putExtra(Constant.KEY_LATE_FEE_ID, late_feeId);
                intent.putExtra(Constant.KEY_LATE_FEE_NAME, lateFeeName);
                intent.putExtra(Constant.KEY_EDITING, isEditing);
                intent.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateRecurringDetail);
                getActivity().startActivityForResult(intent, Constant.requestCodeCreateRecurringDetail);
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
                canSendRecurring = false;
                send_mail = "0";
                if (clientId != null)
                    if (!clientId.isEmpty())
                        canSendRecurring = true;

                if (canSendRecurring) {
                    if (!etCreateOccurrence.getText().toString().isEmpty()) {
                        if (!txt_CreateFrequency.getText().toString().isEmpty()) {
                            createRecurring();
                        } else {
                            global.showAlert("Please select frequency", context);
                        }
                    } else {
                        global.showAlert("Please enter occurrence", context);
                    }
                } else global.showAlert("Please select client", context);
                break;
            case R.id.send:

                canSendRecurring = false;
                send_mail = "1";
                if (clientId != null)
                    if (!clientId.isEmpty())
                        canSendRecurring = true;
                if (canSendRecurring) {
                    if (!etCreateOccurrence.getText().toString().isEmpty()) {
                        if (!txt_CreateFrequency.getText().toString().isEmpty()) {
                            createRecurring();
                        } else {
                            global.showAlert("Please select frequency", context);
                        }
                    } else {
                        global.showAlert("Please enter occurrence", context);
                    }
                } else global.showAlert("Please select client", context);
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == Constant.requestCodeCreateRecurringItem) {
            ItemModel model = (ItemModel) data.getParcelableExtra(Constant.KEY_ITEM);
            if (data.hasExtra(Constant.KEY_EDITING)) {

                if (data.getBooleanExtra(Constant.KEY_EDITING, false))
                    itemList.set(editItemPosition, model);
                else itemList.add(model);


            } else itemList.add(model);
            updateIem();
        }

        if (requestCode == Constant.requestCodeCreateRecurringDetail) {
            recurring_no = data.getStringExtra(Constant.KEY_NO);

            recurring_date = data.getStringExtra(Constant.KEY_DATE);

            recurring_title = data.getStringExtra(Constant.KEY_TITLE);
            recurring_note = data.getStringExtra(Constant.KEY_NOTE);
            //   due_date = data.getStringExtra(Constant.KEY_DUE_DATE);
            late_feeId = data.getStringExtra(Constant.KEY_LATE_FEE_ID);
            latefeeDayAfter = data.getStringExtra(Constant.KEY_LateFee);
            term_condition = data.getStringExtra(Constant.KEY_TermsAndCondition);
            schedule_on = data.getStringExtra(Constant.KEY_SCHEDUAL_DATE);
            lateFeeName = data.getStringExtra(Constant.KEY_LATE_FEE_NAME);
            recurringNoTV.setText(recurring_no);
            recurring_dateTV.setText(recurring_date);

        /*    duedaysTV.setVisibility(View.GONE);
            if (!due_date.isEmpty()) {
                duedaysTV.setVisibility(View.VISIBLE);
                duedaysTV.setText(get_count_of_days(recurring_date, due_date));

            }*/

        }

        if (requestCode == Constant.requestCodeSelectClient) {

            HashMap<String, String> map = (HashMap) data.getSerializableExtra(Constant.KEY_CLIENT);
            if (map != null) {


                clientAdd = map.get(Constant.KEY_ADDRESS);
                clientName = map.get(Constant.KEY_CLIENT_NAME);
                clientOrganization = map.get(Constant.KEY_ORGANIZATION);
                creditAmount = map.get("credit_amount");
                clientId = map.get(Constant.KEY_CLIENT_ID);
                clientCurrency = map.get(Constant.KEY_CLIENT_CURRENCY);
                clientTV.setText(clientOrganization);
                txt_CreditAmnt.setText("Credit Amount : " + creditAmount);
                totalAfterDiscountTV.setText(Utils.FloatToStringLimits(totalAfterDiscount));
                resetAllView();
            }
        }

        if (requestCode == Constant.requestCodeSelectPaymentGateway) {

            selectedPaymentGatewayListList = (ArrayList<HashMap<String, String>>) data.getSerializableExtra(Constant.KEY_PAYMENT_GATEWAY);

        }


        if (requestCode == Constant.requestCodeAutoBill) {

            if (data.getBooleanExtra("fromBack", true)) {

                chk_AutoBilling.setChecked(false);

            } else {
                if (data.getStringExtra("detail_option") == null)
                    return;
                if (data.getStringExtra("detail_option").equals("btnMeFill")) {

                    selectedButton = data.getStringExtra("detail_option");
                    payment_gateway = data.getStringExtra("payment_gateway");
                    first_name = data.getStringExtra("first_name");
                    last_name = data.getStringExtra("last_name");
                    name_on_card = data.getStringExtra("name_on_card");
                    address = data.getStringExtra("address");
                    country = data.getStringExtra("country");
                    city = data.getStringExtra("city");
                    state = data.getStringExtra("state");
                    zip = data.getStringExtra("zip");
                    card_type = data.getStringExtra("card_type");
                    card_number = data.getStringExtra("card_number");
                    cvv = data.getStringExtra("cvv");
                    expiry_year = data.getStringExtra("expiry_year");
                    expiry_month = data.getStringExtra("expiry_month");
                    chk_AutoBilling.setText("Edit Auto Billing");

                    System.out.println("EXPIRY DATE : ");

                } else {
                    System.out.println("EXPIRY DATE : ELSE ");
                    selectedButton = data.getStringExtra("detail_option");
                    payment_gateway = data.getStringExtra("payment_gateway");
                }
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

            try {
                qtyAndCost.setText("QTY : " + global.setLength(Utils.FloatToStringLimits(Double.parseDouble(model.getQuantity().replaceAll(",", ""))), Constant.defultLengthOfText) + "\nUnit Cost : " + global.setLength(Utils.FloatToStringLimits(Double.parseDouble(model.getUnit_cost().replaceAll(",", ""))), Constant.defultLengthOfText));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (model.getDiscount_type().equalsIgnoreCase("Percent")) {
                discountValue.setText("Discount : " + global.setLength(model.getDiscountValue(), Constant.defultLengthOfText) + "%");
            } else
                discountValue.setText("Discount : " + global.setLength(model.getDiscountValue(), Constant.defultLengthOfText));

            if (model.getTotalValue() != null)
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
                    if (model.getDiscountAmount() != null)
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
                if (model.getTotalValue() != null)
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
                        i.putExtra(Constant.KEY_REQUEST, Constant.requestCodeCreateRecurringItem);

                        i.putExtra(Constant.KEY_ITEM, itemList.get(Integer.parseInt(v.getTag().toString())));
                        selectItemTV.setVisibility(View.GONE);

                        editItemPosition = Integer.parseInt(v.getTag().toString());
                        AddItemView.setVisibility(View.VISIBLE);

                        getActivity().startActivityForResult(i,
                                Constant.requestCodeCreateRecurringItem);
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

        grossTotal = netBalance;

        if (!taxListAdapter.selectedItemList.isEmpty()) {
            for (int i = 0; i < taxListAdapter.selectedItemList.size(); i++) {
                if (taxListAdapter.selectedItemList.get(i).get(Constant.KEY_NAME) != null)
                    if (!taxListAdapter.selectedItemList.get(i).get(Constant.KEY_NAME).isEmpty()) {

                        try {
                            grossTotal = grossTotal + (netBalance * (Double.parseDouble(taxListAdapter.selectedItemList.get(i).get(Constant.KEY_VALUE).replaceAll(",", ""))) / 100);
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
//        outStandingLinear.setVisibility(View.GONE);

        if (isEditing) {
//            outStandingLinear.setVisibility(View.VISIBLE);
//            outStandingTV.setText(global.setLength(Utils.FloatToStringLimits(outstandingAmount), Constant.defultLengthOfText));
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
                updateIem();
                break;

            case TAX:
                updateIem();
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

    public void setDataForEditing(String recurringId) {
        if (db == null)
            return;

        JSONObject object = null;
        //---------------Load data from local
        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_RecurringPreviewData + " WHERE " + DatabaseHelper.ID + " ='" + recurringId + "'";
        Cursor cursor = db.getRecords(selectQuery);

        if (cursor.moveToFirst() && cursor.getCount() > 0) {

            //totalItem = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {

                try {
                    object = (new JSONObject
                            (cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)))).getJSONObject("recurring");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (object == null)
            return;

        try {
            recurring_no = object.getString("number");
            recurring_title = object.getString("recurring_title");
            latefeeDayAfter = object.getString("RecurringProfileLateFeeDayAfter");
            recurring_note = object.getString("notes");
            term_condition = object.getString("terms");
            recurring_date = MyDateFormat.GetDate(object.getString("date"));


            late_feeId = object.getString("late_fee_id");
            /*if (object.has("late_fee_name"))*/
            lateFeeName = object.getString("late_fee_name");

            lateFeeAmount = 0;
            if (object.has("late_fee"))

                if (!object.getString("late_fee").isEmpty())
                    lateFeeAmount = Double.parseDouble(object.getString("late_fee").replaceAll(",", ""));

            recurringNoTV.setText(recurring_no);
            recurring_dateTV.setText(recurring_date);
            isItemEditable = true;

            if (object.getString("recurring_status").equalsIgnoreCase("Paid"))
                isItemEditable = false;

            clientAdd = object.getJSONObject("client").getString("address");

            if (object.getJSONObject("client").has("currency"))
                clientCurrency = object.getJSONObject("client").getString("currency");

            clientId = object.getJSONObject("client").getString("client_id");
            clientName = object.getJSONObject("client").getString("client_name");
            txt_CreditAmnt.setText("Credit Amount : " + object.getJSONObject("client").getString("credit_amount"));


            if (object.getJSONObject("client").has("organization"))
                clientOrganization = object.getJSONObject("client").getString("organization");
            if (object.getJSONObject("client").has("credit_amount"))
                creditAmount = object.getJSONObject("client").getString("credit_amount");
            clientTV.setText(clientName);
            txt_CreditAmnt.setText("Credit Amount : " + creditAmount);

            etCreateOccurrence.setText(object.getString("occurence"));
            setFrequency(object.getString("frequency"));

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

                if (itemObj.has("total_priceValue"))
                    model.setTotalValue(itemObj.getString("total_priceValue"));
                if (itemObj.has("total_discountValue"))
                    model.setDiscountAmount(itemObj.getString("total_discountValue"));

                double tax1 = 0, tax2 = 0;

                try {
                    if (itemObj.has("tax1_value"))
                        tax1 = Double.parseDouble(itemObj.getString("tax1_value").replaceAll(",", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (itemObj.has("tax2_value"))
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

            if (object.has("RecurringWiseDiscountType"))

                if (object.getString("RecurringWiseDiscountType").equalsIgnoreCase("Percent")) {

                    isDiscountFlat = false;
                    flatButton.setChecked(false);
                    percentButton.setChecked(true);

                }

            if (object.has("RecurringWiseDiscountType"))
                discountEt.setText(object.getString("RecurringWiseDiscountValue"));


            if (object.has("RecurringProfileAdjustCredit")) {
                if (object.getString("RecurringProfileAdjustCredit").equalsIgnoreCase("Yes"))
                    chk_AutoCredit.setChecked(true);

                else chk_AutoCredit.setChecked(false);
            }


            // addtional value

            additionalChargesAdapter.selectedItemList = new ArrayList<>();
            if (object.has("additional_charges"))
                for (int i = 0; i < object.getJSONObject("additional_charges").getJSONArray("charge").length(); i++) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.KEY_NAME, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("name"));
                    map.put(Constant.KEY_TYPE, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("type"));
                    map.put(Constant.KEY_VALUE, object.getJSONObject("additional_charges").getJSONArray("charge").getJSONObject(i).getString("amount"));
                    additionalChargesAdapter.add(map);

                }
            additionalChargesAdapter.notifyDataSetChanged();

            //set tax

            taxListAdapter.selectedItemList = new ArrayList<>();
            if (object.has("taxes"))
                for (int i = 0; i < object.getJSONObject("taxes").getJSONArray("tax").length(); i++) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.KEY_NAME, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("name"));
                    map.put(Constant.KEY_TYPE, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("type"));
                    map.put(Constant.KEY_VALUE, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("amount"));
                    map.put(Constant.KEY_ID, object.getJSONObject("taxes").getJSONArray("tax").getJSONObject(i).getString("TaxId"));


                    taxListAdapter.add(map);

                }
            //taxListAdapter.notifyDataSetChanged();

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


            /// set auto billing data

            if (object.getString("IsAutoBilled").equalsIgnoreCase("Y")) {
                isCallingFromEdit=true;
                chk_AutoBilling.setChecked(true);
                JSONObject autoBillingObj = object.getJSONObject("autobill");
                isCheckedFromSetEdit = true;
                chk_AutoBilling.setChecked(true);
                chk_AutoBilling.setText("Edit Auto Billing");
                selectedButton = autoBillingObj.getString("selectedButton");
                if (autoBillingObj.has("payment_gateway_name"))
                    payment_gateway = autoBillingObj.getString("payment_gateway_name");
                JSONObject creditCardObject = autoBillingObj.getJSONObject("credit_card");
                card_number = creditCardObject.getString("number");
                name_on_card = creditCardObject.getString("name_on_card");
                first_name = creditCardObject.getString("first_name");
                last_name = creditCardObject.getString("last_name");
                city = creditCardObject.getString("city");
                address = creditCardObject.getString("address");
                state = creditCardObject.getString("state");
                zip = creditCardObject.getString("zip");
                country = creditCardObject.getString("country");
                cvv = creditCardObject.getString("cvv");
                if (creditCardObject.has("type_name"))
                    card_type = creditCardObject.getString("type_name");
                expiry_month = creditCardObject.getJSONObject("expiry").getString("month");
                expiry_year = creditCardObject.getJSONObject("expiry").getString("year");
            } else chk_AutoBilling.setChecked(false);


            updateIem();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }    // create invoice remote call to web and sending data

    @SuppressWarnings("rawtypes")
    private void createRecurring() {

        try {
            JSONObject CreateRecurring = new JSONObject();

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
                        obj1.put("unit_cost", Utils.FloatToStringLimits(item.getUnit_cost().replaceAll(",", "")).replaceAll(",", ""));
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


            JSONObject expiry = new JSONObject();
            expiry.put("month", expiry_month);
            expiry.put("year", expiry_year);

            JSONObject creditCard = new JSONObject();
            creditCard.put("zip", zip);
            creditCard.put("first_name", first_name);
            creditCard.put("address", address);
            creditCard.put("state", state);
            creditCard.put("last_name", last_name);
            creditCard.put("cvv", cvv);
            creditCard.put("number", card_number);
            creditCard.put("type", card_type);
            creditCard.put("name_on_card", name_on_card);
            creditCard.put("expiry", expiry);
            creditCard.put("city", city);
            creditCard.put("country", country);

            JSONObject autoBill = new JSONObject();
            autoBill.put("selectedButton", selectedButton);
            autoBill.put("credit_card", creditCard);
            autoBill.put("payment_gateway", payment_gateway);


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

            for (int i = 0; i < selectedPaymentGatewayListList.size(); i++)
                paymentsgatewayarray.put(selectedPaymentGatewayListList.get(i).get(Constant.KEY_ID));


            if (chk_AutoCredit.isChecked())
                isAutoCredit = "Yes";
            else
                isAutoCredit = "No";

            if (AllowPartialPaymentCheckBox.isChecked()) {
                isPartialPayment = "Y";
            } else {
                isPartialPayment = "N";
            }
            CreateRecurring.put("isPartialPayment", isPartialPayment);
            JSONObject client = new JSONObject();

            client.put("client_id", clientId);

            JSONObject Jdiscount = new JSONObject();
            Jdiscount.put("type", discount_type);
            Jdiscount.put("amount", discountEt.getText().toString());

            CreateRecurring.put("payment_gateway_ids", paymentsgatewayarray);
            CreateRecurring.put("client", client);
            CreateRecurring.put("discount", Jdiscount);


            CreateRecurring.put("send_mail", send_mail);

            CreateRecurring.put("recurring_title", recurring_title);
            CreateRecurring.put("occurence", etCreateOccurrence.getText().toString().trim());

            String frequency = "";
            if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("Weekly")) {
                frequency = "1 Week";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("2 Weeks")) {
                frequency = "2 Week";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("3 Weeks")) {
                frequency = "3 Week";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("4 Weeks")) {
                frequency = "4 Week";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("Monthly")) {
                frequency = "1 Month";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("Quarterly")) {
                frequency = "3 Month";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("Half Yearly")) {
                frequency = "6 Month";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("11 Months")) {
                frequency = "11 Month";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("Yearly")) {
                frequency = "1 Year";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("2 Years")) {
                frequency = "2 Year";
            } else if (txt_CreateFrequency.getText().toString().equalsIgnoreCase("3 Years")) {
                frequency = "3 Year";
            } else {
                frequency = txt_CreateFrequency.getText().toString();
            }

            CreateRecurring.put("frequency", frequency.trim());
            CreateRecurring.put("isAutoCreditAdjuest", isAutoCredit);

            //CreateInvoice.put(GlobalVariables.TAG_CREATED_BY, ConstantList.userId);

            CreateRecurring.put("number", recurring_no);
            CreateRecurring.put("date", recurring_date);
            //CreateRecurring.put("due_date", due_date);
            CreateRecurring.put("late_fee", late_feeId);
            CreateRecurring.put("schedule_date", schedule_on);
            CreateRecurring.put("payment_term", "");

            CreateRecurring.put("status", "");
            CreateRecurring.put("notes", recurring_note);
            CreateRecurring.put("RecurringProfileLateFeeDayAfter", latefeeDayAfter);
            CreateRecurring.put("terms", term_condition);
            CreateRecurring.put("items", item);
            CreateRecurring.put("taxes", tax);
            CreateRecurring.put("additional_charges", charge);
            if (chk_AutoBilling.isChecked()) {
                CreateRecurring.put("autobill", autoBill);
            } else {
                CreateRecurring.put("autobill", "");
            }

            if (!isEditing) {
                CreateRecurring.put("method", "createRecurringBase");
            } else {
                CreateRecurring.put("recurring_id", recurringId);
                CreateRecurring.put("method", "updateRecurringBase");
            }


            JSONObject CreateRecurringMainObj = new JSONObject();
            JSONArray CreateRecurringArray = new JSONArray();


            //save request in database

            values = new ContentValues();
            values.put(DatabaseHelper.JSON_DATA, CreateRecurring.toString());

            if (!isEditing) {
                values.put(DatabaseHelper.TYPE, Constant.KEY_CREATE);
                db.insert(DatabaseHelper.Table_CreateOfflineRecurring, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineRecurring + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_CREATE + "'";
            } else {
                values.put(DatabaseHelper.TYPE, Constant.KEY_EDIT);
                db.insert(DatabaseHelper.Table_CreateOfflineRecurring, values);


                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_CreateOfflineRecurring + " WHERE " + DatabaseHelper.TYPE + "='" + Constant.KEY_EDIT + "'";
            }


            cursor = db.getRecords(selectQuery);
            JSONObject object = null;
            if (cursor.moveToFirst() && cursor.getCount() > 0)
                for (int i = 0; i < cursor.getCount(); i++) {

                    try {
                        object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                        CreateRecurringArray.put(i, object);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            if (!isEditing)
                CreateRecurringMainObj.put("createRecurringBase", CreateRecurringArray);
            else {
                CreateRecurringMainObj.put("updateRecurringBase", CreateRecurringArray);
            }


            if (global.isNetworkAvailable()) {
                WebRequest request = new WebRequest(context, CreateRecurringMainObj, Constant.invoicelistURL, Constant.SERVICE_TYPE.CREATE, Constant.token, this, true);
                request.execute();
            } else {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                global.showAlert("Recurring is saved and it will be update when connect to internet", context);
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
                db.ClearTable(DatabaseHelper.Table_CreateOfflineRecurring);


            if (!result.getString("code").equalsIgnoreCase("200")) {
                if (type == Constant.SERVICE_TYPE.CREATE)
                    db.ClearTable(DatabaseHelper.Table_CreateOfflineRecurring);

                if (result.has("message")) {
                    Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_LONG).show();

                    return;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject object;

        switch (type) {

            case GET_SETTING:
                db.ClearTable(DatabaseHelper.Table_RecurringCreateSettings);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.Table_RecurringCreateSettings, values);

                selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_RecurringCreateSettings;

                cursor = db.getRecords(selectQuery);
                object = null;
                if (cursor.moveToFirst() && cursor.getCount() > 0)
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            object = new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA)));
                            setSetting(object);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                break;


            case CREATE:
                Log.e("**************1", "*******");
                db.ClearTable(DatabaseHelper.Table_RecurringPreviewData);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                try {
                    recurringId = result.getJSONObject("recurring").getString("recurring_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                values.put(DatabaseHelper.ID, recurringId);
                db.insert(DatabaseHelper.Table_RecurringPreviewData, values);

                Bundle arguments = new Bundle();
                //    System.out.println("Id1:" + adapter.estimateList.get(position).getInvoice_id());
                arguments.putString(Constant.KEY_RECURRING_ID, recurringId);
                arguments.putInt(Constant.KEY_POSITION, 1);

                if (global.isNetworkAvailable()) {


                    fragmentChanger.onFragmentReplaceWithBackStack(new RecurringPreviewCreateFragment(), Constant.RecurringPreviewCreateFragmentTag, arguments);
                }


                break;


        }
    }


    public void setSetting(JSONObject object) {


        try {
            recurring_no = object.getString("recurring_no");
            recurring_note = object.getString("Invoice_Notes");
            term_condition = object.getString("UserCompany_Terms");
            recurringNoTV.setText(recurring_no);
            if (!isEditing)
                if (object.has("canAddRecurring")) {
                    if (!object.getString("canAddRecurring").equals("Yes")) {
                        global.showAlert("Please upgrade your plan", myContext);
                        fragmentChanger.onFragmentReplaceWithBackStack(new RecurringList(), Constant.RecurringListTag, null);
                    }

                }


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

            if (object.getString("isAddCredit").equalsIgnoreCase("No"))
                ly_autoCredit.setVisibility(View.GONE);

            else ly_autoCredit.setVisibility(View.VISIBLE);

            if (object.getString("isPartialAmount").equalsIgnoreCase("No"))
                allowPartialPaymentLinear.setVisibility(View.GONE);
            else allowPartialPaymentLinear.setVisibility(View.VISIBLE);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setFrequency(String frequency) {


        switch (frequency) {
            case "1 Week":
                txt_CreateFrequency.setText("weekly");
                break;
            case "2 Week":
                txt_CreateFrequency.setText("2 Weeks");
                break;

            case "3 Week":
                txt_CreateFrequency.setText("3 Weeks");
                break;
            case "4 Week":
                txt_CreateFrequency.setText("4 Weeks");
                break;
            case "1 Month":
                txt_CreateFrequency.setText("Monthly");
                break;
            case "3 Month":
                txt_CreateFrequency.setText("Quarterly");
                break;
            case "6 Month":
                txt_CreateFrequency.setText("Half Yearly");
                break;
            case "11 Month":
                txt_CreateFrequency.setText("11 Months");
                break;
            case "1 Year":
                txt_CreateFrequency.setText("Yearly");
                break;
            case "2 Year":
                txt_CreateFrequency.setText("2 Years");
                break;
            case "3 Year":
                txt_CreateFrequency.setText("3 Years");
                break;


        }

    }


}