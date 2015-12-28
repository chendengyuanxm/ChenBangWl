package com.devin.bangsheng;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.net.ParseException;
import android.os.IBinder;
import android.os.Message;

import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.db.PayColumn;
import com.devin.bangsheng.db.biz.PayDB;
import com.devin.bangsheng.enums.PayWay;
import com.devin.bangsheng.service.YsTransferService;
import com.devin.bangsheng.trans.yinshi.msg.impl.ConsumeNotifyResponse;
import com.devin.framework.base.CommonException;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.tcp.SimpleSocketTransfer;

public class MainService extends Service{
	private Logger logger = LoggerFactory.getLogger(MainService.class);
	private static final long MAX_CACHE = 10*1024*1024;	//最大缓存文件大小 
	private static MainService instance;
	private SimpleSocketTransfer defaultSocketTransfer;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public static MainService getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		logger.info("main service is starting...");
		instance = this;
		defaultSocketTransfer = new SimpleSocketTransfer(this);
		startTransMonitor();
		startFileCacheMonitor();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		defaultSocketTransfer.unregister(this);
	}
	
	/**
	 * 每10分钟执行后台上送未提交交易
	 * 
	 * @version 1.0
	 */
	public void startTransMonitor() {
		Timer timer = new Timer();
		timer.schedule(new ConsumeNotifyTask(), 10000, 180*1000);	
	}
	
	/**
	 * 每小时监控一次文件缓存
	 * 
	 * @version 1.0
	 */
	public void startFileCacheMonitor() {
		Timer timer = new Timer();
		timer.schedule(new FileCacheMonitorTask(), 5000, 7200*1000); 
	}
	
	class FileCacheMonitorTask extends TimerTask {

		@Override
		public void run() {
			logger.info("开始监控缓存文件...");
			String signJbgPath = Const.APP_ROOT + "/temp";
			File folder = new File(signJbgPath);
	        if (!folder.exists()) {
	        	folder.mkdirs();
	        }
	        long total = 0;
	        File[] files = folder.listFiles();
	        for(int i = 0; i < files.length/2; i ++) {
	        	total += files[i].length();
	        }
	        logger.info("total:" + total);
	        logger.info("当前缓存文件夹大小 :" +total);
	        if(total > MAX_CACHE) {	//超过最大缓存大小 ，执行删除文件,删除一半
	        	deleteFileCache(folder);
	        }
		}
	}
	
	private void deleteFileCache(File folder) {
		logger.info("自动删除缓存文件...");
		File[] files = folder.listFiles();
		Arrays.sort(files, new FileComparator());
		for(int i = 0; i < files.length/2; i ++) {
        	files[i].delete();
        }
	}
	
	/**
	 * 去除文件的扩展类型（.log）
	 * @param fileName
	 * @return
	 */
	private String getFileNameWithoutExtension(String fileName){
		return fileName.substring(0, fileName.indexOf("."));
	}
	
	class FileComparator implements Comparator<File>{
		public int compare(File file1, File file2) {
			String createInfo1 = getFileNameWithoutExtension(file1.getName());
			String createInfo2 = getFileNameWithoutExtension(file2.getName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date create1 = sdf.parse(createInfo1);
				Date create2 = sdf.parse(createInfo2);
				if(create1.before(create2)){
					return -1;
				}else{
					return 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	}
	
	class ConsumeNotifyTask extends TimerTask {

		@Override
		public void run() {
			logger.info("开始后台上送消费通知任务...");
			List<Map<String, String>> list = PayDB.getInstance(MainService.this).queryAll();
			logger.debug("消费未通知条数：" + list.size());
			for(int i = 0; i < list.size(); i ++) {
				Map<String, String> map = list.get(i);
				String sysTrace = map.get(PayColumn.SYSTRACE);
				String saveData = map.get(PayColumn.SAVE_DATA);
				logger.debug("第" + i + "条：" + sysTrace);
				logger.debug(saveData);
				try{
					JSONObject jsonObject = new JSONObject(saveData);
					String orderNo = jsonObject.getString("orderNo");
					String posTrace = jsonObject.getString("posTrace");
					String transType = jsonObject.getString("transType");
					String reqTime = jsonObject.getString("reqTime");
					String batchNo = jsonObject.getString("batchNo");
					String pan = jsonObject.getString("pan");
					String rrn = jsonObject.getString("rrn");
					String systrace = jsonObject.getString("systrace");
					String mrchNo = jsonObject.getString("mrchNo");
					String termId = jsonObject.getString("termId");
					String amount = jsonObject.getString("amount");
					String employNo = jsonObject.getString("employNo");
					String payWay = jsonObject.getString("payWay");
					String signflag = jsonObject.getString("signflag");
					String jbgData = jsonObject.getString("jbgData");
					String startDate = jsonObject.getString("startDate");
					String endDate = jsonObject.getString("endDate");
					int live = jsonObject.getInt("live");
					
					if(live > 0) {
						cardConsumeNotify(amount, batchNo, employNo, mrchNo, termId, orderNo, pan, payWay, posTrace, reqTime, rrn, "", signflag, systrace, transType, jbgData, startDate, endDate, --live, false);
					}else {
						PayDB.getInstance(instance).delete(systrace);
					}
				} catch (JSONException e) {
					logger.info("解析JSON失败", e);
				}
			}
		}
	}
	
	public void cardConsumeNotify(String amount, String batchNo
			, String employNo, String mrchNo, String termId, String orderNo, String pan, String payWay
			, String posTrace, String reqTime, String rrn, String signer
			, String signFlag, String systrace, String transType, String jbgData 
			, String startDate, String endDate, int live, boolean first) {
		
		YsTransferService.getInstance().consumeNotify(defaultSocketTransfer, amount+"", batchNo
				, employNo, mrchNo, termId, orderNo, pan, payWay, posTrace, reqTime, rrn, signer
				, signFlag, systrace, transType, jbgData, startDate, endDate);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("posTrace", posTrace);
			jsonObject.put("transType", transType);
			jsonObject.put("reqTime", reqTime);
			jsonObject.put("batchNo", batchNo);
			jsonObject.put("pan", pan);
			jsonObject.put("rrn", rrn);
			jsonObject.put("systrace", systrace);
			jsonObject.put("mrchNo", mrchNo);
			jsonObject.put("termId", termId);
			jsonObject.put("amount", amount);
			jsonObject.put("employNo", employNo);
			jsonObject.put("payWay", payWay);
			jsonObject.put("signflag", signFlag);
			jsonObject.put("jbgData", jbgData);
			jsonObject.put("startDate", startDate);
			jsonObject.put("endDate", endDate);
			jsonObject.put("live", live);
			String consumeData = jsonObject.toString();
			if(first) {	
				PayDB.getInstance(this).insert(systrace, consumeData);
			}else {
				PayDB.getInstance(this).update(systrace, consumeData);
			}
		} catch (JSONException e) {
			logger.info("解析JSON失败", e);
		}
	}
	
	/**
     * EventBus订阅者事件通知的函数, UI线程
     * 
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
    	if(msg.obj instanceof CommonException) {
			CommonException error = (CommonException) msg.obj;
			logger.debug("刷卡缴费失败[" + error.getLocalizedMessage() + "]");
		}else {
			if(msg.obj instanceof ConsumeNotifyResponse) {
				ConsumeNotifyResponse resp = (ConsumeNotifyResponse) msg.obj;
				PayWay payWay = PayWay.getPaywayByCode(resp.getPayWay());
				final String respCode = resp.getRespCode();
				if("00".equals(respCode)) {
					String sysTrace = resp.getSystrace();
					String transType = resp.getTransType();
					PayDB.getInstance(this).delete(sysTrace);	//刷卡缴费通知成功后删除保存记录
					if("01".equals(transType)) {
						logger.info("刷卡缴费成功，成功流水号[" + sysTrace + "]");
					}else {
						logger.info("刷卡缴存成功，成功流水号[" + sysTrace + "]");
					}
				}else {
					logger.debug("刷卡缴费失败[" + respCode + "]");
				}
			}
		}
    }
}
