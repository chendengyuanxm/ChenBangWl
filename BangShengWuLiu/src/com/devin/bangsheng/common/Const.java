package com.devin.bangsheng.common;


public class Const {
	/**物流后台url*/
//	public static String wlUrl = "http://180.96.11.17/njcbdmp/liandi/";	//生产环境
//	public static String wlUrl = "http://njcb.wicp.net:81/dmp/liandi/";	//测试环境
	public static String wlUrl = "http://njcb.wicp.net:81/dmp/newland/";	//新测试环境
	public static String wlVersion = "1.0";
	public static String wlPrivateKey = "135b8ce675a916879d3dc9a259088a9b";
	
	/**银石后台*/
	//8583金融报文发送后台
//	public static String ysUrl = "58.213.110.146";
//	public static String ysUrl = "10.113.11.57";
//	public static int ysPort = 6912;
	public static String ysUrl = "192.168.23.1";
	public static int ysPort = 7777;
	//XML报文发送后台
//	public static String ysXmlUrl = "10.113.11.57";
	public static String ysXmlUrl = "58.213.110.146";
	public static int ysXmlPort = 5960;
	
	public static final String APP_ROOT = "/sdcard/chenbang";
	
	public static class SpConst {
		public static String SP_NAME = "bangsheng";
		public static String posTraceDate = "posTraceDate";
		public static String posTraceNo = "posTraceNo";
		public static String posTraceReNo = "posTraceReNo";
		public static String userName = "userName";
		public static String isCheck = "isCheck";
	}
	
	public static class SettingConst {
//		public static String employNo = "test3";
//		public static String termId = "12345678";
//		public static String employNo = "cbxjy";
//		public static String employNo = "tese2";
//		public static String employNo = "chen";
//		public static String termId = "12345678";
		public static String passwd = "123";
//		public static String employNo = "xdl";
//		public static String termId = "12345678";
//		public static String passwd = "xdl";
		public static String mrchNo = "";
	}
	
	public static class ExtraConst {
		public static String ORDER = "order";
		public static String ORDERS = "orders";
		public static String ORDER_NO = "orderNo";
		public static String ORDER_STATUS = "orderStatus";
		public static String ADDRESS = "address";
		public static String ERRCODE = "errCode";
		public static String ERRMSG = "errMsg";
		public static String AMOUNT = "amount";
		public static String CARD_NO = "cardNo";
		public static String BALANCE = "balance";
		public static String TRANS_TYPE = "transType";
		public static String ORDER_RSLTS = "orderRslts";
	}
	
}
