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

    public void printCustomersMenu() {
        int choice;
        while (isRunning) {
            System.out.println("Customers Menu");
            System.out.println("1. Add a customer");
            System.out.println("2. Modify a customer");
            System.out.println("3. Remove a customer");
            System.out.println("4. View customers");
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
                    customerControl.viewCustomers();
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
