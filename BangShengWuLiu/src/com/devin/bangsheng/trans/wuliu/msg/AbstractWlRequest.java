package com.devin.bangsheng.trans.wuliu.msg;

import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.trans.yinshi.msg.AbstractYsMessage;
import com.devin.bangsheng.util.Md5;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.tcp.HttpRequest;
import com.devin.framework.tcp.Response;
import com.devin.framework.util.DvStrUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.mapper.Mapper;

public abstract class AbstractWlRequest extends HttpRequest{
	
	@XStreamOmitField
	protected Logger logger = LoggerFactory.getLogger(AbstractYsMessage.class);
	private static String XML_DEC = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>";
	
	@XStreamAlias("transaction_header")
	public WlHeader header;
	@XStreamAlias("transaction_body")
	public WlBody body;
	@XStreamOmitField
	public String method;
	@XStreamOmitField
	public String url;
	
	public AbstractWlRequest(String method, String url, WlHeader header, WlBody body) {
		this.header = header;
		this.body = body;
		this.method = method;
		this.url = url;
	}
	
	public String toXml() {
		XStream xStream = new XStream();
		xStream.alias("transaction", getClass());
		xStream.addDefaultImplementation(body.getClass(), WlBody.class);
		xStream.processAnnotations(new Class[]{getClass(), body.getClass(), header.getClass()});
		String xmlBeforeMac = DvStrUtils.replaceBlank(xStream.toXML(this).replaceAll("__", "_"));
		String mac = calMac(xmlBeforeMac+Const.wlPrivateKey);
		header.setMac(mac);
		String xmlAfterMac = xStream.toXML(this);
		String xml = XML_DEC + DvStrUtils.replaceBlank(xmlAfterMac);
		xml = xml.replaceAll("__", "_");	//xStream转换BUG
//		logger.debug(xml.toString());
		
		return xml.toString();
	}
	
	private String calMac(String str) {
		return Md5.digest(str.getBytes());
	}

	@Override
	public int getId() {
		return 0;
	}

	public WlHeader getWlHeader() {
		return header;
	}

	public void setWlHeader(WlHeader header) {
		this.header = header;
	}

	@Override
	public String getBody() {
		return toXml();
	}

	@Override
	public String getHeader() {
		return null;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getMethod() {
		return method;
	}
}
