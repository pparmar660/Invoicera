package com.invoicera.InterFace;

import com.invoicera.GlobalData.Constant.SERVICE_TYPE;

import org.json.JSONObject;



public interface WebApiResult {

	 void getWebResult(SERVICE_TYPE type, JSONObject result);
}
