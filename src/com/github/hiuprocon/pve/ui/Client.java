package com.github.hiuprocon.pve.ui;

import java.io.*;
import java.net.*;

public class Client {
    int port;
    boolean closed = false;

    Socket socket = null;
    BufferedReader br = null;
    PrintWriter pw = null;

    public Client(int port) {
        this.port = port;
        try {
            socket = new Socket("localhost", port);
            InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF8");
            br = new BufferedReader(isr);
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
            BufferedWriter bw = new BufferedWriter(osw);
            pw = new PrintWriter(bw, true);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String post(String msg) {
        if (closed==true)
            return "SOCKET IS CLOSED!!!";
        try {
            if (msg == null)
                return null;
            if (msg.equals("CLOSE")) {
                try {
                    br.close();
                    pw.close();
                    socket.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                closed = true;
                System.out.println("CLOSE:"+port+"!");
                return "CLOSE:"+port+"!";
            }
            pw.println(msg);
            pw.flush();
            return br.readLine();
        } catch(Exception e) {
            e.printStackTrace();
            try {
                socket = new Socket("localhost", port);
                InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF8");
                br = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF8");
                BufferedWriter bw = new BufferedWriter(osw);
                pw = new PrintWriter(bw, true);
                post(msg);
            } catch(Exception ee) {
                ee.printStackTrace();
            }
        }
        return "Network ERROR!";
    }
}
