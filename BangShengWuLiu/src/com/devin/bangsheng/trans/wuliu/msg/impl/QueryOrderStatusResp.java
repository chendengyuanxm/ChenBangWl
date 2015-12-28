package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;

/**
 * 查询运单配送状态返回报文
 *
 *
 * @author Devin_chen
 * @date 2015年8月26日
 * 
 * @version 1.0
 */
public class QueryOrderStatusResp extends AbstractWlResponse{
	
	public static class QueryOrderStatusRespWlBody extends WlBody {
		/**
		 * 寄件网点编号
		 */
		private String netcode;
		/**
		 * 寄件网点名称
		 */
		private String netname ;
		/**
		 * 代收款金额
		 */
		private String cod;
		/**
		 * 快件状态
		 */
		private String status;
		/**
		 * 状态描述
		 */
		private String msg;
		public String getNetcode() {
			return netcode;
		}
		public void setNetcode(String netcode) {
			this.netcode = netcode;
		}
		public String getNetname() {
			return netname;
		}
		public void setNetname(String netname) {
			this.netname = netname;
		}
		public String getCod() {
			return cod;
		}
		public void setCod(String cod) {
			this.cod = cod;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
	}

	@Override
	public Class getBodyClass() {
		return QueryOrderStatusRespWlBody.class;
	}
}
