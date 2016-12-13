package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Product;
import com.teamSuperior.tuiApp.modelLayer.ProductContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Products controller.
 */
public class ProductController {

    private ProductContainer productContainer;

    public ProductController() {
        productContainer = ProductContainer.getInstance();
        load();
    }

    public void create(int id, String name, String subname, int barcode, String category, double price, String location, int quantity, int contractorId) {
        productContainer.getProducts().add(new Product(id, name, subname, barcode, category, price, location, quantity, contractorId));
    }

    public void save() {
        try (
                FileOutputStream fos = new FileOutputStream("data/products.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(productContainer.getProducts());
        } catch (IOException e) {
            System.out.println("Problem saving products.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        ArrayList<Product> products = null;
        try {
            FileInputStream fis = new FileInputStream("data/products.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            products = (ArrayList<Product>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ignored) {

        } catch (ClassNotFoundException c) {
            System.out.println("Error loading products");
            c.printStackTrace();
        }
        if (products != null) {
            productContainer.setProducts(products);
        }
    }

    public int listAll() {
        productContainer.getProducts().forEach(System.out::print);
        return productContainer.getProducts().size();
    }

    public int listIdAndNameOfProducts() {
        for (Product product : productContainer.getProducts()) {
            System.out.printf("ID: %d  Name: %s%n", product.getId(), product.getName());
        }
        return productContainer.getProducts().size();
    }

    public int listIdNamePriceOfProducts() {
        for (Product product : productContainer.getProducts()) {
            System.out.printf("ID: %d  Name: %s  Normal price: %s$%n", product.getId(), product.getName(), product.getPrice());
        }
        return productContainer.getProducts().size();
    }

    private Product getById(int id) {
        Product productFetched = null;
        boolean found = false;
        Iterator<Product> it = productContainer.getProducts().iterator();
        while (!found && it.hasNext()) {
            Product product = it.next();
            if (product.getId() == id) {
                found = true;
                productFetched = product;
            }
        }
        return productFetched;
    }

    public boolean foundProductById(int id) {
        return getById(id) != null;
    }

    public boolean removeProductById(int id) {
        Product toBeRemoved = getById(id);
        if (toBeRemoved != null) {
            productContainer.getProducts().remove(getById(id));
            return true;
        }
        return false;
    }

    public double calculateDiscount(int id, double newPrice) {
        double discount = 0;
        for (Product product : productContainer.getProducts()) {
            if (product.getId() == id) {
                discount = product.getPrice() - newPrice;
            }
        }
        return discount;
    }

}
