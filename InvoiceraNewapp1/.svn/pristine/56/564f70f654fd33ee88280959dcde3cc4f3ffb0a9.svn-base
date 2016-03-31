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

import com.invoicera.CustomView.FloatingAction;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ViewPagerAdpter.ExpenseListPagerAdapter;
import com.invoicera.ViewPagerFragment.ExpenseListPagerFragment;
import com.invoicera.Webservices.DownloadPdfFile;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.Expense_Filter;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.listener.FragmentChanger;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExpenseList extends BaseFragment implements
        ViewPager.OnPageChangeListener, WebApiResult, OnClickListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    ViewPager pager;
    ExpenseListPagerFragment pagerFragment;
    ArrayList<ExpenseListPagerFragment> pagerFragmentList;

    ExpenseListPagerAdapter listPagerAdapter;
    // TabPageIndicator indicator;
    public FragmentChanger fragmentChanger;

    HorizontalScrollView scrollView;
    LinearLayout scrollLiner;
    public int selectedPagePosition = 0;
    public ImageView searchCancelImg, searchImage;
    public RelativeLayout actionRelative;
    public TableLayout actionTable;
    public ImageView closeActionPopUp;
    public TableRow deleteLinear, archiveLinear, restoreLinear, approveLiner, rejectLinear;
    public static String content[] = {"Recent", "Paid", "Invoiced", "Unbilled"};
    WebApiResult webApiResult;
    private RapidFloatingActionLayout rfaLayout;

    private RapidFloatingActionButton rfaButton;
    RapidFloatingActionContentLabelList rfaContent;
    public boolean isSearchEnable = false;
    public boolean isFilterEnable = false;

   public TextView expenseNoTV;

    public String expenseId;
    public static int position = 0;

    public static String[] table_field = {
            DatabaseHelper.JSON_EXPENSE_RECENT_LIST,
            DatabaseHelper.JSON_EXPENSE_PAID_LIST,
            DatabaseHelper.JSON_EXPENSE_INVOICED_LIST,
            DatabaseHelper.JSON_EXPENSE_UNBILLED_LIST

    };

    View view;
    TextView filterExpense;
    public EditText searchEt;
    WebRequest request;

    FloatingAction floatingAction;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.expense_list, container,
                false);


        Home.toolbarText.setText("Expense");
        scrollView = (HorizontalScrollView) view
                .findViewById(R.id.gallery_scrolling_tabs);
        scrollLiner = (LinearLayout) view.findViewById(R.id.scrollLiner);
        pagerFragmentList = new ArrayList<>();
        pager = (ViewPager) view.findViewById(R.id.pager);
        listPagerAdapter = new ExpenseListPagerAdapter(getChildFragmentManager());
        searchEt = (EditText) view.findViewById(R.id.searchEdit);
        filterExpense = (TextView) view.findViewById(R.id.filter_expense);
        pagerFragment = new ExpenseListPagerFragment();
        searchCancelImg = (ImageView) view.findViewById(R.id.searchCancel);
        searchImage = (ImageView) view.findViewById(R.id.search);
        actionTable = (TableLayout) view.findViewById(R.id.actionTable);
        searchCancelImg.setOnClickListener(this);
        closeActionPopUp = (ImageView) view.findViewById(R.id.closeAction);
        actionRelative = (RelativeLayout) view.findViewById(R.id.actionRelative);
