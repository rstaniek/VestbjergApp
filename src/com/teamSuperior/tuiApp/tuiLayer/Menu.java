package com.teamSuperior.tuiApp.tuiLayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Menu class.
 */
public class Menu {
    protected boolean isRunning;
    private Scanner sc = new Scanner(System.in);
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
                System.out.println("You must enter a valid number.");
                i = -1;
                sc.next();
            }
        } while (i < 0);
        return i;
    }

    String scanString() {
        String s = null;
        do {
            try {
                s = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (s != null && s.isEmpty());
        return s;
    }

    double scanDouble() {
        double d;
        do {
            try {
                d = sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("You must enter a valid number.");
                d = -1;
                sc.next();
            }
        } while (d < 0);
        return sc.nextDouble();
    }

    protected void switchSubMenu() {
    }
}
