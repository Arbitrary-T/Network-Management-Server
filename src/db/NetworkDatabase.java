package db;

import models.Network;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Talal Mahmood on 06/03/2016.
 * SID 5296251
 * Coventry University
 */

public class NetworkDatabase extends Database
{
    private String createTableStatement = "CREATE TABLE Networks(" +
                                          "networkID INT NOT NULL PRIMARY KEY, " +
                                          "numberOfNodes INT NOT NULL, " +
                                          "numberOfHubs INT NOT NULL," +
                                          "numberOfSwitches INT NOT NULL, " +
                                          "topologyStructure VARCHAR(256), " +
                                          "countryOfOrigin VARCHAR(256), " +
                                          "currentStatus VARCHAR(256))";
    private Connection databaseConnection;
    private PreparedStatement insertData;
    private PreparedStatement updateData;
    private PreparedStatement deleteData;
    private PreparedStatement doesExist;
    private PreparedStatement getNetworkTable;
    private ArrayList<Network> tableData;

    /**
     * Constructor that connects to the database and prepares the SQL statements.
     */
    public NetworkDatabase()
    {
        tableData = new ArrayList<>();
        databaseConnection = loadDatabase("networks", createTableStatement);
        try
        {
            if(databaseConnection != null)
            {
                insertData = databaseConnection.prepareStatement("INSERT INTO Networks VALUES (?,?,?,?,?,?,?)");
                deleteData = databaseConnection.prepareStatement("DELETE FROM Networks WHERE networkID = ?");
                updateData = databaseConnection.prepareStatement("UPDATE Networks SET networkID=?, numberOfNodes=?, " +
                        "numberOfHubs=?, numberOfSwitches=?, topologyStructure=?, countryOfOrigin=?," +
                        " currentStatus=? WHERE networkID=?");
                doesExist = databaseConnection.prepareStatement("SELECT 1 FROM Networks WHERE networkID = ?");
                getNetworkTable = databaseConnection.prepareStatement("SELECT * FROM Networks");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method that inserts a Network object into the database
     * @param network to be inserted
     * @return true if the operation was successful
     */
    public synchronized boolean insertNetwork(Network network)
    {
        if(databaseConnection != null)
        {
            try
            {
                if(!doesExist(network.getId()))
                {
                    insertData.setInt(1, network.getId());
                    insertData.setInt(2, network.getNodes());
                    insertData.setInt(3, network.getHubs());
                    insertData.setInt(4, network.getSwitches());
                    insertData.setString(5, network.getTopologyStructure());
                    insertData.setString(6, network.getCountryOfOrigin());
                    insertData.setString(7, network.getCurrentStatus());
                    insertData.executeUpdate();
                    return true;
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Method that deletes a Network from the database
     * @param networkID the ID of the network to be deleted
     * @return true if the operation was successful
     */
    public synchronized boolean deleteNetwork(int networkID)
    {
        if(databaseConnection != null)
        {
            try
            {
                if(doesExist(networkID))
                {
                    deleteData.setInt(1, networkID);
                    deleteData.executeUpdate();
                    return true;
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Method that edits a Network in the database
     * @param network network to be modified
     * @return true if the operation was successful
     */
    public synchronized boolean updateNetwork(Network network)
    {
        if(databaseConnection != null)
        {
            if(doesExist(network.getId()))
            {
                try
                {
                    updateData.setInt(1, network.getId());
                    updateData.setInt(2, network.getNodes());
                    updateData.setInt(3, network.getHubs());
                    updateData.setInt(4, network.getSwitches());
                    updateData.setString(5, network.getTopologyStructure());
                    updateData.setString(6, network.getCountryOfOrigin());
                    updateData.setString(7, network.getCurrentStatus());
                    updateData.setInt(8, network.getId());
                    updateData.executeUpdate();
                    return true;
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Method to check if a Network exists
     * @param networkID the ID of the network to check
     * @return true if the network exists
     */
    public synchronized boolean doesExist(int networkID)
    {
        if(databaseConnection != null)
        {
            try
            {
                doesExist.setInt(1, networkID);
                ResultSet resultSet = doesExist.executeQuery();
                return resultSet.next();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Method to get all records in the data
     * @return ArrayList of Networks
     */
    public synchronized ArrayList<Network> getData()
    {
        tableData.clear();
        try
        {
            ResultSet resultSet = getNetworkTable.executeQuery();
            if(resultSet != null)
            {
                while(resultSet.next())
                {
                    tableData.add(new Network(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7)));
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return tableData;
    }
}