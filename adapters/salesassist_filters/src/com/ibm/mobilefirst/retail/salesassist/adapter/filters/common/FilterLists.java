package com.ibm.mobilefirst.retail.salesassist.adapter.filters.common;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

public class FilterLists {
	
	static Logger logger = Logger.getLogger(FilterLists.class.getName());
	
	
	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	public static JSONObject callGetAPI(String url) throws IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getUrl = new HttpGet(url); 
//		String HoldingByPass = AccessProperties.configFile.getProperty("HoldingByPass");

//		getUrl.addHeader("HoldingByPass",HoldingByPass);
		HttpResponse response = client.execute(getUrl);
		String responseString = new BasicResponseHandler().handleResponse(response);
		JSONObject obj = new JSONObject(responseString);
		return obj;
	}

}
