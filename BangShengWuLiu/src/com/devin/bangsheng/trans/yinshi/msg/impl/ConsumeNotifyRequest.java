package com.devin.bangsheng.trans.yinshi.msg.impl;

import com.devin.bangsheng.trans.yinshi.msg.AbstractYsMessage;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class ConsumeNotifyRequest extends AbstractYsMessage{
	/**
	 * 交易码
	 */
	@XStreamAlias("txn_id")
	private String txnId = "300001";
	/**
	 * 渠道编码
	 */
	@XStreamAlias("sp_id")
	private String spId = "90000001";
	
	@XStreamAlias("orderno")
	private String orderNo;
	
	@XStreamAlias("employno")
	private String employNo;
	
	@XStreamAlias("payway")
	private String payWay;
	
	@XStreamAlias("transtype")
	private String transType;
	
	@XStreamAlias("signflag")
	private String signFlag;
	
	@XStreamAlias("signer")
	private String signer;
	
	@XStreamAlias("requtime")
	private String reqTime;
	
	@XStreamAlias("amount")
	private String amount;
	/**
	 * 卡号
	 */
	@XStreamAlias("pan")
	private String pan;
	/**
	 * 商户号
	 */
	@XStreamAlias("mid")
	private String mid;
	/**
	 * 终端号
	 */
	@XStreamAlias("tid")
	private String tid;
	/**
	 * 检索参考号
	 */
	@XStreamAlias("rrn")
	private String rrn;
	/**
	 * 交易流水号
	 */
	@XStreamAlias("systrace")
	private String systrace;
	/**
	 * POS批次号
	 */
	@XStreamAlias("batch_no")
	private String batchNo;
	/**
	 * 受卡方系统跟踪号
	 */
	@XStreamAlias("pos_systrace")
	private String posTrace;
	/**
	 * 电子签名
	 */
	@XStreamAlias("elec_sign")
	private String elecSign;
	/**
	 * 开始时间
	 */
	@XStreamAlias("startdate")
	private String startDate;
	/**
	 * 结束时间
	 */
	@XStreamAlias("enddate")
	private String endDate;
	/**
	 * 请求ID标识 
	 */
	@XStreamOmitField
	private int requestId;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getEmployNo() {
		return employNo;
	}

	public void setEmployNo(String employNo) {
		this.employNo = employNo;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getSystrace() {
		return systrace;
	}

	public void setSystrace(String systrace) {
		this.systrace = systrace;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getPosTrace() {
		return posTrace;
	}

	public void setPosTrace(String posTrace) {
		this.posTrace = posTrace;
	}

	public String getElecSign() {
		return elecSign;
	}

	public void setElecSign(String elecSign) {
		this.elecSign = elecSign;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	@Override
	public int getId() {
		return requestId;
	}
	
	@Override
	public Response createResponse(String resp) {
		ConsumeNotifyResponse response = new ConsumeNotifyResponse();
		response.parse(resp);
		return response;
	}
}
