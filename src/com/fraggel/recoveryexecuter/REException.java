package com.fraggel.recoveryexecuter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;


public class REException extends Throwable
{
	Exception e;
	String mensaje;
	StackTraceElement trace;
	File rutaTmp;
	BufferedOutputStream bos;
	
public REException(Exception e){
	rutaTmp=new File("/mnt/sdcard/RecoveryExecuter/");
	rutaTmp.mkdirs();
	rutaTmp=new File("/mnt/sdcard/RecoveryExecuter/exceptions.log");
	setE(e);
	printTrace();
}
public REException(String mensaje,StackTraceElement trace){
	rutaTmp=new File("/mnt/sdcard/RecoveryExecuter/");
	rutaTmp.mkdirs();
	rutaTmp=new File("/mnt/sdcard/RecoveryExecuter/exceptions.log");
	setMensaje(mensaje);
	setTrace(trace);
	setE(null);
}
private void printTrace(){
	try {
		bos=new BufferedOutputStream(new FileOutputStream(rutaTmp, true));
		PrintStream ps=new PrintStream(bos); 
		e.printStackTrace(ps);
		ps.flush();
		ps.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public void writeException(){
	
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
