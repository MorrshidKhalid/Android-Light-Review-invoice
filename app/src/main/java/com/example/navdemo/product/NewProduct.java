package com.example.navdemo.product;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.navdemo.R;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.data.StockContracts.StockEntry;

import com.example.navdemo.databinding.FragmentNewProductBinding;
import com.example.navdemo.bottom_sheets.BrandModelBottomSheet;
import com.example.navdemo.bottom_sheets.GroupModelBottomSheet;
import com.example.navdemo.objects.ProductObject;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NewProduct extends Fragment {


    // The database object.
    private StockDb db;

    // Direct references to all views that have an ID in the layout.
    private FragmentNewProductBinding binding;

    public NewProduct() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewProductBinding.inflate(getLayoutInflater(), container, false);

        // Get the reference to the root layout.
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Initialize the database object with this getContext().
        db = new StockDb(getContext());


        // Navigate other fragments which is  (Setting Group or Brand).
        navigate();

        // Open bottom sheet to add group, and update it's spinner
        openGroupBottomSheet();
        loadSpinnerData(binding.productSpinners.spGroup, db.cSelectAllFromTable(StockEntry.INDEX_GROUP_TABLE), StockEntry.COLUMN_TABLE_GROUP);


        // Open bottom sheet to add brand.
        openBrandBottomSheet();
        // Update brand spinner based on the selected of group id.
        loadSpinnerDataBrand(binding.productSpinners.spGroup);



        // load the UOM and COLOR spinners.
        loadSpinnerData(binding.productSpinners.spUom, db.cSelectAllFromTable(StockEntry.INDEX_UOM_TABLE), StockEntry.COLUMN_TABLE_UOM);
        loadSpinnerData(binding.productSpinners.spColor, db.cSelectAllFromTable(StockEntry.INDEX_COLOR_TABLE), StockEntry.COLUMN_TABLE_COLOR);


        // Add new product to the database.
        addProduct(binding.productInputs.edtItemName, binding.productInputs.edtItemSuk);


    }


    // Handel all navigation to other fragments when the intended view clicked on the layout.
    private void navigate() {

        // Navigate to groupListFragment
        binding.productSpinners.btnGroupSetting.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_newItem_to_groupListFragment));
        // Navigate to brandListFragment
        binding.productSpinners.btnBrandSetting.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_newItem_to_brandListFragment));
    }

    // Open the GroupBottomSheetDialog to enter group data.
    private void openGroupBottomSheet() {

        binding.productSpinners.btnAddGroup.setOnClickListener(v ->
            new GroupModelBottomSheet(null, null, binding.productSpinners.spGroup)
                    .show(getChildFragmentManager(), getTag()));
    }

    // Open the BrandBottomSheetDialog to enter group data.
    private void openBrandBottomSheet() {

        binding.productSpinners.btnAddBrand.setOnClickListener(v ->
            new BrandModelBottomSheet(null, null, binding.productSpinners.spBrand)
                    .show(getChildFragmentManager(), getTag())
        );
    }

    // Update the spinner on the layout
    private void loadSpinnerData(Spinner spinner, Cursor cursor, String clm) {

        if (cursor.getCount() <= 0) {

            // Set default selection value if no spinners items.
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_option, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

        } else {

            SimpleCursorAdapter adapter;
            adapter = new SimpleCursorAdapter(requireContext(),
                    R.layout.spinner,
                    cursor,
                    new String[]{clm},
                    new int[]{R.id.tv_spinner}, 0);

            spinner.setAdapter(adapter);

        }

    }

    // Load the brand data base on the selection of a group item.
    private void loadSpinnerDataBrand(Spinner spinner) {

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               loadSpinnerData(binding.productSpinners.spBrand, db.cSelectAllFromTable(StockEntry.INDEX_BRAND_TABLE,
                       StockEntry.COLUMNS_TABLE_BRAND[1],(int) id), StockEntry.COLUMNS_TABLE_BRAND[0]);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

    }


    // Return the name with no start or end space(s).
    private String getStr(EditText edt) {
        return Objects.requireNonNull(edt.getText()).toString().trim();
    }

    // Get the inputs and add them to the database after check all conditions.
    private void addProduct(EditText name, EditText des) {

        binding.productSpinners.btnSaveProduct.setOnClickListener(v -> {
            // Check if it's satisfy all conditions.
            if (!db.isEmpty(name) && !db.isEmpty(binding.productSpinners.spGroup, binding.productSpinners.spBrand, name)) {
                // Create a new product.
                ProductObject newProduct = new ProductObject((int)binding.productSpinners.spGroup.getSelectedItemId(),
                        (int)binding.productSpinners.spBrand.getSelectedItemId(),
                        getStr(name),
                        (int)binding.productSpinners.spColor.getSelectedItemId(),
                        getStr(des), 0,
                        (int)binding.productSpinners.spUom.getSelectedItemId(),
                        getStr(binding.productInputs.edtNote)
                );

                // Add a new product to database.
                if (db.addOneProduct(newProduct)) {
                    // Go back to the main product fragment and show temporary message.
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                    Toast.makeText(getContext(), R.string.confirm, Toast.LENGTH_SHORT).show();
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