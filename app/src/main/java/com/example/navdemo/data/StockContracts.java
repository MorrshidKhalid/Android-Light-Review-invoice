package com.example.navdemo.data;

import android.provider.BaseColumns;

public class StockContracts {

    // make the constructor private,
    // To prevent someone from accidentally instantiating the contract class
    private StockContracts() {
    }



    // Inner class that defines the tables and it's columns.
    public static class StockEntry implements BaseColumns {

        public static final String[] TABLE_NAMES = {
                "PRODUCT_GROUP",    // --> idx 0
                "PRODUCT",          // --> idx 1
                "BRAND",            // --> idx 2
                "DEPARTMENT",       // --> idx 3
                "VENDOR",           // --> idx 4
                "STORE",            // --> idx 5
                "RECEIVING",        // --> idx 6
                "RECEIVING_PRODUCT",// --> idx 7
                "ISSUING",          // --> idx 8
                "ISSUING_PRODUCT",  // --> idx 9
                "TEMP",             // --> idx 10
                "UOM",              // --> idx 11
                "COLOR"             // --> idx 12
        };


        public static final String COLUMN_TABLE_GROUP = "group_name" ;


        public static final String[] COLUMNS_TABLE_BRAND = {
                "brand_name",
                "b_group_code" };

        public static final String[] COLUMNS_TABLE_PRODUCT = {
                "p_group_code",
                "p_brand_code",
                "product_name",
                "p_color_code",
                "suk",
                "quantity",
                "p_uom",
                "note" };


        public static final String COLUMN_TABLE_DEPARTMENT = "department_name" ;


        public static final String[] COLUMNS_TABLE_VENDOR = {
                "vendor_name",
                "vendor_phone",
                "vendor_email",
                "company_name" };


        public static final String COLUMN_TABLE_STORE = "store_name" ;


        public static final String[] COLUMNS_TABLE_RECEIVING = {
                "r_department_id",
                "r_vendor_id",
                "r_store_id",
                "date",
                "description" };

        public static final String[] COLUMNS_TABLE_RECEIVING_PRODUCT = {
                "rp_receiving_id",
                "rp_product_id",
                "receiving_product_Quantity"
        };


        public static final String[] COLUMN_TABLE_TEMP = {
                "product_FK",
                "product_quantity",
        };

        public static final String COLUMN_TABLE_UOM = "uom_name";
        public static final String COLUMN_TABLE_COLOR = "color_name";

        public static final String[] UOM_ROW_DATA = {
                "BAG",
                "BKT",
                "BND",
                "BOWL",
                "BX",
                "CRD",
                "CM",
                "CS",
                "CTN",
                "DZ",
                "EA",
                "FT",
                "GAL",
                "GROSS",
                "IN",
                "KIT",
                "LOT",
                "M",
                "MM",
                "PCs",
                "PK",
                "PK100",
                "PK50",
                "PR",
                "RACK",
                "RL",
                "SET",
                "SET3",
                "SET4",
                "SET5",
                "SGL",
                "SHT",
                "SQFT",
                "TUBE",
                "YD"
        };

        public static final String[] COLORS_ROW_DATA = {
                "Red",
                "Purple",
                "Blue",
                "Green",
                "Yellow",
                "Orange",
                "Brown",
                "Black",
                "White",
                "Custom color",
                "Silver",
                "Colorless"
        };


        public static final String UNKNOWING_DEPARTMENT = "Unknowing department";
        public static final String UNKNOWING_VENDOR = "Unknowing vendor";
        public static final String UNKNOWING_STORE = "Unknowing store";

        // Integers matches the index of tables.
        public static final short INDEX_GROUP_TABLE = 0;
        public static final short INDEX_PRODUCT_TABLE = 1;
        public static final short INDEX_BRAND_TABLE = 2;
        public static final short INDEX_DEPARTMENT_TABLE = 3;
        public static final short INDEX_STORE_TABLE = 5;
        public static final short INDEX_VENDOR_TABLE = 4;
        public static final short INDEX_RECEIVING_TABLE = 6;
        public static final short INDEX_RECEIVING_PRODUCT_TABLE = 7;
        public static final short INDEX_UOM_TABLE = 11;
        public static final short INDEX_COLOR_TABLE = 12;


        /**
         * All queries for creating tables in the database.
         */

        // Query that creates the Group Table.
        public static final String CREATE_TABLE_GROUP = "CREATE TABLE " + StockEntry.TABLE_NAMES[0] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMN_TABLE_GROUP + " TEXT)";




