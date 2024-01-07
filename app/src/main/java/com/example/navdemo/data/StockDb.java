package com.example.navdemo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navdemo.R;
import com.example.navdemo.adapters.BrandListCustomAdapter;
import com.example.navdemo.adapters.GroupListCustomAdapter;
import com.example.navdemo.adapters.ProductListCustomAdapter;
import com.example.navdemo.adapters.ReceivingListCustomAdapter;
import com.example.navdemo.data.StockContracts.StockEntry;
import com.example.navdemo.objects.CommonEntities;
import com.example.navdemo.objects.ProductObject;
import com.example.navdemo.objects.ReceiptOrderCard;
import com.example.navdemo.objects.ReceiptedProducts;
import com.example.navdemo.objects.Vendor;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class StockDb {


    // Constant for the Database Name.
    private final String DATABASE_NAME = "SCS.db";

    // Constant for the Database Version.
    private final int DATABASE_VERSION = 1;


    public static class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_GROUP);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_PRODUCT);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_BRAND);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_DEPARTMENT);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_VENDOR);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_STORE);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_RECEIVING);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_RECEIVING_PRODUCT);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_TEMP);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_UOM);
            sqLiteDatabase.execSQL(StockEntry.CREATE_TABLE_COLOR);


            // Insert all data related to UOM table only once, when it created.
            fill_table(sqLiteDatabase, StockEntry.TABLE_NAMES[11], StockEntry.COLUMN_TABLE_UOM, StockEntry.UOM_ROW_DATA);
            // Insert all data related to COLOR table only once, when it created.
            fill_table(sqLiteDatabase, StockEntry.TABLE_NAMES[12], StockEntry.COLUMN_TABLE_COLOR, StockEntry.COLORS_ROW_DATA);
            // Insert all data related to Department table only once, when it created.
            fill_table_first_value(sqLiteDatabase, StockEntry.TABLE_NAMES[3], StockEntry.COLUMN_TABLE_DEPARTMENT, StockEntry.UNKNOWING_DEPARTMENT);
            // Insert all data related to Store table only once, when it created.
            fill_table_first_value(sqLiteDatabase, StockEntry.TABLE_NAMES[5], StockEntry.COLUMN_TABLE_STORE, StockEntry.UNKNOWING_STORE);
            // Insert all data related to Vendor table only once, when it created.
            fill_table_first_value(sqLiteDatabase, StockEntry.TABLE_NAMES[4], StockEntry.COLUMNS_TABLE_VENDOR[0], StockEntry.UNKNOWING_VENDOR);

        }


        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }


        // Fill the table with data.
        private void fill_table(SQLiteDatabase db, String table_name, String clm, String[] data) {

            ContentValues cv = new ContentValues();
            for(String str : data ) {
                cv.put(clm, str);
                // insert data to the database.
                db.insert(table_name, null, cv);
            }

        }

        // Fill the a table with only one data (like first item in spinner).
        private void fill_table_first_value(SQLiteDatabase db, String table_name, String clm, String value) {

            ContentValues cv = new ContentValues();
                cv.put(clm, value);
                // insert data to the database.
                db.insert(table_name, null, cv);

        }


    }



    private Context mContext;
    private OpenHelper mOpenHelper;
    private SQLiteDatabase mSqLiteDatabase;


    public StockDb() {
        // Default constructor require nothing.
    }

    //  Constructor require context.
    public StockDb(Context context) {
        this.mContext = context;
    }

    // Open writeable database
    private void openWDatabase() {

        mOpenHelper = new OpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mSqLiteDatabase = mOpenHelper.getWritableDatabase();
    }

    // Open only readable database
    private void openRDatabase() {

        mOpenHelper = new OpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mSqLiteDatabase = mOpenHelper.getReadableDatabase();
    }



    // Check the input from the user is not empty.
    public boolean isEmpty(EditText edt) {

        if (edt.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, R.string.fill_msg, Toast.LENGTH_SHORT).show();
            edt.setBackgroundResource(R.drawable.shape_red_border);
            return true;
        }
        return false;
    }

    // Check the PRODUCT spinner is not empty.
    public boolean isEmpty(Spinner sp) {

        if (cSelectAllFromTable(StockEntry.INDEX_PRODUCT_TABLE).getCount() == 0) {
            sp.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_red_border));
            Toast.makeText(mContext, R.string.product_msg, Toast.LENGTH_SHORT).show();
            return true;

        }
        return false;
    }

    /**
     * Check if you add a new item when there are no group or brand on the spinners.
     * @param spinner Spinner of group
     * @param spinner2 Spinner of brand
     * @param edt Input of name.
     * @return If all steps are okay.
     */
    public boolean isEmpty(Spinner spinner, Spinner spinner2, EditText edt) {

        if (cSelectAllFromTable((short) 0).getCount() == 0) {
            spinner.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_red_border));
            Toast.makeText(mContext, R.string.group_msg, Toast.LENGTH_SHORT).show();
            return true;

        } else if (cSelectAllFromTable((short) 2).getCount() == 0) {
            spinner2.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_red_border));
            Toast.makeText(mContext, R.string.brand_msg, Toast.LENGTH_SHORT).show();
            return true;

        } else if (isEmpty(edt)) {
            edt.setBackgroundResource(R.drawable.shape_red_border);
            Toast.makeText(mContext, R.string.fill_msg, Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    /**
     * Check if the input is already exists.
     * @param tblPos The table index.
     * @param colPos The column number
     * @param str The data to compare.
     * @return if it's exists return true otherwise false.
     */
    public boolean isExists(short tblPos, short colPos, String str) {

        Cursor cursor = cSelectAllFromTable(tblPos);

        // Compare each row value if it's the same as (str).
            while (cursor.moveToNext()) {
                if (Objects.equals(cursor.getString(colPos), str)) {
                    Toast.makeText(mContext, R.string.exists, Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        cursor.close();

        return false;
    }

    /**
     * Check if the input is already exists both name and it's FOREIGN KEY.
     * @param tblPos The table index.
     * @param colPos1 The column1 number
     * @param colPos2 The column2 number
     * @param str The data to compare.
     * @param fk The FOREING KEY to compare.
     * @return if it's exists return true otherwise false.
     */
    public boolean isExists(short tblPos, short colPos1, short colPos2, String str, int fk) {

        Cursor cursor = cSelectAllFromTable(tblPos);

        // Compare each row value if it's the same as (str) and (fk).
            while (cursor.moveToNext()) {
                if (Objects.equals(cursor.getString(colPos1), str) && Objects.equals(cursor.getInt(colPos2), fk)) {
                Toast.makeText(mContext, R.string.exists, Toast.LENGTH_SHORT).show();
                return true;
                }
            }
        cursor.close();


        return false;
    }




    // add one group to the database.
    public boolean addOneGroup(CommonEntities commonEntities) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMN_TABLE_GROUP, commonEntities.get_mName());

        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[0], null, cv);

        return insert != -1;
    }

    // add one brand to the database.
    public boolean addOneBrand(CommonEntities commonEntities) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMNS_TABLE_BRAND[0], commonEntities.get_mName());
        cv.put(StockEntry.COLUMNS_TABLE_BRAND[1], commonEntities.get_mForeingKey());

        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[2], null, cv);

        return insert != -1;
    }
    // add one product to the database.
    public boolean addOneProduct(ProductObject product) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[0], product.getP_Group_code());
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[1], product.getP_Brand_code());
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[2], product.getProduct_name());
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[3], product.getColor());
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[4], product.getSuk());
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[5], product.getQuantity());
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[6], product.getP_UOM());
        cv.put(StockEntry.COLUMNS_TABLE_PRODUCT[7], product.getNote());

        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[1], null, cv);

        return insert != -1;
    }
    // add one vendor to the database.
    public boolean addOneVendor(Vendor vendor) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMNS_TABLE_VENDOR[0], vendor.getName());
        cv.put(StockEntry.COLUMNS_TABLE_VENDOR[1], vendor.getPhone());
        cv.put(StockEntry.COLUMNS_TABLE_VENDOR[2], vendor.getEmail());
        cv.put(StockEntry.COLUMNS_TABLE_VENDOR[3], vendor.getCompany());


        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[4], null, cv);

        return insert != -1;
    }
    // add one department to the database.
    public boolean addOneDepartment(CommonEntities commonEntities) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMN_TABLE_DEPARTMENT, commonEntities.get_mName());

        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[3], null, cv);

        return insert != -1;
    }
    // add one store to the database.
    public boolean addOneStore(CommonEntities commonEntities) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMN_TABLE_STORE, commonEntities.get_mName());

        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[StockEntry.INDEX_STORE_TABLE], null, cv);

        return insert != -1;
    }
    // add one receiving order to the database.
    public boolean addOneReceiving(ReceiptOrderCard orderCard) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING[0], orderCard.get_mDepartment_FK());
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING[1], orderCard.get_mVendor_FK());
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING[2], orderCard.get_mStore_FK());
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING[3], orderCard.getReceiving_Date());
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING[4], orderCard.get_mDescription());

        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[StockEntry.INDEX_RECEIVING_TABLE], null, cv);

        return insert != -1;
    }
    // add one receiving products to the database.
    public boolean addOneReceivingProduct(ReceiptedProducts products) {

        // Open writeable database and get the data that are coming.
        openWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[0], products.getReceiving_FK());
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[1], products.getProduct_FK());
        cv.put(StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[2], products.getQuantity());


        // insert data to group table.
        long insert = mSqLiteDatabase.insert(StockEntry.TABLE_NAMES[StockEntry.INDEX_RECEIVING_PRODUCT_TABLE], null, cv);

        return insert != -1;
    }


    /**
     * Get all the data inside the table.
     * @param tableIndex The table index.
     * @return The data inside the table
     */
    public Cursor cSelectAllFromTable(short tableIndex) {
        // Open database read only.
        openRDatabase();
        return mSqLiteDatabase.rawQuery("SELECT * FROM " + StockEntry.TABLE_NAMES[tableIndex]
                + " ORDER BY  _id " + "DESC", null);

    }


    /**
     * Get all the data inside the table with condition.
     * @param tableIndex The table index.
     * @param whereClause The column you want the data from.
     * @param id The condition.
     * @return The data inside the table based on the id.
     */
    public Cursor cSelectAllFromTable(short tableIndex, String whereClause, int id) {


        openRDatabase();
        return mSqLiteDatabase.rawQuery("SELECT * FROM " + StockEntry.TABLE_NAMES[tableIndex]
                + " WHERE " + whereClause +" = " + id
                + " ORDER BY  _id " + "DESC", null);
    }


    /**
     *  Get the last Primary-Key of a specific table
     * @param tableName The table you want to get the it's last Primary-Key.
     * @param idColumnName The column you want the max value from.
     * @return the last Primary-Key.
     */
    public int getLastId(String tableName, String idColumnName) {
        openRDatabase();
        String query = "SELECT MAX(" + idColumnName + ") FROM " + tableName;
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);
        int lastId = -1;
        if (cursor.moveToFirst()) {
            lastId = cursor.getInt(0);
        }
        cursor.close();
        return lastId;
    }


    /**
     * Delete one row from a table base PRIMARY KEY.
     * @param id the primary-key.
     * @param index the table index.
     */
    public void deleteRow(long id, short index) {

        openWDatabase();
        mSqLiteDatabase.delete(StockEntry.TABLE_NAMES[index],
                "_id = ?",
                new String[]{String.valueOf(id)});

        // Update the cursor
        new GroupListCustomAdapter().swap_cursor(cSelectAllFromTable(StockEntry.INDEX_GROUP_TABLE));
        new BrandListCustomAdapter().swap_cursor(cSelectAllFromTable(StockEntry.INDEX_BRAND_TABLE));
        new ProductListCustomAdapter().swap_cursor(cSelectAllFromTable(StockEntry.INDEX_PRODUCT_TABLE));
        new ReceivingListCustomAdapter().swap_cursor(cSelectAllFromTable(StockEntry.INDEX_RECEIVING_TABLE));
    }


    /**
     * Delete row base on a specific condition that is the column.
     * @param id The FOREING KEY of the main table.
     * @param colm The base column name you want to delete (whereClause).
     * @param index The table index you will delete from.
     */
    public void deleteRow(long id, String colm, short index) {

        openWDatabase();
        mSqLiteDatabase.delete(StockEntry.TABLE_NAMES[index],
                colm + "=" + "?",
                new String[]{String.valueOf(id)});

        // Update the cursor
        new GroupListCustomAdapter().swap_cursor(cSelectAllFromTable(StockEntry.INDEX_GROUP_TABLE));
        new BrandListCustomAdapter().swap_cursor(cSelectAllFromTable(StockEntry.INDEX_BRAND_TABLE));
        new ReceivingListCustomAdapter().swap_cursor(cSelectAllFromTable(StockEntry.INDEX_RECEIVING_TABLE));
    }


    /**
     * Update one row from a table where new value should be a String.
     * @param tableName The table name.
     * @param columnName The column wants to update.
     * @param newValue The new value (value you want to update).
     * @param idColumnName Value of the column.
     * @param id The primary-key.
     * @return If updated successfully return true otherwise false.
     */
    public boolean updateRow(String tableName, String columnName, String newValue, String idColumnName, long id) {

        ContentValues values = new ContentValues();
        values.put(columnName, newValue);


        String whereClause = idColumnName + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        // Open database to write.
        openWDatabase();
        // Update the row.
        int rowsUpdated = mSqLiteDatabase.update(tableName, values, whereClause, whereArgs);
        mSqLiteDatabase.close();

        return rowsUpdated > 0;
    }


    /**
     * Update one row from a table where new value should be an Integer.
     * @param tableName The table name.
     * @param columnName The column wants to update.
     * @param newValue The new value (value you want to update).
     * @param idColumnName Value of the column.
     * @param id The primary-key.
     * @return If updated successfully return true otherwise false.
     */
    public boolean updateRow(String tableName, String columnName, int newValue, String idColumnName, long id) {

        ContentValues values = new ContentValues();
        values.put(columnName, newValue);


        String whereClause = idColumnName + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        // Open database to write.
        openWDatabase();
        // Update the row.
        int rowsUpdated = mSqLiteDatabase.update(tableName, values, whereClause, whereArgs);
        mSqLiteDatabase.close();

        return rowsUpdated > 0;
    }


    /**
     * Update row from Vendor Table
     * @param tableName Table name.
     * @param newName The new updated value.
     * @param newPhone The new updated value.
     * @param newEmail The new updated value.
     * @param newCompany The new updated value.
     * @param id The primary-key.
     * @return If updated successfully return true otherwise false.
     */
    public boolean updateRow(String tableName, String newName, String newPhone, String newEmail, String newCompany, long id) {

        ContentValues values = new ContentValues();
        values.put(StockEntry.COLUMNS_TABLE_VENDOR[0], newName);
        values.put(StockEntry.COLUMNS_TABLE_VENDOR[1], newPhone);
        values.put(StockEntry.COLUMNS_TABLE_VENDOR[2], newEmail);
        values.put(StockEntry.COLUMNS_TABLE_VENDOR[3], newCompany);


        String whereClause = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        // Open database to write.
        openWDatabase();
        // Update the row.
        int rowsUpdated = mSqLiteDatabase.update(tableName, values, whereClause, whereArgs);
        mSqLiteDatabase.close();

        return rowsUpdated > 0;
    }


    /**
     *  Retrieve data from multiple(2) tables.
     * @param tbl_main The table that you want retrieve data to.
     * @param tbl_name2 The table that you want retrieve data from.
     * @param clm The column in which the FOREING KEY is placed.
     * @return The data from the tables.
     */
    public Cursor retrieve(String tbl_main, String tbl_name2, String clm) {
        // Open database read only.
        openRDatabase();
        return mSqLiteDatabase.rawQuery("SELECT * FROM "
                + tbl_main + " tbl1 "
                + " JOIN " + tbl_name2 + " tbl2 "
                + "ON tbl1." + clm
                + " = tbl2._id"
                + " ORDER BY  _id " + "DESC",null);
    }

    /**
     * Retrieve data from multiple tables which is [ProductObject, Group, Brand, Color].
     * @return The data from the tables.
     */
    public Cursor p_retrieve() {
        // Open database read only.
        openRDatabase();
        return mSqLiteDatabase.rawQuery("SELECT PRODUCT.*, brand_name, product_name, group_name, uom_name, color_name"
                + " FROM PRODUCT"
                + " LEFT JOIN PRODUCT_GROUP ON PRODUCT.p_group_code = PRODUCT_GROUP._id"
                + " LEFT JOIN BRAND ON PRODUCT.p_brand_code = BRAND._id"
                + " LEFT JOIN UOM ON PRODUCT.p_uom = UOM._id"
                + " LEFT JOIN COLOR ON PRODUCT.p_color_code = COLOR._id"
                + " ORDER BY  _id " + "DESC",null);
    }


    /**
     * Retrieve data from multiple tables which is [Department, Vendor ,Store].
     * @return The data from the tables.
     */
    public Cursor r_retrieve() {
        // Open database read only.
        openRDatabase();
        return mSqLiteDatabase.rawQuery("SELECT * FROM "
                + StockEntry.TABLE_NAMES[6] + " tbl_receiving "
                + "LEFT JOIN " + StockEntry.TABLE_NAMES[4] + " tbl_vendor "
                + "ON tbl_receiving." + "r_vendor_id"
                + " = tbl_vendor._id"
                + " JOIN " + StockEntry.TABLE_NAMES[3] + " tbl_department "
                + "ON tbl_receiving." + "r_department_id"
                + " = tbl_department._id"
                + " JOIN " + StockEntry.TABLE_NAMES[5] + " tbl_store "
                + "ON tbl_receiving." + "r_store_id"
                + " = tbl_store._id"
                + " ORDER BY  _id " + "DESC",null);
    }


    /**
     * Retrieve data from multiple tables which is [Receiving_Product, ProductObject, Brand, UOM, Color].
     * @param id the row id you want to retrieve.
     * @return retrieved data.
     */
    public Cursor rp_retrieve(long id) {
        // Open database read only.
        openRDatabase();
        return mSqLiteDatabase.rawQuery("SELECT product_name, uom_name, color_name, brand_name,receiving_product_Quantity"
                + " FROM RECEIVING_PRODUCT"
                + " LEFT JOIN PRODUCT ON RECEIVING_PRODUCT.rp_product_id = PRODUCT._id"
                + " LEFT JOIN UOM ON PRODUCT.p_uom = UOM._id"
                + " LEFT JOIN COLOR ON PRODUCT.p_color_code = COLOR._id"
                + " LEFT JOIN BRAND ON PRODUCT.p_brand_code = BRAND._id"
                + " WHERE RECEIVING_PRODUCT.rp_receiving_id "+ "=" + id
                ,null);
    }


    /**
     * Retrieve data from multiple tables which is [ProductObject, Product_group, Brand, UOM, Color].
     * @param id the row id you want to retrieve.
     * @return retrieved data.
     */
    public Cursor dp_retrieve(long id) {
        // Open database read only.
        openRDatabase();
        return mSqLiteDatabase.rawQuery("SELECT product_name, uom_name, color_name, brand_name, group_name"
                        + " FROM PRODUCT"
                        + " LEFT JOIN UOM ON PRODUCT.p_uom = UOM._id"
                        + " LEFT JOIN COLOR ON PRODUCT.p_color_code = COLOR._id"
                        + " LEFT JOIN BRAND ON PRODUCT.p_brand_code = BRAND._id"
                        + " LEFT JOIN PRODUCT_GROUP ON PRODUCT.p_group_code = PRODUCT_GROUP._id"
                        + " WHERE PRODUCT._id "+ "=" + id
                ,null);
    }
}