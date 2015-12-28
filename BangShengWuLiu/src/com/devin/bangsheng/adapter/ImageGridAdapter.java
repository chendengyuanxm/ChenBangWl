package com.devin.bangsheng.adapter;

import com.devin.bangsheng.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageGridAdapter extends BaseAdapter{
	private Context context;

	private Drawable[] drawables;

	private String[] texts;
	
	
	public ImageGridAdapter(Context context, Drawable[] drawables,
			String[] texts) {
		super();
		this.context = context;
		this.drawables = drawables;
		this.texts = texts;
	}

	@Override
	public int getCount() {
		return drawables.length > texts.length ? drawables.length
				: texts.length;
	}

	@Override
	public Object getItem(int i) {
		return i;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LinearLayout.inflate(context, R.layout.item_home_function, null);
			holder = new ViewHolder();
			holder.tag = (TextView) convertView.findViewById(R.id.tag);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tag.setText(texts[i]);
		holder.icon.setImageDrawable(drawables[i]);

		return convertView;
	}

	private class ViewHolder {

		public ImageView icon;

		public TextView tag;

	}
}
