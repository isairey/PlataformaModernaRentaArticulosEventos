package me.naffah.partyrentals.models;

public class CustomerType {
    private String name;
    private double taxPercentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public CustomerType(String name, double taxPercentage) {
        this.name = name;
        this.taxPercentage = taxPercentage;
    }
}
