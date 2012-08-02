package com.fraggel.recoveryexecuter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class miAdapterApk extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<String> data;

	public miAdapterApk(Context context, List<String> item) {

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = item;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		TextView filerowtext;

		ImageView img1;

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.apkrow, null);

		}

		filerowtext = (TextView) convertView.findViewById(R.id.filerowtextapk);

		img1 = (ImageView) convertView.findViewById(R.id.fileimgapk);

		filerowtext.setText(data.get(position).toString());
		String nom = data.get(position).toString();
		String ext = "";
		if (nom.length() > 3) {
			ext = nom.substring(nom.length() - 4, nom.length()).toLowerCase();

		}
		if (".apk".equals(ext)) {
			img1.setImageResource(R.drawable.imgmd5);
		} else {
			img1.setImageResource(R.drawable.folder);
		}

		return convertView;

	}

	public int getCount() {

		return data.size();

	}

	public Object getItem(int position) {

		return position;

	}

	public long getItemId(int position) {

		return position;

	}
}
