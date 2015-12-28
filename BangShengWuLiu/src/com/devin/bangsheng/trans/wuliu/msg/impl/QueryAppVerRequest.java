package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.enums.MsgType;
import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class QueryAppVerRequest extends AbstractWlRequest{
	
	public QueryAppVerRequest(String method, String url, WlHeader header,
			QueryAppVerReqWlBody body) {
		super( method, url, header, body);
	}
	
	@Override
	public int getId() {
		return MsgType.APP_UPDATE.getId();
	}

	@Override
	public Response createResponse(String resp) {
		QueryAppVerResp response = new QueryAppVerResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class QueryAppVerReqWlBody extends WlBody {
	}
}
