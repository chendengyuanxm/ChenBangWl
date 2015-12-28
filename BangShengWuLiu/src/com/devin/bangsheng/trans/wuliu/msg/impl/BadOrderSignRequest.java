package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.bangsheng.trans.wuliu.msg.impl.BadOrderSignResp.BadOrdSignRespWlBody;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 问题件登记请求报文
 *
 * @author Devin_chen
 * @date 2015年8月27日
 * 
 * @version 1.0
 */
public class BadOrderSignRequest extends AbstractWlRequest{
	
	public BadOrderSignRequest(String method, String url, WlHeader header,
			BadOrdSignReqWlBody body) {
		super(method, url, header, body);
	}

	@Override
	public Response createResponse(String resp) {
		BadOrderSignResp response = new BadOrderSignResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class BadOrdSignReqWlBody extends WlBody {
		/**
		 * 运单号
		 */
		@ReqField(sort=1)
		private String orderno;
		/**
		 * 问题件类型
		 */
		@ReqField(sort=2)
		private String badtype;
		/**
		 * 异常件编码
		 */
		@ReqField(sort=3)
		private String errorcode;
		/**
		 * 异常描述
		 */
		@ReqField(sort=4)
		private String memo;
		/**
		 * 紧急标志<br>0-不紧急 1-紧急
		 */
		@ReqField(sort=5)
		private String urgent;
		public String getOrderno() {
			return orderno;
		}
		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}
		public String getBadtype() {
			return badtype;
		}
		public void setBadtype(String badtype) {
			this.badtype = badtype;
		}
		public String getErrorcode() {
			return errorcode;
		}
		public void setErrorcode(String errorcode) {
			this.errorcode = errorcode;
		}
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
		public String getUrgent() {
			return urgent;
		}
		public void setUrgent(String urgent) {
			this.urgent = urgent;
		}
	}
}
