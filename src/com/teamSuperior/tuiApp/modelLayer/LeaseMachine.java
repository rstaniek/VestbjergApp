package com.teamSuperior.tuiApp.modelLayer;

/**
 * Lease machine model class.
 */
public class LeaseMachine {
    private int id;
    private String name;
    private double priceForDay;
    private boolean leased;

    public LeaseMachine(int id, String name, double priceForDay, boolean leased) {
        this.id = id;
        this.name = name;
        this.priceForDay = priceForDay;
        this.leased = leased;
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

    public void setLeased(boolean leased) {
        this.leased = leased;
    }

    @Override
    public String toString() {
        String[] state = {"No", "Yes"};
        return String.format(
                "ID: %d%nName: %s%nPrice per day: $%.2f%nLeased: %s%n",
                id, name, priceForDay, state[leased ? 1 : 0]
        );
    }
}
