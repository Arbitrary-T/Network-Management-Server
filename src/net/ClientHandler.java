package net;

import models.Network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by T on 07/03/2016.
 */
public class ClientHandler implements Runnable
{
    private Socket socket;
    private DatabaseListener agent;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ClientHandler(Socket socket, DatabaseListener mainAgent)
    {
        this.socket = socket;
        this.agent = mainAgent;
    }

    @Override
    public void run()
    {
        try
        {
            String command;
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            while(true)
            {
                command = (String) objectInputStream.readObject();
                switch (command) {
                    case "Add":
                        Network networkToAdd = (Network) objectInputStream.readObject();
                        Server.addNetwork(networkToAdd);
                        agent.notifyUpdate();
                        break;
                    case "Delete":
                        Network networkToDelete = (Network) objectInputStream.readObject();
                        Server.deleteNetwork(networkToDelete.getId());
                        agent.notifyUpdate();
                        break;
                    /*case "Update": ///@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@edit
                        Network updatedNetwork = (Network) objectInputStream.readObject();
                        int oldNetworkID = objectInputStream.readInt();
                        Server.modifyNetwork(updatedNetwork, oldNetworkID);
                        agent.notifyUpdate();
                        break;*/
                    case "Refresh":
                        objectInputStream.readObject();
                        databaseUpdate();
                        break;
                }
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            try
            {
                socket.close();
                return;
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
    }

    public void databaseUpdate()
    {
        System.out.println("BROADCAST: DATABASE UPDATE EVENT");
        try
        {
            ArrayList<Network> loadedDatabase = Server.getNetworks();
            System.out.println("This is about to be sent: " + loadedDatabase);
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
            io.printStackTrace();
        }
    }
    public Socket getSocket()
    {
        return this.socket;
    }

}
