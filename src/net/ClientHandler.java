package net;

import db.NetworkDatabase;
import models.Network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Talal Mahmood on 07/03/2016.
 * SID 5296251
 * Coventry University
 */

public class ClientHandler implements Runnable
{
    private Socket socket;
    private DatabaseListener agent;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private NetworkDatabase networkDatabase;
    private boolean stayAlive = true;
    /**
     * Constructor to connect to socket, get access to database and activate agent.
     * @param socket
     * @param networkDatabase
     * @param mainAgent
     */
    public ClientHandler(Socket socket, NetworkDatabase networkDatabase, DatabaseListener mainAgent)
    {
        this.socket = socket;
        this.networkDatabase = networkDatabase;
        this.agent = mainAgent;
    }

    /**
     * run method that keeps on listening for commands from the server and handle command.
     */
    @Override
    public void run()
    {
        try
        {
            String command;
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            while(stayAlive)
            {
                command = (String) objectInputStream.readObject();
                switch (command)
                {
                    case "Add":
                        Network networkToAdd = (Network) objectInputStream.readObject();
                        networkDatabase.insertNetwork(networkToAdd);
                        agent.notifyUpdate();
                        break;
                    case "Delete":
                        Network networkToDelete = (Network) objectInputStream.readObject();
                        networkDatabase.deleteNetwork(networkToDelete.getId());
                        agent.notifyUpdate();
                        break;
                    case "Modify":
                        Network updatedNetwork = (Network) objectInputStream.readObject();
                        networkDatabase.updateNetwork(updatedNetwork);
                        agent.notifyUpdate();
                        break;
                    case "Refresh":
                        objectInputStream.readObject();
                        databaseUpdate();
                        break;
                    case "Shutdown":
                        objectInputStream.readObject();
                        socket.close();
                        agent.notifyUpdate();
                        break;
                }
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            try
            {
                if(socket != null)
                {
                    socket.close();
                }
                return;
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
    }

    /**
     * Invoked when the database is updated, sends updated database to client
     */
    public void databaseUpdate()
    {
        try
        {
            ArrayList<Network> loadedDatabase = networkDatabase.getData();
            objectOutputStream.writeObject("Refresh");
            objectOutputStream.writeInt(loadedDatabase.size());
            objectOutputStream.flush();
            for(Network network: loadedDatabase)
            {
                objectOutputStream.writeObject(network);
                objectOutputStream.flush();
            }
        }
        catch (IOException io)
        {
            try
            {
                this.socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            io.printStackTrace();
        }
    }
    public void keepAlive(boolean stayAlive)
    {
        this.stayAlive = stayAlive;
    }
    public Socket getSocket()
    {
        return this.socket;
    }
}