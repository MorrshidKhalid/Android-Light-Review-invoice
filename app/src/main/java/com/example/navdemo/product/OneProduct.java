package com.example.navdemo.product;

public class OneProduct {

    String name, color, des, note;

    int qty;


    public OneProduct(String name, String color, String des, String note, int qty) {
        this.name = name;
        this.color = color;
        this.des = des;
        this.note = note;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDes() {
        return des;
    }

    public String getNote() {
        return note;
    }

    public int getQty() {
        return qty;
    }
}
