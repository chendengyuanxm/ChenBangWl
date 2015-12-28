package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.enums.MsgType;
import com.devin.bangsheng.trans.wuliu.msg.AbstractWlRequest;
import com.devin.bangsheng.trans.wuliu.msg.ReqField;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;
import com.devin.bangsheng.trans.wuliu.msg.WlHeader;
import com.devin.framework.tcp.Response;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class GpsUploadRequest extends AbstractWlRequest{
	
	public GpsUploadRequest(String method, String url, WlHeader header,
			GpsUploadReqWlBody body) {
		super(method, url, header, body);
	}
	
	@Override
	public int getId() {
		return MsgType.GPS_UPLOAD.getId();
	}
	
	@Override
	public Response createResponse(String resp) {
		GpsUploadResp response = new GpsUploadResp();
		response.parse(resp);
		return response;
	}
	
	@XStreamAlias("transaction_body")
	public static class GpsUploadReqWlBody extends WlBody {
		/**
		 * 经度
		 */
		@ReqField(sort=1)
		private String lon;
		/**
		 * 纬度
		 */
		@ReqField(sort=2)
		private String lat;
		/**
		 * 提交时间
		 */
		@ReqField(sort=3)
		private String posttime;
		public String getLon() {
			return lon;
		}
		public void setLon(String lon) {
			this.lon = lon;
		}
		public String getLat() {
			return lat;
		}
		public void setLat(String lat) {
			this.lat = lat;
		}
		public String getPosttime() {
			return posttime;
		}
		public void setPosttime(String posttime) {
			this.posttime = posttime;
		}
	}
}
