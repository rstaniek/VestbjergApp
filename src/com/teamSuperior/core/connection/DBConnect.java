package com.teamSuperior.core.connection;

import com.teamSuperior.core.exception.ConnectionException;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.prefs.Preferences;

import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static javafx.scene.control.Alert.AlertType.ERROR;

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
            displayMessage(ERROR, "Connection Error", ex.getMessage());
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
    public ResultSet getFromDataBase(String query) throws SQLException {
        Connection con = connect(url, username, password);
        ResultSet rs = null;
        Statement statement = con.createStatement();
        rs = statement.executeQuery(query);
        return rs;
    }

    /***
     * Uploads data stated in the query to the database (UNSAFE)
     * @param query an SQL query string
     */
    public void upload(String query) throws SQLException {
        Connection con = connect(url, username, password);
        Statement statement = con.createStatement();
        statement.execute(query);
        con.close();
    }

    /***
     * Uploads data stated in the query to the database (SAFE)
     * @param stmt preparedStatement object with protection against SQLi
     */
    public void uploadSafe(PreparedStatement stmt) throws SQLException, ConnectionException {
        Connection con = connect(url, username, password);
        boolean isExecuted = false;
        try {
            stmt.executeUpdate();
            con.close();
            isExecuted = true;
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage(), ex.getSQLState(), ex.getCause());
        }
        if(!isExecuted) throw new ConnectionException();
    }

    /***
     *Checks the textField for illegal characters
     * @param tf a TextField object
     * @return true when the TextField object doesn't contain any illegal characters. False otherwise
     */
    public static boolean validateField(TextField tf){
        //TODO: should be implemented better but didn't have creativity to do it better
        return !(tf.getText().contains(";") ||
                tf.getText().contains("[") ||
                tf.getText().contains("]") ||
                tf.getText().contains("{") ||
                tf.getText().contains("}")) && !tf.getText().isEmpty();
    }
}