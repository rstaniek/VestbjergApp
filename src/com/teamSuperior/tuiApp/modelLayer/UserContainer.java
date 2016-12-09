package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Created by Smoothini on 08.12.2016.
 */
public class UserContainer {
    private static UserContainer ourInstance = new UserContainer();

    public static UserContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<User> users;

    private UserContainer() {
        users = new ArrayList<User>();
        users.add(new User("employee", "pass", 0));
        users.add(new User("salesman", "pass", 1));
        users.add(new User("manager", "pass", 2));
        users.add(new User("ceo", "pass", 3));
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
