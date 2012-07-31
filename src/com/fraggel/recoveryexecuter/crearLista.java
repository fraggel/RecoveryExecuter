package com.fraggel.recoveryexecuter;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.fraggel.recoveryexecuter.*;
import java.io.*;
import java.util.*;

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
	String items [];
	String values [];
	int[] to = new int[] { R.id.rowtexts};
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.lista);
			setTitle(R.string.version);
			diag = new AlertDialog.Builder(this).create();
			lista = (ListView) findViewById(R.id.listaAcciones);
			
			fillMaps = new ArrayList<HashMap<String, String>>();
			
			items=  getResources().getStringArray(R.array.acciones);
			values=getResources().getStringArray(R.array.accionesValues);
			
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
		} catch (Exception e) {
			try {
				throw new REException(e);
			} catch (REException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	private void reconstruirMap(ArrayList<String> stringArrayListExtra) throws Exception{
		
			
		
		if(stringArrayListExtra!=null && stringArrayListExtra.size()>0){
			
			for (Iterator<String> iterator = stringArrayListExtra.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				int r =-1;
				   for(int x=0;x<items.length;x++){
					   if(string.equals(values[x])){
						   string=items[x];
					   }else if(string.split("/").length-1>0){
						   string=items[items.length-1]+" "+string.split("/")[string.split("/").length-1];
					   }
				   }
				map = new HashMap<String, String>();
				map.put("rowtexts", string);
				file="";
				fillMaps.add(map);
			}
		}
		
	}
	public void guardarLista(View v)throws Exception{
		Intent it=new Intent();
		it.putStringArrayListExtra("lista",listaAcciones);
		setResult(Activity.RESULT_OK,it);
		finish();
	}
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// An item was selected. You can retrieve the selected item using
	
		selected=values[pos];
		if("0".equals(selected)){
			selected="";
		}else if("5".equals(selected)){
			try{
				showFileChooser();
			}catch(Exception e){
				try {
					throw new REException(e);
				} catch (REException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			selected = parent.getItemAtPosition(pos).toString();
		}else{
			selected = parent.getItemAtPosition(pos).toString();
			try{
				anyadir(view);
			}catch(Exception e){
				try {
					throw new REException(e);
				} catch (REException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		
	}
	private void showFileChooser()throws Exception
	{
        	Intent intent=new Intent(this, fileselect.class);
			startActivityForResult(intent, 0);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	public void anyadir(View v) throws Exception{
		map = new HashMap<String, String>();
		
		if(file!=null && !"".equals(file)){
			listaAcciones.add(file);
			File ff=new File(file);
			selected=selected+" "+ff.getName();
		}else if(!"".equals(selected)){
			int r=-1;
			for(int x=0;x<items.length;x++){
				if(selected.equals(items[x])){
					r=x;
				}
			}
			listaAcciones.add(values[r]);
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
			try {
				throw new REException(e);
			} catch (REException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

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
			try {
				throw new REException(e);
			} catch (REException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public void onListItemClick(ListView l, View v, int position, long id) {
		try{
			listaAcciones.remove(position);
			fillMaps.remove(position);
			adapt= new SimpleAdapter(this, fillMaps,	R.layout.rowlist, from, to);
			lista.setAdapter(adapt);
		}catch(Exception e){
			try {
				throw new REException(e);
			} catch (REException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        onListItemClick((ListView) arg0, arg0, arg2, arg3);
    }
	public boolean onCreateOptionsMenu(Menu menu){
		try{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menulist, menu);
		}catch (Exception e)
		{
			try {
				throw new REException(e);
			} catch (REException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem menuitem){
		boolean ret =false;
		try
		{
			switch(menuitem.getItemId()){
				case R.id.opfile1:
				if(listaAcciones!=null && listaAcciones.size()>0){
					AlertDialog dialog=new AlertDialog.Builder(this).create();
					dialog.setMessage(getResources().getString(R.string.salirlista));
					dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int witch)
							{
								finish();
							}
						});
					dialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.si), new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int witch)
							{
								try
								{
									guardarLista(null);
								}
								catch (Exception e)
								{
									try {
										throw new REException(e);
									} catch (REException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						});
					dialog.show();
					}else{
						finish();
					}
					ret= true;
					break;
				default:
					ret= false;
					break;
			}
		}catch (Exception e)
		{
			try {
				throw new REException(e);
			} catch (REException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return ret;
	}

}
