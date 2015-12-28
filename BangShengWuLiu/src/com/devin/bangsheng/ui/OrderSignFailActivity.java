package com.devin.bangsheng.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.enums.TransType;

public class OrderSignFailActivity extends BaseActivity{
	
	private TextView errCodeTv;
	private TextView errMsgTv;
	private TransType transType;
	private String errCode;
	private String errMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_sign_fail);
		transType = (TransType) getIntent().getSerializableExtra(Const.ExtraConst.TRANS_TYPE);
		errCode = getIntent().getStringExtra(Const.ExtraConst.ERRCODE);
		errMsg = getIntent().getStringExtra(Const.ExtraConst.ERRMSG);
		initTopbar(transType.getName() + "失败", false);
		initViews();
	}
	
	private void initViews() {
		errCodeTv = (TextView) findViewById(R.id.tv_errCode);
		errMsgTv = (TextView) findViewById(R.id.tv_errMsg);
		errCodeTv.setText("错误码：" + errCode);
		errMsgTv.setText("错误原因：" + errMsg);
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
