package com.devin.bangsheng.ui;

import com.devin.bangsheng.R;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CashPaySuccessActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_pay_success);
		initTopbar("现金缴款");
		initViews();
	}

	private void initViews() {
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
