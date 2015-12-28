package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 签收订单
 *
 *
 * @author Devin_chen
 * @date 2015年9月24日
 * 
 * @version 1.0
 */
@XStreamAlias("order")
public class SignOrder {
	/**
	 * 运单号
	 */
	public String orderno;
	/**
	 * 代收款金额
	 */
	public String cod;
	/**
	 * 代收款支付方式
	 */
	public String payway;
	/**
	 * 银行系统参考号
	 */
	public String banktrace;
	/**
	 * POS机的流水号
	 */
	public String postrace;
	/**
	 * 银行交易时间
	 */
	public String tracetime;
	/**
	 * 银行卡号
	 */
	public String cardid;
	/**
	 * 本人签收标记
	 */
	public String signflag;
	/**
	 * 签收人
	 */
	public String signer;
	/**
	 * 电商编号
	 */
	public String dssn;
	/**
	 * 电商名称
	 */
	public String dsname;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getPayway() {
		return payway;
	}
	public void setPayway(String payway) {
		this.payway = payway;
	}
	public String getBanktrace() {
		return banktrace;
	}
	public void setBanktrace(String banktrace) {
		this.banktrace = banktrace;
	}
	public String getPostrace() {
		return postrace;
	}
	public void setPostrace(String postrace) {
		this.postrace = postrace;
	}
	public String getTracetime() {
		return tracetime;
	}
	public void setTracetime(String tracetime) {
		this.tracetime = tracetime;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getSignflag() {
		return signflag;
	}
	public void setSignflag(String signflag) {
		this.signflag = signflag;
	}
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	public String getDssn() {
		return dssn;
	}
	public void setDssn(String dssn) {
		this.dssn = dssn;
	}
	public String getDsname() {
		return dsname;
	}
	public void setDsname(String dsname) {
		this.dsname = dsname;
	}
	
	
}
