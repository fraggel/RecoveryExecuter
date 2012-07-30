package com.fraggel.recoveryexecuter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.ice.tar.Tar;
import java.io.*;

public class MainActivity extends Activity
{
    private static final int FILE_SELECT_CODE=0;
	private String file="";
	private String initialDir="/mnt/sdcard/Download/";
	private SharedPreferences sp;
    AlertDialog diag;
    ArrayList lista;
	File rutaTmp=new File("/mnt/sdcard/RecoveryExecuter/");
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		diag = new AlertDialog.Builder(this).create();
		try
		{
			super.setTitle(R.string.version);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
			borrarDirectorio(rutaTmp);
		}
		catch (Exception e )
		{
			diag.setMessage(e.getMessage());
			diag.show();
		}
		//showFileChooser();
    }
    public void Ayuda(View v)
	{
    	try
		{
			Intent intent=new Intent(this, ayuda.class);
			startActivity(intent);
		}
		catch (Exception e)
		{
			diag.setMessage(e.getMessage());
			diag.show();
		}
    }
	public void creaLista(View v)
	{
		try
		{
			borrarDirectorio(rutaTmp);
			sp = getSharedPreferences("recexec", Context.MODE_WORLD_WRITEABLE);
			initialDir = sp.getString("url", "/mnt/sdcard/Download/");

			Intent intent=new Intent(this, crearLista.class);
			intent.putStringArrayListExtra("lista", lista);
			startActivityForResult(intent, FILE_SELECT_CODE);
		}
		catch (Exception e)
		{
			diag.setMessage(e.getMessage());
			diag.show();
		}

	}
	public void Flash(View v)
	{
		borrarDirectorio(rutaTmp);
		showFileChooser();
	}

	private void showFileChooser()
	{
		try
		{
			sp = getSharedPreferences("recexec", Context.MODE_WORLD_WRITEABLE);
			initialDir = sp.getString("url", "/mnt/sdcard/Download/");

        	Intent intent=new Intent(this, fileselect.class);
			startActivityForResult(intent, FILE_SELECT_CODE);
		}
		catch (Exception e)
		{
			diag.setMessage(e.getMessage());
			diag.show();
		}
	}
	private void showConfig()
	{
		try
		{
			Intent intent=new Intent(this, config.class);
			startActivity(intent);
		}
		catch (Exception e)
		{}
	}
	public boolean onCreateOptionsMenu(Menu menu)
	{
		try
		{
			MenuInflater inflater=getMenuInflater();
			inflater.inflate(R.menu.menuflasher, menu);
		}
		catch (Exception e)
		{}
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem menuitem)
	{
		boolean ret =false;
		try
		{
			switch (menuitem.getItemId())
			{
				case R.id.op1:
				borrarDirectorio(rutaTmp);
					showFileChooser();
					ret = true;
					break;
					case R.id.op2:
					creaLista(null);
					ret=true;
					break;
				case R.id.op3:
					showConfig();
					ret = true;
					break;
				case R.id.op4:
					finish();
					ret = true;
					break;
				default:
					ret = false;
					break;
			}
		}
		catch (Exception e)
		{}
		return ret;
	}
	protected void onActivityResult(int request, int result, Intent data)
	{
		try
		{
			switch (request)
			{
				case FILE_SELECT_CODE:
					if (result == RESULT_OK)
					{
						file = data.getStringExtra("file");
						lista = data.getStringArrayListExtra("lista");

						/*Uri uri=data.getData();
						 file=uri.getPath();*/
						if (file != null && !"".equals(file))
						{
							AlertDialog dialog=new AlertDialog.Builder(this).create();
							dialog.setMessage("Se va a flashear el archivo " + file);
							dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog, int witch)
									{
										//finish();
									}
								});
							dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog, int witch)
									{
										try
										{
											if (!"".equals(file))
											{
												crearZipCwm(file);
												escribirRecovery();
											}
										}
										catch (Exception e)
										{
											diag.setMessage(e.getMessage());
											diag.show();
										}
									}
								});
							dialog.show();
						}
						else if (lista != null && lista.size() > 0)
						{
							AlertDialog dialog=new AlertDialog.Builder(this).create();
							dialog.setMessage("Se va a flashear la lista de acciones");
							dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog, int witch)
									{
										//finish();
									}
								});
							dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog, int witch)
									{
										try
										{
											Runtime rt=Runtime.getRuntime();
											java.lang.Process p=rt.exec("su");
											BufferedOutputStream bos=new BufferedOutputStream(p.getOutputStream());
											bos.write(("rm /cache/recovery/extendedcommand\n").getBytes());
											for (int i = 0; i < lista.size(); i++)
											{
												String string = (String)lista.get(i);
												System.out.println(string);
												if ("Wipe Data".equals(string))
												{
													bos.write(("echo 'Wipe Data'\n").getBytes());
													bos.write(("echo 'format (\"/data\");' >> /cache/recovery/extendedcommand\n").getBytes());
												}
												else if ("Wipe Cache".equals(string))
												{
													bos.write(("echo 'Wipe Cache'\n").getBytes());
													bos.write(("echo 'format (\"/cache\");' >> /cache/recovery/extendedcommand\n").getBytes());
												}
												else if ("Wipe Dalvik".equals(string))
												{
													bos.write(("rm -r \"/data/dalvik-cache\"\n").getBytes());
												}
												else if ("Wipe Battery".equals(string))
												{
													bos.write(("rm \"/data/system/batterystats.bin\"\n").getBytes());
												}
												else if ("Selecciona una accion".equals(string))
												{

												}
												else if (!"".equals(string))
												{
													crearZipCwm(string);
													bos.write(("echo 'install_zip(\"" + file.replaceFirst("/mnt/sdcard/", "/emmc/").replaceFirst("/mnt/extSdCard/", "/sdcard/") + "\");' >> /cache/recovery/extendedcommand\n").getBytes());
												}

											}
											bos.write(("reboot recovery").getBytes());
											bos.flush();
											bos.close();
										}
										catch (Exception e)
										{
											diag.setMessage(e.getMessage());
											diag.show();
										}
									}
								});
							dialog.show();
						}
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
	public void escribirRecovery() throws Exception
	{

		Runtime rt=Runtime.getRuntime();
		java.lang.Process p=rt.exec("su");
		CheckBox chkdata=(CheckBox)findViewById(R.id.wipedata);
		CheckBox chkcache=(CheckBox)findViewById(R.id.wipecache);
		CheckBox chkdalvik=(CheckBox)findViewById(R.id.wipedalvik);
		CheckBox chkbattery=(CheckBox)findViewById(R.id.wipebattery);
		BufferedOutputStream bos=new BufferedOutputStream(p.getOutputStream());
		bos.write(("rm /cache/recovery/extendedcommand\n").getBytes());
		if (chkdata.isChecked())
		{
			bos.write(("echo 'Wipe Data'\n").getBytes());
			bos.write(("echo 'format (\"/data\");' > /cache/recovery/extendedcommand\n").getBytes());
		}
		if (chkcache.isChecked())
		{
			bos.write(("echo 'Wipe Cache'\n").getBytes());
			bos.write(("echo 'format (\"/cache\");' >> /cache/recovery/extendedcommand\n").getBytes());
		}
		if (chkdalvik.isChecked())
		{
			//bos.write(("echo 'rm -r \"/data/dalvik-cache\";\n' >> /cache/recovery/extendedcommand\n").getBytes());
			bos.write(("rm -r \"/data/dalvik-cache\"\n").getBytes());
		}
		if (chkbattery.isChecked())
		{
			//bos.write(("echo 'rm -r \"/data/dalvik-cache\";\n' >> /cache/recovery/extendedcommand\n").getBytes());
			bos.write(("rm \"/data/system/batterystats.bin\"\n").getBytes());
		}
		bos.write(("echo 'install_zip(\"" + file.replaceFirst("/mnt/sdcard/", "/emmc/").replaceFirst("/mnt/extSdCard/", "/sdcard/") + "\");' >> /cache/recovery/extendedcommand\n").getBytes());
		bos.write(("reboot recovery").getBytes());
		bos.flush();
		bos.close();
	}
	public void crearZipCwm(String f) throws Exception
	{
		File rutaTmpKernel=new File("/mnt/sdcard/RecoveryExecuter/kernel/");
		File rutaTmpModem=new File("/mnt/sdcard/RecoveryExecuter/modem/");
		rutaTmp.mkdirs();
		rutaTmpKernel.mkdirs();
		rutaTmpModem.mkdirs();

		String  ext=f.substring(f.length() - 4, f.length()).toLowerCase();
		if (".md5".equals(ext))
		{
			//copiar a rutaTmp quitando el .md5
			//asignar ext nuevo file
			//asignar f nuevo File
			try
			{
		    	File tmp=new File(f);
		    	String renamed=tmp.getName().substring(0, tmp.getName().length() - 4);
		    	copiarFichero(tmp, new File(rutaTmp.getPath() + "/" + renamed));
				f = rutaTmp.getPath() + "/" + renamed;
				ext = f.substring(f.length() - 4, f.length()).toLowerCase();
			}
			catch (Exception e)
			{
				f = "";
			}
		}
		if (".tar".equals(ext))
		{
			//unzip
			//asignar ext file extracted
			//asignar f nuevo file
			try
			{
				Tar tar=new Tar();
				File g=new File(rutaTmp + "/" + new File(f).getName());
				if (g.exists())
				{
					g.delete();
				}
				copiarFichero(new File(f), g);	

				tar.extractFiles(g, rutaTmp);
				File[] fichers=rutaTmp.listFiles();
				for (int x=0;x < fichers.length;x++)
				{
					File r=fichers[x];
					if (r.isFile())
					{
						String ex= r.getName().substring(r.getName().length() - 4, r.getName().length()).toLowerCase();
						ext = ex;
						f = r.getPath();
						g.delete();
					}
				}
			}
			catch (Exception e)
			{
				f = "";
			}
		}
		if (".img".equals(ext))
		{

			try
			{
				if (validaCabeceraKernel(f))
				{
					copiarFicheroKernel(f, rutaTmpKernel);
					kernel k=new kernel();
					k.writeKernel(rutaTmpKernel.getPath(), getResources().openRawResource(R.raw.updatebinarykernel), getResources().openRawResource(R.raw.updaterscriptkernel));
					crearZip(rutaTmp.getPath() + "/kernel.zip", new File(rutaTmpKernel.getPath() + "/META-INF/"), "boot.img", "");
					f = rutaTmp.getPath() + "/kernel.zip";
				}
	    	}
			catch (Exception e)
			{
				f = "";
			}
		}
		if (".bin".equals(ext))
		{
		
			try
			{
				if (validacabeceraModem(f))
				{
					copiarFicheroModem(f, rutaTmpModem);
					modem m=new modem();
					m.writeModem(rutaTmpModem.getPath(), getResources().openRawResource(R.raw.updatebinarymodem), getResources().openRawResource(R.raw.updaterscriptmodem), getResources().openRawResource(R.raw.flash_imagemodem));
					crearZip(rutaTmp.getPath() + "/modem.zip", new File(rutaTmpModem.getPath() + "/META-INF/"), "modem.bin", "flash_image");
					f = rutaTmp.getPath() + "/modem.zip";
				}
			}
			catch (Exception e)
			{
				f = "";
			}
		}
        file = f;
	}

	private boolean validacabeceraModem(String f) throws Exception
	{
		boolean ret=false;
		BufferedInputStream bis =new BufferedInputStream(new FileInputStream(f));
		byte[] cabecera=new byte[6];
		bis.read(cabecera);
		bis.close();
		if ("PSIRAM".equals(new String(cabecera)))
		{
			ret = true;
		}
		return ret;
	}

	private boolean validaCabeceraKernel(String f) throws Exception
	{
		boolean ret=false;
		BufferedInputStream bis =new BufferedInputStream(new FileInputStream(f));
		byte[] cabecera=new byte[7];
		bis.read(cabecera);
		bis.close();
		if ("ANDROID".equals(new String(cabecera)))
		{
			ret = true;
		}
		return ret;
	}

	private void copiarFicheroKernel(String f, File rutaTmpKernel) throws Exception
	{
		File g=new File(rutaTmpKernel + "/boot.img");
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(g));
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(f));
		int x=-1;

		x = bis.read();
		while (x != -1)
		{
			bos.write(x);
			x = bis.read();
		}
		bos.flush();
		bos.close();

	}
	private void copiarFicheroModem(String f, File rutaTmpModem) throws Exception
	{

		File g=new File(rutaTmpModem + "/modem.bin");

		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(g));
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(f));
		int x=-1;

		x = bis.read();
		while (x != -1)
		{
			bos.write(x);
			x = bis.read();
		}
		bos.flush();
		bos.close();
	}
	private void copiarFichero(File origen, File destino) throws Exception
	{

		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(destino));
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(origen));
		int x=-1;

		x = bis.read();
		while (x != -1)
		{
			bos.write(x);
			x = bis.read();
		}
		bos.flush();
		bos.close();
	}
	public void borrarDirectorio(File directorio)
	{
		File[] ficheros = directorio.listFiles();

		for (int x=0;x < ficheros.length;x++)
		{
			if (ficheros[x].isDirectory())
			{
				borrarDirectorio(ficheros[x]);
			}
			ficheros[x].delete();
		}               
	}
	public static void crearZip(String ficheroDest, File srcDir, String objeto, String objeto2) throws Exception
	{
		BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(ficheroDest));
		List<String> fileList = listDirectory(srcDir);
		ZipOutputStream zout = new ZipOutputStream(out);
		fileList.add(objeto);
		if (!"".equals(objeto2.trim()))
		{
			fileList.add(objeto2);
		}
		zout.setLevel(9);
		zout.setComment("Zipper v1.2");

		for (String fileName : fileList)
		{
			File file = new File(srcDir.getParent(), fileName);

			// Zip always use / as separator
			String zipName = fileName;
			if (File.separatorChar != '/')
				zipName = fileName.replace(File.separatorChar, '/');
			ZipEntry ze;
			if (file.isFile())
			{
				ze = new ZipEntry(zipName);
				ze.setTime(file.lastModified());
				zout.putNextEntry(ze);
				FileInputStream fin = new FileInputStream(file);
				byte[] buffer = new byte[4096];
				for (int n; (n = fin.read(buffer)) > 0;)
					zout.write(buffer, 0, n);
				fin.close();
			}
			else
			{
				ze = new ZipEntry(zipName + '/');
				ze.setTime(file.lastModified());
				zout.putNextEntry(ze);
			}
		}
		zout.close();
	}

	public static List<String> listDirectory(File directory)
	throws IOException
	{

		Stack<String> stack = new Stack<String>();
		List<String> list = new ArrayList<String>();

		// If it's a file, just return itself
		if (directory.isFile())
		{
			if (directory.canRead())
				list.add(directory.getName());
			return list;
		}

		// Traverse the directory in width-first manner, no-recursively
		String root = directory.getParent();
		stack.push(directory.getName());
		while (!stack.empty())
		{
			String current = (String) stack.pop();
			File curDir = new File(root, current);
			String[] fileList = curDir.list();
			if (fileList != null)
			{
				for (String entry : fileList)
				{
					File f = new File(curDir, entry);
					if (f.isFile())
					{
						if (f.canRead())
						{
							list.add(current + File.separator + entry);
						}
						else
						{
							System.err.println("File " + f.getPath()
											   + " is unreadable");
							throw new IOException("Can't read file: "
												  + f.getPath());
						}
					}
					else if (f.isDirectory())
					{
						list.add(current + File.separator + entry);
						stack.push(current + File.separator + f.getName());
					}
					else
					{
						throw new IOException("Unknown entry: " + f.getPath());
					}
				}
			}
		}
		return list;
	}
	private static void unZip(String strZipFile) throws Exception
	{

		File fSourceZip = new File(strZipFile);
		String zipPath = strZipFile.substring(0, strZipFile.length() - 4);
		File temp = new File(zipPath);
		temp.mkdir();


		/*
		 * STEP 2 : Extract entries while creating required
		 * sub-directories
		 *
		 */
		ZipFile zipFile = new ZipFile(fSourceZip);
		Enumeration e = zipFile.entries();

		while (e.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry)e.nextElement();
			File destinationFilePath = new File(zipPath, entry.getName());

			//create directories if required.
			destinationFilePath.getParentFile().mkdirs();

			//if the entry is directory, leave it. Otherwise extract it.
			if (entry.isDirectory())
			{
				continue;
			}
			else
			{
				System.out.println("Extracting " + destinationFilePath);

				/*
				 * Get the InputStream for current entry
				 * of the zip file using
				 *
				 * InputStream getInputStream(Entry entry) method.
				 */
				BufferedInputStream bis = new BufferedInputStream(zipFile
																  .getInputStream(entry));

				int b;
				byte buffer[] = new byte[1024];

				/*
				 * read the current entry from the zip file, extract it
				 * and write the extracted file.
				 */
				FileOutputStream fos = new FileOutputStream(destinationFilePath);
				BufferedOutputStream bos = new BufferedOutputStream(fos,
																	1024);

				while ((b = bis.read(buffer, 0, 1024)) != -1)
				{
					bos.write(buffer, 0, b);
				}

				//flush the output stream and close it.
				bos.flush();
				bos.close();

				//close the input stream.
				bis.close();
			}
		}

	}
}
