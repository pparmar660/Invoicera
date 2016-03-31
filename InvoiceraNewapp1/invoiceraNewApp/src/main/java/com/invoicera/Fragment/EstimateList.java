package com.invoicera.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.CustomView.CreateModulesOption;
import com.invoicera.CustomView.FloatingAction;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.CreateModule;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ViewPagerAdpter.EstimateListPagerAdapter;
import com.invoicera.ViewPagerFragment.EstimateListPagerFragment;
import com.invoicera.Webservices.DownloadPdfFile;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.Estimate_Filter;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.listener.FragmentChanger;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EstimateList extends BaseFragment implements
        ViewPager.OnPageChangeListener, WebApiResult, OnClickListener, CreateModule, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    ViewPager pager;
    EstimateListPagerFragment pagerFragment;
    ArrayList<EstimateListPagerFragment> pagerFragmentList;
    CreateModulesOption createModule;
    EstimateListPagerAdapter listPagerAdapter;
    // TabPageIndicator indicator;
    public FragmentChanger fragmentChanger;
    LayoutInflater localInflater;
    HorizontalScrollView scrollView;
    LinearLayout scrollLiner;
    public int selectedPagePosition = 0;
    public ImageView searchCancelImg;
    public RelativeLayout actionRelative;
    public TableLayout actionTable;
    public ImageView closeActionPopUp;
    public TableRow deleteLinear, archiveLinear, restoreLinear, exportPdfLinear, sendEstimateLinear;
    public static String content[] = {"Recent", "Draft", "Sent",
            "Viewed", "Expired", "Accepted", "Rejected"};
    WebApiResult webApiResult;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionButton rfaButton;
    RapidFloatingActionContentLabelList rfaContent;
    public boolean isSearchEnable = false;
    public boolean isFilterEnable = false;

    public TextView estimateNoTV;

    public String estimateId;
    public static int position = 0;

    public static String[] table_field = {
            DatabaseHelper.JSON_ESTIMATE_RECENT_LIST,
            DatabaseHelper.JSON_ESTIMATE_DRAFT_LIST,
            DatabaseHelper.JSON_ESTIMATE_SENT_LIST,
            DatabaseHelper.JSON_ESTIMATE_VIEWED_LIST,
            DatabaseHelper.JSON_ESTIMATE_EXPIRED_LIST,
            DatabaseHelper.JSON_ESTIMATE_ACCEPTED_LIST,
            DatabaseHelper.JSON_ESTIMATE_REJECTED_LIST

    };

    View view;
    TextView filterEstimate;
    public EditText searchEt;
    WebRequest request;

    FloatingAction floatingAction;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.estimate_list_fragment, container,
                false);


        createModule = new CreateModulesOption();
        Home.toolbarText.setText("Estimate");
        scrollView = (HorizontalScrollView) view
                .findViewById(R.id.gallery_scrolling_tabs);
        scrollLiner = (LinearLayout) view.findViewById(R.id.scrollLiner);
        pagerFragmentList = new ArrayList<>();
        pager = (ViewPager) view.findViewById(R.id.pager);
        listPagerAdapter = new EstimateListPagerAdapter(getChildFragmentManager());
        searchEt = (EditText) view.findViewById(R.id.searchEdit);
        filterEstimate = (TextView) view.findViewById(R.id.filter_estimate);
        pagerFragment = new EstimateListPagerFragment();
        searchCancelImg = (ImageView) view.findViewById(R.id.searchCancel);
        actionTable = (TableLayout) view.findViewById(R.id.actionTable);
        searchCancelImg.setOnClickListener(this);
        closeActionPopUp = (ImageView) view.findViewById(R.id.closeAction);
        actionRelative = (RelativeLayout) view.findViewById(R.id.actionRelative);
        /// deleteTV = (TextView) view.findViewById(R.id.delete);
        // archiveTV = (TextView) view.findViewById(R.id.archive);
        // exportAsPdfTV = (TextView) view.findViewById(R.id.exportPdf);
        //offlinePaymentTV = (TextView) view.findViewById(R.id.offlinePayment);
        estimateNoTV = (TextView) view.findViewById(R.id.estimateNo);
        // activeTV = (TextView) view.findViewById(R.id.active);
        restoreLinear = (TableRow) view.findViewById(R.id.restoreLinear);
        deleteLinear = (TableRow) view.findViewById(R.id.deleteLinear);
        archiveLinear = (TableRow) view.findViewById(R.id.archiveLinear);
        //convertToInvoiceLinear = (TableRow) view.findViewById(R.id.convertToInvoiceLinear);
        sendEstimateLinear = (TableRow) view.findViewById(R.id.sendEstimateLinear);
        //editEstimateLinear = (TableRow) view.findViewById(R.id.editLinear);
        exportPdfLinear = (TableRow) view.findViewById(R.id.exportPdfLinear);
        webApiResult = this;
        //activeTV.setOnClickListener(this);
        // deleteTV.setOnClickListener(this);
        // archiveTV.setOnClickListener(this);
        // exportAsPdfTV.setOnClickListener(this);
        // offlinePaymentTV.setOnClickListener(this);
        deleteLinear.setOnClickListener(this);
        archiveLinear.setOnClickListener(this);
        restoreLinear.setOnClickListener(this);
        // convertToInvoiceLinear.setOnClickListener(this);
        sendEstimateLinear.setOnClickListener(this);
        exportPdfLinear.setOnClickListener(this);
        //    editEstimateLinear.setOnClickListener(this);

        for (int i = 0; i < content.length; i++)

        {
            Bundle argument = new Bundle();
            argument.putInt(Constant.FRAGMENT_POS, i);
            pagerFragment = new EstimateListPagerFragment();
            pagerFragment.setArguments(argument);
            listPagerAdapter.add(pagerFragment);
            pagerFragmentList.add(pagerFragment);

        }
        searchCancelImg.setVisibility(View.GONE);
        pager.setAdapter(listPagerAdapter);
        // indicator.setViewPager(pager);
        pager.setOnPageChangeListener(this);
        //    createModuleImg = (ImageView) view.findViewById(R.id.createModule);
        //  createModuleImg.setOnClickListener(this);
        for (int i = 0; i < content.length; i++) {
            scrollLiner.addView(addIndicator(i));
        }


        filterEstimate.setOnClickListener(this);


        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        isSearchEnable = true;
                        pagerFragmentList.get(selectedPagePosition).pageNo = 1;
                        getSearchedEstimateList(searchEt.getText().toString(), pagerFragmentList.get(selectedPagePosition).pageNo, Constant.SERVICE_TYPE.GET_SEARCH_DATA, true, content[selectedPagePosition]);
                        searchCancelImg.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    } catch (JSONException e) {

                        Log.i("exception", e.getMessage());

                    }
                    return true;
                }
                return false;
            }
        });

        invalidateTabs(0);

        ///floating tap-------------
        rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaButton = (RapidFloatingActionButton) view.findViewById(R.id.label_list_sample_rfab);
        rfaLayout = (RapidFloatingActionLayout) view.findViewById(R.id.label_list_sample_rfal);

        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        floatingAction = new FloatingAction(context, fragmentChanger, rfaLayout, rfaButton, rfaContent);
        ;


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        Home.toolbarText.setText("Estimate");
        Log.e("onresume", "");

    }


    public View addIndicator(int position) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.indicater_text, scrollLiner,
                false);
        TextView textView = (TextView) view.findViewById(R.id.indicater_text);


        textView.setText(content[position]);
        textView.setTextColor(Color.WHITE);
        view.setTag(position);
        textView.setTag(position);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int position = Integer.parseInt(view.getTag().toString());
                    pager.setCurrentItem(position);
                } catch (NumberFormatException e) {

                    e.getMessage();
                    // TODO: handle exception
                }

            }
        });

        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        fragmentChanger = (FragmentChanger) activity;

        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(requestCode + "," + resultCode + "," + data);
        if (requestCode == Constant.requestCodeEstimateFilter) {
            if (data == null)
                return;
            String Estimate_st = data.getStringExtra(Constant.KEY_ESTIMATE_STATUS);
            String status = data.getStringExtra(Constant.KEY_STATUS);
            String client_id = data.getStringExtra(Constant.KEY_CLIENT_ID);
            String from = data.getStringExtra(Constant.KEY_DATE_FROM);
            String to = data.getStringExtra(Constant.KEY_DATE_TO);


            JSONObject obj = null;
            try {
                obj = new JSONObject();

                obj.put(Constant.KEY_METHOD, "filterEstimate");
                obj.put(Constant.KEY_CLIENT_ID, client_id);
                obj.put(Constant.KEY_DATE_FROM, from);
                obj.put(Constant.KEY_PAGE_NO, 1);
                obj.put(Constant.KEY_PAGE_PER_REORD, 200);
                obj.put(Constant.KEY_DATE_TO, to);
                obj.put(Constant.KEY_ESTIMATE_STATUS, Estimate_st);
                obj.put(Constant.KEY_STATUS, status);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            WebRequest request = new WebRequest(context, obj,
                    Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_FILTER_DATA, Constant.token, this, true);

            if (global.isNetworkAvailable()) {
                isFilterEnable = true;
                request.execute();
            } else
                global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);


        }
        if (requestCode == Constant.requestCodeOffLinePaymentFromList) {

            Bundle arguments = new Bundle();

            arguments.putString(Constant.KEY_ESTIMATE_ID, estimateId);
            arguments.putInt(Constant.KEY_POSITION, 1);

            if (global.isNetworkAvailable()) {

                fragmentChanger.onFragmentReplace(new EstimateList(), new Bundle());
            }

        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(final int arg0) {
        // TODO Auto-generated method stub
        invalidateTabs(arg0);
        searchEt.setText("");
        // pagerFragmentList.get(arg0).setList();
        selectedPagePosition = arg0;

        //Toast.makeText(context, arg0 + "", Toast.LENGTH_SHORT).show();

    }

    private void invalidateTabs(int currentItem) {
        int childCount = scrollLiner.getChildCount();
        for (int i = 0; i < childCount; i++) {
            view = scrollLiner.getChildAt(i);
            if (currentItem == Integer.parseInt(view.getTag().toString())) {

                view.setBackground(context.getResources().getDrawable(
                        R.drawable.tab_indicator_list_back));
                int vLeft = view.getLeft();
                int vRight = view.getRight();
                int sWidth = scrollView.getWidth();
                scrollView.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
            } else {
                view.setBackgroundColor(Color.parseColor("#E56E27"));
            }
        }
    }

    public void getSearchedEstimateList(String searchText, int page_no, Constant.SERVICE_TYPE type,
                                        boolean showProcessing, String estimate_status) throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put(Constant.KEY_METHOD, "searchEstimate");
        obj.put(Constant.KEY_PAGE_NO, page_no);
        obj.put(Constant.KEY_PAGE_PER_REORD, 200);
        obj.put(Constant.KEY_STATUS, "");
        obj.put(Constant.KEY_ESTIMATE_STATUS, estimate_status);
        obj.put("searchText", searchText);




		/*
         * WebRequest request = new WebRequest(context, obj,
		 * Constant.forgotPasswordURL, type, this); request.execute();
		 */
        WebRequest request = new WebRequest(context, obj,
                Constant.invoicelistURL, type, Constant.token, this, showProcessing);
        if (global.isNetworkAvailable())
            request.execute();
        else
            global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
    }


    @Override
    public void onClick(View v) {

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonData = new JSONObject();
        JSONArray estimateIdsArray;
        Intent i;
        switch (v.getId()) {
            case R.id.searchCancel:
                isSearchEnable = false;
                searchCancelImg.setVisibility(View.GONE);
                searchEt.setText("");

                pagerFragmentList.get(selectedPagePosition).setList();
                break;
            case R.id.filter_estimate:
                i = new Intent(context, Estimate_Filter.class);

                i.putExtra(Constant.KEY_ESTIMATE_STATUS, content[selectedPagePosition]);

                getActivity().startActivityForResult(i,
                        Constant.requestCodeEstimateFilter);// Activity


                break;


            case R.id.deleteLinear:

                DeleteEstimateAlertDialog();


                break;

            case R.id.archiveLinear:
                jsonObject = new JSONObject();
                jsonData = new JSONObject();

                try {
                    estimateIdsArray = new JSONArray();
                    estimateIdsArray.put(estimateId);
                    jsonObject.put("estimate_id", estimateIdsArray);
                    jsonObject.put("type", "Archived");// Cancel - >delete ,
                    jsonObject.put("estimate_status", content[selectedPagePosition]);
                    jsonObject.put("method", "changeEstimateStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ARCHIVE, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;


            case R.id.exportPdfLinear:

                jsonData = new JSONObject();

                try {
                    jsonData.put("estimate_id", estimateId);
                    jsonData.put("method", "viewPDF");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonData, Constant.invoicelistURL, Constant.SERVICE_TYPE.EXPORT_PDF, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;

            case R.id.restoreLinear:
                jsonObject = new JSONObject();
                jsonData = new JSONObject();

                try {

                    estimateIdsArray = new JSONArray();
                    estimateIdsArray.put(estimateId);
                    jsonObject.put("estimate_id", estimateIdsArray);
                    jsonObject.put("type", "Active");// Cancel - >delete ,
                    jsonObject.put("method", "changeEstimateStatus");
                    jsonObject.put("estimate_status", content[selectedPagePosition]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ACTIVE, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;


            case R.id.sendEstimateLinear:
                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);

                try {
                    JSONObject sendEstimateObject = new JSONObject();

                    sendEstimateObject.put("method", "sendEstimateMail");
                    sendEstimateObject.put("estimate_id", estimateId);

                    request = new WebRequest(context, sendEstimateObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.SEND_ESTIMATE_MAIL,
                            Constant.token, this, true);
                    if (global.isNetworkAvailable())
                        request.execute();
                    else
                        global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;


        }
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.e("pause", "");

    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {
      /*  Home.progressBarHome.setVisibility(View.GONE);*/
        if (result == null)
            return;
        try {


            if (!result.getString("code").equalsIgnoreCase("200")) {


                if (isSearchEnable) {

                    isSearchEnable = false;
                    searchCancelImg.setVisibility(View.GONE);
                    searchEt.setText("");
                }

                if (isFilterEnable)
                    isFilterEnable = false;

                if (result.has("message"))
                    Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_SHORT).show();
                return;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (type) {
            case GET_SEARCH_DATA:

                pagerFragmentList.get(selectedPagePosition).getWebResult(type, result);
                break;

            case GET_FILTER_DATA:

                pagerFragmentList.get(selectedPagePosition).getWebResult(type, result);
                break;


            case DELETE:
                pagerFragmentList.get(selectedPagePosition).adapter.estimateList.remove(position);
                pagerFragmentList.get(selectedPagePosition).adapter.notifyDataSetChanged();
                pagerFragmentList.get(selectedPagePosition).pageNo = 1;
                pagerFragmentList.get(selectedPagePosition).getWebResult(Constant.SERVICE_TYPE.GET_UPPER_DATA, result);

                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;


            case ARCHIVE:
                pagerFragmentList.get(selectedPagePosition).adapter.estimateList.remove(position);
                pagerFragmentList.get(selectedPagePosition).adapter.notifyDataSetChanged();
                pagerFragmentList.get(selectedPagePosition).pageNo = 1;
                pagerFragmentList.get(selectedPagePosition).getWebResult(Constant.SERVICE_TYPE.GET_UPPER_DATA, result);

                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;
            case ACTIVE:


                pagerFragmentList.get(selectedPagePosition).pageNo = 1;
                pagerFragmentList.get(selectedPagePosition).getWebResult(Constant.SERVICE_TYPE.GET_UPPER_DATA, result);
                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
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
                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);

                break;
            case SEND_ESTIMATE_MAIL:
                try {
                    global.showAlert("Email has been has sent", context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);

                break;

        }
    }

    @Override
    public void getResultOfCreateModule(Constant.CREATE_MODULE type) {

        switch (type) {

 /*           case INVOICE:
                createModuleImg.setVisibility(View.VISIBLE);

                fragmentChanger.onFragmentReplaceWithBackStack(
                        new InvoicePreviewCreateFragment(),
                        Constant.InvoicePreviewCreateFragmentTag, new Bundle());

                break;
            case ESTIMATE:
                createModuleImg.setVisibility(View.VISIBLE);

                fragmentChanger.onFragmentReplaceWithBackStack(
                        new EstimatePreviewCreateFragment(),
                        Constant.EstimatePreviewCreateFragmentTag, new Bundle());
                break;

            case CANCEL:
                createModuleImg.setVisibility(View.VISIBLE);

                break;
*/

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

                        JSONArray estimateIdsArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonData = new JSONObject();
                        estimateIdsArray.put(estimateId);

                        try {
                            jsonObject.put("estimate_id", estimateIdsArray);
                            jsonObject.put("type", "Delete");// Cancel - >delete ,

                            jsonObject.put("method", "changeEstimateStatus");
                            jsonObject.put("estimate_status", content[selectedPagePosition]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        request = new WebRequest(context, jsonObject,
                                Constant.invoicelistURL, Constant.SERVICE_TYPE.DELETE, Constant.token, webApiResult, true);


                        if (global.isNetworkAvailable())
                            request.execute();
                        else
                            global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);

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

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        // showToastMessage("clicked label: " + position);
        // rfabHelper.toggleContent();
        floatingAction.createFunction(position);

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        // rfabHelper.toggleContent();
        floatingAction.createFunction(position);

        // showToastMessage("clicked icon: " + position);

    }


}
