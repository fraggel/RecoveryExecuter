package com.fraggel.recoveryexecuter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class recovery {

	public boolean writeRecovery(String ruta, InputStream isBinary,
			InputStream isScript) {
		boolean retorno = false;
		try {
			File f = new File(ruta + "/META-INF/com/google/android/");
			f.mkdirs();
			File updatebinary = new File(f.getPath() + "/update-binary");
			File updatescript = new File(f.getPath() + "/updater-script");

			BufferedInputStream fisB = new BufferedInputStream(isBinary);
			BufferedOutputStream bosB = new BufferedOutputStream(
					new FileOutputStream(updatebinary));

			int x = -1;

			x = fisB.read();
			while (x != -1) {
				bosB.write(x);
				x = fisB.read();
			}
			bosB.flush();
			bosB.close();

			BufferedInputStream fisS = new BufferedInputStream(isScript);
			BufferedOutputStream bosS = new BufferedOutputStream(
					new FileOutputStream(updatescript));
			int y = -1;
			y = fisS.read();
			while (y != -1) {
				bosS.write(y);
				y = fisS.read();
			}
			bosS.flush();
			bosS.close();
			retorno = true;
		} catch (Exception e) {
			retorno = false;
		}
		return retorno;
	}

}
