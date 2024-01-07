package com.example.navdemo.product;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.navdemo.R;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class UpdateProduct extends DialogFragment {


    public static final String TAG = "UpdateProductDialog";

    private MaterialToolbar toolbar;
    private EditText product_name;
    private StockDb db;
    private Spinner sp_color, sp_uom, sp_group, sp_brand;
    private static long id;

    public void display(FragmentManager fragmentManager, long _id) {
        UpdateProduct exampleDialog = new UpdateProduct();
        exampleDialog.show(fragmentManager, TAG);
        id = _id;
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

        return inflater.inflate(R.layout.fullscreen_update_product, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Find the views.
        toolbar = view.findViewById(R.id.toolbar);
        product_name = view.findViewById(R.id.product_name);
        sp_color = view.findViewById(R.id.sp_color);
        sp_uom = view.findViewById(R.id.sp_uom);
        sp_group = view.findViewById(R.id.sp_group);
        sp_brand = view.findViewById(R.id.sp_brand);


        // Database instance.
        db = new StockDb(getContext());

        setCurrentName();  // Set The current product name on the input edit text.
        handelUpdate(); //  Handel the user update selection.
        setSpCurrentValue();   // Set the spinner with the current product value.
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

    private void handelUpdate() {

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.inflateMenu(R.menu.save);
        toolbar.setTitle(R.string.product_update);
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        // Handel save click
        toolbar.setOnMenuItemClickListener(item -> {

            if (!db.isEmpty(product_name)) {
                boolean newGroup = db.updateRow(StockEntry.TABLE_NAMES[1], StockEntry.COLUMNS_TABLE_PRODUCT[0], (int) sp_group.getSelectedItemId(), StockEntry._ID, id);
                boolean newBrand = db.updateRow(StockEntry.TABLE_NAMES[1], StockEntry.COLUMNS_TABLE_PRODUCT[1], (int) sp_brand.getSelectedItemId(), StockEntry._ID, id);
                boolean newName = db.updateRow(StockEntry.TABLE_NAMES[1], StockEntry.COLUMNS_TABLE_PRODUCT[2], getStr(product_name), StockEntry._ID, id);
                boolean newColor = db.updateRow(StockEntry.TABLE_NAMES[1], StockEntry.COLUMNS_TABLE_PRODUCT[3], (int) sp_color.getSelectedItemId(), StockEntry._ID, id);
                boolean newUom = db.updateRow(StockEntry.TABLE_NAMES[1], StockEntry.COLUMNS_TABLE_PRODUCT[6], (int) sp_uom.getSelectedItemId(), StockEntry._ID, id);


                if (newColor || newUom || newName || newGroup || newBrand) {
                    Toast.makeText(getContext(), R.string.confirm_update_info, Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
                return true;

        });
    }

    // Set the current product name.
    private void setCurrentName() {

        Cursor cursor = db.cSelectAllFromTable(StockEntry.INDEX_PRODUCT_TABLE, StockEntry._ID, (int) id);
        if (cursor.moveToFirst()) {
            String old_value  = cursor.getString(3);
            product_name.setText(old_value);
        }

    }

    // Load all data to the spinner on the layout.
    private void loadSpinnerData(Spinner spinner, Cursor cursor, String clm, int pos) {

        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(requireContext(),
                R.layout.spinner,
                cursor,
                new String[]{clm},
                new int[]{R.id.tv_spinner}, 0);

        spinner.setAdapter(adapter);

        // Set the old selection.
        spinner.setSelection(pos);


    }

    // Set the current spinner value
    private void setSpCurrentValue() {

        Cursor cursor = db.cSelectAllFromTable(StockEntry.INDEX_PRODUCT_TABLE, StockEntry._ID, (int) id);
        if (cursor.moveToFirst()) {
            int cPos = cursor.getInt(4);
            loadSpinnerData(sp_color, db.cSelectAllFromTable(StockEntry.INDEX_COLOR_TABLE), StockEntry.COLUMN_TABLE_COLOR, 12 - cPos);

            int uPos = cursor.getInt(7);
            loadSpinnerData(sp_uom, db.cSelectAllFromTable(StockEntry.INDEX_UOM_TABLE), StockEntry.COLUMN_TABLE_UOM, 35 - uPos);

            int gPos = cursor.getInt(1);
            int gSize = db.cSelectAllFromTable(StockEntry.INDEX_GROUP_TABLE).getCount();
            loadSpinnerData(sp_group, db.cSelectAllFromTable(StockEntry.INDEX_GROUP_TABLE), StockEntry.COLUMN_TABLE_GROUP, gSize - gPos);

            loadSpinnerDataBrand(sp_group);

        }

    }

    // Return the name with no start or end space(s).
    private String getStr(EditText edt) {
        return edt.getText().toString().trim();
    }

    // Load the brand data base on the selection of a group item.
    private void loadSpinnerDataBrand(Spinner spinner) {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadSpinnerData(sp_brand,
                        db.cSelectAllFromTable(StockEntry.INDEX_BRAND_TABLE, StockEntry.COLUMNS_TABLE_BRAND[1],(int) id),
                        StockEntry.COLUMNS_TABLE_BRAND[0],
                        0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
