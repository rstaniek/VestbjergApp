package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ContractorController;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuContractors {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private ContractorController contractorController;

    public MenuContractors() { contractorController = new ContractorController(); }

    public void printContractorsMenu() {
        int choice, id;
        String name, address, city, zip, phone, email;
        while (isRunning) {
            System.out.println("Contractors Menu");
            System.out.println("1. Add a contractor");
            System.out.println("2. Modify a contractor");
            System.out.println("3. Remove a contractor");
            System.out.println("4. View contractors");
            System.out.println("5. Go back");
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Add a contractor:");

                    System.out.println("ID:");
                    id = sc.nextInt();
                    System.out.println("Name:");
                    name = sc.next();
                    System.out.println("Address:");
                    address = sc.next();
                    System.out.println("City:");
                    city = sc.next();
                    System.out.println("Zip:");
                    zip = sc.next();
                    System.out.println("Phone:");
                    phone = sc.next();
                    System.out.println("Email:");
                    email = sc.next();

                    contractorController.addContractor(id, name, address, city, zip, phone, email);
                    break;
                case 2:
                    System.out.println("Select the ID of the contractor you want to modify:");
                    //contractorControl method to list only id and names
                    id = sc.nextInt();
                    //select which atributes you want to modify
                    //modify em
                    //???
                    //profit
                    //~*may need more methods*~
                    break;
                case 3:
                    if(contractorController.listIdAndNames() != 0){
                        System.out.println("Select the ID of the contractor you want to remove:");
                        id = sc.nextInt();
                        if(contractorController.foundContractorById(id)){
                            System.out.println("Are you sure you want to remove this contractor? (y/n)");
                            String confirmation = sc.next();
                            if(confirmation.equals("y") || confirmation.equals("Y"))
                                if(contractorController.removeContractorById(id))
                                    System.out.println("Contractor succesfuly removed");
                        }
                        else
                            System.out.println("No contractor found by that ID");
                    }
                    else
                        System.out.println("There are no contractors at this moment");
                    break;
                case 4:
                    System.out.println("Existing contractors:");
                    if(contractorController.viewContractors() == 0)
                        System.out.println("There are no contractors at this time");
                    break;
                case 5:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Error, please try again");
                    break;
            }

        }

    }
}
