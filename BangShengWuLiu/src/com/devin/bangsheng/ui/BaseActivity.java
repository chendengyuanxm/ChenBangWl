package com.devin.bangsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.devin.bangsheng.BaseApplication;
import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.User;
import com.devin.bangsheng.service.TransferService;
import com.devin.bangsheng.view.Topbar;
import com.devin.bangsheng.view.Topbar.TopbarItem;
import com.devin.framework.ui.base.DvBaseActivity;

public abstract class BaseActivity extends DvBaseActivity{
	
	public TransferService mService;
	public BaseApplication mApplication;
	public User currUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mService = TransferService.getInstance();
		mApplication = BaseApplication.getInstance();
		currUser = mApplication.getUser();
		if (savedInstanceState != null && currUser == null) {
			mApplication.restoreParams(savedInstanceState);
			currUser = mApplication.getUser();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public abstract void onResponse(Message msg);
	
	/**
	 * 初始化标题头
	 * 
	 * @param title
	 * @version 1.0
	 */
	public void initTopbar(String title) {
		initTopbar(title, true);
	}
	
	public void initTopbar(String title, boolean canBack) {
		Topbar mTopbar = (Topbar) findViewById(R.id.topbar);
		mTopbar.setTitle(title);
		mTopbar.setBackVisible(canBack);
		mTopbar.setOnTopbarClickListener(new Topbar.OnTopbarClickListener() {
			
			@Override
			public void onTopbarBtnClick(TopbarItem topbarItem) {
				if(topbarItem == TopbarItem.back) {
					back();
				}else if(topbarItem == TopbarItem.action) {
					
				}
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null) {
				if (getCurrentFocus().getWindowToken() != null) {
					 //调用系统自带的隐藏软键盘
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);  
	}
	
	public void backHomePage() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 保存系统意外进程杀死变量
		BaseApplication.getInstance().saveParams(outState);
	}
}
