package com.devin.bangsheng.ui;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;

public class UserInfoActivity extends BaseActivity{
	
	private TextView userNameTv;
	private TextView employNoTv;
	private TextView mobileTv;
	private TextView addressTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		initTopbar("个人中心");
		initViews();
	}

	private void initViews() {
		userNameTv = (TextView) findViewById(R.id.tv_userName);
		employNoTv = (TextView) findViewById(R.id.tv_id);
		mobileTv = (TextView) findViewById(R.id.tv_phoneNumber);
		addressTv = (TextView) findViewById(R.id.tv_address);
		userNameTv.setText(currUser.getEmployname());
		employNoTv.setText(currUser.getEmployNo());
		mobileTv.setText(currUser.getMobile());
		addressTv.setText(currUser.getNetname());
	}

	@Override
	public void onResponse(Message msg) {
		
	}

}
