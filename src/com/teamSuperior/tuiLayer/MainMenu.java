package com.teamSuperior.tuiLayer;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MainMenu
{
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);



    public MainMenu() {
        while (isRunning) {
            printMenu();
            chooseSubMenu();
        }
    }

    public void printMenu() {
        System.out.println("Main Menu");
        System.out.println("---------");
        System.out.println("1. Customers");
        System.out.println("2. Products");
        System.out.println("3. Offers");
        System.out.println("4. Orders");
        System.out.println("5. Contractors");
        System.out.println("6. Statistics");
        System.out.println("7. Exit");
        System.out.println("-------");
        System.out.println("Your option");
    }

    public void chooseSubMenu(){
        int choice;
        choice = sc.nextInt();
        switch (choice) {
            case 1:
                break;
            case 7:
                System.out.println("Thank you for using our software");
                isRunning = false;
                break;
            default:
                System.out.println("Error, please try again");
                break;
        }
    }

    public void printCustomersMenu()
    {

    }
}
