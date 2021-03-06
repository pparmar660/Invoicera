package com.invoicera.GlobalData;

public class Constant {
    public static enum WALK_THROUGH_SPINNERS {
        TIMEZONE, COMPANY_LIST, DATE_FORMAT_LIST, CURRENCY_LIST, FINANCIAL_YEAR
    }

    ;


    public static enum POP_UP {
        ADDITIONAL_CHARGES, TAX, PAYMENT_TERM, LATE_FEE, PRODUCT_LIST, CREDIT_CARD_TYPE, SERVICE_LIST, TAX_LIST1, TAX_LIST2,
        COUNTRY, CURRENCY, PAYMENT_GATEWAY, CATEGORY_LIST, STAFF_LIST, PROJECT_LIST, CLIENT_LIST
    }

    public static enum CREATE_MODULE {
        INVOICE, CANCEL, ESTIMATE
    }

    ;

    public static enum LIST_ACTION_ANIMATION_TYPE {

        OPEN, CLOSE


    }


    public static enum FRAGMENT_RESULT {
        CLIENT
    }

    public static enum INFORMATION_ROW_TYPE {
        HEADER, INFORMATION
    }

    ;
    public static final String userCurrencyDecimal = "2";
    public static final String KEY_INTERFACE = "interface";
    public static final String KEY_FROM = "from";
    public static final String KEY_TO = "to";
    public static final String KEY_DATE_FROM = "date_from";
    public static final String KEY_CLIENT_CURRENCY = "client_currency";
    public static final String KEY_PAYMENT_GATEWAY = "payment_gateway";
    public static final String KEY_AMOUNT_TO = "to";
    public static final String KEY_AMOUNT_FROM = "amount_from";

    public static String token = "";
    public static final String KEY_DATE_TO = "date_to";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_OCCURRENCE = "occurrence";
    public static final String KEY_CONTACT_NAME = "companyName";
    public static final String KEY_COMPANY_SIZE = "company_size";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHONE = "phone_no";

    public static final String KEY_CountryCode = "CountryCode";
    public static final String KEY_CountryName = "CountryName";
    public static final String KEY_NAME = "name";
    public static final String KEY_PREFIX = "prefix";
    public static final String KEY_NEXT_NUMBER = "next_number";
    public static final String KEY_TIME = "time";
    public static final String KEY_CURRENCY_ = "Currency";
    public static final String KEY_EMAIL = "email";
    public static final String[] KEY_EMAIL2 = {"email", "RAN"};

    public static final String KEY_CREDIT = "credit";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_LOGIN_URL = "loginURL";
    public static final String KEY_MAILING_LIST = "mailingList";
    public static final String KEY_USER_NAME = "username";
    public static final String WALKTHROUGH = "getWalkThrough";
    public static final String KEY_METHOD = "method";
    public static final String KEY_ITEM = "item";
    public static final String KEY_DATA = "data";

    public static final String KEY_TOKEN = "token";
    public static final String KEY_INVOICE_STATUS = "invoice_status";
    public static final String KEY_RECURRING_STATUS = "recurring_status";
    public static final String KEY_EXPENSE_STATUS = "expense_status";
    public static final String KEY_AMOUNT_STATUS = "amount_status";
    public static final String KEY_ESTIMATE_STATUS = "estimate_status";
    public static final String KEY_PAGE_NO = "page";
    public static final String KEY_PAGE_PER_REORD = "per_page_record";


    //--------------------
    public static final String KEY_ESTIMATE = "estimate";
    public static final String KEY_ESTIMATES = "estimates";
    public static final String KEY_INVOICES = "invoices";
    public static final String KEY_INVOICE = "invoice";
    public static final String KEY_REQUEST = "request";
    public static final String KEY_NO = "number";
    public static final String KEY_INVOICE_NO = "invoice_number";
    public static final String KEY_RECURRING_NO = "recurring_number";
    public static final String KEY_EXPENSE_NO = "expense_number";
    public static final String KEY_ESTIMATE_NO = "estimate_number";
    public static final String KEY_TermsAndCondition = "termsCondition";
    public static final String KEY_LateFee = "lateFee";
    public static final String KEY_LateFeeId = "lateFeeId";
    public static final String KEY_EDITING = "editing";

