package com.example.navdemo.info_required.listFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navdemo.adapters.DepartmentCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.databinding.FragmentDepartmentBinding;
import com.example.navdemo.bottom_sheets.DepartmentModelBottomSheet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DepartmentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepartmentListFragment extends Fragment {


    // Direct references to all views that have an ID in the layout.
    private FragmentDepartmentBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public DepartmentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DepartmentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepartmentListFragment newInstance(String param1, String param2) {
        DepartmentListFragment fragment = new DepartmentListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDepartmentBinding.inflate(getLayoutInflater(), container, false);


        // Get the reference to the root layout.
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        // Open to add a new department.
        addOneDepartment();

        // Update the dynamic list on the layout
        updateDynamicList();
    }

    // Show the bottom dialog and send it the dynamicList on the layout.
    private void addOneDepartment() {

        // binding.rvDepartmentList, binding.btnAddNewDepartment --> Don't forget to add those
        binding.btnAddNewDepartment.setOnClickListener(v ->
                new DepartmentModelBottomSheet(null, binding.rvDepartmentList, binding.btnAddNewDepartment)
                        .show(getChildFragmentManager(), getTag()));
    }

    // Update the group dynamicList.
    private void updateDynamicList() {

        binding.rvDepartmentList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvDepartmentList.setAdapter(new DepartmentCustomAdapter(getContext(),
                new StockDb(getContext()).cSelectAllFromTable(StockEntry.INDEX_DEPARTMENT_TABLE)));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}