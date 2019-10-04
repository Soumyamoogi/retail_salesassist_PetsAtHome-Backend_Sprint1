package com.ibm.mobilefirst.retail.salesassist.adapter.cart.common;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.PathParam;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.google.gson.Gson;
import com.ibm.mobilefirst.util.AccessProperties;

public class ApiAdapter {

	static Logger logger = Logger.getLogger(ApiAdapter.class.getName());

	public static JSONObject callGetAPI(String url) throws IOException,
			JSONException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getUrl = new HttpGet(url);
		HttpResponse response = client.execute(getUrl);
		String responseString = new BasicResponseHandler()
				.handleResponse(response);
		return new JSONObject(responseString);
	}

	public static JSONObject callGetAPI(String url, String WCToken,
			String WCTrustedToken) throws IOException, JSONException {
		
		String responseString = null;
		
		HttpGet getUrl = new HttpGet(url);
		logger.info(url);
		logger.info(WCToken);
		logger.info(WCTrustedToken);
		getUrl.addHeader("WCToken", WCToken);
		getUrl.addHeader("WCTrustedToken", WCTrustedToken);
		HttpClient client = Htt.getNewHttpClient();
		HttpResponse response = client.execute(getUrl);
		
		HttpEntity postEntity = response.getEntity();
		
		if (postEntity != null) {
			responseString = EntityUtils.toString(postEntity, "UTF-8");
		}
		
		logger.info(responseString);
		return new JSONObject(responseString);
	}
	
	public static String callPostAPI(String url) throws IOException,
			JSONException {

		String responseString = null;
		HttpPost request = new HttpPost(url);
		request.addHeader("Content-Type", "application/json");
		HttpClient client = Htt.getNewHttpClient();
		HttpResponse response = client.execute(request);

		HttpEntity postEntity = response.getEntity();

		if (postEntity != null) {
			responseString = EntityUtils.toString(postEntity, "UTF-8");
		}

		return responseString;

	}

	public static String callPostAPI(String url, String parameters,
			String WCToken, String WCTrustedToken) throws IOException,
			JSONException {

		String responseString = null;
		HttpPost request = new HttpPost(url);
		request.addHeader("Content-Type", "application/json");
		request.addHeader("WCToken", WCToken);
		request.addHeader("WCTrustedToken", WCTrustedToken);
		HttpClient client = Htt.getNewHttpClient();
		StringEntity params = new StringEntity(parameters);
		request.setEntity(params);
		HttpResponse response = client.execute(request);
		HttpEntity postEntity = response.getEntity();
		if (postEntity != null) {
			responseString = EntityUtils.toString(postEntity, "UTF-8");
		}
		logger.severe("responseString" + responseString);
		return responseString;
	}
	
	public static JSONObject callPutAPI(String url, String parameters,
			String WCToken, String WCTrustedToken) throws IOException,
			JSONException {

		String responseBody = null;
		HttpPut request = new HttpPut(url);
		request.addHeader("Content-Type", "application/json");
		request.addHeader("WCToken", WCToken);

		request.addHeader("WCTrustedToken", WCTrustedToken);
		HttpClient client = Htt.getNewHttpClient();
		StringEntity params = new StringEntity(parameters);
		request.setEntity(params);
		HttpResponse response = client.execute(request);

		HttpEntity entityBody = response.getEntity();

		if (entityBody != null) {
			responseBody = EntityUtils.toString(entityBody, "UTF-8");
		}

		return new JSONObject(responseBody);
	}

	public static JSONObject callPutAPIForCheckOut(String url,
			String parameters, String WCToken, String WCTrustedToken)
			throws IOException, JSONException {

		String responseString = null;
		HttpPut request = new HttpPut(url);
		request.addHeader("Content-Type", "application/json");
		request.addHeader("WCToken", WCToken);

		request.addHeader("WCTrustedToken", WCTrustedToken);
		HttpClient client = Htt.getNewHttpClient();
		StringEntity params = new StringEntity(parameters);
		request.setEntity(params);
		HttpResponse response = client.execute(request);
		
		HttpEntity entityBody = response.getEntity();
		
		if (entityBody != null) {
			responseString = EntityUtils.toString(entityBody, "UTF-8");
		}
		
		if (response.getStatusLine().getStatusCode() == 204)
			return new JSONObject();
		
		return new JSONObject(responseString);
	}

	public static int callDeleteAPI(String url, String WCToken,
			String WCTrustedToken) throws IOException, JSONException {

		int responseString = 500;
		HttpDelete request = new HttpDelete(url);
		request.addHeader("Content-Type", "application/json");
		request.addHeader("WCToken", WCToken);

		request.addHeader("WCTrustedToken", WCTrustedToken);
		HttpClient client = Htt.getNewHttpClient();

		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 204) {
			responseString = response.getStatusLine().getStatusCode();

		}

		return responseString;
	}

	public static JSONObject guestIdentity(@PathParam("storeId") String storeId)
			throws Exception {

		String responseString = new String();
		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);

		String url1 = url + storeId + "/guestidentity";
		logger.severe(url1);
		JSONObject response = new JSONObject();

		try {
			responseString = ApiAdapter.callPostAPI(url1);
			logger.severe(responseString);
			Map jsonJavaRootObject = new Gson().fromJson(responseString,
					Map.class);

			response.put("WCToken",
					(String) (jsonJavaRootObject.get("WCToken")));
			response.put("WCTrustedToken",
					(String) (jsonJavaRootObject.get("WCTrustedToken")));
			response.put("userId", (String) (jsonJavaRootObject.get("userId")));

		} catch (IOException e) {
			logger.severe("IOException in guestidentity" + e.getMessage());
		} catch (JSONException e) {
			logger.severe("JSONException in guestidentity" + e.getMessage());
		}

		return response;
	}
	
	public static JSONObject setError(JSONObject errorResponses) throws JSONException {
		
		JSONObject result = new JSONObject();
		JSONObject errors = new JSONObject();
		JSONArray error = new JSONArray();
			try {
					errors.put("code",422);
					errors.put("message",((JSONObject)((JSONArray)errorResponses.get("errors")).get(0)).get("errorMessage").toString());
					error.add(errors);
					result.put("data", new JSONObject());
					result.put("error", error);
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		
		return result;
	}
	
	public static JSONObject setError(JSONObject errorResponses,int errorcode) throws JSONException {
		
		JSONObject result = new JSONObject();
		JSONObject errors = new JSONObject();
		JSONArray error = new JSONArray();
			try {
					errors.put("code",errorcode);
					errors.put("message",((JSONObject)((JSONArray)errorResponses.get("errors")).get(0)).get("errorMessage").toString());
					error.add(errors);
					result.put("data", new JSONObject());
					result.put("error", error);
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		
		return result;
	}
	
	public static JSONObject setError(JSONObject errorResponses,int errorcode,String message) throws JSONException {
		
		JSONObject result = new JSONObject();
		JSONObject errors = new JSONObject();
		JSONArray error = new JSONArray();
			try {
					errors.put("code",errorcode);
					errors.put("message",message);
					error.add(errors);
					result.put("data", new JSONObject());
					result.put("error", error);
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		
		return result;
	}

}
