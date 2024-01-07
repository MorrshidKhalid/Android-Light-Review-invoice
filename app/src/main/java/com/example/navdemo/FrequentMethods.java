package com.example.navdemo;

import android.database.Cursor;
import android.widget.EditText;
import android.widget.Spinner;

public interface FrequentMethods {

    String getStr(EditText editText);

    void  temporary_feedback();

    void loadData(Spinner spinner, Cursor cursor);
}