package com.invoicera.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.CustomView.FloatingAction;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.RecurringListPagerFragmentListAdapter;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Webservices.DownloadPdfFile;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.androidapp.Recurring_Filter;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.RecurringAttribute;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Parvesh on 25/9/15.
 */
public class RecurringList extends BaseFragment implements WebApiResult, View.OnClickListener,
        RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {


    boolean isTotalRecordGet = false;
    int total_pagesInt = 0;
    public ImageView searchCancelImg;
    public RelativeLayout actionRelative;
    public TableLayout actionTable;
    public ImageView closeActionPopUp;
    public ListView listView;
    int pageNo=1;
    View listFooter;
    RecurringListPagerFragmentListAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    WebApiResult webApiResult;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    RapidFloatingActionContentLabelList rfaContent;
    public boolean isSearchEnable = false;
    public boolean isFilterEnable = false;

    public TextView RecurringNoTV;
    TableRow startRecurring, stopRecurring, exportPdf, sendRecurring, markArchive, active, delete;

    public String recurringId;
    public static int position = 0;
    int recordPerPage = 100;
    boolean isLoading = false;


    View view;
    TextView filterRecurring;
    public EditText searchEt;
    WebRequest request;

    FloatingAction floatingAction;
    public boolean isActionPopOpen = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recurring_list_fragment, container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        position = getArguments().getInt(Constant.FRAGMENT_POS);

        Home.toolbarText.setText("Recurring");


        listView = (ListView) view.findViewById(R.id.list);

        searchEt = (EditText) view.findViewById(R.id.searchEdit);
        filterRecurring = (TextView) view.findViewById(R.id.filter_recurring);

        searchCancelImg = (ImageView) view.findViewById(R.id.searchCancel);
        actionTable = (TableLayout) view.findViewById(R.id.actionTable);
        searchCancelImg.setOnClickListener(this);
        closeActionPopUp = (ImageView) view.findViewById(R.id.closeAction);
        closeActionPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);

            }
        });

        actionRelative = (RelativeLayout) view.findViewById(R.id.actionRelative);
        actionRelative.setOnClickListener(this);
        LayoutInflater footerInflater = LayoutInflater.from(getActivity());
        listFooter = footerInflater.inflate(R.layout.list_footer, null, false);
        listFooter.setVisibility(View.GONE);

        adapter = new RecurringListPagerFragmentListAdapter(context);
        listView.setAdapter(adapter);
        listView.addFooterView(listFooter);
        listFooter.setOnClickListener(null);

        RecurringNoTV = (TextView) view.findViewById(R.id.recurringNo);

        startRecurring = (TableRow) view.findViewById(R.id.startLinear);
        stopRecurring = (TableRow) view.findViewById(R.id.stop_recurringlinear);
        exportPdf = (TableRow) view.findViewById(R.id.exportPdflinear);
        sendRecurring = (TableRow) view.findViewById(R.id.send_recurringlinear);
        markArchive = (TableRow) view.findViewById(R.id.archiveLinear);
        active = (TableRow) view.findViewById(R.id.activelinear);
        delete = (TableRow) view.findViewById(R.id.deleteLinear);


        webApiResult = this;


        startRecurring.setOnClickListener(this);
        stopRecurring.setOnClickListener(this);
        exportPdf.setOnClickListener(this);
        sendRecurring.setOnClickListener(this);
        markArchive.setOnClickListener(this);
        active.setOnClickListener(this);
        delete.setOnClickListener(this);

        ImageView img_search = (ImageView) view.findViewById(R.id.img_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getSearchedRecurringList(searchEt.getText().toString(), pageNo, Constant.SERVICE_TYPE.GET_SEARCH_DATA, true, "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        searchCancelImg.setVisibility(View.GONE);

        // indicator.setViewPager(pager);  pager.setOnPageChangeListener(this);
        //    createModuleImg = (ImageView) view.findViewById(R.id.createModule);
        //  createModuleImg.setOnClickListener(this);

        filterRecurring.setOnClickListener(this);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                         if (searchEt.getText().toString().isEmpty())
                             return false;


                        isSearchEnable = true;

                        getSearchedRecurringList(searchEt.getText().toString(), pageNo, Constant.SERVICE_TYPE.GET_SEARCH_DATA, true, "0");
                        searchCancelImg.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    } catch (Exception e) {

                        Log.i("exception", e.getMessage());

                    }
                    return true;
                }
                return false;
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {

                    if (isSearchEnable) {
                        isSearchEnable = false;
                        searchCancelImg.setVisibility(View.GONE);
                    }
                    if (isFilterEnable)
                        isFilterEnable = false;

                    pageNo = 1;
                    getRecurringList(pageNo, Constant.SERVICE_TYPE.GET_DATA, false);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark, android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_red_light);


        ///floating tap-------------
        rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaButton = (RapidFloatingActionButton) view.findViewById(R.id.label_list_sample_rfab);
        rfaLayout = (RapidFloatingActionLayout) view.findViewById(R.id.label_list_sample_rfal);

        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        floatingAction = new FloatingAction(context, fragmentChanger, rfaLayout, rfaButton, rfaContent);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position >= adapter.RecurringList.size()) return;
                Bundle arguments = new Bundle();
                arguments.putString(Constant.KEY_RECURRING_ID, adapter.RecurringList.get(position).getRecurring_id());
                arguments.putString(Constant.KEY_RECURRING_STATUS, adapter.RecurringList.get(position).getStatus());
                arguments.putInt(Constant.KEY_POSITION, 1);

                if (global.isNetworkAvailable()) {
                    fragmentChanger.onFragmentAddWithBackStack(new RecurringPreviewCreateFragment(), Constant.RecurringPreviewCreateFragmentTag, arguments);
                } else global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!global.isNetworkAvailable()) {
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                    return true;
                }

                isActionPopOpen = true;
                actionRelative.setVisibility(View.VISIBLE);
                recurringId = adapter.RecurringList.get(position).getRecurring_id();
                RecurringNoTV.setText("Recurring no : " + adapter.RecurringList.get(position).getRecurring_no());
                String recurringId = adapter.RecurringList.get(position).getRecurring_id();
