package com.devin.bangsheng.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.OrderListAdapter;
import com.devin.bangsheng.bean.Order;
import com.devin.bangsheng.common.Const;
import com.devin.bangsheng.enums.OrderStatus;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrdersResp;
import com.devin.bangsheng.trans.wuliu.msg.impl.QueryOrdersResp.QueryOrdersRespWlBody;
import com.devin.framework.http.SimpleTransfer;
import com.devin.framework.tcp.ITransfer;
import com.devin.framework.tcp.SimpleHttpTransfer;
import com.devin.framework.util.UIHelper;
import com.devin.framework.view.PullListView;
import com.devin.framework.view.PullListView.OnLoadMoreListener;
import com.devin.framework.view.PullListView.OnRefreshListener;

public class OrderListActivity extends BaseActivity implements OnRefreshListener
					, OnLoadMoreListener, OnItemClickListener{
	
	private static final int REFRESH = 0;
	private static final int LOAD_MORE = 1;
	private int queryType;
	
	private static final int PAGE_SIZE = 10;
	private int startIndex;
	private int total;
	private int type = 1;	//获取的方式
	
	private TextView totalTv;
	private TextView notSignSizeTv;
	private PullListView mListView;
	private OrderListAdapter mAdapter;
	private List<Order> mOrders  = new ArrayList<Order>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		initTopbar("任务列表");
		type = getIntent().getIntExtra("dataType", 1);
		queryOrders(REFRESH);
		initViews();
	}

	private void initViews() {
		totalTv = (TextView) findViewById(R.id.tv_total);
		notSignSizeTv = (TextView) findViewById(R.id.tv_not_receive);
		mListView = (PullListView) findViewById(R.id.plv_orders);
		mAdapter = new OrderListAdapter(this, mOrders);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(true);
		mListView.setAutoLoadMore(true); // 上拉到底里自动加载更多
		mListView.setMoveToFirstItemAfterRefresh(true);// 刷新完成后移动第一条
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadListener(this);
	}

	@Override
	public void onResponse(Message msg) {
		QueryOrdersResp resp = (QueryOrdersResp) msg.obj;
		if(resp.header.success()) {
			UIHelper.showToast(this, "查询成功");
			QueryOrdersRespWlBody body = (QueryOrdersRespWlBody) resp.body;
			total = Integer.parseInt(body.getPage().getTotal());
			List<Order> orders = body.getOrders();
			int size = orders != null ? orders.size() : 0;
			startIndex += size;
			if(queryType == REFRESH) {
				mOrders.clear();
				mOrders.addAll(body.getOrders());
				mListView.onRefreshComplete();
			}else {
				mOrders.addAll(body.getOrders());
				mListView.onLoadMoreComplete(startIndex >= total);
			}
			mAdapter.notifyDataSetChanged();
			totalTv.setText(total+"");
			notSignSizeTv.setText(calDeliveryOrderNum() + "");
		}else {
			UIHelper.showDialog(context, resp.header.getResponseMsg(), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}
	}

	@Override
	public void onLoadMore() {
		if(startIndex < total) {
			queryOrders(LOAD_MORE);
		}else {
			UIHelper.showToast(context, "已加载全部");
			mListView.onLoadMoreComplete(true);
		}
	}

	@Override
	public void onRefresh() {
		startIndex = 0;
		queryOrders(REFRESH);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Order order = mOrders.get(position-1);
		Intent intent = new Intent(this, OrderDetailActivity.class);
		intent.putExtra(Const.ExtraConst.ORDER, order);
		startActivity(intent);
	}
	
	private void queryOrders(int queryType) {
		showProgress();
		this.queryType = queryType;
		ITransfer transfer = new SimpleHttpTransfer(this);
		mService.queryOrders(transfer, startIndex, PAGE_SIZE, type);
	}
	
	private int calDeliveryOrderNum() {
		if(mOrders == null) return 0;
		int num = 0;
		for(Order order : mOrders) {
			if(OrderStatus.DELIVER.getCode().equals(order.getStatus())
					|| OrderStatus.ONWAY.getCode().equals(order.getStatus())) {
				num++;
			}
		}
		return num;
	}
}
