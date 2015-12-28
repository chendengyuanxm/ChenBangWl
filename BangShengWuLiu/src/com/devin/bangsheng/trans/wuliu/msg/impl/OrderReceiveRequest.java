package com.devin.bangsheng.trans.wuliu.msg.impl;

import java.util.List;

import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class OrderReceiveRequest extends AbstractWlRequest{
	
	public OrderReceiveRequest(String method, String url, WlHeader header,
			OrderReceiveReqWlBody body) {
		super(method, url, header, body);
	}

	@Override
	public Response createResponse(String resp) {
		OrderReceiveResp response = new OrderReceiveResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class OrderReceiveReqWlBody extends WlBody {
		@XStreamAlias("orders")
		private List<SignOrder> orders;
		
		public List<SignOrder> getOrders() {
			return orders;
		}
		public void setOrders(List<SignOrder> orders) {
			this.orders = orders;
		}
	}
}
