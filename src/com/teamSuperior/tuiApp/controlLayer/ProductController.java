package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Product;
import com.teamSuperior.tuiApp.modelLayer.ProductContainer;

import java.util.Iterator;

/**
 * Products controller.
 */
public class ProductController {

    private ProductContainer productContainer;

    public ProductController() {
        productContainer = ProductContainer.getInstance();
    }

    public void addProduct(int id, String name, String subname, int barcode, String category, double price, String location, int quantity, int contractorId) {
        productContainer.getProducts().add(new Product(id, name, subname, barcode, category, price, location, quantity, contractorId));
    }

    public int listAllProducts() {
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

    public boolean foundProductById(int id) {
        boolean found = false;
        for (Product product : productContainer.getProducts())
            if (product.getId() == id)
                found = true;
        return found;
    }

    public boolean removeProductById(int id) {
        boolean removed = false;
        Iterator<Product> it = productContainer.getProducts().iterator();
        while (!removed && it.hasNext()) {
            Product product = it.next();
            if (product.getId() == id) {
                removed = true;
                it.remove();
            }
        }
        return removed;
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
