package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.ProductController;

/**
 * Menu for management of products.
 */
class MenuProducts extends Menu {
    private ProductController productController;

    MenuProducts() {
        productController = new ProductController();
        menuItems = new String[]{"Add a product", "Remove a product", "View products", "Go back"};
        title = "Products Menu";
    }

    @Override
    protected void switchSubMenu() {
        int id, barcode, quantity, contractorId;
        double price;
        String name, subname, category, location;
        switch (scanInt()) {
            case 1:
                System.out.println("Add a product:");

                System.out.println("Id:");
                id = scanInt();
                System.out.println("Name:");
                name = scanString();
                System.out.println("Subname:");
                subname = scanString();
                System.out.println("Barcode:");
                barcode = scanInt();
                System.out.println("Category:");
                category = scanString();
                System.out.println("Price:");
                price = scanDouble();
                System.out.println("Location:");
                location = scanString();
                System.out.println("Quantity:");
                quantity = scanInt();
                System.out.println("Contractor ID:");
                contractorId = scanInt();
                productController.addProduct(id, name, subname, barcode, category, price, location, quantity, contractorId);
                break;
            case 2:
                if (productController.listIdAndNameOfProducts() > 0) {
                    System.out.println("Select the ID of the product you want to remove:");
                    id = scanInt();
                    if (productController.foundProductById(id)) {
                        System.out.println("Are you sure you want to remove this product? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (productController.removeProductById(id))
                                System.out.println("Product successfully removed");
                    } else
                        System.out.println("There is no product corresponding to that ID");
                } else
                    System.out.println("There are no products at this moment");
                break;
            case 3:
                if (productController.listAllProducts() == 0)
                    System.out.println("There are no products at this moment");
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
