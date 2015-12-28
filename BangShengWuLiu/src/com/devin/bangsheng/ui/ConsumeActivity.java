package com.devin.bangsheng.ui;

import android.os.Bundle;
import android.os.Message;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;

public class ConsumeActivity extends AbstractTransActivity{
	
	private String transAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos_trans);
		initTopbar("消费");
		initViews();
		transAmount = getIntent().getStringExtra(Const.ExtraConst.AMOUNT);
		startMposTransfer();
	}

	private void initViews() {
		
	}
	
	@Override
	public void startTransfer() {
		showProgress();
		deviceController.consume(this, Double.parseDouble(transAmount));
	}

	@Override
	public void onResponse(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
}
