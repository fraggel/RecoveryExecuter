package com.fraggel.recoveryexecuter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class crearLista extends Activity implements OnItemSelectedListener, AdapterView.OnItemClickListener {

	AlertDialog diag;
	String selected;
	ArrayList<String> listaAcciones =new ArrayList<String>();
	ListView lista;
	String file;
	List<HashMap<String, String>> fillMaps;
	HashMap<String, String> map;
	SimpleAdapter adapt ;
	Spinner spinner;
	String[] from = new String[] {"rowtexts"};
	int[] to = new int[] { R.id.rowtexts};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista);
		setTitle(R.string.version);
		diag = new AlertDialog.Builder(this).create();
		lista = (ListView) findViewById(R.id.listaAcciones);
		
		fillMaps = new ArrayList<HashMap<String, String>>();
		

		Intent intent = this.getIntent();
		ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("lista");
		if(stringArrayListExtra!=null && stringArrayListExtra.size()>0){
			listaAcciones=stringArrayListExtra;
		}
		reconstruirMap(listaAcciones);
		adapt= new SimpleAdapter(this, fillMaps,	R.layout.rowlist, from, to);
		lista.setAdapter(adapt);	
		lista.setOnItemClickListener(this);
		spinner = (Spinner) findViewById(R.id.comboAcciones);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.acciones, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	private void reconstruirMap(ArrayList<String> stringArrayListExtra) {
		if(stringArrayListExtra!=null && stringArrayListExtra.size()>0){
			
			for (Iterator<String> iterator = stringArrayListExtra.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				map = new HashMap<String, String>();
				map.put("rowtexts", string);
				file="";
				fillMaps.add(map);
			}
		}
		
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
			selected="";
		}else if("Flashear Archivo".equals(selected)){
			showFileChooser();
		}else{
			anyadir(view);
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
		
		if(file!=null && !"".equals(file)){
			listaAcciones.add(file);
			File ff=new File(file);
			selected=selected+" "+ff.getName();
		}else if(!"".equals(selected)){
			listaAcciones.add(selected);
		}
		if(!"".equals(selected)){
			map.put("rowtexts", selected);
			fillMaps.add(map);
		}
		file="";
		
		lista.setAdapter(adapt);	
		spinner.setSelection(0);
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
						anyadir(null);
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
	public void ayuda(View v){
		try
		{
			Intent intent=new Intent(this, ayudaLista.class);
			startActivity(intent);
		}
		catch (Exception e)
		{
			diag.setMessage(e.getMessage());
			diag.show();
		}
	}
	public void onListItemClick(ListView l, View v, int position, long id) {
		try{
			listaAcciones.remove(position);
			fillMaps.remove(position);
			adapt= new SimpleAdapter(this, fillMaps,	R.layout.rowlist, from, to);
			lista.setAdapter(adapt);
		}catch(Exception e){}
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        onListItemClick((ListView) arg0, arg0, arg2, arg3);
    }

}
