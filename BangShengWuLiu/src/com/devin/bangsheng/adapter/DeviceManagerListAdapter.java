package com.devin.bangsheng.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.Device;

public class DeviceManagerListAdapter extends BaseAdapter{
	
	private Context context;
	private List<Device> mLists;
	
	public DeviceManagerListAdapter(Context context, List<Device> devices) {
		this.context = context;
		this.mLists = devices;
	}

	@Override
	public int getCount() {
		return mLists.size();
	}

	@Override
	public Object getItem(int position) {
		return mLists.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_device, null);
			holder = new ViewHolder();
			holder.btIv = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.defaultIv = (ImageView) convertView.findViewById(R.id.iv_default);
			holder.deviceNameTv = (TextView) convertView.findViewById(R.id.tv_deviceName);
			holder.lineV = (View) convertView.findViewById(R.id.v_line);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		Device device = mLists.get(position);
		if(device.isDefault) {
			holder.defaultIv.setVisibility(View.VISIBLE);
		}else{
			holder.defaultIv.setVisibility(View.GONE);
		}
		holder.deviceNameTv.setText(device.getDeviceName());
//		if(position == mLists.size()-1) {
//			holder.lineV.setVisibility(View.GONE);
//		}
		holder.lineV.setVisibility(View.GONE);
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView btIv;
		TextView deviceNameTv;
		ImageView defaultIv;
		View lineV;
	}
}
