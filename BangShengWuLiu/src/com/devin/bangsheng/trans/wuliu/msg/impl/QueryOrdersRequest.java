package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;


public class QueryOrdersRequest extends AbstractWlRequest{
	
	public QueryOrdersRequest(String method, String url, WlHeader header,
			QueryOrdersReqWlBody body) {
		super(method, url, header, body);
	}

	@Override
	public Response createResponse(String resp) {
		QueryOrdersResp response = new QueryOrdersResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class QueryOrdersReqWlBody extends WlBody {
		@ReqField(sort=1)
		private String employno;
		@ReqField(sort=2)
		private Page page;
		/**
		 * 获取数据类型1：获取待配送任务列表,2：总订单查询,3：妥投订单查询
		 */
		@ReqField(sort=3)
		private String datatype;
		public String getEmployno() {
			return employno;
		}
		public void setEmployno(String employno) {
			this.employno = employno;
		}
		public Page getPage() {
			return page;
		}
		public void setPage(Page page) {
			this.page = page;
		}
		public String getDatatype() {
			return datatype;
		}
		public void setDatatype(String datatype) {
			this.datatype = datatype;
		}
	}
}
