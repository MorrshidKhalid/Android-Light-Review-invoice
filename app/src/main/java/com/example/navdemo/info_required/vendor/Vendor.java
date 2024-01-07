package com.example.navdemo.info_required.vendor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navdemo.R;
import com.example.navdemo.adapters.VendorListCustomAdapter;
import com.example.navdemo.data.StockDb;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Vendor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Vendor extends Fragment {

    RecyclerView rv_vendor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public Vendor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Vendor.
     */
    // TODO: Rename and change types and number of parameters
    public static Vendor newInstance(String param1, String param2) {
        Vendor fragment = new Vendor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor, container, false);


        // Refers to the components on the layout
        FloatingActionButton btn_addNewVendor = view.findViewById(R.id.btn_addNewVendor);
        rv_vendor = view.findViewById(R.id.rv_vendor);

        // When the (btn_addNewVendor) is clicked navigate to the (newVendor) fragment.
        btn_addNewVendor.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_nav_vendor_to_newVendorFragment));


        updateDynamicList();
        return view;
    }


    private void updateDynamicList() {

        rv_vendor.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_vendor.setAdapter(new VendorListCustomAdapter( requireContext(),
        new StockDb(getContext()).cSelectAllFromTable((short) 4)
        ));

    }
}