package com.devin.bangsheng.enums;

public enum PayWay {
	NO_LIMIT("00", "无限制"),
	CASH("01", "现金"),
	CARD("02", "刷卡");
	
	private String code;
	private String desc;
	private PayWay(String code, String desc) {
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
	
	public static PayWay getPaywayByCode(String code) {
		PayWay[] ways = PayWay.values();
		for(int i = 0; i < ways.length; i ++) {
			if(ways[i].getCode().equals(code)) {
				return ways[i];
			}
		}
		
		return PayWay.NO_LIMIT;
	}
}
