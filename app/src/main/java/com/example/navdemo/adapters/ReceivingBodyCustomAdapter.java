package com.example.navdemo.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navdemo.R;
import com.example.navdemo.data.StockContracts.StockEntry;

public class ReceivingBodyCustomAdapter extends RecyclerView.Adapter<ReceivingBodyCustomAdapter.ViewHolder> {



    private Cursor mCursor;

    // Constructor
    public ReceivingBodyCustomAdapter(Cursor mCursor) {
        this.mCursor = mCursor;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView product,
        uom,
        color,
        body_quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            product = itemView.findViewById(R.id.body_product);
            uom = itemView.findViewById(R.id.body_uom);
            color = itemView.findViewById(R.id.body_color);
            body_quantity = itemView.findViewById(R.id.body_quantity);

        }


        public TextView getProduct() {
            return product;
        }
        public TextView getUom() {
            return uom;
        }

        public TextView getColor() {
            return color;
        }

        public TextView getBody_quantity() {
            return body_quantity;
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_receiving_body, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;

        String prod_name = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_BRAND[0]));
        prod_name += " " + mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_PRODUCT[2]));
        holder.getProduct().setText(prod_name);



        String uom = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_UOM));
        holder.getUom().setText(uom);


        String color = mCursor.getString(mCursor.getColumnIndexOrThrow(StockEntry.COLUMN_TABLE_COLOR));
        holder.getColor().setText(color);

        String qua = String.valueOf(mCursor.getInt(mCursor.getColumnIndexOrThrow(StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[2])));
        holder.getBody_quantity().setText(qua);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


}
