/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.store;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.ibm.mobilefirst.retail.salesassist.adapter.store.common.APIResponseBuilder;
import com.ibm.mobilefirst.retail.salesassist.adapter.store.common.ApiAdapter;
import com.ibm.mobilefirst.retail.salesassist.adapter.store.common.ServiceConstants;
import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class StoreResource {
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(StoreResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();

    @GET
	@Path("/stores/{storeId}")
	@Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
	public Response getStoresOld(@PathParam("storeId") String storeId) {

    	
	    String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);   	    
    	String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
    	String physicalStoreId = AccessProperties.configFile.getProperty(ServiceConstants.PHYSICALID);
    	String url1 = url+storeId+"/storelocator/byStoreId/" +physicalStoreId;
    	logger.severe(url1);
        String longitude = "";
        String latitude = "";
    	JSONObject storeJson = new JSONObject();
    	JSONObject nearbyStores = new JSONObject();
    	JSONObject stores = new JSONObject();    	
    	JSONArray storeArr = new JSONArray();
    	JSONArray finalArr = new JSONArray();
        JSONObject store = new JSONObject();
    	try {
    		storeJson = ApiAdapter.callGetAPI(url1);
    		logger.severe(storeJson.toString());
			JSONArray arr = storeJson.getJSONArray("PhysicalStore");	           
			for(int i = 0;i<arr.length();i++)
			{	             
	             JSONObject jsonObj = arr.getJSONObject(i);
	             if(jsonObj.has("uniqueID")) 
	             {
	            	 store.put("storeId", jsonObj.get("uniqueID").toString());
				 }
	             
	             if(jsonObj.has("storeName")) 
	             {
	            	 String storeName= jsonObj.getString("storeName");
	            	 store.put("name", storeName);
				 }
	             if(jsonObj.get("addressLine") instanceof JSONArray) 
	             {
	            	 StringBuffer address = new StringBuffer();         	 
	            	 String addressln= jsonObj.getJSONArray("addressLine").get(0).toString();
	            	 address.append(addressln).append(",");
	            	 
	            	 if(jsonObj.has("city"))  {
	            		 String city = jsonObj.get("city").toString();
	                	 address.append(",").append(city);
	            	 }
	          	 
	            	 if(jsonObj.has("stateOrProvinceName"))  {
	            		 String state = jsonObj.get("stateOrProvinceName").toString();
	                	 address.append(",").append(state);
	            	 }
	            	 if(jsonObj.has("country"))  {
	            		 String country = jsonObj.get("country").toString();
	                	 address.append(",").append(country);
	            	 }
	
	            	 if(jsonObj.has("postalCode"))  {
	            		 String postal = jsonObj.get("postalCode").toString();
	                	 address.append(",").append(postal);
	            	 }
	            	 store.put("address", address.toString().trim());
				 }
	             
	             if(jsonObj.has("telephone1")) 
	             {
	            	 String phone = jsonObj.get("telephone1").toString().trim();
	            	 phone = phone.replace(".", "-");
	            	 store.put("phone", phone);
				 }  
	        
	           if(jsonObj.has("Attribute")) 
	           {           	   
	               for(int k = 0 ;k<jsonObj.getJSONArray("Attribute").length();k++)
	               {
	            	   if(jsonObj.getJSONArray("Attribute").getJSONObject(k).containsValue("StoreHours")) {
	                       store.put("hours",jsonObj.getJSONArray("Attribute").getJSONObject(k).getString("value").replace("<br />", " "));
	         		   
	            	   }
	               }
	           }
				if(jsonObj.has("latitude")) 
	             {
	            	 latitude = jsonObj.getString("latitude");
	             }
	             if(jsonObj.has("longitude")) {
	            	 longitude = jsonObj.getString("longitude");
	             }

	             String url2 = url+storeId+"/storelocator/latitude/" +latitude + "/longitude/" +longitude + "?siteLevelStoreSearch=false&responseFormat=json";

	         	nearbyStores = ApiAdapter.callGetAPI(url2);
	    		logger.severe(nearbyStores.toString());	    		
				JSONArray nearbyStoreArr = nearbyStores.getJSONArray("PhysicalStore");		           
          
				for(int j = 0;j<nearbyStoreArr.length();j++)
				{
			    	JSONObject distance = new JSONObject();    	
					
		           if(nearbyStoreArr.getJSONObject(j).has("distance")) {
		        	   distance.put("distance",nearbyStoreArr.getJSONObject(j).getString("distance") );
		           }
		           
		           if(nearbyStoreArr.getJSONObject(j).has("uniqueID")) {
		        	   distance.put("storeId",nearbyStoreArr.getJSONObject(j).getString("uniqueID") );
		           }
		           storeArr.add(distance);      	  
				}
				store.put("distances", storeArr);	
			}
			finalArr.add(store);
			stores.put("data", finalArr);
    		return APIResponseBuilder.sendSuccessResponse(JSON.parse(stores.toString()));

		} catch (IOException e) {
	        logger.severe("IOExc in getStoresOld:" + e.toString());
	        return APIResponseBuilder.sendFailResponse(new JSONObject(),"IOExc in getStoresOld:",ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION ,e.toString());

		}catch (JSONException e) {
	        logger.severe("JSONExc in getStoresOld:" + e.toString());
	        return APIResponseBuilder.sendFailResponse(new JSONObject(),"Exception in getStoresOld:", ServiceConstants.ERROR_CODE_IMPROPERJSON ,e.toString());

    	}
    	catch (Exception e) {
	        logger.severe("Exc in getStoresOld:" + e.getMessage());	
	        return APIResponseBuilder.sendFailResponse(new JSONObject(),"Exc in getStoresOld:",ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION ,e.getMessage());

  		}
//		return Response.ok(stores.toString(), MediaType.APPLICATION_JSON).build();
    }
   
    @GET
	@Path("/stores/{storeId}/latitude/{latitude}/longitude/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
	public Response getStores(@PathParam("storeId") String storeId,@PathParam("latitude") String latitude,
			@PathParam("longitude") String longitude) {

    	
    	String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
    	String radius = AccessProperties.configFile.getProperty(ServiceConstants.RADIUS);
    	String maxItems = AccessProperties.configFile.getProperty(ServiceConstants.MAXITEMS);
   	
    	if(latitude.equals("0") && longitude.equals("0")) {
    		latitude = AccessProperties.configFile.getProperty(ServiceConstants.LATITUDE);
    		longitude = AccessProperties.configFile.getProperty(ServiceConstants.LONGITUDE);
    		radius = AccessProperties.configFile.getProperty(ServiceConstants.DEFAULT_RADIUS);
    		maxItems = AccessProperties.configFile.getProperty(ServiceConstants.DEFAULT_MAXITEMS);
    	}
    	
    	String url1 = url+storeId+"/storelocator/latitude/" +latitude+"/longitude/"+longitude+"?maxItems="+maxItems+"&siteLevelStoreSearch="+false+"&radius="+radius;
    	
    	
    	logger.severe(url1);
    	JSONObject storeJson = new JSONObject();
    	JSONObject finalSorting = new JSONObject();
    	JSONObject response = new JSONObject();
 		DecimalFormat df = new DecimalFormat("0.00");      
    	String distance = "";
    	List<JSONObject> obj1 = new ArrayList<JSONObject>();
    	
    	try {
    		storeJson = ApiAdapter.callGetAPI(url1);
 	    		
    			if(storeJson.containsKey("PhysicalStore")) {
		    		
    				JSONArray stores = storeJson.getJSONArray("PhysicalStore");
		    		
		    		for(int i=0; i<stores.length();i++) {
		    			stores.getJSONObject(i).remove("Attribute");
		    			distance = stores.getJSONObject(i).getString("distance");
		    			if(distance!= null && (!distance.isEmpty())) {
		    				stores.getJSONObject(i).put("distance", df.format(Double.parseDouble(distance)));
		    			}   			
	    				stores.getJSONObject(i).put("uniqueID", stores.getJSONObject(i).getString("storeName"));
	    				stores.getJSONObject(i).put("displayStoreName",((JSONObject)((JSONArray)stores.getJSONObject(i).get("Description")).get(0)).getString("displayStoreName"));
	    				
	    				stores.getJSONObject(i).remove("Description");
	    				
	    				obj1.add(stores.getJSONObject(i));
		    		}
		    		
		    		Collections.sort(obj1,new Soriting());  
		    		    		
		    		finalSorting.put("PhysicalStore" ,obj1);
		    		logger.severe("From WCS"+storeJson.toString());
		    		logger.severe("After sorting"+finalSorting.toString());
		    		response.put("data",finalSorting);
		    		response.put("error",new JSONArray());
    		} else {
    			return APIResponseBuilder.sendFailResponse(new JSONObject(),"No Store Found",ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION ,"No Store Found");
    		}
    		
    	}catch (IOException e) {
	        logger.severe("IOExc in getStores:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION ,e.getMessage());

		} catch (JSONException e) {
	        logger.severe("JSONExc in getStores:" + e);
	        return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(), ServiceConstants.ERROR_CODE_IMPROPERJSON ,e.getMessage());

    	}
    	catch (Exception e) {
	        logger.severe("Exc in getStores:" + e);	
	        return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION ,e.getMessage());

  		}
    	
    	return APIResponseBuilder.sendSuccessResponse(response);

    }
}
