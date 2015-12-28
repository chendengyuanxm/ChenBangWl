package com.devin.bangsheng.device;

import com.devin.bangsheng.BaseApplication;
import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.common.CommonException;
import com.devin.bangsheng.util.SocketUtils;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.newland.mispos.api.NewlandSpdbTrans;
import com.newland.sdk.spdbtrans.ISpdbCommnunication;
import com.newland.sdk.spdbtrans.ISpdbTrans;
import com.newland.sdk.spdbtrans.ISpdbTransResult;
import com.newland.sdk.spdbtrans.SpdbTransData;

public class ME3xDeviceControllerImpl implements ME3xDeviceController{
	
	Logger logger = LoggerFactory.getLogger(ME3xDeviceControllerImpl.class);
	public static ME3xDeviceControllerImpl instance;
	public NewlandSpdbTrans controller;
	private Device mConnectedDevice = null;
	public ISpdbTrans mTrans;
	
	public static ME3xDeviceControllerImpl getInstance() {
		if(instance == null) {
			instance = new ME3xDeviceControllerImpl();
		}
		
		return instance;
	}
	
	private ME3xDeviceControllerImpl() {
		controller = new NewlandSpdbTrans();
		controller.init(BaseApplication.getInstance());
		controller.setCommnucation(communication);
	}
	
	ISpdbCommnunication communication = new ISpdbCommnunication() {
		
//		@Override
//		public byte[] exchangeDataWithService(byte[] devData) throws Exception {
//			logger.debug("POS下发报文：" + com.newland.mtype.util.ISOUtils.hexString(devData));
//			int posLen = devData.length;
//			int len = 2+posLen;
//			byte[] data = new byte[len];
//			int offset = 0;
//			byte[] lenbs = com.newland.mtype.util.ISOUtils.intToBytes(len, 2, true);
//			System.arraycopy(lenbs, 0, data, offset, lenbs.length);
//			offset += lenbs.length;
//			System.arraycopy(devData, 0, data, offset, devData.length);
//			offset += devData.length;
//			logger.debug("POS发送到前置报文：" + com.newland.mtype.util.ISOUtils.hexString(data));
//			byte[] resp = SocketUtils.send(data);
//			logger.debug("POS前置返回报文：" + com.newland.mtype.util.ISOUtils.hexString(resp));
//			
//			
////			byte[] resp = SocketUtils.send1(devData);
////			logger.debug("POS前置返回报文：" + com.newland.mtype.util.ISOUtils.hexString(resp));
//			return resp;
//		}

		@Override
		public byte[] exchangeDataWithService(byte[] devData, SpdbTransData arg1) throws Exception{
			logger.debug("POS下发报文：" + com.newland.mtype.util.ISOUtils.hexString(devData));
//			int posLen = devData.length;
//			int len = 2+posLen;
//			byte[] data = new byte[len];
//			int offset = 0;
//			byte[] lenbs = com.newland.mtype.util.ISOUtils.intToBytes(len, 2, true);
//			System.arraycopy(lenbs, 0, data, offset, lenbs.length);
//			offset += lenbs.length;
//			System.arraycopy(devData, 0, data, offset, devData.length);
//			offset += devData.length;
//			logger.debug("POS发送到前置报文：" + com.newland.mtype.util.ISOUtils.hexString(data));
//			byte[] resp = null;
//			try{
//				resp = SocketUtils.send(data);
//				logger.debug("POS前置返回报文：" + com.newland.mtype.util.ISOUtils.hexString(resp));
//			}catch(Exception e) {
//				logger.error("POS前置返回报文失败！", e);
//			}
			
			byte[] resp = null;
			try{
				resp = SocketUtils.send1(devData);
				logger.debug("POS前置返回报文：" + com.newland.mtype.util.ISOUtils.hexString(resp));
			}catch(Exception e) {
				logger.error("POS前置返回报文失败！", e);
			}
			return resp;
		}
	};
	
	@Override
	public void connect(Device device) throws CommonException {
		if(!controller.isConnected()) {
			controller.openDevice(device.getMacAddress());
			if(!controller.isConnected()) {
				throw new CommonException("设备连接失败");
			}
			mConnectedDevice = device;
		}
	}
	
	@Override
	public void destroy() {
		if(controller.isConnected()) {
			controller.closeDevice();
			mConnectedDevice = null;
		}
	}

	@Override
	public Device getConnectedDevice() {
		if(!controller.isConnected()) {
			mConnectedDevice = null;
		}
		return mConnectedDevice;
	}

	@Override
	public void signUp(ISpdbTransResult rslt) {
		controller.loginTran(rslt);
	}

	@Override
	public void signDown(ISpdbTransResult rslt) {
		controller.checkOutTran(rslt);
	}

	@Override
	public void consume(ISpdbTransResult rslt, double amount) {
		controller.consumeTran(rslt, amount);
	}

	@Override
	public void queryBalance(ISpdbTransResult rslt) {
		controller.balanceTran(rslt);
	}
	
	@Override
	public void cancelTrans(ISpdbTransResult rslt) {
//		controller.cancelTran(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public void settle(ISpdbTransResult rslt) {
		controller.settleTran (rslt);
	}

	@Override
	public void endTrade(boolean succ, boolean hasBeenSend) {
		if(controller.isConnected()) {
			controller.init(BaseApplication.getInstance());
		}
	}
	
	@Override
	public void printLastTrans(ISpdbTransResult rslt) {
		controller.printLastTran(rslt);
	}

	@Override
	public void lastTranQuery(ISpdbTransResult resultListener) {
		controller.lastTranQuery(resultListener);
	}

	@Override
	public void updateFirmware(String filePath, ISpdbTransResult resultListener) {
		controller.updateFirmware(filePath, resultListener);
	}

}
