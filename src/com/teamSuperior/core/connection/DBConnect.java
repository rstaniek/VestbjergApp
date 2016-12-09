package com.teamSuperior.core.connection;

import com.teamSuperior.guiApp.GUI.AlertBox;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.ErrorCode;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class DBConnect {

    private static String url;
    private static String username;
    private static String password;
    private Preferences reg;

    public DBConnect() {
        reg = Preferences.userRoot();
        url = reg.get("DATABASE_HOSTNAME", "");
        username = reg.get("DATABASE_USER", "");
        password = reg.get("DATABASE_PASS", "");
    }

    /***
     * Connects to thee database
     * @return returns connection object
     */
    private static Connection connect(String hostname, String user, String pass) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(hostname, user, pass);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
        return con;
    }

    /***
     * Returns the connection object
     * @return Connection
     */
    public Connection getConnection(){
        return connect(url, username, password);
    }

    /***
     * Test the connection
     * @return returns True when connected, otherwise false
     */
    public static boolean testConnection(String host, String user, String pass) {
        try (Connection conn = DriverManager.getConnection(host, user, pass)) {
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /***
     * Executes specified SQL query and returns the data from the table
     */
    public ResultSet getFromDataBase(String query) {
        Connection con = connect(url, username, password);
        ResultSet rs = null;
        try {
            Statement statement = con.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
        return rs;
    }

    public void upload(String query) {
        Connection con = connect(url, username, password);
        boolean isExecuted = false;
        try {
            Statement statement = con.createStatement();
            isExecuted = statement.execute(query);
            con.close();
            isExecuted = true;
        } catch (SQLException ex) {
            AlertBox.display("Connection Error", ex.getMessage());
        }
        if(!isExecuted) Error.displayError(ErrorCode.DATABASE_UPLOAD_ERROR);
    }

    public void uploadSafe(PreparedStatement stmt) {
        Connection con = connect(url, username, password);
        boolean isExecuted = false;
        try {
            stmt.executeUpdate();
            con.close();
            isExecuted = true;
        } catch (SQLException ex) {
            AlertBox.display("Connection Error", ex.getMessage());
        }
        if(!isExecuted) Error.displayError(ErrorCode.DATABASE_UPLOAD_ERROR);
    }
}