package com.devin.bangsheng.trans.wuliu.msg;

import com.devin.bangsheng.trans.yinshi.msg.AbstractYsMessage;
import com.devin.bangsheng.util.Md5;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class WlMessage {
	
	@XStreamOmitField
	protected Logger logger = LoggerFactory.getLogger(AbstractYsMessage.class);
	
	private static String XML_DEC = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>";
	private static String ENCODE = "UTF-8";
	
	@XStreamAlias("transaction_header")
	public WlHeader header;
	@XStreamAlias("transaction_body")
	public WlBody body;
	
	public WlMessage(WlHeader header, WlBody body) {
		this.header = header;
		this.body = body;
	}
	
	public WlMessage(String method, String url, WlHeader header, WlBody body) {
		
	}
	
	public WlBody getBody() {
		return body;
	}

	public void setBody(WlBody body) {
		this.body = body;
	}

	public String toXml() {
		XStream xStream = new XStream();
		xStream.alias("transaction", getClass());
		xStream.addDefaultImplementation(body.getClass(), WlBody.class);
		xStream.processAnnotations(getClass());
		
//		String headerXmlBeforeMac = header.toXml();
//		String bodyXml = xStream.toXML(this);
//		String mac = calMac(headerXmlBeforeMac+bodyXml);
//		header.setMac(mac);
//		String headerXml = header.toXml();
//		StringBuilder xml = new StringBuilder();
//		xml.append(XML_DEC);
//		xml.append("\n");
//		xml.append("<transaction>");
//		xml.append(headerXml);
//		xml.append(bodyXml);
//		xml.append("</transaction>");
		String xmlBeforeMac = xStream.toXML(this);
		String mac = calMac(xmlBeforeMac);
		header.setMac(mac);
		String xml = xStream.toXML(this);
		logger.debug(xml.toString());
		
		return xml.toString();
	}
	
	private String calMac(String str) {
		return Md5.digest(str.getBytes());
	}

	public WlHeader getHeader() {
		return header;
	}

	public void setHeader(WlHeader header) {
		this.header = header;
	}
}
