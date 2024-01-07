package com.example.navdemo.objects;

public class ReceiptedProducts {


    private int receiving_FK,
    product_FK,
    quantity;


    // Constructor
    public ReceiptedProducts(int receiving_FK, int product_FK, int quantity) {
        this.receiving_FK = receiving_FK;
        this.product_FK = product_FK;
        this.quantity = quantity;
    }


    // Getters


    public int getReceiving_FK() {
        return receiving_FK;
    }

    public int getProduct_FK() {
        return product_FK;
    }

    public int getQuantity() {
        return quantity;
    }
}
