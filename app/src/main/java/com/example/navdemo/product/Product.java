package com.example.navdemo.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.navdemo.R;
import com.example.navdemo.adapters.ProductListCustomAdapter;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.databinding.FragmentProductBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product extends Fragment {


    // Refers to the components that are display and add group in the layout.
    private FragmentProductBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public Product() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductObject.
     */
    // TODO: Rename and change types and number of parameters
    public static Product newInstance(String param1, String param2) {
        Product fragment = new Product();
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
        binding = FragmentProductBinding.inflate(getLayoutInflater(), container, false);

        // Root layout.
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // When the (btn_addNewProduct) is clicked navigate to the (newProduct) fragment.
        binding.btnAddNewProduct.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_nav_item_to_newItem));


        // Update the dynamicList on the layout.
        updateDynamicList();
    }


    // Update the product dynamicList.
    private void updateDynamicList() {

        StockDb db = new StockDb(getContext());
        ProductListCustomAdapter adapter = new ProductListCustomAdapter(getContext(),
                db.p_retrieve(),
                getChildFragmentManager(),
                binding.imgEmpty);

        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProducts.setAdapter(adapter);

        // Display the empty image.
        if (adapter.getItemCount() <= 0)
            binding.imgEmpty.setVisibility(View.VISIBLE);
        else binding.imgEmpty.setVisibility(View.GONE);


        // Hide the button on scrolling.
        binding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // if the recycler view is scrolled
                // above hide the FAB
                if (dy > 10 && binding.btnAddNewProduct.isShown())
                    binding.btnAddNewProduct.hide();

                // if the recycler view is
                // scrolled above show the FAB
                if (dy > -10 && !binding.btnAddNewProduct.isShown())
                    binding.btnAddNewProduct.show();


                // of the recycler view is at the first
                // item always show the FAB
                if (!binding.rvProducts.canScrollVertically(-1)) {
                    binding.btnAddNewProduct.show();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}