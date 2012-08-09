package com.fraggel.recoveryexecuter.pro;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class crearLista extends Activity implements OnItemSelectedListener,
		AdapterView.OnItemClickListener,DialogInterface.OnClickListener {

	AlertDialog diag;
	String selected;
	ArrayList<String> listaAcciones = new ArrayList<String>();
	ArrayList<String> listaBackups = new ArrayList<String>();
	ListView lista;
	String file;
	List<HashMap<String, String>> fillMaps;
	HashMap<String, String> map;
	SimpleAdapter adapt;
	Spinner spinner;
	String[] from = new String[] { "rowtexts" };
	String items[];
	String values[];
	String[] g;
	String nomBck;
	int[] to = new int[] { R.id.rowtexts };
	Resources res;
	String[] types;

	public void onCreate(Bundle savedInstanceState) {
		try {
			res = getResources();
			super.onCreate(savedInstanceState);
			setContentView(R.layout.lista);
			setTitle(R.string.version);
			diag = new AlertDialog.Builder(this).create();
			lista = (ListView) findViewById(R.id.listaAcciones);

			fillMaps = new ArrayList<HashMap<String, String>>();

			items = res.getStringArray(R.array.arrayAcciones);
			values = res.getStringArray(R.array.arrayAccionesValues);

			Intent intent = this.getIntent();
			ArrayList<String> stringArrayListExtra = intent
					.getStringArrayListExtra("lista");
			if (stringArrayListExtra != null && stringArrayListExtra.size() > 0) {
				listaAcciones = stringArrayListExtra;
			}
			reconstruirMap(listaAcciones);
			adapt = new SimpleAdapter(this, fillMaps, R.layout.rowlist, from,
					to);
			lista.setAdapter(adapt);
			lista.setOnItemClickListener(this);
			spinner = (Spinner) findViewById(R.id.comboAcciones);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.arrayAcciones,
							android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(this);
		} catch (Exception e) {
			new REException(e);

		}
	}

	private void reconstruirMap(ArrayList<String> stringArrayListExtra)
			throws Exception {

		if (stringArrayListExtra != null && stringArrayListExtra.size() > 0) {

			for (Iterator<String> iterator = stringArrayListExtra.iterator(); iterator
					.hasNext();) {
				String string = (String) iterator.next();
				for (int x = 0; x < items.length; x++) {
					if (string.equals(values[x])) {
						string = items[x];
					} else if (string.split("/").length - 1 > 0) {
						string = items[items.length - 3]
								+ " "
								+ string.split("/")[string.split("/").length - 1];
					}else if(string.split("-").length-1>0){
						String aux1=string.split("-")[0];
						String aux2=string.split("-")[1];
						string=items[Integer.parseInt(aux1)]+" "+aux2;
					}
				}
				map = new HashMap<String, String>();
				map.put("rowtexts", string);
				file = "";
				fillMaps.add(map);
			}
		}

	}

	public void guardarLista(View v) throws Exception {
		Intent it = new Intent();
		it.putStringArrayListExtra("lista", listaAcciones);
		setResult(Activity.RESULT_OK, it);
		finish();
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// An item was selected. You can retrieve the selected item using
		externalClass exCl=new externalClass();
		selected = values[pos];
		selected=exCl.optionSelect(selected);
		if ("0".equals(selected)) {
			selected = "";
			nomBck="";
			spinner.setSelection(0);
		} else if ("5".equals(selected)) {
			nomBck="";
			try {
				showFileChooser();
			} catch (Exception e) {
				new REException(e);

			}
			selected = parent.getItemAtPosition(pos).toString();
			spinner.setSelection(0);
		} else if("6".equals(selected)){
			try {
				selected = parent.getItemAtPosition(pos).toString();
				showBackupChooser();
			} catch (Exception e) {
				new REException(e);

			}
		} else if("7".equals(selected)){
			try {
				selected = parent.getItemAtPosition(pos).toString();
				showRestoreChooser();
			} catch (Exception e) {
				new REException(e);

			}
		}else{
			nomBck="";
			file="";
			selected = parent.getItemAtPosition(pos).toString();
			try {
				anyadir(view);
			} catch (Exception e) {
				new REException(e);

			}
			spinner.setSelection(0);
		}
		
	}

	private void showFileChooser() throws Exception {
		file="";
		Intent intent = new Intent(this, fileselect.class);
		startActivityForResult(intent, 0);
	}
	private void showBackupChooser() throws Exception {
		file="";
		Intent intent =new Intent(this,selectNameBck.class);
		startActivityForResult(intent, 1);
	}
	private void showRestoreChooser() throws Exception {
		file="";
		AlertDialog.Builder b = new Builder(this);
		File fff=new File(Environment.getExternalStorageDirectory().getPath()+"/clockworkmod/backup/");
	    b.setTitle(res.getString(R.string.rdbrestore));
	    types =fff.list(); 
	    g=new String[types.length+listaBackups.size()];
	    int y=0;
	    for(int x=0;x<types.length+listaBackups.size();x++){
	    	if(x<types.length){
	    		g[x]=types[x];
	    	}else if(x>=types.length){
	    		g[x]=listaBackups.get(y)+"(TMP)";
	    		y++;
	    	}
	    }
	    b.setItems(g,this);
	    b.show();
	}
	
	
	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	public void anyadir(View v) throws Exception {
		externalClass exCl=new externalClass();
		exCl.anyadir(map, listaAcciones, diag, res, file, selected, items, values, fillMaps, lista, adapt,nomBck,this);
	}

	protected void onActivityResult(int request, int result, Intent data) {
		try {
			switch (request) {
			case 0:
				if (result == RESULT_OK) {
					file = data.getStringExtra("file");
					anyadir(null);
				}
				break;
			case 1:
				if (result == RESULT_OK) {
					
					selected = spinner.getSelectedItem().toString();
					nomBck=data.getStringExtra("nomBck");
					listaBackups.add(nomBck);
					anyadir(null);
					spinner.setSelection(0);
				}
			}
		} catch (Exception e) {
			new REException(e);

		}
		super.onActivityResult(request, result, data);
	}

	public void ayuda(View v) {
		try {
			Intent intent = new Intent(this, ayudaLista.class);
			startActivity(intent);
		} catch (Exception e) {
			new REException(e);

		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		try {
			listaAcciones.remove(position);
			fillMaps.remove(position);
			adapt = new SimpleAdapter(this, fillMaps, R.layout.rowlist, from,
					to);
			lista.setAdapter(adapt);
		} catch (Exception e) {
			new REException(e);

		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		onListItemClick((ListView) arg0, arg0, arg2, arg3);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menulist, menu);
		} catch (Exception e) {
			new REException(e);

		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean ret = false;
		try {
			switch (menuitem.getItemId()) {
			case R.id.opfile1:
				if (listaAcciones != null && listaAcciones.size() > 0) {
					AlertDialog dialog = new AlertDialog.Builder(this).create();
					dialog.setMessage(res.getString(
							R.string.salirlista));
					dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
							res.getString(R.string.no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int witch) {
									try {
										listaAcciones = null;
										guardarLista(null);
									} catch (Exception e) {
										new REException(e);

									}
								}
							});
					dialog.setButton(AlertDialog.BUTTON_POSITIVE,
							res.getString(R.string.si),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int witch) {
									try {
										guardarLista(null);
									} catch (Exception e) {
										new REException(e);

									}
								}
							});
					dialog.show();
				} else {
					finish();
				}
				ret = true;
				break;
			default:
				ret = false;
				break;
			}
		} catch (Exception e) {
			new REException(e);

		}
		return ret;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		try {
			if(which==-1){
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.fraggel.recoveryexecuter.pro"));
				startActivity(intent);
				finish();
			}else{
				selected = spinner.getSelectedItem().toString();
				nomBck=g[which];
				anyadir(null);
				spinner.setSelection(0);
			}
		} catch (Exception e) {
			new REException(e);

		}
	}

}
