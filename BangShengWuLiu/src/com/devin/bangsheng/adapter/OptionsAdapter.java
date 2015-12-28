package com.devin.bangsheng.adapter;

import com.devin.bangsheng.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OptionsAdapter extends BaseAdapter{
	
	private Context context;
	private String[] options;
	
	public OptionsAdapter(Context context, String... options) {
		this.context = context;
		this.options = options;
	}

	@Override
	public int getCount() {
		return options.length;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_options, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(options[position]);
		
		return convertView;
	}
	
	class ViewHolder {
		TextView tv;
	}
}
