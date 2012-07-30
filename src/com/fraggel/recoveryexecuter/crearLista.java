package com.fraggel.recoveryexecuter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class crearLista extends Activity implements OnItemSelectedListener {

	AlertDialog diag;
	String selected;
	ArrayList<String> listaAcciones =new ArrayList<String>();
	ListView lista;
	String file;
	List<HashMap<String, String>> fillMaps;
	HashMap<String, String> map;
	SimpleAdapter adapt ;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		diag = new AlertDialog.Builder(this).create();
		lista = (ListView) findViewById(R.id.listaAcciones);
		
		fillMaps = new ArrayList<HashMap<String, String>>();

		String[] from = new String[] {"rowtexts"};
		int[] to = new int[] { R.id.rowtexts};
		
		adapt= new SimpleAdapter(this, fillMaps,	R.layout.rowlist, from, to);

		Spinner spinner = (Spinner) findViewById(R.id.comboAcciones);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.acciones, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	public void guardarLista(View v){
		Intent it=new Intent();
		it.putStringArrayListExtra("lista",listaAcciones);
		setResult(Activity.RESULT_OK,it);
		finish();
	}
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// An item was selected. You can retrieve the selected item using
		selected = parent.getItemAtPosition(pos).toString();
		if("Selecciona una accion".equals(selected)){
			
		}else if("Flashear Archivo".equals(selected)){
			showFileChooser();
		}
		
	}
	private void showFileChooser()
	{
		try
		{
        	Intent intent=new Intent(this, fileselect.class);
			startActivityForResult(intent, 0);
		}
		catch (Exception e)
		{
			diag.setMessage(e.getMessage());
			diag.show();
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	public void anyadir(View v) {
		map = new HashMap<String, String>();
		
		if(!"".equals(file)){
			listaAcciones.add(file);
			File ff=new File(file);
			selected=selected+" "+ff.getName();
		}else{
			listaAcciones.add(selected);
		}
		map.put("rowtexts", selected);
		file="";
		fillMaps.add(map);
		lista.setAdapter(adapt);	
	}
	protected void onActivityResult(int request, int result, Intent data)
	{
		try
		{
			switch (request)
			{
				case 0:
					if (result == RESULT_OK)
					{
						file = data.getStringExtra("file");
					}
					break;
			}
		}
		catch (Exception e)
		{
			diag.setMessage(e.getMessage());
			diag.show();
		}
		super.onActivityResult(request, result, data);
	}

}
