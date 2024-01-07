package com.example.navdemo.objects;

public class InvoiceHeader {

    String data;
    String inv_no;
    String ven;
    String dep;
    String sto;
    String des;


    public InvoiceHeader(String data, String inv_no, String ven, String dep, String sto, String des) {
        this.data = data;
        this.inv_no = inv_no;
        this.ven = ven;
        this.dep = dep;
        this.sto = sto;
        this.des = des;
    }


    public String getData() {
        return data;
    }

    public String getInv_no() {
        return inv_no;
    }

    public String getVen() {
        return ven;
    }

    public String getDep() {
        return dep;
    }

    public String getSto() {
        return sto;
    }

    public String getDes() {
        return des;
    }
}
