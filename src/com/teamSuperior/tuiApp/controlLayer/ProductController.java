package com.teamSuperior.tuiApp.controlLayer;

import com.teamSuperior.tuiApp.modelLayer.Product;
import com.teamSuperior.tuiApp.modelLayer.ProductContainer;

import java.io.*;
import java.util.Iterator;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class ProductController {

    private ProductContainer productContainer;
    private String fileName = "data/products.txt";

    public ProductController() {
        productContainer = ProductContainer.getInstance();
    }

    public void importFromFile() {
        try (
                FileInputStream fis = new FileInputStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split("\\|");
                addProduct(Integer.parseInt(args[0]), args[1], args[2], Integer.parseInt(args[3]), args[4], Double.parseDouble(args[5]), args[6], Integer.parseInt(args[7]), Integer.parseInt(args[8]));
            }
        } catch (IOException e) {
            System.out.printf("Problems loading %s %n", fileName);
            e.printStackTrace();
        }
    }

    public void exportToFile() {
        try (
                FileOutputStream fos = new FileOutputStream(fileName);
                PrintWriter writer = new PrintWriter(fos)
        ) {
            for (Product product : productContainer.getProducts()) {
                writer.printf("%d|%s|%s|%d|%s|%f|%s|%d|%d%n", product.getId(), product.getName(), product.getSubname(), product.getBarcode(), product.getCategory(), product.getPrice(), product.getLocation(), product.getQuantity(), product.getContractorId());
            }
        } catch (IOException e) {
            System.out.printf("Problem saving %s %n", fileName);
            e.printStackTrace();
        }
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