//                recurringNoTV.setText("Recurring no : " + adapter.RecurringList.get(position).getRecurring_no());
                position = position;

                if (adapter.RecurringList.get(position).getStatus().equalsIgnoreCase("Delete")) {
                    active.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    markArchive.setVisibility(View.GONE);
                    startRecurring.setVisibility(View.GONE);
                    stopRecurring.setVisibility(View.GONE);
                    sendRecurring.setVisibility(View.GONE);


                } else if (adapter.RecurringList.get(position).getStatus().equalsIgnoreCase("Archived")) {
                    active.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    markArchive.setVisibility(View.GONE);
                    startRecurring.setVisibility(View.VISIBLE);
                    stopRecurring.setVisibility(View.VISIBLE);

                    sendRecurring.setVisibility(View.VISIBLE);
                } else if (adapter.RecurringList.get(position).getStatus().equalsIgnoreCase("Stop")) {
                    active.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    markArchive.setVisibility(View.VISIBLE);
                    startRecurring.setVisibility(View.VISIBLE);
                    stopRecurring.setVisibility(View.GONE);
                    sendRecurring.setVisibility(View.GONE);
                } else if (adapter.RecurringList.get(position).getStatus().equalsIgnoreCase("Start")) {
                    active.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    markArchive.setVisibility(View.VISIBLE);
                    startRecurring.setVisibility(View.GONE);
                    stopRecurring.setVisibility(View.VISIBLE);
                    sendRecurring.setVisibility(View.VISIBLE);
                }else {
                    active.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                    markArchive.setVisibility(View.VISIBLE);
                    startRecurring.setVisibility(View.VISIBLE);
                    stopRecurring.setVisibility(View.VISIBLE);
                    sendRecurring.setVisibility(View.VISIBLE);
                }


                animation(actionTable, R.anim.bottom_up, Constant.LIST_ACTION_ANIMATION_TYPE.OPEN);


                return true;


            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                int currentScrollState = scrollState;


            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                Log.e("", firstVisibleItem + "," + visibleItemCount + "," + "," + totalItemCount);

                if ((firstVisibleItem + visibleItemCount == totalItemCount)
                        && !isLoading && (visibleItemCount != totalItemCount)) {


                    try {

                        if (isSearchEnable)
                            return;
                        if (isFilterEnable) {

                            return;
                        }

                        if (!global.isNetworkAvailable()) {
                            global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                            return;
                        }
                        if (isTotalRecordGet)
                            return;

                        if (pageNo + 1 > total_pagesInt) {
                            isTotalRecordGet=true;
                            global.showAlert("No more record", context);

                            return;
                        }


                        listFooter.setVisibility(View.VISIBLE);
                        isLoading = true;

                        try {

                            ++pageNo;
                            getRecurringList(pageNo, Constant.SERVICE_TYPE.GET_DATA, false);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        if (position == 0)
            setList();
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("visible ", position + "");
            setList();
        }
    }

    public void setList() {

        if (db == null)
            return;
        String selectQuery = "Select " + DatabaseHelper.JSON_DATA + " from " + DatabaseHelper.Table_RecurringList;

        Cursor cursor = db.getRecords(selectQuery);
      /*  Toast.makeText(
                context,
                "cusror Length " + position + "," + cursor.getCount() + ","
                        + cursor.moveToFirst(), Toast.LENGTH_SHORT).show();*/
        try {
            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {


                    updateUI(new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA))));
                    pageNo = 1;

                    getRecurringList(pageNo, Constant.SERVICE_TYPE.GET_DATA, false);

                }
            } else {
                pageNo = 1;
                getRecurringList(pageNo, Constant.SERVICE_TYPE.GET_DATA, true);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getRecurringList(int page_no, Constant.SERVICE_TYPE type, boolean showProcessing) throws JSONException {

        /*{"method":"listRecurring","page":"1","per_page_record":"100","status":"Active","recurring_status":""}*/

        JSONObject obj = new JSONObject();
        obj.put(Constant.KEY_METHOD, "listRecurring");
        obj.put("page", page_no);
        obj.put("per_page_record", recordPerPage);
        obj.put("status", "");
        obj.put("recurring_status", "");

        Home.progressBarHome.setVisibility(View.VISIBLE);

        request = new WebRequest(context, obj, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, false);
        request.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onresume", "");
    }


    @Override
    public void onAttach(Activity activity) {
        fragmentChanger = (FragmentChanger) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(requestCode + "," + resultCode + "," + data);
        if (requestCode == Constant.requestCodeRecurringFilter) {
            if (data == null)
                return;
            String Recurring_st = data.getStringExtra(Constant.KEY_RECURRING_STATUS);
            String status = data.getStringExtra(Constant.KEY_STATUS);
            String client_id = data.getStringExtra(Constant.KEY_CLIENT_ID);
            String from = data.getStringExtra(Constant.KEY_DATE_FROM);
            String to = data.getStringExtra(Constant.KEY_DATE_TO);
            String frequency = data.getStringExtra(Constant.KEY_FREQUENCY);
            String occurrence = data.getStringExtra(Constant.KEY_OCCURRENCE);

            /*{"method":"filterRecurring","client_id":"","date_to":"","date_from":"","occurrence":"","status":"",
            "frequency":"","page":"1","per_page_record":"200","auto_billed":"Y/N"}*/


            JSONObject obj = null;
            try {
                obj = new JSONObject();
                obj.put(Constant.KEY_METHOD, "filterRecurring");
                obj.put(Constant.KEY_CLIENT_ID, client_id);
                obj.put(Constant.KEY_DATE_TO, to);
                obj.put(Constant.KEY_DATE_FROM, from);
                obj.put(Constant.KEY_OCCURRENCE, occurrence);
                obj.put(Constant.KEY_STATUS, status);
                obj.put(Constant.KEY_FREQUENCY, frequency);
                obj.put(Constant.KEY_PAGE_NO, 1);
                obj.put(Constant.KEY_PAGE_PER_REORD, 200);
                obj.put("auto_billed", Recurring_st);
//                obj.put(Constant.KEY_RECURRING_STATUS, Recurring_st);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            WebRequest request = new WebRequest(context, obj, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_FILTER_DATA, Constant.token, this, true);

            if (global.isNetworkAvailable()) {
                isFilterEnable = true;
                request.execute();
            } else
                global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);


        }

    }

    /*{"method":"searchRecurring","searchText":"#INR","status":"","page":"1","per_page_record":"50"}*/
    public void getSearchedRecurringList(String searchText, int page_no, Constant.SERVICE_TYPE type, boolean showProcessing, String recurring_status)
            throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put(Constant.KEY_METHOD, "searchRecurring");
        obj.put("searchText", searchText);
        obj.put(Constant.KEY_PAGE_NO, page_no);
        obj.put(Constant.KEY_PAGE_PER_REORD, 200);
        obj.put(Constant.KEY_STATUS, "");
//        obj.put(Constant.KEY_RECURRING_STATUS, recurring_status);


        WebRequest request = new WebRequest(context, obj, Constant.invoicelistURL, type, Constant.token, this, showProcessing);
        if (global.isNetworkAvailable())
            request.execute();
        else
            global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonData = new JSONObject();
        JSONArray recurringIdsArray;
        switch (v.getId()) {
            case R.id.searchCancel:
                isSearchEnable = false;
                searchCancelImg.setVisibility(View.GONE);
                searchEt.setText("");
                setList();
                break;

            case R.id.filter_recurring:
                i = new Intent(context, Recurring_Filter.class);
                getActivity().startActivityForResult(i, Constant.requestCodeRecurringFilter);// Activity
                break;

            case R.id.startLinear:
                jsonObject = new JSONObject();
                jsonData = new JSONObject();

                try {
                    recurringIdsArray = new JSONArray();
                    recurringIdsArray.put(recurringId);
                    jsonObject.put(Constant.KEY_RECURRING_ID, recurringIdsArray);
                    jsonObject.put("type", "Active");
                    jsonObject.put("method", "updateInvoiceRecurringStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.START_RECURRING, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;

            case R.id.stop_recurringlinear:
                jsonObject = new JSONObject();
                jsonData = new JSONObject();

                try {
                    recurringIdsArray = new JSONArray();
                    recurringIdsArray.put(recurringId);
                    jsonObject.put(Constant.KEY_RECURRING_ID, recurringIdsArray);
                    jsonObject.put("type", "Stop");
                    jsonObject.put("method", "updateInvoiceRecurringStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.STOP_RECURRING, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;

            case R.id.exportPdflinear:
                jsonData = new JSONObject();

                try {
                    jsonData.put(Constant.KEY_RECURRING_ID, recurringId);
                    jsonData.put("method", "viewRecurringPDF");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonData, Constant.invoicelistURL, Constant.SERVICE_TYPE.EXPORT_PDF, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;

            case R.id.send_recurringlinear:
                jsonObject = new JSONObject();
                jsonData = new JSONObject();

                try {
//                    recurringIdsArray = new JSONArray();
//                    recurringIdsArray.put(recurringId);
                    jsonObject.put(Constant.KEY_RECURRING_ID, recurringId);
//                    jsonObject.put("type", "Active");
                    jsonObject.put("method", "sendRecurring");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.SEND, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;

            case R.id.archiveLinear:
                jsonObject = new JSONObject();
                jsonData = new JSONObject();

                try {
                    recurringIdsArray = new JSONArray();
                    recurringIdsArray.put(recurringId);
                    jsonObject.put(Constant.KEY_RECURRING_ID, recurringIdsArray);
                    jsonObject.put("type", "Archived");// Cancel - >delete ,
                    jsonObject.put("method", "updateInvoiceRecurringStatus");

                } catch (JSONException e) {
                    e.printStackTrace();//{"method":"updateInvoiceRecurringStatus","type":"Archived","recurring_id":"62"}
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ARCHIVE, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);

                break;

            case R.id.activelinear:
                jsonObject = new JSONObject();
                jsonData = new JSONObject();

                try {
                    recurringIdsArray = new JSONArray();
                    recurringIdsArray.put(recurringId);
                    jsonObject.put(Constant.KEY_RECURRING_ID, recurringIdsArray);
                    jsonObject.put("type", "Active");
                    jsonObject.put("method", "updateInvoiceRecurringStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ACTIVE, Constant.token, this, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);

                break;

            case R.id.deleteLinear:
                DeleteRecurringAlertDialog();
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
        listFooter.setVisibility(View.GONE);
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

        ContentValues values;
        swipeContainer.setRefreshing(false);

        switch (type) {
            case GET_DATA:
                try {
                    if (pageNo == 1) {

                        db.ClearTable(DatabaseHelper.Table_RecurringList);
                        values = new ContentValues();
                        values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact

                        db.insert(DatabaseHelper.Table_RecurringList, values);


                        String selectQuery = "Select " + DatabaseHelper.JSON_DATA +
                                " From " + DatabaseHelper.Table_RecurringList;

                        Cursor cursor = db.getRecords(selectQuery);

                        if (cursor.moveToFirst() && cursor.getCount() > 0)
                            for (int i = 0; i < cursor.getCount(); i++) {
                                updateUI(new JSONObject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JSON_DATA))));
                            }

                    } else {
                        isLoading = false;
                        updateUI(result);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case GET_SEARCH_DATA:
                pageNo = 1;
                updateUI(result);
                break;

            case GET_FILTER_DATA:
                pageNo = 1;
                updateUI(result);
                break;

            case DELETE:

                pageNo = 1;
                updateUI(result);
                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;

            case ARCHIVE:
                pageNo = 1;

                updateUI(result);
                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;

            case ACTIVE:

                pageNo = 1;
                updateUI(result);
                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;

            case EXPORT_PDF:
                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);

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
                    global.showAlert("Recurring list successfully updated", context);
                    updateUI(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;

            case START_RECURRING:
                try {
                    global.showAlert("Recurring list successfully updated", context);
                    updateUI(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;

            case STOP_RECURRING:
                try {
                    global.showAlert("Recurring list successfully updated", context);
                    updateUI(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;

        }
    }

    private void updateUI(JSONObject jsonreturn) {

        if (!jsonreturn.has("recurrings"))
            return;

        if (pageNo == 1) {
            adapter.RecurringList = new ArrayList<>();
            isTotalRecordGet = false;
            adapter.notifyDataSetChanged();
        }


        try {
            JSONArray recurring = jsonreturn.getJSONObject("recurrings").getJSONArray("recurring");

            JSONObject attributes = jsonreturn.getJSONObject("recurrings").getJSONObject("@attributes");

            String total_pages = attributes.getString("total_pages");

            try {
                total_pagesInt = Integer.parseInt(total_pages);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (pageNo == total_pagesInt)
                isTotalRecordGet = true;

            for (int i = 0; i < recurring.length(); i++) {
                JSONObject c = recurring.getJSONObject(i);
                JSONObject client = c.getJSONObject(Constant.KEY_CLIENT);
                RecurringAttribute atribute = new RecurringAttribute();
                atribute.setRecurring_no(c.getString("number"));

                atribute.setOrganization(client.getString(Constant.KEY_ORGANIZATION));
                atribute.setRecurring_id(c.getString(Constant.KEY_RECURRING_ID));
                atribute.setDate(MyDateFormat.GetDate(c.getString(Constant.KEY_DATE)));   // INVOICE DATE
                atribute.setSent(c.getString("total_sent"));


                if (c.has("recurring_status"))
                    atribute.setStatus(c.getString("recurring_status"));
                atribute.setFrequency(c.getString("frequency"));
                atribute.setOccurrence(c.getString("occurence"));
                atribute.setNet_balance(c.getString("TotalAmount"));
                atribute.setCurrency(c.getString(Constant.KEY_CURRENCY));

                adapter.add(atribute);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void DeleteRecurringAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(Constant.title);
        alertDialog.setMessage("Are you sure you want to delete selected recurring ?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                JSONObject jsonObject = new JSONObject();
                JSONObject jsonData = new JSONObject();
                JSONArray recurringIdsArray;
                try {

                    recurringIdsArray = new JSONArray();
                    recurringIdsArray.put(recurringId);
                    jsonObject.put(Constant.KEY_RECURRING_ID, recurringIdsArray);
                    jsonObject.put("type", "Delete");
                    jsonObject.put("method", "updateInvoiceRecurringStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.DELETE, Constant.token, webApiResult, true);
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
        floatingAction.createFunction(position);

    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        floatingAction.createFunction(position);
    }

    public void animation(TableLayout layout, int animationId, final Constant.LIST_ACTION_ANIMATION_TYPE type) {

        android.view.animation.Animation animationInfo;

        animationInfo = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                animationId);

        animationInfo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                switch (type) {

                    case OPEN:


                        break;
                    case CLOSE:
                        isActionPopOpen = false;

                        actionRelative.setVisibility(View.GONE);


                        break;

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(animationInfo);


    }
}