package com.devin.bangsheng.trans.wuliu.msg;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public abstract class XmlBean implements Serializable{
	
//	public String toXml() {
//		StringBuilder sb = new StringBuilder();
//		XStreamAlias xs = getClass().getAnnotation(XStreamAlias.class);
//		String root = xs == null ? getClass().getSimpleName() : xs.value();
//		sb.append("<" + root + ">");
//		Field[] fields = getClass().getDeclaredFields();
////		Field[] fields = sort(tempFields);
//		for(Field f : fields) {
//			ReqField reqFieldAnno = f.getAnnotation(ReqField.class);
//			XStreamAlias xStreamAlias = f.getAnnotation(XStreamAlias.class);
//			if (reqFieldAnno != null) {
//				try {
//					String key = xStreamAlias != null ? xStreamAlias.value() : f.getName();
//					String value;
//					f.setAccessible(true);
//					Object o = f.get(this);
//					if(o == null) 
//						continue;
//					if(o instanceof XmlBean) {
//						value = ((XmlBean)o).toXml();
//						sb.append(value);
//					}else{
//						value = String.valueOf(o);
//						sb.append("<" + key + ">");
//						sb.append(value);
//						sb.append("</" + key + ">");
//					}
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}
//		sb.append("</" + root + ">");
//		
//		return sb.toString();
//	}
	
	private Field[] sort(Field[] fields) {
		int position = 0;
		for(int i = 1; i < fields.length-1; i ++) {
			Field f = fields[i];
			ReqField reqFieldAnno = f.getAnnotation(ReqField.class);
			int temp = reqFieldAnno.sort();
			int j = i+1;
			for(; j<fields.length; j++) {
				Field f1 = fields[j];
				ReqField reqFieldAnno1 = f1.getAnnotation(ReqField.class);
				int jTemp = reqFieldAnno1.sort();
				if(jTemp < temp) {
					f = fields[j];
					position = j;
				}
				fields[position] = f;
				fields[i] = f;
			}
		}
		
		return fields;
	}
}
