package com.fraggel.recoveryexecuter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class install extends Activity implements IAPKItemListener
{
	
	String ficheroAPK;
	private String initialDir;
	SharedPreferences sp;
	AlertDialog diag;
	public void onCreate(Bundle savedInstanceState) {
	diag=new AlertDialog.Builder(this).create();
   	try{
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.apkselect);
	   setTitle(getResources().getString(R.string.selecarchivo));
	   
	   initialDir= "/mnt/sdcard/Download/";
	   installLayout localFileFolders = (installLayout)findViewById(R.id.apkfilefolders);
	   localFileFolders.setIAPKItemListener(this);
	   localFileFolders.setDir("/mnt");
	   try{
		   sp=getSharedPreferences("recexec",Context.MODE_WORLD_WRITEABLE);
		   initialDir=sp.getString("url","/mnt/sdcard/Download/");
		   localFileFolders.setDir(initialDir);
		   
	   } catch(Exception e){
		   new REException(e);
			
	   }
	   
	}catch(Exception e){
		
		new REException(e);
		
	}
	   
   }
   public void instalarAPK(View v){
	   try {	
		    RadioButton rdbNormal=(RadioButton)findViewById(R.id.rdbnormal);
			RadioButton rdbSistema=(RadioButton)findViewById(R.id.rdbsystem);
			if(rdbNormal.isChecked()){
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(ficheroAPK)), "application/vnd.android.package-archive");
				startActivity(intent);  
			}else if(rdbSistema.isChecked()){
				Runtime runtime=Runtime.getRuntime();
				Process exec = runtime.exec("su\n");
				BufferedOutputStream outputStream =new BufferedOutputStream(exec.getOutputStream());
				outputStream.write(("mount -o rw,remount /system/app\n").getBytes());
				outputStream.flush();
				File rr=new File(ficheroAPK);
				diag.setMessage("/system/app/"+rr.getName());
				diag.show();
				File ff=new File("/system/app/"+rr.getName());
				copiarFichero(rr,ff);
				outputStream.write(("mount -o ro,remount /system/app\n").getBytes());
				outputStream.flush();
				outputStream.close();
			}
	   } catch (Exception e) {
			new REException(e);
			
	
		}
	   
   }
   private void copiarFichero(File origen, File destino) throws Exception
	{

		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(origen));
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(destino));
		int x=-1;

		x = bis.read();
		while (x != -1)
		{
			bos.write(x);
			x = bis.read();
		}
		bos.flush();
		bos.close();
	}
	protected void onActivityResult(int request,int result,Intent data){
		super.onActivityResult(request,result,data);
	}
	public void OnFileClicked(File file) {

		try {
			ficheroAPK=file.getPath();
		} catch (Exception e) {
			new REException(e);
			

		}
    }
	public void OnCannotFileRead(File file)
	{
		// TODO: Implement this method
	}
	public boolean onCreateOptionsMenu(Menu menu){
		try{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menuinstallapk, menu);
		}catch (Exception e)
		{
			new REException(e);
			
		}
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem menuitem){
		boolean ret =false;
		try
		{
		switch(menuitem.getItemId()){
			case R.id.opfile1:
			finish();
			ret= true;
			break;
			default:
			ret= false;
			break;
		}
		}catch (Exception e)
		{
			new REException(e);
			
		}
		return ret;
	}
}
