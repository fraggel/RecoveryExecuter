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
import android.view.View;
import android.widget.EditText;

public class config extends Activity implements IFolderItemListener
{
	
   private EditText edittext;
   private String initialDir;
   SharedPreferences sp;
   AlertDialog diag;
   public void onCreate(Bundle savedInstanceState) {
	   diag=new AlertDialog.Builder(this).create();
   	try{
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.config);
	   setTitle("Configuracion");
	   
	   initialDir= "/mnt/sdcard/Download/";
	   FolderLayout localFolders = (FolderLayout)findViewById(R.id.localfolders);
	   localFolders.setIFolderItemListener(this);
	   localFolders.setDir("/mnt");
	   try{
		   sp=getSharedPreferences("recexec",Context.MODE_WORLD_WRITEABLE);
		   initialDir=sp.getString("url","/mnt/sdcard/Download/");
		   
	   } catch(Exception e){
		   diag.setMessage(e.getMessage());
		   diag.show();
	   }
	   
	   edittext=(EditText)findViewById(R.id.editText);
	   edittext.setText(initialDir);
	}catch(Exception e){
		diag.setMessage(e.getMessage());
		diag.show();
	}
	   
   }
	public void changeProperties(View v){
		
		try
		{
			SharedPreferences.Editor editor=sp.edit();
			editor.putString("url",edittext.getText().toString());
			editor.commit();
			diag.setMessage("Ruta guardada correctamente");
		}
		catch (Exception e)
		{
			diag.setMessage(e.getMessage());
		}
		diag.show();
		finish();
	}
	protected void onActivityResult(int request,int result,Intent data){
		super.onActivityResult(request,result,data);
	}
	public void OnFileClicked(File file) {
		try{
			edittext=(EditText)findViewById(R.id.editText);
			edittext.setText(file.getPath());
			initialDir=file.getPath();
		}catch (Exception e)
		{}

        // TODO Auto-generated method stub
    }
	public void OnCannotFileRead(File file)
	{
		// TODO: Implement this method
	}
	public boolean onCreateOptionsMenu(Menu menu){
		try{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menuconfig,menu);
		}catch (Exception e)
		{}
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem menuitem){
		boolean ret =false;
		try
		{
		switch(menuitem.getItemId()){
			case R.id.op1:
			changeProperties(findViewById(R.layout.config));
			ret= true;
			break;
			case R.id.op2:
			finish();
			ret= true;
			break;
			default:
			ret= false;
			break;
		}
		}catch (Exception e)
		{}
		return ret;
	}
}
