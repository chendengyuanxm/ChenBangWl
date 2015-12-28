package com.devin.bangsheng.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.trans.wuliu.msg.impl.OrderResult;

public class OrderResultAdpater extends BaseAdapter{
	
	private Context context;
	private List<OrderResult> lists;
	
	public OrderResultAdpater(Context context, List<OrderResult> lists) {
		this.context = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
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
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_order_result, null);
			holder = new ViewHolder();
			holder.orderNoTv = (TextView)convertView.findViewById(R.id.tv_orderNo);
			holder.resultMsgTv = (TextView)convertView.findViewById(R.id.tv_result);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		OrderResult result = lists.get(position);
		holder.orderNoTv.setText(result.getOrder_no());
		holder.resultMsgTv.setText(result.getRsltMsg());
		if(!"00".equals(result.getRsltCode())) {
			holder.resultMsgTv.setTextColor(context.getResources().getColor(R.color.text_label_red));
		}else {
			holder.resultMsgTv.setTextColor(context.getResources().getColor(R.color.text_label));
		}
		return convertView;
	}
	
	class ViewHolder {
		TextView orderNoTv;
		TextView resultMsgTv;
	}
}
