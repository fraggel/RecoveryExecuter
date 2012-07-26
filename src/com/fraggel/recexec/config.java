package com.fraggel.recexec;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class config extends Activity  implements IFolderItemListener
{
	
   private EditText edittext;
   private String initialDir;
   SharedPreferences sp;
   AlertDialog diag;
   public void onCreate(Bundle savedInstanceState) {
   	try{
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.config);
	   setTitle("Configuracion");
	   diag=new AlertDialog.Builder(this).create();
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
	   /*acept=(Button)findViewById(R.id.acept);
	   browse=(Button)findViewById(R.id.browse);
	   browse.setOnClickListener(new View.OnClickListener(){
		   		public void onClick(View v){
					if(v.equals(browse)){
						showDirChooser();
					}
				}
				});
	   acept.setOnClickListener(new View.OnClickListener(){
			   public void onClick(View v){
				   if(v.equals(acept)){
					   changeProperties();
				   }
			   }
		   });*/
	   edittext.setText(initialDir);
	   /*acept.setText("Aceptar");
	   browse.setText("Navegar");*/
	}catch(Exception e){}
	   
   }
  /* private void showDirChooser(){
	   try{
        	Intent intent=new Intent(this,config.class);
		    startActivity(intent);
	   }catch (Exception e){
		diag.setMessage(e.getMessage());
		diag.show();
	   }
	}*/
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
		/*try{
		switch(request){
			case DIR_SELECT_CODE:
			if(result==RESULT_OK){
				Uri uri=data.getData();
				file=uri.getPath();
				
				diag.setMessage("Se va a usar la siguiente ruta como predeterminada "+file);
				diag.setButton2("Cancelar",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int witch){
						finish();
						}
					});
				diag.setButton("Aceptar",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int witch){
						edittext=(EditText)findViewById(R.id.editText);
						edittext.setText(file);
					}
				});
				diag.show();
			}
			break;
		}
		super.onActivityResult(request,result,data);
		}catch (Exception e)
		{}*/
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