    public static final String KEY_SCHEDUAL_DATE = "schedual_date";
    public static final String KEY_NOTE = "note";
    public static final String KEY_TITLE = "title";
    public static final String KEY_INVOICE_TITLE = "invoice_title";
    public static final String KEY_ESTIMATE_TITLE = "estimate_title";
    public static final String KEY_CLIENT = "client";
    public static final String KEY_ORGANIZATION = "organization";
    public static final String KEY_ID = "id";
    public static final String KEY_VALUE = "value";
    public static final String KEY_TYPE = "type";
    public static final String KEY_DATE = "date";
    public static final String KEY_DAY = "day";

    public static final String KEY_USER_LOGED = "user_loged";


    public static final String KEY_RECURRING_ID = "recurring_id";


    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_COMPANY_ID = "company_id";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_NET_BALANCE = "net_balance";
    public static final String KEY_OUTSTANDING = "Outstanding";
    public static final String KEY_LATE_FEE = "late_fee";
    public static final String KEY_LATE_FEE_NAME = "late_fee_name";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_TERMS = "terms_conditions";
    public static final String KEY_CODE = "code";
    public static final String KEY_ACTIVE = "Active";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_UNITCOST = "unitcost";
    public static final String KEY_ARCHIVE = "Archived";
    public static final String KEY_DELETE = "Deleted";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_INVOICE_ID = "invoice_id";
    public static final String KEY_ESTIMATE_ID = "estimate_id";
    public static final String KEY_EXPENSE_ID = "expense_id";
    public static final String KEY_invoice_type = "invoice_type";
    public static final String KEY_CLIENT_ID = "client_id";
    public static final Object KEY_COUNTRY = null;
    public static final String KEY_CURRENCY_SYMBOL = "currency";
    public static final String KEY_LATE_FEE_ID = "late_fee_id";
    public static final String KEY_TOTAL_DISCOUNT = "total_discount";
    public static final String KEY_TOTAL_TAX = "total_tax";
    public static final String KEY_ITEMS = "items";
    public static final String KEY_ADDITIONAL_CHARGES = "additional_charges";
    public static final String KEY_CHARGE = "charge";
    public static final String KEY_CLIENT_NAME = "client_name";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_OUTSTANDINF_AMOUNT = "outstanding";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_INVOICE_PO_NO = "po_number";
    public static final String KEY_DUE_DATE = "invoice_due_date";
    public static final String KEY_SCH_DATE = "invoice_schedule_date";
    public static final String KEY_NET_BALENCE = "net_balance";
    public static final String KEY_TOTAL_PAID_AMOUNT = "total_paid";
    public static final String KEY_PREVIEW_STATUS = "invoice_status";
    public static final String KEY_INVOICE_NOTIFICATION = "invoice_notification";
    public static final String KEY_NOTIFICATION_MAIL_TO = "mail_to";
    public static final String KEY_NOTIFICATION_SUBJECT = "subject";
    public static final String KEY_NOTIFICATION_CONTENT = "content";
    public static final String KEY_COUNTRY_ID = "country_id";
    public static final String KEY_TIMEZONE_ID = "";

    public static final String KEY_CURRENCY_ID = "country_id";
    public static final String KEY_COUNTRY_NAME = "country_name";
    public static final String KEY_COMPANY_TYPE = "company_type";
    public static final String KEY_YEAR = "month_value";
    public static final String KEY_TIME_ZONE = "timezone";
    public static final String KEY_TIME_ZONE_SEND = "time_zone";
    public static final String KEY_DATE_VALUE = "date_value";
    public static final String KEY_DATE_TITLE = "date_title";
    public static final String KEY_CURRENCY_NAME = "currency_name";


