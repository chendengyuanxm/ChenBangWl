package com.devin.framework.base;

import com.devin.framework.ui.base.DvBaseActivity;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
/**
 * 全局应用程序类，保存应用的全局方法和全局参数
 *
 * @author Devin_chen
 * @date 2015年3月8日
 * 
 * @version 1.0
 */
public class DvBaseApplication extends Application{
	
	public static DvBaseApplication instance;
	private AppManager mAppManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		mAppManager = AppManager.getAppManager();
	}
	
	public static DvBaseApplication getInstance() {
		return instance;
	}
	
	/**
	 * 是否是第一次运行当前版本应用
	 * 
	 * @return
	 * @version 1.0
	 */
	public boolean isFirstRun() {
		PackageManager packageManager = getPackageManager();
		boolean firstRun = false;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			firstRun = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
					.getBoolean(String.format("%s%04d", "firstRun",packageInfo.versionCode), true);
		} catch (Exception e) {
		}

		return firstRun;
	}
	
	public void addActivity(DvBaseActivity activity) {
		mAppManager.addActivity(activity);
	}
	
	public void finishActivity(DvBaseActivity activity) {
		mAppManager.finishActivity(activity);
	}
	
	public void finishAll() {
		mAppManager.finishAllActivity();
	}
	
	public void exit() {
		finishAll();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		exit();
	}
}
