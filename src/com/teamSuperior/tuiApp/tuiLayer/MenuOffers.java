package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.OfferController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Menu for management of offers.
 */
class MenuOffers extends Menu {
    private ProductController productController;
    private OfferController offerController;

    MenuOffers() {
        productController = new ProductController();
        offerController = new OfferController();
        menuItems = new String[]{"Create an offer", "Remove an offer", "View offers", "Go back"};
        title = "Offers Menu";
    }

    @Override
    protected void switchSubMenu() {
        int id, productId;
        String dateFormatted;
        double price, discount;

        switch (scanInt()) {
            case 1:
                System.out.println("Add an offer:");
                if (productController.listIdNamePriceOfProducts() > 0) {
                    System.out.println("Product ID:");
                    productId = scanInt();

                    if (productController.foundProductById(productId)) {
                        System.out.println("Offer ID: ");
                        id = scanInt();
                        System.out.println("Discounted price:");
                        price = scanDouble();
                        discount = productController.calculateDiscount(productId, price);
                        System.out.println("Discount offered: " + discount + "$");
                        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                        Date date = new Date();
                        dateFormatted = df.format(date);
                        System.out.println("The date assigned to this offer is: " + dateFormatted);
                        offerController.create(id, productId, dateFormatted, price, discount);
                    } else
                        System.out.println("There are no products corresponding to that id");
                } else
                    System.out.println("There are no products available at this time");
                break;
            case 2:
                if (offerController.listOfferDetails() != 0) {
                    System.out.println("Select the ID of the offer you want to remove:");
                    id = scanInt();
                    if (offerController.foundOfferById(id)) {
                        System.out.println("Are you sure you want to remove this offer? (y/n)");
                        String confirmation = scanString();
                        if (confirmation.toLowerCase().equals("y"))
                            if (offerController.removeOfferById(id))
                                System.out.println("Offer successfully removed");
                    } else
                        System.out.println("There is no offer by that ID");
                } else
                    System.out.println("There are no offers at the moment");
                break;
            case 3:
                System.out.println("Existing offers:");
                if (offerController.listAll() == 0)
                    System.out.println("There are no offers at this time!");
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
