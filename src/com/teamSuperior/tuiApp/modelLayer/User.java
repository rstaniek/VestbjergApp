package com.teamSuperior.tuiApp.modelLayer;

/**
 * User model class.
 */
public class User {
    private String user, password;
    private int accessLevel;

    User(String user, String password, int accessLevel) {
        this.user = user;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public String getUser() {
        return user;
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

}
