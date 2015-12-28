package com.devin.bangsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.trans.wuliu.msg.impl.SettleResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.SettleResp.SetleRespWlBody;

public class AccountActivity extends BaseActivity{
	
	private TextView cardAmountTv, cashAmountTv;
	private TextView orderTotalTv, orderReceiptTv;
	private RelativeLayout orderTotalLayout, orderReceiptLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		initTopbar("结算汇总");
		initViews();
		querySettleInfo();
	}

	private void initViews() {
		cardAmountTv = (TextView) findViewById(R.id.tv_total_card_pay);
		cashAmountTv = (TextView) findViewById(R.id.tv_total_cash_pay);
		orderTotalTv = (TextView) findViewById(R.id.tv_total);
		orderReceiptTv = (TextView) findViewById(R.id.tv_total2);
		
		orderTotalLayout = (RelativeLayout) findViewById(R.id.rl_total_order);
		orderTotalLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, OrderListActivity.class);
				intent.putExtra("dataType", 2);
				startActivity(intent);
			}
		});
		orderReceiptLayout = (RelativeLayout) findViewById(R.id.rl_receipt_order);
		orderReceiptLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, OrderListActivity.class);
				intent.putExtra("dataType", 3);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResponse(Message msg) {
		SettleResp resp = (SettleResp) msg.obj;
		if(resp.header.success()) {
			SetleRespWlBody body = (SetleRespWlBody) resp.body;
			cardAmountTv.setText(body.getCardpayamount() + " 元");
			cashAmountTv.setText(body.getCashamount() + " 元");
			orderTotalTv.setText(body.getOrdercnt());
			orderReceiptTv.setText(body.getTuotoucnt());
		}else {
			showDialogEx(resp.header.getResponseMsg());
		}
	}
	
	private void querySettleInfo() {
		showProgress();
		mService.settle(defaultHttpTransfer);
	}
}
