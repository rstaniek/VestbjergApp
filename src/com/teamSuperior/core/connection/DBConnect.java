package com.teamSuperior.core.connection;

import com.teamSuperior.guiApp.GUI.AlertBox;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class DBConnect implements Initializable{

    private static String url;
    private static String username;
    private static String password;
    private Preferences reg;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reg = Preferences.userRoot();
        url = reg.get("DATABASE_HOSTNAME", "");
        username = reg.get("DATABASE_USER", "");
        password = reg.get("DATABASE_PASS", "");
    }
}
