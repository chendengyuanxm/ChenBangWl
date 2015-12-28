package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;


public class OrderReversalRequest extends AbstractWlRequest{
	
	public OrderReversalRequest(String method, String url, WlHeader header,
			OrderReversalReqWlBody body) {
		super(method, url, header, body);
	}

	@Override
	public Response createResponse(String resp) {
		OrderReversalResp response = new OrderReversalResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class OrderReversalReqWlBody extends WlBody {
		/**
		 * 运单号
		 */
		@ReqField(sort=1)
		private String orderno;
		/**
		 * 代收款金额
		 */
		@ReqField(sort=2)
		private String cod;
		/**
		 * 银行卡号
		 */
		@ReqField(sort=3)
		private String cardid;
		/**
		 * 银行系统参考号
		 */
		@ReqField(sort=4)
		private String banktrace;
		/**
		 * POS机的流水号
		 */
		@ReqField(sort=5)
		private String postrace;
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
		public String getCardid() {
			return cardid;
		}
		public void setCardid(String cardid) {
			this.cardid = cardid;
		}
	}
}
