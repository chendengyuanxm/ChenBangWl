package com.devin.bangsheng.trans.yinshi.msg;

import java.io.UnsupportedEncodingException;

import com.devin.bangsheng.common.Const;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.tcp.Response;
import com.devin.framework.tcp.SocketRequest;
import com.newland.mtype.util.ISOUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class AbstractYsMessage implements SocketRequest{
	
	private static int LEN_MSG = 2;	//报文长度表示位数
	private static int LEN_EXTRA = 2; //附加数据长度表示位数
	
	private static String XML_DEC = "<?xml version=\"1.0\"  encoding=\"GBK\"  ?>";
	private static String ENCODE = "GBK";
	
	@XStreamOmitField
	protected Logger logger = LoggerFactory.getLogger(AbstractYsMessage.class);
	
	@Override
	public byte[] createRequest() {
		byte[] reqContent = null;
		try {
			byte[] extra = toXml().getBytes(ENCODE);
			int extraLen = extra.length;
			int len = extraLen;
			reqContent = new byte[len+LEN_MSG];
			int offset = 0;
			byte[] lenbs = ISOUtils.intToBytes(len, LEN_MSG, true);
			System.arraycopy(lenbs, 0, reqContent, offset, lenbs.length);
			offset += lenbs.length;
			System.arraycopy(extra, 0, reqContent, offset, extra.length);
			logger.debug("pack data:" + ISOUtils.hexString(reqContent));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return reqContent;
	}
	
	@Override
	public String getUrl() {
		return Const.ysXmlUrl;
	}
	
	@Override
	public int getPort() {
		return Const.ysXmlPort;
	}
	
	public String toXml() {
		XStream xStream = new XStream();
		xStream.alias("transaction", getClass());
		xStream.processAnnotations(getClass());
		String xml = XML_DEC + "\n" + xStream.toXML(this).replaceAll("__", "_");
		logger.debug(xml);
		
		return xml;
	}
}
