package com.devin.bangsheng.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.enums.OrderStatus;
import com.devin.bangsheng.enums.PayWay;

public class OrderListAdapter extends BaseAdapter{
	
	private Context context;
	private List<Order> orders;
	private LayoutInflater inflater;
	
	public OrderListAdapter(Context context, List<Order> orders) {
		this.context = context;
		this.orders = orders;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return orders.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holer = null;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.item_order, null);
			holer = new ViewHolder();
			holer.mrchIv = (ImageView) convertView.findViewById(R.id.iv_merchant);
			holer.mrchNameTv = (TextView) convertView.findViewById(R.id.tv_merchant);
			holer.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
			holer.orderNoTv = (TextView) convertView.findViewById(R.id.tv_orderNo);
			holer.addressTv = (TextView) convertView.findViewById(R.id.tv_address);
			holer.receiptTv = (TextView) convertView.findViewById(R.id.tv_receipter);
			holer.phoneNumberTv = (TextView) convertView.findViewById(R.id.tv_phoneNumber);
			holer.payTypeTv = (TextView) convertView.findViewById(R.id.tv_pay_type);
			holer.orderStatusTv = (TextView) convertView.findViewById(R.id.tv_order_status);
			convertView.setTag(holer);
		}else {
			holer = (ViewHolder) convertView.getTag();
		}
		Order order = orders.get(position);
		holer.mrchNameTv.setText(order.getDsname());
		holer.orderNoTv.setText(order.getOrderno());
		holer.addressTv.setText(order.getAddress());
		holer.receiptTv.setText(order.getPeople());
		holer.phoneNumberTv.setText(order.getPeopletel());
		String payway = order.getSqpayway() == null ? "未支付" : PayWay.getPaywayByCode(order.getSqpayway()).getDesc();
		holer.payTypeTv.setText(payway);
		holer.orderStatusTv.setText(OrderStatus.getStatusByCode(order.getStatus()).getDesc());
		holer.timeTv.setText(order.getEmaildate());
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView mrchIv;
		TextView mrchNameTv;
		TextView timeTv;
		TextView orderNoTv;
		TextView addressTv;
		TextView receiptTv;
		TextView phoneNumberTv;
		TextView payTypeTv;
		TextView orderStatusTv;
	}
}
