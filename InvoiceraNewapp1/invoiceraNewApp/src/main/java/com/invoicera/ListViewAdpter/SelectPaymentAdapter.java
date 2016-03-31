package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant;
import com.invoicera.androidapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Parvesh on 7/7/15.
 */
public class SelectPaymentAdapter extends BaseAdapter {


    public ArrayList<HashMap<String, String>> paymentGatewayList;
    public ArrayList<HashMap<String, String>> selectedList;
    Context context;
    LayoutInflater inflater;


    public SelectPaymentAdapter(Context context) {


        this.context = context;
        paymentGatewayList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        selectedList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return paymentGatewayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void update(ArrayList<HashMap<String, String>> list) {

        this.paymentGatewayList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.payment_gateway_block, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.name);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!selectedList.isEmpty())
            if (isContain(paymentGatewayList.get(position).get(Constant.KEY_ID),selectedList))
                viewHolder.checkBox.setChecked(true);


        viewHolder.nameTV.setText(paymentGatewayList.get(position).get(Constant.KEY_NAME));
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!isContain(paymentGatewayList.get(position).get(Constant.KEY_ID), selectedList))
                        selectedList.add(paymentGatewayList.get(position));

                } else {

                    if (isContain(paymentGatewayList.get(position).get(Constant.KEY_ID), selectedList)) {

                        for (int j = 0; j < selectedList.size(); j++) {
                            if (selectedList.get(j).get(Constant.KEY_ID).equalsIgnoreCase(paymentGatewayList.get(position).get(Constant.KEY_ID)))
                            {
                                selectedList.remove(j);
                                break;

                            }
                        }
                    }
                }

            }
        });

        return convertView;
    }

    class ViewHolder {

        CheckBox checkBox;
        TextView nameTV;


    }

    public boolean isContain(String id, ArrayList<HashMap<String, String>> list) {

        for (int i = 0; i < list.size(); i++)
            if (list.get(i).get(Constant.KEY_ID).equalsIgnoreCase(id))
                return true;

        return false;
    }
}
