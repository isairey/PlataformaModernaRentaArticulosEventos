package me.naffah.partyrentals.models;

public class CartItem {
    String item;  // Combination of id and product name
    int qty;
    double price;
    double tax;
    double total;
    Product product;

    public CartItem(String item, int qty, double price, double tax, double total, Product product) {
        this.item = item;
        this.qty = qty;
        this.price = price;
        this.tax = tax;
        this.total = total;
        this.product = product;
    }

    public CartItem() {}

    public String getItem() {
        return item;
    }

    public void setItem(String name) {
        this.item = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
