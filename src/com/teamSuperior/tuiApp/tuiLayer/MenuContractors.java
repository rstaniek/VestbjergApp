package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ContractorController;

/**
 * Menu for management of contractors.
 */
class MenuContractors extends Menu {
    private ContractorController contractorController;

    MenuContractors() {
        contractorController = new ContractorController();
        menuItems = new String[]{"Add a contractor", "Remove a contractor", "View contractors", "Go back"};
        title = "Contractors Menu";
    }

    @Override
    protected void switchSubMenu() {
        int id;
        String name, address, city, zip, phone, email;
        switch (scanInt()) {
            case 1:
                System.out.println("Add a contractor:");

                System.out.println("ID:");
                id = scanInt();
                System.out.println("Name:");
                name = scanString();
                System.out.println("Address:");
                address = scanString();
                System.out.println("City:");
                city = scanString();
                System.out.println("Zip:");
                zip = scanString();
                System.out.println("Phone:");
                phone = scanString();
                System.out.println("Email:");
                email = scanString();

                contractorController.addContractor(id, name, address, city, zip, phone, email);
                System.out.println("Contractor successfully added!");
                break;
            case 2:
                if (contractorController.listIdAndNames() != 0) {
                    System.out.println("Select the ID of the contractor you want to remove:");
                    id = scanInt();
                    if (contractorController.foundContractorById(id)) {
                        System.out.println("Are you sure you want to remove this contractor? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (contractorController.removeContractorById(id))
                                System.out.println("Contractor successfully removed");
                    } else
                        System.out.println("No contractor found by that ID");
                } else
                    System.out.println("There are no contractors at this moment");
                break;
            case 3:
                System.out.println("Existing contractors:");
                if (contractorController.viewContractors() == 0)
                    System.out.println("There are no contractors at this time");
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
