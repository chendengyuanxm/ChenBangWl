package com.devin.bangsheng.adapter;

import java.util.List;

import com.devin.bangsheng.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanListAdapter extends BaseAdapter{
	
	private List<String> orders;
	private Context context;
	
	public ScanListAdapter(Context context, List<String> orders) {
		this.context = context;
		this.orders = orders;
	}

	@Override
	public int getCount() {
		return orders.size();
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
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_scan_list, null);
			holder = new ViewHolder();
			holder.sortTv = (TextView) convertView.findViewById(R.id.tv_sort);
			holder.orderNoTv = (TextView) convertView.findViewById(R.id.tv_orderNo);
			holder.delIv = (ImageView) convertView.findViewById(R.id.iv_del);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.sortTv.setText(position+"");
		holder.orderNoTv.setText(orders.get(position));
		return convertView;
	}
	
	class ViewHolder {
		TextView sortTv;
		TextView orderNoTv;
		ImageView delIv;
	}
}
