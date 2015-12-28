package com.devin.bangsheng.trans.wuliu.msg.impl;

import java.util.ArrayList;
import java.util.List;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;


public class OrderReceiveResp extends AbstractWlResponse{
	
	public static class OrderReceiveRespWlBody extends WlBody {
		private ArrayList<OrderResult> orders;

		public ArrayList<OrderResult> getOrders() {
			return orders;
		}

		public void setOrders(ArrayList<OrderResult> orders) {
			this.orders = orders;
		}
		
	}

	@Override
	public Class getBodyClass() {
		return OrderReceiveRespWlBody.class;
	}
}
