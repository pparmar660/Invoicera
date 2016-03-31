package com.invoicera.SpinnerAdpter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.WALK_THROUGH_SPINNERS;
import com.invoicera.androidapp.R;

public class WalkThroughSpinerAdpter extends BaseAdapter {

    private Activity context;
    private WALK_THROUGH_SPINNERS spinnerType;
    private ArrayList<HashMap<String, String>> list;

    public WalkThroughSpinerAdpter(Activity context, ArrayList<HashMap<String, String>> list, Constant.WALK_THROUGH_SPINNERS spinnerType) {
        this.context = context;
        this.list = list;
        this.spinnerType = spinnerType;

    }

    public WalkThroughSpinerAdpter(Activity context, Constant.WALK_THROUGH_SPINNERS spinnerType) {
        this.context = context;
        this.spinnerType = spinnerType;
        this.list = new ArrayList<HashMap<String, String>>();
    }

    public void add(HashMap<String, String> hashMap) {
        this.list.add(hashMap);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HashMap<String,String> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setText(list.get(position).get(Constant.KEY_NAME));
        holder.nameTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.tx_ar, 0);
        holder.nameTv.setTextColor(Color.BLACK);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setBackgroundColor(Color.WHITE);
        holder.nameTv.setText(list.get(position).get(Constant.KEY_NAME));
        holder.nameTv.setTextColor(Color.BLACK);
        return convertView;
    }

    class ViewHolder {
        TextView nameTv;
    }
}
