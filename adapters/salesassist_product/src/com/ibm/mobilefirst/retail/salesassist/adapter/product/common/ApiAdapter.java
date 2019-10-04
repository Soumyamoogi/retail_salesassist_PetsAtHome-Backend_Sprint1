package com.ibm.mobilefirst.retail.salesassist.adapter.product.common;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

public class ApiAdapter

{
	static Logger logger = Logger.getLogger(ApiAdapter.class.getName());
	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject callGetAPI(String url) throws IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getUrl = new HttpGet(url); 
		HttpResponse response = client.execute(getUrl);
		String responseString = new BasicResponseHandler().handleResponse(response);
		return new JSONObject(responseString);
	}



	public static JSONArray callGetAPI(StringBuilder url) throws IOException, JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getUrl = new HttpGet(url.toString()); 
		HttpResponse response = client.execute(getUrl);
		String responseString = new BasicResponseHandler().handleResponse(response);
		return new JSONArray(responseString);
	}
	
}

