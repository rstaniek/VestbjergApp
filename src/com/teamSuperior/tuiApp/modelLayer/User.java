package com.teamSuperior.tuiApp.modelLayer;

/**
 * Created by Smoothini on 08.12.2016.
 */
public class User {
    private String user, password;
    private int accessLevel;

    public User(String user, String password, int accessLevel) {
        this.user = user;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
