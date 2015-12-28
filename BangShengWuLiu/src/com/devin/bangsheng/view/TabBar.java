package com.devin.bangsheng.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.devin.bangsheng.R;

public class TabBar extends LinearLayout implements OnCheckedChangeListener{
	
	private Context mContext;
	
	private RadioGroup rg;
	
	private RadioButton deliveryRb, settingRb;
	
	public enum NavItem{
		delivery,
		setting
	}
	
	private NavItem checkedItem;
	
	private OnNavItemClickListener mCallback;
	
	public interface OnNavItemClickListener {
		public void onItemClick(int position);
	}
	
	public TabBar(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public TabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.tab_view, null);
		deliveryRb = (RadioButton) v.findViewById(R.id.ib1);
		settingRb = (RadioButton) v.findViewById(R.id.ib2);
		rg = (RadioGroup) v.findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(this);
		addView(v, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		setCheck(NavItem.delivery);
	}
	
	public void setOnNavItemClickListener(OnNavItemClickListener listener) {
		mCallback = listener;
	}
	
	public NavItem getCheckedItem() {
		return checkedItem;
	}
	
	public void setCheck(NavItem item) {
		switch (item) {
//		case none:
//			transDetailRb.setChecked(false);
//			repealRb.setChecked(false);
//			balanceRb.setChecked(false);
//			settingRb.setChecked(false);
//			checkedItem = NavItem.none;
//			break;
		case delivery:
			deliveryRb.setChecked(true);
			checkedItem = NavItem.delivery;
			break;
		case setting:
			settingRb.setChecked(true);
			checkedItem = NavItem.setting;
			break;
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.ib1:
			if(mCallback != null && checkedItem != NavItem.delivery)
				mCallback.onItemClick(0);
			checkedItem = NavItem.delivery;
			break;

		case R.id.ib2:
			if(mCallback != null && checkedItem != NavItem.setting)
				mCallback.onItemClick(1);
			checkedItem = NavItem.setting;
			break;
		}
	}
	
}
