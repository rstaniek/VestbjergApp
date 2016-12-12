package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.LoginController;

import java.util.Scanner;

/**
 * Created by Smoothini on 08.12.2016.
 */
public class Login {

    private Scanner sc = new Scanner(System.in);
    private LoginController loginController = new LoginController();
    private boolean isLogged = false;
    private MainMenuEmployee mainMenuEmployee;
    private MainMenuSalesman mainMenuSalesman;
    private MainMenuManager mainMenuManager;
    private MainMenuCeo mainMenuCeo;

    public Login() {
        while (!isLogged) {
            displayLoginScreen();
        }
    }

    private void displayLoginScreen() {
        String user, pass;
        System.out.println("Username: ");
        user = sc.next();
        if (loginController.userExists(user)) {
            System.out.println("Password: ");
            pass = sc.next();
            if (loginController.passwordMatches(user, pass)) {
                System.out.println("You have successfully logged in!");
                isLogged = true;
                switch (loginController.getAccessLevel(user)) {
                    case 0:
                        mainMenuEmployee = new MainMenuEmployee();
                        break;
                    case 1:
                        mainMenuSalesman = new MainMenuSalesman();
                        break;
                    case 2:
                        mainMenuManager = new MainMenuManager();
                        break;
                    case 3:
                        mainMenuCeo = new MainMenuCeo();
                        break;
                }
            } else
                System.out.println("Error, password does not match");
        } else
            System.out.println("Error, that user does not exist");
    }

}
