package com.fraggel.recoveryexecuter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fraggel.recoveryexecuter.pro.MainActivity;
import com.fraggel.recoveryexecuter.pro.crearLista;
import com.fraggel.recoveryexecuter.pro.install;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

public interface iLiteproabstract {
	
	public Intent initialBackup(MainActivity mainActivity,Resources res,AlertDialog diag);
	public void anyadir(HashMap<String, String> map,ArrayList<String> listaAcciones,AlertDialog diag,Resources res,String file,String selected,String[] items,String[] values,List<HashMap<String, String>> fillMaps,ListView lista,SimpleAdapter adapt,String nomBck,crearLista crearLista);
	public void instalarAPK(String ficheroAPK,Resources res,AlertDialog diag,RadioButton rdbNormal,RadioButton rdbSistema,install install);
	public String backup(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck);
	public String restore(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck);
	public String backupMain(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck);
	public String restoreMain(Resources res,AlertDialog diag,OnClickListener onClickListener,String nombreBck,boolean temporal);
	public String buscarCWMySustituirRutas(String fichero);
	public String optionSelect(String selected,AlertDialog diag,OnClickListener onClickListener,Resources res);
}
