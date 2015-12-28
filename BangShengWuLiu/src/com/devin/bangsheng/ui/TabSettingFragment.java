package com.devin.bangsheng.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.SettingListAdapter;
import com.devin.bangsheng.service.TransferService;
import com.devin.bangsheng.trans.wuliu.msg.impl.GpsUploadResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryAppVerResp;
import com.devin.framework.util.DvDateUtils;
import com.devin.framework.util.UIHelper;

public class TabSettingFragment extends BaseFragment implements OnItemClickListener{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rView = inflater.inflate(R.layout.activity_tab_setting, container, false);
		initTopbar("设置", false);
		initViews();
		return rView;
	}

	private void initViews() {
		TypedArray ta = getResources().obtainTypedArray(R.array.setting_icons);
		Drawable[] icons = new Drawable[ta.length()];
		for (int i = 0; i < icons.length; i++) {
			icons[i] = ta.getDrawable(i);
		}
		ta.recycle();
		String[] labels = getResources().getStringArray(R.array.setting_labels);
		ListView lv = (ListView) rView.findViewById(R.id.lv);
		SettingListAdapter adapter = new SettingListAdapter(getActivity(), labels, icons);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		
		RelativeLayout userLayout = (RelativeLayout)rView.findViewById(R.id.rl_personal_center);
		userLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UserInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResponse(Message msg) {
		if(msg.obj instanceof QueryAppVerResp) {
			QueryAppVerResp resp = (QueryAppVerResp) msg.obj;
			if(resp.header.success()) {
				UIHelper.showToast(getActivity(), "版本查询成功");
			}else {
				showDialogEx(resp.header.getResponseMsg());
			}
		}else if(msg.obj instanceof GpsUploadResp) {
			GpsUploadResp resp = (GpsUploadResp) msg.obj;
			if(resp.header.success()) {
				UIHelper.showToast(getActivity(), "签到成功");
			}else {
				showDialogEx(resp.header.getResponseMsg());
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {
		case 0:
			intent = new Intent(getActivity(), DeviceConnManagerActivity.class);
			startActivity(intent);
			break;

		case 1:
			UIHelper.showToast(getActivity(), R.string.function_not_support);
			break;
		case 2:
			queryAppVer();
			break;
		case 3:
			uploadGps();
			break;
		}
	}
	
	private void queryAppVer() {
		showProgress();
		TransferService.getInstance().queryAppVer(defaultHttpTransfer);
	}
	
	LocationClient mLocClient = null;
	boolean waitLoc = true;	//等待定位
	private void uploadGps() {
		showProgress();
		waitLoc = true;
		if(mLocClient == null) {
			// 定位初始化
			mLocClient = new LocationClient(getActivity());
			mLocClient.registerLocationListener(new MyLocationListenner());
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
		}
		mLocClient.start();
	}
	
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public synchronized void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			if(waitLoc){
				waitLoc = false;
				TransferService.getInstance().uploadGps(defaultHttpTransfer
						, location.getLatitude()+"", location.getLongitude()+""
						, DvDateUtils.getCurrentDate("yyyyMMddhhmmss"));
			}
			mLocClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
}
