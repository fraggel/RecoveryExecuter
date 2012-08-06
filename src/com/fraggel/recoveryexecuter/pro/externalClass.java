package com.fraggel.recoveryexecuter.pro;
import java.io.BufferedOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

public class externalClass extends Activity{

	public externalClass() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Intent initialBackup(MainActivity mainActivity){
		Intent intent=null;
		try {
			intent = new Intent(mainActivity, backupRestore.class);
			
		} catch (Exception e) {
			new REException(e);
		}
		return intent;
	}
	public void anyadir(HashMap<String, String> map,ArrayList<String> listaAcciones,AlertDialog diag,Resources res,String file,String selected,String[] items,String[] values,List<HashMap<String, String>> fillMaps,ListView lista,SimpleAdapter adapt,crearLista crearLista){
		map = new HashMap<String, String>();

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

		lista.setAdapter(adapt);

	}
	public void instalarAPK(String ficheroAPK,Resources res,AlertDialog diag,RadioButton rdbNormal,RadioButton rdbSistema,install install) {
		try {

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
		} catch (Exception e) {
			new REException(e);

		}

	}
	public String backup(Resources res,AlertDialog diag,OnClickListener onClickListener) {
		String cadena="";
		BufferedOutputStream bos=null;
		try{
		Runtime rt=Runtime.getRuntime();
		Process exec = rt.exec("su");
		bos= new BufferedOutputStream(exec.getOutputStream());
		File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/RecoveryExecuter/");
		if(fff.exists()){
			File[] listFiles = fff.listFiles();
			if(listFiles!=null && listFiles.length>0){
				diag.setMessage(res.getString(R.string.msgExisteBackup));
				diag.setButton(AlertDialog.BUTTON_NEGATIVE,
						res.getString(R.string.cancelar),onClickListener);
				diag.setButton(AlertDialog.BUTTON_POSITIVE,
						res.getString(R.string.aceptar),onClickListener);
				diag.show();
			}else{
				cadena=("echo 'backup_rom(\""+ fff.getPath()+"\");' >> /cache/recovery/extendedcommand\n");
			}
		}else{
			fff.mkdirs();
			cadena=("echo 'backup_rom(\""+ fff.getPath()+"\");' >> /cache/recovery/extendedcommand\n");
		}
		
		if(cadena!=null && !"".equals(cadena)){
			bos.write(("reboot recovery").getBytes());
			cadena="";
		}
		}catch(Exception e){
			new REException(e);
		}finally{
			try{
			bos.flush();
			bos.close();
			}catch(Exception e){
				new REException(e);
			}
		}
		return cadena;
		//Comprobar si existe ya backup y avisar, dar opci�n a borrar
	}
	public String restore(Resources res,AlertDialog diag,OnClickListener onClickListener) {
		String cadena="";
		BufferedOutputStream bos=null;
		try{
		Runtime rt=Runtime.getRuntime();
		Process exec = rt.exec("su");
		bos= new BufferedOutputStream(exec.getOutputStream());
		File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/RecoveryExecuter/");
		if(fff.exists()){
			File[] listFiles = fff.listFiles();
			if(listFiles==null || listFiles.length<=0){
				diag.setMessage(res.getString(R.string.msgNoExisteBackup));
				diag.show();
			}else{
				cadena=("echo 'restore_rom(\""+ fff.getPath()+"\");' >> /cache/recovery/extendedcommand\n");
			}
		}else{
			diag.setMessage(res.getString(R.string.msgNoExisteBackup));
			diag.show();
		}
		
		if(cadena!=null && !"".equals(cadena)){
			bos.write(("reboot recovery").getBytes());
			cadena="";
		}
		}catch(Exception e){
			new REException(e);
		}finally{
			try{
			bos.flush();
			bos.close();
			}catch(Exception e){
				new REException(e);
			}
		}
		return cadena;
		
	}
	public String backupMain(Resources res,AlertDialog diag,OnClickListener onClickListener) {
		String cadena="";
		BufferedOutputStream bos=null;
		try{
		Runtime rt=Runtime.getRuntime();
		Process exec = rt.exec("su");
		bos= new BufferedOutputStream(exec.getOutputStream());
		File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/RecoveryExecuter/");
		if(fff.exists()){
			File[] listFiles = fff.listFiles();
			if(listFiles!=null && listFiles.length>0){
				diag.setMessage(res.getString(R.string.msgExisteBackup));
				diag.setButton(AlertDialog.BUTTON_NEGATIVE,
						res.getString(R.string.cancelar),onClickListener);
				diag.setButton(AlertDialog.BUTTON_POSITIVE,
						res.getString(R.string.aceptar),onClickListener);
				diag.show();
			}else{
				cadena=("echo 'backup_rom(\""+ fff.getPath()+"\");' >> /cache/recovery/extendedcommand\n");
			}
		}else{
			fff.mkdirs();
			cadena=("echo 'backup_rom(\""+ fff.getPath()+"\");' >> /cache/recovery/extendedcommand\n");
		}
		}catch(Exception e){
			new REException(e);
		}finally{
			try{
			bos.flush();
			bos.close();
			}catch(Exception e){
				new REException(e);
			}
		}
		return cadena;
		//Comprobar si existe ya backup y avisar, dar opci�n a borrar
	}
	public String restoreMain(Resources res,AlertDialog diag,OnClickListener onClickListener) {
		String cadena="";
		try{
			File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/RecoveryExecuter/");
			if(fff.exists()){
				File[] listFiles = fff.listFiles();
				if(listFiles==null || listFiles.length<=0){
					diag.setMessage(res.getString(R.string.msgExisteBackup));
					diag.setButton(AlertDialog.BUTTON_NEGATIVE,
							res.getString(R.string.cancelar),onClickListener);
					diag.setButton(AlertDialog.BUTTON_POSITIVE,
							res.getString(R.string.aceptar),onClickListener);
					diag.show();
				}else{
					cadena=("echo 'restore_rom(\""+ fff.getPath()+"\");' >> /cache/recovery/extendedcommand\n");
				}
			}else{
				diag.setMessage(res.getString(R.string.msgNoExisteBackup));
				diag.show();
			}
		}catch(Exception e){
			new REException(e);
		}
		return cadena;
		
	}

}