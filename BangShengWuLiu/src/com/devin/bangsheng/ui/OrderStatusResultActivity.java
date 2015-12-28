package com.devin.bangsheng.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.Device;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.device.DeviceConnectExecutor;
import com.devin.bangsheng.device.DeviceConnectExecutor.OnConnectDeviceListener;
import com.devin.bangsheng.device.ME3xDeviceController;
import com.devin.bangsheng.device.ME3xDeviceControllerImpl;
import com.devin.bangsheng.enums.OrderStatus;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrderStatusResp.QueryOrderStatusRespWlBody;
import com.devin.bangsheng.view.TableContain;

public class OrderStatusResultActivity extends BaseActivity implements OnClickListener{
	
	private TableContain tableContain;
	private Button confirmBtn;
	
	private QueryOrderStatusRespWlBody orderStatusInfo;
	private String orderNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_order_status);
		initTopbar("配送查询");
		orderStatusInfo = (QueryOrderStatusRespWlBody) getIntent().getSerializableExtra(Const.ExtraConst.ORDER_STATUS);
		orderNo = getIntent().getStringExtra(Const.ExtraConst.ORDER_NO);
		initViews();
	}
	
	private void initViews() {
		tableContain = (TableContain) findViewById(R.id.tableContain);
		confirmBtn = (Button)findViewById(R.id.btn_confirm);
		confirmBtn.setOnClickListener(this);
		TextView orderNoTv = (TextView) findViewById(R.id.tv_orderNo);
		orderNoTv.setText("订单编号：" + orderNo);
		tableContain.addRow("网点编号：", orderStatusInfo.getNetcode());
		tableContain.addRow("网点名称：", orderStatusInfo.getNetname());
		tableContain.addRow("快件状态：", getResources().getColor(R.color.text_label)
				, OrderStatus.getStatusByCode(orderStatusInfo.getStatus()).getDesc(), getResources().getColor(R.color.yellow));
		tableContain.addRow("描述：", orderStatusInfo.getMsg());
	}
	
	@Override
	public void onClick(View v) {
		backHomePage();
	}

	@Override
	public void onResponse(Message msg) {
		
	}
}
