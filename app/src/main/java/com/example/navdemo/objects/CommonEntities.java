package com.example.navdemo.objects;

public class CommonEntities {

    private String mName;
    private int mForeingKey;

    // Constructor create object name.
    public CommonEntities(String mName) {
        this.mName = mName;
    }

    // Constructor create object name and ID.
    public CommonEntities(String mName, int mForeingKey) {
        this.mName = mName;
        this.mForeingKey = mForeingKey;
    }

    // Getters.
    public String get_mName() {
        return mName;
    }

    public int get_mForeingKey() {
        return mForeingKey;
    }
}
