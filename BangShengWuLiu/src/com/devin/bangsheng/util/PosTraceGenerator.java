package com.devin.bangsheng.util;

import android.content.SharedPreferences;

import com.devin.bangsheng.BaseApplication;
import com.devin.bangsheng.common.Const;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.util.DvAppUtils;
import com.devin.framework.util.DvDateUtils;

public class PosTraceGenerator {
	
	private static PosTraceGenerator instance;
	private static String IMEI;
	private String lastDate;
	private static int N = 0;	//流水序号,4位
	private static int M = 0;	//流水纠正位,2位
	
	public static PosTraceGenerator getInstance() {
		if(instance == null) {
			instance = new PosTraceGenerator();
		}
		return instance;
	}
	
	private PosTraceGenerator() {
		IMEI = DvAppUtils.getIMEI(BaseApplication.getInstance());
	}
	
	public void init() {
		SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Const.SpConst.SP_NAME, 0);
		N = sp.getInt(Const.SpConst.posTraceNo, 0);
		M = sp.getInt(Const.SpConst.posTraceReNo, 0);
		lastDate = sp.getString(Const.SpConst.posTraceDate, null);
		String date = DvDateUtils.getCurrentDate("yyyyMMdd");
		if(lastDate != null && !date.equals(lastDate)) {	//不是当天，流水序号重置
			N = 0;
			M = 0;
			sp.edit()
			.putInt(Const.SpConst.posTraceReNo, M)
			.putString(Const.SpConst.posTraceDate, date)
			.commit();
		}else {
			N = 0;
			M ++;
			sp.edit()
			.putInt(Const.SpConst.posTraceReNo, M)
			.putString(Const.SpConst.posTraceDate, date)
			.commit();
		}
	}
	
	public synchronized String generateTrace() {
		String date = DvDateUtils.getCurrentDate("yyyyMMdd");
		if(!date.equals(lastDate)) init();
		String traceNo = IMEI + date + format(M, 2) + format(N++, 4);
		LoggerFactory.getLogger(PosTraceGenerator.class).debug("生成流水号：" + traceNo);
		return traceNo;
	}
	
	private String format(int n, int bit) {
		String str = "";
		if(String.valueOf(n).length() < bit) {
			int paddingL = bit - String.valueOf(n).length();
			for(int i = 0; i < paddingL; i ++) {
				str += "0";
			}
			str += n;
		}else {
			str = String.valueOf(n).substring(0, bit);
		}
		
		return str;
	}
}
