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
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class BrandListCustomAdapter extends RecyclerView.Adapter<BrandListCustomAdapter.ViewHolder> {


    private Context mContext;
    private Cursor mCursor;
    private Snackbar mSnackbar;


    // Default constructor receives nothing.
    public BrandListCustomAdapter() {
        // Require empty constructor.
    }

    // Constructor get the context and cursor that contain data.
    public BrandListCustomAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_group_name, tv_brand_name;
        private final FloatingActionButton btn_delete;
        private final MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            tv_group_name = itemView.findViewById(R.id.tv_no);
            tv_brand_name = itemView.findViewById(R.id.tv_name);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            cardView = itemView.findViewById(R.id.card_id);
        }

        public TextView getTv_group_name() {
            return tv_group_name;
        }
        public TextView getTv_brand_name() {
            return tv_brand_name;
        }
        public FloatingActionButton getBtn_delete() {
            return btn_delete;
        }
        public MaterialCardView getCardView() {
            return cardView;
        }
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_bisc_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;


        String brand_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_BRAND[0]));
        String group_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_GROUP));
        long _id = mCursor.getLong(0);


        // Set the group No and Group name.
        holder.getTv_brand_name().setText(brand_name);
        holder.getTv_group_name().setText(group_name);

        // Delete a brand.
        holder.getBtn_delete().setOnClickListener(v -> confirmDelete(_id, v));

        // Update
        holder.getCardView().setOnClickListener(v -> update_bottomSheet(brand_name, _id));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void swap_cursor(Cursor newCursor) {
        if (mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null)
            notifyDataSetChanged();

    }


    // Call this method whenever button update is clicked.
    @SuppressLint("NotifyDataSetChanged")
    private void update(StockDb stockDb, EditText newValue, FloatingActionButton btn, long _id) {

        btn.setOnClickListener(v -> {

            // Check if it's not empty and not already exists.
            if (!stockDb.isEmpty(newValue) && !stockDb.isExists((short)2, (short) 1, getBrandName(newValue)))
                // Check if updating operation done.
                if (stockDb.updateRow(StockEntry.TABLE_NAMES[2],
                        StockEntry.COLUMNS_TABLE_BRAND[0], getBrandName(newValue), "_id", _id)) {

                    // Show message to confirm the update.
                    Toast.makeText(mContext, R.string.confirm_update, Toast.LENGTH_SHORT).show();

                    // Update the cursor so it update the value on the dynamicList
                    swap_cursor(stockDb.retrieve(
                            StockEntry.TABLE_NAMES[2],
                            StockEntry.TABLE_NAMES[0],
                            StockEntry.COLUMNS_TABLE_BRAND[1])
                    );

                }
        });
    }

    // Open update bottom sheet.
    private void update_bottomSheet(String oldValue, long _id) {

        // Create new instance of database object.
        StockDb stockDb = new StockDb(mContext);


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
        update(stockDb, newValue, btn_update, _id);

    }

    // Confirm deletion based on user selection.
    private void confirmDelete(long id, View v) {


        StockDb stockDb = new StockDb(mContext);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
        builder.setTitle(R.string.confirm_title)
                .setMessage(R.string.confirm_description)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, which) -> {

                    // Delete the brand
                    stockDb.deleteRow(id, (short) 2);

                    // Show the Snack-bar to confirm the deletion.
                    mSnackbar = Snackbar.make(v, R.string.deleted, Snackbar.LENGTH_SHORT);
                    mSnackbar.show();

                    // update the cursor.
                    swap_cursor(stockDb.retrieve(
                            StockEntry.TABLE_NAMES[2],
                            StockEntry.TABLE_NAMES[0],
                            StockEntry.COLUMNS_TABLE_BRAND[1]
                    ));

                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .show();
    }

    // Return the name with no start or end space(s).
    private String getBrandName(EditText edt) {
        return edt.getText().toString().trim();
    }

}
