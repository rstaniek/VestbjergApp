package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ContractorController;
import com.teamSuperior.tuiApp.controlLayer.OrderController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;

/**
 * Menu for management of orders.
 */
class MenuOrders extends Menu {
    private ProductController productController;
    private ContractorController contractorController;
    private OrderController orderController;

    MenuOrders() {
        productController = new ProductController();
        contractorController = new ContractorController();
        orderController = new OrderController();
        menuItems = new String[]{"Add an order", "Remove an order", "Approve an order", "View orders", "Go back"};
        title = "Orders Menu";
    }

    @Override
    protected void switchSubMenu() {
        int id, productId, contractorId, quantity;
        String department;
        switch (scanInt()) {
            case 1:
                System.out.println("Add an order:");
                if (productController.listIdNamePriceOfProducts() > 0) {
                    System.out.println("Product ID:");
                    productId = scanInt();
                    if (productController.foundProductById(productId)) {
                        if (contractorController.listIdAndNames() > 0) {
                            System.out.println("Contractor ID:");
                            contractorId = scanInt();
                            if (contractorController.foundContractorById(contractorId)) {
                                System.out.println("Quantity:");
                                quantity = scanInt();
                                System.out.println("Department:");
                                department = scanString();
                                System.out.println("Order ID:");
                                id = scanInt();

                                orderController.create(id, productId, contractorId, quantity, department, false, false);
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
                if (orderController.viewSimpleOrders() > 0) {
                    System.out.println("Select the ID of the order you want to remove:");
                    id = scanInt();
                    if (orderController.foundOrderById(id)) {
                        System.out.println("Are you sure you want to remove this order? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (orderController.removeOrderById(id))
                                System.out.println("Order successfully removed");
                    } else
                        System.out.println("No order found for that ID");
                } else
                    System.out.println("There are no orders to remove");
                break;
            case 3:
                if (orderController.viewNotApprovedOrders() > 0) {
                    System.out.println("Select the ID of the order you want to approve:");
                    id = scanInt();
                    if (orderController.foundOrderById(id)) {
                        System.out.println("Are you sure you want to approve this order? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (orderController.approveOrderById(id))
                                System.out.println("Order successfully approved");
                    } else
                        System.out.println("No orders found by that ID");
                } else
                    System.out.println("There are no orders waiting approval");
                break;
            case 4:
                if (orderController.listAll() == 0)
                    System.out.println("There are no orders at the moment");
                break;
            case 5:
                isRunning = false;
                orderController.save();
                break;
            default:
                System.out.println("Error, please try again");
                break;
        }

    }
}
