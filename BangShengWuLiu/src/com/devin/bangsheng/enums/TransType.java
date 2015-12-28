package com.devin.bangsheng.enums;

import java.util.HashMap;
import java.util.Map;

public enum TransType {
	UNKNOW("-99", "未知交易"),
	SIGN("00", "签到"),
	SETTLE("02", "结算"),
	BALANCE("03", "余额查询"),
	CONSUME("22", "消费"),
	CANCEL("23", "撤销"),
	LAST_TRANS_QUERY("58", "末笔交易查询"),
	LAST_TRANS_PRINT("A0", "末笔打印");
	
	private String code;
	private String name;
	private TransType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}
	
	static Map<String, TransType> transTypeMap = new HashMap<String, TransType>();
	static {
		TransType[] transTypes = TransType.values();
		for(TransType transType : transTypes) {
			transTypeMap.put(transType.code, transType);
		}
	}
	
	public static TransType parseTransTypeFromCode(String code) {
		TransType transType;
		try{
			transType = transTypeMap.get(code);
			if(transType == null) transType = TransType.UNKNOW;
		}catch(Exception e) {
			transType = TransType.UNKNOW;
		}
		return transType;
	}
	
	public static String getTransName(String code) {
		return parseTransTypeFromCode(code).getName();
	}
}
