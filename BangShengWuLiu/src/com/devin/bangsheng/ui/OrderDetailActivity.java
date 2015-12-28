package com.devin.bangsheng.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.device.DeviceConnectExecutor;
import com.devin.bangsheng.device.DeviceConnectExecutor.OnConnectDeviceListener;
import com.devin.bangsheng.device.ME3xDeviceControllerImpl;
import com.devin.bangsheng.enums.OrderStatus;
import com.devin.bangsheng.enums.PayWay;
import com.devin.bangsheng.view.OptionsDialog;
import com.devin.bangsheng.view.TableContain;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;

public class OrderDetailActivity extends BaseActivity implements OnClickListener{
	
	private static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	
	private Button sendSmsBtn, callPhoneBtn, mapBtn;
	private Button dealBtn;
	private TextView mrchNameTv;
	private TextView timeTv;
	private TextView payAmountTv;
	private TableContain orderContain;
	
	private Order order;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		order = (Order) getIntent().getSerializableExtra(Const.ExtraConst.ORDER);
		initTopbar("订单详情");
		initViews();
		initOrderDetail();
		IntentFilter filter = new IntentFilter(SMS_SEND_ACTIOIN);
		registerReceiver(mBroadcastReceiver, filter);
	}
	
	private void initViews() {
		mrchNameTv = (TextView) findViewById(R.id.tv_merchant);
		timeTv = (TextView) findViewById(R.id.tv_time);
		payAmountTv = (TextView) findViewById(R.id.tv_payAmount);
		sendSmsBtn = (Button) findViewById(R.id.btn_send_sms);
		callPhoneBtn = (Button) findViewById(R.id.btn_call_phone);
		mapBtn = (Button) findViewById(R.id.btn_navigate_map);
		dealBtn = (Button) findViewById(R.id.btn_deal);
		sendSmsBtn.setOnClickListener(this);
		callPhoneBtn.setOnClickListener(this);
		mapBtn.setOnClickListener(this);
		dealBtn.setOnClickListener(this);
	}
	
	private void initOrderDetail() {
		orderContain = (TableContain) findViewById(R.id.tableContain);
		orderContain.addRow("订单号", order.getOrderno());
		orderContain.addRow("订单状态", getResources().getColor(R.color.text_label)
				, OrderStatus.getStatusByCode(order.getStatus()).getDesc(), getResources().getColor(R.color.yellow));
		orderContain.addRow("支付方式", PayWay.getPaywayByCode(order.getSqpayway()).getDesc());
		orderContain.addRow("收件地址", order.getAddress());
		orderContain.addRow("收货人", order.getPeople());
		orderContain.addRow("电话", order.getPeopletel());
		
		timeTv.setText(order.getEmaildate());
		mrchNameTv.setText(order.getDsname());
		payAmountTv.setText(order.getCod() + "元");
	}

	@Override
	public void onResponse(Message msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_sms:
			String phoneNumber0 = order.getPeopletel();
			String message = "您的快递已到，请查收！！快递订单号：" + order.getOrderno();
			if(!DvStrUtils.checkPhoneNumber(phoneNumber0)) {
				UIHelper.showToast(context, "预留电话有问题~~");
			}else {
				PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, OrderDetailActivity.class), 0);
		        SmsManager sms = SmsManager.getDefault();
		        sms.sendTextMessage(phoneNumber0, null, message, pi, null);
			}
			break;

		case R.id.btn_call_phone:
			String phoneNumber = order.getPeopletel();
			if(!DvStrUtils.checkPhoneNumber(phoneNumber)) {
				UIHelper.showToast(context, "预留电话有问题~~");
			}else {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phoneNumber));  
		        startActivity(intent);  
			}
			break;
			
		case R.id.btn_navigate_map:
			Intent intent1 = new Intent(this, MapNavigationActivity.class);
			intent1.putExtra(Const.ExtraConst.ADDRESS, order.getAddress());
			startActivity(intent1);
			break;
		case R.id.btn_deal:
			showReceiveTypeDialog();
			break;
		}
	}
	
	private Dialog dialog;
	private void showReceiveTypeDialog() {
		if(dialog == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.l_receive_type_dialog, null);
			TextView receiveTv = (TextView) view.findViewById(R.id.tv_1);
			TextView returnTv = (TextView) view.findViewById(R.id.tv_2);
			dialog = new Dialog(context, R.style.options_dialog);
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(true);
			//设置宽度
			Window dialogWindow = dialog.getWindow();
			WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
			WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
			p.width = (int) (Math.min(d.getWidth(), d.getHeight()) * 0.7); // 宽度设置为屏幕的0.65
			dialogWindow.setAttributes(p);
			receiveTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					OrderStatus status = OrderStatus.getStatusByCode(order.getStatus());
//					if(status == OrderStatus.ONWAY || status == OrderStatus.DELIVER) {
						Intent intent = new Intent(context, OrderSignActivity.class);
						ArrayList<Order> orders = new ArrayList<Order>();
						orders.add(order);
						intent.putExtra(Const.ExtraConst.ORDERS, orders);
						startActivity(intent);
//					}else {
//						UIHelper.showToast(context, "当前订单不允许签收");
//					}
					dialog.dismiss();
				}
			});
			returnTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					OrderStatus status = OrderStatus.getStatusByCode(order.getStatus());
					if(status == OrderStatus.ONWAY || status == OrderStatus.DELIVER) {
						Intent intent = new Intent(context, BadOrderSignActivity.class);
						intent.putExtra(Const.ExtraConst.ORDER_NO, order.getOrderno());
						startActivity(intent);
					}else {
						UIHelper.showToast(context, "当前订单不允许反馈");
					}
					dialog.dismiss();
				}
			});
		}
		if(!dialog.isShowing())
			dialog.show();
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(SMS_SEND_ACTIOIN.equals(intent.getAction())) {
				if(getResultCode() == RESULT_OK) {
					UIHelper.showToast(context, "短信发送成功~");
				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
}
