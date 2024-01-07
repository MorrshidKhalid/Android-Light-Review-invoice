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
import com.example.navdemo.adapters.GroupListCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.objects.CommonEntities;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class GroupModelBottomSheet extends BottomSheetDialogFragment implements FrequentMethods {


    // Refers to the dynamic list on the GroupListLayout.
    private final RecyclerView RV_GROUP_LIST;

    // Refers to the view on the (group list layout).
    private final FloatingActionButton BTN;
    // Refers to the spinner on the (New item layout).
    private final Spinner SP_GROUP;



    // Constructor to assign a value to the components above.
    public GroupModelBottomSheet(RecyclerView rvGroupList, FloatingActionButton view, Spinner spGroup) {
        RV_GROUP_LIST = rvGroupList;
        BTN = view;
        SP_GROUP = spGroup;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the view the contain all bottom sheet components.
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Refers to all components that are in the group bottom sheet layout.
        FloatingActionButton btn_add = view.findViewById(R.id.btn_add);
        EditText edt_bottom_sheet = view.findViewById(R.id.edt_bottom_sheet);


        // Set a hint to the EditText (input field).
        edt_bottom_sheet.setHint(getString(R.string.group_bottom_sheet));


        // Get the input from editText and add a group when the button is clicked.
        btn_add.setOnClickListener(v -> addGroup(edt_bottom_sheet));

    }

    // Add the input from the user to the database.
    private void addGroup(EditText user_input) {

        StockDb stockDb = new StockDb(getContext());

            // If something happen while adding display an error message.
            // or input is empty or already exists display message to the user.
            if (!stockDb.isEmpty(user_input) &&
                    !stockDb.isExists((short) 0, (short) 1, getStr(user_input))) {


                if (!stockDb.addOneGroup(new CommonEntities(getStr(user_input))))
                    Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();

                // Update the groupDynamicList.
                if (RV_GROUP_LIST != null)
                    RV_GROUP_LIST.setAdapter(new GroupListCustomAdapter(getContext(),
                            stockDb.cSelectAllFromTable((short) 0)));

                // Print SnackBar confirm message.
                if (BTN != null)
                    temporary_feedback();


                // Update the group spinner.
                if (SP_GROUP != null)
                    loadData(SP_GROUP, stockDb.cSelectAllFromTable((short) 0));


                // Dismiss the input dialog.
                dismiss();
            }
    }

    // Update the spinners on the layout
    @Override
    public void loadData(Spinner spinner, Cursor cursor) {

        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(requireContext(),
                R.layout.spinner,
                cursor,
                new String[] {StockEntry.COLUMN_TABLE_GROUP},
                new int[] {R.id.tv_spinner}, 0);

        spinner.setAdapter(adapter);
    }

    // Return the name with no start or end space(s).
    @Override
    public String getStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    // Print confirm message to the user.
    @Override
    public void temporary_feedback() {
        Snackbar snackbar = Snackbar.make(BTN, R.string.confirm, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}