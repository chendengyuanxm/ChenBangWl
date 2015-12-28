package com.devin.bangsheng.ui;

import com.devin.bangsheng.R;
import com.devin.bangsheng.view.Topbar;
import com.devin.bangsheng.view.Topbar.TopbarItem;
import com.devin.framework.ui.base.DvBaseFragment;

public abstract class BaseFragment extends DvBaseFragment{
	
	private Topbar mTopbar;
	
	/**
	 * 初始化标题头
	 * 
	 * @param title
	 * @version 1.0
	 */
	public void initTopbar(String title) {
		initTopbar(title, true);
	}
	
	public void initTopbar(String title, boolean canBack) {
		mTopbar = (Topbar) rView.findViewById(R.id.topbar);
		mTopbar.setTitle(title);
		mTopbar.setBackVisible(canBack);
		mTopbar.setOnTopbarClickListener(new Topbar.OnTopbarClickListener() {
			
			@Override
			public void onTopbarBtnClick(TopbarItem topbarItem) {
				if(topbarItem == TopbarItem.back) {
					back();
				}else if(topbarItem == TopbarItem.action) {
					
				}
			}
		});
	}
}
