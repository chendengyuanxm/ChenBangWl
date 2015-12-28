package com.devin.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class DvAppUtils {
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 *            像素值
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return dp值
	 */
	public static int px2dip(float pxValue, Context context) {
		float scale = getDensity(context);
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 *            dip数值
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return 像素值
	 */
	public static int dip2px(float dipValue, Context context) {
		float scale = getDensity(context);
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 *            像素值 （DisplayMetrics类中属性scaledDensity）
	 * @return 返回sp数值
	 */
	public static int px2sp(float pxValue, Context context) {
		float scale = getDensity(context);

		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            sp数值 （DisplayMetrics类中属性scaledDensity）
	 * @return 返回像素值
	 */
	public static int sp2px(float spValue, Context context) {
		float scale = getDensity(context);
		return (int) (spValue * scale + 0.5f);
	}

	/**
	 * 取得手机屏幕的密度
	 * 
	 * @param context
	 *            上下文
	 * @return 手机屏幕的密度
	 */
	public static float getDensity(Context context) {
		float scale = context.getResources().getDisplayMetrics().density;
		return scale;
	}

	/**
	 * 描述：打开并安装文件.
	 *
	 * @param context the context
	 * @param file apk文件路径
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 描述：卸载程序.
	 *
	 * @param context the context
	 * @param packageName 包名
	 */
	public static void uninstallApk(Context context,String packageName) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		Uri packageURI = Uri.parse("package:" + packageName);
		intent.setData(packageURI);
		context.startActivity(intent);
	}


	/**
	 * 用来判断服务是否运行.
	 *
	 * @param ctx the ctx
	 * @param className 判断的服务名字 "com.xxx.xx..XXXService"
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context ctx, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
		Iterator<RunningServiceInfo> l = servicesList.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			if (className.equals(si.service.getClassName())) {
				isRunning = true;
			}
		}
		return isRunning;
	}

	/**
	 * 停止服务.
	 *
	 * @param ctx the ctx
	 * @param className the class name
	 * @return true, if successful
	 */
	public static boolean stopRunningService(Context ctx, String className) {
		Intent intent_service = null;
		boolean ret = false;
		try {
			intent_service = new Intent(ctx, Class.forName(className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (intent_service != null) {
			ret = ctx.stopService(intent_service);
		}
		return ret;
	}
	

	/** 
	 * Gets the number of cores available in this device, across all processors. 
	 * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu" 
	 * @return The number of cores, or 1 if failed to get result 
	 */ 
	public static int getNumCores() { 
		try { 
			//Get directory containing CPU info 
			File dir = new File("/sys/devices/system/cpu/"); 
			//Filter to only list the devices we care about 
			File[] files = dir.listFiles(new FileFilter(){

				@Override
				public boolean accept(File pathname) {
					//Check if filename is "cpu", followed by a single digit number 
					if(Pattern.matches("cpu[0-9]", pathname.getName())) { 
					   return true; 
				    } 
				    return false; 
				}
				
			}); 
			//Return the number of cores (virtual CPU devices) 
			return files.length; 
		} catch(Exception e) { 
			//Default to return 1 core 
			return 1; 
		} 
	} 
	
	
	/**
	 * 描述：判断网络是否有效.
	 *
	 * @param context the context
	 * @return true, if is network available
	 */
	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * Gps是否打开
	 * 需要<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />权限
	 *
	 * @param context the context
	 * @return true, if is gps enabled
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
	    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * wifi是否打开.
	 *
	 * @param context the context
	 * @return true, if is wifi enabled
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 判断当前网络是否是wifi网络.
	 *
	 * @param context the context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否是3G网络.
	 *
	 * @param context the context
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断蓝牙是否打开
	 * @return
	 */
	public static boolean isBluetoothEnable() {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter == null) {
			return false;
		}else{
			return bluetoothAdapter.isEnabled();
		}
	}
	
	/**
	 * 获取当前客户端版本信息
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = -1;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * 获取当前APP版本描述 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
        		>= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

	/**
	 * 获取手机IMEI
	 * 手机的唯一识别号码
	 * @param context
	 * @return
	 * @version 1.0
	 */
	public static String getIMEI(Context context) {
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}
	
	/**
	 * 判断手机是否可以拨打电话
	 * 
	 * @return
	 * @version 1.0
	 */
	public static boolean isPhone(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		return true;
	}
}
