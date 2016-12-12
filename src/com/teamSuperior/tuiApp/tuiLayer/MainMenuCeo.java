package com.teamSuperior.tuiApp.tuiLayer;


import com.teamSuperior.tuiApp.controlLayer.StatsController;

import java.util.Scanner;

/**
 * Main menu for CEO.
 */
class MainMenuCeo {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private String[] menuItems = {"Products", "Offers", "Orders", "Customers", "Contractors", "Statistics", "Exit"};

    private MenuOffers menuOffers = new MenuOffers();
    private MenuOrders menuOrders = new MenuOrders();
    private MenuProducts menuProducts = new MenuProducts();
    private MenuCustomers menuCustomers = new MenuCustomers();
    private MenuContractors menuContractors = new MenuContractors();
    private StatsController statsController = new StatsController();

    void run() {
        while (isRunning) {
            printMenu();
            chooseSubMenu();
        }
    }

    private void printMenu() {
        System.out.println("Main Menu for big papa C.E.O.$$");
        int i = 1;
        for (String item : menuItems) {
            System.out.println(i + ". " + item);
            i++;
        }
        System.out.println("Your option");
    }

    private void chooseSubMenu() {
        int choice;
        choice = sc.nextInt();
        switch (choice) {
            case 1:
                menuProducts.printProductsMenu();
                break;
            case 2:
                menuOffers.printOffersMenu();
                break;
            case 3:
                menuOrders.printOrdersMenu();
                break;
            case 4:
                menuCustomers.printCustomersMenu();
                break;
            case 5:
                menuContractors.printContractorsMenu();
                break;
            case 6:
                statsController.generateStats();
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
}
