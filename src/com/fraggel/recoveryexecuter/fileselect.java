package com.fraggel.recoveryexecuter;

import java.io.File;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class fileselect extends Activity implements IFileFolderItemListener
{
	
	
   private String initialDir;
   SharedPreferences sp;
   AlertDialog diag;
   public void onCreate(Bundle savedInstanceState) {
	   diag=new AlertDialog.Builder(this).create();
   	try{
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.fileselect);
	   setTitle(getResources().getString(R.string.selecarchivo));
	   
	   initialDir= "/mnt/sdcard/Download/";
	   FileFolderLayout localFileFolders = (FileFolderLayout)findViewById(R.id.localfilefolders);
	   localFileFolders.setIFolderItemListener(this);
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
	protected void onActivityResult(int request,int result,Intent data){
		super.onActivityResult(request,result,data);
	}
	public void OnFileClicked(File file) {

		try {
			
		
			/*edittext=(EditText)findViewById(R.id.editText);
			edittext.setText(file.getPath());
			initialDir=file.getPath();*/
			if(!file.isDirectory()){
			Intent it=new Intent();
			it.putExtra("file",file.getPath());
			setResult(Activity.RESULT_OK,it);
			finish();
			
			}
		} catch (Exception e) {
			new REException(e);
			

		}

        // TODO Auto-generated method stub
    }
	public void OnCannotFileRead(File file)
	{
		// TODO: Implement this method
	}
	public boolean onCreateOptionsMenu(Menu menu){
		try{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menufile, menu);
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
