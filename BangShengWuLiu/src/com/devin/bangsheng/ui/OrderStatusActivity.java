package com.devin.bangsheng.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderInfoResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderInfoResp.QueryOrderInfoRespWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderStatusResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderStatusResp.QueryOrderStatusRespWlBody;
import com.devin.bangsheng.view.IconEditText;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;
import com.zxing.activity.CaptureActivity;

public class OrderStatusActivity extends BaseActivity{
	
	private static final int REQ_SCAN = 0x1001;
	
	private IconEditText orderNoIet;
	private Button confirmBtn;
	private Button scanBtn;
	
	private String orderNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_receive);
		initTopbar("配送查询");
		initViews();
	}

	private void initViews() {
		orderNoIet = (IconEditText) findViewById(R.id.iet_orderNo);
		confirmBtn = (Button) findViewById(R.id.btn_confirm);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				orderNo = orderNoIet.getText().toString().trim();
				try {
					validate();
					queryOrderStatus();
				} catch (Exception e) {
					UIHelper.showToast(context, e.getLocalizedMessage());
				}
			}
		});
		scanBtn = (Button) findViewById(R.id.btn_scan);
		scanBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CaptureActivity.class);
				startActivityForResult(intent, REQ_SCAN);
			}
		});
		orderNoIet.getEditText().setImeOptions(EditorInfo.IME_ACTION_SEND);
		orderNoIet.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEND){
					orderNo = orderNoIet.getText().toString().trim();
					try {
						validate();
						queryOrderStatus();
					} catch (Exception e) {
						UIHelper.showToast(context, e.getLocalizedMessage());
					}
					return true;
				}
				return false;
			}
		});
		orderNoIet.setText("test070028");
	}

	@Override
	public void onResponse(Message msg) {
		QueryOrderStatusResp resp = (QueryOrderStatusResp) msg.obj;
		if(resp.header.success()) {
			QueryOrderStatusRespWlBody body = (QueryOrderStatusRespWlBody) resp.body;
			Intent intent = new Intent(this, OrderStatusResultActivity.class);
			intent.putExtra(Const.ExtraConst.ORDER_STATUS, body);
			intent.putExtra(Const.ExtraConst.ORDER_NO, orderNo);
			startActivity(intent);
		}else {
			showDialogEx(resp.header.getResponseMsg());
		}
	}
	
	private void validate() throws Exception {
		if(DvStrUtils.isEmpty(orderNo)) {
			throw new Exception("单号不能为空");
		}
	}
	
	private void queryOrderStatus() {
		showProgress();
		mService.queryOrderStatus(defaultHttpTransfer, orderNo);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQ_SCAN && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			if(bundle != null) {
				ArrayList<String> orders = (ArrayList<String>) bundle.getSerializable("orders");
				if(orders != null) {
					orderNoIet.setText(orders.get(0));
					orderNoIet.getEditText().setSelection(orders.get(0).length());
				}
			}
		}
	}
}
