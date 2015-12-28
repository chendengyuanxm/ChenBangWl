package com.devin.framework.base;


public class CommonException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4480516009579124461L;
	
	public int code;
	
	public CommonException(int code, String msg, Throwable e) {
		super(msg, e);
		this.code = code;
	}
	
	public CommonException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	@Override
	public String getLocalizedMessage() {
		return "["+code+"]" + super.getLocalizedMessage();
	}
}
