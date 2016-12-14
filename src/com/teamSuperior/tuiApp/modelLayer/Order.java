package com.teamSuperior.tuiApp.modelLayer;

import java.io.Serializable;

/**
 * Order model class.
 */
public class Order implements Serializable {
    private int id, productId, contractorId, quantity;
    private boolean approved, delivered;
    private String department;

    public Order(int id, int productId, int contractorId, int quantity, String department, boolean approved, boolean delivered) {
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

    public int getContractorId() {
        return contractorId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved() {
        this.approved = true;
    }

    public boolean isDelivered() {
        return delivered;
    }

    @Override
    public String toString() {
        String[] state = {"No", "Yes"};
        return String.format(
                "ID: %d%nProduct ID: %d%nContractor ID: %d%nQuantity: %d%nDepartment: %s%nApproved: %s%nDelivered: %s%n%n",
                id, productId, contractorId, quantity, department, state[approved ? 1 : 0], state[delivered ? 1 : 0]
        );
    }
}
