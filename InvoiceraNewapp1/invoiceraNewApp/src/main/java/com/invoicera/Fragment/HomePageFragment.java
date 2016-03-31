package com.invoicera.Fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.HomeGrid;
import com.invoicera.ViewPagerAdpter.GraphPagerAdapter;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.R;
import com.invoicera.listener.FragmentChanger;
import com.invoicera.model.GraphItemModel;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePageFragment extends BaseFragment implements WebApiResult, OnScrollListener {

    ParallaxScrollView parallaxScrollView;
    public ViewPager pager;
    TextView totalTV;
    GraphPagerAdapter graphAdapter;
    public static int selectedPosition = 0;

    TabPageIndicator indicator;
    static String FRAGMENT_POS = "fragment_pos";
    static String DATA_LIST = "data_list";
    // ExpandableHeightGridView gridView;
    HomeGrid gridAdter;
    ParallaxListView listView;
    public static TextView textView;
    FragmentChanger fragmentChanger;
    JSONObject obj = null;
    DatabaseHelper db;
    ContentValues values;
    public int initialPos = -1;
    private boolean temp = false, init = true;

    public boolean isGraphVisible = true;

    public static ArrayList<Integer> totalItem = new ArrayList<>();
    View convertView, convertView2;
    boolean isProgressShowing = false;
    WebRequest request;
    /*
     * ArrayList<ArrayList<GraphItemModel>> itemList = new
     * ArrayList<ArrayList<GraphItemModel>>();
     */
    ArrayList<GraphFragment> myFragmentList = new ArrayList<GraphFragment>();
    boolean showProgress = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.home_page_fragment,
                container, false);


        listView = (ParallaxListView) rootView.findViewById(R.id.list_view);
        gridAdter = new HomeGrid(context, fragmentChanger);
        Home.toolbarText.setText("Home");
        selectedPosition = 0;

        showProgress = false;
        db = new DatabaseHelper(context);


        inflater = (LayoutInflater) Home.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.home_viewpager, null);
        convertView2 = inflater.inflate(R.layout.animation_text, null);
        pager = (ViewPager) convertView.findViewById(R.id.pager);
        pager.getLayoutParams().height = (global.screenHeight * 90) / 200;
        pager.invalidate();
        indicator = (TabPageIndicator) convertView.findViewById(R.id.indicator);
        textView = (TextView) convertView2.findViewById(R.id.textView);
        textView.getLayoutParams().height = (global.screenHeight * 90) / 200;

        listView.addParallaxedHeaderView(convertView);


        listView.setAdapter(gridAdter);

        listView.setOnScrollListener(this);

        graphAdapter = new GraphPagerAdapter(getChildFragmentManager());
        pager.setAdapter(graphAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;

                System.out.println("As: " + position + "," + totalItem.size());

                setTextOfAnimation(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // pager.setBackground(getResources().getDrawable(R.drawable.box_background));
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //-------------------------------------------Get local data------------------------------------


        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_GraphData;

        Cursor cursor = db.getRecords(selectQuery);

        if (cursor.moveToFirst() && cursor.getCount() > 0) {


            setGraph();
            isProgressShowing = false;

        } else

            isProgressShowing = true;


        try {
            obj = new JSONObject();
            obj.put(Constant.KEY_METHOD, "getHomeData");
        } catch (JSONException e) {
            e.printStackTrace();
        }


      /*  if (!global.isNetworkAvailable()) {
            global.showAlert("No connection", context);

        }*/

/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                global.showAlert(position+"",context);
            }

        });*/

        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.showAlert("" + "", context);
            }
        });


        //-------------------------
        return rootView;
    }

    @Override
    public void onStart() {


        super.onStart();

/*
        WebRequest request = new WebRequest(context, obj,
                Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, isProgressShowing);
        request.execute();*/
    }


    @Override
    public void onResume() {
        super.onResume();

  /*      int backStackEntryCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount >= 1)
            return;*/
        request = new WebRequest(context, obj,
                Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_DATA, Constant.token, this, isProgressShowing);
        if (global.isNetworkAvailable()) {
            request.execute();
            Home.progressBarHome.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Home.progressBarHome.setVisibility(View.GONE);
        request.cancel(true);
    }

    public Fragment getGraphItem(String key, JSONObject obj) {
        GraphFragment fragment;

        try {
            JSONArray array = obj.getJSONArray(key);
            GraphItemModel map;
            ArrayList<GraphItemModel> model = new ArrayList<GraphItemModel>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject valueObj = array.getJSONObject(i);
                map = new GraphItemModel();
                map.setNumberOfIem(Integer.parseInt(valueObj.getString("value")));
                map.setColourCode(global.colourArray.get(valueObj.getString("name")));
                map.setItemName(valueObj.getString("name"));
                if (key.equalsIgnoreCase("invoice"))
                    map.setType("INVOICES");
                if (key.equalsIgnoreCase("Estimate"))
                    map.setType("ESTIMATES");
                if (key.equalsIgnoreCase("Expense"))
                    map.setType("EXPENSES");
                model.add(map);

            }

            fragment = new GraphFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(FRAGMENT_POS, 0);
            arguments.putParcelableArrayList(DATA_LIST, model);
            fragment.setArguments(arguments);
            return fragment;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        fragmentChanger = (FragmentChanger) getActivity();
    }


    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {
      /*  Home.progressBarHome.setVisibility(View.GONE);*/

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
                db.ClearTable(DatabaseHelper.Table_GraphData);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact

                db.insert(DatabaseHelper.Table_GraphData, values);

                if (isGraphVisible)
                    setGraph();


                break;


        }


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (initialPos < 0)
            initialPos = firstVisibleItem;

        if (listView.getChildAt(initialPos).getY() <= -20) {

            if (listView.getHeaderViewsCount() == 1 && !temp) {
                listView.removeHeaderView(convertView);
                listView.addParallaxedHeaderView(convertView2);

                temp = true;
                init = false;
                isGraphVisible = false;
            }

        } else {
            if (listView.getChildAt(initialPos).getY() >= -20)
                if (listView.getHeaderViewsCount() == 1 && temp
                        && !init) {
                    listView.removeHeaderView(convertView2);
                    listView.addParallaxedHeaderView(convertView);
                    temp = false;
                    isGraphVisible = true;

                    /*if (isGraphVisible)
                        setGraph();*/
                }

        }

    }

    private void setTextOfAnimation(int position) {

        Log.e("total item list", totalItem.toString());

        if (totalItem.size() > position)
            switch (position) {
                case 0:

                    textView.setText("INVOICES " + totalItem.get(position));
                    break;
                case 1:

                    textView.setText("ESTIMATES " + totalItem.get(position));
                    break;
                case 2:

                    textView.setText("EXPENSES " + totalItem.get(position));
                    break;


            }

    }

    public void setGraph() {

        JSONObject object = null;
        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_GraphData;

        Cursor cursor = db.getRecords(selectQuery);

        if (cursor.moveToFirst() && cursor.getCount() > 0) {

            //totalItem = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                try {
                    HomePageFragment fragment = null;
                    try {
                        fragment = (HomePageFragment) getFragmentManager().findFragmentById(R.id.content_frame);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (fragment == null)
                        return;
                    // Toast.makeText(context, "fragment not null", Toast.LENGTH_LONG).show();

                    object = new JSONObject(cursor.getString(cursor
                            .getColumnIndex(DatabaseHelper.JSON_DATA)));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
                   /* onPageSelected(pager.getCurrentItem());*/
        }

        if (object == null)
            return;

        try {
            JSONObject colorObj = object.getJSONObject("ColorCode");
            global.colourArray = new HashMap<>();
            global.colourArray.put(Constant.KEY_SENT, colorObj.getString(Constant.KEY_SENT));
            global.colourArray.put(Constant.KEY_DRAFT, colorObj.getString(Constant.KEY_DRAFT));
            global.colourArray.put(Constant.KEY_OUTSTANDING, colorObj.getString(Constant.KEY_OUTSTANDING));
            global.colourArray.put(Constant.KEY_PAID, colorObj.getString(Constant.KEY_PAID));
            global.colourArray.put(Constant.KEY_PARTIAL_PAID, colorObj.getString(Constant.KEY_PARTIAL_PAID));
            global.colourArray.put(Constant.KEY_INVOICED, colorObj.getString(Constant.KEY_INVOICED));
            global.colourArray.put(Constant.KEY_UNBILLED, colorObj.getString(Constant.KEY_UNBILLED));
            global.colourArray.put(Constant.KEY_EXPIRED, colorObj.getString(Constant.KEY_EXPIRED));
            global.colourArray.put(Constant.KEY_ACCEPTED, colorObj.getString(Constant.KEY_ACCEPTED));
            global.colourArray.put(Constant.KEY_REJECTED, colorObj.getString(Constant.KEY_REJECTED));


            graphAdapter.fragmentList = new ArrayList<>();
            totalItem = new ArrayList<>();

            if (object.has("invoice_total"))
                totalItem.add(Integer.parseInt(object.getString("invoice_total")));
            if (object.has("estimate_total"))
                totalItem.add(Integer.parseInt(object.getString("estimate_total")));

            if (object.has("expense_total"))
                totalItem.add(Integer.parseInt(object.getString("expense_total")));

            graphAdapter.add(getGraphItem("Invoice", object));
            graphAdapter.add(getGraphItem("Estimate", object));
            graphAdapter.add(getGraphItem("Expense", object));
            indicator.setViewPager(pager);
            indicator.setVisibility(View.VISIBLE);
            pager.setOffscreenPageLimit(graphAdapter.getCount());
            graphAdapter.notifyDataSetChanged();
            setTextOfAnimation(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}