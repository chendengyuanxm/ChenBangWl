package com.devin.framework.ui.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.devin.framework.base.CommonException;
import com.devin.framework.base.DvBaseApplication;
import com.devin.framework.http.InfoResultRequest;
import com.devin.framework.http.SimpleTransfer;
import com.devin.framework.log.Logger;
import com.devin.framework.log.LoggerFactory;
import com.devin.framework.tcp.ITransfer;
import com.devin.framework.tcp.Response;
import com.devin.framework.tcp.SimpleHttpTransfer;
import com.devin.framework.tcp.SimpleSocketTransfer;
import com.devin.framework.util.UIHelper;

import de.greenrobot.event.EventBus;

public abstract class DvBaseFragment extends Fragment implements Callback{
	
	protected static final Logger logger = LoggerFactory.getLogger(DvBaseFragment.class);
	private static final int MSG_SHOW_CONFIRM_DIALOG = -1100;
	private static final int MSG_DISMISS_DIALOG = -1101;
	
	protected DvBaseApplication mApplication;
	protected FragmentManager mFragmentManager;
	protected Activity mAttachActivity;
	protected View rView;
	protected Handler defaultHandler = new Handler();
	
	protected boolean isDestroyed;
	/**网络请求发送逻辑类*/
	protected ITransfer defaultHttpTransfer;
	protected ITransfer defaultSocketTransfer;
	protected ProgressDialog mProgressDialog;
	
	private Map<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		logger.info(getClass().getSimpleName() + ">>>>onCreateView");
		mApplication = (DvBaseApplication) getActivity().getApplication();
		mAttachActivity = getActivity();
		EventBus.getDefault().register(this);
		defaultHttpTransfer = new SimpleHttpTransfer(this);
		defaultSocketTransfer = new SimpleSocketTransfer(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.info(getClass().getSimpleName() + ">>>>onCreate");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		logger.info(getClass().getSimpleName() + ">>>>onStart");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		logger.info(getClass().getSimpleName() + ">>>>onResume");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		logger.info(getClass().getSimpleName() + ">>>>onStop");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		logger.info(getClass().getSimpleName() + ">>>>onDestroyView");
		//反注册所有的订阅者
		defaultHttpTransfer.unregisterAll();
		defaultSocketTransfer.unregisterAll();
		EventBus.getDefault().unregister(this);
		isDestroyed = true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		logger.info(getClass().getSimpleName() + ">>>>onDestroy");
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_SHOW_CONFIRM_DIALOG:
			int dlgId = msg.arg1;
			String message = (String) msg.obj;
			showDialog0(dlgId, message);
			break;

		case MSG_DISMISS_DIALOG:
			int dlgId1 = msg.arg1;
			dismissDialog0(dlgId1);
			break;
		}
		return false;
	}
	
	protected void showDialogEx(String message) {
		showDialogEx(0, message);
	}
	
	/**
	 * 安全的显示对话框，该方法是线程安全的。
	 * 
	 * @param dlgId
	 */
	protected void showDialogEx(int dlgId, String message) {
		if (!Looper.getMainLooper().equals(Looper.myLooper())) {
			// 不在UI线程，使用消息方式来调用此方法。
			Message msg = new Message();
			msg.arg1 = dlgId;
			msg.obj = message;
			msg.what = MSG_SHOW_CONFIRM_DIALOG;
			defaultHandler.sendMessage(msg);

			return;
		}

		showDialog0(dlgId, message);
	}

	/**
	 * 安全的关闭对话框，该方法是线程安全的。
	 * 
	 * @param dlgId
	 */
	protected void dismissDialogEx(int dlgId) {
		if (!Looper.getMainLooper().equals(Looper.myLooper())) {
			// 不在UI线程，使用消息方式来调用此方法。
			Message msg = new Message();
			msg.arg1 = dlgId;
			msg.what = MSG_DISMISS_DIALOG;
			defaultHandler.sendMessage(msg);

			return;
		}

		dismissDialog0(dlgId);
	}
	
	private void showDialog0(int dlgId, String message) {
		Dialog dialog = UIHelper.createDialog(mAttachActivity, message);
		mDialogs.put(dlgId, dialog);
		dialog.show();
	}
	
	private void dismissDialog0(int dlgId) {
		Dialog dialog = mDialogs.remove(dlgId);
		if(dialog != null)
			dialog.dismiss();
	}
	
	protected void showProgress() {
		if(mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(mAttachActivity, "", "请稍候...");
		}
		mProgressDialog.show();
	}
	
	protected void dismissProgress() {
		if(mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}
	
	/**
	 * 回退，子类可重写回退跳转
	 * 
	 * @version 1.0
	 */
	public void back() {
		
	}
	
	/**
     * EventBus订阅者事件通知的函数, UI线程
     * 
     * @param msg
     */
    public void onEventMainThread(Message msg)
    {
    	dismissProgress();
		if (!isDestroyed && !isDetached())
        {
//			if(msg.obj instanceof VolleyError) {
//				VolleyError error = (VolleyError) msg.obj;
//				showDialogEx(error.getLocalizedMessage());
//				return;
//			}
			if(msg.obj instanceof CommonException) {
				CommonException exception = (CommonException)msg.obj;
				showDialogEx(exception.getLocalizedMessage());
				return;
			}
			onResponse(msg);
        }
    }
    
    public abstract void onResponse(Message msg);
    
    /**
     * 取消网络发送
     * 
     * @param tag
     * @version 1.0
     */
    public void cancel(Object tag) {
    	dismissProgress();
    	defaultHttpTransfer.cancel(tag);
    }
}
