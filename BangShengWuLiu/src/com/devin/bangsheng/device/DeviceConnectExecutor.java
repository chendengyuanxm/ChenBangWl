package com.devin.bangsheng.device;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.common.CommonException;
import com.devin.bangsheng.db.biz.DeviceDB;
import com.devin.bangsheng.interfaces.OnDeviceSelectedListener;
import com.devin.framework.util.DvAppUtils;
import com.devin.framework.util.UIHelper;

public class DeviceConnectExecutor {
	
	private final static int MSG_CONNECT_SUCC = 0x10;
	private final static int MSG_CONNECT_FAIL = 0x11;
	
	private Context context;
	private DeviceDB mDeviceDb;
	private ME3xDeviceController deviceController;
	private Device mConnectDevice;
	
	private OnConnectDeviceListener mCallback;
	
	public DeviceConnectExecutor(Context context) {
		this.context = context;
		mDeviceDb = DeviceDB.getInstance(context);
		deviceController = ME3xDeviceControllerImpl.getInstance();
	}
	
	public void setConnectDeviceListener(OnConnectDeviceListener listener) {
		this.mCallback = listener;
	}
	
	public void performConnect() {
		if(!DvAppUtils.isBluetoothEnable()) {
			UIHelper.showToast(context, "蓝牙未打开");
			//注册蓝牙广播侦听
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			context.registerReceiver(mBroadcastReceiver, filter);
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivity(intent);
			return;
		}
		if(deviceController.getConnectedDevice() != null) {
			if(mCallback != null) mCallback.onDeviceConnected(deviceController.getConnectedDevice());
			return;
		}
		Device defaultDevice = mDeviceDb.getDefaultDevice();
		if(defaultDevice == null) {
			searchBluetooth();
			return;
		}else {
			mConnectDevice = defaultDevice;
		}
		new Thread(new ConnectRunnable()).start();
	}
	
	class ConnectRunnable implements Runnable {
		
		ProgressDialog pd;
		
		public ConnectRunnable() {
			String deviceName = mConnectDevice.getDeviceName();
			pd = UIHelper.createProgressbar(context, "正在连接设备[" + deviceName + "]...");
			pd.show();
		}

		@Override
		public void run() {
			try {
				deviceController.connect(mConnectDevice);
				mDeviceDb.insert(mConnectDevice);
				mDeviceDb.setDefault(mConnectDevice, true);
				handler.sendEmptyMessage(MSG_CONNECT_SUCC);
			} catch (CommonException e) {
				e.printStackTrace();
				handler.sendEmptyMessage(MSG_CONNECT_FAIL);
			}finally{
				pd.dismiss();
			}
		}
		
	}

	private void searchBluetooth() {
		AddBluetooth.searchBluetoothDevice(context, "请选择要连接的设备", new OnDeviceSelectedListener() {
			
			@Override
			public void onDeviceSelected(Device device) {
				mConnectDevice = device;
				new Thread(new ConnectRunnable()).start();
			}
			
			@Override
			public void onDeviceNotFound() {
				if(mCallback != null) mCallback.onCancel();
			}
			
			@Override
			public void onCancel() {
				if(mCallback != null) mCallback.onCancel();
			}
		});
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CONNECT_SUCC:
				if(mCallback != null) mCallback.onDeviceConnected(mConnectDevice);
				break;
			case MSG_CONNECT_FAIL:
				UIHelper.showDialog(context, "连接失败，是否要连接其他设备？"
						, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
//								new Thread(new ConnectRunnable()).start();
								searchBluetooth();
							}
						}, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(mCallback != null) mCallback.onCancel();
							}
						});
				break;
			}
		}
	};
	
	public interface OnConnectDeviceListener {
		public void onDeviceConnected(Device device);
		public void onCancel();
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
				if (state == BluetoothAdapter.STATE_ON) {
					context.unregisterReceiver(mBroadcastReceiver);
					performConnect();
				}
			}
		}
	};
}
