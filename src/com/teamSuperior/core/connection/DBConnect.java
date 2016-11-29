package com.teamSuperior.core.connection;

import com.teamSuperior.guiApp.GUI.AlertBox;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class DBConnect {

    /***
     * Connects to thee database
     * @return returns connection object
     */
    public static Connection connect(){
        String url = "jdbc:mysql://voonyx.mrhack.cz:3306/silvan";
        String username = "silvan";
        String password = "relae7VaelaiQuo";
        Connection con = null;
        try
        {
            con = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException ex){
            //getErrorMessage
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
        return con;
    }

    /***
     * Executes specified SQL query and returns the data from the table
     */
    public static ResultSet getFromDataBase(String query){
        Connection con = connect();
        ResultSet rs = null;
        try {
            Statement statement = con.createStatement();
            rs = statement.executeQuery(query);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }
}
