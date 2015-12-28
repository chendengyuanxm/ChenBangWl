package com.devin.framework.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.widget.Toast;

import com.devin.framework.R;
/**
 * 定义一些跟UI操作的帮助类
 *
 * @author Devin_chen
 * @date 2015年3月8日
 * 
 * @version 1.0
 */
public class UIHelper {
	
	/**
	 * 创建一个Dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param pText
	 * @param pListerner
	 * @param nText
	 * @param nListerner
	 * @return
	 * @version 1.0
	 */
	public static Dialog createDialog(Context context, String title, String message
			, String pText, OnClickListener pListerner, String nText, OnClickListener nListerner) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		Dialog dialog = builder
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(pText, pListerner)
				.setNegativeButton(nText, nListerner)
				.create();
		
		return dialog;
	}
	
	public static Dialog createDialog(Context context, int titleId, int messageId
			, int pTextId, OnClickListener pListerner, int nTextId, OnClickListener nListerner) {
		Resources res = context.getResources();
		String title = titleId == -1 ? null : res.getString(titleId);
		String message = res.getString(messageId);
		String pText = res.getString(pTextId);
		String nText = res.getString(nTextId);
		
		return createDialog(context, title, message, pText, pListerner, nText, nListerner);
	}
	
	public static Dialog createDialog(Context context, String message) {
		return createDialog(context, null, message, "确定", null, null, null);
	}
	
	public static Dialog createDialog(Context context, String message, OnClickListener pListerner) {
		return createDialog(context, null, message, "确定", pListerner, null, null);
	}
	
	public static void showDialog(Context context, String title, String message
			, String pText, OnClickListener pListerner, String nText, OnClickListener nListerner) {
		createDialog(context, title, message, pText, pListerner, nText, nListerner).show();
	}
	
	public static void showDialog(Context context, String message
			, OnClickListener pListerner, OnClickListener nListerner) {
		createDialog(context, null, message, "确定", pListerner, "取消", nListerner).show();
	}
	
	public static void showDialog(Context context, String message) {
		createDialog(context, null, message, "确定", null, "取消", null).show();
	}
	
	public static void showDialog(Context context, String message, OnClickListener pListerner) {
		createDialog(context, null, message, "确定", pListerner, null, null).show();
	}
	
	public static void showDialog(Context context, int titleId, int messageId
			, int pTextId, OnClickListener pListerner, int nTextId, OnClickListener nListerner) {
		createDialog(context, titleId, messageId, pTextId, pListerner, nTextId, nListerner).show();
	}
	
	public static void showDialog(Context context, int messageId
			, OnClickListener pListerner, OnClickListener nListerner) {
		createDialog(context, -1, messageId, R.string.dialog_confirm, pListerner, R.string.dialog_cancel, nListerner).show();
	}
	
	public static void showDialog(Context context, int messageId) {
		createDialog(context, -1, messageId, R.string.dialog_confirm, null, R.string.dialog_cancel, null).show();
	}
	
	public static void showDialog(Context context, int messageId, OnClickListener pListerner) {
		createDialog(context, -1, messageId, R.string.dialog_confirm, pListerner, R.string.dialog_cancel, null).show();
	}
	
	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public static void showToast(Context context, int messageId) {
		Toast.makeText(context, messageId, Toast.LENGTH_LONG).show();
	}
	
	public static ProgressDialog createProgressbar(Context context, String message) {
		ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage(message);
		
		return pd;
	}
}
