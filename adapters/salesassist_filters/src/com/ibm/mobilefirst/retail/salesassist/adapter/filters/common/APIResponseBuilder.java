package com.ibm.mobilefirst.retail.salesassist.adapter.filters.common;

import java.io.IOException;
import java.util.Map;

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
		error.put("id", code);
		
		JSONArray ar = new JSONArray();
		ar.add(error);
		finalResponse.put("data", jsonData);
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
