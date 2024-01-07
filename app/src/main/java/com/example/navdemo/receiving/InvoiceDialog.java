package com.example.navdemo.receiving;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navdemo.R;
import com.example.navdemo.adapters.ReceivingBodyCustomAdapter;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.objects.InvoiceHeader;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class InvoiceDialog extends DialogFragment {


    private static long id;
    private TextView inv_no, inv_date, ven_name, dep, des, sto;
    private MaterialToolbar toolbar;
    private RecyclerView rv_invoice_body;
    private static InvoiceHeader invoiceHeader;

    public void display(FragmentManager fragmentManager, long _id, InvoiceHeader _invoiceHeader) {
        InvoiceDialog invoiceDialog = new InvoiceDialog();
        invoiceDialog.show(fragmentManager, getTag());
        id = _id;
        invoiceHeader = _invoiceHeader;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fullscreen_invoice_dialog, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Find Components.
        toolbar = view.findViewById(R.id.toolbar);
        rv_invoice_body = view.findViewById(R.id.rv_invoice_body);
        inv_no = view.findViewById(R.id.inv_no);
        inv_date = view.findViewById(R.id.inv_date);
        ven_name = view.findViewById(R.id.ven_name);
        dep = view.findViewById(R.id.dep);
        des = view.findViewById(R.id.des);
        sto = view.findViewById(R.id.store);

        handelBar();
        loadHeadData();
        updateInvoiceList();
    }



    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(getDialog().getWindow()).setLayout(width, height);
        }
    }

    private void handelBar() {

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(invoiceHeader.getData());
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white));
    }

    private void loadHeadData() {
        inv_no.setText(invoiceHeader.getInv_no());
        inv_date.setText(invoiceHeader.getData());
        ven_name.setText(invoiceHeader.getVen());
        dep.setText(invoiceHeader.getDep());
        des.setText(invoiceHeader.getDes());
        sto.setText(invoiceHeader.getSto());
    }
    private void updateInvoiceList() {

        rv_invoice_body.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_invoice_body.setAdapter(new ReceivingBodyCustomAdapter(new StockDb(getContext()).rp_retrieve(id)));
    }

}
