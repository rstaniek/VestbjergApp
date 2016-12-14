package com.teamSuperior.tuiApp.tuiLayer;


import com.teamSuperior.tuiApp.controlLayer.CustomerController;

/**
 * Menu for management of customers.
 */
class MenuCustomers extends Menu {
    private CustomerController customerController;

    MenuCustomers() {
        customerController = new CustomerController();
        menuItems = new String[]{"Add a customer", "Remove a customer", "View customers", "Go back"};
        title = "Customers Menu";
    }

    @Override
    protected void switchSubMenu() {
        int id;
        String name, surname, address, city, zip, phone, email;
        switch (scanInt()) {
            case 1:
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
                customerController.create(id, name, surname, address, city, zip, phone, email);
                System.out.println("Customer successfully registered!");
                break;
            case 2:
                if (customerController.listIdAndNames() != 0) {
                    System.out.println("Select the ID of the customer you want to remove:");
                    id = scanInt();
                    if (customerController.foundCustomerById(id)) {
                        System.out.println("Are you sure you want to remove this customer? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (customerController.removeCustomerById(id))
                                System.out.println("Contractor successfully removed");
                    } else
                        System.out.println("Could not find customer by that ID");
                } else
                    System.out.println("There are no customers at this moment");
                break;
            case 3:
                if (customerController.listAll() == 0)
                    System.out.println("No customers registered at this time");
                break;
            case 4:
                isRunning = false;
                customerController.save();
                break;
            default:
                System.out.println("Error, please try again");
                break;
        }

    }
}
