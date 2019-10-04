/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.user;


import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.ibm.mobilefirst.retail.salesassist.adapter.user.common.ServiceConstants;
import com.ibm.mobilefirst.retail.salesassist.adapter.user.common.ApiAdapter;
import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class UserResource {
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(UserResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
	private static String WCToken = " ",WCTrustedToken = " ",orderItemId = " ", userId = " ";

    
    @POST
	@Path("/store/{storeId}/auth/users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
	public Response login(@PathParam("storeId") String storeId, JSONObject request) {

    	
//	    String url = AccessProperties.configFile.getProperty("urlssl");
	    String url = AccessProperties.configFile.getProperty(ServiceConstants.URLSSL);

    	JSONObject jsonObj = new JSONObject();
        JSONObject jsonReq = new JSONObject();     
	  	JSONObject outputJson = new JSONObject();
    	JSONObject finalJson = new JSONObject();
		try {
	        String url1 = url+storeId+"/loginidentity";
	        logger.severe(url1);
	        jsonReq.put("logonPassword", request.get("password"));
		  	jsonReq.put("logonId", request.get("userId"));
		  	String credentials = jsonReq.toString();
	        //logger.severe(credentials);
		  	
			jsonObj = ApiAdapter.callPostAPI(url1,credentials);
			WCToken = jsonObj.getString("WCToken");
			WCTrustedToken = jsonObj.getString("WCTrustedToken");
			userId = jsonObj.getString("userId");
			//logger.severe(jsonObj.toString());
			outputJson.put("userId", userId);
			outputJson.put("WCToken", WCToken);
			outputJson.put("WCTrustedToken", WCTrustedToken);
			finalJson.put("data", outputJson);

		} catch (IOException e) {
			logger.severe("IOExc in login:" + e.getMessage());
		} catch (JSONException e) {
	        logger.severe("JSONExc in login:" + e.getMessage());
		}
    
		return Response.ok(finalJson.toString(), MediaType.APPLICATION_JSON).build();
    }    

    
    
    @GET
	@Path("/store/{storeId}/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
	public Response getUserById(@PathParam("storeId") String storeId, @PathParam("userId") String userId) {

    	JSONObject jsonObj = new JSONObject();
		
	    String url = AccessProperties.configFile.getProperty(ServiceConstants.URLSSL);
	    String responseFormat = AccessProperties.configFile.getProperty("responseFormat");
        String url1 = url+storeId+"/person/" + userId + "?responseFormat="+responseFormat;
        logger.info(url1);
	  	JSONObject outputJson = new JSONObject();
	  	JSONObject output = new JSONObject();
		
		try {
			jsonObj = ApiAdapter.callGetAPI(url1,WCToken,WCTrustedToken);
			//logger.severe(jsonObj.toString());
			outputJson.put("userId", userId);
			if(jsonObj.has("logonId"))
			outputJson.put("userName", jsonObj.getString("logonId"));
			if(jsonObj.has("logonPassword"))
				outputJson.put("password", jsonObj.getString("logonPassword"));
			if(jsonObj.has("email1"))
				outputJson.put("email", jsonObj.getString("email1"));
			if(jsonObj.has("phone1"))
				outputJson.put("phone", jsonObj.getString("phone1"));
			if(jsonObj.has("address"))
				outputJson.put("address", jsonObj.getString("address"));
			if(jsonObj.has("securityId"))
				outputJson.put("securityId", jsonObj.getString("securityId"));
			if(jsonObj.has("picture"))
				outputJson.put("picture", jsonObj.getString("picture"));
			
			output.put("data", outputJson);
		} catch (IOException e) {
	        logger.severe("IOExc in getUserById:" + e.getMessage());
		} catch (JSONException e) {
	        logger.severe("JSONExc in getUserById:" + e.getMessage());
		}
    
		return Response.ok(output.toString(), MediaType.APPLICATION_JSON).build();
    }    
}
