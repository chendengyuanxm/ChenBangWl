package com.devin.bangsheng.trans.wuliu.msg;

import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.trans.yinshi.msg.AbstractYsMessage;
import com.devin.bangsheng.util.Md5;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class AbstractWlResponse implements Response{
	
	@XStreamOmitField
	protected Logger logger = LoggerFactory.getLogger(AbstractYsMessage.class);
	private static String XML_DEC = "<?xml version='1.0' encoding='UTF-8' ?>";
	
	@XStreamAlias("transaction_header")
	public WlHeader header;
	@XStreamAlias("transaction_body")
	public WlBody body;
	
	@Override
	public void parse(String resp) {
		XStream xStream = new XStream();
		xStream.autodetectAnnotations(true);
		xStream.alias("transaction", getClass());
		xStream.addDefaultImplementation(getBodyClass(), WlBody.class);
		AbstractWlResponse response = (AbstractWlResponse) xStream.fromXML(resp);
		logger.debug(response.header.getResponseCode() + " [" + response.header.getResponseMsg() + "]");
		String xmlBeforeMac = resp.replaceAll("<\\?xml(.*)\\?>", "").replaceAll("<mac>(.*)</mac>", "") + Const.wlPrivateKey;
		String mac = Md5.digest(xmlBeforeMac.getBytes());
		String macByWeb = response.header.getMac();
		logger.debug("xmlBeforeMac:" + xmlBeforeMac);
		logger.debug("mac:" + mac);
		logger.debug("macByWeb:" + macByWeb);
		if(!macByWeb.equals(mac)) {
			throw new RuntimeException("MAC校验失败");
		}
		
		header = response.header;
		body = response.body;
	}
	
	public abstract Class getBodyClass();
}
