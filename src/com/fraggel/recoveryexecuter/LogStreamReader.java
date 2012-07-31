package com.fraggel.recoveryexecuter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LogStreamReader implements Runnable {

    private BufferedReader reader;
    String line="";
    public LogStreamReader(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getLine(){
    	return line;
    }
}
