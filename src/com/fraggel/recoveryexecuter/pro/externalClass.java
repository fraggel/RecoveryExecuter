package com.fraggel.recoveryexecuter.pro;
import java.io.BufferedOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.fraggel.recoveryexecuter.iLiteproabstract;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

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
		try {
			intent = new Intent(mainActivity, backupRestore.class);
			
		} catch (Exception e) {
			new REException(e);
		}
		return intent;
	}
	public void anyadir(HashMap<String, String> map,ArrayList<String> listaAcciones,AlertDialog diag,Resources res,String file,String selected,String[] items,String[] values,List<HashMap<String, String>> fillMaps,ListView lista,SimpleAdapter adapt,String nomBck,crearLista crearLista){
		map = new HashMap<String, String>();

		if (file != null && !"".equals(file)) {
			listaAcciones.add(file);
			File ff = new File(file);
			selected = selected + " " + ff.getName();
		} else if (!"".equals(selected)) {
			if(nomBck==null || "".equals(nomBck)){
				int r = -1;
				for (int x = 0; x < items.length; x++) {
					if (selected.equals(items[x])) {
						r = x;
					}
				}
				listaAcciones.add(values[r]);
			}else{
				int r = -1;
				for (int x = 0; x < items.length; x++) {
					if (selected.equals(items[x])) {
						r = x;
					}
				}
				listaAcciones.add(values[r]+"-"+nomBck);
				selected = selected + " " + nomBck;
			}
		}
		if (!"".equals(selected)) {
			map.put("rowtexts", selected);
			fillMaps.add(map);
		}
		file = "";
		nomBck="";
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
	public String backup(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck) {
		String cadena="";
		BufferedOutputStream bos=null;
		try{
		Runtime rt=Runtime.getRuntime();
		Process exec = rt.exec("su");
		
		bos= new BufferedOutputStream(exec.getOutputStream());
		
			bos.write(("rm /cache/recovery/extendedcommand\n")
					.getBytes());
		String fabricante=Build.BRAND;
		String procesador=Build.HARDWARE;
		if("JIAYU".equals(fabricante.toUpperCase().trim())||procesador.toUpperCase().indexOf("MT")!=-1){
			prepPartitionsJIAYU(bos);
		}else{
			prepPartitionsI9300(bos);
		}	
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
			//fff.mkdirs();
			cadena=("echo 'backup_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
		}
		
		if(cadena!=null && !"".equals(cadena)){
		 	bos.write(cadena.getBytes());
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
			bos.write(("rm /cache/recovery/extendedcommand\n")
						.getBytes());	
			String fabricante=Build.BRAND;
			String procesador=Build.HARDWARE;
			if("JIAYU".equals(fabricante.toUpperCase().trim())||procesador.toUpperCase().indexOf("MT")!=-1){
				prepPartitionsJIAYU(bos);
			}else{
				prepPartitionsI9300(bos);
			}
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
				bos.write(cadena.getBytes());
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
			if(nombreBck==null || "".equals(nombreBck)){
				nombreBck=(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"-"+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"+Calendar.getInstance().get(Calendar.YEAR)+" "+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":"+Calendar.getInstance().get(Calendar.MINUTE)+":"+Calendar.getInstance().get(Calendar.SECOND));
			}
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
				//fff.mkdirs();
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
	public String restoreMain(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck,boolean temporal) {
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
				if(!temporal){
					diag.setMessage(res.getString(R.string.msgNoExisteBackup));
					diag.show();	
				}else{
					cadena=("echo 'restore_rom(\""+ buscarCWMySustituirRutas(fff.getPath())+"\");' >> /cache/recovery/extendedcommand\n");
				}
				
			}
		}catch(Exception e){
			new REException(e);
		}
		return cadena;
		
	}
	public String buscarCWMySustituirRutas(String fichero){
		String rutCWM="";
		String fabricante=Build.BRAND;
		String procesador=Build.HARDWARE;
		if("JIAYU".equals(fabricante.toUpperCase().trim())||procesador.toUpperCase().indexOf("MT")!=-1){
			if(sdCard!=null){
				fichero=fichero.replaceFirst(sdCard.getPath(),"/sdcard");
			}
			if(extSdCard!=null){
				fichero=fichero.replaceFirst(extSdCard.getPath(),"sdcard");
			}
			fichero=fichero.replaceFirst("/mnt/sdcard/","/sdcard/").replaceFirst("/mnt/extSdCard/","/sdcard/");
		}else{
			if(sdCard!=null){
				fichero=fichero.replaceFirst(sdCard.getPath(),"/data/media");
			}
			if(extSdCard!=null){
				fichero=fichero.replaceFirst(extSdCard.getPath(),"/data/media/external");
			}
			fichero=fichero.replaceFirst("/mnt/sdcard/","/data/media/").replaceFirst("/mnt/extSdCard/","/data/media/external/");	
		}
		rutCWM=fichero;
		return rutCWM;
	}
	public String optionSelect(String selected, AlertDialog diag,
			OnClickListener onClickListener, Resources res) {
		return selected;
	}
	private void prepPartitionsI9300(
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
	private void prepPartitionsJIAYU(
			BufferedOutputStream bos) {
			ArrayList<String> listaVold=new ArrayList<String>();
			ArrayList<String> mMounts=new ArrayList<String>();
			listaVold=StorageOptions.listaVoldF;
			mMounts=StorageOptions.mMountsF;
			
			try {
				bos.write(("echo 'run_program(\"/sbin/umount\",\"/sdcard\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/mount\",\"/sdcard\");' >> /cache/recovery/extendedcommand\n").getBytes());
				
			} catch (Exception e) {
				
			}
			
		
	}
}
