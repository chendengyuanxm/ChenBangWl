package com.devin.bangsheng.interfaces;

import com.devin.bangsheng.bean.Device;


public interface OnDeviceSelectedListener {
	
	public void onDeviceSelected(Device device);
	
	public void onCancel();
	
	public void onDeviceNotFound();
}
