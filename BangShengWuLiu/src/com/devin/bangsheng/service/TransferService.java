package com.devin.bangsheng.service;

import java.util.Date;
import java.util.List;

import com.devin.bangsheng.BaseApplication;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.enums.MsgType;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.bangsheng.trans.wuliu.msg.impl.BadOrderSignRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.BadOrderSignRequest.BadOrdSignReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.GpsUploadRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.GpsUploadRequest.GpsUploadReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderReceiveRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderReceiveRequest.OrderReceiveReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderReversalRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderReversalRequest.OrderReversalReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.Page;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryAppVerRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryAppVerRequest.QueryAppVerReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderInfoRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderInfoRequest.QueryOrderInfoReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderStatusRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderStatusRequest.QueryOrderStatusReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrdersRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrdersRequest.QueryOrdersReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.SettleRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.SettleRequest.SettleReqWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.SignOrder;
import com.devin.bangsheng.trans.wuliu.msg.impl.UserLoginRequest;
import com.devin.bangsheng.trans.wuliu.msg.impl.UserLoginRequest.UserLoginReqWlBody;
import com.devin.bangsheng.util.Md5;
import com.devin.framework.tcp.ITransfer;
import com.devin.framework.util.DvDateUtils;

public class TransferService {
	
	public static TransferService instance;
	private TransferService() {
	}
	
	public static TransferService getInstance() {
		if(instance == null) {
			instance = new TransferService();
		}
		
		return instance;
	}
	
	private WlHeader generateHeader(MsgType transType) {
		WlHeader header = new WlHeader();
		header.setVersion(Const.wlVersion);
		header.setTransType(transType.getCode());
		header.setEmployNo(BaseApplication.getInstance().getUser().getEmployNo());
//		header.setTermId(Const.SettingConst.termId);
		header.setRequestTime(DvDateUtils.getStringByFormat(new Date(), "yyyyMMddHHmmss"));
		
		return header;
	}
	
