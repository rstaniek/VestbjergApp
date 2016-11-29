package com.teamSuperior.tuiApp.tuiLayer;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuOffers {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);

    public void printOffersMenu() {
        int choice;
        while (isRunning) {
            System.out.println("Offers Menu");
            System.out.println("1. Add an offer");
            System.out.println("2. Modify an offer");
            System.out.println("3. Remove an offer");
            System.out.println("4. Offers statistics");
            System.out.println("5. Go back");
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("add");
                    break;
                case 2:
                    System.out.println("modify");
                    break;
                case 3:
                    System.out.println("remove");
                    break;
                case 4:
                    System.out.println("stats");
                    break;
                case 5:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Error, please try again");
                    break;
            }

        }

    }
}
