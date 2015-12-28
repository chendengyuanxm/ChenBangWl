package com.devin.bangsheng.device;

import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.common.CommonException;
import com.newland.sdk.spdbtrans.ISpdbTransResult;

public interface ME3xDeviceController {
	
	/**
	 * 连接设备
	 * <p>
	 * 
	 * @since ver1.0
	 * @throws Exception
	 */
	public void connect(Device device) throws CommonException;
	
	/**
	 * 销毁连接控制器，释放相关资源
	 * <p>
	 * 
	 * @since ver1.0
	 */
	public void destroy();

	/**
	 * 获取当前连接设备
	 * 
	 * @return
	 * @version 1.0
	 */
	public Device getConnectedDevice() ;
	
	/**
	 * 签到
	 * 
	 * @param rslt
	 * @version 1.0
	 */
	public void signUp(ISpdbTransResult rslt);
	
	/**
	 * 签退
	 * 
	 * @param rslt
	 * @version 1.0
	 */
	public void signDown(ISpdbTransResult rslt);
	
	/**
	 * 消费
	 * 
	 * @param rslt
	 * @param amount
	 * @version 1.0
	 */
	public void consume(ISpdbTransResult rslt, double amount);
	
	/**
	 * 查询余额
	 * 
	 * @param rslt
	 * @version 1.0
	 */
	public void queryBalance(ISpdbTransResult rslt);
	
	/**
	 * 结算
	 * 
	 * @param rslt
	 * @version 1.0
	 */
	public void settle(ISpdbTransResult rslt) ;
	
	/**
	 * 交易撤销
	 * 
	 * @param rslt
	 * @version 1.0
	 */
	public void cancelTrans(ISpdbTransResult rslt);
	
	/**
	 * 末笔交易打印
	 * @param resultListener
	 */
	public void printLastTrans(ISpdbTransResult rslt);
	
	/**
	 * 末笔交易查询
	 * @param resultListener
	 */
	public void lastTranQuery(ISpdbTransResult resultListener);
	
	/**
	 * 固件升级
	 * @param filePath
	 * @param resultListener
	 */
	public void updateFirmware(final String filePath, final ISpdbTransResult resultListener);
	
	public void endTrade(boolean succ, boolean hasBeenSend);

}
