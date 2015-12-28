package com.devin.framework.log;

/**
 * 标准设备日志实现类<p>
 * 
 * @author lance
 *
 * @since ver1.0
 */
public interface Logger {
	
	public void error(String msg,Throwable e);
	
	public void warn(String msg,Throwable e);
	
	public void error(String msg);
	
	public void warn(String msg);
	
	public void info(String msg);
	
	public void info(String msg,Throwable e);
	
	public void debug(String msg);
	
	public void debug(String msg,Throwable e);
}
