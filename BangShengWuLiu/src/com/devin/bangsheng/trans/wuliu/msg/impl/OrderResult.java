package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.XmlBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("order")
public class OrderResult extends XmlBean{
	private String order_no;
	private String rsltCode;
	private String rsltMsg;
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getRsltCode() {
		return rsltCode;
	}
	public void setRsltCode(String rsltCode) {
		this.rsltCode = rsltCode;
	}
	public String getRsltMsg() {
		return rsltMsg;
	}
	public void setRsltMsg(String rsltMsg) {
		this.rsltMsg = rsltMsg;
	}
	
}
