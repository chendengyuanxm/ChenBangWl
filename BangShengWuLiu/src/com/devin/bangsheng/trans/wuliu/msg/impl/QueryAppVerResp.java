package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;


public class QueryAppVerResp extends AbstractWlResponse{
	
	public static class QueryAppVerRespWlBody extends WlBody {
		/**
		 * 版本号
		 */
		private String ver;
		/**
		 * 发布时间
		 */
		private String releasedate;
		/**
		 * 版本文件存放位置
		 */
		private String filepath;
		/**
		 * 版本说明
		 */
		private String verdefine;
		public String getVer() {
			return ver;
		}
		public void setVer(String ver) {
			this.ver = ver;
		}
		public String getReleasedate() {
			return releasedate;
		}
		public void setReleasedate(String releasedate) {
			this.releasedate = releasedate;
		}
		public String getFilepath() {
			return filepath;
		}
		public void setFilepath(String filepath) {
			this.filepath = filepath;
		}
		public String getVerdefine() {
			return verdefine;
		}
		public void setVerdefine(String verdefine) {
			this.verdefine = verdefine;
		}
		
	}

	@Override
	public Class getBodyClass() {
		return QueryAppVerRespWlBody.class;
	}
}
