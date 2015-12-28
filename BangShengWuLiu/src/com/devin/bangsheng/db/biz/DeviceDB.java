package com.devin.bangsheng.db.biz;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.db.DeviceColumn;
import com.devin.bangsheng.enums.DeviceType;
import com.devin.framework.db.base.DBHelper;

public class DeviceDB{
	
	private static DeviceDB instance;
	private DBHelper dbHelper;
	
	private DeviceDB(Context context) {
		dbHelper = DBHelper.getInstance(context);
	}
	
	public static DeviceDB getInstance(Context context) {
		if(instance == null) {
			instance = new DeviceDB(context);
		}
		return instance;
	}
	
	public List<Device> queryAll() {
		Cursor c = dbHelper.query(DeviceColumn.TABLE_NAME, null, null, null);
		List<Device> lists = new ArrayList<Device>();
		while(c.moveToNext()) {
			Device device = new Device();
			device.id = c.getString(c.getColumnIndex(DeviceColumn._ID));
			device.deviceName = c.getString(c.getColumnIndex(DeviceColumn.DEVICE_NAME));
			device.macAddress = c.getString(c.getColumnIndex(DeviceColumn.MAC_ADDRESS));
			device.deviceSn = c.getString(c.getColumnIndex(DeviceColumn.DEVICE_SN));
			device.isDefault = c.getInt(c.getColumnIndex(DeviceColumn.DEFAULT)) == 0 ? false : true;
			device.isActive = c.getInt(c.getColumnIndex(DeviceColumn.ACTIVITY));
			device.deviceType = DeviceType.valueOf(c.getString(c.getColumnIndex(DeviceColumn.DEVICE_TYPE)));
			lists.add(device);
		}
		
		return lists;
	}
	
	public boolean isRepeat(String macAddress) {
		Cursor c = dbHelper.query(DeviceColumn.TABLE_NAME, null, DeviceColumn.MAC_ADDRESS+"=?", new String[]{macAddress});
		if(c.moveToNext()) {
			return true;
		}
		
		return false;
	}
	
	public void insert(String deviceName, String macAddress, String deviceSn, boolean isDefault, int isActivity, DeviceType deviceType) {
		if(isRepeat(macAddress)) return;
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(DeviceColumn.DEVICE_NAME, deviceName);
		contentValues.put(DeviceColumn.MAC_ADDRESS, macAddress);
		contentValues.put(DeviceColumn.DEVICE_SN, deviceSn);
		contentValues.put(DeviceColumn.DEFAULT, isDefault ? "1" : "0");
		contentValues.put(DeviceColumn.ACTIVITY, isActivity);
		contentValues.put(DeviceColumn.DEVICE_TYPE, deviceType.name());
		dbHelper.insert(DeviceColumn.TABLE_NAME, contentValues);
	}
	
	public void insert(Device device) {
		if(device == null) return;
		insert(device.deviceName
				, device.macAddress
				, device.deviceSn
				, device.isDefault
				, device.isActive
				, device.deviceType);
	}
	
	public void insert(List<Device> devices) {
		if(devices == null) return;
		
		for(int i = 0; i < devices.size(); i ++) {
			Device device = devices.get(i);
			insert(device);
		}
	}
	
	public void delete(String id) {
		dbHelper.delete(DeviceColumn.TABLE_NAME, Integer.parseInt(id));
	}
	
	public int update(String id, String deviceName, String macAddress, String deviceSn, boolean isDefault, int isActivity, DeviceType deviceType) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DeviceColumn.DEVICE_NAME, deviceName);
		contentValues.put(DeviceColumn.MAC_ADDRESS, macAddress);
		contentValues.put(DeviceColumn.DEVICE_SN, deviceSn);
		contentValues.put(DeviceColumn.DEFAULT, isDefault ? "1" : "0");
		contentValues.put(DeviceColumn.ACTIVITY, isActivity);
		contentValues.put(DeviceColumn.DEVICE_TYPE, deviceType.name());
		return dbHelper.update(DeviceColumn.TABLE_NAME, contentValues, DeviceColumn.MAC_ADDRESS+"=?", new String[]{macAddress});
	}
	
	public void setDefault(Device device, boolean isDefault) {
		int r = update(device.getId(), device.getDeviceName(), device.getMacAddress(), device.getDeviceSn(), isDefault, device.getIsActive(), device.getDeviceType());
		if(r != -1 && isDefault) {
			List<Device> devices = queryAll();
			for(int i = 0; i < devices.size(); i ++) {
				Device d = devices.get(i);
				if(d.isDefault && !device.getMacAddress().equals(d.getMacAddress())) {
					update(d.getId(), d.getDeviceName(), d.getMacAddress(), d.getDeviceSn(), !isDefault, d.getIsActive(), d.getDeviceType());
				}
			}
		}
	}
	
	public Device getDefaultDevice() {
		String sql = "select * from " + DeviceColumn.TABLE_NAME + " where " + DeviceColumn.DEFAULT + "=1";
		Cursor c = dbHelper.rawQuery(sql, null);
		if(c.moveToNext()) {
			Device device = new Device();
			device.id = c.getString(c.getColumnIndex(DeviceColumn._ID));
			device.deviceName = c.getString(c.getColumnIndex(DeviceColumn.DEVICE_NAME));
			device.macAddress = c.getString(c.getColumnIndex(DeviceColumn.MAC_ADDRESS));
			device.deviceSn = c.getString(c.getColumnIndex(DeviceColumn.DEVICE_SN));
			device.isDefault = c.getInt(c.getColumnIndex(DeviceColumn.DEFAULT)) == 0 ? false : true;
			device.isActive = c.getInt(c.getColumnIndex(DeviceColumn.ACTIVITY));
			device.deviceType = DeviceType.valueOf(c.getString(c.getColumnIndex(DeviceColumn.DEVICE_TYPE)));
			return device;
		}
		
		return null;
	}
}
