package com.teamSuperior.core.connection;

import org.junit.Before;
import org.junit.Test;

import java.util.prefs.Preferences;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.02.28.
 */
public class DBConnectTest {
    private DBConnect connect;
    private Preferences reg;

    @Before
    public void setUp() throws Exception {
        connect = new DBConnect();
        reg = Preferences.userRoot();
    }

    @Test(timeout = 2000)
    public void getConnection() throws Exception {
        assertNotNull(connect.getConnection());
    }

    @Test
    public void testConnection() throws Exception {
        assertTrue(DBConnect.testConnection(reg.get("DATABASE_HOSTNAME", ""),
                reg.get("DATABASE_USER", ""),
                reg.get("DATABASE_PASS", "")));
        assertFalse(DBConnect.testConnection("",
                reg.get("DATABASE_USER", ""),
                reg.get("DATABASE_PASS", "")));
        assertFalse(DBConnect.testConnection(reg.get("DATABASE_HOSTNAME", ""),
                reg.get("DATABASE_USER", ""), ""));
        assertFalse(DBConnect.testConnection("", "", ""));
    }

}