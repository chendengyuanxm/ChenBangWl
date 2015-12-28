package com.devin.bangsheng.ui;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.DeviceManagerListAdapter;
import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.db.biz.DeviceDB;
import com.devin.bangsheng.device.AddBluetooth;
import com.devin.bangsheng.device.ME3xDeviceController;
import com.devin.bangsheng.device.ME3xDeviceControllerImpl;
import com.devin.bangsheng.interfaces.OnDeviceSelectedListener;
import com.devin.bangsheng.view.OptionsDialog;
import com.devin.framework.db.base.DBHelper;
import com.devin.framework.util.DvAppUtils;
import com.devin.framework.util.UIHelper;

public class DeviceConnManagerActivity extends BaseActivity 
							implements OnClickListener, OnItemClickListener{
	
	private TextView currDeviceTv;
	private ImageView dStatusIv;
	private ListView mDeviceLv;
	private DeviceManagerListAdapter mAdapter;
	private List<Device> mDevices = new ArrayList<Device>();
	
	private ME3xDeviceController deviceController = ME3xDeviceControllerImpl.getInstance();
	private DeviceDB mDeviceDb;
	private DoWhatAfterBluetoothOpened doWhat;
	/**
	 * 打开蓝牙设备后做
	 * */
	private enum DoWhatAfterBluetoothOpened {
		search, connect;
	}
	private Device mCurrConnectDevice;	//当前连接设备
	private Device mSelectedDevice;	//选中设备
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_conn_manager);
		initTopbar("设备连接管理");
		mDeviceDb = DeviceDB.getInstance(context);
		mDevices = mDeviceDb.queryAll();
		initViews();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	private void initViews() {
		currDeviceTv = (TextView) findViewById(R.id.tv_curr_device);
		dStatusIv = (ImageView) findViewById(R.id.iv_device_conn_status);
		Button addDeviceBtn = (Button) findViewById(R.id.btn_add_device);
		addDeviceBtn.setOnClickListener(this);
		
		mDeviceLv = (ListView) findViewById(R.id.lv_devices);
		mAdapter = new DeviceManagerListAdapter(this, mDevices);
		mDeviceLv.setAdapter(mAdapter);
		mDeviceLv.setOnItemClickListener(this);
		
		mCurrConnectDevice = deviceController.getConnectedDevice();
		refreshDevicePanel();
	}

	@Override
	public void onResponse(Message msg) {
		
	}
	
	private void performConnectDevice(final Device device) {
		mSelectedDevice = device;
		if(!DvAppUtils.isBluetoothEnable()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			doWhat = DoWhatAfterBluetoothOpened.search;// 搜索设备
			startActivity(enableBtIntent);
			return;
		}
		try {
			deviceController.connect(mSelectedDevice);
			mCurrConnectDevice = mSelectedDevice;
			refreshDevicePanel();
		} catch (Exception e) {
			e.printStackTrace();
			UIHelper.showToast(context, e.getLocalizedMessage());
		}
	}
	
	private void performDisconnectDevice(final Device device) {
		deviceController.destroy();
		mCurrConnectDevice = null;
		refreshDevicePanel();
	}
	
	private void performSetDefault(final Device device) {
		mDeviceDb.setDefault(device, true);
		mDevices.clear();
		mDevices.addAll(mDeviceDb.queryAll());
		mAdapter.notifyDataSetChanged();
	}
	
	private void performDeleteDevice(final Device device) {
		performDisconnectDevice(device);
		mDeviceDb.delete(device.getId());
		mDevices.remove(device);
		mAdapter.notifyDataSetChanged();
	}
	
	private void refreshDevicePanel() {
		if(mCurrConnectDevice == null) {
			currDeviceTv.setText("设备未连接");
			dStatusIv.setImageResource(R.drawable.img_connect);
			dStatusIv.setVisibility(View.INVISIBLE);
		}else {
			currDeviceTv.setText(mCurrConnectDevice.getDeviceName());
			dStatusIv.setImageResource(R.drawable.img_connect);
			dStatusIv.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if(!DvAppUtils.isBluetoothEnable()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			doWhat = DoWhatAfterBluetoothOpened.search;// 搜索设备
			startActivity(enableBtIntent);
			return;
		}
		searchBluetoothDevice();
	}
	
	private void saveDevice2Db(Device device) {
		if(mDevices.size() == 0) device.setDefault(true); 
		mDeviceDb.insert(device);
		mDevices = mDeviceDb.queryAll();
		mAdapter = new DeviceManagerListAdapter(context, mDevices);
		mDeviceLv.setAdapter(mAdapter);
	}
	
	private void searchBluetoothDevice() {
		AddBluetooth.searchBluetoothDevice(context, null, new OnDeviceSelectedListener() {
			
			@Override
			public void onDeviceSelected(Device device) {
				saveDevice2Db(device);
				refreshDevicePanel();
			}
			@Override
			public void onDeviceNotFound() {
			}
			@Override
			public void onCancel() {
			}
		});
	}
	
	private void showUnConnectedDevice(final Device selectedDevice) {
		OptionsDialog dialog = new OptionsDialog(this, new OptionsDialog.OnOptionsClickedListener() {
			
			@Override
			public void onOptionsClicked(View view, int position) {
				switch (position) {
				case 0:
					performConnectDevice(selectedDevice);
					break;
				case 1:
					performDeleteDevice(selectedDevice);
					break;
				case 2:
					performSetDefault(selectedDevice);
					break;
				case 3:
					break;
				}
			}
		}, null, new String[]{"连接设备", "删除设备", "设为默认", "取消"});
		dialog.show();
	}
	
	private void showConnectedDevice(final Device selectedDevice) {
		OptionsDialog dialog = new OptionsDialog(this, new OptionsDialog.OnOptionsClickedListener() {
			
			@Override
			public void onOptionsClicked(View view, int position) {
				switch (position) {
				case 0:
					performDisconnectDevice(selectedDevice);
					break;
				case 1:
					performDeleteDevice(selectedDevice);
					break;
				case 2:
					performSetDefault(selectedDevice);
					break;
				case 3:
					break;
				}
			}
		}, null, new String[]{"断开设备", "删除设备", "设为默认", "取消"});
		dialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Device selectedDevice = (Device) mAdapter.getItem(position);
		if(deviceController.getConnectedDevice() == null) {
			showUnConnectedDevice(selectedDevice);
		}else {
			showConnectedDevice(selectedDevice);
		}
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				int state = intent
						.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
				if (state == BluetoothAdapter.STATE_ON) {
					if (doWhat == DoWhatAfterBluetoothOpened.search) {
						searchBluetoothDevice();
					} else {
						performConnectDevice(mSelectedDevice);
					}
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
}
