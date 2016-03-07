package net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by T on 06/03/2016.
 */
public class Server
{
    public static void main(String args[])
    {
        Server s = new Server();
        try {
            s.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() throws Exception
    {
        ServerSocket serverSocket = new ServerSocket(13337);
        Socket socket = serverSocket.accept();
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String message = bufferedReader.readLine();
        System.out.println(message);
        if(message !=null)
        {
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("messafge rejovj");
        }
    }
}