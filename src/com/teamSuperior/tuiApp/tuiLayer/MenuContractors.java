package com.teamSuperior.tuiApp.tuiLayer;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuContractors {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);

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

                    //contractorControl.addContractor(name, address, city, zip, phone, email);
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
                    System.out.println("Select the ID of the contractor you want to modify:");
                    //contractorControl method to list only id and names
                    id = sc.nextInt();
                    //maybe a confirmation before removal?
                    //contracterControl.removeById(id);
                    break;
                case 4:
                    System.out.println("Existing contractors:");
                    //contracterControl.viewContractors();
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
