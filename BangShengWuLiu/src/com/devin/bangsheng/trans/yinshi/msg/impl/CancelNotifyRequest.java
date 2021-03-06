package com.devin.bangsheng.trans.yinshi.msg.impl;

import com.devin.bangsheng.trans.yinshi.msg.AbstractYsMessage;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CancelNotifyRequest extends AbstractYsMessage{
	/**
	 * 交易码
	 */
	@XStreamAlias("txn_id")
	private String txnId = "300002";
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
	
	@XStreamAlias("wltrace")
	private String wlTrace;
	
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
	
	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

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

	public String getWlTrace() {
		return wlTrace;
	}

	public void setWlTrace(String wlTrace) {
		this.wlTrace = wlTrace;
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

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Response createResponse(String resp) {
		CancelNotifyResponse response = new CancelNotifyResponse();
		response.parse(resp);
		return response;
	}

	@Override
	public String toString() {
		return "ConsumeNotifyRequest [txnId=" + txnId + ", spId=" + spId
				+ ", orderNo=" + orderNo + ", employNo=" + employNo
				+ ", payWay=" + payWay + ", transType=" + transType
				+ ", wlTrace=" + wlTrace + ", signFlag=" + signFlag
				+ ", signer=" + signer + ", reqTime=" + reqTime + ", amount="
				+ amount + ", pan=" + pan + ", mid=" + mid + ", tid=" + tid
				+ ", rrn=" + rrn + ", systrace=" + systrace + ", batchNo="
				+ batchNo + ", posTrace=" + posTrace + "]";
	}
}
