package com.devin.framework.tcp;

import com.devin.framework.base.CommonException;
import com.devin.framework.base.ExCode;

public class HttpException extends CommonException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4120351264160105653L;
	
	public HttpException(int code, String msg) {
		super(code, msg);
	}
	
	public HttpException(String msg) {
		super(ExCode.HTTP_STATUS_ERROR, msg);
	}
	
	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}
}
