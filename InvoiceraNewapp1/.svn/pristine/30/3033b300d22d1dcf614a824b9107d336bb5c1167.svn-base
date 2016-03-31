package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.invoicera.androidapp.R;

public class ItemListAdapter extends BaseAdapter {

	Context context;

	public ItemListAdapter(Context context) {
		// TODO Auto-generated constructor stub

		this.context = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.invoice_item_adpter,
					parent, false);
			holder = new ViewHolder();
			holder.itemName = (TextView) convertView
					.findViewById(R.id.item_name);

			holder.itemPrice = (TextView) convertView
					.findViewById(R.id.itemPrice);

			holder.UnitCostQuantity = (TextView) convertView
					.findViewById(R.id.unitcost_quantity);

			holder.crossView = (ImageView) convertView.findViewById(R.id.cross);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		return convertView;
	}

	class ViewHolder {

		TextView itemPrice, UnitCostQuantity, itemName;

		ImageView crossView;

	}

}
