package com.devin.bangsheng;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.devin.bangsheng.bean.User;
import com.devin.bangsheng.device.ME3xDeviceControllerImpl;
import com.devin.bangsheng.util.PosTraceGenerator;
import com.devin.framework.base.DvBaseApplication;


public class BaseApplication extends DvBaseApplication{
	
	private User user;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
		SDKInitializer.initialize(getApplicationContext());  
		instance = this;
		init();
	}
	
	public static BaseApplication getInstance() {
		return (BaseApplication) instance;
	}
	
	private void init() {
		PosTraceGenerator.getInstance().init();
		//启动主服务
		Intent mainService = new Intent(this, MainService.class);
		startService(mainService);
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * 保存全局参数
	 * 
	 * @param outState
	 */
	public void saveParams(Bundle outState) {
		outState.putSerializable("currUser", user);
	}

	/**
	 * 恢复全局参数
	 * 
	 * @param saveInstance
	 */
	public void restoreParams(Bundle saveInstance) {
		user = (User) saveInstance.get("currUser");
		setUser(user);
	}
	
	@Override
	public void exit() {
		super.exit();
		Intent mainService = new Intent(this, MainService.class);
		stopService(mainService);
		ME3xDeviceControllerImpl.getInstance().destroy();
	}
}
