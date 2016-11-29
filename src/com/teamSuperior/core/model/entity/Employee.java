package com.teamSuperior.core.model.entity;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.permission.Level1;
import com.teamSuperior.core.model.permission.Level2;
import com.teamSuperior.core.model.permission.Level3;
import com.teamSuperior.guiApp.GUI.AlertBox;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.teamSuperior.core.connection.DBConnect.*;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Employee{

    private Level1 accessLevel1;
    private Level2 accessLevel2;
    private Level3 accessLevel3;
    ResultSet res = null;
    private static int id = 0;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String zip;
    private String email;
    private String phone;
    private String password;
    private String position;
    private int numberOfSales;
    private double totalRevenue;
    private int accessLevel;
    public Employee() {
        try
        {
            id++;
            String query = "SELECT * FROM employees WHERE id = 1";
            res = getFromDataBase(query);
            while(res.next())
            {
                this.name = res.getString(2);
                this.surname = res.getString(3);
                this.address = res.getString(4);
                this.city = res.getString(5);
                this.zip = res.getString(6);
                this.email = res.getString(7);
                this.phone = res.getString(8);
                this.password = res.getString(9);
                this.position = res.getString(10);
                this.numberOfSales = res.getInt(11);
                this.totalRevenue = res.getDouble(12);
                this.accessLevel = res.getInt(13);
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
            AlertBox.display("Connection Error", ex.getMessage());
        }
        accessLevel1 = new Level1();
        accessLevel2 = new Level2();
        accessLevel3 = new Level3();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getNumberOfSales() {
        return numberOfSales;
    }

    public void setNumberOfSales(int numberOfSales) {
        this.numberOfSales = numberOfSales;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
