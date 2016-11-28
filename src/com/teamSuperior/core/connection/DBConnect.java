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
        String hostName = "";
        String userName = "";
        String passWord = "";
        Connection con = null;

        try{
            con = DriverManager.getConnection(hostName, userName, passWord);
        }catch (SQLException ex){
            //getErrorMessage
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
        return con;
    }

    /***
     * Executes specified SQL query and returns the data from the table
     * @param query
     * @return ResultSet object
     */
    public static ResultSet getFromDataBase(String query){
        Connection con = connect();
        ResultSet rs = null;
        try{
            Statement statement = con.createStatement();
            rs = statement.executeQuery(query);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }

}
