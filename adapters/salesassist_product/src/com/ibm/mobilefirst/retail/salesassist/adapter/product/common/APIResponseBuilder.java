package com.ibm.mobilefirst.retail.salesassist.adapter.product.common;

import java.io.IOException;
import java.util.Map;

import org.apache.wink.json4j.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ibm.mobilefirst.response.ServiceResponse;

import javax.ws.rs.core.Response;


public class APIResponseBuilder {
	/**
	 * Send success message with default values
	 * @param jsonData
	 * @return
	 */
	public static Response  sendSuccessResponse(Object jsonData)
	{
		
		Response response = Response.status(200).entity(jsonData).build();

		Map map = response.getMetadata();

		return response;
	}
	
	public static Response  sendSuccessResponseStd(Object jsonData)
{

		JSONObject error = new JSONObject();
		JSONObject finalResponse = new JSONObject();
		error.put("message", "Success");
		error.put("code", "200");
		
		JSONArray ar = new JSONArray();
		ar.add(error);
		finalResponse.put("data", jsonData);
		finalResponse.put("error", ar);

		Response response = Response.status(200).entity(finalResponse)
				.build();

		Map map = response.getMetadata();

		return response;
	}
	
	public static Response  sendSuccessResponseStd1(org.apache.wink.json4j.JSONObject jsonData)
{

		JSONObject error = new JSONObject();
		JSONObject finalResponse = new JSONObject();
		error.put("message", "Success");
		error.put("code", "200");
		
		JSONArray ar = new JSONArray();
		ar.add(error);
		//finalResponse.put("data", jsonData);
		try {
			jsonData.put("error", ar);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//finalResponse.put("error", ar);

		Response response = Response.status(200).entity(jsonData)
				.build();

		Map map = response.getMetadata();

		return response;
	}
	/**
	 * Service is invocation is successful.But you want to edit the message.
	 * For example, object not found on GET invocation.
	 * @param jsonData
	 * @param message
	 * @return
	 */
	
	
	public static ServiceResponse  sendSuccessResponse(Object jsonData,String message)
	{
		ServiceResponse response = new ServiceResponse(); 

		

		response.setData(jsonData);

		return response;
	} 
	
	
	
	public static Response  sendFailResponse(Object jsonData,String description, String code,String message)
	{
		
		JSONObject error = new JSONObject();
		JSONObject finalResponse = new JSONObject();
		error.put("message", message);
		error.put("code", code);
		error.put("description", description);
		
		JSONArray ar = new JSONArray();
		ar.add(error);
		finalResponse.put("data", jsonData);
		finalResponse.put("error", ar);

		Response response = Response.status(200).entity(finalResponse)
				.build();

		Map map = response.getMetadata();

		return response;
	}
	
	
	public static Response  sendFailResponseStd(String message, String code)
	{
		
		JSONArray ar = new JSONArray();
		JSONObject error = new JSONObject();
		JSONObject finalResponse = new JSONObject();
		
		error.put("message", message);
		error.put("code", code);
		error.put("id", code);
		
		ar.add(error);
		finalResponse.put("data", new JSONArray());
		finalResponse.put("error", ar);		

		Response response = Response.status(200).entity(finalResponse)
				.build();

		Map map = response.getMetadata();

		return response;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static ServiceResponse  sendUpdateResponse(Object jsonData,String message)
	{
		ServiceResponse response = new ServiceResponse();

		
		
		com.cloudant.client.api.model.Response jsonData1 = (com.cloudant.client.api.model.Response)jsonData;
		
		String idFieldVal = jsonData1.getId();
		
		JSONObject newJsonObj = new JSONObject();
		
		response.setData(newJsonObj);
		

		return response;
	} 
	
	public static ServiceResponse sendSuccessResponse2(Object JSONObject) throws IOException {

		
		
	      ServiceResponse response = new ServiceResponse(); 

			

			response.setData(JSONObject);

			return response;

		}}
