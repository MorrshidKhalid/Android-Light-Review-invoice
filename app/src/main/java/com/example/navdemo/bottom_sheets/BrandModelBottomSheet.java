package com.example.navdemo.bottom_sheets;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navdemo.R;
import com.example.navdemo.adapters.BrandListCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.objects.CommonEntities;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class BrandModelBottomSheet extends BottomSheetDialogFragment {


    // Refers to the dynamic list on the BrandListLayout.
    private final RecyclerView RV_BRAND;

    // Refers to the button on the BrandListLayout,
    // that you want the SnackBar print the confirm message above  or under it.
    private final FloatingActionButton BTN;
    // Refers to the spinner on the (bottomSheetLayout).
    private final Spinner SP_BRAND;

    public BrandModelBottomSheet(RecyclerView rvBrand, FloatingActionButton btn, Spinner spBrand) {
        RV_BRAND = rvBrand;
        BTN = btn;
        SP_BRAND = spBrand;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the view the contain all bottom sheet components.
        return inflater.inflate(R.layout.bottom_sheet_brand, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        // Refers to all components that are in the bottom sheet layout.
        EditText edt_bottom_sheet_brand = view.findViewById(R.id.edt_bottom_sheet_brand);
        Spinner sp_b_group = view.findViewById(R.id.sp_b_group);
        FloatingActionButton btn_bs_add_brand = view.findViewById(R.id.btn_addBrand);


        // Set a hint to the EditText (input field).
        edt_bottom_sheet_brand.setHint(R.string.brand_bottom_sheet);


        // Update group spinner.
        update_spinner(sp_b_group,
                new StockDb(getContext()).cSelectAllFromTable((short) 0), StockEntry.COLUMN_TABLE_GROUP);


        // When (btn_bs_add_brand) clicked add brand to database.
        addBrand(btn_bs_add_brand, edt_bottom_sheet_brand);

    }

    int selectedGroupId;
    // Update the spinner on the layout
    private void update_spinner(Spinner spinner, Cursor cursor, String clm) {

        if (cursor.getCount() == 0) {

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

        // Set selected group id;
        getGroupFk(spinner);
    }

    // Get the id-->(FOREIGN KEY) of selected group.
    private void getGroupFk(Spinner spinner) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroupId = (int)id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Do nothing.*/ }
        });

    }

    // Add the input from the user to the database.
    private void addBrand(FloatingActionButton btn, EditText brand_name) {

        StockDb stockDb = new StockDb(getContext());

        btn.setOnClickListener(v -> {
            // If something happen while adding display an error message.
            // or input is empty or already exists display message to the user.
            if (!stockDb.isEmpty(brand_name) && !stockDb.isExists((short) 2, (short) 1, (short) 2, getBrandName(brand_name), selectedGroupId)) {
                if (selectedGroupId > 0) {
                    if (!stockDb.addOneBrand(new CommonEntities(getBrandName(brand_name), selectedGroupId)))
                        Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), R.string.sp_brand, Toast.LENGTH_LONG).show();



                // Update the brandDynamicList of the brand list.
                if (RV_BRAND != null)
                    RV_BRAND.setAdapter(new BrandListCustomAdapter(getContext(),
                            stockDb.retrieve(
                            StockEntry.TABLE_NAMES[2],
                            StockEntry.TABLE_NAMES[0],
                            StockEntry.COLUMNS_TABLE_BRAND[1])
                    ));


                // Print SnackBar confirm message.
                if (BTN != null)
                    printConfirmMessage();

                if (selectedGroupId != 0 && SP_BRAND != null)
                    update_spinner(SP_BRAND,
                            new StockDb(getContext()).cSelectAllFromTable((short) 2, StockEntry.COLUMNS_TABLE_BRAND[1], (short) selectedGroupId)
                            ,StockEntry.COLUMNS_TABLE_BRAND[0]);

                // Dismiss the input dialog.
                dismiss();
            }
        });

    }

    // Return the name with no start or end space(s).
    private String getBrandName(EditText edt) {
        return edt.getText().toString().trim();
    }

    private void printConfirmMessage() {
        Snackbar snackbar = Snackbar.make(BTN, R.string.confirm, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}