package com.devin.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DvStrUtils {
    
    /**
     * 描述：将null转化为“”.
     *
     * @param str 指定的字符串
     * @return 字符串的String类型
     */
    public static String parseEmpty(String str) {
        if(str==null || "null".equals(str.trim())){
        	str = "";
        }
        return str.trim();
    }
    
    /**
     * 描述：判断一个字符串是否为null或空值.
     *
     * @param str 指定的字符串
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }
    
     /**
 	 * 检测手机号是否合法 </br>含有空值和长度少于11位
 	 * 
 	 * @param number手机号码
 	 * @return true 合法 false 不合法
 	 */
 	public static boolean checkPhoneNumber(String number) {
 		if (number.equals("") || number.length() != 11) {
 			return false;
 		}
 		Pattern pattern = Pattern
 				.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[6-8])|(14[5,7]))\\d{8}$");
 		Matcher matcher = pattern.matcher(number);
 		if (matcher.matches()) {
 			return true;
 		} else {
 			return false;
 		}
 	}
 	
 	/**
	 * 校验email地址的合法性
	 * 
	 * @param emailString
	 *            电子邮箱地址
	 * @return true 合法 false 不合法
	 */
	public static boolean checkEmailAddress(String emailString) {
		String regEx = "\\w+([-+._]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

		Matcher matcherObj = Pattern.compile(regEx).matcher(emailString);

		if (matcherObj.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验身份证号的合法性 <br>
	 * （现在仅检查长度，已经包含内容合法性，不检验真实性）
	 * 
	 * @param idCard
	 *            身份证号
	 * 
	 * @return true 合法 false 不合法
	 */
	public static boolean checkIdCard(String idCard) {
		String regEx = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
		Matcher matcherObj = Pattern.compile(regEx).matcher(idCard);

		if (matcherObj.matches()) {
			if (idCard.length() == 15) {
				// 如果是15位身份证号，则认为验证成功。
				return true;
			}

			// 如果是18位身份证号，现在计算校验码是否正确。
			int sigma = 0;
			// 系统数表
			Integer[] coeTable = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
					8, 4, 2 };
			// 校验码表
			String[] codeTable = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
					"3", "2" };

			// 将身份证每一位乘以系数表中的系数，结果相加。
			for (int i = 0; i < 17; i++) {
				int ai = Integer.parseInt(idCard.substring(i, i + 1));
				int wi = coeTable[i];
				sigma += ai * wi;
			}
			// 结果取 11 的余数
			int number = sigma % 11;
			// 使用余数做索引，取校验码。
			String check_number = codeTable[number];
			if (idCard.substring(17).equalsIgnoreCase(check_number)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 检测密码强度
	 * 
	 * @param password
	 * @return 好：GOOD 一般：GENERAL 坏：BAD
	 */
	public static String checkPWLevel(String password) {
		String pwLevel = null;
		int count = 0;
		if (Pattern.compile("(?i)[a-zA-Z]").matcher(password).find()) {
			count += 10;
		}
		if (Pattern.compile("(?i)[0-9]").matcher(password).find()) {
			count += 10;
		}
		if (Pattern
				.compile(
						"(?i)[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]")
				.matcher(password).find()) {
			count += 10;
		}
		if (count == 10) {
			pwLevel = "BAD";
		} else if (count == 20) {
			pwLevel = "GENERAL";
		} else if (count == 30) {
			pwLevel = "GOOD";
		}
		return pwLevel;
	}
 	
 	/**
	  * 描述：是否是中文.
	  *
	  * @param str 指定的字符串
	  * @return  是否是中文:是为true，否则false
	  */
    public static boolean isChinese(String str) {
    	Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if(!isEmpty(str)){
	         //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
	         for (int i = 0; i < str.length(); i++) {
	             //获取一个字符
	             String temp = str.substring(i, i + 1);
	             //判断是否为中文字符
	             if (temp.matches(chinese)) {
	             }else{
	            	 isChinese = false;
	             }
	         }
        }
        return isChinese;
    }
    
    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return  是否包含中文:是为true，否则false
     */
    public static boolean isContainChinese(String str) {
    	Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if(!isEmpty(str)){
	         //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
	         for (int i = 0; i < str.length(); i++) {
	             //获取一个字符
	             String temp = str.substring(i, i + 1);
	             //判断是否为中文字符
	             if (temp.matches(chinese)) {
	            	 isChinese = true;
	             }else{
	            	 
	             }
	         }
        }
        return isChinese;
    }
    
    /**
     * 获取字符串中文字符的长度（每个中文算2个字符）.
     *
     * @param str 指定的字符串
     * @return 中文字符的长度
     */
    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        if(!isEmpty(str)){
	        for (int i = 0; i < str.length(); i++) {
	            /* 获取一个字符 */
	            String temp = str.substring(i, i + 1);
	            /* 判断是否为中文字符 */
	            if (temp.matches(chinese)) {
	                valueLength += 2;
	            }
	        }
        }
        return valueLength;
    }
    
    /**
     * 描述：获取字符串的长度.
     *
     * @param str 指定的字符串
     * @return  字符串的长度（中文字符计2个）
     */
     public static int strLength(String str) {
         int valueLength = 0;
         String chinese = "[\u0391-\uFFE5]";
         if(!isEmpty(str)){
	         //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
	         for (int i = 0; i < str.length(); i++) {
	             //获取一个字符
	             String temp = str.substring(i, i + 1);
	             //判断是否为中文字符
	             if (temp.matches(chinese)) {
	                 //中文字符长度为2
	                 valueLength += 2;
	             } else {
	                 //其他字符长度为1
	                 valueLength += 1;
	             }
	         }
         }
         return valueLength;
     }
     
     /**
      * 描述：获取指定长度的字符所在位置.
      *
      * @param str 指定的字符串
      * @param maxL 要取到的长度（字符长度，中文字符计2个）
      * @return 字符的所在位置
      */
     public static int subStringLength(String str,int maxL) {
    	 int currentIndex = 0;
         int valueLength = 0;
         String chinese = "[\u0391-\uFFE5]";
         //获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
         for (int i = 0; i < str.length(); i++) {
             //获取一个字符
             String temp = str.substring(i, i + 1);
             //判断是否为中文字符
             if (temp.matches(chinese)) {
                 //中文字符长度为2
                 valueLength += 2;
             } else {
                 //其他字符长度为1
                 valueLength += 1;
             }
             if(valueLength >= maxL){
            	 currentIndex = i;
            	 break;
             }
         }
         return currentIndex;
     }
 	
 	/**
	  * 描述：从输入流中获得String.
	  *
	  * @param is 输入流
	  * @return 获得的String
	  */
 	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			//最后一个\n删除
			if(sb.indexOf("\n")!=-1 && sb.lastIndexOf("\n") == sb.length()-1){
				sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n")+1);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
 	
	/**
	 * 获取大小的描述.
	 *
	 * @param size 字节个数
	 * @return  大小的描述
	 */
    public static String getSizeDesc(long size) {
	   	 String suffix = "B";
	   	 if (size >= 1024){
			suffix = "K";
			size = size>>10;
			if (size >= 1024){
				suffix = "M";
				size = size>>10;
				if (size >= 1024){
	    		        suffix = "G";
	    		        size = size>>10;
		        }
			}
	   	}
        return size+suffix;
    }
    
    /**
	 * 整形待分割符字符串比对
	 * 
	 * @param dist
	 *            "3.5.8"
	 * @param targetVersion
	 *            "3.5.7"
	 * @return "3.5.8">"3.5.7" "3.5.10">"3.5.7" ...
	 */
	public static boolean strCompare(String dist, String targetVersion) {

		String[] osVersions = dist.split("\\.");
		String[] targetVersions = targetVersion.split("\\.");
		int osLength = osVersions.length;
		int tarLength = targetVersions.length;
		int length = Math.min(osLength, tarLength);
		int isNew = 0;

		for (int i = 0; i < length; i++) {
			int version = Integer.parseInt(osVersions[i]);
			int target = Integer.parseInt(targetVersions[i]);
			if (version > target) {
				isNew = version - target;
				break;
			} else if (version < target) {
				isNew = version - target;
				break;
			}
		}

		if ((isNew == 0) && (osLength > tarLength)) {
			isNew = osLength - tarLength;
		}
		return isNew > 0;
	}
	
	/**
	 * 替换字符串所有空格
	 * 
	 * @param str
	 * @return
	 * @version 1.0
	 */
	public static String replaceBlank(String str) {
		  String dest = "";
		  if (str!=null) {
		   Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		   Matcher m = p.matcher(str);
		   dest = m.replaceAll("");
		  }
		  return dest;
	}
	
}
