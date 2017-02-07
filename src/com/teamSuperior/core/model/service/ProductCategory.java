package com.teamSuperior.core.model.service;

import com.teamSuperior.core.model.Model;

import javax.persistence.*;

/**
 * Product category entity
 */
@Entity
@Table(name = "productCategories")
public class ProductCategory implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name, url;

    public ProductCategory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    @Override
    public String toJson() {
        return null;
    }
}
