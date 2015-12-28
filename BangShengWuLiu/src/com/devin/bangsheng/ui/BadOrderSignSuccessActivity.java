package com.devin.bangsheng.ui;

import com.devin.bangsheng.R;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class BadOrderSignSuccessActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bad_order_sign_succ);
		initTopbar("问题件", false);
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

}
