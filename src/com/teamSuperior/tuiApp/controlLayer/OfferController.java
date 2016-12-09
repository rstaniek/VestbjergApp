package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Offer;
import com.teamSuperior.tuiApp.modelLayer.OfferContainer;
import com.teamSuperior.tuiApp.modelLayer.Product;

import java.io.*;
import java.util.Iterator;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class OfferController {

    private OfferContainer offerContainer;
    private String fileName = "data/offers.txt";

    public OfferController() {
        offerContainer = OfferContainer.getInstance();
    }

    public void importFromFile() {
//        try (
//                FileInputStream fis = new FileInputStream(fileName);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
//        ) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] args = line.split("\\|");
//                createOffer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));
//            }
//        } catch (IOException e) {
//            System.out.printf("Problems loading %s %n", fileName);
//            e.printStackTrace();
//        }
    }

    public void exportToFile() {
//        try (
//                FileOutputStream fos = new FileOutputStream(fileName);
//                PrintWriter writer = new PrintWriter(fos)
//        ) {
//            writer.print("");
//            for (Offer offer : offerContainer.getOffers()) {
//                writer.printf("%d|%d|%s|%f|%f%n", offer.getId(), offer.getProductId(), offer.getDate(), offer.getPrice(), offer.getDiscount());
//            }
//        } catch (IOException e) {
//            System.out.printf("Problem saving %s %n", fileName);
//            e.printStackTrace();
//        }
    }

    public void createOffer(int id, int productId, String date, double price, double discount) {
        offerContainer.getOffers().add(new Offer(id, productId, date, price, discount));
    }

    public int viewOffers() {
        offerContainer.getOffers().forEach(System.out::print);
        return offerContainer.getOffers().size();
    }

    public int listOfferDetails() {
        for (Offer offer : offerContainer.getOffers())
            System.out.printf("ID: %d  Product ID: %d  Discounted price: %s$%n", offer.getId(), offer.getProductId(), offer.getPrice());
        return offerContainer.getOffers().size();
    }

    public boolean foundOfferById(int id) {
        boolean found = false;
        for (Offer offer : offerContainer.getOffers())
            if (offer.getId() == id)
                found = true;
        return found;
    }

    public boolean removeOfferById(int id) {
        boolean removed = false;
        Iterator<Offer> it = offerContainer.getOffers().iterator();
        while (!removed && it.hasNext()) {
            Offer offer = it.next();
            if (offer.getId() == id) {
                removed = true;
                it.remove();
            }
        }
        return removed;
    }

}
