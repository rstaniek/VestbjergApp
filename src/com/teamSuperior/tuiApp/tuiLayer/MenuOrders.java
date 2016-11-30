package com.teamSuperior.tuiApp.tuiLayer;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuOrders {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);

    public void printOrdersMenu() {
        int choice, id, productId, contractorId, quantity, approved, delivered;
        String department;
        while (isRunning) {
            System.out.println("Orders Menu");
            System.out.println("1. Add an order");
            System.out.println("2. Modify an order");
            System.out.println("3. Remove an order");
            System.out.println("4. View orders");
            System.out.println("5. Go back");
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Add an order:");

                    //show products
                    System.out.println("Product ID:");
                    productId = sc.nextInt();
                    //show contractors
                    System.out.println("Contractor ID:");
                    contractorId = sc.nextInt();
                    System.out.println("Quantity:");
                    quantity = sc.nextInt();
                    System.out.println("Department:");
                    department = sc.next();
                    //approved = 0;
                    //delivered = 0;
                    //orderControl.addOrder(productId, contractorId, quantity, department, approved, delivered);
                    break;
                case 2:
                    System.out.println("Select the ID of the order you want to modify:");
                    //orderControl.viewOrders()
                    id = sc.nextInt();
                    //select which atributes you want to modify
                    //modify em
                    //???
                    //profit
                    //~*may need more methods*~
                    break;
                case 3:
                    System.out.println("Select the ID of the order you want to remove:");
                    //orderControl.viewOrders();
                    id = sc.nextInt();
                    //maybe a confirmation before removal?
                    //orderControl.removeById(id);
                    break;
                case 4:
                    System.out.println("Existing orders:");
                    //orderControl.viewOrders();
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
