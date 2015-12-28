package com.devin.framework.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public class DvBankUtils {
	
	/**
	 * 检查金额是否有效
	 * 
	 * @param amountString
	 *            金额
	 * @return
	 */
	public static boolean isAmountVaild(String amountString) {
		if (amountString != null && amountString.length() > 0) {
			double amount = 0.0;
			try {
				amount = Double.parseDouble(amountString);
			} catch (Exception e) {
				// Util.toast(R.string.toast_money_format_error);
				return false;
			}
			// 金额必须大于0
			if (amount > 0.001) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断金额是否有效
	 * 
	 * @param amountString
	 *            输入的金额
	 * @param max
	 *            该业务限制的最大金额
	 * @return true，false
	 */
	public static boolean isAmountVaild(String amountString, double max) {
		if (isAmountVaild(amountString)) {
			try {
				double amount = Double.parseDouble(amountString);
				if (amount > max) {
					return false;
				} else {
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	/**
	 * BCD格式的金额 转 BigDecimal格式的金额 示例：000000000111 转 1.11
	 * 
	 * @param bcdAmt
	 * @return
	 */
	public static BigDecimal amtToBigDecimal(String bcdAmt) {
		BigDecimal bigDecimal = new BigDecimal(bcdAmt);
		bigDecimal.setScale(2, RoundingMode.HALF_UP);
		bigDecimal = bigDecimal.divide(new BigDecimal("100"), 2,
				RoundingMode.HALF_UP);
		return bigDecimal;
	}
	
	/**
	 * 校验银行卡卡号(借记卡)
	 * 
	 * @param cardId
	 * @return false 非法卡号 true 适合合法卡号
	 */
	public static boolean checkDebitCard(String cardId) {
		char bit = getDebitCardCheckCode(cardId.substring(0,
				cardId.length() - 1));
		if (bit == 'f') {
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeCardId
	 *            不含校验位的银行卡卡号
	 * @return char 校验位
	 */
	private static char getDebitCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("^[0-9]+$")) {

			return 'f';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	/**
	 * 校验信用卡卡号是否为正确卡号（防止输入错误卡号） Luhn 算法校验
	 * 
	 * @param cardNumber
	 *            信用卡卡号
	 * @return false 非法卡号 true 适合合法卡号
	 */
	public static boolean checkCreditCard(String cardNumber) {
		String digitsOnly = getDigitsOnly(cardNumber);
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;

		for (int i = digitsOnly.length() - 1; i >= 0; i--) {
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
			if (timesTwo) {
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;
				}
			} else {
				addend = digit;
			}
			sum += addend;
			timesTwo = !timesTwo;
		}

		int modulus = sum % 10;
		return modulus == 0;
	}
	
	/**
	 * 诊断卡号是否合法
	 * 
	 * @param cardNumber
	 * @return
	 */
	public static boolean checkCardNumber(String cardNumber) {
		if (cardNumber != null && cardNumber.length() >= 13
				&& cardNumber.length() <= 19) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 过滤掉非数字字符
	 * 
	 * @param s
	 *            字符串
	 * @return 过滤后卡号字符
	 */
	private static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}
	
	/**
	 * 格式化银行卡号为 XXXX XXXX XXXX XXXXXXX
	 * 
	 * @param bankAccNo
	 * @return
	 * @version 1.0
	 */
	public static String formatBankNo(String bankAccNo) {
		if (DvStrUtils.isEmpty(bankAccNo))
			return "";

		String temp0 = bankAccNo;
		String temp = temp0.replaceAll(" ", "");
		StringBuilder sb = new StringBuilder();
		int len = temp.length();
		for (int i = 0; i < len; i++) {
			if (i == 4 || i == 8 || i == 12)
				sb.append(" ");
			sb.append(temp.charAt(i));
		}

		return sb.toString();
	}

	/**
	 * 获取银行卡后几位字符串
	 * 
	 * @param bankNo
	 * @param digit
	 *            后多少位
	 * @return
	 * @version 1.0
	 */
	public static String getBankTailNo(String bankNo, int digit) {
		int len = bankNo.length();
		return bankNo.substring(len - digit, len);
	}
	
	/**
	 * 金额格式化
	 * 
	 * @param s
	 *            金额
	 * @return 格式后的金额(###,###.##)
	 */
	public static String formatAmount(String s) {
		return formatAmount(s, false);
	}

	/**
	 * 金额格式化
	 * 
	 * @param s
	 *            金额
	 * @param isInitNull
	 *            是否初始化为空字符
	 * @return 格式后的金额(###,###.##)
	 */
	public static String formatAmount(String s, boolean isInitNull) {
		String result = "";
		if (DvStrUtils.isEmpty(s))
			return "";
		try {
			String temp = s;
			s = formatString(s);// 去除string可能的分隔符
			double num = 0.0;
			try {
				num = Double.parseDouble(s);
			} catch (NumberFormatException e) {
			}
			if (num == 0) {
				if (isInitNull) {
					return "";
				} else {
					return "0.00";
				}
			}
			if (num < 1) {// 小于1情况特殊处理
				if (s.length() == 4) {// 0.05
					return temp;
				} else if (s.length() == 3) {// 0.5
					return temp + "0";
				}
			}
			NumberFormat formater = new DecimalFormat("##.00");
			result = formater.format(num);
		} catch (Exception e) {
		}
		if (result.startsWith(".")) {
			result = "0" + result;
		}
		return result;
	}
	
	/**
	 * 格式化卡号，前6后4，中间根据位数以星号显示
	 */
	public static String formatCardNumberWithStar(String cardNumber) {

		return formatCardNumberWithStar(cardNumber, "*");
	}

	/**
	 * 格式化卡号，前6后4，中间根据位数以星号显示
	 * 
	 * @param cardNumber
	 *            卡号
	 * @param interval
	 *            分隔符
	 */
	public static String formatCardNumberWithStar(String cardNumber,
			String interval) {
		if (!checkCardNumber(cardNumber)) {
			return cardNumber;
		}
		int length = cardNumber.length();
		StringBuilder builder = new StringBuilder();
		builder.append(cardNumber.substring(0, 4));
		builder.append(" **** ");
		builder.append(cardNumber.substring(length - 4, length));

		return builder.toString();
	}
	
	public static String formatCardNumberWith4Star(String cardNumber) {
		return formatCardNumberWith4Star(cardNumber, "*");
	}

	/**
	 * 格式化卡号，前6后4，中间根据4位数分隔符显示
	 * 
	 * @param cardNumber
	 *            卡号
	 * @param interval
	 *            分隔符
	 */
	public static String formatCardNumberWith4Star(String cardNumber,
			String interval) {
		int length = cardNumber.length();
		if(length < 10) 
			return cardNumber;
		StringBuilder builder = new StringBuilder();
		builder.append(cardNumber.substring(0, 6));
		int middle = length -6 - 4;
		for (int i = 0; i < middle; i++) {
			builder.append(interval);
		}
		builder.append(cardNumber.substring(length - 4, length));

		return builder.toString();
	}
	
	/**
	 * 去除字符中间的 "空格/-/," 等间隔符
	 * 
	 * @param string
	 *            要格式化的字符
	 * @return 格式化后的字符
	 */
	public static String formatString(String string) {
		if (string == null)
			return "";
		String newString = string.replaceAll(" ", "").replaceAll("-", "")
				.replaceAll(",", "");
		return newString;
	}
	
	/**
	 * 将分转换为元,带两个小数点
	 * 
	 * @param fenStr
	 * @return
	 */
	public static String fen2Yuan(String fenStr) {

		if (fenStr == null)
			return "0.00";

		// 过滤非数字的情况，服务端可能返回“null”字符串，此时返回“0”
		boolean isAllDigit = Pattern.matches("[\\d.]+", fenStr);
		if (!isAllDigit) {
			return "0.00";
		}
		String yuanString = new BigDecimal(fenStr)
				.divide(new BigDecimal("100"))
				.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		return yuanString;
	}

	/**
	 * 将金额由元转换为分
	 * 
	 * @param yuanStr
	 * @return
	 */
	public static String yuan2Fen(String yuanStr) {
		if (DvStrUtils.isEmpty(yuanStr)) {
			return "0";
		}
		yuanStr = formatString(yuanStr);
		String fenString = new BigDecimal(yuanStr)
				.multiply(new BigDecimal("100")).setScale(0).toString();
		return fenString;
	}
}
