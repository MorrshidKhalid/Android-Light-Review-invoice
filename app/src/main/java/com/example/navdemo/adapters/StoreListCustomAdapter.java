package com.example.navdemo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navdemo.R;
import com.example.navdemo.data.StockContracts;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class StoreListCustomAdapter extends RecyclerView.Adapter<StoreListCustomAdapter.ViewHolder> {


    private final Context mContext;
    private Cursor mCursor;
    private Snackbar mSnackbar;
    private StockDb db;

    public StoreListCustomAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_dep_no, tv_dep_name;
        private final FloatingActionButton btn_delete;
        private final MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            tv_dep_no = itemView.findViewById(R.id.tv_no);
            tv_dep_name = itemView.findViewById(R.id.tv_name);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            cardView = itemView.findViewById(R.id.card_id);
        }

        public TextView getTv_dep_no() {
            return tv_dep_no;
        }

        public TextView getTv_dep_name() {
            return tv_dep_name;
        }

        public FloatingActionButton getBtn_delete() {
            return btn_delete;
        }

        public MaterialCardView getCardView() {
            return cardView;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_bisc_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;


        String _ID = "No: ";
        _ID += String.valueOf(mCursor.getLong(mCursor.getColumnIndexOrThrow(StockContracts.StockEntry._ID)));
        String store_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_STORE));
        long _id = mCursor.getLong(mCursor.getColumnIndexOrThrow(StockContracts.StockEntry._ID));


        // Set the store No and store name.
        holder.getTv_dep_no().setText(_ID);
        holder.getTv_dep_name().setText(store_name);

        // When Card  is clicked Open bottom-sheet in order to update group_name.
        holder.getCardView().setOnClickListener(v -> update_bottomSheet(store_name, _id));


        // delete by _ID.
        holder.getBtn_delete().setOnClickListener(v -> confirmDelete(_id, v));


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }



    // Confirm deletion based on user selection.
    private void confirmDelete(long id, View v) {


        if (id > 1) {
            db = new StockDb(mContext);

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
            builder.setTitle(R.string.confirm_title)
                    .setMessage(R.string.confirm_description)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {

                        // Delete the group
                        db.deleteRow(id, StockEntry.INDEX_STORE_TABLE);


                        // Show the Snack-bar to confirm the deletion.
                        mSnackbar = Snackbar.make(v, R.string.deleted, Snackbar.LENGTH_SHORT);
                        mSnackbar.show();

                        // update the cursor.
                        swap_cursor(db.cSelectAllFromTable(StockEntry.INDEX_STORE_TABLE));

                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .show();
        }
        else
            Toast.makeText(mContext, R.string.prevent_deletion, Toast.LENGTH_SHORT).show();
    }

    // Open update bottom sheet.
    private void update_bottomSheet(String oldValue, long _id) {

        // Create new instance of database object.
        db = new StockDb(mContext);



        // Create a new view, which defines the UI of the bottom-sheet components.
        @SuppressLint("InflateParams") View view = ((FragmentActivity)mContext).getLayoutInflater().inflate(R.layout.bottom_sheet_update, null);
        // Show the bottom-sheet.
        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);
        dialog.show();

        // Refers to the components on the bottomSheetLayout.
        EditText newValue = view.findViewById(R.id.edt_bottom_sheet_update);
        FloatingActionButton btn_update = view.findViewById(R.id.btn_update);

        // set the old value and hint into the edit-text.
        newValue.setText(oldValue);
        newValue.setHint(R.string.update_hint);

        // Update when button clicked
        update(db, newValue, btn_update, _id);

    }

    // Call this method whenever button update is clicked.
    @SuppressLint("NotifyDataSetChanged")
    private void update(StockDb db, EditText newValue, FloatingActionButton btn, long _id) {

        btn.setOnClickListener(v -> {

         if (_id > 1)
            // Check if it's not empty and not already exists.
            if (!db.isEmpty(newValue) && !db.isExists(StockEntry.INDEX_STORE_TABLE, (short) 1, getStr(newValue)))
                // Check if updating operation done.
                if (db.updateRow(StockContracts.StockEntry.TABLE_NAMES[5],
                        StockEntry.COLUMN_TABLE_STORE, getStr(newValue), "_id", _id)) {

                    // Show message to confirm the update.
                    Toast.makeText(mContext, R.string.confirm_update, Toast.LENGTH_SHORT).show();

                    // Update the cursor so it update the value on the dynamicList
                    swap_cursor(db.cSelectAllFromTable(StockEntry.INDEX_STORE_TABLE));
                }

         else
                    Toast.makeText(mContext, R.string.prevent_update, Toast.LENGTH_SHORT).show();
        });
    }

    // Return the name with no start or end space(s).
    private String getStr(EditText edt) {
        return edt.getText().toString().trim();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void swap_cursor(Cursor newCursor) {
        if (mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null)
            notifyDataSetChanged();

    }


}
