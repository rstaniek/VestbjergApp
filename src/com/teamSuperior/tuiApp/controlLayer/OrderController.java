package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Order;
import com.teamSuperior.tuiApp.modelLayer.OrderContainer;

import java.util.Iterator;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class OrderController {
    private OrderContainer orderContainer;

    public OrderController() {
        orderContainer = OrderContainer.getInstance();
    }

    public void addOrder(int id, int productId, int contractorId, int quantity, String department, int approved, int delivered) {
        orderContainer.getOrders().add(new Order(id, productId, contractorId, quantity, department, approved, delivered));
    }

    public int viewSimpleOrders() {
        for (Order order : orderContainer.getOrders())
            System.out.printf("Order ID: %d  Product ID: %d  Contractor ID: %d%n", order.getId(), order.getProductId(), order.getContractorId());
        return orderContainer.getOrders().size();
    }

    public boolean foundOrderById(int id) {
        boolean found = false;
        for (Order order : orderContainer.getOrders())
            if (order.getId() == id)
                found = true;
        return found;
    }

    public boolean removeOrderById(int id) {
        boolean removed = false;
        Iterator<Order> it = orderContainer.getOrders().iterator();
        while (!removed && it.hasNext()) {
            Order order = it.next();
            if (order.getId() == id) {
                removed = true;
                it.remove();
            }
        }
        return removed;
    }

    public int viewOrders() {
        orderContainer.getOrders().forEach(System.out::print);
        return orderContainer.getOrders().size();
    }

    public int viewNotApprovedOrders() {
        int count = 0;
        for (Order order : orderContainer.getOrders())
            if (order.getApproved() == 0) {
                System.out.printf("Order ID: %d  Product ID: %d  Contractor ID: %d%n", order.getId(), order.getProductId(), order.getContractorId());
                count++;
            }
        return count;
    }

    public boolean approveOrderById(int id) {
        boolean approved = false;
        for (Order order : orderContainer.getOrders())
            if (order.getId() == id) {
                order.setApproved(1);
                approved = true;
            }
        return approved;
    }
}
