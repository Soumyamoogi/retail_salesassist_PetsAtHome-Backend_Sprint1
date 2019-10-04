/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.promocode;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.ibm.mobilefirst.retail.salesassist.adapter.promocode.common.APIResponseBuilder;
import com.ibm.mobilefirst.retail.salesassist.adapter.promocode.common.ApiAdapter;
import com.ibm.mobilefirst.retail.salesassist.adapter.promocode.common.ServiceConstants;
import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class PromoCodeResource {
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(PromoCodeResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();

    @GET
	@Path("/store/{storeId}/assigned_promotion_code")
	@Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled=false)
	public Response getPromotion(@PathParam("storeId") String storeId,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId) throws Exception {

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + ServiceConstants.PROMO_ASSIGNED+"?responseFormat="
				+ responseFormat;
		logger.severe(url1);
		JSONObject obj = new JSONObject();
		JSONObject resObj = new JSONObject();
		JSONObject order = new JSONObject();
	
		JSONArray itemArr = new JSONArray();
		
		
		JSONObject result = new JSONObject();
		
		try {
			obj = ApiAdapter.callGetAPI(url1, commerceToken, wcTrustedToken);
			logger.severe(obj.toString());
			
			if(obj.containsKey(ServiceConstants.ERRORS)) {
				if(((JSONObject)((JSONArray)obj.get("errors")).get(0)).get("errorCode").toString().equals("CWXFR0210E")) {
					result = ApiAdapter.setError(obj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else if(((JSONObject)((JSONArray)obj.get("errors")).get(0)).get("errorCode").toString().equals("CMN1039E")) {
					result = ApiAdapter.setError(obj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
				
				result = ApiAdapter.setError(obj,503);
				return APIResponseBuilder.sendSuccessResponse(result);
			} else if(obj.containsKey(ServiceConstants.ERROR)) {
				if(obj.get("error").toString().contains("An invalid cookie was received for the user")) {
					result = ApiAdapter.setError(obj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else {
					result = ApiAdapter.setError(obj,425,"Something Went Wrong, Please try after sometime!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
			}
			
			if (obj.has(ServiceConstants.ORDERID)) {
				resObj.put(ServiceConstants.ORDERID, obj.getString(ServiceConstants.ORDERID));
			}
		if(obj.has("promotionCode")){
			JSONArray promocode = obj.getJSONArray("promotionCode");
			for (int i = 0; i < promocode.length(); i++) {
				JSONObject promoObj = new JSONObject();
				JSONObject itemJson = promocode.getJSONObject(i);
				promoObj.put("code", itemJson.getString("code"));
				if(itemJson.has("associatedPromotion")){
					JSONArray assopromoarr= (JSONArray) itemJson.get("associatedPromotion");
					for(int j=0;j<assopromoarr.length();j++)
					{
						JSONObject assPromoObj = assopromoarr.getJSONObject(j);
						if(assPromoObj.has("description"))
						{
							if( assPromoObj.getString("description")==null || assPromoObj.getString("description").isEmpty()  )
							{
								promoObj.put("description", ".");
							}
							else
							{
								
								promoObj.put("description", assPromoObj.getString("description"));
							}
						}
						
					}
				
				}
				itemArr.add(promoObj);
				
			}
			}
		
			resObj.put("promotionCode", itemArr);
			
	
			order.put("data", resObj);
			order.put(ServiceConstants.ERROR, new JSONArray());

		} catch (IOException e) {
			logger.severe("IOExc in getPromoCode:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());
		} catch (JSONException e) {
			logger.severe("JSONExc in getPromoCode:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		} catch (Exception e) {
			logger.severe("Exc in getPromoCode:" + e);

			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		}
		
		return Response.ok(order.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
   
    @POST
	@Path("/store/{storeId}/apply_promotion_code")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response applyPromoCode(@PathParam("storeId") String storeId,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId, JSONObject request) {

		
		JSONObject jsonRes = new JSONObject();
		
		JSONArray itemArr = new JSONArray();
		JSONObject result = new JSONObject();
		String output = "";
		
		try {
			
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + ServiceConstants.PROMO_ASSIGNED +"?responseFormat="+ responseFormat;
		logger.severe(url1);
	
		
	
//		if (request.get(ServiceConstants.PROMOCODE).toString().trim().isEmpty()) {
//			jsonReq.put(ServiceConstants.PROMOCODE, ".");
//		} else {
//			jsonReq.put(ServiceConstants.PROMOCODE, request.get(ServiceConstants.PROMOCODE));
//		}
		
		String input = request.toString();

		logger.severe(input);

		logger.severe("WCToken:" + commerceToken);
		logger.severe("WCTrustedToken:" + wcTrustedToken);

			output = ApiAdapter.callPostAPI(url1, input, commerceToken,
					wcTrustedToken);
			
			logger.severe(output);
			
			JSONObject errorResponses = new JSONObject(output);
			if(errorResponses.containsKey(ServiceConstants.ERRORS)) {

				if(((JSONObject)((JSONArray)errorResponses.get("errors")).get(0)).get("errorCode").toString().equals("CWXFR0210E")) {
					result = ApiAdapter.setError(errorResponses,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else if(((JSONObject)((JSONArray)errorResponses.get("errors")).get(0)).get("errorCode").toString().equals("CMN1039E")) {
					result = ApiAdapter.setError(errorResponses,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
				
				result = ApiAdapter.setError(errorResponses,503);
				return APIResponseBuilder.sendSuccessResponse(result);	
				
			}  else if(errorResponses.containsKey(ServiceConstants.ERROR)) {
				if(errorResponses.get("error").toString().contains("An invalid cookie was received for the user")) {
					result = ApiAdapter.setError(errorResponses,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else {
					result = ApiAdapter.setError(errorResponses,425,"Something Went Wrong, Please try after sometime!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
			} else {
				JSONObject resObj = new JSONObject();
				JSONObject outputobj = new JSONObject(output);
				if (outputobj.has(ServiceConstants.ORDERID)) {
					resObj.put(ServiceConstants.ORDERID, outputobj.getString(ServiceConstants.ORDERID));
				}
				if(outputobj.has("promotionCode")){
					JSONArray promocode = outputobj.getJSONArray("promotionCode");
					for (int i = 0; i < promocode.length(); i++) {
						JSONObject item1 = new JSONObject();
						JSONObject itemJson = promocode.getJSONObject(i);
						item1.put("code", itemJson.getString("code"));
						
						itemArr.add(item1);
					}}
					resObj.put("promotionCode", itemArr);
				
				//guest.put("cart", new JSONObject(output));
				//jsonRes.put("data", guest);
					jsonRes.put("data", resObj);
				jsonRes.put(ServiceConstants.ERROR, new JSONArray());
			}

		} catch (IOException e) {
			logger.severe("IOExc in applyPromoCode:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());

		} catch (JSONException e) {
			logger.severe("JSONExc in applyPromoCode:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());

		} catch(Exception e) {
			logger.severe("Exception in applyPromoCode:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
					.getMessage());
		}

		return APIResponseBuilder.sendSuccessResponse(jsonRes);
	}
    @DELETE
	@Path("/store/{storeId}/delete_promotion_code/{promoCode}")
    @Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response deleteCart(@PathParam("storeId") String storeId,
			@PathParam("promoCode") String promoCode,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId) {

		logger.info(commerceToken);
		logger.info(wcTrustedToken);
		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String url1 = url + storeId + ServiceConstants.PROMO_ASSIGNED +"/"+promoCode;
		logger.severe(url1);
		JSONObject jsonRes = new JSONObject();
		JSONObject result = new JSONObject();
		try {
		String resonseString=	ApiAdapter.callDeleteAPI(url1, commerceToken, wcTrustedToken);
		JSONObject errorResponses = new JSONObject(resonseString);
		if(errorResponses.containsKey(ServiceConstants.ERRORS)) {

			if(((JSONObject)((JSONArray)errorResponses.get("errors")).get(0)).get("errorCode").toString().equals("CWXFR0210E")) {
				result = ApiAdapter.setError(errorResponses,426,"Unauthorized!!");
				return APIResponseBuilder.sendSuccessResponse(result);
			} else if(((JSONObject)((JSONArray)errorResponses.get("errors")).get(0)).get("errorCode").toString().equals("CMN1039E")) {
				result = ApiAdapter.setError(errorResponses,426,"Unauthorized!!");
				return APIResponseBuilder.sendSuccessResponse(result);
			}
			
			result = ApiAdapter.setError(errorResponses,503);
			return APIResponseBuilder.sendSuccessResponse(result);	
			
		}  else if(errorResponses.containsKey(ServiceConstants.ERROR)) {
			if(errorResponses.get("error").toString().contains("An invalid cookie was received for the user")) {
				result = ApiAdapter.setError(errorResponses,426,"Unauthorized!!");
				return APIResponseBuilder.sendSuccessResponse(result);
			} else {
				result = ApiAdapter.setError(errorResponses,425,"Something Went Wrong, Please try after sometime!!");
				return APIResponseBuilder.sendSuccessResponse(result);
			}
		} else {
		
			jsonRes.put("data", "Success");
			jsonRes.put(ServiceConstants.ERROR, new JSONArray());
			
		}
		
		
		}

		catch (IOException e) {
			logger.severe("IOExc in deletePromocode:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());
		} catch (JSONException e) {
			logger.severe("JSONExc in deletePromocode:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		} catch (Exception e) {
			logger.severe("Exc in deletePromocode:" + e);

			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		}
		
		return Response.ok(jsonRes.toString(), MediaType.APPLICATION_JSON)
				.build();

	}
}
