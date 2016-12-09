package com.teamSuperior.tuiApp.tuiLayer;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MainMenu {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private String[] menuItems = {"Add testing data", "Products", "Offers", "Contractors", "Orders", "Statistics", "Exit"};

    //private MenuCustomers menuCustomers = new MenuCustomers();
    private MenuProducts menuProducts = new MenuProducts();
    private MenuOffers menuOffers = new MenuOffers();
    private MenuContractors menuContractors = new MenuContractors();
    private MenuOrders menuOrders = new MenuOrders();
    private MenuStatistics menuStatistics = new MenuStatistics();

    public MainMenu() {
        while (isRunning) {
            printMenu();
            chooseSubMenu();
        }
    }

    public void printMenu() {
        System.out.println("Main Menu");
        int i = 0;
        for (String item : menuItems) {
            System.out.println(i + ". " + item);
            i++;
        }
        System.out.println("Your option");
    }

    public void chooseSubMenu() {
        int choice;
        choice = sc.nextInt();
        switch (choice) {
            case 0:
                AddTestingData addTestingData = new AddTestingData();
                break;
            /*case 1:
                menuCustomers.printCustomersMenu();
                break;*/
            case 1:
                menuProducts.printProductsMenu();
                break;
            case 2:
                menuOffers.printOffersMenu();
                break;
            case 3:
                menuContractors.printContractorsMenu();
                break;
            case 4:
                menuOrders.printOrdersMenu();
                break;
            case 5:
                menuStatistics.printStatisticsMenu();
                break;
            case 6:
                System.out.println("Thank you for using our software");
                isRunning = false;
                break;
            default:
                System.out.println("Error, please try again");
                break;
        }
    }
}
