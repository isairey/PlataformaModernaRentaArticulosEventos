package me.naffah.partyrentals.models;

import java.sql.Date;

public class Order {
    private int id;
    private int customer;
    private String paymentMethod;
    private double paidAmount;
    private Date startDate;
    private Date endDate;
    private Date returnedDate;
    private String status;
    private Date createdDate;
    private Date modifiedDate;

    public Order(int id, int customer, String paymentMethod, double paidAmount, Date startDate, Date endDate, String status) {
        this.id = id;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.paidAmount = paidAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Order() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
