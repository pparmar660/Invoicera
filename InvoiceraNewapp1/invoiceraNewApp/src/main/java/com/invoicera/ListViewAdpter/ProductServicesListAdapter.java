package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.invoicera.Utility.MyDateFormat;
import com.invoicera.androidapp.Global;
import com.invoicera.androidapp.R;
import com.invoicera.model.ProductServicesAttribute;

import java.util.ArrayList;

/**
 * Created by Parvesh on 25/8/15.
 */
public class ProductServicesListAdapter extends BaseAdapter {

    Context context;
    Global global;
    ArrayList<ProductServicesAttribute> list;

    public ProductServicesListAdapter(Context context, ArrayList<ProductServicesAttribute> list) {

        this.context = context;
        this.list = list;
        global = (Global) context.getApplicationContext();

    }


    @Override
    public int getCount() {
        return list.size();
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

        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            LayoutInflater infalter = LayoutInflater.from(context);
            convertView = infalter.inflate(R.layout.product_services_list_adapter, parent, false);
            holder.arrowImage = (ImageView) convertView.findViewById(R.id.arrow);
            holder.dateTv = (TextView) convertView.findViewById(R.id.date);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name);
            holder.quantityTv = (TextView) convertView.findViewById(R.id.quantity);
            holder.unitCostTv = (TextView) convertView.findViewById(R.id.unitCost);
            holder.statusTv=(TextView)convertView.findViewById(R.id.status);
            convertView.setTag(holder);


        } else holder = (ViewHolder) convertView.getTag();


        holder.nameTv.setText(list.get(position).getName());
        holder.dateTv.setText(MyDateFormat.GetDate(list.get(position).getDate()));
        holder.unitCostTv.setText(list.get(position).getCurrency_name() + " " + global.setLength(list.get(position).getUnitCost(),9));
        holder.statusTv.setText(list.get(position).getStatus());
        if (list.get(position).getType().equalsIgnoreCase("Product"))
            holder.quantityTv.setText("QTY : " +global.setLength(list.get(position).getQuantity().replaceAll(",",""),9));
        else
            holder.quantityTv.setText("UNIT : " + global.setLength(list.get(position).getQuantity(),9));


        return convertView;
    }


    class ViewHolder {


        TextView nameTv, dateTv, quantityTv, unitCostTv,statusTv;
        ImageView arrowImage;


    }
}