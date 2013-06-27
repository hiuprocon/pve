package com.github.hiuprocon.pve.ui;

import java.io.*;
import java.net.*;
import com.github.hiuprocon.pve.core.PVEMsgListener;

public class Server implements Runnable {
    int port;
    PVEMsgListener ml;
    boolean dispose = false;
    ServerSocket serverSocket = null;
    Socket socket = null;
    BufferedReader br = null;
    PrintWriter pw = null;

    public Server(int port, PVEMsgListener ml) {
        this.port = port;
        this.ml = ml;
        new Thread(this,"PVEServerPort="+port).start();
    }

    public void run() {
        while (dispose==false) {
            serverSocket = null;
            socket = null;
            InputStreamReader isr = null;
            br = null;
            OutputStreamWriter osw = null;
            BufferedWriter bw = null;
            pw = null;
            try {
                serverSocket = new ServerSocket(port);
                socket = serverSocket.accept();
System.out.println("GAHA:");
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
                e.printStackTrace();
            } finally {
                try {
                    if (br!=null) br.close();
                    if (pw!=null) pw.close();
                    if (socket!=null) socket.close();
                    if (serverSocket!=null) serverSocket.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    public void dispose() {
        dispose = true;
        try {
            if (br!=null) br.close();
            if (pw!=null) pw.close();
            if (socket!=null) socket.close();
            if (serverSocket!=null) serverSocket.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}
