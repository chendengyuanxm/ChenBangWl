package com.devin.bangsheng.ui;

import android.os.Bundle;

import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.device.DeviceConnectExecutor;
import com.devin.bangsheng.device.DeviceConnectExecutor.OnConnectDeviceListener;
import com.devin.bangsheng.device.ME3xDeviceController;
import com.devin.bangsheng.device.ME3xDeviceControllerImpl;
import com.devin.bangsheng.enums.TransType;
import com.devin.framework.util.DvStrUtils;
import com.newland.sdk.spdbtrans.ISpdbTransResult;
import com.newland.sdk.spdbtrans.SpdbTransData;

public abstract class AbstractTransActivity extends BaseActivity implements ISpdbTransResult{
	
	protected TransType transType;
	protected ME3xDeviceController deviceController = ME3xDeviceControllerImpl.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void startMposTransfer() {
		if(deviceController.getConnectedDevice() != null) {
			startTransfer();
		}else {
			DeviceConnectExecutor deviceConnectExecutor = new DeviceConnectExecutor(this);
			deviceConnectExecutor.setConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void onDeviceConnected(Device device) {
					startTransfer();
				}
				
				@Override
				public void onCancel() {
					finish();
				}
			});
			deviceConnectExecutor.performConnect();
		}
	}
	
	private String rString(SpdbTransData resultData) {
		StringBuilder sb = new StringBuilder();
		sb.append("==============================================\n");
		sb.append("acq_bank_no:" + resultData.acq_bank_no + "\n");
		sb.append("amount:" + resultData.amount + "\n");
		sb.append("authNo:" + resultData.authNo + "\n");
		sb.append("batchNo:" + resultData.batchNo + "\n");
		sb.append("eleAmount:" + resultData.eleAmount + "\n");
		sb.append("ic_card_data:" + resultData.ic_card_data + "\n");
		sb.append("iss_bank_no:" + resultData.iss_bank_no + "\n");
		sb.append("merchantName:" + resultData.merchantName + "\n");
		sb.append("merchantNO:" + resultData.merchantNO + "\n");
		sb.append("old_batchNo:" + resultData.old_batchNo + "\n");
		sb.append("old_merchantNO:" + resultData.old_merchantNO + "\n");
		sb.append("old_refNo:" + resultData.old_refNo + "\n");
		sb.append("old_terminalNo:" + resultData.old_terminalNo + "\n");
		sb.append("old_transDate:" + resultData.old_transDate + "\n");
		sb.append("old_voucherNo:" + resultData.old_voucherNo + "\n");
		sb.append("payeeAcctNo:" + resultData.payeeAcctNo + "\n");
		sb.append("payerAcctNo:" + resultData.payerAcctNo + "\n");
		sb.append("printRuslt:" + resultData.printRuslt + "\n");
		sb.append("refNo:" + resultData.refNo + "\n");
		sb.append("resCode:" + resultData.resCode + "\n");
		sb.append("signData:" + resultData.signData + "\n");
		sb.append("sn:" + resultData.sn + "\n");
		sb.append("terminalNo:" + resultData.terminalNo + "\n");
		sb.append("termType:" + resultData.termType + "\n");
		sb.append("terVersion:" + resultData.terVersion + "\n");
		sb.append("traceNo:" + resultData.traceNo + "\n");
		sb.append("trans_result_desc:" + resultData.trans_result_desc + "\n");
		sb.append("transDate:" + resultData.transDate + "\n");
		sb.append("transTime:" + resultData.transTime + "\n");
		sb.append("transType:" + resultData.transType + "\n");
		sb.append("==============================================\n");
		
		return sb.toString();
	}
	
	@Override
	public void onSucc(SpdbTransData resultData) {
		logger.debug("onSucc:\n" + rString(resultData));
		if(!DvStrUtils.isEmpty(resultData.refNo)){
			dismissProgress();
//			UIHelper.showToast(context, "消费成功");
		}
	}
	
	@Override
	public void onProgress(String code, String detail, SpdbTransData resultData) {
		logger.debug("onProgress:" + "[" + code + "," + detail + "]\n" + rString(resultData));
	}
	
	@Override
	public void onFail(String code, String detail, SpdbTransData resultData) {
		logger.debug("onFail:" + TransType.getTransName(resultData.transType)+"[" + code + "," + detail + "]\n" + rString(resultData));
		dismissProgress();
	}
	
	public abstract void startTransfer();
}
