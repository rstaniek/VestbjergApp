package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.*;

/**
 * Created by Smoothini on 02.12.2016.
 */
public class StatsController {
    public void generateStats(){
        int approvedOrders = 0, deliveredOrders = 0;
        for(Order order : OrderContainer.getInstance().getOrders()){
            if(order.getApproved() != 0)
                approvedOrders++;
            if(order.getDelivered() != 0)
                deliveredOrders++;
        }
        System.out.println("- Number of products: " + ProductContainer.getInstance().getProducts().size());
        System.out.println("- Number of offers: " + OfferContainer.getInstance().getOffers().size());
        System.out.println("- Number of contractors: " + ContractorContainer.getInstance().getContractors().size());

        System.out.println("- Number of orders: " + OrderContainer.getInstance().getOrders().size());
        System.out.println("|-- Approved orders: " + approvedOrders);
        System.out.println("|-- Delivered orders: " + deliveredOrders);

    }
}
