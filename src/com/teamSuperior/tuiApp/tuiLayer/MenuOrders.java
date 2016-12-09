package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ContractorController;
import com.teamSuperior.tuiApp.controlLayer.OrderController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuOrders {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private ProductController productController;
    private ContractorController contractorController;
    private OrderController orderController;
    private String[] menuItems = {"Add an order", "Modify an order", "Remove an order", "View orders", "Go back"};

    public MenuOrders() {
        productController = new ProductController();
        contractorController = new ContractorController();
        orderController = new OrderController();
    }

    public void printOrdersMenu() {
        int choice, id, productId, contractorId, quantity;
        String department;
        while (isRunning) {
            System.out.println("Orders Menu");
            int i = 1;
            for (String item : menuItems) {
                System.out.println(i + ". " + item);
                i++;
            }
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Add an order:");
                    if (productController.listIdNamePriceOfProducts() > 0) {
                        System.out.println("Product ID:");
                        productId = sc.nextInt();
                        if (productController.foundProductById(productId)) {
                            if (contractorController.listIdAndNames() > 0) {
                                System.out.println("Contractor ID:");
                                contractorId = sc.nextInt();
                                if (contractorController.foundContractorById(contractorId)) {
                                    System.out.println("Quantity:");
                                    quantity = sc.nextInt();
                                    System.out.println("Department:");
                                    department = sc.next();
                                    System.out.println("Order ID:");
                                    id = sc.nextInt();
                                    //approved = 0;
                                    //delivered = 0;

                                    orderController.addOrder(id, productId, contractorId, quantity, department, 0, 0);
                                } else
                                    System.out.println("No contractor found by that id");
                            } else
                                System.out.println("No contractors available at this time");
                        } else
                            System.out.println("No product found by that ID");
                    } else
                        System.out.println("There are no products at this time");
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
                    if (orderController.viewSimpleOrders() > 0) {
                        System.out.println("Select the ID of the order you want to remove:");
                        id = sc.nextInt();
                        if (orderController.foundOrderById(id)) {
                            System.out.println("Are you sure you want to remove this order? (y/n)");
                            String confirmation = sc.next();
                            if (confirmation.equals("y") || confirmation.equals("Y"))
                                if (orderController.removeOrderById(id))
                                    System.out.println("Order successfully removed");
                        } else
                            System.out.println("No order found for that ID");
                    } else
                        System.out.println("There are no orders to remove");
                    break;
                case 4:
                    if (orderController.viewOrders() == 0)
                        System.out.println("There are no orders at the moment");
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
