package com.devin.bangsheng.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.devin.bangsheng.R;

public class TableContain extends LinearLayout {

	private TableLayout tableLayout;
	private Context context;

	public TableContain(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		loadLayout(this);
	}
	
	public void addRow(String label, int Lcolor, boolean Lbold, String D, int Dcolor, boolean Dbold) {
		LayoutInflater inflater = LayoutInflater.from(context);
		TableRow tableRow = (TableRow) inflater.inflate(R.layout.l_text_row, null);
		TextView tvLabel = (TextView) tableRow.findViewById(R.id.tv_label);
		TextView detail = (TextView) tableRow.findViewById(R.id.tv_detail);
		
		detail.setGravity(Gravity.LEFT); // 右对齐
		
		tvLabel.setTextColor(Lcolor);
		tvLabel.setText(label);
		detail.setText(D);
		detail.setTextColor(Dcolor);

		if(Lbold)
			tvLabel.getPaint().setFakeBoldText(true);
		if(Dbold)
			detail.getPaint().setFakeBoldText(true);
		tableLayout.addView(tableRow);
	}

	protected ViewGroup loadLayout(ViewGroup container) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.l_table_contain, container);

		LinearLayout centerContainer = (LinearLayout) findViewById(R.id.linearLayout_table);
		tableLayout = (TableLayout) findViewById(R.id.tablecontainbase);

		return centerContainer;
	}
	
	public void addRow(String label, int Lcolor, String D, int Dcolor){
		addRow(label, Lcolor, false, D, Dcolor, false);
	}
	
	public void addRow(String label, String content) {
		int color = getResources().getColor(R.color.text_label);
		addRow(label, color, content, color);
	}
}
