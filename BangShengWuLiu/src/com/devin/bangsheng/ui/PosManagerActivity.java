package com.devin.bangsheng.ui;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.SettingListAdapter;
import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.device.DeviceConnectExecutor;
import com.devin.bangsheng.device.DeviceConnectExecutor.OnConnectDeviceListener;
import com.devin.bangsheng.device.ME3xDeviceController;
import com.devin.bangsheng.device.ME3xDeviceControllerImpl;
import com.devin.framework.util.UIHelper;
import com.newland.mispos.api.TransCode;
import com.newland.sdk.spdbtrans.ISpdbTransResult;
import com.newland.sdk.spdbtrans.SpdbTransData;

public class PosManagerActivity extends BaseActivity implements OnItemClickListener, ISpdbTransResult{
	
	private ME3xDeviceController deviceController = ME3xDeviceControllerImpl.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos_manager);
		initTopbar("pos管理");
		initViews();
	}

	private void initViews() {
		TypedArray ta = getResources().obtainTypedArray(R.array.pos_manager_icons);
		Drawable[] icons = new Drawable[ta.length()];
		for (int i = 0; i < icons.length; i++) {
			icons[i] = ta.getDrawable(i);
		}
		ta.recycle();
		String[] labels = getResources().getStringArray(R.array.pos_manager_labels);
		ListView lv = (ListView) findViewById(R.id.lv);
		SettingListAdapter adapter = new SettingListAdapter(this, labels, icons);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onResponse(Message msg) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			Intent intent = new Intent(this, BalanceQueryActivity.class);
			startActivity(intent);
			break;

		case 1:
			signup(); 
			break;
			
		case 2:
			print();
			break;
			
		case 3:
			settle();
			break;
			
		case 4:
			cancelTrans();
			break;
			
		case 5:
			lastTransQuery();
			break;
			
		case 6:
			updateFirmware();
			break;
		}
	}
	
	private void signup() {
		if(deviceController.getConnectedDevice() == null) {
			DeviceConnectExecutor deviceConnectExecutor = new DeviceConnectExecutor(this);
			deviceConnectExecutor.setConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void onDeviceConnected(Device device) {
					signup();
				}
				
				@Override
				public void onCancel() {
					finish();
				}
			});
			deviceConnectExecutor.performConnect();
			return;
		}
		
		showProgress();
		deviceController.signUp(this);
	}
	
	private void print() {
		if(deviceController.getConnectedDevice() == null) {
			DeviceConnectExecutor deviceConnectExecutor = new DeviceConnectExecutor(this);
			deviceConnectExecutor.setConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void onDeviceConnected(Device device) {
					print();
				}
				
				@Override
				public void onCancel() {
					finish();
				}
			});
			deviceConnectExecutor.performConnect();
			return;
		}
		
		showProgress();
		deviceController.printLastTrans(this);
	}
	
	private void settle() {
		if(deviceController.getConnectedDevice() == null) {
			DeviceConnectExecutor deviceConnectExecutor = new DeviceConnectExecutor(this);
			deviceConnectExecutor.setConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void onDeviceConnected(Device device) {
					settle();
				}
				
				@Override
				public void onCancel() {
					finish();
				}
			});
			deviceConnectExecutor.performConnect();
			return;
		}
		
		showProgress();
		deviceController.settle(this);
	}
	
	private void cancelTrans() {
		if(deviceController.getConnectedDevice() == null) {
			DeviceConnectExecutor deviceConnectExecutor = new DeviceConnectExecutor(this);
			deviceConnectExecutor.setConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void onDeviceConnected(Device device) {
					cancelTrans();
				}
				
				@Override
				public void onCancel() {
					finish();
				}
			});
			deviceConnectExecutor.performConnect();
			return;
		}
		
		showProgress();
		deviceController.cancelTrans(this);
	}
	
	private void lastTransQuery() {
		if(deviceController.getConnectedDevice() == null) {
			DeviceConnectExecutor deviceConnectExecutor = new DeviceConnectExecutor(this);
			deviceConnectExecutor.setConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void onDeviceConnected(Device device) {
					lastTransQuery();
				}
				
				@Override
				public void onCancel() {
					finish();
				}
			});
			deviceConnectExecutor.performConnect();
			return;
		}
		
		showProgress();
		deviceController.lastTranQuery(this);
	}
	
	private void updateFirmware() {
		if(deviceController.getConnectedDevice() == null) {
			DeviceConnectExecutor deviceConnectExecutor = new DeviceConnectExecutor(this);
			deviceConnectExecutor.setConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void onDeviceConnected(Device device) {
					updateFirmware();
				}
				
				@Override
				public void onCancel() {
					finish();
				}
			});
			deviceConnectExecutor.performConnect();
			return;
		}
		
		showProgress();
		deviceController.signDown(this);
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
	public void onFail(final String code, final String detail, final SpdbTransData resultData) {
		logger.debug("onFail:" + TransCode.getTransName(Integer.parseInt(resultData.transType))+"[" + code + "," + detail + "]\n" + rString(resultData));
		dismissProgress();
		UIHelper.showToast(context, TransCode.getTransName(Integer.parseInt(resultData.transType))+"失败[" + code + "," + detail + "]");
		
	}

	@Override
	public void onProgress(String code, String detail, SpdbTransData resultData) {
		logger.debug("onProgress:" + "[" + code + "," + detail + "]\n" + rString(resultData));
	}

	@Override
	public void onSucc(final SpdbTransData resultData) {
		logger.debug("onSucc:\n" + rString(resultData));
		dismissProgress();
		UIHelper.showToast(context, "成功");
	}
}
