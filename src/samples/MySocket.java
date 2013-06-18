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
    public MySocket(int port) throws Exception {
        socket = new Socket("localhost", port);
        br = new BufferedReader(
              new InputStreamReader(socket.getInputStream(),"UTF8"));
        pw = new PrintWriter(new BufferedWriter(
              new OutputStreamWriter(socket.getOutputStream(),"UTF8")), true);
    }

    /**
     * Send a one-line message, and receive a one-line message.
     */
    public String send(String msg) throws Exception {
        pw.println(msg);
        pw.flush();
        return br.readLine();
    }

    /**
     * Close this MySocket.
     */
    public void close() throws Exception {
        br.close();
        pw.close();
        socket.close();
    }
}

