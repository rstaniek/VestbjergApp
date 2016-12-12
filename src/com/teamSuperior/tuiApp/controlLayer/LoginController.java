package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.User;
import com.teamSuperior.tuiApp.modelLayer.UserContainer;

/**
 * Created by Smoothini on 08.12.2016.
 */
public class LoginController {

    private UserContainer userContainer;

    public LoginController() {
        userContainer = UserContainer.getInstance();
    }

    public boolean userExists(String user) {
        boolean exists = false;
        for (User u : userContainer.getUsers())
            if (u.getUser().equals(user))
                exists = true;
        return exists;
    }

    public boolean passwordMatches(String user, String password) {
        boolean match = false;
        for (User u : userContainer.getUsers())
            if (u.getUser().equals(user) && u.getPassword().equals(password))
                match = true;
        return match;
    }

    public int getAccessLevel(String user) {
        int level = 0;
        for (User u : userContainer.getUsers())
            if (u.getUser().equals(user))
                level = u.getAccessLevel();
        return level;
    }
}
