package com.fraggel.recoveryexecuter.pro;


import java.io.BufferedOutputStream;
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioButton;

public class backupRestore extends Activity implements OnItemSelectedListener,
AdapterView.OnItemClickListener,DialogInterface.OnClickListener {
	AlertDialog diag;
	Resources res;
	String nomBackup="";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		res = this.getResources();
		setContentView(R.layout.backuprestore);
		diag = new AlertDialog.Builder(this).create();
		try {
			Runtime rt = Runtime.getRuntime();
			java.lang.Process p = rt.exec("su");
			BufferedOutputStream bos = new BufferedOutputStream(
					p.getOutputStream());
			bos.write(("rm /cache/recovery/extendedcommand\n")
					.getBytes());	
		} catch (Exception e) {
			new REException(e);
		}
		
	}

	public void salir(View v) {
		finish();

	}
	
	public void makeBackupRestore(View v) {
		RadioButton rdbBck = (RadioButton) findViewById(R.id.rdbbackup);
		RadioButton rdbRes = (RadioButton) findViewById(R.id.rdbrestore);
		String cad="";
		externalClass exCl=new externalClass();
		try {
			if(rdbBck.isChecked()){
				Intent intent =new Intent(this,selectNameBck.class);
				startActivityForResult(intent, 1);
			}else if(rdbRes.isChecked()){
				AlertDialog.Builder b = new Builder(this);
				File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/");
			    b.setTitle(res.getString(R.string.rdbrestore));
			    String[] types =fff.list(); 
			    b.setItems(types,this);
			    b.setPositiveButton(res.getString(R.string.aceptar), null);
			    b.setNegativeButton(res.getString(R.string.cancelar), null);
			    b.show();
			}	
		}catch(Exception e){
			new REException(e);
		}
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		/*if(which==-1){
			externalClass exCl=new externalClass();
			if(nomBackup!=null && !"".equals(nomBackup)){
				diag.setMessage(nomBackup);
				diag.show();
				//exCl.restore(res,diag,this,"");
			}else{
				//Mensaje de nombre vacío
			}
				
		}else if(which==-2){
			
		}*/
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
	protected void onActivityResult(int request, int result, Intent data) {
		try {
			switch (request) {
			case 1:
				if (result == RESULT_OK) {
					nomBackup=data.getStringExtra("nomBck");
					externalClass exCl=new externalClass();
					exCl.backup(res,diag,this,nomBackup);
					finish();
				}else{
					nomBackup="";
				}
			}
		}catch(Exception e){
			new REException(e);
		}
		}
}
