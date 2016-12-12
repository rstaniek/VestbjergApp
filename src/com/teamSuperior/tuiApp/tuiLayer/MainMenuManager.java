package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ContractorController;
import com.teamSuperior.tuiApp.controlLayer.StatsController;

/**
 * Main menu for managers.
 */
class MainMenuManager extends Menu {

    private MenuOffers menuOffers = new MenuOffers();
    private MenuOrders menuOrders = new MenuOrders();
    private MenuProducts menuProducts = new MenuProducts();
    private MenuCustomers menuCustomers = new MenuCustomers();
    private StatsController statsController = new StatsController();
    private ContractorController contractorController = new ContractorController();

    MainMenuManager() {
        menuItems = new String[]{"Products", "Offers", "Orders", "Customers", "Display contractors", "Statistics", "Exit"};
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
