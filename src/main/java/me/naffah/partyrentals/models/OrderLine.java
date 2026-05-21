package me.naffah.partyrentals.models;

import java.sql.Date;

public class OrderLine {
    private int id;
    private int orderId;
    private int productId;
    private String name;
    private String sku;
    private double price;
    private int qty;
    private double taxAmount;
    private double totalAmount;
    private Date createdDate;
    private Date modifiedDate;

    public OrderLine(int productId, String name, String sku, double price, int qty, double taxAmount, double totalAmount) {
        this.productId = productId;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.qty = qty;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
    }

    public OrderLine() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
