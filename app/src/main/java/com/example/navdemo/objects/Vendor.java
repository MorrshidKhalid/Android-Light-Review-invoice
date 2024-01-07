package com.example.navdemo.objects;

public class Vendor {

    String name,
    phone,
    email,
    company;


    // Constructor create a vendor object.
    public Vendor(String name, String phone, String email, String company) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.company = company;
    }


    // Getters
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }
}
