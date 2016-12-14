package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Offer;
import com.teamSuperior.tuiApp.modelLayer.OfferContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Offers controller.
 */
public class OfferController {

    private OfferContainer offerContainer;

    public OfferController() {
        offerContainer = OfferContainer.getInstance();
        load();
    }

    public void create(int id, int productId, String date, double price, double discount) {
        offerContainer.getOffers().add(new Offer(id, productId, date, price, discount));
    }

    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream("data/offers.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(offerContainer.getOffers());
        } catch (IOException e) {
            System.out.println("Problem saving offers.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        ArrayList<Offer> offers = null;
        try {
            FileInputStream fis = new FileInputStream("data/offers.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            offers = (ArrayList<Offer>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ignored) {

        } catch (ClassNotFoundException c) {
            System.out.println("Error loading offers.");
            c.printStackTrace();
        }
        if (offers != null) {
            offerContainer.setOffers(offers);
        }
    }

    public int listAll() {
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
