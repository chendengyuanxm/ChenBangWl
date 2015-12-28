package com.devin.bangsheng.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.enums.DeviceType;
import com.devin.bangsheng.interfaces.OnDeviceFoundListener;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.util.DvStrUtils;

public class BluetoothSearch {
	
	private Logger logger = LoggerFactory.getLogger(BluetoothSearch.class);

    private BluetoothAdapter bluetoothAdapter;

    private Context context;

    private OnDeviceFoundListener  onDeviceFoundListener;
    
    private boolean isSearching;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                if(onDeviceFoundListener != null){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    logger.info("Found device:" + device.getName() + "****" + device.getAddress());
                    if(DvStrUtils.isEmpty(device.getName())) return;		//蓝牙名称没有搜索到直接过滤
                    Device btDevice = new Device();
                    btDevice.setMacAddress(device.getAddress());
                    btDevice.setDeviceName(DvStrUtils.isEmpty(device.getName()) ? device.getAddress() : device.getName());
                    btDevice.setDeviceType(DeviceType.UNKNOW);
                    btDevice.setDefault(false);
                    onDeviceFoundListener.onDeviceFound(btDevice);
                }
            }
        }
    };

    /**
     * 实例化
     * 注册蓝牙搜索广播
     * @param context
     */
    public BluetoothSearch(Context context){
        this.context = context;
        context.registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        isSearching = false;
    }
    
    /**
     * 设置蓝牙搜索侦听
     * @param onDeviceFoundListener
     */
    public void setBluetoothSearchListener(OnDeviceFoundListener onDeviceFoundListener) {
    	this.onDeviceFoundListener = onDeviceFoundListener;
    }

    /**
     * 开始蓝牙搜索
     */
    public void startSearch(){
    	logger.info("start search...");
        bluetoothAdapter.startDiscovery();
        isSearching = true;
    }

    /**
     * 停止搜索
     */
    public void stopSearch(){
    	logger.info("stop search...");
        bluetoothAdapter.cancelDiscovery();
        isSearching = false;
    }

    /**
     * 注销
     */
    public void destorySearch(){
    	if(isSearching) {
    		bluetoothAdapter.cancelDiscovery();
            context.unregisterReceiver(broadcastReceiver);
    	}
        isSearching = false;
    }

}
