package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.LoginController;

import java.util.Scanner;

/**
 * Created by Smoothini on 08.12.2016.
 */
public class Login {

    Scanner sc = new Scanner(System.in);
    private LoginController loginController = new LoginController();
    private boolean isLogged = false;
    private MainMenu mainMenu;

    public Login() {
        while (!isLogged) {
            displayLoginScreen();
        }
    }

    public void displayLoginScreen(){
        String user, pass;
        System.out.println("Username: ");
        user = sc.next();
        if(loginController.userExists(user)){
            System.out.println("Password: ");
            pass = sc.next();
            if(loginController.passwordMatches(user, pass)){
                System.out.println("You have succesfuly logged in!");
                isLogged = true;
                mainMenu = new MainMenu();
            }
            else
                System.out.println("Error, password does not match");
        }
        else
            System.out.println("Error, that user does not exist");
    }

}
