package com.github.hiuprocon.pve.core;

import java.io.*;
import java.net.*;

public class Peer implements Runnable {
    int port;
    PVEMsgListener ml;
    public Peer(int port,PVEMsgListener ml) {
        this.port = port;
        this.ml = ml;
    	new Thread(this).start();
    }
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                InputStreamReader isr = new InputStreamReader(socket.getInputStream(),"UTF8");
                BufferedReader br = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(),"UTF8");
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw);
                String line;                                                        
                while ((line=br.readLine())!=null) {
                    String res = ml.processMessage(line);
                    pw.println(res);
                    pw.flush();
                }
                br.close();
                pw.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
