package com.devin.framework.tcp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReqField {

	public enum ParamType {
		URL, HEADER, BODY;
	}

	public ParamType tag();

	/**
	 * 交易名称
	 * */
	public String paramName();	

	public String declare() default "";
}
