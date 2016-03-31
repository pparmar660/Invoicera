package com.invoicera.ViewPagerFragment;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.Fragment.InvoiceList;
import com.invoicera.Fragment.InvoicePreviewCreateFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.SERVICE_TYPE;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.InvoiceListPagerFragmentListAdapter;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.InvoiceAttribute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InvoiceListPagerFragment extends InvoiceList implements
        WebApiResult {
    public FragmentChanger fragmentChanger;
    View listFooter;
    ListView listView;
    public InvoiceListPagerFragmentListAdapter adapter;
    DatabaseHelper db;
    ContentValues values;
    public int pageNo = 1;
    InvoiceList parentFragment;
    int total_pagesInt = 0;
    int recordPerPage = 100;
    private SwipeRefreshLayout swipeContainer;


    int currentScrollState;
    boolean isLoading = false;
    int position = 0;
    boolean isTotalRecordGet = false;

    public boolean isActionPopOpen = false;
    WebRequest request;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_pager_fragment,
                container, false);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.container);
        position = getArguments().getInt(Constant.FRAGMENT_POS);

        LayoutInflater footerInflater = LayoutInflater.from(getActivity());
        listFooter = footerInflater.inflate(R.layout.list_footer, null, false);
        listFooter.setVisibility(View.GONE);
        listView = (ListView) view.findViewById(R.id.list);
        parentFragment = (InvoiceList) getParentFragment();
        listView.addFooterView(listFooter);
        db = new DatabaseHelper(context);

        adapter = new InvoiceListPagerFragmentListAdapter(context);
        listView.setAdapter(adapter);

        listFooter.setOnClickListener(null);

        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {

                    if (parentFragment.isSearchEnable) {
                        parentFragment.isSearchEnable = false;
                        parentFragment.searchCancelImg.setVisibility(View.GONE);
                        parentFragment.searchEt.setText("");
                    }
                    if (parentFragment.isFilterEnable)
                        parentFragment.isFilterEnable = false;

                    getInvoiceList(1, SERVICE_TYPE.GET_UPPER_DATA, false,
                            InvoiceList.content[position]);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });

        swipeContainer.setColorSchemeResources(
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_red_light);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position >= adapter.InvoicesList.size()) return;
                Bundle arguments = new Bundle();
                System.out.println("Id1:" + adapter.InvoicesList.get(position).getInvoice_id());
                arguments.putString(Constant.KEY_INVOICE_ID, adapter.InvoicesList.get(position).getInvoice_id());
                arguments.putString(Constant.KEY_INVOICE_STATUS, content[parentFragment.selectedPagePosition]);
                arguments.putInt(Constant.KEY_POSITION, 1);

                if (global.isNetworkAvailable()) {
                    fragmentChanger.onFragmentAddWithBackStack(new InvoicePreviewCreateFragment(), Constant.InvoicePreviewCreateFragmentTag, arguments);
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

   /*             if (isActionPopOpen)
                    return true;*/


                isActionPopOpen = true;
                parentFragment.actionRelative.setVisibility(View.VISIBLE);
                parentFragment.invoiceId = adapter.InvoicesList.get(position).getInvoice_id();
                parentFragment.InvoiceNoTV.setText("Invoice no : " + adapter.InvoicesList.get(position).getInvoice_no());
                parentFragment.position = position;

                if(adapter.InvoicesList.get(position).getInvoice_status().equalsIgnoreCase("Paid"))
                    parentFragment.offlinePaymentLiner.setVisibility(View.GONE);
                else
                    parentFragment.offlinePaymentLiner.setVisibility(View.VISIBLE);

                if (adapter.InvoicesList.get(position).getStatus().equalsIgnoreCase("Cancel") || adapter.InvoicesList.get(position).getStatus().equalsIgnoreCase("Deleted")) {
                    parentFragment.restoreLinear.setVisibility(View.VISIBLE);
                    parentFragment.deleteLinear.setVisibility(View.GONE);
                    parentFragment.archiveLinear.setVisibility(View.VISIBLE);


                } else if (adapter.InvoicesList.get(position).getStatus().equalsIgnoreCase("Archived")) {


                    parentFragment.restoreLinear.setVisibility(View.VISIBLE);
                    parentFragment.deleteLinear.setVisibility(View.VISIBLE);
                    parentFragment.archiveLinear.setVisibility(View.GONE);

                } else {
                    parentFragment.restoreLinear.setVisibility(View.GONE);
                    parentFragment.deleteLinear.setVisibility(View.VISIBLE);
                    parentFragment.archiveLinear.setVisibility(View.VISIBLE);
                }

                animation(parentFragment.actionTable, R.anim.bottom_up, Constant.LIST_ACTION_ANIMATION_TYPE.OPEN);


                return true;


            }
        });

        parentFragment.closeActionPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                animation(parentFragment.actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);

            }
        });
        parentFragment.actionRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                currentScrollState = scrollState;


            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                if ((firstVisibleItem + visibleItemCount == totalItemCount)
                        && !isLoading && (visibleItemCount != totalItemCount)) {


                    try {

                        if (parentFragment.isSearchEnable)
                            return;
                        if (parentFragment.isFilterEnable) {

                            return;
                        }

                        if (!global.isNetworkAvailable()) {
                            global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                            return;
                        }
                        if (isTotalRecordGet)
                            return;

                        if (pageNo + 1 > total_pagesInt) {
                            global.showAlert("No more record", context);
                            return;
                        }


                        listFooter.setVisibility(View.VISIBLE);
                        isLoading = true;

                        try {


                            getInvoiceList(++pageNo,
                                    SERVICE_TYPE.GET_BOTTOM_DATA,
                                    false,
                                    InvoiceList.content[position]);

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

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        System.out.println("ONstart");
        super.onStart();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentChanger = (FragmentChanger) activity;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // setList();
        // .makeText(context, position+"", Toast.LENGTH_SHORT).show();

    }


    public String getActiveFragment() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
        Log.e("Tag", tag);
        return tag;
    }


    public void getInvoiceList(int page_no, SERVICE_TYPE type,
                               boolean showProcessing, String invoice_status) throws JSONException {

     /*   int i1=0;
        if (i1==0)
            return ;*/

        JSONObject obj = new JSONObject();

        obj.put(Constant.KEY_METHOD, "listInvoice");
        obj.put(Constant.KEY_PAGE_NO, page_no);
        obj.put(Constant.KEY_PAGE_PER_REORD, recordPerPage);
        obj.put(Constant.KEY_STATUS, "Active");
        obj.put(Constant.KEY_INVOICE_STATUS, invoice_status);

        this.pageNo = page_no;

		/*
         * WebRequest request = new WebRequest(context, obj,
		 * Constant.forgotPasswordURL, type, this); request.execute();
		 */

        Home.progressBarHome.setVisibility(View.VISIBLE);


        request = new WebRequest(context, obj,
                Constant.invoicelistURL, type, Constant.token, this, false);
        request.execute();

    }

    @Override
    public void getWebResult(SERVICE_TYPE type, JSONObject result) {
        // TODO Auto-generated method stub
        listFooter.setVisibility(View.GONE);
        isTotalRecordGet = false;

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


        boolean isSearchResult = false;

        switch (type) {
            case GET_UPPER_DATA:

                swipeContainer.setRefreshing(false);
                break;

            case GET_BOTTOM_DATA:
                isLoading = false;


                break;
            case GET_SEARCH_DATA:
                isSearchResult = true;
                break;


            case GET_FILTER_DATA:
                isSearchResult = true;
                break;
            default:
                break;
        }

        if (!isSearchResult) {

            try {
                if (pageNo == 1) {

                    values = new ContentValues();
                    values.put(InvoiceList.table_field[position], (String) null);
                    db.clearTableColumn(DatabaseHelper.Table_InvoiceList, values);


                    values = new ContentValues();
                    values.put(InvoiceList.table_field[position], result.toString()); // Contact

                    db.insert(DatabaseHelper.Table_InvoiceList, values);


                    String selectQuery = "Select " + InvoiceList.table_field[position]
                            + " From " + DatabaseHelper.Table_InvoiceList + " WHERE "
                            + InvoiceList.table_field[position] + " !='' AND "
                            + InvoiceList.table_field[position] + " IS NOT NULL";

                    Cursor cursor = db.getRecords(selectQuery);

                    if (cursor.moveToFirst() && cursor.getCount() > 0)
                        for (int i = 0; i < cursor.getCount(); i++) {
                            updateUI(new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(InvoiceList.table_field[position]))));
                        }
                } else updateUI(result);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

            updateUI(result);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (request != null)
            request.cancel(true);


    }

    public void setList() {

        if (db == null)
            return;
        String selectQuery = "Select " + InvoiceList.table_field[position]
                + " From " + DatabaseHelper.Table_InvoiceList + " WHERE "
                + InvoiceList.table_field[position] + " !='' AND "
                + InvoiceList.table_field[position] + " IS NOT NULL";

        Cursor cursor = db.getRecords(selectQuery);
      /*  Toast.makeText(
                context,
                "cusror Length " + position + "," + cursor.getCount() + ","
                        + cursor.moveToFirst(), Toast.LENGTH_SHORT).show();*/
        try {
            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {


                    updateUI(new JSONObject(cursor.getString(cursor
                            .getColumnIndex(InvoiceList.table_field[position]))));
                    pageNo = 1;

                    getInvoiceList(1, SERVICE_TYPE.GET_UPPER_DATA, false,
                            InvoiceList.content[position]);

                }
            } else {
                pageNo = 1;
                getInvoiceList(1, SERVICE_TYPE.GET_UPPER_DATA, true,
                        InvoiceList.content[position]);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void updateUI(JSONObject jsonreturn) {


        if (pageNo == 1)
            adapter.InvoicesList = new ArrayList<>();

        try {
            JSONArray invoice = jsonreturn.getJSONObject(
                    Constant.KEY_INVOICES)
                    .getJSONArray(Constant.KEY_INVOICE);

			/*
             * if (!requestingLoadmore) { invoiceList.clear(); //
			 * invoiceadapter.notifyDataSetChanged(); }
			 */

            JSONObject attributes = jsonreturn.getJSONObject(
                    Constant.KEY_INVOICES).getJSONObject("@attributes");

            String total_pages = attributes.getString("total_pages");

            total_pagesInt = Integer.parseInt(total_pages);
            if (pageNo == total_pagesInt)
                isTotalRecordGet = true;

            for (int i = 0; i < invoice.length(); i++) {
                JSONObject c = invoice.getJSONObject(i);

                /***** Storing each JSON item in variable ****/

                JSONObject client = c.getJSONObject(Constant.KEY_CLIENT);

                InvoiceAttribute atribute = new InvoiceAttribute();

                atribute.setCurrency(c.getString(Constant.KEY_CURRENCY));
                atribute.setDate(MyDateFormat.GetDate(c
                        .getString(Constant.KEY_DATE)));
                atribute.setInvoice_id(c.getString(Constant.KEY_INVOICE_ID));
                atribute.setInvoice_no(c.getString(Constant.KEY_NUMBER));
                atribute.setInvoice_status(c
                        .getString(Constant.KEY_INVOICE_STATUS));
                atribute.setNet_balance(c.getString(Constant.KEY_NET_BALANCE));
                atribute.setOrganization(client
                        .getString(Constant.KEY_ORGANIZATION));
                if (c.has(Constant.KEY_STATUS))
                    atribute.setStatus(c.getString(Constant.KEY_STATUS));
                atribute.setInvoiceTitle(c.getString(Constant.KEY_INVOICE_TITLE));
                atribute.setLate_fee(c.getString(Constant.KEY_LATE_FEE));

                adapter.add(atribute);

            }
            adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
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

                        parentFragment.actionRelative.setVisibility(View.GONE);


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