package net;

import db.NetworkDatabase;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Talal Mahmood on 06/03/2016.
 * SID 5296251
 * Coventry University
 */

public class Server implements DatabaseListener
{
    private LinkedList<ClientHandler> clientHandlerLinkedList;
    private NetworkDatabase networkDatabase;

    /**
     * constructor that configures the socket to listen too.
     * @param serverSocket socket to listen too
     */
    public Server(int serverSocket)
    {
        clientHandlerLinkedList = new LinkedList<>();
        networkDatabase = new NetworkDatabase();
        try
        {
            run(serverSocket);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * while loop to listen to connections and instantiate a new handler for each client (multi threaded)
     * @param serverSocketInt socket to listen too
     * @throws Exception
     */
    public void run(int serverSocketInt) throws Exception
    {
        ServerSocket serverSocket = new ServerSocket(serverSocketInt);
        try
        {
            while(true)
            {
                Socket socket = serverSocket.accept();
                ClientHandler temp = new ClientHandler(socket, networkDatabase, this);
                Thread clientHandlerThread = new Thread(temp);
                clientHandlerThread.setDaemon(true);
                clientHandlerThread.start();
                clientHandlerLinkedList.add(temp);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Invoked when the database is updated, loops through the client handlers and notifies of an update to the database
     * the client handler would then send an updated database to the connected clients. (auto refresh)
     */
    @Override
    public void notifyUpdate()
    {
        for(Iterator<ClientHandler> clientHandlerIterator = clientHandlerLinkedList.iterator(); clientHandlerIterator.hasNext();)
        {
            ClientHandler clientHandler = clientHandlerIterator.next();
            if(!clientHandler.getSocket().isClosed())
            {
                clientHandler.databaseUpdate();
            }
            else
            {
                clientHandlerIterator.remove();
            }
        }
    }
}