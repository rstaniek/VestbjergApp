package com.teamSuperior.tuiApp.tuiLayer;

import java.util.Scanner;

/**
 * Created by Smoothini on 28.11.2016.
 */
public class MenuOffers {
    private boolean isRunning = true;
    private Scanner sc = new Scanner(System.in);

    public void printOffersMenu() {
        int choice, id, productId;
        String date;
        double price, discount;

        while (isRunning) {
            System.out.println("Offers Menu");
            System.out.println("1. Add an offer");
            System.out.println("2. Modify an offer");
            System.out.println("3. Remove an offer");
            System.out.println("4. View offers");
            System.out.println("5. Go back");
            System.out.println("Your option:");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Add an offer:");
                    //show products by id name and price

                    System.out.println("Date:");
                    date = sc.next();
                    System.out.println("Product ID:");
                    productId = sc.nextInt();
                    System.out.println("Discounted price:");
                    price = sc.nextDouble();
                    //calculate the dicount
                    //dicount = old price - price
                    //offersControl.addOffer(date, productId, price, discount)
                    break;
                case 2:
                    System.out.println("Select the ID of the offer you want to modify:");
                    //offerControl method to list all but date
                    id = sc.nextInt();
                    //select which atributes you want to modify
                    //modify em
                    //???
                    //profit
                    //~*may need more methods*~
                    break;
                case 3:
                    System.out.println("Select the ID of the offer you want to remove:");
                    //offerControl method to list all but date
                    id = sc.nextInt();
                    //maybe a confirmation before removal?
                    //offerControl.removeById(id);
                    break;
                case 4:
                    System.out.println("Existing offers:");
                    //offerControl.viewOffers();
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
