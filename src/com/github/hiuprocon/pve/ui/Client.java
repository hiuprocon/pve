package com.github.hiuprocon.pve.ui;

import java.io.*;
import java.net.*;

public class Client implements Runnable {
    int port;
    Object waitingRoom = new Object();
    String msg;
    String ret;
    public Client(int port) {
        this.port = port;
    	new Thread(this).start();
    }
    public void run() {
        while (true) {
            Socket socket=null;
            InputStreamReader isr=null;
            BufferedReader br=null;
            OutputStreamWriter osw=null;
            BufferedWriter bw=null;
            PrintWriter pw=null;
            try {
                socket = new Socket("localhost",port);
                isr = new InputStreamReader(socket.getInputStream(),"UTF8");
                br = new BufferedReader(isr);
                osw = new OutputStreamWriter(socket.getOutputStream(),"UTF8");
                bw = new BufferedWriter(osw);
                pw = new PrintWriter(bw,true);
                while (true) {
                	synchronized (waitingRoom) {
                		waitingRoom.wait();
                		if (msg==null)
                			continue;
                		pw.println(msg);
                		pw.flush();
                		ret = br.readLine();
                		waitingRoom.notifyAll();
                	}
                }
            } catch (Exception e) {
                try {
                    br.close();
                    pw.close();
                    socket.close();
                    e.printStackTrace();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
    }
    public String post(String msg) {
    	synchronized (waitingRoom) {
    		this.msg = msg;
    		waitingRoom.notifyAll();
    		try{waitingRoom.wait();}catch(Exception e){e.printStackTrace();}
    	}
    	return ret;
    }
}
