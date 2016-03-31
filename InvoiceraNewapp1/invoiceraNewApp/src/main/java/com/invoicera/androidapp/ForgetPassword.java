package com.invoicera.androidapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.GlobalData.Constant;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Parvesh on 15/12/15.
 */
public class ForgetPassword extends BaseActivity implements WebApiResult {


    private static final String MSG_TAG = "message";
    private static final String CODE_TAG = "code";

    private String message;

    private EditText email, sub_domain;
    private String code = "";
    //TextView back_button;
    ImageView backImage;
    WebApiResult webApiResult;
    private Boolean doubleBackToExitPressedOnce = false;

/*        *//* on remote call complete *//*
        JSONListener lgetPassword = new JSONListener() {

            @Override
            public void onRemoteCallComplete(JSONObject jsonFromNet) {
                if (jsonFromNet != null) {
                    try {
                        message = jsonFromNet.getString(MSG_TAG);
                        code = jsonFromNet.getString(CODE_TAG);
                        alertDialog();
                    } catch (final Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final Toast toast = Toast.makeText(
                                            ForgotPasswordActivity.this,
                                            e.getMessage(), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, -30, -60);
                                    toast.show();
                                } catch (Exception e) {

                                }
                            }

                        });
                    }
                } else {
                    message = GlobalVariables.request_time_out;
                    alertDialog();
                }
            }
        };*/

    /****
     * to show alert dialog
     ****/
    @SuppressWarnings("deprecation")
    private void alertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setTitle(Constant.title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equalsIgnoreCase("200")) {
                    code = "";
                    Intent intent = new Intent(context, Login.class);
                    startActivity(intent);
                    overridePendingTransition(0, R.anim.exit_slide_right);
                } else {
                    // do nothing
                }
            }
        });
        alertDialog.show();
    }

    /*
     * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
     * menu; this adds items to the action bar if it is present.
     * getMenuInflater().inflate(R.menu.forgot_password, menu); return true; }
     */
    // handling back button
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            Intent intent = new Intent(this, Splash.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // finish();
            // android.os.Process.killProcess( android.os.Process.myPid() );
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "" + Constant.back_again, Toast.LENGTH_SHORT)
                .show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /**
     * initializing all the variables and views
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        context = this;

        Button password = (Button) findViewById(R.id.submitButton);
        email = (EditText) findViewById(R.id.email);
        sub_domain = (EditText) findViewById(R.id.sub_domain);
        backImage=(ImageView)findViewById(R.id.back);
        //back_button = (TextView) findViewById(R.id.back_button);
        webApiResult = this;

		/*		*//**** dynamic lay out for forgot logo image (left, top, right, bottom) ****/
        /*
         * RelativeLayout.LayoutParams login = new RelativeLayout.LayoutParams(
		 * RelativeLayout.LayoutParams.WRAP_CONTENT,
		 * RelativeLayout.LayoutParams.WRAP_CONTENT); login.setMargins(10,
		 * GlobalVariables.getDeviceHeight() / 5, 10, 10);
		 * forget_logo.setLayoutParams(login);
		 */
/*        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();

            }
        });*/

        /**** password button to retrieve password ****/
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Pattern pattern = Pattern.compile("[a-z0-9._]+@.+\\.[a-z]+");
                Matcher match = pattern.matcher(email.getText().toString());

                if (!email.getText().toString().equals("")) {
                    if (match.matches()) {
                        if (!sub_domain.getText().toString().equals("")) {
                            if (global.isNetworkAvailable()) {
                                /*
                                 * connection to internet check
								 */
                                try {


                                    JSONObject filter = new JSONObject();
                                    filter.put("email_id", email.getText()
                                            .toString());
                                    filter.put("dns", sub_domain.getText()
                                            .toString());
                                    filter.put("method", "getPassword");

                                    WebRequest request = new WebRequest(context, filter, Constant.invoicelistURL,
                                            Constant.SERVICE_TYPE.FORGET_PASSWORE, null, webApiResult, true);

                                    request.execute();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Toast toast = Toast
                                                        .makeText(
                                                                context,
                                                                Constant.request_not_done,
                                                                Toast.LENGTH_LONG);
                                                toast.setGravity(
                                                        Gravity.CENTER, -30,
                                                        -60);
                                                toast.show();
                                            } catch (Exception e) {

                                            }
                                        }

                                    });
                                }
                            } else {
                                message = Constant.network_error;

                                alertDialog();
                            }

                        } else {
                            message = Constant.subdoamin_error;
                            alertDialog();
                        }
                    } else {
                        message = Constant.email_error_match;
                        alertDialog();
                    }
                } else {
                    message = Constant.email_error;
                    alertDialog();
                }

            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void getWebResult(Constant.SERVICE_TYPE type, JSONObject result) {

        if (result == null)
            return;

        try {
            if (!result.getString("code").equalsIgnoreCase("200")) {
                if (result.has("message"))
                    Toast.makeText(context, result.getString("message").toString(), Toast.LENGTH_SHORT).show();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (type) {

            case FORGET_PASSWORE:
                try {
                    message = result.getString("message");
                    alertDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;


        }

    }
}
