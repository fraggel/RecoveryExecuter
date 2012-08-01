package com.fraggel.recoveryexecuter;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

public class ayuda extends Activity {
	AlertDialog diag;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ayuda);
		setTitle("Ayuda");
		diag = new AlertDialog.Builder(this).create();
	}

	public void salir(View v) {
		finish();

	}
}
