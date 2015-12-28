package com.devin.bangsheng.bean;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8680500802080228424L;
	private String employNo;
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
	public String getEmployNo() {
		return employNo;
	}
	public void setEmployNo(String employNo) {
		this.employNo = employNo;
	}
	
}
