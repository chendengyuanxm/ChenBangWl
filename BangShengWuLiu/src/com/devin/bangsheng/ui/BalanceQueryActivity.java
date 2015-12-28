package com.devin.bangsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.enums.TransType;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;
import com.newland.sdk.spdbtrans.SpdbTransData;

public class BalanceQueryActivity extends AbstractTransActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos_trans);
		initTopbar("余额查询");
		transType = TransType.BALANCE;
		initViews();
		startMposTransfer();
	}

	private void initViews() {
	}
	
	@Override
	public void onResponse(Message msg) {
		
	}

	@Override
	public void startTransfer() {
		showProgress();
		deviceController.queryBalance(this);
	}
	
	@Override
	public void onFail(String code, String detail, SpdbTransData resultData) {
		super.onFail(code, detail, resultData);
		if(detail != null && detail.contains("取消")) {
			UIHelper.showToast(context, detail);
			finish();
		}else if(detail != null && detail.contains("超时")){
			UIHelper.showToast(context, detail);
			finish();
		}else {
			Intent intent = new Intent(context, OrderSignFailActivity.class);
			intent.putExtra(Const.ExtraConst.ERRCODE, code);
			intent.putExtra(Const.ExtraConst.ERRMSG, detail);
			intent.putExtra(Const.ExtraConst.TRANS_TYPE, transType);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onSucc(SpdbTransData resultData) {
		super.onSucc(resultData);
		if(transType == TransType.parseTransTypeFromCode(resultData.transType)
				&& !DvStrUtils.isEmpty(resultData.refNo)){
			Intent intent = new Intent(context, BalanceQueryResultActivity.class);
			intent.putExtra(Const.ExtraConst.CARD_NO, resultData.payerAcctNo);
			intent.putExtra(Const.ExtraConst.BALANCE, resultData.amount);
			startActivity(intent);
		}
	}
}