	/**
	 * 登陆
	 * 
	 * @param transfer
	 * @version 1.0
	 */
	public void userLogin(ITransfer transfer, String employNo, String passwd) {
		UserLoginReqWlBody body = new UserLoginReqWlBody();
		body.setPasswd(Md5.digest(passwd.getBytes()));
		WlHeader header = new WlHeader();
		header.setVersion(Const.wlVersion);
		header.setTransType(MsgType.LOGIN.getCode());
		header.setEmployNo(employNo);
//		header.setTermId(Const.SettingConst.termId);
		header.setRequestTime(DvDateUtils.getStringByFormat(new Date(), "yyyyMMddHHmmss"));
		UserLoginRequest request = new UserLoginRequest("POST", Const.wlUrl+"toLogin", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 获取任务
	 * 
	 * @param transfer
	 * @param pageIndex
	 * @param pageSize
	 * @version 1.0
	 */
	public void queryOrders(ITransfer transfer, int pageIndex, int pageSize, int type) {
		WlHeader header = generateHeader(MsgType.ORDERS_QUERY);
		QueryOrdersReqWlBody body = new QueryOrdersReqWlBody();
		body.setEmployno(BaseApplication.getInstance().getUser().getEmployNo());
		Page page = new Page();
		page.setPageNo(pageIndex+"");
		page.setPageSize(pageSize+"");
		body.setPage(page);
		body.setDatatype(type+"");
		QueryOrdersRequest request = new QueryOrdersRequest("POST", Const.wlUrl+"toGetDeliveryTask", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 派件签收
	 * 
	 * @param transfer
	 * @param orders
	 * @version 1.0
	 */
	public void orderReceive(ITransfer transfer, List<SignOrder> orders) {
		WlHeader header = generateHeader(MsgType.ORDER_SIGN);
		OrderReceiveReqWlBody body = new OrderReceiveReqWlBody();
		body.setOrders(orders);
		OrderReceiveRequest request = new OrderReceiveRequest("POST", Const.wlUrl+"toPayAmount", header, body);
		transfer.sendRequest(request);
	}
	
	/**
	 * 运单信息查询
	 * 
	 * @param transfer
	 * @param orderno
	 * @version 1.0
	 */
	public void queryOrderInfo(ITransfer transfer, String orderno) {
		WlHeader header = generateHeader(MsgType.ORDER_QUERY);
		QueryOrderInfoReqWlBody body = new QueryOrderInfoReqWlBody();
		body.setOrderno(orderno);
		QueryOrderInfoRequest request = new QueryOrderInfoRequest("POST", Const.wlUrl+"toCwbSearch", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 异常件登记
	 * 
	 * @param transfer
	 * @param orderno
	 * @param badtype
	 * @param errorcode
	 * @param memo
	 * @param urgent
	 * @version 1.0
	 */
	public void badOrderSign(ITransfer transfer, String orderno, String badtype, String errorcode, String memo, String urgent) {
		WlHeader header = generateHeader(MsgType.BAD_ORDER_SIGN);
		BadOrdSignReqWlBody body = new BadOrdSignReqWlBody();
		body.setOrderno(orderno);
		body.setBadtype(badtype);
		body.setErrorcode(errorcode);
		body.setMemo(memo);
		body.setUrgent(urgent);
		BadOrderSignRequest request = new BadOrderSignRequest("POST", Const.wlUrl+"toExpFeedBack", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 运单签收撤销
	 * 
	 * @param transfer
	 * @param orderno
	 * @param banktrace
	 * @param cardid
	 * @param cod
	 * @param postrace
	 * @version 1.0
	 */
	public void orderReversal(ITransfer transfer, String orderno, String banktrace, String cardid, String cod, String postrace) {
		WlHeader header = generateHeader(MsgType.ORDER_SIGN_REVERS);
		OrderReversalReqWlBody body = new OrderReversalReqWlBody();
		body.setOrderno(orderno);
		body.setBanktrace(banktrace);
		body.setCardid(cardid);
		body.setCod(cod);
		body.setPostrace(postrace);
		OrderReversalRequest request = new OrderReversalRequest("POST", Const.wlUrl+"toSignCancel", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 查询订单状态
	 * 
	 * @param transfer
	 * @param orderno
	 * @version 1.0
	 */
	public void queryOrderStatus(ITransfer transfer, String orderno) {
		WlHeader header = generateHeader(MsgType.ORDER_STATUS_QUERY);
		QueryOrderStatusReqWlBody body = new QueryOrderStatusReqWlBody();
		body.setOrderno(orderno);
		QueryOrderStatusRequest request = new QueryOrderStatusRequest("POST", Const.wlUrl+"toCwbStatus", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 结算汇总
	 * 
	 * @param transfer
	 * @version 1.0
	 */
	public void settle(ITransfer transfer) {
		WlHeader header = generateHeader(MsgType.SETTLE);
		SettleReqWlBody body = new SettleReqWlBody();
		SettleRequest request = new SettleRequest("POST", Const.wlUrl+"toSettlementSummary", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 查询APP版本
	 * 
	 * @param transfer
	 * @version 1.0
	 */
	public void queryAppVer(ITransfer transfer) {
		WlHeader header = generateHeader(MsgType.APP_UPDATE);
		QueryAppVerReqWlBody body = new QueryAppVerReqWlBody();
		QueryAppVerRequest request = new QueryAppVerRequest("POST", Const.wlUrl+"toSoftwareUpdate", header, body);
		
		transfer.sendRequest(request);
	}
	
	/**
	 * 上送位置信息
	 * 
	 * @param transfer
	 * @param location
	 * @param posttime
	 * @version 1.0
	 */
	public void uploadGps(ITransfer transfer, String lat, String lon, String posttime) {
		WlHeader header = generateHeader(MsgType.GPS_UPLOAD);
		GpsUploadReqWlBody body = new GpsUploadReqWlBody();
		body.setLat(lat);
		body.setLon(lon);
		body.setPosttime(posttime);
		GpsUploadRequest request = new GpsUploadRequest("POST", Const.wlUrl+"toGPSUpload", header, body);
		
		transfer.sendRequest(request);
	}
}
