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

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navdemo.R;
import com.example.navdemo.receiving.InvoiceDialog;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.objects.InvoiceHeader;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;


public class ReceivingListCustomAdapter extends RecyclerView.Adapter<ReceivingListCustomAdapter.ViewHolder> {


    private Context mContext;
    private Cursor mCursor;
    private StockDb db;
    private FragmentManager fragmentManager;
    private ImageView img;


    // Constructor.
    public ReceivingListCustomAdapter(Context mContext, Cursor mCursor, FragmentManager fragmentManager, ImageView img) {
        this.mContext = mContext;
        this.mCursor = mCursor;
        this.fragmentManager = fragmentManager;
        this.img = img;
    }

    public ReceivingListCustomAdapter() {
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {


        // Refers to the views on the layout that display and control content.
        private final TextView date, no, ven_name, department;
        private final ImageButton more;
        private final MaterialCardView r_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View
            date = itemView.findViewById(R.id.date);
            no = itemView.findViewById(R.id.no);
            ven_name = itemView.findViewById(R.id.tv_ven_name);
            department = itemView.findViewById(R.id.tv_dep);
            more = itemView.findViewById(R.id.more);
            r_card = itemView.findViewById(R.id.r_card);
        }

        public TextView getDate() {
            return date;
        }

        public TextView getNo() {
            return no;
        }
        public TextView getVen_name() {
            return ven_name;
        }

        public TextView getDepartment() {
            return department;
        }
        public ImageButton getMore() {
            return more;
        }
        public MaterialCardView getR_card() {
            return r_card;
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_receiving, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;

        // Instance of database.
        db = new StockDb(mContext);

        // Primary-key
        long id = mCursor.getLong(0);


        // Set data to components in the layout.
        String no = "INV";
        no += String.valueOf(id);
        holder.getNo().setText(no);

        String date = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_RECEIVING[3]));
        holder.getDate().setText(date);


        String ven_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[0]));
        holder.getVen_name().setText(ven_name);


        String dep = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_DEPARTMENT));
        holder.getDepartment().setText(dep);


        String store = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_STORE));

        String des = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_RECEIVING[4]));

        // Create new object that contain all invoice info in order to send it to details dialog.
        InvoiceHeader invoiceHeader = new InvoiceHeader(date, no, ven_name, dep, store, des);

        // Show and handel more options.
        holder.getMore().setOnClickListener(v -> showMenu(v, R.menu.delete, id));

        // Move to detailed page.
        holder.getR_card().setOnClickListener(v -> openInvoiceDialog(id, invoiceHeader));

    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    // Delete one invoice
    private void delete(View v, long id) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
        builder.setTitle(R.string.confirm_title)
                .setMessage(R.string.confirm_r)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialog, which) -> {


                    // Delete all related info to the invoice.
                    deleteRelatedInfo(id);

                    // Delete the invoice (Receipted card).
                    db.deleteRow(id, StockEntry.INDEX_RECEIVING_TABLE);

                    // Show the Snack-bar to confirm the deletion.
                    Snackbar snackbar = Snackbar.make(v, R.string.deletedInvoice, Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    // update the cursor.
                    swap_cursor(db.r_retrieve());
                    // Display the empty image
                    if (img != null) {
                        if (getItemCount() <= 0)
                            img.setVisibility(View.VISIBLE);
                        else img.setVisibility(View.GONE);
                    }

                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .show();


    }


    // Delete all info that are related to the invoice.
    private void deleteRelatedInfo(long id) {

        Cursor rCursor = db.cSelectAllFromTable(StockEntry.INDEX_RECEIVING_PRODUCT_TABLE, "rp_receiving_id", (int)id);

        int invQuantity, oldQty;
        if (rCursor.moveToFirst()) {

            do {

                /*
                   Send the product key that exists on receipted product table in order to get all of it's quantity,
                   Get the old product quantity and subtract the deleted invoice
                   quantity of the stock.
                   Then update the stock quantity of the product.
                 */
                invQuantity = rCursor.getInt(3);
                oldQty = getOldQuantity(rCursor.getInt(2));
                db.updateRow(StockEntry.TABLE_NAMES[1], StockEntry.COLUMNS_TABLE_PRODUCT[5], (oldQty - invQuantity),  "_id", rCursor.getInt(2));

                // Delete the row that has the same receiving id.
                db.deleteRow(id, "rp_receiving_id", StockEntry.INDEX_RECEIVING_PRODUCT_TABLE);

            } while (rCursor.moveToNext());
        }

    }

    private void showMenu(View v, @MenuRes int menuRes, long id) {

        PopupMenu popup = new PopupMenu(mContext, v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        // Handel option click.
        popup.setOnMenuItemClickListener(item -> {

                delete(v, id);

            return true;
        });

        popup.show();
    }

    private void openInvoiceDialog(long inv_no, InvoiceHeader invoiceHeader) {

        new InvoiceDialog().display(fragmentManager, inv_no, invoiceHeader);
    }

    private int getOldQuantity(int id) {

        Cursor cursor = db.cSelectAllFromTable(StockEntry.INDEX_PRODUCT_TABLE, StockEntry._ID, id);

        if (cursor.moveToFirst())
            return cursor.getInt(6);

        return 0;
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