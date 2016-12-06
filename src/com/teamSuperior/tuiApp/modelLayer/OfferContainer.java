package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class OfferContainer {
    private static OfferContainer ourInstance = new OfferContainer();

    public static OfferContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<Offer> offers;

    private OfferContainer() {
        offers = new ArrayList<Offer>();
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }
}
