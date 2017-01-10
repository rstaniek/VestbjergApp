package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Order;
import com.teamSuperior.tuiApp.modelLayer.OrderContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Orders controller.
 */
public class OrderController {

    private OrderContainer orderContainer;

    public OrderController() {
        orderContainer = OrderContainer.getInstance();
        load();
    }

    public void create(int id, int productId, int contractorId, int quantity, String department, boolean approved, boolean delivered) {
        orderContainer.getOrders().add(new Order(id, productId, contractorId, quantity, department, approved, delivered));
    }

    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream("data/orders.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(orderContainer.getOrders());
        } catch (IOException e) {
            System.out.println("Problem saving orders.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        ArrayList<Order> orders = null;
        try {
            FileInputStream fis = new FileInputStream("data/orders.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            orders = (ArrayList<Order>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ignored) {

        } catch (ClassNotFoundException c) {
            System.out.println("Error loading orders.");
            c.printStackTrace();
        }
        if (orders != null) {
            orderContainer.setOrders(orders);
        }
    }

    public int listAll() {
        orderContainer.getOrders().forEach(System.out::print);
        return orderContainer.getOrders().size();
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

    public int viewNotApprovedOrders() {
        int count = 0;
        for (Order order : orderContainer.getOrders())
            if (!order.isApproved()) {
                System.out.printf("Order ID: %d  Product ID: %d  Contractor ID: %d%n", order.getId(), order.getProductId(), order.getContractorId());
                count++;
            }
        return count;
    }

    public boolean approveOrderById(int id) {
        boolean approved = false;
        for (Order order : orderContainer.getOrders())
            if (order.getId() == id) {
                order.setApproved();
                approved = true;
            }
        return approved;
    }
}
