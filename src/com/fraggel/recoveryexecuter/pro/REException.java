package com.fraggel.recoveryexecuter.pro;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import android.os.Environment;

public class REException extends Throwable {
	Exception e;
	String mensaje;
	StackTraceElement trace;
	File rutaTmp;
	BufferedOutputStream bos;

	public REException(Exception e) {
		rutaTmp = new File(Environment.getExternalStorageDirectory().getPath()+"/RecoveryExecuter/");
		rutaTmp.mkdirs();
		rutaTmp = new File(Environment.getExternalStorageDirectory().getPath()+"/RecoveryExecuter/exceptions.log");
		setE(e);
		printTrace();
	}

	public REException(String mensaje, StackTraceElement trace) {
		rutaTmp = new File(Environment.getExternalStorageDirectory().getPath()+"/RecoveryExecuter/");
		rutaTmp.mkdirs();
		rutaTmp = new File(Environment.getExternalStorageDirectory().getPath()+"/RecoveryExecuter/exceptions.log");
		setMensaje(mensaje);
		setTrace(trace);
		setE(null);
	}

	private void printTrace() {
		try {
			bos = new BufferedOutputStream(new FileOutputStream(rutaTmp, true));
			PrintStream ps = new PrintStream(bos);
			String fecha = "";
			fecha = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/"
					+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/"
					+ Calendar.getInstance().get(Calendar.YEAR) + " "
					+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":"
					+ Calendar.getInstance().get(Calendar.MINUTE) + ":"
					+ Calendar.getInstance().get(Calendar.SECOND);
			ps.append(fecha + "\n");
			e.printStackTrace(ps);
			ps.append("----------------------------\n");
			ps.flush();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeException() {

	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public StackTraceElement getTrace() {
		return trace;
	}

	public void setTrace(StackTraceElement trace) {
		this.trace = trace;
	}

}
