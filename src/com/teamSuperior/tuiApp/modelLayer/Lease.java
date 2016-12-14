package com.teamSuperior.tuiApp.modelLayer;

/**
 * Lease model class.
 */
public class Lease {
    private int id, leaseMachineId, customerId;
    private String borrowDate, returnDate;
    private double price;

    public Lease(int id, int leaseMachineId, int customerId, String borrowDate, String returnDate, double price) {
        this.id = id;
        this.leaseMachineId = leaseMachineId;
        this.customerId = customerId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeaseMachineId() {
        return leaseMachineId;
    }

    public void setLeaseMachineId(int leaseMachineId) {
        this.leaseMachineId = leaseMachineId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d%nLease Machine ID: %d%nCustomer ID: %d%nBorrow date: %s%nReturn date: %s%nPrice: $%.2f%n%n",
                id, leaseMachineId, customerId, borrowDate, returnDate, price
        );
    }
}
