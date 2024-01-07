package com.example.navdemo.info_required.listFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navdemo.adapters.GroupListCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.databinding.FragmentGroupListBinding;
import com.example.navdemo.bottom_sheets.GroupModelBottomSheet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListFragment extends Fragment {

    // Direct references to all views that have an ID in the layout.
    private FragmentGroupListBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public GroupListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupListFragment newInstance(String param1, String param2) {
        GroupListFragment fragment = new GroupListFragment();
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
        binding = FragmentGroupListBinding.inflate(getLayoutInflater(), container, false);

        // Get the reference to the root layout.
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Add new group.
        // Show the bottom dialog and send it the dynamicList on the layout.
        addOneGroup();

        // Update the group recyclerView.
        updateDynamicList();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // After outlive the fragment make sure to clean up any references.
        binding = null;
    }


    // Show the bottom dialog and send it the dynamicList on the layout.
    private void addOneGroup() {

        binding.btnAddNewGroup.setOnClickListener(v ->
                new GroupModelBottomSheet(binding.rvGroupList, binding.btnAddNewGroup, null)
                .show(getChildFragmentManager(), getTag()));
    }

    // Update the group dynamicList.
    private void updateDynamicList() {

        binding.rvGroupList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvGroupList.setAdapter(new GroupListCustomAdapter(getContext(),
                new StockDb(getContext()).cSelectAllFromTable(StockEntry.INDEX_GROUP_TABLE)));

    }

}



