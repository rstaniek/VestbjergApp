package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ContractorController;
import com.teamSuperior.tuiApp.controlLayer.CustomerController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;
import com.teamSuperior.tuiApp.controlLayer.StatsController;
import com.teamSuperior.tuiApp.modelLayer.Contractor;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MainMenuManager {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private String[] menuItems = {"Products", "Offers", "Orders", "Customers", "Display contractors", "Statistics", "Exit"};

    private MenuOffers menuOffers = new MenuOffers();
    private MenuOrders menuOrders = new MenuOrders();
    private MenuProducts menuProducts = new MenuProducts();
    private MenuCustomers menuCustomers = new MenuCustomers();
    private StatsController statsController = new StatsController();
    private ContractorController contractorController = new ContractorController();

    public MainMenuManager() {
        while (isRunning) {
            printMenu();
            chooseSubMenu();
        }
    }

    public void printMenu() {
        System.out.println("Main Menu for manager");
        int i = 1;
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
                contractorController.listIdAndNames();
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
