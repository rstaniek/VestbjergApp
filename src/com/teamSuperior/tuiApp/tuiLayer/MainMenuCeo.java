package com.teamSuperior.tuiApp.tuiLayer;


import com.teamSuperior.tuiApp.controlLayer.StatsController;

/**
 * Main menu for CEO.
 */
class MainMenuCeo extends Menu {

    private MenuOffers menuOffers = new MenuOffers();
    private MenuOrders menuOrders = new MenuOrders();
    private MenuProducts menuProducts = new MenuProducts();
    private MenuCustomers menuCustomers = new MenuCustomers();
    private MenuContractors menuContractors = new MenuContractors();
    private MenuLeaseMachines menuLeaseMachines = new MenuLeaseMachines();
    private MenuLeases menuLeases = new MenuLeases();
    private StatsController statsController = new StatsController();

    MainMenuCeo() {
        menuItems = new String[]{"Products", "Offers", "Orders", "Customers", "Contractors", "Lease Machines", "Leases", "Statistics", "Exit"};
        title = "Main Menu for big papa C.E.O.$$";
    }

    @Override
    protected void switchSubMenu() {
        switch (scanInt()) {
            case 1:
                menuProducts.run();
                break;
            case 2:
                menuOffers.run();
                break;
            case 3:
                menuOrders.run();
                break;
            case 4:
                menuCustomers.run();
                break;
            case 5:
                menuContractors.run();
                break;
            case 6:
                menuLeaseMachines.run();
                break;
            case 7:
                menuLeases.run();
                break;
            case 8:
                statsController.generateStats();
                break;
            case 9:
                System.out.println("Thank you for using our software");
                isRunning = false;
                break;
            default:
                System.out.println("Error, please try again");
                break;
        }
    }
}
