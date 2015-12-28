package com.devin.bangsheng.trans.wuliu.msg.impl;

import java.util.List;

import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.bangsheng.trans.wuliu.msg.impl.UserLoginResp.UserLoginRespWlBody;
import com.devin.framework.tcp.Response;


public class QueryOrdersResp extends AbstractWlResponse{
	
	public static class QueryOrdersRespWlBody extends WlBody {
		private Page page;
		private List<Order> orders;
		public Page getPage() {
			return page;
		}
		public void setPage(Page page) {
			this.page = page;
		}
		public List<Order> getOrders() {
			return orders;
		}
		public void setOrders(List<Order> orders) {
			this.orders = orders;
		}
	}

	@Override
	public Class getBodyClass() {
		return QueryOrdersRespWlBody.class;
	}
}
