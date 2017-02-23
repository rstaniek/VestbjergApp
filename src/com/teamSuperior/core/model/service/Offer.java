package com.teamSuperior.core.model.service;

import com.teamSuperior.core.model.Model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Offer entity
 */
@Entity
@Table(name = "offers")
public class Offer implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date expiresDate;
    @ManyToOne
    private Product product;
    private double price, discount;
    private Timestamp createDate;

    public Offer() {
    }

    public Offer(Product product, double price, double discount, Date expiresDate) {
        this.product = product;
        this.price = price;
        this.discount = discount;
        this.expiresDate = expiresDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
