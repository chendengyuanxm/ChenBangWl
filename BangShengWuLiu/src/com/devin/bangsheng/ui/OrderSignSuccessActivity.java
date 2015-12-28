package com.devin.bangsheng.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.OrderResultAdpater;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderResult;

public class OrderSignSuccessActivity extends BaseActivity{
	
	private ArrayList<OrderResult> orderRslts = new ArrayList<OrderResult>();
	private ListView mListView;
	private OrderResultAdpater mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bad_order_sign_succ);
		initTopbar("订单签收", false);
		orderRslts = (ArrayList<OrderResult>) getIntent().getSerializableExtra(Const.ExtraConst.ORDER_RSLTS);
		initViews();
	}
	
	private void initViews() {
		mListView = (ListView) findViewById(R.id.lv);
		mAdapter = new OrderResultAdpater(this, orderRslts);
		mListView.setAdapter(mAdapter);
		Button confirmBtn = (Button) findViewById(R.id.btn_confirm);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backHomePage();
			}
		});
	}

	@Override
	public void onResponse(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
	}
}
