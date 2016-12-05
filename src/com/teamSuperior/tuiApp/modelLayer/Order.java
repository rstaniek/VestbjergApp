package com.teamSuperior.tuiApp.modelLayer;

/**
 * Created by Smoothini on 01.12.2016.
 */
public class Order {
    private int id, productId, contractorId, quantity, approved, delivered;
    private String department;

    public Order(int id, int productId, int contractorId, int quantity, String department, int approved, int delivered) {
        this.id = id;
        this.productId = productId;
        this.contractorId = contractorId;
        this.quantity = quantity;
        this.department = department;
        this.approved = approved;
        this.delivered = delivered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getContractorId() {
        return contractorId;
    }

    public void setContractorId(int contractorId) {
        this.contractorId = contractorId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        String[] state = {"No", "Yes"};
        return String.format(
                "ID: %d%nProduct ID: %d%nContractor ID: %d%nQuantity: %d%nDepartment: %s%nApproved: %s%nDelivered: %s%n%n",
                id, productId, contractorId, quantity, department, state[approved], state[delivered]
        );
    }
}
