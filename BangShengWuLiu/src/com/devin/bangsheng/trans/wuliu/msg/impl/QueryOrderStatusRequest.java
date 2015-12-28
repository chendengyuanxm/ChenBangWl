package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 运单配送状态查询报文
 *
 *
 * @author Devin_chen
 * @date 2015年8月26日
 * 
 * @version 1.0
 */
public class QueryOrderStatusRequest extends AbstractWlRequest{
	
	public QueryOrderStatusRequest(String method, String url, WlHeader header,
			QueryOrderStatusReqWlBody body) {
		super(method, url, header, body);
	}

	@Override
	public Response createResponse(String resp) {
		QueryOrderStatusResp response = new QueryOrderStatusResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class QueryOrderStatusReqWlBody extends WlBody {
		@ReqField(sort=1)
		private String orderno;

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}
		
	}
}
