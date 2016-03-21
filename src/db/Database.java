package db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Talal Mahmood on 27/01/2016.
 * SID 5296251
 * Coventry University
 */

public abstract class Database
{
    private Connection databaseConnection;

    /**
     * method to be called by all databases, connects to a database, if it doesn't exist creates one.
     * @param database the name of the database to connect to or create
     * @param createTableStatement the table to create if it does not exist
     * @return
     */
    public Connection loadDatabase(String database, String createTableStatement)
    {
        File databaseFile = new File(database);
        String existsDatabaseURL = "jdbc:derby:" + database;
        String newDatabaseURL = "jdbc:derby:" + database + ";create=true;";

        if (databaseFile.exists())
        {
            try
            {
                databaseConnection = DriverManager.getConnection(existsDatabaseURL);
                System.out.println("Successfully connected to existing " + database + " database.");
                return databaseConnection;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return databaseConnection;
            }
        }
        else
        {
            try
            {
                databaseConnection = DriverManager.getConnection(newDatabaseURL);
                Statement firstRunStatement = databaseConnection.createStatement();
                firstRunStatement.executeUpdate(createTableStatement);
                firstRunStatement.close();
                System.out.println("Successfully connected to newly created " + database + " database.");
                return databaseConnection;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return databaseConnection;
            }
        }
    }
}