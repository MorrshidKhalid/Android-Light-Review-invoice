package com.example.navdemo.objects;

public class ReceiptOrderCard {

    long mDepartment_FK,
            mVendor_FK,
            mStore_FK;
    String receiving_Date;
    String mDescription;


    // Constructor
    public ReceiptOrderCard(long mDepartment_FK, long mVendor_FK, long mStore_FK, String receiving_Date, String description) {
        this.mDepartment_FK = mDepartment_FK;
        this.mVendor_FK = mVendor_FK;
        this.mStore_FK = mStore_FK;
        this.receiving_Date = receiving_Date;
        this.mDescription = description;
    }

    @Override
    public String toString() {
        return "ReceiptOrderCard{" +
                "mDescription='" + mDescription + '\'' +
                '}';
    }

    // Getters

    public long get_mDepartment_FK() {
        return mDepartment_FK;
    }

    public long get_mVendor_FK() {
        return mVendor_FK;
    }

    public long get_mStore_FK() {
        return mStore_FK;
    }

    public String getReceiving_Date() {
        return receiving_Date;
    }

    public String get_mDescription() {
        return mDescription;
    }
}
