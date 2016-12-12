package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.OfferController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Menu for management of offers.
 */
class MenuOffers {
    private Scanner sc = new Scanner(System.in);
    private ProductController productController;
    private OfferController offerController;
    private String[] menuItems = {"Create an offer", "Remove an offer", "View offers", "Go back"};

    MenuOffers() {
        productController = new ProductController();
        offerController = new OfferController();
    }

    void printOffersMenu() {
        boolean isRunning = true;
        int choice, id, productId;
        String dateFormatted;
        double price, discount;

        while (isRunning) {
            System.out.println("Offers Menu");
            int i = 1;
            for (String item : menuItems) {
                System.out.println(i + ". " + item);
                i++;
            }
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Add an offer:");
                    if (productController.listIdNamePriceOfProducts() > 0) {
                        System.out.println("Product ID:");
                        productId = sc.nextInt();

                        if (productController.foundProductById(productId)) {
                            System.out.println("Offer ID: ");
                            id = sc.nextInt();
                            System.out.println("Discounted price:");
                            price = sc.nextDouble();
                            discount = productController.calculateDiscount(productId, price);
                            System.out.println("Discount offered: " + discount + "$");
                            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            Date date = new Date();
                            dateFormatted = df.format(date);
                            System.out.println("The date assigned to this offer is: " + dateFormatted);
                            offerController.createOffer(id, productId, dateFormatted, price, discount);
                        } else
                            System.out.println("There are no products corresponding to that id");
                    } else
                        System.out.println("There are no products available at this time");
                    break;
                case 2:
                    if (offerController.listOfferDetails() != 0) {
                        System.out.println("Select the ID of the offer you want to remove:");
                        id = sc.nextInt();
                        if (offerController.foundOfferById(id)) {
                            System.out.println("Are you sure you want to remove this offer? (y/n)");
                            String confirmation = sc.next();
                            if (confirmation.equals("y") || confirmation.equals("Y"))
                                if (offerController.removeOfferById(id))
                                    System.out.println("Offer successfully removed");
                        } else
                            System.out.println("There is no offer by that ID");
                    } else
                        System.out.println("There are no offers at the moment");
                    break;
                case 3:
                    System.out.println("Existing offers:");
                    if (offerController.viewOffers() == 0)
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
}
