package com.devin.bangsheng.device;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.interfaces.OnDeviceFoundListener;
import com.devin.bangsheng.interfaces.OnDeviceSelectedListener;
import com.devin.bangsheng.view.OptionsDialog;
import com.devin.bangsheng.view.OptionsDialog.OnOptionsClickedListener;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.util.UIHelper;

public class AddBluetooth {
	
	private static Logger logger = LoggerFactory.getLogger(AddBluetooth.class);
	
	private static final long SEARCH_BT_DEVICE_DURATION = 3*1000;
	
	public AddBluetooth(String appKeyId) {
	}
	
	public static void searchBluetoothDevice(final Context context
			, final String showMessage, final OnDeviceSelectedListener listener) {
		final Dialog scanningDialog = UIHelper.createProgressbar(context, "正在搜索设备");
		scanningDialog.show();
		final List<Device> deviceProfiles = new ArrayList<Device>();
		final BluetoothSearch btSearch = new BluetoothSearch(context);
		btSearch.setBluetoothSearchListener(new OnDeviceFoundListener() {

			@Override
			public void onDeviceFound(Device btDevice) {

				if (isDeviceRepeat(deviceProfiles, btDevice)) {
					return;
				}
				deviceProfiles.add(btDevice);
			}
		});
		
		btSearch.startSearch();
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

			@Override
			public void run() {
				btSearch.destorySearch();
				if (scanningDialog != null && scanningDialog.isShowing()) {
					scanningDialog.dismiss();
				}
				if (deviceProfiles.size() == 0) {
					if(listener != null) listener.onDeviceNotFound();
					UIHelper.showToast(context, "未搜索到蓝牙设备!");
					return;
				}
				
				final String[] items = getDeviceNames(deviceProfiles);
				OptionsDialog foundBtDevicesDialog = new OptionsDialog(context, new OnOptionsClickedListener() {
					
					@Override
					public void onOptionsClicked(View view, int position) {
						if(position == items.length-1) {
							//取消
							if(listener != null)
								listener.onCancel();
						}else {
							if(listener != null) {
								listener.onDeviceSelected(deviceProfiles.get(position));
							}
						}
					}
				}, showMessage, items);
				foundBtDevicesDialog.setCancelable(false);
				foundBtDevicesDialog.show();
			}
		}, SEARCH_BT_DEVICE_DURATION);
	}
	
	/**
	 * 判断蓝牙检测到的设备是否已存在
	 * 
	 * @param devices
	 * @param device
	 * @return
	 */
	private synchronized static boolean isDeviceRepeat(List<Device> devices,
			Device device) {
		if (devices == null || devices.size() == 0) {
			return false;
		}
		for (Device d : devices) {
			String dAddress = d.getMacAddress();
			if (dAddress.equals(device.getMacAddress())) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * 获取设备名数组,默认设备始终在第一条，不管是否搜索到,如下规则：<br>
	 *   		
	 * XXX (默认)<br>
	 * XXX		<br>
	 * XXX		<br>
	 * 
	 * @param devices
	 * @return
	 */
	private static String[] getDeviceNames(List<Device> devices) {
		logger.info("============蓝牙搜索到的设备================");
		for(Device device : devices) {
			logger.info("   " + device.getDeviceName());
		}
		logger.info("===========================================");
		String[] deviceNames = new String[devices.size() + 1];
		for(int i = 0; i < devices.size(); i ++) {
			Device device = devices.get(i);
			deviceNames[i] = device.getDeviceName() != null ? device
					.getDeviceName() : device.getMacAddress();
		}
		deviceNames[deviceNames.length - 1] = "取消";
		return deviceNames;
	}
}
