package com.devin.bangsheng.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devin.bangsheng.R;

/**
 * 标题栏
 *
 * @author Devin_chen
 * @date 2015年3月7日
 * 
 * @version 1.0
 */
public class Topbar extends RelativeLayout implements OnClickListener {

	private ImageButton btnBack;
	private TextView tvTitle;
	private ImageButton btnAction;

	private OnTopbarClickListener onTopbarClickListener;

	public enum TopbarItem {
		back, // 返回按钮
		action, // 操作按钮，即最右边的按钮
	}

	/**
	 * 导航点击事件监听接口
	 */
	public interface OnTopbarClickListener {
		public void onTopbarBtnClick(TopbarItem topbarItem);
	}

	public Topbar(Context context) {
		super(context);
	}

	public Topbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	@Override
	public void onClick(View view) {
		if (onTopbarClickListener == null)
			return;

		if (view.equals(btnBack)) {
			onTopbarClickListener.onTopbarBtnClick(TopbarItem.back);
		} else if (view.equals(btnAction)) {
			onTopbarClickListener.onTopbarBtnClick(TopbarItem.action);
		} else if (view.equals(tvTitle)) {
			onTopbarClickListener.onTopbarBtnClick(TopbarItem.back);
		}
	}

	/**
	 * 初始化导航条中的相关元素
	 */
	private void initView() {
		LayoutInflater.from(getContext())
				.inflate(R.layout.l_topbar, this, true);

		btnBack = (ImageButton) findViewById(R.id.ib_back);
		btnAction = (ImageButton) findViewById(R.id.ib_action);
		tvTitle = (TextView) findViewById(R.id.tv_main_title);

		// 设置事件监听器
		tvTitle.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnAction.setOnClickListener(this);
	}

	/**
	 * 设置导航栏事件监听器
	 * 
	 * @param onNavBarClickListener
	 *            点击监听器
	 */
	public void setOnTopbarClickListener(
			OnTopbarClickListener onTopbarClickListener) {
		this.onTopbarClickListener = onTopbarClickListener;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题文字
	 */
	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	/**
	 * 设置标题
	 * 
	 * @param resId
	 *            标题文字id
	 */
	public void setTitle(int resId) {
		String text = getContext().getString(resId);
		setTitle(text);
	}
	
	/**
	 * 设置功能按钮图标
	 * 
	 * @param resId
	 * @version 1.0
	 */
	public void setBtnActionBackground(int resId) {		
		btnAction.setImageResource(resId);
	}

	/**
	 * 获取标题名称
	 * 
	 * @return 标题字符串
	 */
	public String getTitle() {
		return tvTitle.getText().toString();
	}
	
	/**
	 * 后退按钮是否存在
	 * @param enableBack
	 */
	public void setBackVisible(boolean enableBack) {
		if(enableBack) {
			btnBack.setVisibility(View.VISIBLE);
		}else {
			btnBack.setVisibility(View.GONE);
		}
	}
}
