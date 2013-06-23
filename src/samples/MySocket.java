package samples;

import java.io.*;
import java.net.*;

/**
 * MySocket is a simplyfied socket for one-line text messaging.
 */
public class MySocket {
    Socket socket = null;
    BufferedReader br = null;
    PrintWriter pw = null;

    /**
     * A constructor of MySocket requires port number.
     */
    public MySocket(int port) {
        try {
            socket = new Socket("localhost", port);
            br = new BufferedReader(
                  new InputStreamReader(socket.getInputStream(),"UTF8"));
            pw = new PrintWriter(new BufferedWriter(
                  new OutputStreamWriter(socket.getOutputStream(),"UTF8")), true);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println(-1);
        }
    }

    /**
     * Send a one-line message, and receive a one-line message.
     */
    public String send(String msg) {
        try {
            pw.println(msg);
            pw.flush();
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            close();
            //System.out.println(-1);
            return null;
        }
    }

    /**
     * Close this MySocket.
     */
    public void close() {
        try {
            br.close();
            pw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

