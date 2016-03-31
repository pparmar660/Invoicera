package com.invoicera.androidapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.SelectClientAdpter;
import com.invoicera.Webservices.WebRequest;
import com.invoicera.model.ClientAttribute;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Parvesh on 7/7/15.
 */
public class SelectClient extends BaseActivity implements WebApiResult, OnClickListener {


    ArrayList<HashMap<String, String>> paymentGatewayList;
    ListView clientListView;
    ImageView save, back;

    String selectQuery;
    Cursor cursor;
    DatabaseHelper db;
    WebRequest request;
    JSONObject obj;
    ContentValues values;
    ArrayList<ClientAttribute> clientList;
    SelectClientAdpter listAdapter;

    boolean isShowProgressBar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_client);
        // TODO Auto-generated method stub


        clientListView = (ListView) findViewById(R.id.list);


        db = new DatabaseHelper(context);
        back = (ImageView) findViewById(R.id.back);


        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.TAble_ClientList;

        Cursor cursor = db.getRecords(selectQuery);
        back.setOnClickListener(this);

        boolean isShowProgressBar = false;
        if (cursor.moveToFirst() && cursor.getCount() > 0) {

            clientList = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {

                try {
                    ClientAttribute map;
                    JSONObject object = new JSONObject(cursor.getString(cursor
                            .getColumnIndex(DatabaseHelper.JSON_DATA)));

                    for (int k = 0; k < object.getJSONObject("clients").getJSONArray("client").length(); k++) {
                        map = new ClientAttribute();
                        JSONObject chargeObj = object.getJSONObject("clients").getJSONArray("client").getJSONObject(k);

                        map.setClientName(chargeObj.getString("name"));
                        map.setClientId(chargeObj.getString("client_id"));
                        map.setAddress(chargeObj.getString("address"));
                        map.setCreditAmount(chargeObj.getString("credit_amount"));
                        map.setClientCurrency(chargeObj.getString("currency"));
                        map.setOrganizationname(chargeObj.getString("organization"));
                        clientList.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            listAdapter = new SelectClientAdpter(context, clientList);
            clientListView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            Home.progressBarHome.setVisibility(View.VISIBLE);
        } else isShowProgressBar = true;


        JSONObject obj = new JSONObject();
        try {
            obj.put(Constant.KEY_METHOD, "listClient");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request = new WebRequest(context, obj, Constant.invoicelistURL, Constant.SERVICE_TYPE.GET_LIST, Constant.token, this, isShowProgressBar);
        request.execute();


        clientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*        InvoicePreviewCreateFragment fragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragment = (InvoicePreviewCreateFragment)fragmentManager.getFragments().get(fragmentManager.getBackStackEntryCount()-2);
                fragment.receiveCreateInvoiceData(clientList.get(position).getAddress(), clientList.get(position).getClientName(), clientList.get(position).getClientId());*/
                //  FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Constant.KEY_CLIENT_NAME, clientList.get(position).getClientName());
                map.put(Constant.KEY_CLIENT_ID, clientList.get(position).getClientId());
                map.put(Constant.KEY_ADDRESS, clientList.get(position).getAddress());
                map.put(Constant.KEY_CLIENT_CURRENCY, clientList.get(position).getClientCurrency());
                map.put(Constant.KEY_ORGANIZATION, clientList.get(position).getOrganizationname());
                map.put("credit_amount", clientList.get(position).getCreditAmount());


                try {
                 /*   ((InvoiceCreateEditPagerFragment) getParentFragment()).getChildFragment(Constant.FRAGMENT_RESULT.CLIENT, map);
                    if (request != null)
                        request.cancel(true);*/

                    Intent intent = new Intent();
                    Bundle argument = new Bundle();

                    intent.putExtra(Constant.KEY_CLIENT, map);
                    setResult(Constant.requestCodeCreateRecurringItem, intent);

                    finish();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {
        Home.progressBarHome.setVisibility(View.GONE);

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


            case GET_LIST:


                clientList = new ArrayList<>();

                db.ClearTable(DatabaseHelper.TAble_ClientList);

                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString());
                db.insert(DatabaseHelper.TAble_ClientList, values);

                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.TAble_ClientList;

                Cursor cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0) {

                    clientList = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i++) {

                        try {
                            ClientAttribute map;
                            JSONObject object = new JSONObject(cursor.getString(cursor
                                    .getColumnIndex(DatabaseHelper.JSON_DATA)));

                            for (int k = 0; k < object.getJSONObject("clients").getJSONArray("client").length(); k++) {
                                map = new ClientAttribute();
                                JSONObject chargeObj = object.getJSONObject("clients").getJSONArray("client").getJSONObject(k);

                                map.setClientName(chargeObj.getString("name"));
                                map.setClientId(chargeObj.getString("client_id"));
                                map.setAddress(chargeObj.getString("address"));
                                map.setClientCurrency(chargeObj.getString("currency"));
                                map.setOrganizationname(chargeObj.getString("organization"));
                                map.setCreditAmount(chargeObj.getString("credit_amount"));


                                clientList.add(map);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    if (listAdapter != null)
                        listAdapter.notifyDataSetChanged();
                    else {
                        listAdapter = new SelectClientAdpter(context, clientList);
                        clientListView.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();

                    }
                }


                break;
        }


    }

    @Override
    public void
    onClick(View v) {

        switch (v.getId()) {


            case R.id.back:
                request.cancel(true);
                finish();
                break;
        }


    }
}




