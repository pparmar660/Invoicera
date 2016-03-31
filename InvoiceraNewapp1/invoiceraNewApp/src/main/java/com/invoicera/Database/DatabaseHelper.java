package com.invoicera.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TIME_KEY = "time_key";

    private static final String DATABASE_NAME = "invoicera.db";
    private static final int DATABASE_VERSION = 1;
    public static String JSON_DATA = "json_data";
    public static String TYPE = "type";
    public static String ID = "id";
    // list invoice table-------------------------------------------------------
    public static String Table_InvoiceList = "listInvoice";

    public static String Table_EstimateList = "estimateList";

    public static String Table_CategoryList = "categoryList";
    public static String Table_StaffList = "staffList";

    public static String Table_RecurringList = "recurringList";

    public static String Table_CurrencyList = "currencyList";

    public static String Table_ListCountry = "listCountry";

    public static String Table_CreditCardType = "creditCardType";


    public static String Table_ExpenseList = "listExpense";

    public static String Table_ProjectList = "projectList";


    public static String Table_LoginData = "loginData";

    public static String Table_TaxList = "taxList";
    public static String Table_GraphData = "graphData";
    public static String Table_InvoicePreviewData = "invoicePreviewData";
    public static String Table_RecurringPreviewData = "recurringPreviewData";

    public static String Table_EstimatePreviewData = "estimatePreviewData";

    public static String Table_ClientPreview = "clientPreview";

    public static String Table_CreateOfflineInvoice = "createOfflineInvoice";

    public static String Table_CreateOfflineRecurring = "createOfflineRecurring";

    public static String Table_CreateOfflineEstimate = "createOfflineEstimate";


    public static String Table_CreateOfflineExpense = "createOfflineExpese";

    public static String Table_InvoiceCreateSettings = "invoice_create_setting";

    public static String Table_RecurringCreateSettings = "recurring_create_setting";

    public static String Table_EstimateCreateSettings = "estimate_create_setting";

    public static String Table_AdditionalCharges = "invoice_additional_charges";
    public static String Table_LateFee = "invoice_late_fee";

    public static String Table_ProductAndServices = "productAndServices";

    public static String Table_PaymentGateways = "paymentGateways";
    public static String Table_PaymentMethod = "paymentMethod";


    public static String PAGE_NO = "page_no";

    //invoice status

    public static String JSON_INVOICE_RECENT_LIST = "invoice_recent_list";
    public static String JSON_INVOICE_DRAFT_LIST = "invoice_draft_list";
    public static String JSON_INVOICE_SENT_LIST = "invoice_sent_list";
    public static String JSON_INVOICE_PAID_LIST = "invoice_paid_list";
    public static String JSON_INVOICE_OUTSTANDING_LIST = "invoice_outstanding_list";
    public static String JSON_INVOICE_PARTIAL_PAID_LIST = "invoice_partial_paid_list";
    public static String JSON_INVOICE_VIEWED_LIST = "invoice_viewed_list";


    //-------------------------------------------
    // estimate status

    public static String JSON_ESTIMATE_RECENT_LIST = "estimate_recent_list";
    public static String JSON_ESTIMATE_DRAFT_LIST = "estimate_draft_list";
    public static String JSON_ESTIMATE_SENT_LIST = "estimate_sent_list";
    public static String JSON_ESTIMATE_EXPIRED_LIST = "estimate_expired_list";
    public static String JSON_ESTIMATE_ACCEPTED_LIST = "estimate_accepted_list";
    public static String JSON_ESTIMATE_REJECTED_LIST = "estimate_rejected_list";
    public static String JSON_ESTIMATE_VIEWED_LIST = "estimate_viewed_list";


    //expense status
    public static String JSON_EXPENSE_RECENT_LIST = "expense_recent_list";
    public static String JSON_EXPENSE_PAID_LIST = "expense_paid_list";
    public static String JSON_EXPENSE_UNBILLED_LIST = "expense_unbilled_list";
    public static String JSON_EXPENSE_INVOICED_LIST = "expense_invoiced_list";


    //----------------------------------

    public static String TAble_ClientList = "client_list";


    //------------------------Invoice List-------------------------------------------------------
    private static final String CREATE_Table_invoiceList = "CREATE TABLE "
            + Table_InvoiceList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + JSON_INVOICE_RECENT_LIST + " TEXT," + JSON_INVOICE_DRAFT_LIST
            + " TEXT," + JSON_INVOICE_SENT_LIST + " TEXT,"
            + JSON_INVOICE_PAID_LIST + " TEXT," + JSON_INVOICE_OUTSTANDING_LIST
            + " TEXT," + JSON_INVOICE_PARTIAL_PAID_LIST + " TEXT,"
            + JSON_INVOICE_VIEWED_LIST + " TEXT," + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ---------------------------------------------------------------------


    //------------------------Expense List-------------------------------------------------------
    private static final String CREATE_Table_ExpenseList = "CREATE TABLE "
            + Table_ExpenseList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT," + JSON_EXPENSE_RECENT_LIST + " TEXT,"
            + JSON_EXPENSE_PAID_LIST + " TEXT," + JSON_EXPENSE_UNBILLED_LIST
            + " TEXT," + JSON_EXPENSE_INVOICED_LIST + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ---------------------------------------------------------------------


    //------------------------Estimate List-------------------------------------------------------
    private static final String CREATE_Table_estimateList = "CREATE TABLE "
            + Table_EstimateList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + JSON_ESTIMATE_RECENT_LIST + " TEXT," + JSON_ESTIMATE_DRAFT_LIST
            + " TEXT," + JSON_ESTIMATE_SENT_LIST + " TEXT,"
            + JSON_ESTIMATE_EXPIRED_LIST + " TEXT," + JSON_ESTIMATE_ACCEPTED_LIST
            + " TEXT," + JSON_ESTIMATE_REJECTED_LIST + " TEXT,"
            + JSON_ESTIMATE_VIEWED_LIST + " TEXT," + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ---------------------------------------------------------------------


    //______________Client List------------------------------------------------
    private static final String CREATE_Table_ClientList = "CREATE TABLE "
            + TAble_ClientList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //-------------------------------------------

    // ----------------Invoice ,Expanse,Recurring and Estimate's  Graph Data-------------------------------------------
    private static final String CREATE_Table_GraphData = "CREATE TABLE "
            + Table_GraphData + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //------------------------------------------------------------


    // ----------------Invoice preview Data-------------------------------------------
    private static final String CREATE_Table_InvoicePreview = "CREATE TABLE "
            + Table_InvoicePreviewData + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT," + ID + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //--------------------------Recurring preview Data----------------------------------

    private static final String CREATE_Table_RecurringPreview = "CREATE TABLE "
            + Table_RecurringPreviewData + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT," + ID + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";


    // ----------------Client preview Data-------------------------------------------
    private static final String CREATE_Table_ClientPreview = "CREATE TABLE "
            + Table_ClientPreview + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT," + ID + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //------------------------------------------------------------


    // ---------------- Estimate preview Data-------------------------------------------

    private static final String CREATE_Table_EstimatePreview = "CREATE TABLE "
            + Table_EstimatePreviewData + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT," + ID + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //------------------------------------------------------------


    //----------------------- Inovice creating setting-------------------------
    private static final String CREATE_Table_InvoiceCreating = "CREATE TABLE "
            + Table_InvoiceCreateSettings + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // --------------------------------Recurring creating setting----------------------------------
    private static final String CREATE_Table_RecurringCreating = "CREATE TABLE "
            + Table_RecurringCreateSettings + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";


    //----------------------- Additional charges  -------------------------
    private static final String CREATE_Table_AdditionalCharges = "CREATE TABLE "
            + Table_AdditionalCharges + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------

    //----------------------- Late Fee  -------------------------
    private static final String CREATE_Table_LateFee = "CREATE TABLE "
            + Table_LateFee + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------


    //----------------------- product and services Fee  -------------------------
    private static final String CREATE_Table_productAndServices = "CREATE TABLE "
            + Table_ProductAndServices + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------


    //----------------------- tax list  -------------------------
    private static final String CREATE_Table_taxlist = "CREATE TABLE "
            + Table_TaxList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------


    //----------------------- paymentGateWay list  -------------------------
    private static final String CREATE_Table_PaymentGateway = "CREATE TABLE "
            + Table_PaymentGateways + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------


    //----------------------- Invoice create request  -------------------------

    private static final String CREATE_Table_CreateOfflineInvoice = "CREATE TABLE "
            + Table_CreateOfflineInvoice + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TYPE + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";


    //----------------------- Expense create request  -------------------------

    private static final String CREATE_Table_CreateOfflineExpense = "CREATE TABLE "
            + Table_CreateOfflineExpense + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TYPE + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // --------------------------Recurring create request ----------------------------------------

    private static final String CREATE_Table_CreateOfflineRecurring = "CREATE TABLE "
            + Table_CreateOfflineRecurring + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TYPE + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //----------------------- Edtimate create request  -------------------------

    private static final String CREATE_Table_CreateOfflineEstimate = "CREATE TABLE "
            + Table_CreateOfflineEstimate + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TYPE + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------


    //----------------------- Login Data  -------------------------
    private static final String CREATE_Table_LoginData = "CREATE TABLE "
            + Table_LoginData + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------


    //----------------------- Estimate creating setting-------------------------
    private static final String CREATE_Table_EstimateCreating = "CREATE TABLE "
            + Table_EstimateCreateSettings + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------------------------------------------------------------------


    // -----Currency list------------------------------------------

    private static final String CREATE_Table_CurrencyList = "CREATE TABLE "
            + Table_CurrencyList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //-------------------------

    // -----country list------------------------------------------

    private static final String CREATE_Table_CountryList = "CREATE TABLE "
            + Table_ListCountry + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    //-------------------------

    // -----payment method list------------------------------------------

    private static final String CREATE_Table_paymentMethodList = "CREATE TABLE "
            + Table_PaymentMethod + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // -----Recurring  list------------------------------------------

    private static final String CREATE_Table_RecurringList = "CREATE TABLE "
            + Table_RecurringList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";


    // -----Card Type------------------------------------------

    private static final String CREATE_Table_CreditCardType = "CREATE TABLE "
            + Table_CreditCardType + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";

    // ------category list----


    private static final String CREATE_Table_CategoryList = "CREATE TABLE "
            + Table_CategoryList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";


    //-----------------staff list -----


    private static final String CREATE_Table_StaffList = "CREATE TABLE "
            + Table_StaffList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";


    //-----------------project list -----


    private static final String CREATE_Table_ProjectList = "CREATE TABLE "
            + Table_ProjectList + "(" + PAGE_NO
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
            + TIME_KEY
            + " TIMESTAMP DEFAULT (DATETIME('now','localtime')))";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // System.out.println("Look :"+CREATE_Table_invoiceList);
        db.execSQL(CREATE_Table_invoiceList);
        db.execSQL(CREATE_Table_ClientList);
        db.execSQL(CREATE_Table_GraphData);
        db.execSQL(CREATE_Table_InvoicePreview);
        db.execSQL(CREATE_Table_RecurringPreview);
        db.execSQL(CREATE_Table_InvoiceCreating);
        db.execSQL(CREATE_Table_RecurringCreating);
        db.execSQL(CREATE_Table_AdditionalCharges);
        db.execSQL(CREATE_Table_LateFee);
        db.execSQL(CREATE_Table_productAndServices);
        db.execSQL(CREATE_Table_taxlist);
        db.execSQL(CREATE_Table_PaymentGateway);
        db.execSQL(CREATE_Table_CreateOfflineInvoice);
        db.execSQL(CREATE_Table_CreateOfflineRecurring);
        db.execSQL(CREATE_Table_estimateList);
        db.execSQL(CREATE_Table_EstimatePreview);
        db.execSQL(CREATE_Table_LoginData);
        db.execSQL(CREATE_Table_EstimateCreating);
        db.execSQL(CREATE_Table_CreateOfflineEstimate);
        db.execSQL(CREATE_Table_CurrencyList);
        db.execSQL(CREATE_Table_CountryList);
        db.execSQL(CREATE_Table_ClientPreview);
        db.execSQL(CREATE_Table_paymentMethodList);
        db.execSQL(CREATE_Table_RecurringList);
        db.execSQL(CREATE_Table_ExpenseList);
        db.execSQL(CREATE_Table_CreditCardType);
        db.execSQL(CREATE_Table_CategoryList);
        db.execSQL(CREATE_Table_StaffList);
        db.execSQL(CREATE_Table_ProjectList);
        db.execSQL(CREATE_Table_CreateOfflineExpense);
        // db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_invoiceList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_ClientList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_GraphData);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_InvoicePreview);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_RecurringPreview);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_InvoiceCreating);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_AdditionalCharges);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_LateFee);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_productAndServices);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_taxlist);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_PaymentGateway);

        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CreateOfflineInvoice);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CreateOfflineRecurring);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_estimateList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_EstimatePreview);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_LoginData);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_EstimateCreating);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CreateOfflineEstimate);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CurrencyList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CountryList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_ClientPreview);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_paymentMethodList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_RecurringList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_ExpenseList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CreditCardType);

        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CategoryList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_StaffList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_ProjectList);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_Table_CreateOfflineExpense);

        onCreate(db);
        // db.close();

    }

    public void removeAllTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_InvoiceList, null, null);
        db.delete(Table_TaxList, null, null);
        db.delete(Table_GraphData, null, null);
        db.delete(Table_InvoicePreviewData, null, null);
        db.delete(Table_RecurringPreviewData, null, null);
        db.delete(Table_CreateOfflineInvoice, null, null);
        db.delete(Table_InvoiceCreateSettings, null, null);
        db.delete(Table_RecurringCreateSettings, null, null);

        db.delete(Table_AdditionalCharges, null, null);
        db.delete(Table_LateFee, null, null);
        db.delete(Table_ProductAndServices, null, null);
        db.delete(Table_PaymentGateways, null, null);

        db.delete(TAble_ClientList, null, null);
        db.delete(Table_CreateOfflineRecurring, null, null);
        db.delete(Table_EstimateList, null, null);
        db.delete(Table_EstimatePreviewData, null, null);
        db.delete(Table_LoginData, null, null);
        db.delete(Table_EstimateCreateSettings, null, null);
        db.delete(Table_CreateOfflineEstimate, null, null);
        db.delete(Table_CurrencyList, null, null);
        db.delete(Table_ListCountry, null, null);
        db.delete(Table_ClientPreview, null, null);
        db.delete(Table_PaymentMethod, null, null);
        db.delete(Table_RecurringList, null, null);
        db.delete(Table_ExpenseList, null, null);
        db.delete(Table_CreditCardType, null, null);

        db.delete(Table_CategoryList, null, null);
        db.delete(Table_StaffList, null, null);
        db.delete(Table_ProjectList, null, null);

        db.delete(Table_CreateOfflineExpense, null, null);

        db.close();
    }


    public void insert(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            synchronized (db) {
                db.insert(tableName, null, values);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
        }

    }

    public Cursor getRecords(String selectQuery) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;

    }

    public void update(String tableName, ContentValues values, String key_name,
                       String key_value) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(tableName, values, key_name + " = ?",
                new String[]{String.valueOf(key_value)});

        db.close();
    }

    public void clearTableColumn(String tableName, ContentValues values) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(tableName, values, null, null);

    }

    public void ClearTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();

    }

    public int countRow(String selectQuery) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;

    }


}
