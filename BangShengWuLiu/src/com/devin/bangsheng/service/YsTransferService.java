package com.devin.bangsheng.service;

import com.devin.bangsheng.trans.yinshi.msg.impl.CashQueryRequest;
import com.devin.bangsheng.trans.yinshi.msg.impl.ConsumeNotifyRequest;
import com.devin.framework.tcp.ITransfer;

public class YsTransferService {
	
	public static YsTransferService instance;
	private YsTransferService() {
	}
	
	public static YsTransferService getInstance() {
		if(instance == null) {
			instance = new YsTransferService();
		}
		
		return instance;
	}
	
	public void consumeNotify(ITransfer transfer, int requestId, String amount, String batchNo
			, String employNo, String mid, String tid, String orderNo, String pan, String payWay
			, String posTrace, String reqTime, String rrn, String signer
			, String signFlag, String systrace, String transType, String elecSign
			, String startDate, String endDate) {
		ConsumeNotifyRequest request = new ConsumeNotifyRequest();
		request.setRequestId(requestId);
		request.setAmount(amount);
		request.setBatchNo(batchNo);
		request.setEmployNo(employNo);
		request.setMid(mid);
		request.setTid(tid);
		request.setOrderNo(orderNo);
		request.setPan(pan);
		request.setPayWay(payWay);
		request.setPosTrace(posTrace);
		request.setReqTime(reqTime);
		request.setRrn(rrn);
		request.setSigner(signer);
		request.setSignFlag(signFlag);
		request.setSystrace(systrace);
		request.setTransType(transType);
		request.setElecSign(elecSign);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		
		transfer.sendRequest(request);
	}
	
	public void consumeNotify(ITransfer transfer, String amount, String batchNo
			, String employNo, String mid, String tid, String orderNo, String pan, String payWay
			, String posTrace, String reqTime, String rrn, String signer
			, String signFlag, String systrace, String transType, String elecSign, String startDate
			, String endDate) {
		consumeNotify(transfer, 0, amount, batchNo, employNo, mid, tid, orderNo, pan, payWay, posTrace, reqTime, rrn, signer, signFlag, systrace, transType, elecSign, startDate, endDate);
	}
	
	public void queryCash(ITransfer transfer, String employNo, String mid, String tid, String reqTime,  String transType
			, String wlTrace, String systrace, String startDate, String endDate) {
		CashQueryRequest request = new CashQueryRequest();
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setEmployNo(employNo);
		request.setMid(mid);
		request.setReqTime(reqTime);
		request.setSystrace(systrace);
		request.setTransType(transType);
		request.setTid(tid);
		
		transfer.sendRequest(request);
	}
}
