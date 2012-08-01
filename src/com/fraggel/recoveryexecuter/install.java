package com.fraggel.recoveryexecuter;

import java.io.BufferedOutputStream;
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class install extends Activity implements IAPKItemListener {

	String ficheroAPK;
	private String initialDir;
	SharedPreferences sp;
	AlertDialog diag;
	Resources res;
	File root;

	public void onCreate(Bundle savedInstanceState) {
		diag = new AlertDialog.Builder(this).create();
		try {
			res = getResources();
			root = Environment.getExternalStorageDirectory();
			super.onCreate(savedInstanceState);
			setContentView(R.layout.apkselect);

			setTitle(res.getString(R.string.selecarchivo));

			initialDir = root.getAbsolutePath()+"/Download/";
			installLayout localFileFolders = (installLayout) findViewById(R.id.apkfilefolders);
			localFileFolders.setIAPKItemListener(this);
			localFileFolders.setDir("/mnt");
			try {
				sp = getSharedPreferences("recexec",
						Context.MODE_WORLD_WRITEABLE);
				initialDir = sp.getString("url", root.getAbsolutePath()+"/Download/");
				localFileFolders.setDir(initialDir);

			} catch (Exception e) {
				new REException(e);

			}

		} catch (Exception e) {

			new REException(e);

		}

	}

	public void instalarAPK(View v) {
		try {
			RadioButton rdbNormal = (RadioButton) findViewById(R.id.rdbnormal);
			RadioButton rdbSistema = (RadioButton) findViewById(R.id.rdbsystem);
			if (ficheroAPK != null && !"".equals(ficheroAPK)
					&& ficheroAPK.toLowerCase().endsWith(".apk")) {
				if (rdbNormal.isChecked()) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(ficheroAPK)),
							"application/vnd.android.package-archive");
					startActivity(intent);
				} else if (rdbSistema.isChecked()) {
					Runtime runtime = Runtime.getRuntime();
					Process exec = runtime.exec("su");
					BufferedOutputStream outputStream = new BufferedOutputStream(
							exec.getOutputStream());
					outputStream.write(("mount -o rw,remount /system\n")
							.getBytes());
					outputStream
							.write(("busybox cp \"" + ficheroAPK + "\" /system/app/\n")
									.getBytes());
					outputStream.flush();
					outputStream.write(("mount -o ro,remount /system\n")
							.getBytes());
					outputStream.flush();
					outputStream.close();
					diag.setMessage(res.getString(R.string.msgApkInstalada));
					diag.show();
				}
			}
		} catch (Exception e) {
			new REException(e);

		}

	}

	protected void onActivityResult(int request, int result, Intent data) {
		super.onActivityResult(request, result, data);
	}

	public void OnFileClicked(File file) {

		try {
			ficheroAPK = file.getPath();
		} catch (Exception e) {
			new REException(e);

		}
	}

	public void OnCannotFileRead(File file) {
		// TODO: Implement this method
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menuinstallapk, menu);
		} catch (Exception e) {
			new REException(e);

		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean ret = false;
		try {
			switch (menuitem.getItemId()) {
			case R.id.opinstall1:
				finish();
				ret = true;
				break;
			default:
				ret = false;
				break;
			}
		} catch (Exception e) {
			new REException(e);

		}
		return ret;
	}
}
