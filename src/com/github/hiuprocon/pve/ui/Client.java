package com.github.hiuprocon.pve.ui;

import java.io.*;
import java.net.*;

public class Client implements Runnable {
    int port;
    Object waitingRoom = new Object();
    String msg;
    String ret;
    boolean closed = true;

    public Client(int port) {
        this.port = port;
        new Thread(this,"PVEClientPort="+port).start();
        while(closed==true) {
            try{Thread.sleep(10);}catch(Exception e){;}
        }
    }

    public void run() {
        while (true) {
            Socket socket = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            OutputStreamWriter osw = null;
            BufferedWriter bw = null;
            PrintWriter pw = null;
            try {
                socket = new Socket("localhost", port);
                isr = new InputStreamReader(socket.getInputStream(), "UTF8");
                br = new BufferedReader(isr);
                osw = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
                bw = new BufferedWriter(osw);
                pw = new PrintWriter(bw, true);
                closed = false;
                while (true) {
                    synchronized (waitingRoom) {
                        waitingRoom.wait();
                        if (msg == null)
                            continue;
                        if (msg.equals("CLOSE")) {
                            closed = true;
                            System.out.println("CLOSE:"+port+"!");
                            ret = "CLOSE:"+port+"!";
                            waitingRoom.notifyAll();
                            return;
                        }
                        pw.println(msg);
                        pw.flush();
                        ret = br.readLine();
                        waitingRoom.notifyAll();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                    pw.close();
                    socket.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    public String post(String msg) {
        if (closed==true)
            return "SOCKET IS CLOSED!!!";
        synchronized (waitingRoom) {
            this.msg = msg;
            waitingRoom.notifyAll();
            try {
                waitingRoom.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
