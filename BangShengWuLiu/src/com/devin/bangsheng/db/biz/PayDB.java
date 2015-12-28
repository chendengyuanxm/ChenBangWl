package com.devin.bangsheng.db.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.devin.bangsheng.db.DeviceColumn;
import com.devin.bangsheng.db.PayColumn;
import com.devin.framework.db.base.DBHelper;

public class PayDB {
	private static PayDB instance;
	private DBHelper dbHelper;
	
	private PayDB(Context context) {
		dbHelper = DBHelper.getInstance(context);
	}
	
	public static PayDB getInstance(Context context) {
		if(instance == null) {
			instance = new PayDB(context);
		}
		return instance;
	}
	
	public synchronized void insert(String sysTrace, String saveData) {
		 ContentValues contentValues = new ContentValues();
		 contentValues.put(PayColumn.SYSTRACE, sysTrace);
		 contentValues.put(PayColumn.SAVE_DATA, saveData);
		 
		 long i = dbHelper.insert(PayColumn.TABLE_NAME, contentValues);
	}
	
	public synchronized List<Map<String, String>> queryAll() {
		Cursor c = dbHelper.query(PayColumn.TABLE_NAME, null, null, null);
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		while(c.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(PayColumn.SYSTRACE, c.getString(c.getColumnIndex(PayColumn.SYSTRACE)));
			map.put(PayColumn.SAVE_DATA, c.getString(c.getColumnIndex(PayColumn.SAVE_DATA)));
			list.add(map);
		}
		
		return list;
	}
	
	public synchronized void delete(String sysTrace) {
		dbHelper.delete(PayColumn.TABLE_NAME, PayColumn.SYSTRACE+"=?", new String[]{sysTrace});
	}
	
	public synchronized int update(String sysTrace, String saveData) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PayColumn.SYSTRACE, sysTrace);
		contentValues.put(PayColumn.SAVE_DATA, saveData);
		return dbHelper.update(PayColumn.TABLE_NAME, contentValues, PayColumn.SYSTRACE+"=?", new String[]{sysTrace});
	}
}
