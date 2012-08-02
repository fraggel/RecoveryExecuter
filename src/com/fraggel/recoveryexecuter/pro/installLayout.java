package com.fraggel.recoveryexecuter.pro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fraggel.recoveryexecuter.R;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class installLayout extends LinearLayout implements
		AdapterView.OnItemClickListener {

	Context context;
	IAPKItemListener folderListener;
	private List<String> item = null;
	private List<String> path = null;
	private String root = "/mnt";
	private TextView myPath;
	private ListView lstView;
	AlertDialog diag;

	public installLayout(Context context, AttributeSet attrs) {

		super(context, attrs);
		// TODO Auto-generated constructor stub
		try {
			this.context = context;

			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.apkfolderview, this);
			myPath = (TextView) findViewById(R.id.filepathapk);
			lstView = (ListView) findViewById(R.id.filelistapk);
			// folderListener=
			getDir(root, lstView);
		} catch (Exception e) {
			new REException(e);

		}

	}

	public void setIAPKItemListener(IAPKItemListener folderItemListener) {
		this.folderListener = folderItemListener;
	}

	// Set Directory for view at anytime
	public void setDir(String dirPath) throws Exception {
		getDir(dirPath, lstView);
	}

	private void getDir(String dirPath, ListView v) throws Exception {

		item = new ArrayList<String>();
		path = new ArrayList<String>();
		myPath.setText(dirPath);
		File f = new File(dirPath);
		if (!f.isDirectory()) {
			f = f.getParentFile();
		}
		File[] files = f.listFiles();
		java.util.Arrays.sort(files);

		if (!dirPath.equals(root)) {
			item.add("../");
			path.add(f.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				path.add(file.getPath());
				item.add(file.getName() + "/");
			}
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			String ext = "";
			if (!file.isDirectory()) {
				ext = file.getName().substring(file.getName().length() - 4,
						file.getName().length());
			}

			if (!file.isDirectory() && (".apk".equals(ext.toLowerCase()))) {
				path.add(file.getPath());
				item.add(file.getName());
			}
		}
		setItemList(item);
	}

	// can manually set Item to display, if u want
	public void setItemList(List<String> item) {
		try {
			miAdapterApk fileList = new miAdapterApk(context, item);
			// ArrayAdapter<String> fileList = new
			// ArrayAdapter<String>(context,R.layout.filerow, item);

			lstView.setAdapter(fileList);
			lstView.setOnItemClickListener(this);
		} catch (Exception e) {
			new REException(e);
		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		try {
			File r = new File(path.get(position));
			if (folderListener != null && !r.isDirectory()) {
				folderListener.OnFileClicked(r);
			} else {
				folderListener = null;
			}

			if (r.canRead()) {
				getDir(path.get(position), l);
			}
		} catch (Exception e) {
			new REException(e);
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		try {
			onListItemClick((ListView) arg0, arg0, arg2, arg3);
		} catch (Exception e) {
			new REException(e);

		}
	}
}
