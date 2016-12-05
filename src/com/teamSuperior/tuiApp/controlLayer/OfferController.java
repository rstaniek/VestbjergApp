package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Offer;
import com.teamSuperior.tuiApp.modelLayer.OfferContainer;

import java.util.Iterator;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class OfferController {
    private OfferContainer offerContainer;

    public OfferController() {
        offerContainer = OfferContainer.getInstance();
    }

    public void createOffer(int id, int productId, String date, double price, double discount) {
        offerContainer.getOffers().add(new Offer(id, productId, date, price, discount));
    }

    public int viewOffers() {
        for (Offer offer : offerContainer.getOffers()) {
            System.out.println("ID: " + offer.getId());
            System.out.println("Product ID: " + offer.getProductId());
            System.out.println("Date: " + offer.getDate());
            System.out.println("Price: " + offer.getPrice() + "$");
            System.out.println("Discount: " + offer.getDiscount() + "$");
            System.out.println();
        }
        return offerContainer.getOffers().size();
    }

    public int listOfferDetails() {
        for (Offer offer : offerContainer.getOffers())
            System.out.println("ID: " + offer.getId() + "  Product ID: " + offer.getProductId() + "  Discounted price: " + offer.getPrice() + "$");
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
