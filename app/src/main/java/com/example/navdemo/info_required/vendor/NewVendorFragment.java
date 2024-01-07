package com.example.navdemo.info_required.vendor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.navdemo.data.StockDb;
import com.example.navdemo.databinding.FragmentNewVendorBinding;
import com.example.navdemo.objects.Vendor;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NewVendorFragment extends Fragment {


    // Direct reference to the views that are have IDs
    FragmentNewVendorBinding binding;

    public NewVendorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewVendorBinding.inflate(getLayoutInflater(), container, false);


        // Return the root view of this fragment
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // add user input to the database.
        binding.venInfo.btnSaveUpdateVendor.setOnClickListener(v -> addVendor());
    }

    private void addVendor() {

        StockDb db = new StockDb(getContext());
        if (!db.isEmpty(binding.venInfo.edtVendorName))
            if (db.addOneVendor(new Vendor(getStr(binding.venInfo.edtVendorName), getStr(binding.venInfo.edtVendorPhone),
                    getStr(binding.venInfo.edtVendorEmail), getStr(binding.venInfo.edtVendorCompany)))) {

                Navigation.findNavController(binding.getRoot()).popBackStack();
            }

    }

    private String getStr(EditText edt) {
        return Objects.requireNonNull(edt.getText()).toString().trim();
    }
}