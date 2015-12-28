package com.devin.bangsheng.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devin.bangsheng.R;

public class ScanningDialog {
	/**
	 * 得到自定义的扫描dialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog create(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.radar_scannig_dialog, null);// 得到加载view
//		ImageView im_scan = (ImageView) v.findViewById(R.id.im_scan);
//		ImageView im_dian = (ImageView) v.findViewById(R.id.im_dian);
//		TextView tv_msg = (TextView) v.findViewById(R.id.tv_msg);
//		tv_msg.setText(msg);
//		RotateAnimation animation = new RotateAnimation(0, 360,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		animation.setInterpolator(new LinearInterpolator());
//		animation.setDuration(2000);
//		animation.setRepeatCount(Animation.INFINITE);
//		im_scan.startAnimation(animation);
//
//		AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
//		animation2.setDuration(1000);
//		animation2.setRepeatCount(Animation.INFINITE);
//		im_dian.startAnimation(animation2);

		Dialog scanningDialog = new Dialog(context, R.style.options_dialog);// 创建自定义样式dialog

//		scanningDialog.setCancelable(false);// 不可以用“返回键”取消
//		scanningDialog.setContentView(v, new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return scanningDialog;

	}
}
