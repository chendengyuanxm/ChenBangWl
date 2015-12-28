package com.devin.bangsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.view.InputPanel;
import com.devin.framework.util.UIHelper;

public class CashPayActivityGone extends BaseActivity{
	
	private TextView amountTv;
	private InputPanel mInputPanel;
	private StringBuilder sb_num = new StringBuilder();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_pay_gone);
		initTopbar("现金缴款");
		initViews();
	}
	
	private void initViews() {
		amountTv = (TextView) findViewById(R.id.tv_amount);
		mInputPanel = (InputPanel) findViewById(R.id.inputPanel);
		mInputPanel.setKeyListener(new InputPanel.OnKeyClickListener() {
			
			@Override
			public void onOk() {
				if(!"".equals(sb_num.toString())) {
					nexStep();
				}else {
					UIHelper.showToast(CashPayActivityGone.this, "金额必须大于0");
				}
			}
			
			@Override
			public void onKeyDown(String key) {
				if(sb_num.length() < 12) {
					sb_num.append(key);
					update();
				}
			}
			
			@Override
			public void onBack() {
				if (!"".equals(sb_num.toString())) {
					sb_num.deleteCharAt(sb_num.length() - 1);
					update();
				}
			}
		});
	}
	
	private void update() {
		if (sb_num.length() == 0) {
			amountTv.setText("0.00");
		} else {
			long num = Long.parseLong(sb_num.toString());
			amountTv.setText(num/100 + "." + (num%100 < 10 ? ("0" + num%100) : num%100));
		}
	}

	@Override
	public void onResponse(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
	private void nexStep() {
		Intent intent = new Intent(this, BalanceQueryActivity.class);
		intent.putExtra("transAmount", amountTv.getText().toString());
		startActivity(intent);
	}
}
