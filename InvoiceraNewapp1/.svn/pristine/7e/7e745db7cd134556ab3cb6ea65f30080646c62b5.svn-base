package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.invoicera.androidapp.R;
import com.invoicera.model.ClientAttribute;

import java.util.ArrayList;

public class SelectClientAdpter extends BaseAdapter {

	Context context;


    ArrayList<ClientAttribute> clientList;
	public SelectClientAdpter(Context context,ArrayList<ClientAttribute> clientList) {
		// TODO Auto-generated constructor stub

		this.context = context;
        this.clientList=clientList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clientList.size();
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

        viewHolder holder;
        if(convertView==null){

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.select_client_adpter, parent,
                    false);

            holder=new viewHolder();

            holder.clientName=(TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);



        }else {
            holder=(viewHolder)convertView.getTag();
        }

        holder.clientName.setText(clientList.get(position).getOrganizationname());

		return convertView;
	}

    class  viewHolder{

        TextView clientName;


    }

}
