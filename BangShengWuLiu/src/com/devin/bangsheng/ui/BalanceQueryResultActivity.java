package com.devin.bangsheng.ui;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BalanceQueryResultActivity extends BaseActivity{
	
	private TextView cardNoTv;
	private TextView balanceTv;
	
	private String cardNo;
	private String balance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance_query_result);
		initTopbar("余额查询", false);
		cardNo = getIntent().getStringExtra(Const.ExtraConst.CARD_NO);
		balance = getIntent().getStringExtra(Const.ExtraConst.BALANCE);
		initViews();
	}

	private void initViews() {
		cardNoTv = (TextView) findViewById(R.id.tv_cardNo);
		balanceTv = (TextView) findViewById(R.id.tv_balance);
		cardNoTv.setText(cardNo);
		balanceTv.setText(balance + " 元");
		Button confirmBtn = (Button)findViewById(R.id.btn_confirm);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backHomePage();
			}
		});
	}

	@Override
	public void onResponse(Message msg) {
		
	}
	
	@Override
	public void onBackPressed() {
	}
}
