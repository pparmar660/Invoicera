package com.invoicera.ListViewAdpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.invoicera.GlobalData.Constant.DrwerListSection;
import com.invoicera.androidapp.Global;
import com.invoicera.androidapp.R;
import com.invoicera.model.LeftDrawerItem;

import java.util.ArrayList;

public class LeftDrawer extends BaseAdapter {

	ArrayList<LeftDrawerItem> List = new ArrayList<LeftDrawerItem>();
	Context context;
	Global global;

	public LeftDrawer(ArrayList<LeftDrawerItem> list, Context context) {

		this.List = list;
		System.out.println("List Get:" + list);
		this.context = context;
		this.global = (Global) context.getApplicationContext();

		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return List.size();
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

		LeftDrawerItem map = new LeftDrawerItem();
		map = List.get(position);
		DrwerListSection type = DrwerListSection.valueOf(map.getType());
		ViewHolder holder = new ViewHolder();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		switch (type) {
		case DRAWER_HEADER:
			convertView = inflater.inflate(R.layout.drawer_header, parent,
					false);

			holder.imageView = (ImageView) convertView
					.findViewById(R.id.drawer_header);
			holder.imageView.setImageResource(map.getResId());

			break;
		case DRAWER_SECTION:
			convertView = inflater.inflate(R.layout.drawer_section, parent,
					false);

			holder.textView = (TextView) convertView
					.findViewById(R.id.drawer_section_tv);
			holder.textView.setText(map.getItemName());

			break;
		case DRAWER_ITEM:
			convertView = inflater.inflate(R.layout.drawer_item, parent, false);

			holder.imageView = (ImageView) convertView
					.findViewById(R.id.drawer_item_img);
			holder.textView = (TextView) convertView
					.findViewById(R.id.drawer_item_tv);

			holder.imageView.setImageResource(map.getResId());

			holder.textView.setText(map.getItemName());

			break;

		}

		return convertView;
	}

	class ViewHolder {

		TextView textView;
		ImageView imageView;

	}
}
