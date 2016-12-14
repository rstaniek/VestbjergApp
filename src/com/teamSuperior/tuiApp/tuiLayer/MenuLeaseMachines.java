package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.LeaseMachineController;

/**
 * Created by Smoothini on 12.12.2016.
 */
class MenuLeaseMachines extends Menu {
    private LeaseMachineController leaseMachineController;

    MenuLeaseMachines(){
        leaseMachineController = new LeaseMachineController();
        menuItems = new String[]{"Create a lease machine", "Remove a lease machine", "View lease machines", "Go back"};
        title = "Lease machines Menu";
    }

    @Override
    protected void switchSubMenu() {
        int id;
        String name;
        double priceForDay;
        switch (scanInt()) {
            case 1:
                System.out.println("ID: ");
                id = scanInt();
                System.out.println("Name: ");
                name = scanString();
                System.out.println("Price for a day: ");
                priceForDay = scanDouble();
                leaseMachineController.addLeaseMachine(id,name,priceForDay);
                System.out.println("Lease machine successfully created!");
                break;
            case 2:
                if (leaseMachineController.listIdAndNames() != 0) {
                    System.out.println("Select the ID of the machine you want to remove:");
                    id = scanInt();
                    if (leaseMachineController.foundMachineById(id)) {
                        System.out.println("Are you sure you want to remove this machine? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (leaseMachineController.removeMachineById(id))
                                System.out.println("Machine successfully removed");
                    } else
                        System.out.println("Could not find machine by that ID");
                } else
                    System.out.println("There are no lease machines at this moment");
                break;
            case 3:
                if (leaseMachineController.viewLeaseMachines() == 0)
                    System.out.println("No lease machines available at this time");
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
