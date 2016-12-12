package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.LoginController;

import java.util.Scanner;

/**
 * Login provider.
 */
public class Login {

    private Scanner sc = new Scanner(System.in);
    private LoginController loginController = new LoginController();
    private boolean isLogged = false;
    private MainMenuEmployee mainMenuEmployee = new MainMenuEmployee();
    private MainMenuSalesman mainMenuSalesman = new MainMenuSalesman();
    private MainMenuManager mainMenuManager = new MainMenuManager();
    private MainMenuCeo mainMenuCeo = new MainMenuCeo();

    public void run() {
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
                        mainMenuEmployee.run();
                        break;
                    case 1:
                        mainMenuSalesman.run();
                        break;
                    case 2:
                        mainMenuManager.run();
                        break;
                    case 3:
                        mainMenuCeo.run();
                        break;
                }
            } else
                System.out.println("Error, password does not match");
        } else
            System.out.println("Error, that user does not exist");
    }

}
