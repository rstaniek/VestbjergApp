package com.teamSuperior.tuiApp.tuiLayer;

import java.util.InputMismatchException;
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
        int counter = 1;
        for (String item : menuItems) {
            System.out.printf("%d.)  %s %n", counter, item);
            counter++;
        }
        System.out.print("Your choice:  ");
    }

    int scanInt() {
        int i;
        do {
            try {
                i = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You must enter an integer.");
                i = -1;
                sc.next();
            }
        } while (i < 0);
        return i;
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
