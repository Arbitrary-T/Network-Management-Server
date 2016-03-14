package net;

import db.NetworkDatabase;
import models.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by T on 06/03/2016.
 */
public class Server implements DatabaseListener
{
    private LinkedList<ClientHandler> clientHandlerLinkedList;
    private static NetworkDatabase networkDatabase;

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

    public void run(int serverSocketInt) throws Exception
    {
        ServerSocket serverSocket = new ServerSocket(serverSocketInt);
        try
        {
            while(true)
            {
                Socket socket = serverSocket.accept();
                ClientHandler temp = new ClientHandler(socket, this);
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

    public static synchronized void addNetwork(Network network)
    {
        networkDatabase.insertNetwork(network);
    }

    public static synchronized void modifyNetwork(Network network)
    {
        networkDatabase.updateNetwork(network);
    }

    public static synchronized void deleteNetwork(int networkID)
    {
        networkDatabase.deleteNetwork(networkID);
    }
    public static synchronized ArrayList<Network> getNetworks()
    {
        return networkDatabase.getData();
    }

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