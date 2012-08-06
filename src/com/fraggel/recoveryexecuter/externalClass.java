package com.fraggel.recoveryexecuter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

public class externalClass extends Activity implements OnItemSelectedListener,
AdapterView.OnItemClickListener,DialogInterface.OnClickListener {

	public externalClass() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Intent initialBackup(MainActivity mainActivity,Resources res,AlertDialog diag){
		Intent intent=null;
		
		diag.setMessage(res.getString(R.string.msgNoFull));
		diag.setButton(AlertDialog.BUTTON_NEGATIVE,
				res.getString(R.string.cancelar),mainActivity);
		diag.setButton(AlertDialog.BUTTON_POSITIVE,
				res.getString(R.string.aceptar),mainActivity);
		diag.show();
		return intent;
	}
	public void anyadir(HashMap<String, String> map,ArrayList<String> listaAcciones,AlertDialog diag,Resources res,String file,String selected,String[] items,String[] values,List<HashMap<String, String>> fillMaps,ListView lista,SimpleAdapter adapt,crearLista crearLista){
		map = new HashMap<String, String>();
		if(listaAcciones.size()==3){
			diag.setMessage(res.getString(R.string.msgNoFullLista));
			diag.setButton(AlertDialog.BUTTON_NEGATIVE,
					res.getString(R.string.cancelar),crearLista);
			diag.setButton(AlertDialog.BUTTON_POSITIVE,
					res.getString(R.string.aceptar),crearLista);
			diag.show();
		}
		else{
			if (file != null && !"".equals(file)) {
				listaAcciones.add(file);
				File ff = new File(file);
				selected = selected + " " + ff.getName();
			} else if (!"".equals(selected)) {
				int r = -1;
				for (int x = 0; x < items.length; x++) {
					if (selected.equals(items[x])) {
						r = x;
					}
				}
				listaAcciones.add(values[r]);
			}
			if (!"".equals(selected)) {
				map.put("rowtexts", selected);
				fillMaps.add(map);
			}
			file = "";
		}
		lista.setAdapter(adapt);

	}
	public void instalarAPK(String ficheroAPK,Resources res,AlertDialog diag,RadioButton rdbNormal,RadioButton rdbSistema,install install) {
		try {
			diag.setMessage(res.getString(R.string.msgNoFull));
			diag.setButton(AlertDialog.BUTTON_NEGATIVE,
					res.getString(R.string.cancelar),install);
			diag.setButton(AlertDialog.BUTTON_POSITIVE,
					res.getString(R.string.aceptar),install);
			diag.show();
				
		} catch (Exception e) {
			new REException(e);

		}

	}
	public String backupRestore(CheckBox chkbackup, CheckBox chkrestore,
			CheckBox chkadvbackup, CheckBox chkadvrestore){
		String bos="";
		//restore_rom("/sdcard/clockworkmod/backup/2011-05-13-13.03.52", "boot", "system", "data", "cache", "sd-ext");
		if(chkbackup.isChecked()){
				bos=("echo 'backup_rom(\"/sdcard/clockworkmod/backup/RecoveryExecuter\");' >> /cache/recovery/extendedcommand\n");	
		}else if(chkrestore.isChecked()){
			bos=("echo 'restore_rom(\"/sdcard/clockworkmod/backup/RecoveryExecuter\");' >> /cache/recovery/extendedcommand\n");
		}else if(chkadvbackup.isChecked()){
			
		}else if(chkadvrestore.isChecked()){
			
		}
		
		return bos;
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