/*        deleteTV = (TextView) view.findViewById(R.id.delete);
        archiveTV = (TextView) view.findViewById(R.id.archive);
        exportAsPdfTV = (TextView) view.findViewById(R.id.exportPdf);
        offlinePaymentTV = (TextView) view.findViewById(R.id.offlinePayment);*/
        expenseNoTV = (TextView) view.findViewById(R.id.expenseNo);
        //   activeTV = (TextView) view.findViewById(R.id.active);
        restoreLinear = (TableRow) view.findViewById(R.id.restoreLinear);
        deleteLinear = (TableRow) view.findViewById(R.id.deleteLinear);
        archiveLinear = (TableRow) view.findViewById(R.id.archiveLinear);
        rejectLinear = (TableRow) view.findViewById(R.id.rejectLinear);
        approveLiner = (TableRow) view.findViewById(R.id.approveLinear);
        searchImage.setOnClickListener(this);
        webApiResult = this;
        deleteLinear.setOnClickListener(this);
        archiveLinear.setOnClickListener(this);
        rejectLinear.setOnClickListener(this);
        approveLiner.setOnClickListener(this);
        restoreLinear.setOnClickListener(this);

        for (int i = 0; i < content.length; i++)

        {
            Bundle argument = new Bundle();
            argument.putInt(Constant.FRAGMENT_POS, i);
            pagerFragment = new ExpenseListPagerFragment();
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


        filterExpense.setOnClickListener(this);


        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    isSearchEnable = true;
                    pagerFragmentList.get(selectedPagePosition).pageNo = 1;
                    sendSearchRequest();
                    searchCancelImg.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


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
        if (data == null)
            return;
        if (requestCode == Constant.requestCodeExpenseFilter) {

           // Log.e("expense filter", data.getExtras().toString());

            String amount_status = data.getStringExtra(Constant.KEY_AMOUNT_STATUS);
            String status = data.getStringExtra(Constant.KEY_STATUS);
            String client_id = data.getStringExtra(Constant.KEY_CLIENT_ID);
            String date = data.getStringExtra(Constant.KEY_DATE_FROM);
            String amountFrom = data.getStringExtra(Constant.KEY_AMOUNT_FROM);
            String amountTo= data.getStringExtra(Constant.KEY_AMOUNT_TO);


            JSONObject obj = null;
            try {
                obj = new JSONObject();

                obj.put(Constant.KEY_METHOD, "filterExpense");
                obj.put(Constant.KEY_CLIENT_ID, client_id);
                obj.put("date", date);
                obj.put(Constant.KEY_PAGE_NO, 1);
                obj.put(Constant.KEY_PAGE_PER_REORD, 200);
                obj.put(Constant.KEY_AMOUNT_TO,amountTo);
                obj.put(Constant.KEY_AMOUNT_FROM,amountFrom);
                obj.put(Constant.KEY_AMOUNT_STATUS,amount_status);
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
                global.showAlert("No connection", context);


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

    public void getSearchedExpenseList(String searchText, int page_no, Constant.SERVICE_TYPE type,
                                       boolean showProcessing, String expense_status) throws JSONException {

        JSONObject obj = new JSONObject();

        obj.put(Constant.KEY_METHOD, "searchExpense");
        obj.put(Constant.KEY_PAGE_NO, page_no);
        obj.put(Constant.KEY_PAGE_PER_REORD, 200);
        obj.put(Constant.KEY_STATUS, "");
        obj.put(Constant.KEY_AMOUNT_STATUS, expense_status);
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
        Intent i;

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonData = new JSONObject();
        JSONArray expenseIdsArray;
        switch (v.getId()) {

            case R.id.filter_expense:
                i = new Intent(context, Expense_Filter.class);
                // System.out.println("Value send :" + content[selectedPagePosition]);
                i.putExtra(Constant.KEY_AMOUNT_STATUS, content[selectedPagePosition]);

                getActivity().startActivityForResult(i,
                        Constant.requestCodeExpenseFilter);// Activity


                break;
            case R.id.search:

                isSearchEnable = true;
                pagerFragmentList.get(selectedPagePosition).pageNo = 1;
                sendSearchRequest();
                searchCancelImg.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                break;
            case  R.id.searchCancel:
                isSearchEnable = false;
                searchCancelImg.setVisibility(View.GONE);
                searchEt.setText("");

                pagerFragmentList.get(selectedPagePosition).setList();
                break;

            case R.id.deleteLinear:

                DeleteExpenseAlertDialog();
                break;

            case R.id.archiveLinear:

                try {

                    expenseIdsArray = new JSONArray();
                    expenseIdsArray.put(expenseId);
                    jsonObject.put(Constant.KEY_AMOUNT_STATUS, content[selectedPagePosition]);
                    jsonObject.put(Constant.KEY_STATUS, "");
                    jsonObject.put("expense_id", expenseIdsArray);
                    jsonObject.put("type", "Archived");// Cancel - >delete ,
                    jsonObject.put("method","updateExpenseStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ARCHIVE, Constant.token, webApiResult, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;

            case R.id.approveLinear:


                try {

                    expenseIdsArray = new JSONArray();
                    expenseIdsArray.put(expenseId);
                    jsonObject.put(Constant.KEY_AMOUNT_STATUS, content[selectedPagePosition]);
                    jsonObject.put(Constant.KEY_STATUS, "");
                    jsonObject.put("expense_id", expenseIdsArray);
                    jsonObject.put("type", "Approve");// Cancel - >delete ,
                    jsonObject.put("method","updateExpenseStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ARCHIVE, Constant.token, webApiResult, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;

            case R.id.rejectLinear:


                try {

                    expenseIdsArray = new JSONArray();
                    expenseIdsArray.put(expenseId);
                    jsonObject.put(Constant.KEY_AMOUNT_STATUS, content[selectedPagePosition]);
                    jsonObject.put(Constant.KEY_STATUS, "");
                    jsonObject.put("expense_id", expenseIdsArray);
                    jsonObject.put("type", "Reject");// Cancel - >delete ,
                    jsonObject.put("method","updateExpenseStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ARCHIVE, Constant.token, webApiResult, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                break;


            case R.id.restoreLinear:


                try {

                    expenseIdsArray = new JSONArray();
                    expenseIdsArray.put(expenseId);
                    jsonObject.put(Constant.KEY_AMOUNT_STATUS, content[selectedPagePosition]);
                    jsonObject.put(Constant.KEY_STATUS, "");
                    jsonObject.put("expense_id", expenseIdsArray);
                    jsonObject.put("type", "Active");// Cancel - >delete ,
                    jsonObject.put("method","updateExpenseStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ARCHIVE, Constant.token, webApiResult, true);
                if (global.isNetworkAvailable())
                    request.execute();
                else
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
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
        Home.progressBarHome.setVisibility(View.GONE);
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
                pagerFragmentList.get(selectedPagePosition).adapter.expenseList.remove(position);
                pagerFragmentList.get(selectedPagePosition).adapter.notifyDataSetChanged();
                pagerFragmentList.get(selectedPagePosition).pageNo = 1;
                pagerFragmentList.get(selectedPagePosition).getWebResult(Constant.SERVICE_TYPE.GET_UPPER_DATA, result);

                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;


            case ARCHIVE:
                pagerFragmentList.get(selectedPagePosition).adapter.expenseList.remove(position);
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

                break;
            case SEND:
                try {
                    global.showAlert(result.getString("message"), context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    public void DeleteExpenseAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(Constant.title);
        alertDialog.setMessage("Are you sure you want to delete selected expense ?");
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonData = new JSONObject();
                        JSONArray expenseIdsArray;
                        try {

                            expenseIdsArray = new JSONArray();
                            expenseIdsArray.put(expenseId);
                            jsonObject.put(Constant.KEY_AMOUNT_STATUS, content[selectedPagePosition]);
                            jsonObject.put(Constant.KEY_STATUS, "Active");
                            jsonObject.put("expense_id", expenseIdsArray);
                            jsonObject.put("type", "Deleted");// Cancel - >delete ,
                            jsonObject.put("method","updateExpenseStatus");
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


    public void sendSearchRequest() {


        try {
            JSONObject obj = new JSONObject();
            obj.put(Constant.KEY_METHOD, "searchExpense");
            obj.put("searchText", searchEt.getText().toString());
            obj.put(Constant.KEY_PAGE_NO, 1);
            obj.put(Constant.KEY_PAGE_PER_REORD, 200);
            obj.put(Constant.KEY_STATUS, "");
            obj.put("amount_status", content[selectedPagePosition]);


            WebRequest request = new WebRequest(context, obj, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_SEARCH_DATA, Constant.token, this, true);
            if (global.isNetworkAvailable())
                request.execute();
            else
                global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
