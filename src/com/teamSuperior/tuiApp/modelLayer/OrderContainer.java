package com.teamSuperior.tuiApp.modelLayer;

import java.util.ArrayList;

/**
 * Container of orders.
 */
public class OrderContainer {
    private static OrderContainer ourInstance = new OrderContainer();

    public static OrderContainer getInstance() {
        return ourInstance;
    }

    private ArrayList<Order> orders;

    private OrderContainer() {
        orders = new ArrayList<>();
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
