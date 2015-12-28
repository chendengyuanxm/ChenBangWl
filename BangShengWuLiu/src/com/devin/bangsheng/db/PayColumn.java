package com.devin.bangsheng.db;

import java.util.HashMap;
import java.util.Map;

import com.devin.framework.db.base.DatabaseColumn;

public class PayColumn extends DatabaseColumn{
	
	public static final String TABLE_NAME = "pay";
	public static final String SYSTRACE = "sysTrace";
	public static final String SAVE_DATA = "saveData";
	
	private static final Map<String,String> mColumnMap = new HashMap<String,String>();
	 static {
		 
		 mColumnMap.put(_ID, "integer primary key autoincrement");
		 mColumnMap.put(SYSTRACE, "varchar(42)");
		 mColumnMap.put(SAVE_DATA, "text");
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
