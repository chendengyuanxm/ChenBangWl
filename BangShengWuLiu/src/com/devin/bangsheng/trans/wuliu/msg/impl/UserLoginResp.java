package com.devin.bangsheng.trans.wuliu.msg.impl;

import com.devin.bangsheng.trans.wuliu.msg.AbstractWlResponse;
import com.devin.bangsheng.trans.wuliu.msg.WlBody;

public class UserLoginResp extends AbstractWlResponse{
	
	public static class UserLoginRespWlBody extends WlBody {
		private String employname;
		private String netcode;
		private String netname;
		private String mobile;
		private String rolename;
		public String getEmployname() {
			return employname;
		}
		public void setEmployname(String employname) {
			this.employname = employname;
		}
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
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getRolename() {
			return rolename;
		}
		public void setRolename(String rolename) {
			this.rolename = rolename;
		}
		
	}

	@Override
	public Class getBodyClass() {
		return UserLoginRespWlBody.class;
	}
}
