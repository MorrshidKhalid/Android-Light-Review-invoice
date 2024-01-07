package com.example.navdemo.receiving;

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
import com.example.navdemo.adapters.ReceivingListCustomAdapter;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.databinding.FragmentReceivingOrderBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceivingOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceivingOrder extends Fragment {


    // Direct references to all views that have an ID in the layout
    private FragmentReceivingOrderBinding binding;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ReceivingOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReceivingOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static ReceivingOrder newInstance(String param1, String param2) {
        ReceivingOrder fragment = new ReceivingOrder();
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
        binding = FragmentReceivingOrderBinding.inflate(getLayoutInflater(), container, false);


        // Get the reference to the root layout.
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.btnAddReceiving.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_receivingOrder_to_addReceivingFragment));

        update_dynamicList();
    }

    private void update_dynamicList() {

        StockDb db = new StockDb(getContext());
        ReceivingListCustomAdapter adapter = new ReceivingListCustomAdapter(getContext(), db.r_retrieve(), getChildFragmentManager(), binding.imgEmpty);
        binding.rvReceiving.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvReceiving.setAdapter(adapter);

        if (adapter.getItemCount() <= 0)
            binding.imgEmpty.setVisibility(View.VISIBLE);
        else binding.imgEmpty.setVisibility(View.GONE);


        // Hide the button on scrolling.
        binding.rvReceiving.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // if the recycler view is scrolled
                // above hide the FAB
                if (dy > 10 && binding.btnAddReceiving.isShown())
                    binding.btnAddReceiving.hide();

                // if the recycler view is
                // scrolled above show the FAB
                if (dy > -10 && !binding.btnAddReceiving.isShown())
                    binding.btnAddReceiving.show();


                // of the recycler view is at the first
                // item always show the FAB
                if (!binding.rvReceiving.canScrollVertically(-1)) {
                    binding.btnAddReceiving.show();
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