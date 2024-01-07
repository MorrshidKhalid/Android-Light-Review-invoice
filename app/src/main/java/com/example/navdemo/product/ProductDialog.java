package com.example.navdemo.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.navdemo.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class ProductDialog extends DialogFragment{

    private MaterialToolbar toolbar;
    private static OneProduct oneProduct;

    public void display(FragmentManager fragmentManager, OneProduct _oneProduct) {
        ProductDialog dialog = new ProductDialog();
        dialog.show(fragmentManager, getTag());
        oneProduct = _oneProduct;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fullscreen_product_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Find Components.
        toolbar = view.findViewById(R.id.toolbar);
        TextView p_quantity = view.findViewById(R.id.p_quantity),
                 p_color = view.findViewById(R.id.p_color),
                 p_name = view.findViewById(R.id.p_name),
                 p_description = view.findViewById(R.id.p_description),
                 p_note = view.findViewById(R.id.p_note);


        handelBar();
        loadHeadData(p_quantity, p_color, p_name, p_description, p_note);
    }



    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(getDialog().getWindow()).setLayout(width, height);
        }
    }


    private void loadHeadData(TextView qty, TextView color, TextView name, TextView des, TextView note) {

        String[] contentOfTV = { "Available: " + oneProduct.getQty(), "Color " + oneProduct.getColor(), "Description " + oneProduct.getDes(), "Note: " + oneProduct.getNote() };

        qty.setText(contentOfTV[0]);
        color.setText(contentOfTV[1]);
        name.setText(oneProduct.getName());
        des.setText(contentOfTV[2]);
        note.setText(contentOfTV[3]);
    }

    private void handelBar() {

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(R.string.product_details);
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white));

    }


}
