package com.teamSuperior.core.tuiLayer;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuOrders {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);

    public void printOrdersMenu() {
        int choice;
        while (isRunning) {
            System.out.println("Orders Menu");
            System.out.println("1. Add an order");
            System.out.println("2. Modify an order");
            System.out.println("3. Remove an order");
            System.out.println("4. Orders statistics");
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
