package com.fraggel.recoveryexecuter;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;

public class acercade extends Activity{
	AlertDialog diag;
	Resources res;
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.acercade);
		 res=this.getResources();
		 diag=new AlertDialog.Builder(this).create();
	 }
	 public void salir(View v){
		 finish();
		 
	 }
	 public void enviarEmail(View v){
		 Intent i = new Intent(Intent.ACTION_SEND);
		 i.setType("message/rfc822");
		 i.putExtra(Intent.EXTRA_EMAIL  , new String[]{res.getString(R.string.email)});
		 i.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.version));
		 i.putExtra(Intent.EXTRA_TEXT   , "");
		 try {
		     startActivity(Intent.createChooser(i, res.getString(R.string.enviaEmail)));
		 } catch (android.content.ActivityNotFoundException ex) {
		     Toast.makeText(this, res.getString(R.string.noEmailCliente), Toast.LENGTH_SHORT).show();
		 }
	 }
	 public void enviarEmailErrores(View v){
		 File f=new File("/mnt/sdcard/RecoveryExecuter/exceptions.log");
		 if(f.exists()){
		 Intent i = new Intent(Intent.ACTION_SEND);
		 i.setType("message/rfc822");
		 i.putExtra(Intent.EXTRA_EMAIL  , new String[]{res.getString(R.string.email)});
		 i.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.envioErrores));
		 i.putExtra(Intent.EXTRA_TEXT   , res.getString(R.string.version));
		 i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///mnt/sdcard/RecoveryExecuter/exceptions.log"));
		 try {
		     startActivity(Intent.createChooser(i, res.getString(R.string.enviaEmail)));
		 } catch (android.content.ActivityNotFoundException ex) {
		     Toast.makeText(this, res.getString(R.string.noEmailCliente), Toast.LENGTH_SHORT).show();
		 }
		 }else{
			 diag.setMessage(res.getResourceName(R.string.noerrorlog));
			 diag.show();
		 }
	 }
}
