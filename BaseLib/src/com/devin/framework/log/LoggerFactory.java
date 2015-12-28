package com.devin.framework.log;

import android.util.Log;

import com.devin.framework.base.RunningModel;

/**
 * 日志工厂<p>
 * 
 * TODO
 * 
 * @author lance
 *
 */
public class LoggerFactory {
	
	public static final Logger getLogger(final String className){
		return new Logger() {
			
			private String TAG = "lDevin";
			
			@Override
			public void warn(String msg) {
				if(RunningModel.isDebugEnabled)
					Log.w(TAG,msg);
			}
			
			@Override
			public void warn(String msg, Throwable e) {
				if(RunningModel.isDebugEnabled)
					Log.w(TAG, msg,e);
			}
			
			@Override
			public void info(String msg, Throwable e) {
				if(RunningModel.isDebugEnabled)
					Log.i(TAG, msg,e);
			}
			
			@Override
			public void info(String msg) {
				if(RunningModel.isDebugEnabled)
					Log.i(TAG, msg);
			}
			
			@Override
			public void error(String msg) {
				Log.e(TAG, msg);
			}
			
			@Override
			public void error(String msg, Throwable e) {
				Log.e(TAG, msg,e);
			}
			
			@Override
			public void debug(String msg, Throwable e) {
				if(RunningModel.isDebugEnabled)
					Log.d(TAG, msg,e);
			}
			
			@Override
			public void debug(String msg) {
				if(RunningModel.isDebugEnabled)
					Log.d(TAG, msg);
			}
		};
	}
	
	public static final Logger getLogger(Class clazz){
		return getLogger(clazz.getName());
	}

}
