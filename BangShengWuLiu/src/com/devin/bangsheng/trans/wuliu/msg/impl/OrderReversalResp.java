package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;


public class OrderReversalResp extends AbstractWlResponse{
	
	public static class OrderReversalRespWlBody extends WlBody {
	}

	@Override
	public Class getBodyClass() {
		return OrderReversalRespWlBody.class;
	}
}
