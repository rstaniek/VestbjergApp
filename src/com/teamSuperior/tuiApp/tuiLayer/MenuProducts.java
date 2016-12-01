package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ProductController;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuProducts {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private ProductController productController;

    public MenuProducts(){
        productController = new ProductController();
    }

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

                    System.out.println("Id:");
                    id = sc.nextInt();
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
                    productController.addProduct(id, name, subname, barcode, category, price, location, quantity, contractorId);
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
                    if(productController.listIdAndNameOfProducts() > 0) {
                        System.out.println("Select the ID of the product you want to remove:");
                        id = sc.nextInt();
                        if (productController.foundProductById(id)) {
                            System.out.println("Are you sure you want to remove this product? (y/n)");
                            String confirmation = sc.next();
                            if (confirmation.equals("y") || confirmation.equals("Y"))
                                if (productController.removeProductById(id))
                                    System.out.println("Product succesfuly removed");
                        } else
                            System.out.println("There is no product coresponding to that ID");
                    }
                    else
                        System.out.println("There are no products at this moment");
                    break;
                case 4:
                    if(productController.listAllProducts() == 0)
                        System.out.println("There are no products at this moment");
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
