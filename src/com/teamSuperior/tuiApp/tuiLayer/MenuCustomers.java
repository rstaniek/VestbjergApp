package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.core.controlLayer.CustomerControl;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuCustomers {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private CustomerControl customerControl = new CustomerControl();
    private String[] menuItems = {"Add a customer", "Remove a customer", "View customers", "Go back"};

    public void printCustomersMenu() {
        int choice;
        while (isRunning) {
            System.out.println("Customers Menu");
            int i = 1;
            for (String item : menuItems) {
                System.out.println(i + ". " + item);
                i++;
            }
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("add");
                    break;
                case 2:
                    System.out.println("remove");
                    break;
                case 3:
                    customerControl.viewCustomers();
                    break;
                case 4:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Error, please try again");
                    break;
            }

        }

    }
}