    public static final String KEY_POSITION = "position";
    public static final String KEY_SENT = "Sent";
    public static final String KEY_DRAFT = "Draft";
    public static final String KEY_PAID = "Paid";
    public static final String KEY_PARTIAL_PAID = "Partial-Paid";
    public static final String KEY_INVOICED = "Invoiced";
    public static final String KEY_UNBILLED = "Unbilled";
    public static final String KEY_EXPIRED = "Expired";
    public static final String KEY_ACCEPTED = "Accepted";
    public static final String KEY_REJECTED = "Rejected";
    public static final String KEY_CREATE = "create";
    public static final String KEY_EDIT = "edit";


    public static final String KEY_PRODUCT_TAX_VALUE1 = "product_tax_value1";
    public static final String KEY_PRODUCT_TAX_VALUE2 = "product_tax_value2";
    public static final String KEY_PRODUCT_CURRENCY_ID = "product_currency_id";
    public static final String KEY_PRODUCT_CURRENCY_NAME = "product_currency_name";
    public static final String KEY_PRODUCT_TAX_ONE_NAME = "product_tax_one_name";
    public static final String KEY_PRODUCT_TAX_TWO_NAME = "product_tax_two_name";

    public static final String KEY_PRODUCT_TAXID1 = "product_taxID1";

    public static final String KEY_PRODUCT_TAXID2 = "product_taxID2";


    public static final String KEY_STATUS = "status";
    public static final String KEY_SERVICE_TAX_VALUE1 = "service_tax_value1";
    public static final String KEY_SERVICE_TAX_VALUE2 = "service_tax_value2";
    public static final String KEY_SERVICE_CURRENCY_ID = "service_currency_id";
    public static final String KEY_SERVICE_CURRENCY_NAME = "service_currency_name";
    public static final String KEY_SERVICE_TAX_ONE_NAME = "service_tax_one_name";
    public static final String KEY_SERVICE_TAX_TWO_NAME = "service_tax_two_name";
    public static final String KEY_INVENTORY_CURRENT_STOCK = "inventory_current_stock";
    public static final String KEY_TRACK_INVENTORY = "track_inventory";
    public static final String KEY_SERVICE_TAXID1 = "service_taxID1";

    public static final String KEY_SERVICE_TAXID2 = "product_taxID2";
    public static final String KEY_CREATED_BY = "created_by";

    public static final String POSITION_NUMBER = "position_number";
    public static final String FRAGMENT_POS = "fragment_pos";
    public static final String NO_CONNECTION_MESSAGE = "No network connection";


    public static final int TIMEOUT_MILLISEC = 10 * 1000;


    public static int defultLengthOfText = 12;

    public static enum DrwerListSection {

        DRAWER_HEADER, DRAWER_SECTION, DRAWER_ITEM
    }


    public static enum SERVICE_TYPE {
        STAFF_LIST, CATEGORY_LIST, SIGN_IN, CREATE_ESTIMATE_OFFLINE,
        CREATE_RECURRING_OFFLINE, LOGIN, GET_LIST, GET_PAYMENT_LIST,
        START_RECURRING, STOP_RECURRING, GET_CREDIT_CARD_TYPE, GET_UPPER_DATA,
        SEND_ESTIMATE_MAIL, GET_BOTTOM_DATA, GET_DATA, SEND_DATA, GET_SEARCH_DATA,
        GET_FILTER_DATA, GET_SETTING, GET_ADDITIONAL_CHARGE,
        GET_PRODUCT_LIST, GET_SERVICE_LIST, GET_TAX_LIST1, GET_TAX_LIST2, GET_TAX_LIST,
        CREATE, OFF_LINE_PAYMENT, PAYMENT_HISTORY, DELETE_OFF_LINE_PAYMENT, DELETE, ACTIVE,
        ARCHIVE, EXPORT_PDF, SEND, GET_COUNTRY_LIST, ACCOUNT_STATEMENT, SEND_LOGIN_INFO, GET_PROJECT_LIST, GET_CLIENT_LIST,GET_EXPENSE,FORGET_PASSWORE
    }

// error message--------------

