package com.example.navdemo.receiving;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.navdemo.R;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.data.StockDb;
import com.example.navdemo.databinding.FragmentAddReceivingBinding;
import com.example.navdemo.bottom_sheets.DepartmentModelBottomSheet;
import com.example.navdemo.bottom_sheets.StoreModelBottomSheet;
import com.example.navdemo.bottom_sheets.VendorModelBottomSheet;
import com.example.navdemo.objects.ReceiptOrderCard;
import com.example.navdemo.objects.ReceiptedProducts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReceivingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReceivingFragment extends Fragment {

    // Direct references to all views that have an ID in the layout
    private FragmentAddReceivingBinding binding;

    // Instance of database object.
    private StockDb db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public AddReceivingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddReceivingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddReceivingFragment newInstance(String param1, String param2) {
        AddReceivingFragment fragment = new AddReceivingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddReceivingBinding.inflate(getLayoutInflater(), container, false);

        // Get the reference to the root layout.
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // Instance of database object.
        db = new StockDb(getContext());

        // Navigate to other fragment
        navigate();


        // All methods to Open bottom sheet.
        openDepartmentBottomSheet();
        openStoreBottomSheet();
        openVendorBottomSheet();

        // All methods load data to spinners.
        loadSpinnerData(binding.receiptOrderCard.spDepartment, db.cSelectAllFromTable(StockEntry.INDEX_DEPARTMENT_TABLE), StockEntry.COLUMN_TABLE_DEPARTMENT);
        loadSpinnerData(binding.receiptOrderCard.spStore, db.cSelectAllFromTable(StockEntry.INDEX_STORE_TABLE), StockEntry.COLUMN_TABLE_STORE);
        loadSpinnerData(binding.receiptOrderCard.spVendor, db.cSelectAllFromTable(StockEntry.INDEX_VENDOR_TABLE) , StockEntry.COLUMNS_TABLE_VENDOR[0]);


        // Save a receipted products to database.
        binding.btnSaveInvoice.setOnClickListener(v -> {

            // Make sure that the invoice is not empty.
            if (newInvoice()) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
                // Show temporary feedback to the user.
                Toast.makeText(getContext(), R.string.confirm, Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getContext(), R.string.entReceipt, Toast.LENGTH_SHORT).show();

            });

        // Add new dynamicView in order to add a product to the invoice.
        binding.receiptProductList.btnAdd.setOnClickListener(v -> addAndRmView());
    }


    // Add and remove dynamicView from the list.
    private void addAndRmView() {

        @SuppressLint("InflateParams") View dynamicView = getLayoutInflater().inflate(R.layout.row_dynamic_list, null, false);

        // Find components on the layout.
        TextView vColor = dynamicView.findViewById(R.id.vColor);
        TextView vUom = dynamicView.findViewById(R.id.vUom);
        TextView vGroup = dynamicView.findViewById(R.id.vGroup);
        TextView vBrand = dynamicView.findViewById(R.id.vBrand);
        Spinner spProduct = dynamicView.findViewById(R.id.spProduct);

        // Load all products
        loadSpinnerData(spProduct, db.cSelectAllFromTable(StockEntry.INDEX_PRODUCT_TABLE),StockEntry.COLUMNS_TABLE_PRODUCT[2]);

        // Update the selected product details in order to display it's info.
        displayInfo(spProduct, vColor, vUom, vGroup, vBrand);

        // Add the dynamicView to the list.
        binding.receiptProductList.listView.addView(dynamicView);


        // Remove the  dynamicView of the list when X button clicked.
        FloatingActionButton rmView = dynamicView.findViewById(R.id.rmView);
        rmView.setOnClickListener(v -> removeView(dynamicView));
    }

    // Remove the dynamicView of the list
    private void removeView(View v) {
        binding.receiptProductList.listView.removeView(v);
    }


    // Get all the necessary information for the receipt card.
    private void setInvoiceCard() {

        ReceiptOrderCard orderCard = new ReceiptOrderCard(
                binding.receiptOrderCard.spDepartment.getSelectedItemId(),
                binding.receiptOrderCard.spVendor.getSelectedItemId(),
                binding.receiptOrderCard.spStore.getSelectedItemId(),
                palaceDate(),
                binding.receiptOrderCard.edtReceivingDescription.getText().toString()
        );

        db.addOneReceiving(orderCard);
    }


    // Save the user entries to database.
    private boolean newInvoice() {

        // The parent view.
        View listViewChildAt;

        // Views on the layout.
        EditText qty;
        Spinner spProduct;

        // Make sure the invoice has no empty body.
        if (binding.receiptProductList.listView.getChildCount() <= 0)
            return false;

        // Make sure non of the quantity are empty or zero.
        for (int i = 0; i < binding.receiptProductList.listView.getChildCount(); i++) {

            listViewChildAt = binding.receiptProductList.listView.getChildAt(i);
            qty = listViewChildAt.findViewById(R.id.qty);
            spProduct = listViewChildAt.findViewById(R.id.spProduct);

            if (db.isEmpty(qty) || qty.getText().toString().equals("0") || db.isEmpty(spProduct))
                return false;
        }


        // Create the invoice header card.
        setInvoiceCard();


        // Refers to the last invoice of receiving and old product quantity.
        final int INV_NO = db.getLastId(StockEntry.TABLE_NAMES[6], StockEntry._ID);

        for (int i = 0; i < binding.receiptProductList.listView.getChildCount(); i++) {

            listViewChildAt = binding.receiptProductList.listView.getChildAt(i);

            qty = listViewChildAt.findViewById(R.id.qty);
            spProduct = listViewChildAt.findViewById(R.id.spProduct);

            int productNo = (int)spProduct.getSelectedItemId();
            // Add to database and update the quantity of a product.
            db.addOneReceivingProduct(new ReceiptedProducts(INV_NO, productNo, Integer.parseInt(qty.getText().toString())));
            int newQty = getOldQuantity(productNo) + Integer.parseInt(qty.getText().toString());
            db.updateRow(StockEntry.TABLE_NAMES[1], StockEntry.COLUMNS_TABLE_PRODUCT[5], newQty, StockEntry._ID, productNo);
        }

        return true;
    }


    // Get the old quantity that exists on the stock of the product from the database.
    private int getOldQuantity(int id) {

        db = new StockDb(getContext());
        Cursor cursor = db.cSelectAllFromTable(StockEntry.INDEX_PRODUCT_TABLE, StockEntry._ID, id);

        if (cursor.moveToFirst())
            return cursor.getInt(6);

        return 0;
    }

    /**
     * Retrieve data from database base on the product id and column
     * @param id ProductObject Primary-key.
     * @param clm Column name of the data that wanted to retrieve.
     * @return Data from database.
     */
    private String showProductDetails(long id, String clm) {

        Cursor cursor = db.dp_retrieve(id);
        String value = "Empty";
        if (cursor.moveToFirst())
            value = cursor.getString(cursor.getColumnIndexOrThrow(clm));

        cursor.close();
        return value;
    }

    // Display product info on the view.
    private void displayInfo(Spinner sp, TextView color, TextView uom, TextView group, TextView brand) {
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color.setText(showProductDetails(id, StockEntry.COLUMN_TABLE_COLOR));
                uom.setText(showProductDetails(id, StockEntry.COLUMN_TABLE_UOM ));
                group.setText(showProductDetails(id, StockEntry.COLUMN_TABLE_GROUP));
                brand.setText(showProductDetails(id, StockEntry.COLUMNS_TABLE_BRAND[0]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    // Get the current date and time.
    private String palaceDate() {

        String systemDate = "";
        DateTimeFormatter dtf;
        LocalDateTime now;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            now = LocalDateTime.now();
            systemDate = dtf.format(now);
        }


        return systemDate;
    }


    // All methods used to open bottom sheet.
    private void openDepartmentBottomSheet() {

        binding.receiptOrderCard.btnAddDepartment.setOnClickListener(v ->
                new DepartmentModelBottomSheet(binding.receiptOrderCard.spDepartment, null, null)
                        .show(getChildFragmentManager(), getTag())
        );
    }
    private void openStoreBottomSheet() {

        binding.receiptOrderCard.btnAddStore.setOnClickListener(v ->
                new StoreModelBottomSheet(binding.receiptOrderCard.spStore, null, null)
                        .show(getChildFragmentManager(), getTag())
        );
    }
    private void openVendorBottomSheet() {

        binding.receiptOrderCard.btnAddVendor.setOnClickListener(v ->
                new VendorModelBottomSheet(binding.receiptOrderCard.spVendor)
                        .show(getChildFragmentManager(), getTag()));
    }

    // Used to load data to spinners on the layout.
    private void loadSpinnerData(Spinner spinner, Cursor cursor, String clm) {

        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(requireContext(),
                R.layout.spinner,
                cursor,
                new String[]{clm},
                new int[]{R.id.tv_spinner}, 0);

        spinner.setAdapter(adapter);

    }

    // Navigate between fragments
    private void navigate() {

        binding.receiptOrderCard.btnDepartmentSetting.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addReceivingFragment_to_departmentFragment));
        binding.receiptOrderCard.btnStoreSetting.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addReceivingFragment_to_storeFragment));
        binding.receiptOrderCard.btnVendorSetting.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).navigate(R.id.action_addReceivingFragment_to_nav_vendor));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // After outlive the fragment make sure to clean up any references.
        binding = null;
    }

}