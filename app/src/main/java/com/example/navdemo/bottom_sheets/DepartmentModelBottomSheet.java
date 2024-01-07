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
import com.example.navdemo.adapters.DepartmentCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.objects.CommonEntities;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class DepartmentModelBottomSheet extends BottomSheetDialogFragment implements FrequentMethods {

    // Refers to the department spinner
    private final Spinner SP_DEPARTMENT;
    private final RecyclerView RV_DEPARTMENT;
    private final FloatingActionButton BTN_DEPARTMENT;

    // Constructor to assign a value to the department spinner above.
    public DepartmentModelBottomSheet(Spinner spDepartment, RecyclerView rvDepartment, FloatingActionButton btnDepartment) {
        SP_DEPARTMENT = spDepartment;
        RV_DEPARTMENT = rvDepartment;
        BTN_DEPARTMENT = btnDepartment;
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the view the contain all bottom sheet components.
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Find the components on the layout.
        FloatingActionButton btn_add = view.findViewById(R.id.btn_add);
        EditText edt_bottom_sheet_department = view.findViewById(R.id.edt_bottom_sheet);

        // set a hint to the user.
        edt_bottom_sheet_department.setHint(getString(R.string.department_bottom_sheet));


        // Get the input from editText and add a department when the button is clicked.
        btn_add.setOnClickListener(v -> addDepartment(edt_bottom_sheet_department));
    }

    private void addDepartment(EditText user_input) {

        StockDb stockDb = new StockDb(getContext());

            // If something happen while adding display an error message.
            // or input is empty or already exists display message to the user.
            if (!stockDb.isEmpty(user_input) &&
                    !stockDb.isExists(StockEntry.INDEX_DEPARTMENT_TABLE,
                            (short) 1, getStr(user_input))) {

                if (!stockDb.addOneDepartment(new CommonEntities(getStr(user_input))))
                    Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();



                // Update the store spinner
                if (SP_DEPARTMENT != null)
                        loadData(SP_DEPARTMENT, stockDb.cSelectAllFromTable(StockEntry.INDEX_DEPARTMENT_TABLE));

                // Update the dynamic list.
                if (RV_DEPARTMENT != null)
                    updateDynamicList();

                // Display feedback to the user
                if (BTN_DEPARTMENT != null)
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
        Snackbar snackbar = Snackbar.make(BTN_DEPARTMENT, R.string.confirm, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    // Update the spinners on the layout
    @Override
    public void loadData(Spinner spinner, Cursor cursor) {

        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(requireContext(),
                R.layout.spinner,
                cursor,
                new String[] {StockEntry.COLUMN_TABLE_DEPARTMENT},
                new int[] {R.id.tv_spinner}, 0);

        spinner.setAdapter(adapter);
    }


    // Update the group dynamicList.
    private void updateDynamicList() {

        RV_DEPARTMENT.setAdapter(new DepartmentCustomAdapter(getContext(),
                new StockDb(getContext()).cSelectAllFromTable(StockEntry.INDEX_DEPARTMENT_TABLE)));
    }

}
