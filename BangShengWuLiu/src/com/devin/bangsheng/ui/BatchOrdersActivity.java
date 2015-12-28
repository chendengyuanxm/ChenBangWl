package com.devin.bangsheng.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.OrderListAdapter;
import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.common.Const;
import com.devin.framework.view.PullListView;

public class BatchOrdersActivity extends BaseActivity implements OnItemClickListener{
	
	private PullListView mListView;
	private OrderListAdapter mAdapter;
	private List<Order> mOrders  = new ArrayList<Order>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_batch_orders);
		initTopbar("批量订单列表");
		mOrders = (List<Order>) getIntent().getSerializableExtra("orders");
		initViews();
	}

	private void initViews() {
		mListView = (PullListView) findViewById(R.id.plv_orders);
		mAdapter = new OrderListAdapter(this, mOrders);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setCanRefresh(false);
		mListView.setCanLoadMore(false);
		mListView.setAutoLoadMore(false); // 上拉到底里自动加载更多
		mListView.setMoveToFirstItemAfterRefresh(true);// 刷新完成后移动第一条
		
		Button batchSignBtn = (Button) findViewById(R.id.btn_batch_sign);
		batchSignBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, OrderSignActivity.class);
				intent.putExtra(Const.ExtraConst.ORDERS, (ArrayList<Order>)mOrders);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResponse(Message msg) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Order order = mOrders.get(position);
		Intent intent = new Intent(this, OrderDetailActivity.class);
		intent.putExtra(Const.ExtraConst.ORDER, order);
		startActivity(intent);
	}
}
