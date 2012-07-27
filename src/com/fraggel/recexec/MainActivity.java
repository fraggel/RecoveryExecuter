package com.fraggel.recexec;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.content.*;
import android.net.*;
import android.nfc.*;
import android.util.*;
import java.util.*;

public class MainActivity extends Activity
{
    private static final int FILE_SELECT_CODE=0;
	private String file="";
	private String initialDir="/mnt/sdcard/Download/";
	private SharedPreferences sp;
    AlertDialog dialog;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		dialog=new AlertDialog.Builder(this).create();
		try{
		super.setTitle("RecoveryExecuter 1.2.0");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		}catch(Exception e ){
			dialog.setMessage(e.getMessage());
			dialog.show();
		}
		//showFileChooser();
    }
	public void Flash(View v){
		showFileChooser();
	}
	
	private void showFileChooser(){
		try{
			sp=getSharedPreferences("recexec",Context.MODE_WORLD_WRITEABLE);
			initialDir=sp.getString("url","/mnt/sdcard/Download/");
			
        	Intent intent=new Intent(this,fileselect.class);
			startActivityForResult(intent,FILE_SELECT_CODE);
        	}catch (Exception e)
		{
			dialog.setMessage(e.getMessage());
			dialog.show();
		}
	}
	private void showConfig(){
		try{
			Intent intent=new Intent(this,config.class);
			startActivity(intent);
		}catch (Exception e)
		{}
	}
	public boolean onCreateOptionsMenu(Menu menu){
		try{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menuflasher,menu);
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
			showFileChooser();
			ret= true;
			break;
			case R.id.op2:
			showConfig();
			ret= true;
			break;
			case R.id.op3:
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
	protected void onActivityResult(int request,int result,Intent data){
		try{
		switch(request){
			case FILE_SELECT_CODE:
			if(result==RESULT_OK){
				file=data.getStringExtra("file");
				/*Uri uri=data.getData();
				file=uri.getPath();*/
	
				dialog.setMessage("Se va a flashear el archivo "+file);
				dialog.setButton2("Cancelar",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int witch){
						//finish();
						}
					});
				dialog.setButton("Aceptar",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int witch){
						try
						{
							if(!"".equals(file)){
								crearZipCwm();
								escribirRecovery();
							}
						}
						catch (Exception e)
						{}
					}
				});
				dialog.show();
			}
			break;
		}
		}catch (Exception e)
		{
			dialog.setMessage(e.getMessage());
			dialog.show();
		}
		super.onActivityResult(request,result,data);
	}
	public void escribirRecovery() throws Exception{
		
		Runtime rt=Runtime.getRuntime();
		java.lang.Process p=rt.exec("su");
		CheckBox chkdata=(CheckBox)findViewById(R.id.wipedata);
		CheckBox chkcache=(CheckBox)findViewById(R.id.wipecache);
		CheckBox chkdalvik=(CheckBox)findViewById(R.id.wipedalvik);
		BufferedOutputStream bos=new BufferedOutputStream(p.getOutputStream());
		bos.write(("rm /cache/recovery/extendedcommand\n").getBytes());
		if(chkdata.isChecked()){
			bos.write(("echo 'Wipe Data'\n").getBytes());
			bos.write(("echo 'format (\"/data\");' > /cache/recovery/extendedcommand\n").getBytes());
		}
		if(chkcache.isChecked()){
			bos.write(("echo 'Wipe Cache'\n").getBytes());
			bos.write(("echo 'format (\"/cache\");' >> /cache/recovery/extendedcommand\n").getBytes());
		}
		if(chkdalvik.isChecked()){
			//bos.write(("echo 'rm -r \"/data/dalvik-cache\";\n' >> /cache/recovery/extendedcommand\n").getBytes());
			bos.write(("rm -r \"/data/dalvik-cache\"\n").getBytes());
		}
		bos.write(("echo 'install_zip(\""+file.replaceFirst("/mnt/sdcard/","/emmc/").replaceFirst("/mnt/extSdCard/","/sdcard/")+"\");' >> /cache/recovery/extendedcommand\n").getBytes());
		bos.write(("reboot recovery").getBytes());
		bos.flush();
		bos.close();
	}
	public void crearZipCwm() throws Exception{
		
	}
}
