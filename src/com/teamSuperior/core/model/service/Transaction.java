package com.teamSuperior.core.model.service;

import com.teamSuperior.core.model.Model;
import com.teamSuperior.core.model.entity.Customer;
import com.teamSuperior.core.model.entity.Employee;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Transaction entity
 */
@Entity
@Table(name = "transactions")
public class Transaction implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Customer customer;
    private String productIDs, discountIDs;
    private String description;
    private double price;
    private Timestamp createDate;

    public Transaction() {
    }

    public Transaction(Employee employee, Customer customer, String productIDs, String discountIDs, String description, double price) {
        this.employee = employee;
        this.customer = customer;
        this.productIDs = productIDs;
        this.discountIDs = discountIDs;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getProductIDs() {
        return productIDs;
    }

    public void setProductIDs(String productIDs) {
        this.productIDs = productIDs;
    }

    public String getDiscountIDs() {
        return discountIDs;
    }

    public void setDiscountIDs(String discountIDs) {
        this.discountIDs = discountIDs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toJson() {
        return null;
    }
}
