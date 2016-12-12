package com.teamSuperior.tuiApp.tuiLayer;


import com.teamSuperior.tuiApp.controlLayer.CustomerController;

import java.util.Scanner;

/**
 * Menu for management of customers.
 */
class MenuCustomers {
    private Scanner sc = new Scanner(System.in);
    private CustomerController customerController;
    private String[] menuItems = {"Add a customer", "Remove a customer", "View customers", "Go back"};

    MenuCustomers() {
        customerController = new CustomerController();
    }

    void printCustomersMenu() {
        boolean isRunning = true;
        int choice, id;
        String name, surname, address, city, zip, phone, email;
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
                case 2:
                    if (customerController.listIdAndNames() != 0) {
                        System.out.println("Select the ID of the customer you want to remove:");
                        id = sc.nextInt();
                        if (customerController.foundCustomerById(id)) {
                            System.out.println("Are you sure you want to remove this customer? (y/n)");
                            String confirmation = sc.next();
                            if (confirmation.equals("y") || confirmation.equals("Y"))
                                if (customerController.removeCustomerById(id))
                                    System.out.println("Contractor successfully removed");
                        } else
                            System.out.println("Could not find customer by that ID");
                    } else
                        System.out.println("There are no customers at this moment");
                    break;
                case 3:
                    if (customerController.viewCustomers() == 0)
                        System.out.println("No customers registered at this time");
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
