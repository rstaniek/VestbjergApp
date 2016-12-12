package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.CustomerController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;
import com.teamSuperior.tuiApp.controlLayer.StatsController;

import java.util.Scanner;

/**
 * Main menu for salesmen.
 */
class MainMenuSalesman {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private String[] menuItems = {"Display products", "Offers", "Orders", "Register a customer", "Statistics", "Exit"};

    private MenuOffers menuOffers = new MenuOffers();
    private MenuOrders menuOrders = new MenuOrders();
    private ProductController productController = new ProductController();
    private CustomerController customerController = new CustomerController();
    private StatsController statsController = new StatsController();

    void run() {
        while (isRunning) {
            printMenu();
            chooseSubMenu();
        }
    }

    private void printMenu() {
        System.out.println("Main Menu for salesmen");
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
                if (productController.listAllProducts() == 0)
                    System.out.println("There are no products at this moment");
                break;
            case 2:
                menuOffers.printOffersMenu();
                break;
            case 3:
                menuOrders.printOrdersMenu();
                break;
            case 4:
                int id;
                String name, surname, address, city, zip, phone, email;
                System.out.println("ID: ");
                id = sc.nextInt();
                System.out.println("Name: ");
                name = sc.next();
                System.out.println("Surname: ");
                surname = sc.next();
                System.out.println("Address: ");
                address = sc.next();
                System.out.println("City: ");
                city = sc.next();
                System.out.println("Zip: ");
                zip = sc.next();
                System.out.println("Phone: ");
                phone = sc.next();
                System.out.println("Email: ");
                email = sc.next();
                customerController.addCustomer(id, name, surname, address, city, zip, phone, email);
                System.out.println("Customer successfully registered!");
                break;
            case 5:
                statsController.generateStats();
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
