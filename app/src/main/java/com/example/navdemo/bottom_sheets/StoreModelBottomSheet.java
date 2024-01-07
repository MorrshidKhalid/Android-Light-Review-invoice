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
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navdemo.FrequentMethods;
import com.example.navdemo.R;
import com.example.navdemo.adapters.StoreListCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.objects.CommonEntities;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class StoreModelBottomSheet extends BottomSheetDialogFragment implements FrequentMethods {

    // Refers to the store spinner
    private final Spinner SP_STORE;
    private final RecyclerView RV_STORE;
    private final FloatingActionButton BTN_STORE;

    public StoreModelBottomSheet(Spinner spStore, RecyclerView rvStore, FloatingActionButton btnStore) {
        SP_STORE = spStore;
        RV_STORE = rvStore;
        BTN_STORE = btnStore;
    }

    // Constructor to assign a value to the store spinner above.



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Find the components on the layout.
        FloatingActionButton btn_add = view.findViewById(R.id.btn_add);
        EditText edt_bottom_sheet_store = view.findViewById(R.id.edt_bottom_sheet);

        // set a hint to the user.
        edt_bottom_sheet_store.setHint(getString(R.string.store_bottom_sheet));


        btn_add.setOnClickListener(v -> addStore(edt_bottom_sheet_store));

    }

    private void addStore(EditText user_input) {

        StockDb stockDb = new StockDb(getContext());


            // If something happen while adding display an error message.
            // or input is empty or already exists display message to the user.
            if (!stockDb.isEmpty(user_input) && !stockDb.isExists(StockEntry.INDEX_STORE_TABLE, (short) 1, getStr(user_input))) {

                if (!stockDb.addOneStore(new CommonEntities(getStr(user_input))))
                    Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();


                // Update the department spinner
                if (SP_STORE != null)
                    loadData(SP_STORE, stockDb.cSelectAllFromTable(StockEntry.INDEX_STORE_TABLE));

                // Update the dynamic list.
                if (RV_STORE != null)
                    updateDynamicList();

                // Display feedback to the user
                if (BTN_STORE != null)
                    temporary_feedback();

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
        Snackbar snackbar = Snackbar.make(BTN_STORE, R.string.confirm, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    // Update the spinners on the layout
    @Override
    public void loadData(Spinner spinner, Cursor cursor) {

        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(requireContext(),
                R.layout.spinner,
                cursor,
                new String[] {StockEntry.COLUMN_TABLE_STORE},
                new int[] {R.id.tv_spinner}, 0);

        spinner.setAdapter(adapter);
    }

    // Update the group dynamicList.
    private void updateDynamicList() {

        RV_STORE.setAdapter(new StoreListCustomAdapter(getContext(),
                new StockDb(getContext()).cSelectAllFromTable(StockEntry.INDEX_STORE_TABLE)));

    }
}