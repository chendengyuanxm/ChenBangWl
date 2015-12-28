package com.devin.bangsheng.db;

import java.util.HashMap;
import java.util.Map;

import com.devin.framework.db.base.DatabaseColumn;

public class DeviceColumn extends DatabaseColumn{
	
	public static final String TABLE_NAME = "device";
	public static final String DEVICE_NAME = "deviceName";
	public static final String MAC_ADDRESS = "macAddress";
	public static final String DEVICE_SN = "deviceSn";
	public static final String DEFAULT = "isDefault";
	public static final String ACTIVITY = "isActivity";
	public static final String DEVICE_TYPE = "deviceType";
	
	private static final Map<String,String> mColumnMap = new HashMap<String,String>();
	 static {
		 
		 mColumnMap.put(_ID, "integer primary key autoincrement");
		 mColumnMap.put(DEVICE_NAME, "varchar(16) not null");
		 mColumnMap.put(MAC_ADDRESS, "char(11)");
		 mColumnMap.put(DEVICE_SN, "char(12)");
		 mColumnMap.put(DEFAULT, "bool");
		 mColumnMap.put(ACTIVITY, "int");
		 mColumnMap.put(DEVICE_TYPE, "varchar(8)");
	 }

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> getTableMap() {
		return mColumnMap;
	}

}
