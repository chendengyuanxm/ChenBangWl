package com.devin.bangsheng.trans.wuliu.msg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReqField {
	/**
	 * 别名
	 */
	public String alias() default "";
	/**
	 * 顺序
	 */
	public int sort();
}
