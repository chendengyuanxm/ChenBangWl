package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 结算汇总请求
 *
 *
 * @author Devin_chen
 * @date 2015年8月26日
 * 
 * @version 1.0
 */
public class SettleRequest extends AbstractWlRequest{
	
	public SettleRequest(String method, String url, WlHeader header,
			SettleReqWlBody body) {
		super(method, url, header, body);
	}

	@Override
	public Response createResponse(String resp) {
		SettleResp response = new SettleResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class SettleReqWlBody extends WlBody {
	}
}
