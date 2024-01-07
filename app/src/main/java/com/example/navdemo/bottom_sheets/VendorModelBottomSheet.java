package com.example.navdemo.bottom_sheets;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.example.navdemo.FrequentMethods;
import com.example.navdemo.R;
import com.example.navdemo.data.StockContracts;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.objects.Vendor;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VendorModelBottomSheet extends BottomSheetDialogFragment implements FrequentMethods {

    // Refers to all vendor spinner on the layout.
    private final Spinner SP_VENDOR;

    // Constructor to assign a value to the vendor spinner above.
    public VendorModelBottomSheet(Spinner spVendor) {
        SP_VENDOR = spVendor;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottom_sheet_vendor_updates, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Find the components on the layout.
        EditText name = view.findViewById(R.id.edt_vendor_name),
                phone = view.findViewById(R.id.edt_vendor_phone),
                email = view.findViewById(R.id.edt_vendor_email),
                company = view.findViewById(R.id.edt_vendor_company);


        FloatingActionButton btn_add_update_vendor = view.findViewById(R.id.btn_add_update_vendor);

        // Change the srcCompat icon to plus, the main view is icon of update, and add new vendor.
        btn_add_update_vendor.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.baseline_add_24));
        btn_add_update_vendor.setOnClickListener(v -> addVendor(name, phone, email, company));

    }

    private void addVendor(EditText in_name, EditText in_phone, EditText in_email, EditText in_company) {

        StockDb stockDb = new StockDb(getContext());


            // If something happen while adding display an error message.
            // or input is empty display message to the user.
            if (!stockDb.isEmpty(in_name)) {

                if (!stockDb.addOneVendor(new Vendor(getStr(in_name), getStr(in_phone), getStr(in_email), getStr(in_company))))
                         temporary_feedback();


                // Update the department spinner
                if (SP_VENDOR != null)
                    loadData(SP_VENDOR, stockDb.cSelectAllFromTable(StockContracts.StockEntry.INDEX_VENDOR_TABLE));


                // Dismiss the input dialog.
                dismiss();
            }
    }


    // Return the name with no start or end space(s).
    @Override
    public String getStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    @Override
    public void temporary_feedback() {
        Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    // Update the spinners on the layout
    @Override
    public void loadData(Spinner spinner, Cursor cursor) {

        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(requireContext(),
                R.layout.spinner,
                cursor,
                new String[] {StockContracts.StockEntry.COLUMNS_TABLE_VENDOR[0]},
                new int[] {R.id.tv_spinner}, 0);

        spinner.setAdapter(adapter);
    }
}
