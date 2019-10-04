package com.ibm.mobilefirst.retail.salesassist.adapter.user.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

public class ApiAdapter {

	public static JSONObject callGetAPI(String url,String WCToken,String WCTrustedToken) throws IOException, JSONException {
		HttpGet getUrl = new HttpGet(url); 
//		String HoldingByPass = AccessProperties.configFile.getProperty("HoldingByPass");

//		getUrl.addHeader("HoldingByPass",HoldingByPass);
		getUrl.addHeader("WCToken",WCToken);
		getUrl.addHeader("WCTrustedToken",WCTrustedToken);
		HttpClient client = Htt.getNewHttpClient();
		HttpResponse response = client.execute(getUrl);
		String responseString = new BasicResponseHandler().handleResponse(response);
		JSONObject obj = new JSONObject(responseString);

		return obj;
	}
	
	
	public static JSONObject callPostAPI(String url,String parameters) throws IOException, JSONException {
		try {
			HttpPost request = new HttpPost(url);
//			request.addHeader("HoldingByPass","S5g86bZGwT98rwxGTHyr7D49hc2EEw2bA9funj7PpZq2vHUdGMAqtXvafKbyyBfN");
			request.addHeader("Content-Type","application/json"); 
			HttpClient client = Htt.getNewHttpClient();
			StringEntity params = new StringEntity(parameters);
			request.setEntity(params);
			HttpResponse response = client.execute(request); 
			String responseString = new BasicResponseHandler().handleResponse(response);
			JSONObject obj = new JSONObject(responseString);
			return obj;
		} 
		finally 
		{

		}
	}	
}
