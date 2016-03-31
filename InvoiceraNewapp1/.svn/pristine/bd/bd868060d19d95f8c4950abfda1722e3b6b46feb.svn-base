package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant;
import com.invoicera.Utility.MyDateFormat;
import com.invoicera.androidapp.Global;
import com.invoicera.androidapp.R;
import com.invoicera.model.ExpenseAttribute;

import java.util.ArrayList;

/**
 * Created by Parvesh on 28/9/15.
 */
public class ExpenseListPagerFragmentListAdapter extends BaseAdapter {

    Context context;
    public ArrayList<ExpenseAttribute> expenseList;
    public Global global;

    public ExpenseListPagerFragmentListAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.expenseList = new ArrayList<ExpenseAttribute>();
        this.context = context;
        global = (Global) context.getApplicationContext();

    }

    public void add(ExpenseAttribute attribute) {
        expenseList.add(attribute);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return expenseList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.expense_list_pager_list_adapter, parent, false);

            holder = new ViewHolder();
            holder.clientTv = (TextView) convertView.findViewById(R.id.client);

            holder.categoryTv = (TextView) convertView
                    .findViewById(R.id.category);
            holder.dateTv = (TextView) convertView
                    .findViewById(R.id.date);
            holder.statusTv = (TextView) convertView.findViewById(R.id.status);
            holder.expenseStatusTv = (TextView) convertView.findViewById(R.id.expense_status);
            holder.amountTv = (TextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        String client = "";

        if (expenseList.get(position)
                .getClient_name().length() > 10)
            client = expenseList.get(position)
                    .getClient_name().substring(0, 10) + "..";
        else
            client = expenseList.get(position)
                    .getClient_name();

        if (client.isEmpty())
client="N/A";
        holder.clientTv.setText(client);
        holder.statusTv.setText(expenseList.get(position).getStatus());

        holder.categoryTv.setText(expenseList.get(position).getCategory());


        holder.amountTv.setText(expenseList.get(position).getCurrency()
                + " " + global.setLength(expenseList.get(position).getAmount(), Constant.defultLengthOfText));

        holder.dateTv.setText(MyDateFormat.GetDate(expenseList.get(position).getDate()));
        String statusString = expenseList.get(position).getExpense_status();
        holder.expenseStatusTv.setText(statusString);

        if (statusString.equalsIgnoreCase("Paid")) {

            holder.expenseStatusTv.setBackgroundColor(Color.parseColor("#4a8f01"));
        } else if (statusString.equalsIgnoreCase("Invoiced")) {

            holder.expenseStatusTv.setBackgroundColor(Color.parseColor("#C75F19"));
        } else if (statusString.equalsIgnoreCase("Unbilled")) {

            holder.expenseStatusTv.setBackgroundColor(Color.parseColor("#f00000"));
        }
        holder.expenseStatusTv.setTextColor(Color.parseColor("#ffffff"));
        return convertView;
    }

    class ViewHolder {

        TextView clientTv, categoryTv, dateTv, statusTv, expenseStatusTv, amountTv;

    }

}
