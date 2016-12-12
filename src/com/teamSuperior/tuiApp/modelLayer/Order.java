package com.teamSuperior.tuiApp.modelLayer;

/**
 * Order model class.
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

    public int getContractorId() {
        return contractorId;
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

    @Override
    public String toString() {
        String[] state = {"No", "Yes"};
        return String.format(
                "ID: %d%nProduct ID: %d%nContractor ID: %d%nQuantity: %d%nDepartment: %s%nApproved: %s%nDelivered: %s%n%n",
                id, productId, contractorId, quantity, department, state[approved], state[delivered]
        );
    }
}
