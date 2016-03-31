package com.invoicera.ViewPagerFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.Fragment.RecurringList;
import com.invoicera.Fragment.RecurringPreviewCreateFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Utility.Utils;
import com.invoicera.Webservices.DownloadPdfFile;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.ItemTaxModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Parvesh on 30/9/15.
 */
public class RecurringPreviewPagerFragment extends RecurringPreviewCreateFragment implements WebApiResult, View.OnClickListener {
    DatabaseHelper db;
    ContentValues values;
    TextView senderNameTV, senderAddressTV, senderPhoneTV, recurringNoTV, dateTV, totalAdditionalChargesTV, totalRecurringTaxTV, totalItemTaxTV;
    TextView clientNameTV, lateFeeTV, clientAddTV, clientPhoneTV, subTotalTV, RecurringTotalTV, outStandingTV, amountPaidTV, netBalanceTV, discountTV, balanceAfterDiscountTV;
    TableLayout itemTable;
    ImageView logoImage;
    LinearLayout clientDetailLinear;
    TableRow taxLinear, lateFeeLinear, recurringTotalLinear, valueAfterDiscountLinear, discountLinear, additionalChargeLinear, totalRecurringTaxLiner, amountPaidLiner, balanceLiner, netBalanceLiner;
    ArrayList<ItemTaxModel> taxModelsList;
    public String recurring_id = "", recurringStatus = "";
    public static String ClientCurrency = "";
    boolean isShowProcessing = false;
    TableLayout amountDetailLinear;
    WebApiResult webApiResult;
    WebRequest request;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    public FragmentChanger fragmentChanger;
    TextView deleteTV, exportPdfTV, sendTV, amountTypeTV; //offlinePaymentTV
    RecurringPreviewCreateFragment parentFragment;
    Calendar myCalendar;
    boolean isOfflineDataCalled = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.recurring_preview, container, false);
        Home.toolbarText.setText("Recurring");

        Bundle argument = getArguments();

        if (argument.getString(Constant.KEY_RECURRING_ID) != null) {
            if (!argument.getString(Constant.KEY_RECURRING_ID).isEmpty())
                recurring_id = argument.getString(Constant.KEY_RECURRING_ID);
            if (argument.getString(Constant.KEY_RECURRING_STATUS) != null)
                recurringStatus = argument.getString(Constant.KEY_RECURRING_STATUS);
        }
        myCalendar = Calendar.getInstance();
        db = new DatabaseHelper(context);
        webApiResult = this;
        parentFragment = (RecurringPreviewCreateFragment) getParentFragment();

        senderAddressTV = (TextView) view.findViewById(R.id.senderAddress);
        senderNameTV = (TextView) view.findViewById(R.id.senderName);
        senderPhoneTV = (TextView) view.findViewById(R.id.senderNumber);
        recurringNoTV = (TextView) view.findViewById(R.id.recurringNumber);
        isOfflineDataCalled = false;
        outStandingTV = (TextView) view.findViewById(R.id.balance);
        amountPaidTV = (TextView) view.findViewById(R.id.amoutpaid);
        netBalanceTV = (TextView) view.findViewById(R.id.netBalance);
        discountTV = (TextView) view.findViewById(R.id.discountvalue);
        balanceAfterDiscountTV = (TextView) view.findViewById(R.id.valueAfterDiscount);
        dateTV = (TextView) view.findViewById(R.id.date);
        clientNameTV = (TextView) view.findViewById(R.id.clientName);
        clientAddTV = (TextView) view.findViewById(R.id.clientAddress);
        clientPhoneTV = (TextView) view.findViewById(R.id.ClientNumber);
        itemTable = (TableLayout) view.findViewById(R.id.itemTable);
        logoImage = (ImageView) view.findViewById(R.id.logo);
        taxLinear = (TableRow) view.findViewById(R.id.itemTaxLinear);
        subTotalTV = (TextView) view.findViewById(R.id.subtotleValue);
        RecurringTotalTV = (TextView) view.findViewById(R.id.recurringtotleValue);
        recurringTotalLinear = (TableRow) view.findViewById(R.id.recurringtotaleLinear);
        valueAfterDiscountLinear = (TableRow) view.findViewById(R.id.valueAfterDiscountLiner);
        discountLinear = (TableRow) view.findViewById(R.id.discountLinear);
        additionalChargeLinear = (TableRow) view.findViewById(R.id.addtionalChargeLinear);
        totalRecurringTaxLiner = (TableRow) view.findViewById(R.id.recurringTotalTaxLinear);
        amountPaidLiner = (TableRow) view.findViewById(R.id.amountPaidLiner);
        balanceLiner = (TableRow) view.findViewById(R.id.balanceLiner);
        netBalanceLiner = (TableRow) view.findViewById(R.id.netBalanceLiner);
        totalAdditionalChargesTV = (TextView) view.findViewById(R.id.totalAftreAdditionalCharges);
        totalRecurringTaxTV = (TextView) view.findViewById(R.id.totalRecurringTax);
        totalItemTaxTV = (TextView) view.findViewById(R.id.totalItemTax);
        amountDetailLinear = (TableLayout) view.findViewById(R.id.amountDetailLinear);
        clientDetailLinear = (LinearLayout) view.findViewById(R.id.clientInfoLinear);
        lateFeeLinear = (TableRow) view.findViewById(R.id.lateFeeLinear);
        lateFeeTV = (TextView) view.findViewById(R.id.lateFee);
        amountTypeTV = (TextView) view.findViewById(R.id.amountType);
        deleteTV = (TextView) view.findViewById(R.id.delete);
        exportPdfTV = (TextView) view.findViewById(R.id.exportPdf);
        sendTV = (TextView) view.findViewById(R.id.send);

        deleteTV.setOnClickListener(this);
        exportPdfTV.setOnClickListener(this);
        sendTV.setOnClickListener(this);

        //--------------------------
        RecurringCreateEditPagerFragment fragment = (RecurringCreateEditPagerFragment) parentFragment.pagerAdapter.getItem(0);
        if (fragment != null) {

            if (!fragment.isEditing) {
                itemTable.setVisibility(View.GONE);
                amountDetailLinear.setVisibility(View.GONE);
                clientDetailLinear.setVisibility(View.GONE);
            }

        }

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.preview_image)
                .showImageForEmptyUri(R.drawable.preview_image)
                .showImageOnFail(R.drawable.preview_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();

        ImageLoader loder = ImageLoader.getInstance();

        loder.init(ImageLoaderConfiguration.createDefault(context));

        taxModelsList = new ArrayList<>();

        if (!recurring_id.isEmpty()) {

            //---------------Load data from local

            String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                    + " From " + DatabaseHelper.Table_RecurringPreviewData + " WHERE " + DatabaseHelper.ID + " ='" + recurring_id + "'";
            Cursor cursor = db.getRecords(selectQuery);


            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {

                    try {

                        JSONObject object = (new JSONObject
                                (cursor.getString(cursor.getColumnIndex(db.JSON_DATA)))).getJSONObject("recurring");
                        setData(object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                isShowProcessing = false;


            } else isShowProcessing = true;
        }

        //st data

        JSONObject obj = null;
        try {
            obj = new JSONObject();

            obj.put(Constant.KEY_METHOD, "getRecurring");
            obj.put(Constant.KEY_RECURRING_ID, recurring_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebRequest request = new WebRequest(context, obj,
                Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, isShowProcessing);

        if (!recurring_id.isEmpty()) {
            Home.progressBarHome.setVisibility(View.VISIBLE);
            request.execute();
        }

        setSenderData();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentChanger = (FragmentChanger) getActivity();
    }

    public void setData(JSONObject object) {


        {
            if (clientNameTV == null)
                return;

            try {
                // reset all

                itemTable.setVisibility(View.VISIBLE);
                amountDetailLinear.setVisibility(View.VISIBLE);
                clientDetailLinear.setVisibility(View.VISIBLE);

                taxModelsList = new ArrayList<>();

                while (itemTable.getChildCount() > 1) {

                    itemTable.removeViewAt(1);
                }

                //load data from server

                if (object.has("logo_url")) {

                    String logoUrl = object.getString("logo_url");

                    logoUrl.replace("\\", "");

                    ImageLoader.getInstance().displayImage("https://" + logoUrl, logoImage, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {


                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                    message = "Input/Output error";
                                    break;
                                case DECODING_ERROR:
                                    message = "Image can't be decoded";
                                    break;
                                case NETWORK_DENIED:
                                    message = "Downloads are denied";
                                    break;
                                case OUT_OF_MEMORY:
                                    message = "Out Of Memory error";
                                    break;
                                case UNKNOWN:
                                    message = "Unknown error";
                                    break;
                            }
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                        }
                    });

                }

                clientNameTV.setText(object.getJSONObject("client").getString("client_name"));

                recurringStatus=object.getString("recurring_status");
                if (recurringStatus.equalsIgnoreCase("Deleted")||recurringStatus.equalsIgnoreCase("Delete")||recurringStatus.equalsIgnoreCase("stop")) {
                    deleteTV.setVisibility(View.GONE);
                    sendTV.setVisibility(View.GONE);
                }
                else {
                    deleteTV.setVisibility(View.VISIBLE);
                    sendTV.setVisibility(View.VISIBLE);
                }
                if (!object.getJSONObject("client").getString("address").isEmpty()) {

                    clientAddTV.setText("Add : " + object.getJSONObject("client").getString("address"));
                } else {
                    clientAddTV.setText("Add : N/A ");
                }

                if (object.getJSONObject("client").has("phone_number"))
                    if (!object.getJSONObject("client").getString("phone_number").isEmpty()) {

                        clientPhoneTV.setText("Ph : " + object.getJSONObject("client").getString("phone_number"));
                    } else {
                        clientPhoneTV.setText("Ph : N/A ");
                    }
                else
                    clientPhoneTV.setText("Ph : Not Available");


                if (object.getJSONObject("client").has("currency"))
                    ClientCurrency = object.getJSONObject("client").getString("currency");

                senderNameTV.setText(object.getJSONObject("sender").getString("sender_name"));

                String senderAddress = object.getJSONObject("sender").getString("address");
                if (senderAddress.length() > 40)
                    senderAddress = senderAddress.substring(0, 39) + "..";

                senderAddressTV.setText(senderAddress);
                senderPhoneTV.setText(object.getJSONObject("sender").getString("phone_number"));
                dateTV.setText(MyDateFormat.GetDate(object.getString("date")));
                recurringNoTV.setText(object.getString("number"));

                for (int j = 0; j < object.getJSONObject("items").getJSONArray("item").length(); j++) {

                    JSONObject itemObj = object.getJSONObject("items").getJSONArray("item").getJSONObject(j);

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                            .LAYOUT_INFLATER_SERVICE);
                    View itemView = inflater.inflate(R.layout.item_table_row, null);


                    TextView itemName = (TextView) itemView.findViewById(R.id.item_name);
                    TextView itemDescription = (TextView) itemView.findViewById(R.id.item_description);
                    TextView qtyAndUc = (TextView) itemView.findViewById(R.id.qty_uc);
                    TextView discount = (TextView) itemView.findViewById(R.id.discount);
                    TextView price = (TextView) itemView.findViewById(R.id.price);


                    itemName.setText(global.setLength(itemObj.getString("name"), Constant.defultLengthOfText));
                    if (!itemObj.getString("discount").isEmpty()) {
                        if (itemObj.getString("discount_type").equalsIgnoreCase("Percent"))
                            discount.setText(global.setLength(itemObj.getString("discount"), Constant.defultLengthOfText) + " %");
                        else
                            discount.setText(global.setLength(itemObj.getString("discount"), Constant.defultLengthOfText));

                    } else
                        discount.setText("0.0");

                    if (itemObj.has("total_priceValue"))
                        price.setText(global.setLength(itemObj.getString("total_priceValue"), Constant.defultLengthOfText));

                    qtyAndUc.setText("QTY:" + global.setLength(itemObj.getString("quantity"), Constant.defultLengthOfText) + "\nUC:" + global.setLength(itemObj.getString("unit_cost"), Constant.defultLengthOfText));

                    itemTable.addView(itemView);


                    if (!itemObj.getString("tax1_type").isEmpty()) {

                        ItemTaxModel taxModel = new ItemTaxModel();
                        taxModel.setName(itemObj.getString("tax1_name"));
                        taxModel.setType(itemObj.getString("tax1_type"));

                        if (itemObj.has("tax1_value"))
                            taxModel.setValue(itemObj.getString("tax1_value"));
                        taxModel.setPercentage(itemObj.getString("tax1_percent"));
                        taxModelsList.add(taxModel);
                    }

                    if (!itemObj.getString("tax2_type").isEmpty()) {

                        ItemTaxModel ItemTaxModel = new ItemTaxModel();
                        ItemTaxModel.setName(itemObj.getString("tax2_name"));
                        ItemTaxModel.setType(itemObj.getString("tax2_type"));

                        if (itemObj.has("tax2_value"))
                            ItemTaxModel.setValue(itemObj.getString("tax2_value"));
                        ItemTaxModel.setPercentage(itemObj.getString("tax2_percent"));
                        taxModelsList.add(ItemTaxModel);
                    }
                }

                if (object.has("sub_total"))
                    subTotalTV.setText(global.setLength(object.getString("sub_total"), Constant.defultLengthOfText));
                taxLinear.setVisibility(View.GONE);

                if (!object.getString("total_tax").isEmpty()) {
                    taxLinear.setVisibility(View.VISIBLE);
                    totalItemTaxTV.setText(global.setLength(object.getString("total_tax"), Constant.defultLengthOfText));

                }
                recurringTotalLinear.setVisibility(View.GONE);
                if (!taxModelsList.isEmpty()) {


                    recurringTotalLinear.setVisibility(View.VISIBLE);
                    if (object.has("recurring_total"))
                        RecurringTotalTV.setText(global.setLength(object.getString("recurring_total"), Constant.defultLengthOfText));

                }

                // set discount
                valueAfterDiscountLinear.setVisibility(View.GONE);
                discountLinear.setVisibility(View.GONE);

                if (object.has("RecurringWiseDiscountType"))
                    if (!object.getString("RecurringWiseDiscountValue").isEmpty())
                        if (Double.parseDouble(object.getString("RecurringWiseDiscountValue").replaceAll(",", "")) > 0) {
                            discountLinear.setVisibility(View.VISIBLE);
                            valueAfterDiscountLinear.setVisibility(View.VISIBLE);

                            if ((object.has("RecurringWiseDiscountCalValue")))
                                discountTV.setText(global.setLength(object.getString("RecurringWiseDiscountCalValue"), Constant.defultLengthOfText));
                            balanceAfterDiscountTV.setText(global.setLength(object.getString("after_discount_total"), Constant.defultLengthOfText));

                        }

                additionalChargeLinear.setVisibility(View.GONE);
                if (object.has("AdditionalChargeTotal"))
                    if (!object.getString("AdditionalChargeTotal").isEmpty()) {
                        additionalChargeLinear.setVisibility(View.VISIBLE);
                        totalAdditionalChargesTV.setText(global.setLength(object.getString("AdditionalChargeTotal"), Constant.defultLengthOfText));
                    }

                totalRecurringTaxLiner.setVisibility(View.GONE);
                if (object.has("RecurringTaxTotal"))
                    if (!object.getString("RecurringTaxTotal").isEmpty()) {
                        totalRecurringTaxLiner.setVisibility(View.VISIBLE);
                        totalRecurringTaxTV.setText(global.setLength(object.getString("RecurringTaxTotal"), Constant.defultLengthOfText));
                    }

                lateFeeLinear.setVisibility(View.GONE);
                if (object.has("late_fee"))
                    if (Double.parseDouble(object.getString("late_fee").replaceAll(",", "")) > 0) {

                        lateFeeLinear.setVisibility(View.VISIBLE);
                        lateFeeTV.setText(global.setLength(Utils.FloatToStringLimits(object.getString("late_fee")), Constant.defultLengthOfText));

                    }

                netBalanceTV.setText(global.setLength(object.getString("net_balance"), Constant.defultLengthOfText));

                if (object.has("outstanding"))
                    outStandingTV.setText(ClientCurrency + " " + global.setLength(object.getString("outstanding"), Constant.defultLengthOfText));
                if (object.has("total_paid"))
                    amountPaidTV.setText(global.setLength(object.getString("total_paid"), Constant.defultLengthOfText));
                amountTypeTV.setText("Amount(" + ClientCurrency + ")");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        JSONObject jsonData = new JSONObject();

        switch (v.getId()) {
            case R.id.delete:
                DeleteRecurringAlertDialog();
                break;

            case R.id.send:
                jsonData = new JSONObject();

                try {
                    jsonData.put(Constant.KEY_RECURRING_ID, recurring_id);
                    jsonData.put("method", "sendRecurring");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonData, Constant.invoicelistURL, Constant.SERVICE_TYPE.SEND, Constant.token, this, true);
                request.execute();

                break;

            case R.id.exportPdf:
                jsonData = new JSONObject();

                try {
                    jsonData.put(Constant.KEY_RECURRING_ID, recurring_id);
                    jsonData.put("method", "viewRecurringPDF");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonData, Constant.invoicelistURL, Constant.SERVICE_TYPE.EXPORT_PDF, Constant.token, this, true);
                request.execute();
                break;
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!isOfflineDataCalled)
            return;
        isOfflineDataCalled = false;
        if (requestCode == Constant.requestCodeOffLinePaymentFromPreview) {

            Bundle arguments = new Bundle();
            //    System.out.println("Id1:" + adapter.estimateList.get(position).getInvoice_id());
            arguments.putString(Constant.KEY_RECURRING_ID, recurring_id);
            arguments.putInt(Constant.KEY_POSITION, 1);

            if (global.isNetworkAvailable()) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject();

                    obj.put(Constant.KEY_METHOD, "getRecurring");
                    obj.put(Constant.KEY_RECURRING_ID, recurring_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                request = new WebRequest(context, obj,
                        Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, false);

                if (!recurring_id.isEmpty()) {
                    Home.progressBarHome.setVisibility(View.VISIBLE);
                    request.execute();
                }

                //fragmentChanger.onFragmentReplaceWithBackStack(new InvoicePreviewCreateFragment(), Constant.InvoicePreviewCreateFragmentTag, arguments);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {
       /* Home.progressBarHome.setVisibility(View.GONE);*/
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
            case GET_DATA:
                db.ClearTable(DatabaseHelper.Table_RecurringPreviewData);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                values.put(DatabaseHelper.ID, recurring_id);
                db.insert(DatabaseHelper.Table_RecurringPreviewData, values);

                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_RecurringPreviewData + " WHERE " + DatabaseHelper.ID + " ='" + recurring_id + "'";

                Cursor cursor = db.getRecords(selectQuery);
                taxModelsList = new ArrayList<>();

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    //totalItem = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            JSONObject object = (new JSONObject
                                    (cursor.getString(cursor.getColumnIndex(db.JSON_DATA)))).getJSONObject("recurring");
                            setData(object);
                            RecurringCreateEditPagerFragment createEditRecurringPagerFragment = (RecurringCreateEditPagerFragment) parentFragment.pagerAdapter.getItem(0);
                            createEditRecurringPagerFragment.setDataForEditing(recurring_id);
                            RecurringInformationPagerFragment informationRecurringPagerFragment = (RecurringInformationPagerFragment) parentFragment.pagerAdapter.getItem(2);
                            informationRecurringPagerFragment.setData(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case DELETE:
              /*  getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().executePendingTransactions();*/
               /* values = new ContentValues();
                values.put(RecurringList.table_field[RecurringList.position], (String) null);*/
                db.ClearTable(DatabaseHelper.Table_RecurringList);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                db.insert(DatabaseHelper.Table_RecurringList, values);
                fragmentChanger.onFragmentReplace(new RecurringList(), new Bundle());
                break;

            case EXPORT_PDF:
                try {
                    String pdfUrl = result.getString("pdfUrl");
                    pdfUrl.replace("\\", "");
                    new DownloadPdfFile(context).downloadAndOpenPDF(pdfUrl);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;

            case SEND:
                try {
                    if (global.isNetworkAvailable()) {

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject();
                            obj.put(Constant.KEY_METHOD, "getRecurring");
                            obj.put(Constant.KEY_RECURRING_ID, recurring_id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        request = new WebRequest(context, obj,
                                Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, false);

                        if (!recurring_id.isEmpty()) {
                            Home.progressBarHome.setVisibility(View.VISIBLE);
                            request.execute();
                        }
                    }
                    global.showAlert(result.getString("message"), context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void DeleteRecurringAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(Constant.title);
        alertDialog.setMessage("Are you sure you want to delete selected recurring?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (global.isNetworkAvailable()) {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonData = new JSONObject();
                    JSONArray recurringIdsArray;
                    try {
                        recurringIdsArray = new JSONArray();
                        recurringIdsArray.put(recurring_id);
                        jsonObject.put(Constant.KEY_RECURRING_STATUS, recurringStatus);
                        jsonObject.put(Constant.KEY_STATUS, "Active");
                        jsonObject.put(Constant.KEY_RECURRING_ID, recurringIdsArray);
                        jsonObject.put("type", "Delete");// Cancel - >delete ,
                        jsonObject.put("method", "updateInvoiceRecurringStatus");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.DELETE, Constant.token, webApiResult, true);
                    request.execute();
                }
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void setSenderData() {
        JSONObject object = null;
        try {
            String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                    + " From " + DatabaseHelper.Table_LoginData;

            Cursor cursor = db.getRecords(selectQuery);

            if (cursor.moveToFirst() && cursor.getCount() > 0)
                for (int i = 0; i < cursor.getCount(); i++) {
                    object = new JSONObject(cursor.getString(cursor
                            .getColumnIndex(DatabaseHelper.JSON_DATA)));
                }

            if (object == null)
                return;

            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            dateTV.setText(sdf.format(myCalendar.getTime()));
            senderNameTV.setText(object.getJSONObject("sender").getString("sender_name"));

            String senderAddress = object.getJSONObject("sender").getString("address");
            if (senderAddress.length() > 40)
                senderAddress = senderAddress.substring(0, 39) + "..";

            senderAddressTV.setText(senderAddress);
            senderPhoneTV.setText(object.getJSONObject("sender").getString("phone_number"));
            recurringNoTV.setText("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}