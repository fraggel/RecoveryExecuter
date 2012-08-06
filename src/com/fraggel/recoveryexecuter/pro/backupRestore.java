package com.fraggel.recoveryexecuter.pro;


import java.io.BufferedOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioButton;

public class backupRestore extends Activity implements OnItemSelectedListener,
AdapterView.OnItemClickListener,DialogInterface.OnClickListener {
	AlertDialog diag;
	Resources res;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		res = this.getResources();
		setContentView(R.layout.backuprestore);
		diag = new AlertDialog.Builder(this).create();
	}

	public void salir(View v) {
		finish();

	}
	
	public void makeBackupRestore(View v) {
		RadioButton rdbBck = (RadioButton) findViewById(R.id.rdbbackup);
		RadioButton rdbRes = (RadioButton) findViewById(R.id.rdbrestore);
		String cad="";
		externalClass exCl=new externalClass();
		if(rdbBck.isChecked()){
			cad=exCl.backup(res,diag,this);
			finish();
		}else if(rdbRes.isChecked()){
			cad=exCl.restore(res,diag,this);
			finish();
		}	
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
