package com.example.navdemo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navdemo.R;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.product.ProductDialog;
import com.example.navdemo.product.UpdateProduct;
import com.example.navdemo.product.OneProduct;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class ProductListCustomAdapter extends RecyclerView.Adapter<ProductListCustomAdapter.ViewHolder> {


    private Context mContext;
    private Cursor mCursor;
    private StockDb db;
    private FragmentManager fragmentManager;
    private ImageView img;
    public ProductListCustomAdapter() {
        // Require empty constructor.
    }


    // Constructor get the context and cursor that contain data also FragmentManager.
    public ProductListCustomAdapter(Context mContext, Cursor mCursor, FragmentManager fragmentManager, ImageView img) {
        this.mContext = mContext;
        this.mCursor = mCursor;
        this.fragmentManager = fragmentManager;
        this.img = img;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_product_name,
                tv_quantity,
                tv_note;


        private final ImageButton more;
        private final MaterialCardView pCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_quantity = itemView.findViewById(R.id.content_quantity);
            more = itemView.findViewById(R.id.more);
            pCard = itemView.findViewById(R.id.pCard);

        }
        public TextView getTv_product_name() {
            return tv_product_name;
        }
        public TextView getTv_note() {
            return tv_note;
        }
        public TextView getTv_quantity() {
            return tv_quantity;
        }
        public ImageButton getMore() {
            return more;
        }
        public MaterialCardView getP_card() {
            return pCard;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_product_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;

        // Database instance.
        db = new StockDb(mContext);


        String product_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_GROUP));
        product_name += " [" +  mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_BRAND[0]));
        product_name += " ]: " +  mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_PRODUCT[2]));
        holder.getTv_product_name().setText(product_name);


        String note = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_PRODUCT[7]));
        holder.getTv_note().setText(note);


        int qty = mCursor.getInt(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_PRODUCT[5]));
        holder.getTv_quantity().setText(String.valueOf(qty));

        String suk = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_PRODUCT[4]));
        String color = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_COLOR));

        // get the current row id, and product name.
        long rowId = mCursor.getLong(mCursor.getColumnIndexOrThrow(StockEntry._ID));

        // Move to details page when row clicked.
        OneProduct product = new OneProduct(product_name, color, suk, note, qty);
        holder.getP_card().setOnClickListener(v -> openProductDetail(product));

        // Show and handel more options.
        holder.getMore().setOnClickListener(v -> showMenu(v, R.menu.options, rowId));
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

    // Confirm deletion based on user selection.
    private void confirmDelete(long id, View v) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
        builder.setTitle(R.string.confirm_title)
                .setMessage(R.string.confirm_p)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, which) -> {

                    // Show the Snack-bar to confirm the deletion.
                    if (isRowDeleted(id, db)) {
                        Snackbar snackbar = Snackbar.make(v, R.string.deleted, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    else
                        Toast.makeText(mContext, R.string.product_deletion, Toast.LENGTH_SHORT).show();

                    // Update the cursor.
                    swap_cursor(db.p_retrieve());
                    // Display the empty image
                    if (getItemCount() <= 0)
                        img.setVisibility(View.VISIBLE);
                    else img.setVisibility(View.GONE);

                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .show();
    }


    // Open update bottom sheet.
    private void openUpdateDialog(long _id) {
        // Display fullscreen dialog.
        new UpdateProduct().display(fragmentManager, _id);
    }


    // Open product details dialog
    private void openProductDetail(OneProduct oneProduct) {
        // Display fullscreen dialog.
        new ProductDialog().display(fragmentManager, oneProduct);
    }

    // Delete a row.
    private boolean isRowDeleted(long id, StockDb db) {

        // Check if any related info to the product that to be delete.
        Cursor cursor = db.cSelectAllFromTable(StockEntry.INDEX_RECEIVING_PRODUCT_TABLE);
        if (cursor.moveToFirst()) {
            do {
                if (id == cursor.getInt(cursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[1]))) {
                    cursor.close();
                    return false;
                }

            }while (cursor.moveToNext());
        }

        // Delete the product
        db.deleteRow(id, StockEntry.INDEX_PRODUCT_TABLE);

        // update the cursor.
        swap_cursor(db.p_retrieve());

        return true;
    }

    private void showMenu(View v, @MenuRes int menuRes, long id) {

        PopupMenu popup = new PopupMenu(mContext, v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        // Handel option click.
        popup.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_edit)
                openUpdateDialog(id);

            else if (item.getItemId() == R.id.action_delete)
                confirmDelete(id, v);

            return true;
        });

        popup.show();
    }
}