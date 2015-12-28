package com.devin.bangsheng.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devin.bangsheng.BaseApplication;
import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.User;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderResult;
import com.devin.bangsheng.trans.wuliu.msg.impl.UserLoginResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.UserLoginResp.UserLoginRespWlBody;
import com.devin.framework.tcp.ITransfer;
import com.devin.framework.tcp.SimpleHttpTransfer;
import com.devin.framework.util.DvAppUtils;
import com.devin.framework.util.DvFileUtils;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;
import com.newlandcomputer.jbig.JbigUtils;

public class LoginActivity extends BaseActivity implements OnClickListener{
	
	private EditText userNameEt;
	private EditText passWordEt;
	private TextView tmnlNoTv;
	private TextView siteNoTv;
	private CheckBox remeberCb;
	
	private String employNo;
	private String passwd;
	
	private SharedPreferences mSharePreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initViews();
		mSharePreferences = getSharedPreferences(Const.SpConst.SP_NAME, 0);
		boolean remeber = mSharePreferences.getBoolean(Const.SpConst.isCheck, false);
		remeberCb.setChecked(remeber);
		if(remeber) {
			String userName = mSharePreferences.getString(Const.SpConst.userName, "");
			userNameEt.setText(userName);
			userNameEt.setSelection(userName.length());
		}
	}

	private void initViews() {
		userNameEt = (EditText) findViewById(R.id.et_userName);
		passWordEt = (EditText) findViewById(R.id.et_passwd);
		tmnlNoTv = (TextView) findViewById(R.id.tv_tmnlNo);
		siteNoTv = (TextView) findViewById(R.id.tv_siteNo);
		remeberCb = (CheckBox) findViewById(R.id.cb_remeber);
		Button loginBtn = (Button) findViewById(R.id.btn_login);
		loginBtn.setOnClickListener(this);
		
//		userNameEt.setText(Const.SettingConst.employNo);
		passWordEt.setText(Const.SettingConst.passwd);
		siteNoTv.setText("版本号 " + DvAppUtils.getAppVersionName(context));
	}
	
	private void validate() throws Exception {
		employNo = userNameEt.getText().toString().trim();
		passwd = passWordEt.getText().toString();
		if(DvStrUtils.isEmpty(employNo)) {
			throw new Exception("用户号不能为空");
		}else if(DvStrUtils.isEmpty(passwd)) {
			throw new Exception("密码不能为空");
		}
	}
	
	@Override
	public void onClick(View v) {
		try {
			validate();
			login();
		} catch (Exception e) {
			UIHelper.showToast(context, e.getLocalizedMessage());
		}
//		Intent intent = new Intent(this, HomeActivity.class);
//		startActivity(intent);
//		Intent intent = new Intent(this, SignatureActivity.class);
//		startActivity(intent);
//		File folder = new File("/sdcard/chenbang/temp/");
//		File[] files = folder.listFiles();
//		byte[] data = DvFileUtils.getByteArrayFromSD(files[0].getAbsolutePath());
//		try {
//			Bitmap bmp = JbigUtils.decodeJbig(data);
//			ImageView iv = (ImageView) findViewById(R.id.iv_logo);
//			iv.setImageBitmap(bmp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	private void login() {
		showProgress();
		ITransfer transfer = new SimpleHttpTransfer(this);
		mService.userLogin(transfer, employNo, passwd);
	}

	@Override
	public void onResponse(Message msg) {
		UserLoginResp resp = (UserLoginResp) msg.obj;
		if(resp.header.success()) {
			remeberUserName();
			UserLoginRespWlBody body = (UserLoginRespWlBody) resp.body;
			User user = new User();
			user.setEmployname(body.getEmployname());
			user.setMobile(body.getMobile());
			user.setNetcode(body.getNetcode());
			user.setNetname(body.getNetname());
			user.setRolename(body.getRolename());
			user.setEmployNo(employNo);
			BaseApplication.getInstance().setUser(user);
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}else {
			showDialogEx(resp.header.getResponseMsg());
		}
	}
	
	private void remeberUserName() {
		mSharePreferences.edit().putBoolean(Const.SpConst.isCheck, remeberCb.isChecked()).commit();
		if(remeberCb.isChecked()) {
			String remeberName = userNameEt.getText().toString().trim();
			mSharePreferences.edit().putString(Const.SpConst.userName, remeberName).commit();
		}
	}
}
