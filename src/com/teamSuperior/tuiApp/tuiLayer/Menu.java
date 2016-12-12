package com.teamSuperior.tuiApp.tuiLayer;

import java.util.Scanner;

/**
 * Menu class.
 */
public class Menu {
    protected boolean isRunning;
    private Scanner sc = new Scanner(System.in);
    protected String[] menuItems;
    String title;

    void run() {
        isRunning = true;
        while (isRunning) {
            printMenu();
            switchSubMenu();
        }
    }

    private void printMenu() {
        System.out.println(title);
        int i = 1;
        for (String item : menuItems) {
            System.out.println(i + ". " + item);
            i++;
        }
        System.out.println("Your option");
    }

    //TODO: validate integer
    int scanInt() {
        return sc.nextInt();
    }

    String scanString() {
        return sc.next();
    }

    double scanDouble() {
        return sc.nextDouble();
    }

    protected void switchSubMenu() {

    }
}
