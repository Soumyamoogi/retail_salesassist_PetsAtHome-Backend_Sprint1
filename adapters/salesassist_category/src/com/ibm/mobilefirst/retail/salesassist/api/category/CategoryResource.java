/*
  *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.category;

import java.util.logging.Logger;
import java.io.IOException;
import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirst.retail.salesassist.adapter.category.common.APIResponseBuilder;
import com.ibm.mobilefirst.retail.salesassist.adapter.category.common.ApiAdapter;
import com.ibm.mobilefirst.retail.salesassist.adapter.category.common.Category;
import com.ibm.mobilefirst.retail.salesassist.adapter.category.common.ServiceConstants;
import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class CategoryResource {
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(CategoryResource.class.getName());
	private static String WCToken = " ",WCTrustedToken = " ",orderItemId = " ", userId = " ";
    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
    
//    @Context
//    HttpServletRequest request;
//    String sessionId = request.getSession().getId();
    
	/* Path for method: "<server address>/MFP/adapters/MFPAdapter/users" */
    @GET
    @Path("/store/{storeId}/categories/@top")
    @Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
    public Response getTopCategories(@PathParam("storeId") String storeId) {

    	
    	String langId = AccessProperties.configFile.getProperty(ServiceConstants.LANGID);
	    String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);
	    String currency = AccessProperties.configFile.getProperty(ServiceConstants.CURRENTCY);
	    String catalogId = AccessProperties.configFile.getProperty(ServiceConstants.CATALOGID);
    	String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
    	String url1 = url+storeId+"/categoryview/@top?langId=" + langId + "&catalogId=" + catalogId + "&currency="+ currency + "&responseFormat=" + responseFormat;
    	logger.severe(url1);
    	
    	JSONObject result = new JSONObject();
    	JSONObject finalresult = new JSONObject();
    	JSONArray result1 = new JSONArray();
    	
    	JSONArray errors = new JSONArray();
    	JSONObject error = new JSONObject();
    	try {
    		JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
    		//logger.severe(jsonObj.toString());
    		JSONArray arr = jsonObj.getJSONArray("CatalogGroupView");
    		if(arr != null && !arr.isEmpty()) {	
	    		for(int i=0;i<arr.length();i++)
	    		{
	    			Category prod = new Category();
	    			if(arr.getJSONObject(i).has("name") && (arr.getJSONObject(i).getString("name").equalsIgnoreCase("content")
	    					|| arr.getJSONObject(i).getString("name").equalsIgnoreCase("Offers"))) {
	    				arr.remove(i);
	    			} else {
	    				prod.setEntityFromJsonTopCategories(arr.getJSONObject(i));
		    			result = prod.toObjectTopCategories();
		    			result1.add(result);
	    			}
	    			
	    		}
    		} else {
    			error.put("message", "No Result Found");
    			error.put("code", "1000");
    			error.put("description", "No Result Found");
    			
    			errors.add(error);
    		}
    		
    		finalresult.put("data",result1);
    		finalresult.put("error",errors);
    		
    		return APIResponseBuilder.sendSuccessResponse(finalresult);
    	
    	} catch (IOException e) {
	        logger.severe("IOExc in getTopCategories:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(),ServiceConstants.ERROR_CODE_INTERNALSERVERERROR);

    	} catch (JSONException e) {
	        logger.severe("JSONExc in getTopCategories:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(), ServiceConstants.ERROR_CODE_IMPROPERJSON);

    	}
    	catch (Exception e) {
	        logger.severe("Exc in getTopCategories:" + e);	
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION);

  		}

    }

    
    @GET
   	@Path("/store/{storeId}/categories/byParentCategory/{parentId}")
   	@Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
    public Response getSubCategories(@PathParam("storeId") String storeId,@PathParam("parentId") String catId) {

    	

    	String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);

    	String url1 = url+storeId+"/categoryview/byParentCategory/"+catId;
    	logger.severe(url1);
    	
    	JSONObject result = new JSONObject();
    	JSONArray result1 = new JSONArray();
    	JSONArray errors = new JSONArray();
    	JSONObject error = new JSONObject();
    	JSONObject finalresult = new JSONObject();

    	try {
    		JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
        	//logger.severe("subCategory output:" + jsonObj.toString());
    		if(jsonObj.has("CatalogGroupView")) {
	    		JSONArray arr = jsonObj.getJSONArray("CatalogGroupView");
	    		if(arr!= null) {
		    		for(int i=0;i<arr.length();i++)
		    		{
		    			Category prod = new Category();
		    			prod.setEntityFromJsonByParent(arr.getJSONObject(i), catId);	
		    			result = prod.toObjectByParent();	
		    			result1.add(result);
		    		}	
	    		} else {
	    			error.put("message", "No Result Found");
	    			error.put("code", "1000");
	    			error.put("description", "No Result Found");
	    			
	    			errors.add(error);
	    		}
    		} 
    		
    		finalresult.put("data",result1);
    		finalresult.put("error",errors);
    		
    		return APIResponseBuilder.sendSuccessResponse(finalresult);

    	} catch (IOException e) {
	        logger.severe("IOExc in getSubCategories:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(),ServiceConstants.ERROR_CODE_INTERNALSERVERERROR);

    	}catch (JSONException e) {
	        logger.severe("JSONExc in getSubCategories:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(), ServiceConstants.ERROR_CODE_IMPROPERJSON);

    	}
    	catch (Exception e) {
	        logger.severe("Exc in getSubCategories:" + e);	
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION);

  		}
    }	
    
    
    
    
    @GET
   	@Path("/store/{storeId}/categoryById/{categoryId}")
   	@Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
    public Response getCategoryById(@PathParam("storeId") String storeId,@PathParam("categoryId") String catId) {

    	

    	String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);

    	String url1 = url+storeId+"/categoryview/byId/"+catId;
    	logger.severe("getcategoryById url" + url1);
    	
    	JSONObject result = new JSONObject();
    	JSONArray result1 = new JSONArray();
    	JSONArray errors = new JSONArray();
    	JSONObject error = new JSONObject();
    	JSONObject finalresult = new JSONObject();

    	try {
    		JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
    		if(jsonObj.has("CatalogGroupView")) {
	    		JSONArray arr = jsonObj.getJSONArray("CatalogGroupView");
	    		if(arr!= null) {
		    		for(int i=0;i<arr.length();i++)
		    		{
		    			Category prod = new Category();
		    			prod.setEntityFromJson(arr.getJSONObject(i), catId);	
		    			result = prod.toObjectByParent();	
		    			result1.add(result);
		    		}	
	    		} else {
	    			error.put("message", "No Result Found");
	    			error.put("code", "1000");
	    			error.put("description", "No Result Found");
	    			
	    			errors.add(error);
	    		}
    		} 
    		
    		finalresult.put("data",result1);
    		finalresult.put("error",errors);
    		
    		return APIResponseBuilder.sendSuccessResponse(finalresult);

    	} catch (IOException e) {
	        logger.severe("IOExc in getSubCategories:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(),ServiceConstants.ERROR_CODE_INTERNALSERVERERROR);

    	}catch (JSONException e) {
	        logger.severe("JSONExc in getSubCategories:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(), ServiceConstants.ERROR_CODE_IMPROPERJSON);

    	}
    	catch (Exception e) {
	        logger.severe("Exc in getSubCategories:" + e);	
	        return APIResponseBuilder.sendFailResponse(new JSONArray(),e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION);

  		}
    }
  }
