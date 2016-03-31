package com.invoicera.ViewPagerFragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.invoicera.Database.DatabaseHelper;
import com.invoicera.Fragment.BaseFragment;
import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.ListViewAdpter.EstimateInformationListAdapter;
import com.invoicera.androidapp.R;
import com.invoicera.model.EstimateInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Parvesh on 30/7/15.
 */
public class EstimateInformationPagerFragment extends BaseFragment implements WebApiResult {


    ListView listView;


    EstimateInformationListAdapter
            adapter;
    String estimateId;

    DatabaseHelper db;

    ArrayList<HashMap<String, String>> DateList;
    ArrayList<EstimateInformation> dataList;
    public static String createdBy = "", createdFor = "";

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.information, container,
                false);
        Bundle argument = getArguments();
        createdBy = "";
        createdFor = "";
        if (argument.get(Constant.KEY_ESTIMATE_ID) != null)
            estimateId = argument.getString(Constant.KEY_ESTIMATE_ID);

        db = new DatabaseHelper(context);
        listView = (ListView) view.findViewById(R.id.list);
        dataList = new ArrayList<>();

/*        listView = (ListView) view.findViewById(R.id.list);
        Information info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.HEADER);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.HEADER);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
        list.add(info);
        info = new Information();
        info.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
        list.add(info);
        adapter = new InvoiceInformationListAdapter(context, list);
        listView.setAdapter(adapter);*/


        //---------------Load data from local


        String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                + " From " + DatabaseHelper.Table_EstimatePreviewData + " WHERE " + DatabaseHelper.ID + " ='" + estimateId + "'";
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


        }


        return view;
    }

    public void setData(JSONObject jsonDataGet) {
        if (listView == null)
            return;

        JSONObject jsonData = null;
        String currency = "";
        try {
            jsonData = jsonDataGet.getJSONObject("estimate_history");
            currency = jsonDataGet.getJSONObject("client").getString("currency");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        HashMap<String, String> map;
        try {
            if (jsonData.has("activityDateDetail")) {
                JSONArray dateArray = jsonData.getJSONArray("activityDateDetail");
                createdBy = jsonData.getJSONObject("estimate_notification").getJSONObject("Created").getString("created_by");
                createdFor = jsonData.getJSONObject("estimate_notification").getJSONObject("Created").getString("created_for");

                DateList = new ArrayList<>();
                for (int i = 0; i < dateArray.length(); i++) {

                    map = new HashMap<String, String>();
                    map.put(Constant.KEY_DATE, dateArray.getJSONObject(i).getString("date"));
                    map.put(Constant.KEY_DAY, dateArray.getJSONObject(i).getString("day"));
                    DateList.add(map);
                }


                dataList = new ArrayList<>();

                for (int i = 0; i < DateList.size(); i++) {
                    if (!jsonData.getJSONObject("estimate_notification").has(DateList.get(i).get(Constant.KEY_DATE)))
                        break;

                    JSONArray dataArray = jsonData.getJSONObject("estimate_notification").getJSONArray(DateList.get(i).get(Constant.KEY_DATE));

                    EstimateInformation model = new EstimateInformation();
                    model.setRow_type(Constant.INFORMATION_ROW_TYPE.HEADER);
                    model.setDate(DateList.get(i).get(Constant.KEY_DATE));
                    model.setDay(DateList.get(i).get(Constant.KEY_DAY));
                    dataList.add(model);
                    for (int j = 0; j < dataArray.length(); j++) {

                        model = new EstimateInformation();
                        model.setRow_type(Constant.INFORMATION_ROW_TYPE.INFORMATION);
                        model.setDate(dataArray.getJSONObject(j).getString(Constant.KEY_DATE));
                        model.setDay(DateList.get(i).get(Constant.KEY_DAY));
                        model.setType(dataArray.getJSONObject(j).getString(Constant.KEY_TYPE));
                        if (dataArray.getJSONObject(j).has(Constant.KEY_AMOUNT))
                            model.setAmount(dataArray.getJSONObject(j).getString(Constant.KEY_AMOUNT));


                        model.setCreatedBy(createdBy);
                        model.setCreatedFor(createdFor);


                        if (dataArray.getJSONObject(j).getString(Constant.KEY_TYPE).equalsIgnoreCase("Offline")) {

                            model.setPaymentMethod(dataArray.getJSONObject(j).getString("payment_method"));
                            model.setPaymentNote(dataArray.getJSONObject(j).getString("payment_note"));
                            model.setPaidAmount(dataArray.getJSONObject(j).getString("paid_amount"));


                        }


                        dataList.add(model);


                    }
                }

            }

            adapter = new EstimateInformationListAdapter(context, currency, dataList);
            listView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                setData(result);

                break;
        }

    }


}
