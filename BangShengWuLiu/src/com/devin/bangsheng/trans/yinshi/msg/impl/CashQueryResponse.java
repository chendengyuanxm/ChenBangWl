package com.devin.bangsheng.trans.yinshi.msg.impl;

import com.devin.bangsheng.trans.yinshi.msg.AbstractYsResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CashQueryResponse extends AbstractYsResponse{
	
	/**
	 * 交易码
	 */
	@XStreamAlias("txn_id")
	private String txnId = "300003";
	/**
	 * 渠道编码
	 */
	@XStreamAlias("sp_id")
	private String spId = "90000001";
	
	@XStreamAlias("employno")
	private String employNo;
	
	@XStreamAlias("transtype")
	private String transType;
	
	@XStreamAlias("resptime")
	private String respTime;
	
	@XStreamAlias("respcode")
	private String respCode;
	
	@XStreamAlias("startdate")
	private String startDate;
	
	@XStreamAlias("enddate")
	private String endDate;
	
	@XStreamAlias("amount")
	private String amount;
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
	 * 交易流水号
	 */
	@XStreamAlias("systrace")
	private String systrace;
	public String getEmployNo() {
		return employNo;
	}
	public void setEmployNo(String employNo) {
		this.employNo = employNo;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getRespTime() {
		return respTime;
	}
	public void setRespTime(String respTime) {
		this.respTime = respTime;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	public String getSystrace() {
		return systrace;
	}
	public void setSystrace(String systrace) {
		this.systrace = systrace;
	}
	
}
