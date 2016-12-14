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
                        new MainMenuEmployee().run();
                        break;
                    case 1:
                        new MainMenuSalesman().run();
                        break;
                    case 2:
                        new MainMenuManager().run();
                        break;
                    case 3:
                        new MainMenuCeo().run();
                        break;
                }
            } else
                System.out.println("Error, password does not match");
        } else
            System.out.println("Error, that user does not exist");
    }

}
