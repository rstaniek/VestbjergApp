package com.teamSuperior.core.model.service;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Domestos Maximus on 24-Nov-16.
 */
public class Lease {
    private int id, leaseMachineID, customerID, employeeID;
    private float price;
    private Date borrowDate;
    private Time borrowTime;
    private Date returnDate;
    private Time returnTime;

    public Lease (int id, int leaseMachineID, int customerID, float price, Date borrowDate, Time borrowTime, Date returnDate, Time returnTime, int employeeID) {
        this.id = id;
        this.leaseMachineID = leaseMachineID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.price = price;
        this.borrowDate = borrowDate;
        this.borrowTime = borrowTime;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
    }

    public int getId() {
        return id;
    }

    public int getLeaseMachineID() {
        return leaseMachineID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public float getPrice() {
        return price;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Time getBorrowTime() {
        return borrowTime;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public Time getReturnTime() {
        return returnTime;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", leaseMachineID=" + leaseMachineID +
                ", customerID=" + customerID +
                ", employeeID=" + employeeID +
                ", price=" + price +
                ", borrowDate=" + borrowDate +
                ", borrowTime=" + borrowTime +
                ", returnDate=" + returnDate +
                ", returnTime=" + returnTime +
                '}';
    }
}
