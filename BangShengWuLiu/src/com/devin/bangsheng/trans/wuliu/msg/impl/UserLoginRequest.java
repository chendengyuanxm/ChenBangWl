package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;


public class UserLoginRequest extends AbstractWlRequest{
	
	public UserLoginRequest(String method, String url, WlHeader header,
			UserLoginReqWlBody body) {
		super(method, url, header, body);
	}

	@Override
	public Response createResponse(String resp) {
		UserLoginResp response = new UserLoginResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class UserLoginReqWlBody extends WlBody {
		@ReqField(sort=1)
		private String passwd;
		
		public UserLoginReqWlBody(){}

		public String getPasswd() {
			return passwd;
		}
		public void setPasswd(String passwd) {
			this.passwd = passwd;
		}
	}
	
}
