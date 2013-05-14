package com.github.hiuprocon.pve.ui;

import java.io.*;
import java.net.*;
import com.github.hiuprocon.pve.core.PVEMsgListener;

public class Server implements Runnable {
    int port;
    PVEMsgListener ml;

    public Server(int port, PVEMsgListener ml) {
        this.port = port;
        this.ml = ml;
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            ServerSocket serverSocket = null;
            Socket socket = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            OutputStreamWriter osw = null;
            BufferedWriter bw = null;
            PrintWriter pw = null;
            try {
                serverSocket = new ServerSocket(port);
                socket = serverSocket.accept();
                isr = new InputStreamReader(socket.getInputStream(), "UTF8");
                br = new BufferedReader(isr);
                osw = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
                bw = new BufferedWriter(osw);
                pw = new PrintWriter(bw, true);
                String line;
                while ((line = br.readLine()) != null) {
                    String res = ml.processMessage(line);
                    pw.println(res);
                    pw.flush();
                }
            } catch (Exception e) {
                try {
                    br.close();
                    pw.close();
                    socket.close();
                    serverSocket.close();
                    e.printStackTrace();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
    }
}
