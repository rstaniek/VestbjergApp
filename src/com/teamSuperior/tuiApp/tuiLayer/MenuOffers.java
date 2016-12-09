package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.controlLayer.OfferController;
import com.teamSuperior.tuiApp.controlLayer.ProductController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuOffers {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);
    private ProductController productController;
    private OfferController offerController;
    private String[] menuItems = {"Create an offer", "Modify an offer", "Remove an offer", "View offers", "Go back"};

    public MenuOffers() {
        productController = new ProductController();
        offerController = new OfferController();
    }

    public void printOffersMenu() {
        int choice, id, productId;
        String date;
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
                            Date dateobj = new Date();
                            date = df.format(dateobj);
                            System.out.println("The date assigned to this offer is: " + date);
                            offerController.createOffer(id, productId, date, price, discount);
                        } else
                            System.out.println("There are no products corresponding to that id");
                    } else
                        System.out.println("There are no products available at this time");
                    break;
                case 2:
                    System.out.println("Select the ID of the offer you want to modify:");
                    //offerControl method to list all but date
                    id = sc.nextInt();
                    //select which attributes you want to modify
                    //modify em
                    //fok bitches
                    //???
                    //profit
                    //~*may need more methods*~
                    break;
                case 3:
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
                case 4:
                    System.out.println("Existing offers:");
                    if (offerController.viewOffers() == 0)
                        System.out.println("There are no offers at this time!");
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
