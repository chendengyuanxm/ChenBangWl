package com.devin.bangsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.trans.wuliu.msg.impl.BadOrderSignResp;

public class BadOrderSignActivity extends BaseActivity{
	
	private TextView orderNoTv;
	private Spinner exCodeSp;
	private Spinner exTypeSp;
	private Spinner exLevelSp;
	private EditText exDescEt;
	private Button confirmBtn;
	private ArrayAdapter<String> exCodeAdapter;
	private ArrayAdapter<String> exTypeAdapter;
	private ArrayAdapter<String> exLevelAdapter;
	
	static String[] exCodes2 = new String[]{
		  "1 客户更改派送时间"
		, "2 客户无法联络"
		, "3 客户要求改地址，需中转"
		, "4 天气原因"
		, "5 转寄他处或地址错误"
		, "6 发错货需中转"
		, "7 人力或设备不足"};
	static String[] exCodes1 = new String[]{
		  "13 商家发错货"
		, "14 少发货，多发货"
		, "15 电话问题"
		, "16 供货商拦截订单"
		, "17 超出我司配送范围"
		, "18 未开箱，客户自愿拒收"
		, "19 验货后不满意 拒收"
		, "21 上门取退单与实物不符"
	};
	static String[] exCodes = exCodes1;
	static String[] exTypes = new String[]{"01 问题件", "02 滞留件"};
	static String[] exLevels = new String[]{"0 紧急", "1 不紧急"};
	
	private String orderNo;
	private String exType;
	private String exCode;
	private String exLevel;
	private String exDesc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bad_order_sign);
		initTopbar("问题件登记");
		orderNo = getIntent().getStringExtra(Const.ExtraConst.ORDER_NO);
		initViews();
	}
	
	private void initViews() {
		orderNoTv = (TextView) findViewById(R.id.tv_orderNo);
		exDescEt = (EditText) findViewById(R.id.et_desc);
		orderNoTv.setText(orderNo);
		
		confirmBtn = (Button) findViewById(R.id.btn_confirm);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				badOrderSign();
			}
		});
		
		exCodeSp = (Spinner) findViewById(R.id.sp_code);
		exCodeAdapter = new ArrayAdapter<String>(context, R.layout.l_spinner, exCodes);
		exCodeSp.setAdapter(exCodeAdapter);
		exCodeSp.setOnItemSelectedListener(new ExCodeItemSelectedListener());
		
		exTypeSp = (Spinner) findViewById(R.id.sp_type);
		exTypeAdapter = new ArrayAdapter<String>(context, R.layout.l_spinner, exTypes);
		exTypeSp.setAdapter(exTypeAdapter);
		exTypeSp.setOnItemSelectedListener(new ExTypeItemSelectedListener());
		
		exLevelSp = (Spinner) findViewById(R.id.sp_level);
		exLevelAdapter = new ArrayAdapter<String>(context, R.layout.l_spinner, exLevels);
		exLevelSp.setAdapter(exLevelAdapter);
		exLevelSp.setOnItemSelectedListener(new ExLevelItemSelectedListener());
	}
	
	private class ExCodeItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			exCode = exCodes[position].split(" ")[0];
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
		
	}
	
	private class ExTypeItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			exType = exTypes[position].split(" ")[0];
			if(position == 0) {
				exCodes = exCodes1;
			}else {
				exCodes = exCodes2;
			}
			exCodeAdapter = new ArrayAdapter<String>(context, R.layout.l_spinner, exCodes);
			exCodeSp.setAdapter(exCodeAdapter);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
		
	}
	
	private class ExLevelItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			exLevel = exLevels[position].split(" ")[0];
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
		
	}

	@Override
	public void onResponse(Message msg) {
		BadOrderSignResp resp = (BadOrderSignResp) msg.obj;
		if(resp.header.success()) {
			Intent intent = new Intent(this, BadOrderSignSuccessActivity.class);
			startActivity(intent);
		}else {
			showDialogEx(resp.header.getResponseMsg());
		}
	}
	
	private void badOrderSign() {
		showProgress();
		exDesc = exDescEt.getText().toString().trim();
		mService.badOrderSign(defaultHttpTransfer, orderNo, exType, exCode, exDesc, exLevel);
	}
}
