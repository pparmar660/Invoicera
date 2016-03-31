package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.Utility.Utils;
import com.invoicera.ViewPagerFragment.RecurringPreviewPagerFragment;
import com.invoicera.androidapp.Global;
import com.invoicera.androidapp.R;
import com.invoicera.model.RecurringInformation;

import java.util.ArrayList;

/**
 * Created by Parvesh on 30/9/15.
 */
public class RecurringInformationListAdapter extends BaseAdapter {

    Context context;
    ArrayList<RecurringInformation> dataList;
    String currency = "";
    Global global;

    public RecurringInformationListAdapter(Context context, String currency, ArrayList<RecurringInformation> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.currency = currency;
        this.global = (Global) context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (dataList.get(position).getRow_type() == Constant.INFORMATION_ROW_TYPE.HEADER) {
            convertView = inflater.inflate(
                    R.layout.information_list_row1, parent, false);

            TextView date, day;
            date = (TextView) convertView.findViewById(R.id.date);
            day = (TextView) convertView.findViewById(R.id.day_name);
            date.setText(dataList.get(position).getDate());
            day.setText(dataList.get(position).getDay());
        } else {
            convertView = inflater.inflate(
                    R.layout.information_list_row2, parent, false);

            TextView typeTV = (TextView) convertView.findViewById(R.id.type);
            TextView timeTV = (TextView) convertView.findViewById(R.id.time);
            typeTV.setText("Invoice " + dataList.get(position).getType());
            timeTV.setText(MyDateFormat.GetTime(dataList.get(position).getDate()));
            RelativeLayout linRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.historyLinear);

            if (dataList.get(position).getType().equalsIgnoreCase("Create")) {


                typeTV.setText(dataList.get(position).getCreatedBy() + " Created Recurring to " + dataList.get(position).getCreatedFor() + " of " + RecurringPreviewPagerFragment.ClientCurrency + " " + dataList.get(position).getAmount());
            }

            if (dataList.get(position).getType().equalsIgnoreCase("Edit")) {
                typeTV.setText(dataList.get(position).getCreatedBy() + " Edited Recurring to " + dataList.get(position).getCreatedFor() + " of " + RecurringPreviewPagerFragment.ClientCurrency + " " + dataList.get(position).getAmount());

            }
            if (dataList.get(position).getType().equalsIgnoreCase("Sent")) {
                typeTV.setText(dataList.get(position).getCreatedBy() + " Emailed Recurring to " + dataList.get(position).getCreatedFor() + " of " + RecurringPreviewPagerFragment.ClientCurrency + " " + dataList.get(position).getAmount());

            }


            if (dataList.get(position).getType().equalsIgnoreCase("Offline")) {

                typeTV.setText("An amount of " + currency + " " + global.setLength(Utils.FloatToStringLimits(dataList.get(position).getPaidAmount()), Constant.defultLengthOfText) + " Received from " + dataList.get(position).getCreatedFor() + " by " + dataList.get(position).getPaymentMethod());
            }

            if (!((position + 1) == dataList.size()))
                if (isLastElementOfDay(dataList.get(position + 1)))
                    linRelativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));

        }


        return convertView;
    }

    boolean isLastElementOfDay(RecurringInformation model) {

        if (Constant.INFORMATION_ROW_TYPE.HEADER == model.getRow_type())
            return true;


        return false;
    }


}
