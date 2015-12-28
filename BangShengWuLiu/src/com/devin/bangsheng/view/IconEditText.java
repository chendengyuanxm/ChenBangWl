package com.devin.bangsheng.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.devin.bangsheng.R;

public class IconEditText extends LinearLayout{
	
	private Context context;
	private ImageView leftIv, rightIv;
	private EditText et;

	public IconEditText(Context context) {
		this(context, null);
	}

	public IconEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}
	
	private void init(AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.l_icon_edit, this);
		leftIv = (ImageView) findViewById(R.id.iv_left);
		rightIv = (ImageView) findViewById(R.id.iv_right);
		et = (EditText) findViewById(R.id.et);
		
		if(attrs == null) return;
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconEditText);
		et.setHint(ta.getString(R.styleable.IconEditText_etHint));
		et.setTextSize(ta.getDimension(R.styleable.IconEditText_etTextSize, 12));
		et.setText(ta.getString(R.styleable.IconEditText_etText));
		et.setInputType(ta.getInt(R.styleable.IconEditText_etInputType, 1));
		et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				ta.getInt(R.styleable.IconEditText_etInputMaxLen, 1000)) });
		
		Drawable leftDrawable = ta.getDrawable(R.styleable.IconEditText_leftIcon);
		if(leftDrawable != null) {
			leftIv.setImageDrawable(leftDrawable);
		}else {
			leftIv.setVisibility(View.GONE);
		}
		Drawable rightDrawable = ta.getDrawable(R.styleable.IconEditText_rightIcon);
		if(leftDrawable != null) {
			rightIv.setImageDrawable(rightDrawable);
		}else {
			rightIv.setVisibility(View.GONE);
		}
		ta.recycle();
	}
	
	public void setText(String text) {
		et.setText(text);
	}
	
	public String getText() {
		return et.getText().toString();
	}
	
	public EditText getEditText() {
		return et;
	}
}
