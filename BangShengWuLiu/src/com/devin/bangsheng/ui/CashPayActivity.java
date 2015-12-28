package com.devin.bangsheng.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.service.YsTransferService;
import com.devin.bangsheng.trans.yinshi.msg.impl.CashQueryResponse;
import com.devin.bangsheng.util.PosTraceGenerator;
import com.devin.framework.util.DvBankUtils;
import com.devin.framework.util.DvDateUtils;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;

public class CashPayActivity extends BaseActivity{
	
	private TextView amountTv;
	private Button confirmBtn;
	private Double amount = 0.00;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_pay);
		initTopbar("现金缴款");
		initViews();
		queryCash();
	}

	private void initViews() {
		amountTv = (TextView) findViewById(R.id.tv_amount);
		confirmBtn = (Button) findViewById(R.id.btn_confirm);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(amount <= 0) {
					UIHelper.showToast(context, "缴款金额不能为0");
					return;
				}
				Intent intent = new Intent(context, CashPayTransActivity.class);
				intent.putExtra(Const.ExtraConst.AMOUNT, amount);
				startActivity(intent);
			}
		});
	}
	
	private void queryCash() {
		showProgress();
		String reqTime = DvDateUtils.getCurrentDate("yyyyMMddhhmmss");
		String transType = "02";
		String wlTrace = "";
		String systrace = PosTraceGenerator.getInstance().generateTrace();
		String startDate = DvDateUtils.getCurrentDate("yyyyMMdd");
		String endDate = DvDateUtils.getCurrentDate("yyyyMMdd");
		String employNo = currUser.getEmployNo();
		String termId = "";
		YsTransferService.getInstance().queryCash(defaultSocketTransfer, employNo
				, "", termId, reqTime, transType, wlTrace
				, systrace, startDate, endDate);
	}

	@Override
	public void onResponse(Message msg) {
		CashQueryResponse response = (CashQueryResponse) msg.obj;
		if("00".equals(response.getRespCode())) {
			String amountStr = response.getAmount();
			if(DvStrUtils.isEmpty(amountStr)) amountStr = "0.00";
			amount = Double.parseDouble(amountStr);
			amountTv.setText(DvBankUtils.formatAmount(amountStr));
		}else {
			UIHelper.showDialog(context, "现金缴存查询失败", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}
	}
}
