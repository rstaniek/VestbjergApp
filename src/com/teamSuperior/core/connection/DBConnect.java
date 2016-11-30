package com.teamSuperior.core.connection;

import com.teamSuperior.guiApp.GUI.AlertBox;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class DBConnect {

    private static final String url = "jdbc:mysql://voonyx.mrhack.cz:3306/silvan";
    private static final String username = "silvan";
    private static final String password = "relae7VaelaiQuo";

    /***
     * Connects to thee database
     * @return returns connection object
     */
    private static Connection connect(String hostname, String user, String pass){
        Connection con = null;
        try
        {
            con = DriverManager.getConnection(hostname, user, pass);
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
        return con;
    }

    /***
     * Test the connection
     * @return returns True when connected, otherwise false
     */
    public static boolean testConnection(String host, String user, String pass){
        try(Connection conn = DriverManager.getConnection(host, user, pass)){
            return true;
        }
        catch (SQLException ex){
            return false;
        }
    }

    /***
     * Executes specified SQL query and returns the data from the table
     */
    public static ResultSet getFromDataBase(String query){
        Connection con = connect(url, username, password);
        ResultSet rs = null;
        try {
            Statement statement = con.createStatement();
            rs = statement.executeQuery(query);
            con.close();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
        return rs;
    }

    public static boolean upload(String query){
        Connection con = connect(url, username, password);
        boolean isExecuted = false;
        try{
            Statement statement = con.createStatement();
            isExecuted = statement.execute(query);
            con.close();
        }catch (SQLException ex){
            AlertBox.display("Connection Error", ex.getMessage());
        }
        return isExecuted;
    }
}
