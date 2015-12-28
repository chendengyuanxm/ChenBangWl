package com.devin.bangsheng.trans.yinshi.msg;

import java.io.UnsupportedEncodingException;

import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.tcp.Response;
import com.devin.framework.util.DvStrUtils;
import com.newland.mtype.util.ISOUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class AbstractYsResponse implements Response{
	
	private static int LEN_MSG = 2;	//报文长度表示位数
	@XStreamOmitField
	protected Logger logger = LoggerFactory.getLogger(AbstractYsMessage.class);
	
	public void unpack0(byte[] resp) {
		int offset = 0;
		byte[] lenbs = new byte[LEN_MSG];
		System.arraycopy(resp, offset, lenbs, 0, lenbs.length);
		offset += lenbs.length;
		int msgLen = ISOUtils.bytesToInt(lenbs, 0, lenbs.length, true);
		byte[] xmlbs = new byte[msgLen];
		System.arraycopy(resp, offset, xmlbs, 0, xmlbs.length);
		String xml = "";
		logger.debug(ISOUtils.hexString(xmlbs));
		try {
			xml = new String(xmlbs, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.debug("返回xml：" + xml);
		if(!DvStrUtils.isEmpty(xml)) {
			toBean(xml);
		}else {
			throw new RuntimeException("服务器返回数据为空");
		}
	}
	
	public void toBean(String xml) {
		XStream xStream = new XStream();
		xStream.alias("transaction", getClass());
		xStream.processAnnotations(getClass());
		xStream.fromXML(xml, this);
	}

	@Override
	public void parse(String resp) {
		byte[] data = ISOUtils.hex2byte(resp);
		unpack0(data);
	}
}
