package com.devin.bangsheng.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.devin.bangsheng.R;
import com.devin.bangsheng.adapter.OptionsAdapter;


public class OptionsDialog extends Dialog implements OnItemClickListener {
	private Context context;
	private OnOptionsClickedListener listener;
	private String message;
	private String[] options;
	private ListView lvOptions;
	private TextView tvMessage;

	public interface OnOptionsClickedListener {
		
		public void onOptionsClicked(View view, int position);
	}

	public OptionsDialog(Context context, OnOptionsClickedListener listener,
			String message, String... options) {
		super(context, R.style.options_dialog);
		this.listener = listener;
		this.message = message;
		this.options = options;
		this.context = context;
		init();
	}
	
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.l_options_dialog, null);
		tvMessage = (TextView) view.findViewById(R.id.tv_message);
		lvOptions = (ListView) view.findViewById(R.id.lv_options);
		if (message == null) {
			tvMessage.setVisibility(View.GONE);
		} else {
			tvMessage.setText(message);
		}

		if (options == null) {
			lvOptions.setVisibility(View.GONE);
		} else {
			lvOptions.setAdapter(new OptionsAdapter(getContext(), options));
			lvOptions.setOnItemClickListener(this);
		}
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		setContentView(view);
		setAttribute();
	}
	
	public void setGravity(int gravity){
		Window dialogWindow = getWindow();
		WindowManager m = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		dialogWindow.setGravity(gravity);
	}

	private void setAttribute() {
		Window dialogWindow = getWindow();
		WindowManager m = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		//p.height = LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.6
		p.width = (int) (Math.min(d.getWidth(), d.getHeight()) * 0.7); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (listener != null) {
			listener.onOptionsClicked(arg1, arg2);
		}
		dismiss();
	}
	
	public void setMessageGravity(int gravity) {
		tvMessage.setGravity(Gravity.LEFT);
	}
	
	/**
	 * 刷新列表中的数据
	 * @param options
	 */
	public void refreshList(String... options) {
		this.options = options;
		lvOptions.setAdapter(new OptionsAdapter(getContext(), options));
		lvOptions.setOnItemClickListener(this);
	}
}
