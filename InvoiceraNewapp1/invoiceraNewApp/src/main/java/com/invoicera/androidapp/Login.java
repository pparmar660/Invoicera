package com.invoicera.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.BackgroundServices.GetDataFromServerOnLogin;
import com.invoicera.Database.DatabaseHelper;
import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.SERVICE_TYPE;
import com.invoicera.GlobalData.Preferences;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Utility.Validation;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends BaseActivity implements WebApiResult,
        OnClickListener {
    EditText et_mailId, et_psswd;
    TextView sign_up, forget_password;
    boolean mSignInClicked;
    boolean mIntentInProgress, doubleBackToExitPressedOnce = false;
    int RC_SIGN_IN = 0;
    Intent intent, Ravi_avcd;
    static Context ctx;
    SERVICE_TYPE type;
    Preferences pref;
    Button google_plus, loginBtn;
    ImageView imagelogo;
    WebRequest request;
    CheckBox rememberMeCheckBox;
    Boolean isLogin, isRemember;
    LinearLayout linerlayout;

    int i = 0;
    DatabaseHelper db;
    String personName = "N", email = "N", personPhotoUrl = "N",
            newpersonPhotoUrl = "N";

    void sendRequest() {
        request = new WebRequest(this, new JSONObject(), Constant.loginURL,
                type, Constant.token, this, true);
        request.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ctx = Login.this;
        pref = new Preferences(getApplicationContext());
        isRemember = pref.getBoolean("isRemember");
        loginBtn = (Button) findViewById(R.id.Login_button);
        imagelogo = (ImageView) findViewById(R.id.iv_login_logo);
        sign_up = (TextView) findViewById(R.id.sign_up_text);
        et_mailId = (EditText) findViewById(R.id.email);
        et_psswd = (EditText) findViewById(R.id.password);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCheck_box);
        db = new DatabaseHelper(context);
        forget_password = (TextView) findViewById(R.id.forgot_password);
        linerlayout=(LinearLayout)findViewById(R.id.signUpLinear);

        /*

        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, right, bottom);
        yourbutton.setLayoutParams(params);*/

    /*    imagelogo.getHeight()*/
        // Check if remember me is ON/OFF
        if (isRemember == true) {
            et_mailId.setText(pref.getString("mail"));
            et_psswd.setText(pref.getString("password"));

            //  Log.e("my token",pref.getString("MyToken"));
            rememberMeCheckBox.setChecked(true);
        }

        linerlayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.invoicera.com/testbeta/sign_up.php?cGxhbklEPTEw"));
                startActivity(browserIntent);
            }
        });

        sign_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Sign_up.class);
                startActivity(i);
                finish();

            }
        });

        forget_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ForgetPassword.class);

                startActivity(i);

            }
        });

        loginBtn.setOnClickListener(this);

        type = SERVICE_TYPE.LOGIN;

    }

    private void sendData() throws JSONException {
        // TODO Auto-generated method stub

        JSONObject obj = new JSONObject();
        obj.put(Constant.KEY_USER_NAME, et_mailId.getText());
        obj.put(Constant.KEY_PASSWORD, et_psswd.getText());

		/*
         * WebRequest request = new WebRequest(context, obj,
		 * Constant.forgotPasswordURL, type, this); request.execute();
		 */
        WebRequest request = new WebRequest(context, obj, Constant.loginURL, type, Constant.token, this, true);
        request.execute();
    }

	/*
     * private void writeToFile(String data) { try { String path =
	 * context.getExternalFilesDir(null).getAbsolutePath();
	 * System.out.println(path); File file = new File(path + "/my.txt");
	 * FileOutputStream stream = new F/ileOutputStream(file); try {
	 * stream.write(data.getBytes()); } finally { stream.close(); } } catch
	 * (IOException e) { Log.e("Exception", "File write failed: " +
	 * e.toString()); } }
	 */

    @Override
    public void getWebResult(SERVICE_TYPE type, JSONObject result) {
        // TODO Auto-generated method stub

        // String st = result.toString();
        // writeToFile(st);
        if (result == null)
            return;
        try {
            if (!result.getString("code").equalsIgnoreCase("200")) {
                if (result.has("message"))
                    Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_SHORT).show();
                return;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {


            {

                ContentValues values;
                db.ClearTable(DatabaseHelper.Table_LoginData);
                values = new ContentValues();
                values.put(DatabaseHelper.JSON_DATA, result.toString()); // Contact

                db.insert(DatabaseHelper.Table_LoginData, values);
                /*
                String selectQuery = "Select " + DatabaseHelper.JSON_DATA
                        + " From " + DatabaseHelper.Table_LoginData;


                Cursor cursor = db.getRecords(selectQuery);

                if (cursor.moveToFirst() && cursor.getCount() > 0)
                    for (int i = 0; i < cursor.getCount(); i++) {
                        updateUI(new JSONObject(cursor.getString(cursor
                                .getColumnIndex(parentFragment.table_field[position]))));
                    }*/
            }


            String token = result.getString("token");
            Constant.token = token;
            pref.setBoolean(Constant.KEY_USER_LOGED, true);

            pref.setString("MyToken", token);

            Intent i = new Intent(context, Home.class);
            startActivity(i);
            finish();


            Constant.token = token;
            startService(new Intent(this, GetDataFromServerOnLogin.class));
            //Intent i = new Intent(context, WalkThrough.class);
            startActivity(i);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.Login_button:


                if (!global.isNetworkAvailable()) {
                    global.showAlert(Constant.NO_CONNECTION_MESSAGE, context);
                    return;
                }
                try {

                    if (checkValidation()) {
       /*                 if (rememberMeCheckBox.isChecked()) {*/
                        String user_name = et_mailId.getText().toString();
                        String password = et_psswd.getText().toString();
                        if (rememberMeCheckBox.isChecked()) {
                            pref.setBoolean("isRemember", true);
                            pref.setString("mail", user_name);
                            pref.setString("password", password);
                        } else {
                            pref.setBoolean("isRemember", false);
                            pref.setString("mail", "");
                            pref.setString("password", "");
                        }

                       /* } else {
                            pref.setclear();
                        }*/
                        sendData();
/*
                        Toast.makeText(Login.this, "Login Successfully",
                                Toast.LENGTH_LONG).show();*/

                    } else {
                        Toast.makeText(Login.this, "Field required",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }

    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(et_mailId, Constant.ErrorMessage_UserName))
            ret = false;


        if (!Validation.hasText(et_psswd,"Please enter Password"))
            ret = false;


        if (!Validation.hasLength(et_psswd, Constant.ErrorMessage_password))
            ret = false;
        return ret;
    }
}