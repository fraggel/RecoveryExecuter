package com.fraggel.recexec;

import java.io.*;

public class modem
{

public boolean writeModem(String ruta,InputStream isBinary,InputStream isScript,InputStream isFlash){
		boolean retorno=false;
		try{
			File r=new File(ruta);
			File f=new File(ruta+"/META-INF/com/google/android/");
			f.mkdirs();
			File updatebinary=new File(f.getPath()+"/update-binary");
			File updatescript=new File(f.getPath()+"/updater-script");
			File flashimage=new File(r.getPath()+"/flash_image");
	
			BufferedInputStream fisB=new BufferedInputStream(isBinary);
			BufferedOutputStream bosB =new BufferedOutputStream(new FileOutputStream(updatebinary));
			
			int x=-1;
			
			x=fisB.read();
			while(x!=-1){
				bosB.write(x);
				x=fisB.read();
			}
			bosB.flush();
			bosB.close();
			
			int y=-1;
			BufferedInputStream fisS=new BufferedInputStream(isScript);
			BufferedOutputStream bosS =new BufferedOutputStream(new FileOutputStream(updatescript));
			y=fisS.read();
			while(y!=-1){
				bosS.write(y);
				y=fisS.read();
			}
			bosS.flush();
			bosS.close();
			
			int z=-1;
			BufferedInputStream fisF=new BufferedInputStream(isFlash);
			BufferedOutputStream bosF =new BufferedOutputStream(new FileOutputStream(flashimage));
			z=fisF.read();
			while(z!=-1){
				bosF.write(z);
				z=fisF.read();
			}
			bosF.flush();
			bosF.close();
			retorno=true;
		}catch(Exception e){
			retorno=false;
		}
		return retorno;
	
}
}
