package com.devin.bangsheng.view;

import com.devin.bangsheng.R;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.view.View.OnClickListener;;

public class InputPanel extends TableLayout implements OnClickListener{
	
	private int[][] drawables = new int[][]{
			{R.drawable.selector_num_1, R.drawable.selector_num_2, R.drawable.selector_num_3},
			{R.drawable.selector_num_4, R.drawable.selector_num_5, R.drawable.selector_num_6},
			{R.drawable.selector_num_7, R.drawable.selector_num_8, R.drawable.selector_num_9},
			{R.drawable.selector_num_back, R.drawable.selector_num_0, R.drawable.selector_num_ok}
	};
	private String[][] keys = new String[][]{
			{"1", "2", "3"},
			{"4", "5", "6"},
			{"7", "8", "9"},
			{"back", "0", "ok"}
	};
	
	private Context context;
	private Vibrator vibrator;
	private OnKeyClickListener mListener;
	
	public InputPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public InputPanel(Context context) {
		this(context, null);
	}
	
	private void init() {
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		int row = drawables.length;
		for(int i = 0; i < row; i ++) {
			TableRow tr = new TableRow(context);
			for(int j = 0; j < drawables[i].length; j ++) {
				ImageButton ib = new ImageButton(context);
				ib.setBackgroundResource(drawables[i][j]);
				ib.setTag(keys[i][j]);
				ib.setOnClickListener(this);
				TableRow.LayoutParams lp = new TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
				lp.weight = 1;
				tr.addView(ib, lp);
			}
			addView(tr, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		setBackgroundColor(getResources().getColor(R.color.blue));
	}

	@Override
	public void onClick(View v) {
		vibrator.vibrate(80);	//按键震动
		String key = (String) v.getTag();
		if("back".equals(key)) {
			if(mListener != null) {
				mListener.onBack();
			}
		}else if("ok".equals(key)) {
			if(mListener != null) {
				mListener.onOk();
			}
		}else {
			if(mListener !=	null) {
				mListener.onKeyDown(key);
			}
		}
	}
	
	public void setKeyListener(OnKeyClickListener listener) {
		mListener = listener;
	}
	
	public interface OnKeyClickListener {
		public void onKeyDown(String num);
		public void onOk();
		public void onBack();
	}
}
