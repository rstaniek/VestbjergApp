package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of users.
 */
public class UserContainer {
    private static UserContainer ourInstance = new UserContainer();

    public static UserContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<User> users;

    private UserContainer() {
        users = new ArrayList<>();
        users.add(new User("employee", "pass", 0));
        users.add(new User("salesman", "pass", 1));
        users.add(new User("manager", "pass", 2));
        users.add(new User("ceo", "pass", 3));
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
