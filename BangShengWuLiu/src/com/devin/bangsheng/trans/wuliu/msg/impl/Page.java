package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.XmlBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("page")
public class Page extends XmlBean{
	@ReqField(sort=1)
	@XStreamAlias("startindex")
	private String startindex;
	
	@ReqField(sort=2)
	@XStreamAlias("page_size")
	private String page_size;
	
	@ReqField(sort=3)
	@XStreamAlias("total")
	private String total;

	public String getPageNo() {
		return startindex;
	}

	public void setPageNo(String pageNo) {
		this.startindex = pageNo;
	}

	public String getPageSize() {
		return page_size;
	}

	public void setPageSize(String pageSize) {
		this.page_size = pageSize;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	
}
