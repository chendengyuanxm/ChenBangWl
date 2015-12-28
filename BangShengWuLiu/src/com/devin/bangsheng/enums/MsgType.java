package com.devin.bangsheng.enums;

public enum MsgType {
	LOGIN(1, "P001", "登陆"),
	ORDER_SIGN(3, "P003", "派件签收"),
	ORDER_QUERY(4, "P004", "运单查询"),
	BAD_ORDER_SIGN(5, "P005", "问题件登记"),
	ORDER_SIGN_REVERS(6, "P006", "签收撤销"),
	ORDERS_QUERY(7, "P007", "获取任务"),
	ORDER_CANCEL(9, "P009", "投递任务标记取消"),
	ORDER_STATUS_QUERY(8, "P008", "配送状态查询"),
	ORDER_UPLOAD(10, "P010", "接收到的任务上送"),
	SETTLE(10, "P010", "结算汇总"),
	APP_UPDATE(11, "P011", "软件更新"),
	GPS_UPLOAD(12, "P012", "GPS信息上传"),
	
	CARD_CONSUME_NOTIFY(13, "P013", "刷卡缴费通知"),
	CASH_CONSUME_NOTIFY(14, "P014", "现金缴费通知");
	
	int id;
	String code;
	String desc;
	
	private MsgType(int id, String code, String desc) {
		this.id = id;
		this.code = code;
		this.desc = desc;
	}
	
	public int getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDesc() {
		return desc;
	}
}
