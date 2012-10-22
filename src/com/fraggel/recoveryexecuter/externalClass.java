package com.fraggel.recoveryexecuter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

public class externalClass extends Activity implements iLiteproabstract{
	private File sdCard;
	private File extSdCard;
	public externalClass() {
		super();
		sdCard=Environment.getExternalStorageDirectory();
		
		StorageOptions.determineStorageOptions();
		ArrayList<String> mounts = StorageOptions.getMounts();
		if(mounts!=null && mounts.size()>0){
			extSdCard=new File(mounts.get(0));	
		}else{
			extSdCard=null;
		}
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
	public void anyadir(HashMap<String, String> map,
			ArrayList<String> listaAcciones, AlertDialog diag, Resources res,
			String file, String selected, String[] items, String[] values,
			List<HashMap<String, String>> fillMaps, ListView lista,
			SimpleAdapter adapt, String nomBck, crearLista crearLista){
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
	public String backup(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck) {
		String cadena="";
		BufferedOutputStream bos=null;
		try{
		Runtime rt=Runtime.getRuntime();
		Process exec = rt.exec("su");
		bos= new BufferedOutputStream(exec.getOutputStream());
		File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/"+nombreBck+"/");
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
				cadena=("echo 'backup_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
			}
		}else{
			fff.mkdirs();
			cadena=("echo 'backup_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
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
		//Comprobar si existe ya backup y avisar, dar opción a borrar
	}
	public String restore(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck) {
		String cadena="";
		BufferedOutputStream bos=null;
		try{
		Runtime rt=Runtime.getRuntime();
		Process exec = rt.exec("su");
		bos= new BufferedOutputStream(exec.getOutputStream());
		File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/"+nombreBck+"/");
		if(fff.exists()){
			File[] listFiles = fff.listFiles();
			if(listFiles==null || listFiles.length<=0){
				diag.setMessage(res.getString(R.string.msgNoExisteBackup));
				diag.show();
			}else{
				cadena=("echo 'restore_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
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
	public String backupMain(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck) {
		String cadena="";
		BufferedOutputStream bos=null;
		try{
		Runtime rt=Runtime.getRuntime();
		Process exec = rt.exec("su");
		bos= new BufferedOutputStream(exec.getOutputStream());
		File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/"+nombreBck+"/");
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
				cadena=("echo 'backup_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
			}
		}else{
			fff.mkdirs();
			cadena=("echo 'backup_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
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
		//Comprobar si existe ya backup y avisar, dar opción a borrar
	}
	public String restoreMain(Resources res, AlertDialog diag,
			OnClickListener onClickListener, String nombreBck, boolean temporal) {
		String cadena="";
		try{
			File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/"+nombreBck+"/");
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
					cadena=("echo 'restore_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
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
	public String optionSelect(String selected,AlertDialog diag,OnClickListener onClickListener,Resources res) {
		if("6".equals(selected)||"7".equals(selected)){
			diag.setMessage(res.getString(R.string.msgNoFull));
			diag.setButton(AlertDialog.BUTTON_NEGATIVE,
					res.getString(R.string.cancelar),onClickListener);
			diag.setButton(AlertDialog.BUTTON_POSITIVE,
					res.getString(R.string.aceptar),onClickListener);
			diag.show();
			selected="0";
		}
		return selected;
	}
	public String buscarCWMySustituirRutas(String fichero){
		String rutCWM="";
		rutCWM=fichero.replaceFirst("/mnt/sdcard/","/data/media/").replaceFirst("/mnt/extSdCard/","/data/media/external/").replaceFirst(sdCard.getPath(),"/data/media").replaceFirst(extSdCard.getPath(),"/data/media/external");
		return rutCWM;
	}
	private void prepPartitions(
			BufferedOutputStream bos) {
			ArrayList<String> listaVold=new ArrayList<String>();
			ArrayList<String> mMounts=new ArrayList<String>();
			listaVold=StorageOptions.listaVoldF;
			mMounts=StorageOptions.mMountsF;
			
			try {
				bos.write(("echo 'run_program(\"/sbin/busybox\",\"mount\",\"-a\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/busybox\",\"mount\",\"-t\",\"auto\",\""+listaVold.get(0)+"\",\"/data/\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/mkdir\",\"/data/media/\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/busybox\",\"mount\",\"-t\",\"auto\",\""+listaVold.get(0)+"\",\"/data/media/\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/mkdir\",\"/data/media/external/\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/busybox\",\"mount\",\"-t\",\"auto\",\""+listaVold.get(1)+"\",\"/data/media/external/\");' >> /cache/recovery/extendedcommand\n").getBytes());
				
			} catch (Exception e) {
				
			}
			
		
	}
}
