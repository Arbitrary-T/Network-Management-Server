package db;

import pojo.Network;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by T on 06/03/2016.
 */
public class NetworkDatabase extends Database
{
    String createTableStatement = "CREATE TABLE Networks(" +
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

    public NetworkDatabase()
    {
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

    public synchronized boolean insertNetwork(Network network)
    {
        if(databaseConnection != null)
        {
            try
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
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public synchronized boolean deleteNetwork(int networkID)
    {
        if(databaseConnection != null)
        {
            try
            {
                deleteData.setInt(1, networkID);
                deleteData.executeUpdate();
                return true;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public synchronized boolean updateNetwork(Network network, int oldNetworkID)
    {
        if(databaseConnection != null)
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
                updateData.setInt(8, oldNetworkID);
                updateData.executeUpdate();
                return true;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    public synchronized boolean doesExist(int networkID)
    {
        if(databaseConnection != null)
        {
            try
            {
                doesExist.setInt(1, networkID);
                ResultSet resultSet = doesExist.executeQuery();
                if(resultSet.getFetchSize() > 0)
                {
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
