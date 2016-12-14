package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of orders.
 */
public class OrderContainer {
    private static OrderContainer ourInstance = new OrderContainer();
    private ArrayList<Order> orders;

    private OrderContainer() {
        orders = new ArrayList<>();
    }

    public static OrderContainer getInstance() {
        return ourInstance;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
