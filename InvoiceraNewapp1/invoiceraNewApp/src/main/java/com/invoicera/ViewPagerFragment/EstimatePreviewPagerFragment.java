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
import android.util.Log;
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
import com.invoicera.Fragment.EstimateList;
import com.invoicera.Fragment.EstimatePreviewCreateFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Utility.MyDateFormat;
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
 * Created by Parvesh on 30/7/15.
 */
public class EstimatePreviewPagerFragment extends EstimatePreviewCreateFragment implements WebApiResult, View.OnClickListener {
    DatabaseHelper db;
    ContentValues values;
    TextView senderNameTV, senderAddressTV, senderPhoneTV, estimateNoTV, dateTV, totalAdditionalChargesTV, totalItemTaxTV;
    TextView clientNameTV, clientAddTV, clientPhoneTV, subTotalTV, estimateTotalTV,
            netBalanceTV, discountTV, balanceAfterDiscountTV;
    TableLayout itemTable;
    ImageView logoImage;
    LinearLayout clientDetailLinear;
    TableRow taxLinear, estimateTotalLinear, valueAfterDiscountLinear,
            discountLinear, additionalChargeLinear, netBalanceLiner;
    ArrayList<ItemTaxModel> taxModelsList;
    public String estimate_id = "", estimateStatus = "";
    public static String ClientCurrency = "";
    boolean isShowProcessing = false;
    TableLayout amountDetailLinear;
    WebApiResult webApiResult;
    WebRequest request;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    //Button chooseActionButton;
    public FragmentChanger fragmentChanger;
    TextView deleteTV, exportPdfTV, sendTV, amountTypeTV;
    EstimatePreviewCreateFragment parentFragment;
    Calendar myCalendar;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.estimate_preview, container, false);
        Home.toolbarText.setText("Estimate");

        Bundle argument = getArguments();

        if (argument.getString(Constant.KEY_ESTIMATE_ID) != null) {
            if (!argument.getString(Constant.KEY_ESTIMATE_ID).isEmpty())
                estimate_id = argument.getString(Constant.KEY_ESTIMATE_ID);
            if (argument.getString(Constant.KEY_ESTIMATE_STATUS) != null)
                estimateStatus = argument.getString(Constant.KEY_ESTIMATE_STATUS);
        }
        myCalendar = Calendar.getInstance();
        db = new DatabaseHelper(context);
        webApiResult = this;
        parentFragment = (EstimatePreviewCreateFragment) getParentFragment();

        senderAddressTV = (TextView) view.findViewById(R.id.senderAddress);
        senderNameTV = (TextView) view.findViewById(R.id.senderName);
        senderPhoneTV = (TextView) view.findViewById(R.id.senderNumber);
        estimateNoTV = (TextView) view.findViewById(R.id.estimateNumber);

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
        estimateTotalTV = (TextView) view.findViewById(R.id.estimateTotalValue);
        estimateTotalLinear = (TableRow) view.findViewById(R.id.estimateTotalLinear);
        valueAfterDiscountLinear = (TableRow) view.findViewById(R.id.valueAfterDiscountLiner);
        discountLinear = (TableRow) view.findViewById(R.id.discountLinear);
        additionalChargeLinear = (TableRow) view.findViewById(R.id.addtionalChargeLinear);
        netBalanceLiner = (TableRow) view.findViewById(R.id.netBalanceLiner);
        totalAdditionalChargesTV = (TextView) view.findViewById(R.id.totalAftreAdditionalCharges);
        totalItemTaxTV = (TextView) view.findViewById(R.id.totalItemTax);
        amountDetailLinear = (TableLayout) view.findViewById(R.id.amountDetailLinear);
        clientDetailLinear = (LinearLayout) view.findViewById(R.id.clientInfoLinear);
        amountTypeTV = (TextView) view.findViewById(R.id.amountType);
        deleteTV = (TextView) view.findViewById(R.id.delete);
        exportPdfTV = (TextView) view.findViewById(R.id.exportPdf);
        sendTV = (TextView) view.findViewById(R.id.send);


        deleteTV.setOnClickListener(this);
        exportPdfTV.setOnClickListener(this);
        sendTV.setOnClickListener(this);


        //chooseActionButton.setOnClickListener(this);

        //--------------------------
        EstimateCreateEditPagerFragment fragment = (EstimateCreateEditPagerFragment) parentFragment.pagerAdapter.getItem(0);
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

        if (!estimate_id.isEmpty()) {


            //---------------Load data from local

            String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                    + " From " + DatabaseHelper.Table_EstimatePreviewData + " WHERE " + DatabaseHelper.ID + " ='" + estimate_id + "'";
            Cursor cursor = db.getRecords(selectQuery);


            if (cursor.moveToFirst() && cursor.getCount() > 0) {


                //totalItem = new ArrayList<>();
                for (int i = 0; i < cursor.getCount(); i++) {

                    try {

                        JSONObject object = (new JSONObject
                                (cursor.getString(cursor.getColumnIndex(db.JSON_DATA)))).getJSONObject("estimate");
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

            obj.put(Constant.KEY_METHOD, "getEstimate");
            obj.put(Constant.KEY_ESTIMATE_ID, estimate_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebRequest request = new WebRequest(context, obj,
                Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, isShowProcessing);

        if (!estimate_id.isEmpty()) {
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

                String logoUrl = object.getString("logo_url");

                logoUrl.replace("\\", "");
              /*  System.out.println(logoUrl);
                logoUrl="https://"+logoImage;*/

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
                        // Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }
                });

                //--


                //itemTable.removeAllViews();


                clientNameTV.setText(object.getJSONObject("client").getString("client_name"));

                if (object.getJSONObject("client").getString("address").isEmpty())
                    clientAddTV.setText("N/A");
                else
                    clientAddTV.setText("Add :" + object.getJSONObject("client").getString("address"));
                if (object.getJSONObject("client").getString("phone_number").isEmpty())
                    clientPhoneTV.setText("Ph: N/A");
                else
                    clientPhoneTV.setText("Ph :" + object.getJSONObject("client").getString("phone_number"));

                ClientCurrency = object.getJSONObject("client").getString("currency");


                senderNameTV.setText(object.getJSONObject("sender").getString("sender_name"));

                String senderAddress = object.getJSONObject("sender").getString("address");
                if (senderAddress.length() > 40)
                    senderAddress = senderAddress.substring(0, 39) + "..";

                senderAddressTV.setText(senderAddress);
                senderPhoneTV.setText(object.getJSONObject("sender").getString("phone_number"));
                dateTV.setText(MyDateFormat.GetDate(object.getString("date")));
                estimateNoTV.setText(object.getString("number"));


                //      Log.e("Child count1 :",from+"--"+itemTable.getChildCount()+"");

                for (int j = 0; j < object.getJSONObject("items").getJSONArray("item").length(); j++) {

                    JSONObject itemObj = object.getJSONObject("items").getJSONArray("item").getJSONObject(j);

                    Log.e("look list items#####", itemObj.toString());

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                            .LAYOUT_INFLATER_SERVICE);
                    View itemView = inflater.inflate(R.layout.item_table_row, null);


                    TextView itemName = (TextView) itemView.findViewById(R.id.item_name);
                    TextView itemDescription = (TextView) itemView.findViewById(R.id.item_description);
                    TextView qtyAndUc = (TextView) itemView.findViewById(R.id.qty_uc);
                    TextView discount = (TextView) itemView.findViewById(R.id.discount);
                    TextView price = (TextView) itemView.findViewById(R.id.price);


                    itemName.setText(global.setLength(itemObj.getString("name"), Constant.defultLengthOfText));

             /*       if (!itemObj.getString("total_discountValue").isEmpty())
                        discount.setText(global.setLength(itemObj.getString("total_discountValue")));
                    else
                        discount.setText("0.0");*/
                    if (!itemObj.getString("discount").isEmpty()) {
                        if (itemObj.getString("discount_type").equalsIgnoreCase("Percent"))
                            discount.setText(global.setLength(itemObj.getString("discount"), Constant.defultLengthOfText) + " %");
                        else
                            discount.setText(global.setLength(itemObj.getString("discount"), Constant.defultLengthOfText));

                    } else
                        discount.setText("0.0");
                    price.setText(global.setLength(itemObj.getString("total_priceValue"), Constant.defultLengthOfText));

       /*             if (itemObj.getString("description").isEmpty())
                        itemDescription.setVisibility(View.GONE);
                    else
                        itemDescription.setVisibility(View.VISIBLE);
                    itemDescription.setText(itemObj.getString("description"));*/


                    qtyAndUc.setText("QTY:" + global.setLength(itemObj.getString("quantity"), Constant.defultLengthOfText) + "\nUC:" + global.setLength(itemObj.getString("unit_cost"), Constant.defultLengthOfText));

                    itemTable.addView(itemView);


                    if (!itemObj.getString("tax1_type").isEmpty()) {

                        ItemTaxModel taxModel = new ItemTaxModel();
                        taxModel.setName(itemObj.getString("tax1_name"));
                        taxModel.setType(itemObj.getString("tax1_type"));
                        taxModel.setValue(itemObj.getString("tax1_value"));
                        taxModel.setPercentage(itemObj.getString("tax1_percent"));
                        taxModelsList.add(taxModel);


                    }


                    if (!itemObj.getString("tax2_type").isEmpty()) {

                        ItemTaxModel ItemTaxModel = new ItemTaxModel();
                        ItemTaxModel.setName(itemObj.getString("tax2_name"));
                        ItemTaxModel.setType(itemObj.getString("tax2_type"));
                        ItemTaxModel.setValue(itemObj.getString("tax2_value"));
                        ItemTaxModel.setPercentage(itemObj.getString("tax2_percent"));
                        taxModelsList.add(ItemTaxModel);


                    }


                }

                //    Log.e("Child count2 :",itemTable.getChildCount()+"");
                subTotalTV.setText(global.setLength(object.getString("sub_total"), Constant.defultLengthOfText));
                taxLinear.setVisibility(View.GONE);
/*
                for (int k = 0; k < taxModelsList.size(); k++) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                            .LAYOUT_INFLATER_SERVICE);
                    View taxRow = inflater.inflate(R.layout.item_tax_row, null);
                    TextView taxName = (TextView) taxRow.findViewById(R.id.taxName);

                    TextView taxValue = (TextView) taxRow.findViewById(R.id.taxValue);


                    taxName.setText(taxModelsList.get(k).getName());
                    taxValue.setText(taxModelsList.get(k).getValue());
                    taxLinear.addView(taxRow);


                }*/

                if (!object.getString("total_tax").isEmpty()) {
                    taxLinear.setVisibility(View.VISIBLE);
                    totalItemTaxTV.setText(global.setLength(object.getString("total_tax"), Constant.defultLengthOfText));

                }
                estimateTotalLinear.setVisibility(View.GONE);


                if (!taxModelsList.isEmpty()) {


                    estimateTotalLinear.setVisibility(View.VISIBLE);
                    estimateTotalTV.setText(global.setLength(object.getString("estimate_total"), Constant.defultLengthOfText));

                }

                // set discount
                valueAfterDiscountLinear.setVisibility(View.GONE);
                discountLinear.setVisibility(View.GONE);
                if (!object.getString("EstimateWiseDiscountValue").isEmpty())
                    if (Double.parseDouble(object.getString("EstimateWiseDiscountValue").replaceAll(",", "")) > 0) {
                        discountLinear.setVisibility(View.VISIBLE);
                        valueAfterDiscountLinear.setVisibility(View.VISIBLE);


                        if ((object.has("EstimateWiseDiscountCalValue")))
                            discountTV.setText(global.setLength(object.getString("EstimateWiseDiscountCalValue"), Constant.defultLengthOfText));
                        balanceAfterDiscountTV.setText(global.setLength(object.getString("after_discount_total"), Constant.defultLengthOfText));

                    }

                additionalChargeLinear.setVisibility(View.GONE);
                if (!object.getString("AdditionalChargeTotal").isEmpty()) {
                    additionalChargeLinear.setVisibility(View.VISIBLE);
                    totalAdditionalChargesTV.setText(global.setLength(object.getString("AdditionalChargeTotal"), Constant.defultLengthOfText));


                }

                netBalanceTV.setText(global.setLength(object.getString("net_balance"), Constant.defultLengthOfText));

                amountTypeTV.setText("Amount(" + ClientCurrency + ")");


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void onClick(View v) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonData = new JSONObject();
        JSONArray estimateIdsArray;
        switch (v.getId()) {


            case R.id.delete:

                DeleteEstimateAlertDialog();


                break;
            case R.id.send:
                jsonData = new JSONObject();

                try {
                    jsonData.put("estimate_id", estimate_id);
                    jsonData.put("method", "sendEstimateMail");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonData, Constant.invoicelistURL, Constant.SERVICE_TYPE.SEND, Constant.token, this, true);
                request.execute();

                break;

            case R.id.exportPdf:
                jsonData = new JSONObject();

                try {
                    jsonData.put("estimate_id", estimate_id);
                    jsonData.put("method", "viewPDF");
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

                db.ClearTable(DatabaseHelper.Table_EstimatePreviewData);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact
                values.put(DatabaseHelper.ID, estimate_id);
                db.insert(DatabaseHelper.Table_EstimatePreviewData, values);


                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_EstimatePreviewData + " WHERE " + DatabaseHelper.ID + " ='" + estimate_id + "'";

                Cursor cursor = db.getRecords(selectQuery);
                taxModelsList = new ArrayList<>();

                if (cursor.moveToFirst() && cursor.getCount() > 0) {


                    //totalItem = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {

                            JSONObject object = (new JSONObject
                                    (cursor.getString(cursor.getColumnIndex(db.JSON_DATA)))).getJSONObject("estimate");
                            setData(object);


                            EstimateCreateEditPagerFragment createEditEstimatePagerFragment = (EstimateCreateEditPagerFragment) parentFragment.pagerAdapter.getItem(0);
                            createEditEstimatePagerFragment.setDataForEditing(estimate_id);

                            EstimateInformationPagerFragment informationEstimatePagerFragment = (EstimateInformationPagerFragment)
                                    parentFragment.pagerAdapter.getItem(2);
                            informationEstimatePagerFragment.setData(object);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }


                break;

            case DELETE:

                values = new ContentValues();
                values.put(EstimateList.table_field[EstimateList.position], (String) null);
                db.clearTableColumn(DatabaseHelper.Table_EstimateList, values);


                values = new ContentValues();
                values.put(EstimateList.table_field[EstimateList.position], result.toString()); // Contact

                db.insert(DatabaseHelper.Table_EstimateList, values);
                fragmentChanger.onFragmentReplace(new EstimateList(), new Bundle());
               
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

                            obj.put(Constant.KEY_METHOD, "getEstimate");
                            obj.put(Constant.KEY_ESTIMATE_ID, estimate_id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        request = new WebRequest(context, obj,
                                Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, false);

                        if (!estimate_id.isEmpty()) {
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

    public void DeleteEstimateAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(Constant.title);
        alertDialog.setMessage("Are you sure you want to delete selected estimate ?");
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (global.isNetworkAvailable()) {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonData = new JSONObject();
                            JSONArray EstimateIdsArray;

                            try {

                                EstimateIdsArray = new JSONArray();
                                EstimateIdsArray.put(estimate_id);
                                jsonObject.put(Constant.KEY_ESTIMATE_STATUS, estimateStatus);
                                jsonObject.put(Constant.KEY_STATUS, "Active");
                                jsonObject.put("estimate_id", EstimateIdsArray);
                                jsonObject.put("type", "Delete");// Cancel - >delete ,
                                jsonObject.put("method", "changeEstimateStatus");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.DELETE,
                                    Constant.token, webApiResult, true);
                            request.execute();
                        }

                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
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

            estimateNoTV.setText("");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}