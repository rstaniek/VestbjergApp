package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.CustomerController;
import com.teamSuperior.tuiApp.controlLayer.LeaseController;
import com.teamSuperior.tuiApp.controlLayer.LeaseMachineController;

/**
 * Menu for management of leases.
 */
public class MenuLeases extends Menu {
    private LeaseController leaseController;
    private LeaseMachineController leaseMachineController;
    private CustomerController customerController;

    MenuLeases() {
        leaseController = new LeaseController();
        leaseMachineController = new LeaseMachineController();
        customerController = new CustomerController();
        menuItems = new String[]{"Create a lease", "Return a lease", "View leases", "Go back"};
        title = "Leases Menu";
    }

    @Override
    protected void switchSubMenu() {
        int id, leaseMachineId, customerId;
        String borrowDate, returnDate;
        double price;
        switch (scanInt()) {
            case 1:
                if (leaseController.canLease()) {
                    System.out.println("ID:");
                    id = scanInt();
                    leaseMachineController.listIdAndNames();
                    System.out.println("Lease Machine ID:");
                    leaseMachineId = scanInt();
                    if (leaseMachineController.foundMachineById(leaseMachineId)) {
                        customerController.listIdAndNames();
                        System.out.println("Customer ID:");
                        customerId = scanInt();
                        if (customerController.foundCustomerById(customerId)) {
                            System.out.println("Borrow date: ");
                            borrowDate = scanString();
                            System.out.println("Return date: ");
                            returnDate = scanString();
                            System.out.println("Price: ");
                            price = scanDouble();
                            leaseController.create(id, leaseMachineId, customerId, borrowDate, returnDate, price);
                            leaseMachineController.markAsLeased(leaseMachineId);
                        } else
                            System.out.println("No customer found by that ID");
                    } else
                        System.out.println("No machine found by that ID");
                } else
                    System.out.println("There are no lease machines or customers at this moment");
                break;
            case 2:
                if (leaseController.listIdAndNames() != 0) {
                    System.out.println("Select the ID of the lease you want to remove:");
                    id = scanInt();
                    if (leaseController.foundLeaseById(id)) {
                        System.out.println("Are you sure you want to return this lease? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (leaseController.removeLeaseById(id)) {
                                System.out.println("Lease successfully returned");
                                leaseMachineController.markAsNotLeased(leaseController.getMachineId(id));
                            }
                    } else
                        System.out.println("Could not find machine by that ID");
                } else
                    System.out.println("There are no leases at this moment");
                break;
            case 3:
                if (leaseController.listAll() == 0)
                    System.out.println("No leases available at this time");
                break;
            case 4:
                isRunning = false;
                leaseController.save();
                break;
            default:
                System.out.println("Error, please try again");
                break;
        }

    }

}
