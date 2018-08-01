package com.example.richado.uschat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    public SocketClient(String addr, int port) throws Exception{
        try {
            this.socket = new Socket(addr, port);
            OutputStream os= this.socket.getOutputStream();
            this.out = new PrintWriter(os);

            InputStream is= this.socket.getInputStream();
            this.in = new BufferedReader(new InputStreamReader(is));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean SendMessage(String msg) throws Exception {
        try {
            this.out.write(msg);
            this.out.flush();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String ReceiveMessage() throws Exception {
        StringBuilder r = new StringBuilder();
        String line = null;
        char[] buf = new char[1024];
        try {
            int size = this.in.read(buf);
            return new String(buf, 0, size);
            //while ((line = this.in.readLine()) != null) {
            //    System.out.println(line);
            //    r.append(line);
            //}
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        //return r.toString();
    }


}
