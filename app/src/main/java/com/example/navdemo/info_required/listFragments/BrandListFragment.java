package com.example.navdemo.info_required.listFragments;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navdemo.adapters.BrandListCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.databinding.FragmentBrandListBinding;
import com.example.navdemo.bottom_sheets.BrandModelBottomSheet;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrandListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrandListFragment extends Fragment {

    // Refers to the components that are display and add group in the layout.
    private FragmentBrandListBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public BrandListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrandListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrandListFragment newInstance(String param1, String param2) {
        BrandListFragment fragment = new BrandListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBrandListBinding.inflate(getLayoutInflater(), container, false);


        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Add new brand.
        // Show the bottom dialog and send it the dynamicList on the layout.
        addOneBrand();


        // Update the brand recyclerView
        updateDynamicList();

    }

    // Show the bottom dialog and send it the dynamicList on the layout.
    private void addOneBrand() {
        binding.btnAddNewBrand.setOnClickListener(v ->
                new BrandModelBottomSheet(binding.rvBrandList, binding.btnAddNewBrand, null)
                     .show(getChildFragmentManager(), getTag())
        );
    }

    // Update the brand dynamicList.
    private void updateDynamicList() {

        binding.rvBrandList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvBrandList.setAdapter(new BrandListCustomAdapter(getContext(),
                new StockDb(getContext()).retrieve(
                        StockEntry.TABLE_NAMES[2],
                        StockEntry.TABLE_NAMES[0],
                        StockEntry.COLUMNS_TABLE_BRAND[1]
                )));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}

