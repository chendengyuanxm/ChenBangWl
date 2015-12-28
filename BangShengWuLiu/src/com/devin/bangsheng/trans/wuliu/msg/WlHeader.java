package com.devin.bangsheng.trans.wuliu.msg;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("transaction_header")
public class WlHeader extends XmlBean{
	
	@XStreamAlias("version")
	@ReqField(sort = 1)
	private String version;
	
	@XStreamAlias("transtype")
	@ReqField(sort = 2)
	private String transType;
	
	@XStreamAlias("employno")
	@ReqField(sort = 3)
	private String employNo;
	
	@XStreamAlias("termid")
	@ReqField(sort = 4)
	private String termId;
	
	@XStreamAlias("request_time")
	@ReqField(sort = 5)
	private String requestTime;
	
	@XStreamAlias("response_time")
	@ReqField(sort = 6)
	private String responseTime;
	
	@XStreamAlias("response_code")
	@ReqField(sort = 7)
	private String responseCode;
	
	@XStreamAlias("response_msg")
	@ReqField(sort = 8)
	private String responseMsg;
	
	@XStreamAlias("mac")
	@ReqField(sort = 9)
	private String mac;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getEmployNo() {
		return employNo;
	}

	public void setEmployNo(String employNo) {
		this.employNo = employNo;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
	public boolean success() {
		return "00".equals(responseCode);
	}
}
