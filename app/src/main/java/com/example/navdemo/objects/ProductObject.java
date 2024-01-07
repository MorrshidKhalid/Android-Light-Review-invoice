package com.example.navdemo.objects;

public class ProductObject {

    int p_Group_code,
        p_Brand_code;


    String product_name;
    int color;
    String suk;


    int quantity,
        p_UOM;
    String note;

    // Constructor Create object FK-Group, FK-Brand, product-name, color, suk, quantity, FK-UOM.


    public ProductObject(int p_Group_code, int p_Brand_code, String product_name, int color, String suk, int quantity, int p_UOM, String note) {
        this.p_Group_code = p_Group_code;
        this.p_Brand_code = p_Brand_code;
        this.product_name = product_name;
        this.color = color;
        this.suk = suk;
        this.quantity = quantity;
        this.p_UOM = p_UOM;
        this.note = note;
    }

    // Getters
    public int getP_Group_code() {
        return p_Group_code;
    }
    public int getP_Brand_code() {
        return p_Brand_code;
    }

    public int getQuantity() {
        return quantity;
    }
    public int getP_UOM() {
        return p_UOM;
    }
    public String getProduct_name() {
        return product_name;
    }

    public int getColor() {
        return color;
    }
    public String getSuk() {
        return suk;
    }
    public String getNote() {
        return note;
    }
}

