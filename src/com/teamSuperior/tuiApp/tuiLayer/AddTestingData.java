package com.teamSuperior.tuiApp.tuiLayer;

import com.teamSuperior.tuiApp.modelLayer.*;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class AddTestingData {
    public AddTestingData() {
        OfferContainer.getInstance().getOffers().add(new Offer(1, 2, "12/12/2066", 24, 4));

        ContractorContainer.getInstance().getContractors().add(new Contractor(1, "johny cocksucher", "qwety", "fuckvile", "12345", "0987654321", "cock@sucker.com"));

        OrderContainer.getInstance().getOrders().add(new Order(1, 2, 1, 10, "food", 0, 0));
        OrderContainer.getInstance().getOrders().add(new Order(2, 1, 1, 120, "cunt", 1, 0));
        OrderContainer.getInstance().getOrders().add(new Order(3, 3, 1, 1, "cock", 1, 1));
        OrderContainer.getInstance().getOrders().add(new Order(4, 3, 1, 10, "cock", 1, 0));
    }
}