    public static final String ErrorMessage_UserName = "Please enter user name";
    public static final String ErrorMessage_password = "Password should be greater than 6 characters";
    public static final String ErrorMessage_match = "Password does not match";
    public static final String ErrorMessage_company = "Please enter company name";
    public static final String ErrorMessage_email = "Please enter email";
    public static final String ErrorMessage_contact = "Please enter contact name";
    public static final String ErrorMessage_host = "Please enter host name";
    public static final String ErrorMessage_invoiceNumber = "Please enter invoice number";
    public static final String ErrorMessage_recurringNumber = "Please enter recurring number";
    public static final String offlinepayment_created = "Payment details have been successfully updated.";
    public static final String no_op = "No options(s) available.";
    public static final String No_REcord_Found = "No records found.";
    public static final String title = "INVOICERA";
    public static final String DeleteHistory = "Are you sure you want to delete this payment?";
    public static final String delete_successfully = "Successfully deleted.";
    public static String request_not_done = "Connection to server fail.";
    public static String network_error = "Network Not Found";
    public static String subdoamin_error = "Please enter subdomain name.";
    public static String email_error_match = "Please enter valid email id.";
    public static String email_error = "Please enter email id.";
   // public static final String delete_message = "Are you sure you want to delete selected ?";
    //------------------------------
    // start for activity result request Code

    public static final int requestCodeCreateInvoiceItem = 1;
    public static final int requestCodeInvoiceFilter = 2;
    public static final int requestCodeCreateInvoiceDetail = 3;
    public static final int requestCodeSelectPaymentGateway = 4;

    public static final int requestCodeSelectClient = 5;
    public static final int requestCodeOffLinePaymentFromPreview = 6;
    public static final int requestCodeOffLinePaymentFromList = 7;
    public static final int requestCodeEstimateFilter = 8;
    public static final int requestCodeCreateEstimateItem = 9;
    public static final int requestCodeCreateEstimateDetail = 10;
    public static final int requestCodeItemFilter = 11;
    public static final int requestCodeExpenseFilter = 13;
    public static final int requestCodeClientPreview = 14;
    public static final int requestCodeAddCredit = 15;
    public static final int requestCodeClientCreateEdit = 16;

    public static final int requestCodeRecurringFilter = 17;
    public static final int requestCodeCreateRecurringItem = 18;
    public static final int requestCodeCreateRecurringDetail = 19;
    public static final int requestCodeAutoBill = 20;
    public static final int requestCodeAutoBillEdit = 21;


    // ----------------------- URL

    // public static final String Main_url = "https://www.invoicera.com/app/";
    public static final String Main_url = "https://www.invoicera.com/testbeta/";

    // public static final String Main_url = "http://10.0.2.62/invoicera_live/";

    public static final String loginURL = Main_url + "api/auth/?";

    public  static  final  String SignUpUrl="https://www.invoicera.com/testbeta/sign_up.php?cGxhbklEPTEw";

    public static final String forgotPasswordURL = Main_url
            + "api/json/2.0/index.php?";
    public static final String invoicelistURL = Main_url
            + "api/json/2.0/index.php?";

    public static final String ImageURL = Main_url
            + "common/orange_theme/images/american_express_logo.gif";

    public static String back_again = "Please click BACK again to exit.";


    //-----------------------Fragment tag--------------------------------------

    public static final String HomeFragmentTag = "homeFragment";
    public static final String InvoiceListFragmentTag = "invoiceListFragment";
    public static final String InvoicePreviewCreateFragmentTag = "invoicePreviewCreateFragment";
    public static final String EstimateListFragmentTag = "estimateListFragment";
    public static final String EstimatePreviewCreateFragmentTag = "estimatePreviewCreateFragment";
    public static final String CreateProductServicesFragmentTag = "createProductServicesFragmentTag";
    public static final String CreateExpenseFragmentTag = "createExpenseFragmentTag";
    public static final String RecurringPreviewCreateFragmentTag = "recurringPreviewCreateFragment";
    public static final String RecurringListTag = "recurringListFragment";


}