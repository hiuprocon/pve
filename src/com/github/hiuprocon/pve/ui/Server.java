package com.github.hiuprocon.pve.ui;

import java.io.*;
import java.net.*;
import com.github.hiuprocon.pve.core.PVEMsgListener;

public class Server implements Runnable {
    public final int port;
    PVEMsgListener ml;
    boolean dispose = false;
    boolean pauseRequest = false;
    ServerSocket serverSocket = null;
    Socket socket = null;
    BufferedReader br = null;
    PrintWriter pw = null;

    public Server(int port, PVEMsgListener ml) {
        this.port = port;
        this.ml = ml;
        new Thread(this,"PVEServerPort="+port).start();
    }

    public void changePVEMessageListener(PVEMsgListener ml) {
        this.ml = ml;
    }
    public void pause() {
        pauseRequest = true;
    }
    public void resume() {
        pauseRequest = false;
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
                isr = new InputStreamReader(socket.getInputStream(), "UTF8");
                br = new BufferedReader(isr);
                osw = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
                bw = new BufferedWriter(osw);
                pw = new PrintWriter(bw, true);
                String line;
                while ((line = br.readLine()) != null) {
                    while (pauseRequest==true) {
                        try{Thread.sleep(100);}catch(Exception e){;}
                    }
                    String res = ml.processMessage(line);
                    pw.println(res);
                    pw.flush();
                }
            } catch (SocketException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
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
