package com.devin.bangsheng.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.widget.Toast;

import com.devin.bangsheng.R;
import com.devin.bangsheng.view.TabBar;
import com.devin.bangsheng.view.TabBar.OnNavItemClickListener;

public class HomeActivity extends BaseActivity implements OnNavItemClickListener{
	
	private static final String DELIVERY = "快递";
	private static final String SETTING = "设置";
	
	private FragmentTabHost mTabHost;
	private FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		initViews();
	}

	private void initViews() {
		mFragmentManager = getSupportFragmentManager();
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, mFragmentManager, R.id.realtabcontent);
		
		mTabHost.addTab(mTabHost.newTabSpec(DELIVERY).setIndicator(DELIVERY), TabDeliveryFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(SETTING).setIndicator(SETTING), TabSettingFragment.class, null);
		
		TabBar navigation = (TabBar) findViewById(R.id.navigation);
		navigation.setOnNavItemClickListener(this);
	}

	@Override
	public void onItemClick(int position) {
		switch (position) {
		case 0:		//收付款
			mTabHost.setCurrentTabByTag("快递");
			break;
		case 1:		//生活服务
			mTabHost.setCurrentTabByTag("设置");
			break;
		}
	}
	
	long exitTime;
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			mApplication.exit();
		}
	}

	@Override
	public void onResponse(Message msg) {
	}
}
