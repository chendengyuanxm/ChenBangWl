package com.devin.bangsheng.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

	public static String digest(byte[] data){
		StringBuffer sb=new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(data);
			byte[] result=digest.digest();
			for (int i = 0; i < result.length; i++) {
				int hi=((result[i]>>4)&0x0f);
				int lo=(result[i]&0x0f);
				sb.append(hi>9?(char)((hi-10)+'a'):(char)(hi+'0'));
				sb.append(lo>9?(char)((lo-10)+'a'):(char)(lo+'0'));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return sb.toString().toUpperCase();
	}

	public static void main(String[] args) {
		String a = "123456";
		System.out.println(Md5.digest(a.getBytes()));
	}

}
