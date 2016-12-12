package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.CustomerController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;
import com.teamSuperior.tuiApp.controlLayer.StatsController;

/**
 * Main menu for salesmen.
 */
class MainMenuSalesman extends Menu {

    private MenuOffers menuOffers = new MenuOffers();
    private MenuOrders menuOrders = new MenuOrders();
    private ProductController productController = new ProductController();
    private CustomerController customerController = new CustomerController();
    private StatsController statsController = new StatsController();

    MainMenuSalesman() {
        menuItems = new String[]{"Display products", "Offers", "Orders", "Register a customer", "Statistics", "Exit"};
        title = "Main Menu for salesmen";
    }

    @Override
    protected void switchSubMenu() {
        switch (scanInt()) {
            case 1:
                if (productController.listAllProducts() == 0)
                    System.out.println("There are no products at this moment");
                break;
            case 2:
                menuOffers.run();
                break;
            case 3:
                menuOrders.run();
                break;
            case 4:
                int id;
                String name, surname, address, city, zip, phone, email;
                System.out.println("ID: ");
                id = scanInt();
                System.out.println("Name: ");
                name = scanString();
                System.out.println("Surname: ");
                surname = scanString();
                System.out.println("Address: ");
                address = scanString();
                System.out.println("City: ");
                city = scanString();
                System.out.println("Zip: ");
                zip = scanString();
                System.out.println("Phone: ");
                phone = scanString();
                System.out.println("Email: ");
                email = scanString();
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