        // Query that creates the ProductObject Table.
        public static final String CREATE_TABLE_PRODUCT = "CREATE TABLE " + StockEntry.TABLE_NAMES[1] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMNS_TABLE_PRODUCT[0] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_PRODUCT[1] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_PRODUCT[2] + " TEXT," +
                StockEntry.COLUMNS_TABLE_PRODUCT[3] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_PRODUCT[4] + " TEXT," +
                StockEntry.COLUMNS_TABLE_PRODUCT[5] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_PRODUCT[6] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_PRODUCT[7] + " TEXT," +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_PRODUCT[0]+") REFERENCES "+StockEntry.TABLE_NAMES[0]+"("+StockEntry._ID+")," +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_PRODUCT[1]+") REFERENCES "+StockEntry.TABLE_NAMES[2]+"("+StockEntry._ID+"), " +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_PRODUCT[3]+") REFERENCES "+StockEntry.TABLE_NAMES[12]+"("+StockEntry._ID+"), " +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_PRODUCT[6]+") REFERENCES "+StockEntry.TABLE_NAMES[11]+"("+StockEntry._ID+"));";


        // Query that creates the Brand Table.
        public static final String CREATE_TABLE_BRAND = "CREATE TABLE " + StockEntry.TABLE_NAMES[2] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMNS_TABLE_BRAND[0] + " TEXT," +
                StockEntry.COLUMNS_TABLE_BRAND[1] + " INTEGER," +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_BRAND[1]+") REFERENCES "+StockEntry.TABLE_NAMES[0]+"("+StockEntry._ID+"));";


        // Query that creates the Department Table.
        public static final String CREATE_TABLE_DEPARTMENT = "CREATE TABLE " + StockEntry.TABLE_NAMES[3] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMN_TABLE_DEPARTMENT + " TEXT)";


        // Query that creates the Department Table.

        public static final String CREATE_TABLE_VENDOR = "CREATE TABLE " + StockEntry.TABLE_NAMES[4] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMNS_TABLE_VENDOR[0] + " TEXT," +
                StockEntry.COLUMNS_TABLE_VENDOR[1] + " TEXT," +
                StockEntry.COLUMNS_TABLE_VENDOR[2] + " TEXT," +
                StockEntry.COLUMNS_TABLE_VENDOR[3] + " TEXT)";


        // Query that creates the Store Table.
        public static final String CREATE_TABLE_STORE = "CREATE TABLE " + StockEntry.TABLE_NAMES[5] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMN_TABLE_STORE + " TEXT)";



        // Query that creates the ReceiptOrderCard Table.
        public static final String CREATE_TABLE_RECEIVING = "CREATE TABLE " + StockEntry.TABLE_NAMES[6] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMNS_TABLE_RECEIVING[0] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_RECEIVING[1] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_RECEIVING[2] + " INTEGER," +
                StockEntry.COLUMNS_TABLE_RECEIVING[3] + " TEXT," +   // <-- Set it as date <--
                StockEntry.COLUMNS_TABLE_RECEIVING[4] + " TEXT," +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_RECEIVING[0]+") REFERENCES "+StockEntry.TABLE_NAMES[3]+"("+StockEntry._ID+")" +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_RECEIVING[1]+") REFERENCES "+StockEntry.TABLE_NAMES[4]+"("+StockEntry._ID+")" +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_RECEIVING[2]+") REFERENCES "+StockEntry.TABLE_NAMES[5]+"("+StockEntry._ID+"));";


        // Query that creates the ReceivingItem Table.
        public static final String CREATE_TABLE_RECEIVING_PRODUCT = "CREATE TABLE " + StockEntry.TABLE_NAMES[7] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[0] + " INTEGER ," +
                StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[1] + " INTEGER ," +
                StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[2] + " INTEGER," +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[0]+") REFERENCES "+StockEntry.TABLE_NAMES[6]+"("+StockEntry._ID+")" +
                " FOREIGN KEY ("+StockEntry.COLUMNS_TABLE_RECEIVING_PRODUCT[1]+") REFERENCES "+StockEntry.TABLE_NAMES[1]+"("+StockEntry._ID+"));";



        // Query that creates the Temp Table.
        public static final String CREATE_TABLE_TEMP = "CREATE TABLE " + StockEntry.TABLE_NAMES[10] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMN_TABLE_TEMP[0] + " INTEGER," +
                StockEntry.COLUMN_TABLE_TEMP[1] + " INTEGER)";

        // Query that creates the UOM Table.
        public static final String CREATE_TABLE_UOM = "CREATE TABLE " + StockEntry.TABLE_NAMES[11] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMN_TABLE_UOM + " TEXT)";


        // Query that creates the UOM Table.
        public static final String CREATE_TABLE_COLOR = "CREATE TABLE " + StockEntry.TABLE_NAMES[12] + " (" +
                StockEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StockEntry.COLUMN_TABLE_COLOR + " TEXT)";

    }


}
