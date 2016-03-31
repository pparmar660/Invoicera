package com.invoicera.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.invoicera.GlobalData.Constant;
import com.invoicera.GlobalData.Constant.SERVICE_TYPE;
import com.invoicera.Utility.Validation;
import com.invoicera.InterFace.WebApiResult;
import com.invoicera.Webservices.WebRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Sign_up extends BaseActivity implements OnClickListener,
		WebApiResult {

	
	TextView sign_in,termsService;
	EditText contactNameEt, companyNameEt, emailEt, userNameEt, passwordEt,
			confirmpasswordEt, hostNameEt;
	Button signUpBtn;
	SERVICE_TYPE type;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		termsService = (TextView) findViewById(R.id.terms_text_click);
		sign_in = (TextView) findViewById(R.id.sign_in_text);
		sign_in.setOnClickListener(this);
		termsService.setOnClickListener(this);

		// init variable

		contactNameEt = (EditText) findViewById(R.id.contact_name);
		companyNameEt = (EditText) findViewById(R.id.Company_name);
		emailEt = (EditText) findViewById(R.id.email_edit);
		userNameEt = (EditText) findViewById(R.id.User_name_edit);
		passwordEt = (EditText) findViewById(R.id.Password_edit);
		confirmpasswordEt = (EditText) findViewById(R.id.confirm_pass_edit);
		hostNameEt = (EditText) findViewById(R.id.url_et);
		signUpBtn = (Button) findViewById(R.id.btn);
		signUpBtn.setOnClickListener(this);
		/* termsService.setText(Html.fromHtml(getString(R.string.terms))); */

		type = Constant.SERVICE_TYPE.SIGN_IN;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch (v.getId()) {
		case R.id.sign_in_text:
			i = new Intent(Sign_up.this, Login.class);
			startActivity(i);
			finish();
			break;
		case R.id.btn:

			
			 

			try {
				if (checkValidation()) {

					/*i = new Intent(context, WalkThrough.class);
					startActivity(i);*/
					finish();
					sendData();

				} else {
					Toast.makeText(Sign_up.this, "Field required",
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

	private void sendData() throws JSONException {
		// TODO Auto-generated method stub

		JSONObject obj = new JSONObject();

		obj.put(Constant.KEY_METHOD, "userRegistration");
		obj.put(Constant.KEY_CONTACT_NAME, contactNameEt.getText());
		obj.put(Constant.KEY_NAME, companyNameEt.getText());
		obj.put(Constant.KEY_EMAIL, emailEt.getText());
		obj.put(Constant.KEY_USER_NAME, userNameEt.getText());
		obj.put(Constant.KEY_PASSWORD, passwordEt.getText());
		obj.put(Constant.KEY_LOGIN_URL, hostNameEt.getText());
		obj.put(Constant.KEY_MAILING_LIST, "yes");

		/*
		 * WebRequest request = new WebRequest(context, obj,
		 * Constant.forgotPasswordURL, type, this); request.execute();
		 */
		WebRequest request = new WebRequest(context, obj,
				Constant.forgotPasswordURL, type, Constant.token,this,true);
		request.execute();
	}

	@Override
	public void getWebResult(SERVICE_TYPE type, JSONObject result) {
		// TODO Auto-generated method stub

		switch (type) {
		case SIGN_IN:
			System.out.println("++++++++++++++++Sign_up+++++++++++"+result);
			Intent i = new Intent(context, WalkThrough.class);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}

	}

	private boolean checkValidation() {
		boolean ret = true;

		if (!Validation.hasText(userNameEt, Constant.ErrorMessage_UserName))
			ret = false;
		if (!Validation.hasLength(passwordEt, Constant.ErrorMessage_password))
			ret = false;
		if (!Validation.hasMatching(passwordEt, confirmpasswordEt,
				Constant.ErrorMessage_match))
			ret = false;
		if (!Validation.hasText(companyNameEt, Constant.ErrorMessage_company))
			ret = false;
		if (!Validation.hasText(contactNameEt, Constant.ErrorMessage_contact))
			ret = false;
		if (!Validation.hasText(hostNameEt, Constant.ErrorMessage_host))
			ret = false;
		if (!Validation.isEmailAddress(emailEt, true,
				Constant.ErrorMessage_email))
			ret = false;
		return ret;
	}
}
