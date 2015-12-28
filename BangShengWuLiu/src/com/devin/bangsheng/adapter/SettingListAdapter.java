package com.devin.bangsheng.adapter;

import com.devin.bangsheng.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingListAdapter extends BaseAdapter{
	
	private Context context;
	private String[] labels;
	private Drawable[] iconIds;
	
	public SettingListAdapter(Context context, String[] labels, Drawable[] iconIds) {
		this.context = context;
		this.labels = labels;
		this.iconIds = iconIds;
	}

	@Override
	public int getCount() {
		return labels.length;
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
		View v = LayoutInflater.from(context).inflate(R.layout.item_setting, null);
		ImageView iconIv = (ImageView) v.findViewById(R.id.icon);
		TextView labelTv = (TextView) v.findViewById(R.id.label);
		View lineV = (View) v.findViewById(R.id.v_line);
		
		String label = labels[position];
		Drawable icon = iconIds[position];
		iconIv.setImageDrawable(icon);
		labelTv.setText(label);
		if(position == labels.length-1) {
			lineV.setVisibility(View.INVISIBLE);
		}
		
		return v;
	}

}
