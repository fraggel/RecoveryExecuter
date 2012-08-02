package com.fraggel.recoveryexecuter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

public class externalClass extends Activity{

	public externalClass() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void anyadir(HashMap<String, String> map,ArrayList<String> listaAcciones,AlertDialog diag,Resources res,String file,String selected,String[] items,String[] values,List<HashMap<String, String>> fillMaps,ListView lista,SimpleAdapter adapt,crearLista crearLista){
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

}
