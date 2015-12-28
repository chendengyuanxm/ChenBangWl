package com.devin.framework.tcp;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public abstract class HttpRequest implements Request<HttpRequestBase>{
	
	@XStreamOmitField
	private Logger logger = LoggerFactory.getLogger(HttpRequest.class);
	
	@Override
	public HttpRequestBase createRequest() {
		final String method = getMethod();
		HttpEntityEnclosingRequestBase request = new HttpEntityEnclosingRequestBase() {
			@Override
			public String getMethod() {
				return method;
			}
		}; 
		try {
			String body = getBody();
			request.setURI(URI.create(getUrl()));
//			request.getParams().setParameter("context", getBody());
			request.getParams().setParameter("Accept", "text/xml;charset=UTF-8");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("context", body));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
			request.setEntity(entity);
			logger.debug("url:" + "[" + request.getMethod() + "]" + request.getURI().toString());
			logger.debug("entity:" + body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return request;
	}
	
	public abstract String getBody();
	public abstract String getHeader();
	public abstract String getUrl();
	public abstract String getMethod();
}
