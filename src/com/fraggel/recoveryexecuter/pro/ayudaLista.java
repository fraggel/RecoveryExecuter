package com.fraggel.recoveryexecuter.pro;

import com.fraggel.recoveryexecuter.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

public class ayudaLista extends Activity {
	AlertDialog diag;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ayudalista);
		setTitle("Ayuda");
		diag = new AlertDialog.Builder(this).create();
	}

	public void salir(View v) {
		finish();

	}
}
