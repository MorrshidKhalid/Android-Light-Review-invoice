package com.example.navdemo.adapters;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class VendorListCustomAdapter extends RecyclerView.Adapter<VendorListCustomAdapter.ViewHolder> {


    private Context mContext;
    private Cursor mCursor;
    private Snackbar mSnackbar;

    // Constructor
    public VendorListCustomAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_vendor_name,
                tv_vendor_phone,
                tv_vendor_email,
                tv_vendor_company;
        private final MaterialCardView vendor_card;
        private final ImageButton more;
        private final ConstraintLayout inner_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Define click listener for the ViewHolder's View
            tv_vendor_name = itemView.findViewById(R.id.tv_vendor_name);
            tv_vendor_phone = itemView.findViewById(R.id.tv_vendor_phone);
            tv_vendor_email = itemView.findViewById(R.id.tv_vendor_email);
            tv_vendor_company = itemView.findViewById(R.id.tv_vendor_company);
            more = itemView.findViewById(R.id.more);
            vendor_card = itemView.findViewById(R.id.vendor_card);
            inner_layout = itemView.findViewById(R.id.inner_layout);


        }

        public TextView getTv_vendor_name() {
            return tv_vendor_name;
        }
        public TextView getTv_vendor_phone() {
            return tv_vendor_phone;
        }
        public TextView getTv_vendor_email() {
            return tv_vendor_email;
        }
        public TextView getTv_vendor_company() {
            return tv_vendor_company;
        }

        public ImageButton getMore() {
            return more;
        }
        public MaterialCardView getVendor_card() {
            return vendor_card;
        }
        public ConstraintLayout getInner_layout() {
            return inner_layout;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_vendor_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;

        // Load data from the database and set it to it's textView.
        loadData(holder);

        // When the user clicked on the card expand and show more info and components.
        holder.getVendor_card().setOnClickListener(v -> expand(holder.getInner_layout()));


        String vendor_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[0]));
        String vendor_phone = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[1]));
        String vendor_email = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[2]));
        String vendor_company = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[3]));

        long _id = mCursor.getLong(0);

        // Image button more options.
        holder.getMore().setOnClickListener(v -> showMenu(v, R.menu.options, _id, vendor_name, vendor_phone, vendor_email, vendor_company));
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

    // Expand the view to show the more info and components.
    private void expand(ConstraintLayout layout) {

        int v = (layout.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;

        TransitionManager.beginDelayedTransition(layout, new AutoTransition());
        layout.setVisibility(v);
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
    }

    // Set all info to it's textView on the layout
    private void loadData(ViewHolder holder) {
        // Set all vendor info
        String vendor_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[0]));
        holder.getTv_vendor_name().setText(vendor_name);

        String vendor_phone = "Phone : ";
        vendor_phone += mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[1]));
        holder.getTv_vendor_phone().setText(vendor_phone);


        String vendor_email = "Email  : ";
        vendor_email += mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[2]));
        holder.getTv_vendor_email().setText(vendor_email);


        String vendor_company = "Company: ";
        vendor_company += mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_VENDOR[3]));
        holder.getTv_vendor_company().setText(vendor_company);
    }
    // Delete a vendor
    private void confirmDelete(long id, View v) {


        if (id > 1) {
            StockDb db = new StockDb(mContext);

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
            builder.setTitle(R.string.confirm_title)
                    .setMessage(R.string.confirm_v)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {

                        // Delete the brand
                        db.deleteRow(id, (short) 4);

                        // Show the Snack-bar to confirm the deletion.
                        mSnackbar = Snackbar.make(v, R.string.deleted, Snackbar.LENGTH_SHORT);
                        mSnackbar.show();

                        // update the cursor.
                        swap_cursor(db.cSelectAllFromTable((short) 4));

                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .show();
        }
        else
            Toast.makeText(mContext, R.string.prevent_deletion, Toast.LENGTH_SHORT).show();
    }


    // Input components on the bottom sheet layout.
    private  EditText newValue_edt_vendor_name,
                      newValue_edt_vendor_phone,
                      newValue_edt_vendor_email,
                      newValue_edt_vendor_company;
    // Button on the bottom sheet layout to update when user clicked it.
    private FloatingActionButton btn_update_vendor;
    // Open update bottom sheet and set the old values.
    private void update_bottomSheet(String old_name, String old_phone, String old_email, String old_company, long _id) {

        // Create a new view, which defines the UI of the bottom-sheet components.
        @SuppressLint("InflateParams") View view = ((FragmentActivity)mContext).getLayoutInflater().inflate(R.layout.bottom_sheet_vendor_updates, null);
        // Show the bottom-sheet.
        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);
        dialog.show();

        // Refers to the components on the bottomSheetLayout.
       newValue_edt_vendor_name = view.findViewById(R.id.edt_vendor_name);
       newValue_edt_vendor_phone = view.findViewById(R.id.edt_vendor_phone);
       newValue_edt_vendor_email = view.findViewById(R.id.edt_vendor_email);
       newValue_edt_vendor_company = view.findViewById(R.id.edt_vendor_company);
       btn_update_vendor = view.findViewById(R.id.btn_add_update_vendor);


        // set the old value and hint into the edit-text.
        newValue_edt_vendor_name.setText(old_name);
        newValue_edt_vendor_phone.setText(old_phone);
        newValue_edt_vendor_email.setText(old_email);
        newValue_edt_vendor_company.setText(old_company);

        //  Update the vendor info that are in the database.
        update(_id);
    }

    // Call this method whenever button update is clicked.
    @SuppressLint("NotifyDataSetChanged")
    private void update(long _id) {

        // Create new instance of database object.
        StockDb stockDb = new StockDb(mContext);

        btn_update_vendor.setOnClickListener(v -> {

            if (_id > 1) {
                if (!stockDb.isEmpty(newValue_edt_vendor_name))
                    if (stockDb.updateRow(StockEntry.TABLE_NAMES[4],
                            getStr(newValue_edt_vendor_name),
                            getStr(newValue_edt_vendor_phone),
                            getStr(newValue_edt_vendor_email),
                            getStr(newValue_edt_vendor_company), _id)) {
                        // Show message to confirm the update.
                        Toast.makeText(mContext, R.string.confirm_update_info, Toast.LENGTH_SHORT).show();

                        // Update the cursor so it update the value on the dynamicList
                        swap_cursor(stockDb.cSelectAllFromTable((short) 4));
                    }
            }
            else
                Toast.makeText(mContext, R.string.prevent_update, Toast.LENGTH_SHORT).show();
        });

    }

    private void showMenu(View v, @MenuRes int menuRes, long id, String name, String phone, String email, String company) {

        PopupMenu popup = new PopupMenu(mContext, v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        // Handel option click.
        popup.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_edit)
                update_bottomSheet(name, phone, email, company, id);

            else if (item.getItemId() == R.id.action_delete)
                confirmDelete(id, v);

            return true;
        });

        popup.show();
    }

    // Return the name with no start or end space(s).
    private String getStr(EditText edt) {
        return edt.getText().toString().trim();
    }
}
