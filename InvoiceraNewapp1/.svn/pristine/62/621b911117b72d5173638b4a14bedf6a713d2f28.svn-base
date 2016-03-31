package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant;
import com.invoicera.androidapp.Global;
import com.invoicera.androidapp.R;
import com.invoicera.model.InvoiceAttribute;

import java.util.ArrayList;

public class InvoiceListPagerFragmentListAdapter extends BaseAdapter {

    Context context;
    public ArrayList<InvoiceAttribute> InvoicesList;
    public Global global;

    public InvoiceListPagerFragmentListAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.InvoicesList = new ArrayList<InvoiceAttribute>();
        this.context = context;
        global = (Global) context.getApplicationContext();

    }

    public void add(InvoiceAttribute attribute) {
        InvoicesList.add(attribute);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return InvoicesList.size();
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
                    R.layout.list_pager_list_adapter, parent, false);

            holder = new ViewHolder();
            holder.invNumberTv = (TextView) convertView

                    .findViewById(R.id.number);
            holder.orgnizationTv = (TextView) convertView
                    .findViewById(R.id.organization);
            holder.netBalanceTv = (TextView) convertView
                    .findViewById(R.id.netBalance);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.invoice_status = (TextView) convertView.findViewById(R.id.main_status);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        String organization = "";

        if (InvoicesList.get(position)
                .getOrganization().length() > 10)
            organization = InvoicesList.get(position)
                    .getOrganization().substring(0, 10) + "..";
        else
            organization = InvoicesList.get(position)
                    .getOrganization();

        holder.orgnizationTv.setText(organization);

        holder.status.setText(InvoicesList.get(position).getStatus());


        if (InvoicesList.get(position).getStatus().equalsIgnoreCase("cancel"))

            holder.status.setText("Deleted");

        holder.invNumberTv.setText(InvoicesList.get(position).getInvoice_no());


        holder.netBalanceTv.setText(InvoicesList.get(position).getCurrency()
                + " " + global.setLength(InvoicesList.get(position).getNet_balance(), Constant.defultLengthOfText));

        holder.time.setText(InvoicesList.get(position).getDate());
        String statusString = InvoicesList.get(position).getInvoice_status();
        holder.invoice_status.setText(statusString);

        if (statusString.equalsIgnoreCase("draft")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#7F7F7F"));
        } else if (statusString.equalsIgnoreCase("sent")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#0447AC"));
        } else if (statusString.equalsIgnoreCase("viewed")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#2CA6AF"));
        } else if (statusString.equalsIgnoreCase("paid")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#4A8F01"));// #E8B900
            // //4A8F01
        } else if (statusString.equalsIgnoreCase("partial-paid")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#5cb200"));//
        } else if (statusString.equalsIgnoreCase("outstanding")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#D05808"));
        } else if (statusString.equalsIgnoreCase("auto-failed")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#FF0000"));
        } else if (statusString.equalsIgnoreCase("auto-paid")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#B4D964"));
        } else if (statusString.equalsIgnoreCase("disputed")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#5385B4"));
        } else if (statusString.equalsIgnoreCase("resolved")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#1A5A9C"));
        } else if (statusString.equalsIgnoreCase("rejected")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#ED4E00"));
        } else if (statusString.equalsIgnoreCase("accepted")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#63A239"));
        } else if (statusString.equalsIgnoreCase("expired")) {

            holder.invoice_status.setBackgroundColor(Color.parseColor("#c0001b"));
        }
        holder.invoice_status.setTextColor(Color.parseColor("#ffffff"));
        return convertView;
    }

    class ViewHolder {

        TextView orgnizationTv, invNumberTv, netBalanceTv, time, invoice_status, status;

    }

}
