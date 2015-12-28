package com.devin.bangsheng.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.devin.bangsheng.MainService;
import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.db.biz.PayDB;
import com.devin.bangsheng.enums.MsgType;
import com.devin.bangsheng.enums.PayWay;
import com.devin.bangsheng.enums.TransType;
import com.devin.bangsheng.service.YsTransferService;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderReceiveResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderReceiveResp.OrderReceiveRespWlBody;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderResult;
import com.devin.bangsheng.trans.wuliu.msg.impl.SignOrder;
import com.devin.bangsheng.trans.yinshi.msg.impl.ConsumeNotifyResponse;
import com.devin.bangsheng.util.PosTraceGenerator;
import com.devin.framework.base.CommonException;
import com.devin.framework.util.DvDateUtils;
import com.devin.framework.util.DvFileUtils;
import com.devin.framework.util.DvStrUtils;
import com.devin.framework.util.UIHelper;
import com.newland.mtype.util.ISOUtils;
import com.newland.sdk.spdbtrans.SpdbTransData;

public class OrderSignActivity extends AbstractTransActivity implements OnClickListener{
	
	private TextView amountTv;
	private TextView totalTv;
	private Button confirmBtn;
	private EditText signerEt;
	
	private List<Order> orders = new ArrayList<Order>();
	private PayWay payway = PayWay.CARD;
	private String signflag = "0";
	private String signer;
	private Double amount;
	private SpdbTransData resultData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_sign);
		initTopbar("订单签收");
		transType = TransType.CONSUME;
		orders = (List<Order>) getIntent().getSerializableExtra(Const.ExtraConst.ORDERS);
		initViews();
	}
	
	private void initViews() {
		amountTv = (TextView) findViewById(R.id.tv_amount);
		totalTv = (TextView) findViewById(R.id.tv_num);
		signerEt = (EditText) findViewById(R.id.et_signer);
		signerEt.setImeOptions(EditorInfo.IME_ACTION_DONE);
		confirmBtn = (Button)findViewById(R.id.btn_confirm);
		confirmBtn.setOnClickListener(this);
		totalTv.setText(orders.size()+"");
		amount = calTotalAmount();
		amountTv.setText(amount+"");
		
		RadioGroup signerRg = (RadioGroup) findViewById(R.id.rg_signer);
		signerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.rb_signer1) {
					signflag = "0";
				}else {
					signflag = "1";
				}
			}
		});
		
		RadioGroup paywayRg = (RadioGroup) findViewById(R.id.rg_payway);
		paywayRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.rb_card) {
					payway = PayWay.CARD;
				}else {
					payway = PayWay.CASH;
				}
			}
		});
		
		//无签收金额
		if(amount == 0) {
			paywayRg.setVisibility(View.GONE);
		}
	}
	
	private Double calTotalAmount() {
		Double total = 0.0;
		for(Order order : orders) {
			total += Double.parseDouble(order.getCod());
		}
		
		return total;
	}
	
	private String getOrderNos() {
		String ordersStr = "";
		if(orders == null || orders.size() == 0) return "";
		for(int i = 0; i < orders.size()-1; i ++) {
			ordersStr += orders.get(i).getOrderno() + "|";
		}
		ordersStr += orders.get(orders.size()-1).getOrderno();
		
		return ordersStr;
	}
	
	@Override
	public void onClick(View v) {
		signer = signerEt.getText().toString();
		if(DvStrUtils.isEmpty(signer)) {
			UIHelper.showToast(context, "请签名");
			return;
		}
		if(amount == 0) {
			signOrderNoCash();
		}else {
			if(payway == PayWay.CARD) {
				startMposTransfer();
			}else if(payway == PayWay.CASH) {
				consumeByCash();
			}
		}
	}
	
	@Override
	public void onEventMainThread(Message msg) {		
		if(msg.what == MsgType.CARD_CONSUME_NOTIFY.getId()) {	//由于刷卡缴费通知采取后台上送模式，这里重写
			if(msg.obj instanceof ConsumeNotifyResponse) {
				ConsumeNotifyResponse resp = (ConsumeNotifyResponse) msg.obj;
				final String respCode = resp.getRespCode();
				if("00".equals(respCode)) {
					String sysTrace = resp.getSystrace();
					PayDB.getInstance(context).delete(sysTrace);	//刷卡缴费通知成功后删除保存记录
				}else {
					logger.info("刷卡缴费通知失败[" + respCode + "]");
				}
			}else if(msg.obj instanceof CommonException){
				CommonException error = (CommonException) msg.obj;
				logger.info("刷卡缴费通知失败[" + error.getLocalizedMessage() + "]");
			}
		}else {
			super.onEventMainThread(msg);
		}
	}
	
	@Override
	public void onResponse(Message msg) {
		if(msg.obj instanceof ConsumeNotifyResponse) {
			ConsumeNotifyResponse resp = (ConsumeNotifyResponse) msg.obj;
			PayWay payWay = PayWay.getPaywayByCode(resp.getPayWay());
			final String respCode = resp.getRespCode();
			if(payWay == PayWay.CASH){		//现金缴费通知
				if("00".equals(respCode)) {
					signOrderByCash();
				}else {
					Intent intent = new Intent(context, OrderSignFailActivity.class);
					intent.putExtra(Const.ExtraConst.ERRCODE, respCode);
					intent.putExtra(Const.ExtraConst.ERRMSG, "现金缴费失败");
					intent.putExtra(Const.ExtraConst.TRANS_TYPE, transType);
					startActivity(intent);
					finish();
				}
			}
		}else if(msg.obj instanceof OrderReceiveResp) {
			OrderReceiveResp resp = (OrderReceiveResp) msg.obj;
			if(resp.header.success()) {
				OrderReceiveRespWlBody body = (OrderReceiveRespWlBody) resp.body;
				ArrayList<OrderResult> orderRslts = body.getOrders();
				Intent intent = new Intent(this, OrderSignSuccessActivity.class);
				intent.putExtra(Const.ExtraConst.ORDER_RSLTS, orderRslts);
				startActivity(intent);
				finish();
			}else {
				showDialogEx(resp.header.getResponseMsg());
			}
		}
	}
	
	private void consumeByCard(SpdbTransData resultData, String jbgData) {
		String orderNo = getOrderNos();
		String posTrace = resultData.old_voucherNo;
		String transType = "01";
		String reqTime = resultData.transDate + resultData.transTime;
		String batchNo = resultData.batchNo;
		String pan = resultData.payerAcctNo;
		String rrn = resultData.refNo;
		String systrace = PosTraceGenerator.getInstance().generateTrace();
		String mrchNo = resultData.merchantNO;
		String termId = resultData.terminalNo;
		String employNo = currUser.getEmployNo();
		String payWay = payway.getCode();
		//后台上送缴费通知 
		MainService.getInstance().cardConsumeNotify(amount+"", batchNo, employNo, mrchNo, termId, orderNo, pan, payWay
				, posTrace, reqTime, rrn, signer, signflag, systrace, transType, jbgData, "", "", 3, true);
	}
	
	private void consumeByCash() {
		showProgress();
		String orderNo = getOrderNos();
		String posTrace = "";
		String transType = "01";
		String reqTime = DvDateUtils.getCurrentDate("yyyyMMddhhmmss");
		String batchNo = "";
		String pan = "";
		String rrn = "";
		String systrace = PosTraceGenerator.getInstance().generateTrace();
		String employNo = currUser.getEmployNo();
		YsTransferService.getInstance().consumeNotify(defaultSocketTransfer, amount+"", batchNo
				, employNo, null, null
				, orderNo, pan, payway.getCode(), posTrace, reqTime, rrn, signer, signflag, systrace
				, transType, null, "", "");
	}
	
	private void signOrderByCard(SpdbTransData resultData) {
		showProgress();
		List<SignOrder> signOrders = new ArrayList<SignOrder>();
		for(Order order : orders) {
			SignOrder sOrder = new SignOrder();
			sOrder.setBanktrace(resultData.refNo);
			sOrder.setCardid(resultData.payerAcctNo);
			sOrder.setCod(resultData.amount);
			sOrder.setDsname(order.getDsname());
			sOrder.setDssn(order.getDssn());
			sOrder.setOrderno(order.getOrderno());
			sOrder.setPayway(payway.getCode());
			sOrder.setPostrace(resultData.traceNo);
			sOrder.setSigner(signer);
			sOrder.setSignflag(signflag);
			sOrder.setTracetime(resultData.transTime);
			signOrders.add(sOrder);
		}
		mService.orderReceive(defaultHttpTransfer, signOrders);
	}
	
	private  void signOrderByCash() {
		showProgress();
		List<SignOrder> signOrders = new ArrayList<SignOrder>();
		for(Order order : orders) {
			SignOrder sOrder = new SignOrder();
			sOrder.setCod(order.getCod());
			sOrder.setDsname(order.getDsname());
			sOrder.setDssn(order.getDssn());
			sOrder.setOrderno(order.getOrderno());
			sOrder.setPayway(payway.getCode());
			sOrder.setPostrace("");
			sOrder.setSigner(signer);
			sOrder.setSignflag(signflag);
			sOrder.setTracetime(DvDateUtils.getCurrentDate("yyyyMMddHHmmss"));
			signOrders.add(sOrder);
		}
		mService.orderReceive(defaultHttpTransfer, signOrders);
	}
	
	private void signOrderNoCash() {
		showProgress();
		List<SignOrder> signOrders = new ArrayList<SignOrder>();
		for(Order order : orders) {
			SignOrder sOrder = new SignOrder();
			sOrder.setCod(order.getCod());
			sOrder.setDsname(order.getDsname());
			sOrder.setDssn(order.getDssn());
			sOrder.setOrderno(order.getOrderno());
			sOrder.setPayway("");
			sOrder.setPostrace("");
			sOrder.setSigner(signer);
			sOrder.setSignflag(signflag);
			sOrder.setTracetime(DvDateUtils.getCurrentDate("yyyyMMddHHmmss"));
			signOrders.add(sOrder);
		}
		mService.orderReceive(defaultHttpTransfer, signOrders);
	}
	
	@Override
	public void startTransfer() {
		showProgress();
		deviceController.consume(this, amount);
	}

	@Override
	public void onSucc(SpdbTransData resultData) {
		super.onSucc(resultData);
		if(transType == TransType.parseTransTypeFromCode(resultData.transType) 
				&& !DvStrUtils.isEmpty(resultData.refNo)) {
			this.resultData = resultData;
			Intent intent = new Intent(context, SignatureActivity.class);
			startActivityForResult(intent, 2);
		}
	}
	
	@Override
	public void onFail(String code, String detail, SpdbTransData resultData) {
		super.onFail(code, detail, resultData);
		if(detail != null && detail.contains("取消")) {
//			UIHelper.showToast(context, detail);
			finish();
		}else if(detail != null && detail.contains("超时")){
//			UIHelper.showToast(context, detail);
			finish();
		}else {
			Intent intent = new Intent(context, OrderSignFailActivity.class);
			intent.putExtra(Const.ExtraConst.ERRCODE, code);
			intent.putExtra(Const.ExtraConst.ERRMSG, detail);
			intent.putExtra(Const.ExtraConst.TRANS_TYPE, transType);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			String jbgPath = data.getStringExtra("jbgPath");
			byte[] jbgData = DvFileUtils.getByteArrayFromSD(jbgPath);
			logger.debug("JBG签名数据长度：" + jbgData.length);
			consumeByCard(resultData, ISOUtils.hexString(jbgData));
			signOrderByCard(resultData);
		}
	}
}
