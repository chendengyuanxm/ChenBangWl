package com.devin.bangsheng.enums;

public enum OrderStatus {
	UNKNOW("-1", "未知状态"),
	ONWAY("01", "途中"),
	DELIVER("02", "已分配派送员"),
	SIGNED("03", "已签收"),
	BAD_ORDER("04", "问题件"),
	REFUSE("05", "退回");
	
	private String code;
	private String desc;
	private OrderStatus(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static OrderStatus getStatusByCode(String code) {
		OrderStatus[] status = OrderStatus.values();
		for(int i = 0; i < status.length; i ++) {
			if(status[i].getCode().equals(code)) {
				return status[i];
			}
		}
		
		return OrderStatus.UNKNOW;
	}
}
