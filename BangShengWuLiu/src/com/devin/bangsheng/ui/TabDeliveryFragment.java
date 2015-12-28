package com.devin.bangsheng.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.allthelucky.common.view.ImageIndicatorView;
import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.ImageGridAdapter;
import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.service.TransferService;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderInfoResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderInfoResp.QueryOrderInfoRespWlBody;
import com.devin.bangsheng.view.IconEditText;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;
import com.zxing.activity.CaptureActivity;

public class TabDeliveryFragment extends BaseFragment implements OnItemClickListener, OnClickListener{
	
	private static final int REQ_SCAN = 0x1001;
	private ImageIndicatorView mImageIndicatorView;
	private IconEditText orderNoIet;
	private String orderNo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rView = inflater.inflate(R.layout.activity_tab_delivery, container, false);
		initTopbar("首页", false);
		initViews();
		initFunctions();
		return rView;
	}
	
	private void initViews() {
		mImageIndicatorView = (ImageIndicatorView) rView.findViewById(R.id.indicate_view);
		final Integer[] resArray = new Integer[] { R.drawable.home_1, R.drawable.home_2, R.drawable.home_3 };
		mImageIndicatorView.setupLayoutByDrawable(resArray);
		mImageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_ARROW_ROUND_STYLE);
		mImageIndicatorView.show();
		
		Button scanBtn = (Button) rView.findViewById(R.id.btn_scan);
		scanBtn.setOnClickListener(this);
		rView.findViewById(R.id.tv_hidden).requestFocus();
		orderNoIet = (IconEditText) rView.findViewById(R.id.iet_orderNo);
		orderNoIet.getEditText().setImeOptions(EditorInfo.IME_ACTION_SEND);
		orderNoIet.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEND){
					orderNo = orderNoIet.getText().toString().trim();
					queryOrderInfo();
					return true;
				}
				return false;
			}
		});
	}
	
	private void initFunctions() {
		GridView gv = (GridView) rView.findViewById(R.id.gv);
		TypedArray ta = getResources().obtainTypedArray(R.array.home_function_icons);
		Drawable[] drawables_tool = new Drawable[ta.length()];
		for (int i = 0; i < drawables_tool.length; i++) {
			drawables_tool[i] = ta.getDrawable(i);
		}
		String[] texts_tool = getResources().getStringArray(R.array.home_function_labels);
		ta.recycle();
		ImageGridAdapter adapter = new ImageGridAdapter(getActivity(), drawables_tool, texts_tool);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(this);
	}

	@Override
	public void onResponse(Message msg) {
		QueryOrderInfoResp resp = (QueryOrderInfoResp) msg.obj;
		if(resp.header.success()) {
			QueryOrderInfoRespWlBody body = (QueryOrderInfoRespWlBody) resp.body;
			ArrayList<Order> orders = (ArrayList<Order>) body.getOrders();
			if(orders.size() == 1) {
				Order order = orders.get(0);
				Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
				intent.putExtra(Const.ExtraConst.ORDER, order);
				startActivity(intent);
			}else {
				Intent intent = new Intent(getActivity(), BatchOrdersActivity.class);
				intent.putExtra(Const.ExtraConst.ORDERS, orders);
				startActivity(intent);
			}
		}else {
			showDialogEx(resp.header.getResponseMsg());
		}
	}
	
	private void queryOrderInfo() {
		if(!DvStrUtils.isEmpty(orderNo)) {
			showProgress();
			TransferService.getInstance().queryOrderInfo(defaultHttpTransfer, orderNo);
		}else {
			UIHelper.showToast(getActivity(), "订单号不能为空");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {
		case 0:		//任务列表
			intent = new Intent(getActivity(), OrderListActivity.class);
			intent.putExtra("dataType", 1);
			startActivity(intent);
			break;

		case 1:		//派件签收
			intent = new Intent(getActivity(), OrderQueryActivity.class);
			startActivity(intent);
			break;
			
		case 2:		//配送查询
			intent = new Intent(getActivity(), OrderStatusActivity.class);
			startActivity(intent);
			break;
			
		case 3:		//现金缴款
			intent = new Intent(getActivity(), CashPayActivity.class);
			startActivity(intent);
			break;
			
		case 4:		//结算汇总
			intent = new Intent(getActivity(), AccountActivity.class);
			startActivity(intent);
			break;
			
		case 5:	    //pos管理
			intent = new Intent(getActivity(), PosManagerActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), CaptureActivity.class);
		startActivityForResult(intent, REQ_SCAN);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQ_SCAN && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			if(bundle != null) {
				ArrayList<String> orders = (ArrayList<String>) bundle.getSerializable("orders");
				if(orders != null) {
					String orderStr = "";
					for(int i = 0; i < orders.size()-1; i ++) {
						orderStr += orders.get(i) + ",";
					}
					orderStr += orders.get(orders.size()-1);
					orderNoIet.setText(orderStr);
					orderNoIet.getEditText().setSelection(orderStr.length());
					orderNo = orderStr;
					queryOrderInfo();
				}
			}
		}
	}
	
}
