package com.invoicera.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import com.invoicera.CustomView.FloatingAction;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ViewPagerAdpter.ProductServiceListPagerAdapter;
import com.invoicera.ViewPagerFragment.ProductServicePagerFragment;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.androidapp.Home;
import com.invoicera.androidapp.ProductServiceFilter;
import com.invoicera.androidapp.R;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductServicesList extends BaseFragment implements
        ViewPager.OnPageChangeListener, WebApiResult, OnClickListener, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    ViewPager pager;
    ProductServicePagerFragment pagerFragment;
    ArrayList<ProductServicePagerFragment> pagerFragmentList;

    ProductServiceListPagerAdapter listPagerAdapter;

    HorizontalScrollView scrollView;
    LinearLayout scrollLiner;
    public int selectedPagePosition = 0;
    public ImageView searchCancelImg;
    public RelativeLayout actionRelative;
    public TableLayout actionTable;
    public ImageView closeActionPopUp;

    public static String content[] = {"Product", "Service"};
    WebApiResult webApiResult;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionButton rfaButton;
    RapidFloatingActionContentLabelList rfaContent;
    public boolean isSearchEnable = false;
    public boolean isProductFilterEnable = false;
    public boolean isServiceFilterEnable = false;


    public TextView deleteTV, archiveTV, NameTV, activeTV;
    public TableRow deleteLinear, archiveLinear, restoreLinear, editLiner;

    public static int position = 0;


    View view;
    TextView filterItem;
    public EditText searchEt;
    WebRequest request;

    FloatingAction floatingAction;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.product_services_list, container,
                false);


        scrollView = (HorizontalScrollView) view
                .findViewById(R.id.gallery_scrolling_tabs);
        scrollLiner = (LinearLayout) view.findViewById(R.id.scrollLiner);
        pagerFragmentList = new ArrayList<>();
        pager = (ViewPager) view.findViewById(R.id.pager);
        listPagerAdapter = new ProductServiceListPagerAdapter(getChildFragmentManager());
        searchEt = (EditText) view.findViewById(R.id.searchEdit);
        filterItem = (TextView) view.findViewById(R.id.filter_item);
        pagerFragment = new ProductServicePagerFragment();
        searchCancelImg = (ImageView) view.findViewById(R.id.searchCancel);
        actionTable = (TableLayout) view.findViewById(R.id.actionTable);
        searchCancelImg.setOnClickListener(this);
        closeActionPopUp = (ImageView) view.findViewById(R.id.closeAction);
        actionRelative = (RelativeLayout) view.findViewById(R.id.actionRelative);
        deleteTV = (TextView) view.findViewById(R.id.delete);
        archiveTV = (TextView) view.findViewById(R.id.archive);
        editLiner = (TableRow) view.findViewById(R.id.editProductServicesLinear);
        NameTV = (TextView) view.findViewById(R.id.productServicesName);
        activeTV = (TextView) view.findViewById(R.id.active);
        restoreLinear = (TableRow) view.findViewById(R.id.restoreLinear);
        deleteLinear = (TableRow) view.findViewById(R.id.deleteLinear);
        archiveLinear = (TableRow) view.findViewById(R.id.archiveLinear);
        webApiResult = this;
        activeTV.setOnClickListener(this);
        deleteTV.setOnClickListener(this);
        archiveTV.setOnClickListener(this);

        deleteLinear.setOnClickListener(this);
        editLiner.setOnClickListener(this);
        restoreLinear.setOnClickListener(this);
        archiveLinear.setOnClickListener(this);


        for (int i = 0; i < content.length; i++)

        {
            Bundle argument = new Bundle();
            argument.putInt(Constant.FRAGMENT_POS, i);
            pagerFragment = new ProductServicePagerFragment();
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


        filterItem.setOnClickListener(this);


        ///floating tap-------------
        rfaContent = new RapidFloatingActionContentLabelList(context);
        rfaButton = (RapidFloatingActionButton) view.findViewById(R.id.label_list_sample_rfab);
        rfaLayout = (RapidFloatingActionLayout) view.findViewById(R.id.label_list_sample_rfal);

        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        floatingAction = new FloatingAction(context, fragmentChanger, rfaLayout, rfaButton, rfaContent);
        ;


        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    try {


                        JSONObject obj = new JSONObject();

                        if (selectedPagePosition == 0)
                            obj.put(Constant.KEY_METHOD, "searchItem");
                        else obj.put(Constant.KEY_METHOD, "searchService");
                        obj.put(Constant.KEY_PAGE_NO, "1");
                        obj.put(Constant.KEY_PAGE_PER_REORD, "200");
                        obj.put(Constant.KEY_STATUS, "");

                        obj.put("searchText", searchEt.getText());

/*

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
*/


                        WebRequest request = new WebRequest(context, obj,
                                Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_SEARCH_DATA, Constant.token, webApiResult, true);
                        if (global.isNetworkAvailable())
                            request.execute();
                        else {
                            global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                            isSearchEnable = true;


                            searchCancelImg.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {

                        Log.i("exception", e.getMessage());

                    }
                    //searchEt.setText("");
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        long pos = 0;

        try {
            pos = getArguments().getLong(Constant.POSITION_NUMBER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pos == 1l) {
            pager.setCurrentItem(1);
            invalidateTabs(1);

        } else {
            invalidateTabs(0);
            pager.setCurrentItem(0);
        }


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("onresume", "");
        Home.toolbarText.setText("Items");
    }

    /*
         * @Override public void onCreate(Bundle savedInstanceState) { // TODO
         * Auto-generated method stub super.onCreate(savedInstanceState); final
         * Context contextThemeWrapper = new ContextThemeWrapper( getActivity(),
         * R.style.InvoiceList); localInflater =
         * LayoutInflater.from(getActivity()).cloneInContext( contextThemeWrapper);
         * }
         */
    public View addIndicator(int position) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.indicater_text, scrollLiner,
                false);
        TextView textView = (TextView) view.findViewById(R.id.indicater_text);


		/*
         * LinearLayout.LayoutParams layoutParams = new
		 * LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
		 * LinearLayout.LayoutParams.WRAP_CONTENT);
		 */
        /*
         * layoutParams.setMargins((int) global.convertDpToPixel(10, context),
		 * (int) global.convertDpToPixel(10, context), (int)
		 * global.convertDpToPixel(10, context), (int)
		 * global.convertDpToPixel(10, context));
		 */// margins as you
        // wish

        // textView.setLayoutParams(layoutParams);

        // imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // imageView.setImageResource(R.mipmap.ic_launcher);
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


    @Override
    public void onPause() {
        super.onPause();

        Log.e("pause", "");

    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {


        if (type != Constant.SERVICE_TYPE.GET_SEARCH_DATA)
            searchEt.setText("");
        pagerFragmentList.get(selectedPagePosition).getWebResult(type, result);


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            if (requestCode == Constant.requestCodeItemFilter) {

                String status = data.getStringExtra(Constant.KEY_STATUS);
                if (status == null)
                    status = "Active";
                String unitCostFrom = data.getStringExtra(Constant.KEY_FROM);
                String unitCostTo = data.getStringExtra(Constant.KEY_TO);

               /* {"name":"","date_from":"","date_to":"" +
                        "","unitCostFrom":"","unitCostTo":"","status":"",
                        "inventroy":"Y/N","quantity":"1","per_page_record":"200","type":"service/product " }


*/


                try {
                    JSONObject object = new JSONObject();
                    object.put("unitCostFrom", unitCostFrom);
                    object.put("unitCostTo", unitCostTo);
                    if (selectedPagePosition == 0)
                        object.put("method", "filterProduct");
                    else
                        object.put("method", "filterService");
                    object.put("status", status);
                    object.put("per_page_record", 200);
                    object.put("page", 1);


                    request = new WebRequest(context, object, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_FILTER_DATA, Constant.token, webApiResult, true);
                    request.execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.filter_item:
                if (selectedPagePosition == 0) {
                    isProductFilterEnable = true;
                    isServiceFilterEnable = false;
                } else {
                    isServiceFilterEnable = true;
                    isProductFilterEnable = false;
                }

                Intent intent = new Intent(context, ProductServiceFilter.class);
                startActivityForResult(intent, Constant.requestCodeItemFilter);

                break;


            case R.id.closeAction:
                pagerFragmentList.get(selectedPagePosition).animation(actionTable, R.anim.bottom_down, Constant.LIST_ACTION_ANIMATION_TYPE.CLOSE);
                break;

            case R.id.searchCancel:

                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_ProductAndServices;

                Cursor cursor = db.getRecords(selectQuery);


                if (cursor.moveToFirst() && cursor.getCount() > 0) {


                    for (int i = 0; i < cursor.getCount(); i++) {
                        try {
                            pagerFragmentList.get(selectedPagePosition).setListData(new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                searchEt.setText("");
                searchCancelImg.setVisibility(View.GONE);

                break;


            case R.id.editProductServicesLinear:
                actionRelative.setVisibility(View.GONE);

/*                Intent i = new Intent(context, CreateEditProductServices.class);
                if (selectedPagePosition == 0)
                    i.putExtra(Constant.KEY_CLIENT_ID, pagerFragmentList.get(selectedPagePosition).productsList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
else
                    i.putExtra(Constant.KEY_CLIENT_ID, pagerFragmentList.get(selectedPagePosition).servicesList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());

                getActivity().startActivity(i);*/

                Bundle arguments = new Bundle();
                if (selectedPagePosition == 0) {
                    arguments.putString(Constant.KEY_TYPE, "Product");
                    arguments.putParcelable(Constant.KEY_DATA, pagerFragmentList.get(selectedPagePosition).productsList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition));
                    // arguments.putString(Constant.KEY_ID, pagerFragmentList.get(selectedPagePosition).productsList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                } else {
                    arguments.putString(Constant.KEY_TYPE, "Services");
                    // arguments.putString(Constant.KEY_ID, pagerFragmentList.get(selectedPagePosition).servicesList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                    arguments.putParcelable(Constant.KEY_DATA, pagerFragmentList.get(selectedPagePosition)
                            .servicesList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition));


                }

                fragmentChanger.onFragmentAddWithBackStack(new CreateEditProductServices(), Constant.CreateProductServicesFragmentTag, arguments);
                //isActionPopOpen = false;
                pagerFragmentList.get(selectedPagePosition).isActionPopOpen = false;
                actionRelative.setVisibility(View.GONE);

                break;

            case R.id.deleteLinear:

                DeleteAlertDialog();
                pagerFragmentList.get(selectedPagePosition).isActionPopOpen = false;
                actionRelative.setVisibility(View.GONE);

                break;
            case R.id.archiveLinear:

                if (global.isNetworkAvailable()) {
                    actionRelative.setVisibility(View.GONE);

                    try {
                        JSONObject deleteItemObject = new JSONObject();

                        JSONObject jsonObject = new JSONObject();
                        deleteItemObject.put("type", "Archive");// Deleted - >Deleted ,
                        // Archive->Archive
                        if (selectedPagePosition == 0) {
                            deleteItemObject.put("product_id", pagerFragmentList.
                                    get(selectedPagePosition).productsList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                            jsonObject.put("deleteProduct", deleteItemObject);
                        } else {
                            deleteItemObject.put("service_id", pagerFragmentList.
                                    get(selectedPagePosition).servicesList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                            jsonObject.put("deleteService", deleteItemObject);

                        }


                        request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ARCHIVE, Constant.token, webApiResult, true);
                        request.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                break;
            case R.id.restoreLinear:

                if (global.isNetworkAvailable()) {
                    actionRelative.setVisibility(View.GONE);

                    try {
                        JSONObject deleteItemObject = new JSONObject();

                        JSONObject jsonObject = new JSONObject();
                        deleteItemObject.put("type", "Active");// Deleted - >Deleted ,
                        // Archive->Archive
                        if (selectedPagePosition == 0) {
                            deleteItemObject.put("product_id", pagerFragmentList.get(selectedPagePosition).productsList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                            jsonObject.put("deleteProduct", deleteItemObject);
                        } else {
                            deleteItemObject.put("service_id", pagerFragmentList.get(selectedPagePosition).servicesList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                            jsonObject.put("deleteService", deleteItemObject);

                        }


                        request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.ACTIVE, Constant.token, webApiResult, true);
                        request.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                break;

        }

    }


    public void DeleteAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle(Constant.title);
        alertDialog.setMessage("Are you sure you want to delete selected item ?");
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (global.isNetworkAvailable()) {


                            try {
                                JSONObject deleteItemObject = new JSONObject();

                                JSONObject jsonObject = new JSONObject();
                                deleteItemObject.put("type", "Deleted");// Deleted - >Deleted ,
                                // Archive->Archive
                                if (selectedPagePosition == 0) {
                                    deleteItemObject.put("product_id", pagerFragmentList.get(selectedPagePosition).productsList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                                    jsonObject.put("deleteProduct", deleteItemObject);
                                } else {
                                    deleteItemObject.put("service_id", pagerFragmentList.get(selectedPagePosition).servicesList.get(pagerFragmentList.get(selectedPagePosition).selectedPosition).getId());
                                    jsonObject.put("deleteService", deleteItemObject);

                                }


                                request = new WebRequest(context, jsonObject, Constant.invoicelistURL, Constant.SERVICE_TYPE.DELETE, Constant.token, webApiResult, true);
                                request.execute();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


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


}
