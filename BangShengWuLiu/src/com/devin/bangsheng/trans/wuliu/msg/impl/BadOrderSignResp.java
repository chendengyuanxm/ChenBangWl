package com.devin.bangsheng.trans.wuliu.msg.impl;
import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
/**
 *  异常件登记返回报文
 *
 * @author Devin_chen
 * @date 2015年8月27日
 * 
 * @version 1.0
 */
public class BadOrderSignResp extends AbstractWlResponse{
	
	public static class BadOrdSignRespWlBody extends WlBody {
	}

	@Override
	public Class getBodyClass() {
		return BadOrdSignRespWlBody.class;
	}
}
