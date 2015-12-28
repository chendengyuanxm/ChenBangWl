package com.devin.bangsheng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.devin.bangsheng.MainService;
import com.devin.bangsheng.R;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.enums.PayWay;
import com.devin.bangsheng.enums.TransType;
import com.devin.bangsheng.util.PosTraceGenerator;
import com.devin.framework.util.DvDateUtils;
import com.devin.framework.util.DvFileUtils;
import com.devin.framework.util.DvStrUtils;
import com.newland.mtype.util.ISOUtils;
import com.newland.sdk.spdbtrans.SpdbTransData;

public class CashPayTransActivity extends AbstractTransActivity{
	
	private SpdbTransData resultData;
	private Double amount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos_trans);
		initTopbar("现金缴存");
		transType = TransType.CONSUME;
		amount = getIntent().getDoubleExtra(Const.ExtraConst.AMOUNT, 0);
		startMposTransfer();
	}
	
	@Override
	public void startTransfer() {
		showProgress();
		deviceController.consume(this, amount);
	}
	
	/**
	 * 缴存通知
	 * 
	 * @param resultData
	 * @version 1.0
	 */
	private void cashPayNotify(SpdbTransData resultData) {
		String orderNo = "";
		String posTrace = resultData.old_voucherNo;
		String transType = "02";
		String reqTime = resultData.transDate + resultData.transTime;
		String batchNo = resultData.batchNo;
		String pan = resultData.payerAcctNo;
		String rrn = resultData.refNo;
		String systrace = PosTraceGenerator.getInstance().generateTrace();
		String mrchNo = resultData.merchantNO;
		String termId = resultData.terminalNo;
		String employNo = currUser.getEmployNo();
		String payWay = PayWay.CASH.getCode();
		String startDate = DvDateUtils.getCurrentDate("yyyyMMdd");
		String endDate = DvDateUtils.getCurrentDate("yyyyMMdd");
		MainService.getInstance().cardConsumeNotify(amount+"", batchNo, employNo, mrchNo, termId, orderNo, pan, payWay
				, posTrace, reqTime, rrn, "", "0", systrace, transType, "", startDate, endDate, 3, true);
	}
	
	/**
	 * 刷卡成功缴费通知
	 * 
	 * @param resultData
	 * @param jbgData
	 * @version 1.0
	 */
	private void consumeByCard(SpdbTransData resultData, String jbgData) {
		String orderNo = "";
		String posTrace = resultData.old_voucherNo;
		String transType = "01";
		String reqTime = resultData.transDate + resultData.transTime;
		String batchNo = resultData.batchNo;
		String pan = resultData.payerAcctNo;
		String rrn = resultData.refNo;
		String systrace = PosTraceGenerator.getInstance().generateTrace();
		String mrchNo = resultData.merchantNO;
		String termId = resultData.terminalNo;
		String employNo = currUser.getEmployNo();
		String payWay = PayWay.CARD.getCode();
		MainService.getInstance().cardConsumeNotify(amount+"", batchNo, employNo, mrchNo, termId, orderNo, pan, payWay
				, posTrace, reqTime, rrn, "", "0", systrace, transType, jbgData, "", "", 3, true);
	}
	
	@Override
	public void onSucc(SpdbTransData resultData) {
		super.onSucc(resultData);
		this.resultData = resultData;
		if(transType == TransType.parseTransTypeFromCode(resultData.transType) 
				&& !DvStrUtils.isEmpty(resultData.refNo)) {
			Intent intent = new Intent(context, SignatureActivity.class);
			startActivityForResult(intent, 2);
		}
	}
	
	@Override
	public void onFail(String code, String detail, SpdbTransData resultData) {
		super.onFail(code, detail, resultData);
		if(detail != null && detail.contains("取消")) {
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			String jbgPath = data.getStringExtra("jbgPath");
			byte[] jbgData = DvFileUtils.getByteArrayFromSD(jbgPath);
			consumeByCard(resultData, ISOUtils.hexString(jbgData));
			cashPayNotify(resultData);
			Intent intent = new Intent(context, CashPaySuccessActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
//	@Override
//	public void onEventMainThread(Message msg) {		
//		if(msg.what == MsgType.CARD_CONSUME_NOTIFY.getId()) {	//由于刷卡缴费通知采取后台上送模式，这里重写
//			if(msg.obj instanceof ConsumeNotifyResponse) {
//				ConsumeNotifyResponse resp = (ConsumeNotifyResponse) msg.obj;
//				final String respCode = resp.getRespCode();
//				if("00".equals(respCode)) {
//					String sysTrace = resp.getSystrace();
//					String transType = resp.getTransType();
//					PayDB.getInstance(context).delete(sysTrace);	//刷卡缴费通知成功后删除保存记录
//					if("01".equals(transType)) {
//						logger.info("刷卡缴费成功，成功流水号[" + sysTrace + "]");
//					}else {
//						logger.info("刷卡缴存成功，成功流水号[" + sysTrace + "]");
//					}
//				}else {
//					logger.info("刷卡缴存通知失败[" + respCode + "]");
//				}
//			}else if(msg.obj instanceof CommonException){
//				CommonException error = (CommonException) msg.obj;
//				logger.info("现金缴存通知失败[" + error.getLocalizedMessage() + "]");
//			}
//		}else {
//			super.onEventMainThread(msg);
//		}
//	}

	@Override
	public void onResponse(Message msg) {
	}
}
