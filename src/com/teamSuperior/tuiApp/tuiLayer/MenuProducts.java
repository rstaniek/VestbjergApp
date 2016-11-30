package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.core.controlLayer.ProductControl;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuProducts {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    ProductControl productControl = new ProductControl();

    public void printProductsMenu() {
        int choice, id, barcode, quantity, contractorId;
        double price;
        String name, subname, category, location;
        while (isRunning) {
            System.out.println("Products Menu");
            System.out.println("1. Add a product");
            System.out.println("2. Modify a product");
            System.out.println("3. Remove a product");
            System.out.println("4. View products");
            System.out.println("5. Go back");
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Add a product:");

                    System.out.println("Name:");
                    name = sc.next();
                    System.out.println("Subname:");
                    subname = sc.next();
                    System.out.println("Barcode:");
                    barcode = sc.nextInt();
                    System.out.println("Category:");
                    category = sc.next();
                    System.out.println("Price:");
                    price = sc.nextDouble();
                    System.out.println("Location:");
                    location = sc.next();
                    System.out.println("Quantity:");
                    quantity = sc.nextInt();
                    System.out.println("Contractor ID:");
                    contractorId = sc.nextInt();
                    //productControl.addProduct(name, subname, barcode, category, price, location, quantity, contractorId);
                    break;
                case 2:
                    System.out.println("Select the ID of the product you want to modify:");
                    //productControl method to list only id and product names
                    id = sc.nextInt();
                    //select which atributes you want to modify
                    //modify em
                    //???
                    //profit
                    //~*may need more methods*~
                    break;
                case 3:
                    System.out.println("Select the ID of the product you want to modify:");
                    //productControl method to list only id and product names
                    id = sc.nextInt();
                    //maybe a confirmation before removal?
                    //productControl.removeById(id);
                    break;
                case 4:
                    System.out.println("Existing products:");
                    //productControl.viewProducts();
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
