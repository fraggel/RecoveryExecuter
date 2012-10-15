package com.fraggel.recoveryexecuter.pro;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;

import com.ice.tar.Tar;

public class MainActivity extends Activity implements OnItemSelectedListener,
AdapterView.OnItemClickListener,DialogInterface.OnClickListener {
	private static final int FILE_SELECT_CODE = 0;
	private String file = "";
	private String initialDir;
	private SharedPreferences sp;
	AlertDialog diag;
	ArrayList lista;
	File rutaTmp=null;
	File root=null;
	File sdCard=null;
	File extSdCard=null;
	String items[];
	String values[];
	Resources res;
	/*TEST LITE*/
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		diag = new AlertDialog.Builder(this).create();
		
		try {
			res = this.getResources();
			DisplayMetrics dm = res.getDisplayMetrics();
			android.content.res.Configuration conf = res.getConfiguration();
			//conf.locale=new Locale("en");
			res.updateConfiguration(conf, dm);
			root=Environment.getRootDirectory();
			sdCard=Environment.getExternalStorageDirectory();
			initialDir=sdCard+"/Download/";
			StorageOptions.determineStorageOptions();
			ArrayList<String> mounts = StorageOptions.getMounts();
			if(mounts!=null && mounts.size()>0){
				extSdCard=new File(mounts.get(0));	
			}else{
				extSdCard=null;
			}
			
			rutaTmp = new File(sdCard.getPath()+"/RecoveryExecuter/");
			if(!sdCard.canRead()){
				diag.setMessage(res.getString(R.string.msgNoSdcard));
				diag.setButton(AlertDialog.BUTTON_POSITIVE,
						res.getString(R.string.aceptar),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int witch) {
								finish();
							}
						});
				diag.show();
			}else{
				rutaTmp.mkdirs();
			}
			String tmpSD="";
			String tmpEXT="";
			
			if(sdCard!=null){
				tmpSD=sdCard.getPath();
			}
			if(extSdCard!=null){
				tmpEXT=extSdCard.getPath();
			}
			StorageOptions.determineStorageOptionsMount(tmpSD,tmpEXT);
			if (controlRoot()) {
				if (!controlBusybox()) {
					instalarBusyBox();
				}
			}
			super.setTitle(R.string.version);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);

			items = res.getStringArray(R.array.arrayAcciones);
			values = res.getStringArray(R.array.arrayAccionesValues);
			borrarDirectorio(rutaTmp);
		} catch (Exception e) {
			new REException(e);
		}
		// showFileChooser();
	}

	private boolean controlRoot() {
		boolean rootB = false;
		File f = new File(root.getPath()+"/bin/su");
		if (!f.exists()) {
			f = new File(root.getPath()+"/xbin/su");
			if (!f.exists()) {
				f = new File(root.getPath()+"/system/bin/su");
				if (!f.exists()) {
					f = new File(root.getPath()+"/system/xbin/su");
					if (f.exists()) {
						rootB=true;
					}
				}else{
					rootB=true;
				}
			}else{
				rootB=true;
			}
		}else{
			rootB=true;
		}
		if (rootB) {
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec("su");
			} catch (Exception e) {
				new REException(e);
			}
		}
		if(!rootB){
			diag.setMessage(res.getString(R.string.msgNoRoot));
			diag.show();
			
		}

		return rootB;
	}

	public void installApk(View v) {
		try {
			Intent intent = new Intent(this, install.class);
			startActivity(intent);
		} catch (Exception e) {
			new REException(e);
		}
	}

	public void nandroid(View v) {
		try {
			externalClass exCl=new externalClass();
			Intent intent=exCl.initialBackup( this,null,null);
			if(intent!=null){
				startActivity(intent);
			}
		} catch (Exception e) {
			new REException(e);
		}
	}

	private void instalarBusyBox() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setMessage(res.getString(R.string.msgNoBusybox));
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
				res.getString(R.string.cancelar),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int witch) {
						finish();
					}
				});
		dialog.setButton(AlertDialog.BUTTON_POSITIVE,
				res.getString(R.string.aceptar),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int witch) {
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri
									.parse("market://details?id=com.jrummy.busybox.installer"));
							startActivity(intent);
							finish();
						} catch (Exception e) {
							new REException(e);

						}
					}
				});
		dialog.show();

	}

	private boolean controlBusybox() {
		boolean busybox = true;
		File f = new File(root.getPath()+"/bin/busybox");
		if (!f.exists()) {
			f = new File(root.getPath()+"/xbin/busybox");
			if (!f.exists()) {
				busybox = false;
			} else {
				busybox = true;
			}
		} else {
			busybox = true;
		}
		return busybox;
	}

	public void Ayuda(View v) {
		try {
			Intent intent = new Intent(this, ayuda.class);
			startActivity(intent);
		} catch (Exception e) {
			new REException(e);

		}
	}

	public void creaLista(View v) {
		try {
			borrarDirectorio(rutaTmp);
			sp = getSharedPreferences("recexec", Context.MODE_WORLD_WRITEABLE);
			initialDir = sp.getString("url", sdCard.getPath()+"/Download/");

			Intent intent = new Intent(this, crearLista.class);
			intent.putStringArrayListExtra("lista", lista);
			startActivityForResult(intent, FILE_SELECT_CODE);
		} catch (Exception e) {
			new REException(e);
		}

	}

	public void Flash(View v) {
		borrarDirectorio(rutaTmp);
		showFileChooser();
	}
	public void limpiarChecks(){
		CheckBox chkdata = (CheckBox) findViewById(R.id.wipedata);
		CheckBox chkcache = (CheckBox) findViewById(R.id.wipecache);
		CheckBox chkdalvik = (CheckBox) findViewById(R.id.wipedalvik);
		CheckBox chkbattery = (CheckBox) findViewById(R.id.wipebattery);
		chkdata.setChecked(false);
		chkcache.setChecked(false);
		chkdalvik.setChecked(false);
		chkbattery.setChecked(false);
	}
	public void executeWipe(View v) {
		try {
			diag.setMessage(res.getString(R.string.msgSoloWipe)
					+ " " + file);
			diag.setButton(AlertDialog.BUTTON_NEGATIVE,
					res.getString(R.string.cancelar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int witch) {
							limpiarChecks();
						}
					});
			diag.setButton(AlertDialog.BUTTON_POSITIVE,
					res.getString(R.string.aceptar),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int witch) {
							try {
								file="";
								escribirRecovery();
							} catch (Exception e) {
								new REException(e);

							}
						}
					});
			diag.show();
			
		} catch (Exception e) {
			new REException(e);
		}
	}
	

	private void showFileChooser() {
		try {
			sp = getSharedPreferences("recexec", Context.MODE_WORLD_WRITEABLE);
			initialDir = sp.getString("url", sdCard.getPath()+"/Download/");

			Intent intent = new Intent(this, fileselect.class);
			startActivityForResult(intent, FILE_SELECT_CODE);
		} catch (Exception e) {
			new REException(e);

		}
	}

	private void showAbout() {
		try {
			Intent intent = new Intent(this, acercade.class);
			startActivity(intent);
		} catch (Exception e) {
			new REException(e);

		}
	}

	private void showConfig() {
		try {
			Intent intent = new Intent(this, config.class);
			startActivity(intent);
		} catch (Exception e) {
			new REException(e);

		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menuflasher, menu);
		} catch (Exception e) {
			new REException(e);

		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean ret = false;
		try {
			switch (menuitem.getItemId()) {
			case R.id.op1:
				borrarDirectorio(rutaTmp);
				showFileChooser();
				ret = true;
				break;
			case R.id.op2:
				creaLista(null);
				ret = true;
				break;
			case R.id.op3:
				installApk(null);
				ret = true;
				break;
			case R.id.op4:
				nandroid(null);
				ret = true;
				break;	
			case R.id.op5:
				showConfig();
				ret = true;
				break;
			case R.id.op6:
				showAbout();
				ret = true;
				break;
			case R.id.op7:
				finish();
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

	protected void onActivityResult(int request, int result, Intent data) {
		try {
			switch (request) {
			case FILE_SELECT_CODE:
				if (result == RESULT_OK) {
					file = data.getStringExtra("file");
					lista = data.getStringArrayListExtra("lista");

					/*
					 * Uri uri=data.getData(); file=uri.getPath();
					 */
					if (file != null && !"".equals(file)) {
						AlertDialog dialog = new AlertDialog.Builder(this)
								.create();
						dialog.setMessage(res.getString(R.string.aflashear)
								+ " " + file);
						dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
								res.getString(R.string.cancelar),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int witch) {
										limpiarChecks();
									}
								});
						dialog.setButton(AlertDialog.BUTTON_POSITIVE,
								res.getString(R.string.aceptar),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int witch) {
										try {
											if (!"".equals(file)) {
												boolean erroneo = false;
												erroneo = crearZipCwm(file);
												if (!erroneo) {
													escribirRecovery();
												} else {
													diag.setMessage(res
															.getString(R.string.msgFileErroneo));
													diag.show();
												}
											}
										} catch (Exception e) {
											new REException(e);

										}
									}
								});
						dialog.show();
					} else if (lista != null && lista.size() > 0) {
						AlertDialog dialog = new AlertDialog.Builder(this)
								.create();
						dialog.setMessage(res
								.getString(R.string.accionesflashear));
						dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
								res.getString(R.string.cancelar),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int witch) {
										// finish();
									}
								});
						dialog.setButton(AlertDialog.BUTTON_POSITIVE,
								res.getString(R.string.aceptar),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int witch) {
										try {
											Runtime rt = Runtime.getRuntime();
											java.lang.Process p = rt.exec("su");
											BufferedOutputStream bos = new BufferedOutputStream(
													p.getOutputStream());
											bos.write(("rm /cache/recovery/extendedcommand\n")
													.getBytes());
											boolean algoSelect = false;
											boolean algoSelectRebootNormal = false;
											boolean erroneo = false;
											String nomBck="";
											prepPartitions(bos);
											for (int i = 0; i < lista.size(); i++) {
												String string = (String) lista
														.get(i);
												boolean temporal=false;
												if(string.length()>1 && (string.indexOf("/")==-1)){
													String aux1=string.split("-")[0];
													String aux2=string.split("-")[1];
													string=aux1;
													nomBck=aux2.replaceAll("TMP","").replace("(", "").replace(")","");
													if(aux2.indexOf("(TMP)")!=-1){
														temporal=true;
													}
												}else{
													nomBck="";
												}
												if ("1".equals(string)) {
													bos.write(("echo 'Wipe Data'\n")
															.getBytes());
													bos.write(("echo 'format (\"/data\");' >> /cache/recovery/extendedcommand\n")
															.getBytes());
													algoSelect = true;
												} else if ("2".equals(string)) {
													bos.write(("echo 'Wipe Cache'\n")
															.getBytes());
													bos.write(("echo 'format (\"/cache\");' >> /cache/recovery/extendedcommand\n")
															.getBytes());
													algoSelect = true;
												} else if ("3".equals(string)) {
													bos.write(("rm -r \"/data/dalvik-cache\"\n")
															.getBytes());
													algoSelectRebootNormal = true;
												} else if ("4".equals(string)) {
													bos.write(("rm \"/data/system/batterystats.bin\"\n")
															.getBytes());
												}else if ("6".equals(string)) {
													externalClass exCl=new externalClass();
													bos.write(exCl.backupMain(res, diag, this,nomBck).getBytes());
												}else if ("7".equals(string)) {
													externalClass exCl=new externalClass();
													bos.write(exCl.restoreMain(res, diag, this,nomBck,temporal).getBytes());
												} else if ("0".equals(string)) {

												} else if (!"".equals(string)) {
													erroneo = crearZipCwm(string);
													if (erroneo) {
														diag.setMessage(res
																.getString(R.string.msgFileErroneo));
														diag.show();
														break;
													}
													if (!"".equals(file)) {
														String rutaCWM="";
														rutaCWM=buscarCWMySustituirRutas(file);
														bos.write(("echo 'install_zip(\""+ rutaCWM+"\");' >> /cache/recovery/extendedcommand\n")
																.getBytes());
														algoSelect = true;
													}
												}

											}
											if (algoSelect) {
												bos.write(("reboot recovery")
														.getBytes());
											} else if (algoSelectRebootNormal) {
												bos.write(("reboot").getBytes());
											}
											bos.flush();
											bos.close();
										} catch (Exception e) {
											new REException(e);

										}
									}

								});
						dialog.show();
					}
				}
				break;
			}
		} catch (Exception e) {
			new REException(e);

		}
		super.onActivityResult(request, result, data);
	}
	public String buscarCWMySustituirRutas(String fichero){
		String rutCWM="";
		rutCWM=file.replaceFirst(sdCard.getPath()+"/","/data/media/").replaceFirst(extSdCard.getPath(),"/data/media/external");
		return rutCWM;
	}
	public void escribirRecovery() throws Exception {

		Runtime rt = Runtime.getRuntime();
		java.lang.Process p = rt.exec("su");
		CheckBox chkdata = (CheckBox) findViewById(R.id.wipedata);
		CheckBox chkcache = (CheckBox) findViewById(R.id.wipecache);
		CheckBox chkdalvik = (CheckBox) findViewById(R.id.wipedalvik);
		CheckBox chkbattery = (CheckBox) findViewById(R.id.wipebattery);
		BufferedOutputStream bos = new BufferedOutputStream(p.getOutputStream());
		//bos.write(("rm /cache/recovery/extendedcommand\n").getBytes());
		boolean algoSelect = false;
		boolean algoSelectRebootNormal = false;
		prepPartitions(bos);
		if (chkdata.isChecked()) {
			bos.write(("echo 'Wipe Data'\n").getBytes());
			bos.write(("echo 'format (\"/data\");' > /cache/recovery/extendedcommand\n")
					.getBytes());
			algoSelect = true;
		}
		if (chkcache.isChecked()) {
			bos.write(("echo 'Wipe Cache'\n").getBytes());
			bos.write(("echo 'format (\"/cache\");' >> /cache/recovery/extendedcommand\n")
					.getBytes());
			algoSelect = true;
		}
		if (chkdalvik.isChecked()) {
			bos.write(("rm -r \"/data/dalvik-cache\"\n").getBytes());
			algoSelectRebootNormal = true;
		}
		if (chkbattery.isChecked()) {
			bos.write(("rm \"/data/system/batterystats.bin\"\n").getBytes());
		}
		if (!"".equals(file)) {
			String rutaCWM="";
			rutaCWM=buscarCWMySustituirRutas(file);
			
			bos.write(("echo 'install_zip(\""+ rutaCWM+"\");' >> /cache/recovery/extendedcommand\n")
					.getBytes());
			algoSelect = true;
		}
		if (algoSelect) {
			bos.write(("reboot recovery").getBytes());
		} else if (algoSelectRebootNormal) {
			bos.write(("reboot").getBytes());
		}
		
		bos.flush();
		bos.close();
		
	}
	private void prepPartitions(
			BufferedOutputStream bos) {
			ArrayList<String> listaVold=new ArrayList<String>();
			ArrayList<String> mMounts=new ArrayList<String>();
			listaVold=StorageOptions.listaVoldF;
			mMounts=StorageOptions.mMountsF;
			
			try {
				bos.write(("echo 'run_program(\"/sbin/busybox\",\"mount\",\"-a\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/mkdir\",\"/data/media/external/\");' >> /cache/recovery/extendedcommand\n").getBytes());
				bos.write(("echo 'run_program(\"/sbin/busybox\",\"mount\",\"-t\",\"auto\",\""+listaVold.get(1)+"\",\"/data/media/external/\");' >> /cache/recovery/extendedcommand\n").getBytes());
				
			} catch (Exception e) {
				
			}
			
		
	}
	public boolean crearZipCwm(String f) throws Exception {
		File rutaTmpKernel = new File(sdCard.getPath()+"/RecoveryExecuter/kernel/");
		File rutaTmpModem = new File(sdCard.getPath()+"/RecoveryExecuter/modem/");
		rutaTmp.mkdirs();
		rutaTmpKernel.mkdirs();
		rutaTmpModem.mkdirs();
		boolean erroneo = false;
		String ext = f.substring(f.length() - 4, f.length()).toLowerCase();
		if (".md5".equals(ext)) {
			try {
				File tmp = new File(f);
				String renamed = tmp.getName().substring(0,
						tmp.getName().length() - 4);
				copiarFichero(tmp, new File(rutaTmp.getPath() + "/" + renamed));
				f = rutaTmp.getPath() + "/" + renamed;
				ext = f.substring(f.length() - 4, f.length()).toLowerCase();
			} catch (Exception e) {
				new REException(e);
				f = "";

			}
		}
		if (".tar".equals(ext)) {
			try {
				Tar tar = new Tar();
				File g = new File(rutaTmp + "/" + new File(f).getName());
				if (g.exists()) {
					g.delete();
				}
				copiarFichero(new File(f), g);

				tar.extractFiles(g, rutaTmp);
				File[] fichers = rutaTmp.listFiles();
				for (int x = 0; x < fichers.length; x++) {
					File r = fichers[x];
					if (r.isFile()) {
						String ex = r
								.getName()
								.substring(r.getName().length() - 4,
										r.getName().length()).toLowerCase();
						ext = ex;
						f = r.getPath();
						g.delete();
					}
				}
			} catch (Exception e) {
				new REException(e);
				f = "";

			}
		}
		if (".img".equals(ext)) {

			try {
				if (validaCabeceraKernel(f)) {
					copiarFicheroKernel(f, rutaTmpKernel);
					kernel k = new kernel();
					k.writeKernel(
							rutaTmpKernel.getPath(),
							getResources().openRawResource(
									R.raw.updatebinarykernel),
							getResources().openRawResource(
									R.raw.updaterscriptkernel));
					crearZip(rutaTmp.getPath() + "/kernel.zip", new File(
							rutaTmpKernel.getPath() + "/META-INF/"),
							"boot.img", "");
					f = rutaTmp.getPath() + "/kernel.zip";
				}
			} catch (Exception e) {
				new REException(e);
				f = "";

			}
		}
		if (".bin".equals(ext)) {

			try {
				if (validacabeceraModem(f)) {
					copiarFicheroModem(f, rutaTmpModem);
					modem m = new modem();
					m.writeModem(
							rutaTmpModem.getPath(),
							getResources().openRawResource(
									R.raw.updatebinarymodem),
							getResources().openRawResource(
									R.raw.updaterscriptmodem), getResources()
									.openRawResource(R.raw.flash_imagemodem));
					crearZip(rutaTmp.getPath() + "/modem.zip", new File(
							rutaTmpModem.getPath() + "/META-INF/"),
							"modem.bin", "flash_image");
					f = rutaTmp.getPath() + "/modem.zip";
				}
			} catch (Exception e) {
				new REException(e);
				f = "";

			}
		}
		/*if (".zip".equals(ext)) {
			if (!validaCabeceraZip(f)) {
				f = "";
			}
		}*/
		if ("".equals(f)) {
			erroneo = true;
		}
		file = f;
		return erroneo;
	}

	private boolean validaCabeceraZip(String f) throws Exception {
		boolean ret = false;
		File fSourceZip = new File(f);
		ZipFile zipFile = new ZipFile(fSourceZip);
		Enumeration e = zipFile.entries();

		while (e.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) e.nextElement();

			if (entry.isDirectory()) {
				if ((entry.getName().endsWith("META-INF/"))) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	private boolean validacabeceraModem(String f) throws Exception {
		boolean ret = false;
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(f));
		byte[] cabecera = new byte[6];
		bis.read(cabecera);
		bis.close();
		if ("PSIRAM".equals(new String(cabecera))) {
			ret = true;
		}
		return ret;
	}

	private boolean validaCabeceraKernel(String f) throws Exception {
		boolean ret = false;
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(f));
		byte[] cabecera = new byte[7];
		bis.read(cabecera);
		bis.close();
		if ("ANDROID".equals(new String(cabecera))) {
			ret = true;
		}
		return ret;
	}

	private void copiarFicheroKernel(String f, File rutaTmpKernel)
			throws Exception {
		File g = new File(rutaTmpKernel + "/boot.img");
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(g));
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(f));
		int x = -1;

		x = bis.read();
		while (x != -1) {
			bos.write(x);
			x = bis.read();
		}
		bos.flush();
		bos.close();

	}

	private void copiarFicheroModem(String f, File rutaTmpModem)
			throws Exception {

		File g = new File(rutaTmpModem + "/modem.bin");

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(g));
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(f));
		int x = -1;

		x = bis.read();
		while (x != -1) {
			bos.write(x);
			x = bis.read();
		}
		bos.flush();
		bos.close();
	}

	private void copiarFichero(File origen, File destino) throws Exception {

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(destino));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				origen));
		int x = -1;

		x = bis.read();
		while (x != -1) {
			bos.write(x);
			x = bis.read();
		}
		bos.flush();
		bos.close();
	}

	public void borrarDirectorio(File directorio) {
		File[] ficheros = directorio.listFiles();

		for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].isDirectory()) {
				borrarDirectorio(ficheros[x]);
			}
			if (!"exceptions.log".equals(ficheros[x].getName())
					|| ("exceptions.log".equals(ficheros[x].getName()) && ficheros[x]
							.length() > (512 * 1024))) {
				ficheros[x].delete();
			}
		}
	}

	public static void crearZip(String ficheroDest, File srcDir, String objeto,
			String objeto2) throws Exception {
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(ficheroDest));
		List<String> fileList = listDirectory(srcDir);
		ZipOutputStream zout = new ZipOutputStream(out);
		fileList.add(objeto);
		if (!"".equals(objeto2.trim())) {
			fileList.add(objeto2);
		}
		zout.setLevel(9);
		zout.setComment("Zipper v1.2");

		for (String fileName : fileList) {
			File file = new File(srcDir.getParent(), fileName);

			String zipName = fileName;
			if (File.separatorChar != '/')
				zipName = fileName.replace(File.separatorChar, '/');
			ZipEntry ze;
			if (file.isFile()) {
				ze = new ZipEntry(zipName);
				ze.setTime(file.lastModified());
				zout.putNextEntry(ze);
				FileInputStream fin = new FileInputStream(file);
				byte[] buffer = new byte[4096];
				for (int n; (n = fin.read(buffer)) > 0;)
					zout.write(buffer, 0, n);
				fin.close();
			} else {
				ze = new ZipEntry(zipName + '/');
				ze.setTime(file.lastModified());
				zout.putNextEntry(ze);
			}
		}
		zout.close();
	}

	public static List<String> listDirectory(File directory) throws IOException {

		Stack<String> stack = new Stack<String>();
		List<String> list = new ArrayList<String>();

		// If it's a file, just return itself
		if (directory.isFile()) {
			if (directory.canRead())
				list.add(directory.getName());
			return list;
		}

		// Traverse the directory in width-first manner, no-recursively
		String root = directory.getParent();
		stack.push(directory.getName());
		while (!stack.empty()) {
			String current = (String) stack.pop();
			File curDir = new File(root, current);
			String[] fileList = curDir.list();
			if (fileList != null) {
				for (String entry : fileList) {
					File f = new File(curDir, entry);
					if (f.isFile()) {
						if (f.canRead()) {
							list.add(current + File.separator + entry);
						} else {
							System.err.println("File " + f.getPath()
									+ " is unreadable");
							throw new IOException("Can't read file: "
									+ f.getPath());
						}
					} else if (f.isDirectory()) {
						list.add(current + File.separator + entry);
						stack.push(current + File.separator + f.getName());
					} else {
						throw new IOException("Unknown entry: " + f.getPath());
					}
				}
			}
		}
		return list;
	}

	private static void unZip(String strZipFile) throws Exception {

		File fSourceZip = new File(strZipFile);
		String zipPath = strZipFile.substring(0, strZipFile.length() - 4);
		File temp = new File(zipPath);
		temp.mkdir();

		/*
		 * STEP 2 : Extract entries while creating required sub-directories
		 */
		ZipFile zipFile = new ZipFile(fSourceZip);
		Enumeration e = zipFile.entries();

		while (e.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			File destinationFilePath = new File(zipPath, entry.getName());

			// create directories if required.
			destinationFilePath.getParentFile().mkdirs();

			// if the entry is directory, leave it. Otherwise extract it.
			if (entry.isDirectory()) {
				continue;
			} else {
				System.out.println("Extracting " + destinationFilePath);

				/*
				 * Get the InputStream for current entry of the zip file using
				 * 
				 * InputStream getInputStream(Entry entry) method.
				 */
				BufferedInputStream bis = new BufferedInputStream(
						zipFile.getInputStream(entry));

				int b;
				byte buffer[] = new byte[1024];

				/*
				 * read the current entry from the zip file, extract it and
				 * write the extracted file.
				 */
				FileOutputStream fos = new FileOutputStream(destinationFilePath);
				BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

				while ((b = bis.read(buffer, 0, 1024)) != -1) {
					bos.write(buffer, 0, b);
				}

				// flush the output stream and close it.
				bos.flush();
				bos.close();

				// close the input stream.
				bis.close();
			}
		}

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		try {
			if(which==-1){
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri
						.parse("market://details?id=com.fraggel.recoveryexecuter.pro"));
				startActivity(intent);
			}
		} catch (Exception e) {
			new REException(e);

		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
