/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.retail.salesassist.api.cart;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.google.gson.Gson;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.APIResponseBuilder;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ApiAdapter;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ServiceConstants;
import com.ibm.mobilefirst.retail.salesassist.api.cart.services.AddressLookUpService;
import com.ibm.mobilefirst.retail.salesassist.api.cart.services.CustomerSearchService;
import com.ibm.mobilefirst.retail.salesassist.api.cart.services.SubmitOrderService;
import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class CartResource {

	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(CartResource.class.getName());

	// Define the server api to be able to perform server operations
	WLServerAPI api = WLServerAPIProvider.getWLServerAPI();

	@POST
	@Path("/store/{storeId}/addCartItem")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response addCartItem(@PathParam("storeId") String storeId,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId, JSONObject request) {

		JSONObject guest = new JSONObject();
		JSONObject guestResponse = new JSONObject();
		JSONObject jsonRes = new JSONObject();
		JSONObject jsonReq = new JSONObject();
		JSONObject item = new JSONObject();
		JSONArray itemArr = new JSONArray();
		JSONObject result = new JSONObject();
		String output = "";
		
		try {
		
			if ((commerceToken == null || commerceToken.isEmpty())
				&& (wcTrustedToken == null || wcTrustedToken.isEmpty())) {
				guestResponse = ApiAdapter.guestIdentity(storeId);
				commerceToken = guestResponse.getString(ServiceConstants.WCTOKEN);
				wcTrustedToken = guestResponse.getString(ServiceConstants.WCTRUSTEDTOKEN);
			} else {
				guestResponse.put(ServiceConstants.WCTOKEN, commerceToken);
				guestResponse.put(ServiceConstants.WCTRUSTEDTOKEN, wcTrustedToken);
				guestResponse.put(ServiceConstants.USERID, userId);
			}

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + "/cart?responseFormat=" + responseFormat;
		logger.severe(url1);

		if (request.get(ServiceConstants.CARTID).toString().trim().isEmpty()) {
			jsonReq.put(ServiceConstants.ORDERID, ".");
		} else {
			jsonReq.put(ServiceConstants.ORDERID, request.get(ServiceConstants.CARTID));
		}
		item.put(ServiceConstants.PRODUCTID, request.get("skuId"));
		item.put(ServiceConstants.QUANTITY, request.get("itemCount"));
		itemArr.put(item);
		jsonReq.put(ServiceConstants.ORDERITEM, itemArr);
		jsonReq.put("x_calculateOrder", "0");
		jsonReq.put("x_inventoryValidation", "true");
		String input = jsonReq.toString();

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
				
				result = ApiAdapter.setError(errorResponses,425);
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
				guest.put(ServiceConstants.GUEST, guestResponse);
				guest.put("cart", new JSONObject(output));
				jsonRes.put("data", guest);
				jsonRes.put(ServiceConstants.ERROR, new JSONArray());
			}

		} catch (IOException e) {
			logger.severe("IOExc in addCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());

		} catch (JSONException e) {
			logger.severe("JSONExc in addCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());

		} catch(Exception e) {
			logger.severe("Exception in addCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
					.getMessage());
		}

		return APIResponseBuilder.sendSuccessResponse(jsonRes);
	}

	@PUT
	@Path("/store/{storeId}/carts/updateitem")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response updateCartItem(@QueryParam("cartId") String cartId,
			@PathParam("storeId") String storeId,
			@QueryParam("orderItemId") String orderItemId,
			@QueryParam("itemCount") String itemCount,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId) {

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + ServiceConstants.CARTSELF
				+ responseFormat;
		logger.info(url1);

		JSONObject jsonObj = null;
		JSONObject jsonReq = new JSONObject();
		JSONObject item = new JSONObject();
		JSONObject output = new JSONObject();
		JSONObject guest = new JSONObject();
		JSONObject guestResponse = new JSONObject();
		JSONArray itemArr = new JSONArray();
		JSONArray itemArray = new JSONArray();
		JSONObject result = new JSONObject();
		JSONObject obj = new JSONObject();
		
		try {
			jsonReq.put(ServiceConstants.ORDERID, cartId);
			item.put(ServiceConstants.ORDERITEMID, orderItemId);
			item.put(ServiceConstants.QUANTITY, itemCount);
			itemArr.put(item);
			jsonReq.put(ServiceConstants.ORDERITEM, itemArr);
			String input = jsonReq.toString();
			logger.info(input);
			logger.info(commerceToken);
			logger.info(wcTrustedToken);
			
			guestResponse.put(ServiceConstants.WCTOKEN, commerceToken);
			guestResponse.put(ServiceConstants.WCTRUSTEDTOKEN, wcTrustedToken);
			guestResponse.put(ServiceConstants.USERID, userId);
			guest.put(ServiceConstants.GUEST, guestResponse);
			
			jsonObj = ApiAdapter.callPutAPI(url1, input, commerceToken,
					wcTrustedToken);

			if(jsonObj.containsKey(ServiceConstants.ERRORS)) {

				if(((JSONObject)((JSONArray)jsonObj.get("errors")).get(0)).get("errorCode").toString().equals("CWXFR0210E")) {
					result = ApiAdapter.setError(jsonObj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else if(((JSONObject)((JSONArray)jsonObj.get("errors")).get(0)).get("errorCode").toString().equals("CMN1039E")) {
					result = ApiAdapter.setError(jsonObj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
				
				result = ApiAdapter.setError(jsonObj,425);
				return APIResponseBuilder.sendSuccessResponse(result);	
				
			}  else if(jsonObj.containsKey(ServiceConstants.ERROR)) {
				if(jsonObj.get("error").toString().contains("An invalid cookie was received for the user")) {
					result = ApiAdapter.setError(jsonObj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else {
					result = ApiAdapter.setError(jsonObj,425,"Something Went Wrong, Please try after sometime!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
			} else {
				
				logger.info("updatecart response : "+jsonObj.toString());
				
				String url2 = url + storeId + ServiceConstants.CARTSELF
						+ responseFormat;
				logger.severe(url2);

				obj = ApiAdapter.callGetAPI(url2, commerceToken, wcTrustedToken);
				logger.info("getcart response : "+obj.toString());
				
				if(obj.containsKey(ServiceConstants.ERRORS)) {
					result = ApiAdapter.setError(jsonObj,425);
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
				Map m= new HashMap<>();
				
				Map displayMap= new HashMap<>();
					if(obj.has("adjustment"))
					{
						JSONArray itemstemp = obj.getJSONArray("adjustment");
						
					for (int k = 0; k < itemstemp.length(); k++) 
					{
						JSONObject itemJson1 = itemstemp.getJSONObject(k);
						String adjusDesc = itemJson1.getString("description");
						Float  discount =Float.valueOf(itemJson1.getString("amount"));
						String displayLevel = itemJson1.getString("displayLevel");
						Float  amount =Float.valueOf(itemJson1.getString("amount"));
						if(displayMap.containsKey(displayLevel))
						{
							Float value=	(Float) displayMap.get(displayLevel);
							amount = value +discount;
						}
						displayMap.put(displayLevel, amount);
						
							if(m.containsKey(adjusDesc.toUpperCase()))
							{
								Float abc=	(Float) m.get(adjusDesc.toUpperCase());
								discount = abc +discount;
							}
							m.put(adjusDesc.toUpperCase(), discount);
					}}
					
					JSONArray promoArr = new JSONArray();
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
										String desc=assPromoObj.getString("description");
										if( desc==null ||desc.isEmpty()  )
										{
											promoObj.put("description", "");
										}
										else
										{
											desc.toUpperCase();
										
										
											if(m.containsKey(desc.toUpperCase()))
											{
												Float temp = (Float) m.get(desc.toUpperCase());
												
													if(temp!= null)
													{
													
													if(temp<0)
													{
														temp= temp*(-1);
													}
													
													 DecimalFormat twoDForm = new DecimalFormat("0.00");
													    String dd= twoDForm.format(temp);
													
												String descriptionWithDiscount = desc +" (£"+dd +")";
												promoObj.put("description", descriptionWithDiscount);
												}}
										
											}}
										}
									
							}
							if(!promoObj.containsKey("description"))
							{
								promoObj.put("description", "");
							}
							promoArr.add(promoObj);
							
						}
					jsonObj.put("promotionCode", promoArr);}
			
			
		
				JSONArray itemss = obj.getJSONArray("orderItem");
				JSONObject orderItemIdObject = (JSONObject) jsonObj.getJSONArray("orderItem").get(0);
				
				for (int i = 0; i < itemss.length(); i++) {
					JSONObject items = new JSONObject();
					JSONObject itemJson = itemss.getJSONObject(i);
					if(orderItemIdObject.get("orderItemId").toString().equals(itemJson.getString("orderItemId"))) {
					
						items.put("skuId", itemJson.getString("productId"));
						items.put("price",
								Float.parseFloat(itemJson.getString("orderItemPrice")));
						items.put("orderItemId", itemJson.getString("orderItemId"));
						items.put("quantity", itemJson.getString("quantity"));
						items.put("status", itemJson.getString("orderItemStatus"));
						if(itemJson.containsKey("adjustment")) {
							items.put("promotion_name", ((JSONObject)((JSONArray)itemJson.get("adjustment")).get(0)).get("description"));
						} else {
							items.put("promotion_name", "");
						}
						
						
					if(i==0)
					{
						if(obj.has("adjustment"))
						{
					String order1 = "ORDER";
					String orderitem1 ="orderitem";
					for ( Object key : displayMap.keySet() ) {
						
						String keyStr = String.valueOf(key);
					   Float value =  (Float) displayMap.get(keyStr);
					   DecimalFormat twoDForm = new DecimalFormat("0.00");
							if(keyStr.equalsIgnoreCase(order1))
							{
							items.put("promotionDiscount", twoDForm.format(value));
							}
						
							
							if(keyStr.equalsIgnoreCase(orderitem1))
							{
								items.put("discount", twoDForm.format(value));
							}
							
					}	
					if(!items.containsKey("promotionDiscount"))
					{
						items.put("promotionDiscount", "");
					}
					if(!items.containsKey("discount"))
					{
						items.put("discount", "");
					}
						
						}
						else
						{
							items.put("discount", "");
							items.put("promotionDiscount", "");
						}
					
				} 
					
				else {
					items.put("discount", "");
						items.put("promotionDiscount", "");
					}
						
							
						
											
						itemArray.add(items);
					}					
				}
				
				jsonObj.put("orderItem", itemArray);
				
				logger.info(jsonObj.toString());
				
				guest.put("cart", jsonObj);
	
				output.put("data", guest);
				output.put(ServiceConstants.ERROR, new JSONArray());
			}

		} catch (IOException e) {
			logger.severe("IOExc in updateCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());

		} catch (JSONException e) {
			logger.severe("JSONExc in updateCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());

		} catch(Exception e) {
			logger.severe("Exception in updateCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
					.getMessage());
		}
		
		return APIResponseBuilder.sendSuccessResponse(output);

	}

	@PUT
	@Path("/store/{storeId}/cart/removeItem")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response removeCartItem(@QueryParam("cartId") String cartId,
			@PathParam("storeId") String storeId,
			@QueryParam("orderItemId") String orderItemId,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId) {
		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + "/cart/@self?responseFormat="
				+ responseFormat;
		logger.severe(url1);

		JSONObject jsonObj = null;
		JSONObject jsonReq = new JSONObject();
		JSONObject item = new JSONObject();
		JSONObject output = new JSONObject();
		JSONObject guest = new JSONObject();
		JSONObject guestResponse = new JSONObject();
		JSONArray itemArr = new JSONArray();
		
		JSONObject result = new JSONObject();
		
		try {
			jsonReq.put(ServiceConstants.ORDERID, cartId);
			item.put(ServiceConstants.ORDERITEMID, orderItemId);
			item.put(ServiceConstants.QUANTITY, "0.0");
			itemArr.put(item);
			jsonReq.put(ServiceConstants.ORDERITEM, itemArr);
			String input = jsonReq.toString();
			logger.info(input);
			logger.info(commerceToken);
			logger.info(wcTrustedToken);
			
			guestResponse.put(ServiceConstants.WCTOKEN, commerceToken);
			guestResponse.put(ServiceConstants.WCTRUSTEDTOKEN, wcTrustedToken);
			guestResponse.put(ServiceConstants.USERID, userId);
			guest.put("guest", guestResponse);
			logger.severe(input);
			
			jsonObj = ApiAdapter.callPutAPI(url1, input, commerceToken,
					wcTrustedToken);
			
			if(jsonObj.containsKey(ServiceConstants.ERRORS)) {
				if(((JSONObject)((JSONArray)jsonObj.get("errors")).get(0)).get("errorCode").toString().equals("CWXFR0210E")) {
					result = ApiAdapter.setError(jsonObj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else if(((JSONObject)((JSONArray)jsonObj.get("errors")).get(0)).get("errorCode").toString().equals("CMN1039E")) {
					result = ApiAdapter.setError(jsonObj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
				
				result = ApiAdapter.setError(jsonObj,425);
				return APIResponseBuilder.sendSuccessResponse(result);	
				
			}  else if(jsonObj.containsKey(ServiceConstants.ERROR)) {
				if(jsonObj.get("error").toString().contains("An invalid cookie was received for the user")) {
					result = ApiAdapter.setError(jsonObj,426,"Unauthorized!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				} else {
					result = ApiAdapter.setError(jsonObj,425,"Something Went Wrong, Please try after sometime!!");
					return APIResponseBuilder.sendSuccessResponse(result);
				}
			} else {
				logger.severe(jsonObj.toString());
				guest.put("cart", jsonObj);
				output.put("data", guest);
				output.put(ServiceConstants.ERROR, new JSONObject());
			}
		} catch (IOException e) {
			logger.severe("IOExc in removeCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());

		} catch (JSONException e) {
			logger.severe("JSONExc in removeCartItem:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());

		} catch (Exception e) {
			logger.severe("Exc in removeCartItem:" + e);

			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		}

		return APIResponseBuilder.sendSuccessResponse(output);
		

	}

	@DELETE
	@Path("/store/{storeId}/cart/{cartId}/deletecart")
	@OAuthSecurity(enabled = false)
	public Response deleteCart(@PathParam("storeId") String storeId,
			@PathParam("cartId") String cartId,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId) {

		logger.info(commerceToken);
		logger.info(wcTrustedToken);
		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String url1 = url + storeId + "/cart/@self";
		logger.severe(url1);
		JSONObject jsonRes = new JSONObject();

		try {
			ApiAdapter.callDeleteAPI(url1, commerceToken, wcTrustedToken);
			jsonRes.put("data", "Success");
			jsonRes.put(ServiceConstants.ERROR, new JSONArray());
		}

		catch (IOException e) {
			logger.severe("IOExc in deleteCart:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());
		} catch (JSONException e) {
			logger.severe("JSONExc in deleteCart:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		} catch (Exception e) {
			logger.severe("Exc in deleteCart:" + e);

			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		}
		
		return Response.ok(jsonRes.toString(), MediaType.APPLICATION_JSON)
				.build();

	}

	@GET
	@Path("/store/{storeId}/cart/@self")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response getCart(@PathParam("storeId") String storeId,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId) throws Exception {

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + ServiceConstants.CARTSELF
				+ responseFormat;
		logger.severe(url1);
		JSONObject obj = new JSONObject();
		JSONObject resObj = new JSONObject();
		JSONObject order = new JSONObject();
		JSONObject guest = new JSONObject();
		JSONObject guestResponse = new JSONObject();
		JSONArray itemArr = new JSONArray();
		JSONArray orderArr = new JSONArray();
		
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
				
				result = ApiAdapter.setError(obj,425);
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
			if (obj.has(ServiceConstants.ORDERSTATUS)) {
				resObj.put(ServiceConstants.ORDERSTATUS, obj.getString(ServiceConstants.ORDERSTATUS));
			}
			if (obj.has("storeUniqueID")) {
				resObj.put("storeId", obj.getString("storeUniqueID"));
			}
			if (obj.has("buyerId")) {
				resObj.put("customerId", obj.getString("buyerId"));
			}
			if (obj.has("grandTotal")) {
				resObj.put("orderTotal", obj.getString("grandTotal"));
			}
			if (obj.has("totalSalesTax")) {
				resObj.put("orderTax", obj.getString("totalSalesTax"));
			}
			if (obj.has("totalShippingCharge")) {
				resObj.put("orderShipping",
						obj.getString("totalShippingCharge"));
			}
			
			Map m= new HashMap<>();
			
		Map displayMap= new HashMap<>();
			if(obj.has("adjustment"))
			{
				JSONArray itemstemp = obj.getJSONArray("adjustment");
				
			for (int k = 0; k < itemstemp.length(); k++) 
			{
				JSONObject itemJson1 = itemstemp.getJSONObject(k);
				String adjusDesc = itemJson1.getString("description");
				Float  discount =Float.valueOf(itemJson1.getString("amount"));
				String displayLevel = itemJson1.getString("displayLevel");
				Float  amount =Float.valueOf(itemJson1.getString("amount"));
				if(displayMap.containsKey(displayLevel))
				{
					Float value=	(Float) displayMap.get(displayLevel);
					amount = value +discount;
				}
				displayMap.put(displayLevel, amount);
				
					if(m.containsKey(adjusDesc.toUpperCase()))
					{
						Float abc=	(Float) m.get(adjusDesc.toUpperCase());
						discount = abc +discount;
					}
					m.put(adjusDesc.toUpperCase(), discount);
			}}
			
			JSONArray promoArr = new JSONArray();
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
								String desc=assPromoObj.getString("description");
								if( desc==null ||desc.isEmpty()  )
								{
									promoObj.put("description", "");
								}
								else
								{
									desc.toUpperCase();
								
								
									if(m.containsKey(desc.toUpperCase()))
									{
										Float temp = (Float) m.get(desc.toUpperCase());
										
											if(temp!= null)
											{
											
											if(temp<0)
											{
												temp= temp*(-1);
											}
											
											 DecimalFormat twoDForm = new DecimalFormat("0.00");
											    String dd= twoDForm.format(temp);
											
										String descriptionWithDiscount = desc +" (£"+dd +")";
										promoObj.put("description", descriptionWithDiscount);
										}}
								
									}}
								}
							
					}
					if(!promoObj.containsKey("description"))
					{
						promoObj.put("description", "");
					}
					promoArr.add(promoObj);
					
				}
			resObj.put("promotionCode", promoArr);}
	
	
	
	
			JSONArray items = obj.getJSONArray("orderItem");
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = new JSONObject();
				JSONObject itemJson = items.getJSONObject(i);
				
			if(i==0)
			{
				if(obj.has("adjustment"))
				{
			String order1 = "ORDER";
			String orderitem1 ="orderitem";
			for ( Object key : displayMap.keySet() ) {
				
				String keyStr = String.valueOf(key);
			   Float value =  (Float) displayMap.get(keyStr);
			   DecimalFormat twoDForm = new DecimalFormat("0.00");
					if(keyStr.equalsIgnoreCase(order1))
					{
					item.put("promotionDiscount", twoDForm.format(value));
					}
				
					
					if(keyStr.equalsIgnoreCase(orderitem1))
					{
						item.put("discount", twoDForm.format(value));
					}
					
			}	
			if(!item.containsKey("promotionDiscount"))
			{
				item.put("promotionDiscount", "");
			}
			if(!item.containsKey("discount"))
			{
				item.put("discount", "");
			}
				
				}
				else
				{
					item.put("discount", "");
					item.put("promotionDiscount", "");
				}
			
		} 
			
		else {
				item.put("discount", "");
				item.put("promotionDiscount", "");
			}
				
				item.put("skuId", itemJson.getString("productId"));
				item.put("price",
						Float.parseFloat(itemJson.getString("orderItemPrice")));
				item.put("orderItemId", itemJson.getString("orderItemId"));
				item.put("quantity", itemJson.getString("quantity"));
				item.put("status", itemJson.getString("orderItemStatus"));
				if(itemJson.containsKey("adjustment")) {
					item.put("promotion_name", ((JSONObject)((JSONArray)itemJson.get("adjustment")).get(0)).get("description"));
				} else {
					item.put("promotion_name", "");
				}

								
				
				itemArr.add(item);
			}
			resObj.put("items", itemArr);
			resObj.put("orderDate", obj.getString("lastUpdateDate"));

			if (obj.has(ServiceConstants.ORDERSTATUS)) {
				resObj.put("orderStatus", obj.getString("orderStatus"));
			}
			
			orderArr.add(resObj);
			guestResponse.put(ServiceConstants.WCTOKEN, commerceToken);
			guestResponse.put(ServiceConstants.WCTRUSTEDTOKEN, wcTrustedToken);
			guestResponse.put(ServiceConstants.USERID, userId);
			
			guest.put("guest", guestResponse);
			guest.put("cart", orderArr);
			order.put("data", guest);
			order.put(ServiceConstants.ERROR, new JSONArray());

		} catch (IOException e) {
			logger.severe("IOExc in getCart:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());
		} catch (JSONException e) {
			logger.severe("JSONExc in getCart:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		} catch (Exception e) {
			logger.severe("Exc in getCart:" + e);

			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					e.getMessage(),
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		}
		
		return Response.ok(order.toString(), MediaType.APPLICATION_JSON)
				.build();
	}

	@POST
	@Path("/store/{storeId}/guestidentity")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response guestIdentity(@PathParam("storeId") String storeId) {

		String responseString = new String();
		
		String url = AccessProperties.configFile.getProperty("urlssl");

		String url1 = url + storeId + "/guestidentity";
		logger.severe(url1);
		JSONObject response = new JSONObject();

		try {
			responseString = ApiAdapter.callPostAPI(url1);
			logger.severe(responseString);

			if (responseString != null) {
				Map jsonJavaRootObject = new Gson().fromJson(responseString,
						Map.class);

				response.put("WCToken",
						(String) (jsonJavaRootObject.get("WCToken")));
				response.put("WCTrustedToken",
						(String) (jsonJavaRootObject.get("WCTrustedToken")));
				response.put("userId",
						(String) (jsonJavaRootObject.get("userId")));
			}

		} catch (IOException e) {
			logger.severe("IOExc in guestIdentity:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					"IOExc in guestIdentity:",
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
							.getMessage());

		} catch (JSONException e) {
			logger.severe("JSONExc in guestIdentity:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					"JSONExc in guestIdentity:",
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());

		} catch (Exception e) {
			logger.severe("Exc in guestIdentity:" + e);

			return APIResponseBuilder.sendFailResponse(new JSONObject(),
					"JSONExc in getCart:",
					ServiceConstants.ERROR_CODE_IMPROPERJSON, e.getMessage());
		}

		return APIResponseBuilder.sendSuccessResponse(response);
	}

	@GET
	@Path("/store/{storeId}/cart/@self/getShippingModes")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public String getShippingModes(@PathParam("storeId") String storeId,String shipmodeId)  {

		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + "/cart/shipping_modes?responseFormat="
				+ responseFormat;
		logger.severe(url1);
		JSONObject obj = new JSONObject();
		JSONObject shipmentInfo = new JSONObject();
		JSONArray shipmentInfoArr = new JSONArray();
		try {
			obj = ApiAdapter.callGetAPI(url1);
			//logger.severe(obj.toString());
			if (obj.has("usableShippingMode")) {
				shipmentInfoArr = obj.getJSONArray("usableShippingMode");
				
				for(int i=0;i<shipmentInfoArr.size();i++) {
					JSONObject shippingObject = shipmentInfoArr.getJSONObject(i);
					
						if(shipmodeId.equals("10001") && 
								shippingObject.getString("shipModeCode").equals("PickupInStore")) {
							shipmodeId = shippingObject.getString("shipModeId");
						} else if(shipmodeId.equals("10601") && 
								shippingObject.getString("shipModeCode").equals("Standard48hour")) {
							shipmodeId = shippingObject.getString("shipModeId");
						}
				}
			}
			
			logger.severe("shipmodeId from API"+shipmodeId);
		} catch (IOException e) {
			logger.severe("IOExc in getShippingModes:" + e);
		} catch (JSONException e) {
			logger.severe("JSONExc in getShippingModes:" + e);
		} catch (Exception e) {
			logger.severe("Exce in getShippingModes:" + e);
		}
		return shipmodeId;
	}
	
	@GET
	@Path("/store/{storeId}/cart/@self/contact/{nickName}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public JSONObject getPersonContact(@PathParam("storeId") String storeId,
			@PathParam("nickName") String nickName,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("WCToken") String commerceToken)  {

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		
		String url1 = url + storeId + "/person/@self/contact/"+nickName;
		
		logger.severe("getPersonContact url:" + url1);
		JSONObject obj = new JSONObject();
		
		try {
			obj = ApiAdapter.callGetAPI(url1,commerceToken,wcTrustedToken);
		
		} catch (IOException e) {
			logger.severe("IOExc in getPersonContact:" + e);
		} catch (JSONException e) {
			logger.severe("JSONExc in getPersonContact:" + e);
		} catch (Exception e) {
			logger.severe("Exce in getPersonContact:" + e);
		}
		return obj;
	}

	@PUT
	@Path("/store/{storeId}/cart/@self/updateShippingMode")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response updateShippingMode(@PathParam("storeId") String storeId) {

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId
				+ "/cart/@self/shipping_modes?responseFormat=" + responseFormat;
		logger.severe(url1);
		JSONObject obj = new JSONObject();
		JSONObject shipmentInfo = new JSONObject();
		JSONArray shipmentInfoArr = new JSONArray();
		try {
			obj = ApiAdapter.callGetAPI(url1);
			logger.severe(obj.toString());
			if (obj.has("usableShippingMode")) {
				shipmentInfoArr = obj.getJSONArray("usableShippingMode");
			}
			shipmentInfo.put("data", shipmentInfoArr);

		} catch (IOException e) {
			logger.severe("IOExc in updateShippingMode:" + e);
		} catch (JSONException e) {
			logger.severe("JSONExc in updateShippingMode:" + e);
		} catch (Exception e) {
			logger.severe("Exce in updateShippingMode:" + e);
		}
		return Response.ok(shipmentInfo.toString(), MediaType.APPLICATION_JSON)
				.build();
	}
	
	
//	@POST
//    @Path("/store/{storeId}/cart/contact")
//    @Produces(MediaType.APPLICATION_JSON)
//    @OAuthSecurity(enabled = false)
//    public JSONObject addPersonContact(
//                    @PathParam("storeId") String storeId, JSONObject request,
//                    @HeaderParam("WCToken") String commerceToken,
//                    @HeaderParam("WCTrustedToken") String wcTrustedToken,
//                    @HeaderParam("userId") String userId) {
//
//            logger.severe(wcTrustedToken);
//            logger.severe(commerceToken);
//    		JSONObject guest = new JSONObject();
//    		JSONObject guestResponse = new JSONObject();
//
//           
//            
//            JSONObject response = new JSONObject();
//            JSONObject result = new JSONObject();
//            String url = AccessProperties.configFile
//                            .getProperty(ServiceConstants.URLSSL);
//            String responseFormat = AccessProperties.configFile
//                            .getProperty(ServiceConstants.RESPONSE_FORMATE);
//            String url1 = url + storeId + "/person/@self/contact?responseFormat="
//                            + responseFormat;
//            logger.severe("addcontact:"+url1);
//            
//            String output = "";
//
//
//            try {
//            	
//            	 if ((commerceToken == null || commerceToken.isEmpty())
//                         && (wcTrustedToken == null || wcTrustedToken.isEmpty())) {
//                         guestResponse = ApiAdapter.guestIdentity(storeId);
//                         commerceToken = guestResponse.getString(ServiceConstants.WCTOKEN);
//                         wcTrustedToken = guestResponse.getString(ServiceConstants.WCTRUSTEDTOKEN);
//                 } else {
//                         guestResponse.put(ServiceConstants.WCTOKEN, commerceToken);
//                         guestResponse.put(ServiceConstants.WCTRUSTEDTOKEN, wcTrustedToken);
//                         guestResponse.put(ServiceConstants.USERID, userId);
//         }
//
//                    String input = request.toString();
//                    logger.severe(input);
//                    output = ApiAdapter.callPostAPI(url1, input, commerceToken,
//                                    wcTrustedToken);
//                    
//                    logger.info("addpersonContact:"+output);
//                    response = new JSONObject(output);
//
//                    if(response.containsKey(ServiceConstants.ERRORS)) {
//                            result = ApiAdapter.setError(response);
//
//                    } else {
//                    	response.put("guestResponse",guestResponse);
//                            result.put("data",response);
//                            result.put("error",new JSONArray());
//                    }
//
//            } catch (IOException e) {
//                    logger.severe("IOExc in addPersonContact:" + e);
//            } catch (JSONException e) {
//                    logger.severe("JSONExc in addPersonContact:" + e);
//            } catch (Exception e) {
//                    logger.severe("Exc in addPersonContact:" + e);
//            }
//
//            return result;
//    }

//	@PUT
//	@Path("/store/{storeId}/cart/contact/{nickName}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@OAuthSecurity(enabled = false)
//	public JSONObject updatePersonContact(@PathParam("storeId") String storeId,
//			@PathParam("nickName") String nickName, JSONObject request,
//			@HeaderParam("WCToken") String commerceToken,
//			@HeaderParam("WCTrustedToken") String wcTrustedToken,
//			@HeaderParam("userId") String userId) {
//
//		
//		String url = AccessProperties.configFile
//				.getProperty(ServiceConstants.URLSSL);
//		String responseFormat = AccessProperties.configFile
//				.getProperty(ServiceConstants.RESPONSE_FORMATE);
//		String url1 = url + storeId + "/person/@self/contact/" + nickName
//				+ "?responseFormat=" + responseFormat;
//		logger.severe(url1);
//		JSONObject output = new JSONObject();
//		JSONObject result = new JSONObject();
//		JSONObject guest = new JSONObject();
//		JSONObject guestResponse = new JSONObject();
//		
//		try {
//			
//			
//			if ((commerceToken == null || commerceToken.isEmpty())
//					&& (wcTrustedToken == null || wcTrustedToken.isEmpty())) {
//					guestResponse = ApiAdapter.guestIdentity(storeId);
//					
//						commerceToken = guestResponse.getString(ServiceConstants.WCTOKEN);
//
//					wcTrustedToken = guestResponse.getString(ServiceConstants.WCTRUSTEDTOKEN);
//				} else {
//					guestResponse.put(ServiceConstants.WCTOKEN, commerceToken);
//					guestResponse.put(ServiceConstants.WCTRUSTEDTOKEN, wcTrustedToken);
//					guestResponse.put(ServiceConstants.USERID, userId);
//				}
//			
//			String input = request.toString();
//			logger.severe(input);
//			output = ApiAdapter
//					.callPutAPI(url1, input, commerceToken, wcTrustedToken);
//			logger.severe("updatePerson:"+output.toString());
//			
//			
//			if(output.containsKey(ServiceConstants.ERRORS)) {
//				result = ApiAdapter.setError(output);
//				
//			} else {
////				result.put("guestResponse",guestResponse);
//				result.put("data",output);
//				result.put("error",new JSONArray());
//			}
//		} catch (IOException e) {
//			logger.severe("IOExc in updatePersonContact:" + e);		
//		} catch (JSONException e) {
//			logger.severe("JSONExc in updatePersonContact:" + e);
//		} catch (Exception e) {
//			logger.severe("Exc in updatePersonContact:" + e);
//		}
//		return result;
//	}
	
	@POST
    @Path("/store/{storeId}/cart/contact")
    @Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled = false)
    public JSONObject addPersonContact(
                    @PathParam("storeId") String storeId, JSONObject request,
                    @HeaderParam("WCToken") String commerceToken,
                    @HeaderParam("WCTrustedToken") String wcTrustedToken,
                    @HeaderParam("userId") String userId) {

		logger.severe(wcTrustedToken);
		logger.severe(commerceToken);
		JSONObject guestResponse = new JSONObject();

		
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);

		String output = "";
		String addressId = "";
		String contactUserId = "";
		String nickName = "";
		JSONObject updateContact = new JSONObject();
		JSONObject finalOutput = new JSONObject();
		
		try {
			// phone1 from app should be mapped to phone2 in WCS
			logger.info("Add Contact Request From App == " + request);
//			if (request.containsKey("phone1")) {
//				logger.severe("Phone1");
//				logger.severe((String) request.get("phone1"));
//				//request.put("phone1", request.get("phone1"));
//				request.put("phone2", request.get("phone1"));
//				//request.remove("phone1");
//			}
			
			request.put("addressType", "ShippingAndBilling");
			
			if ((commerceToken == null || commerceToken.isEmpty())
					&& (wcTrustedToken == null || wcTrustedToken.isEmpty())) {
				guestResponse = ApiAdapter.guestIdentity(storeId);
				commerceToken = guestResponse.getString(ServiceConstants.WCTOKEN);
				wcTrustedToken = guestResponse.getString(ServiceConstants.WCTRUSTEDTOKEN);
			} else {
				guestResponse.put(ServiceConstants.WCTOKEN, commerceToken);
				guestResponse.put(ServiceConstants.WCTRUSTEDTOKEN, wcTrustedToken);
				guestResponse.put(ServiceConstants.USERID, userId);
			}            	 	

			if(request.containsKey("addressId"))
				request.remove("addressId");
			
			//logger.severe("nickname from front end:"+request.getString("nickName"));
			if(request.getString("nickName")!= null && (!request.getString("nickName").isEmpty())) {
				
				if(request.get("addressType")!=null &&request.get("addressType").toString().isEmpty())
					request.remove("addressType");
				if(request.get("zipCode")!=null &&request.get("zipCode").toString().isEmpty())
					request.remove("zipCode");
				if(request.get("city")!=null &&request.get("city").toString().isEmpty())
					request.remove("city");
				if(request.get("country")!=null &&request.get("country").toString().isEmpty())
					request.remove("country");
				
				if(request.get("addressLine")!=null) {
					if(request.getJSONArray("addressLine").get(0).toString().isEmpty())
							request.remove("addressLine");
				}
				
			//	logger.severe("update address at the second time for :"+request.getString("nickName"));
				nickName = request.getString("nickName");						
				updateContact = this.updatePersonContact(storeId, nickName, request, commerceToken, wcTrustedToken, userId);
			//	logger.severe("updateContact o/p:" + updateContact.toString());
				if(updateContact != null && (!updateContact.isEmpty()))
					addressId = updateContact.getString("addressId");

			} else {
				String url1 = url + storeId + "/person/@self/contact?responseFormat="
						+ responseFormat;
				logger.severe("addcontact url:"+url1);
				
				String lastName = request.getString("lastName");
				int randomNum = new Random().nextInt((999 - 100) + 1) + 100;
				nickName = lastName+randomNum;
				nickName = nickName.replaceAll("\\s+","");
			//	logger.severe("nickName after random num generation:" + nickName);
				request.put("nickName",nickName);

				logger.severe("INPUT"+request);
				
				
				if(request.get("addressType")!=null &&request.get("addressType").toString().isEmpty())
					request.remove("addressType");
				if(request.get("zipCode")!=null &&request.get("zipCode").toString().isEmpty())
					request.remove("zipCode");
				if(request.get("city")!=null &&request.get("city").toString().isEmpty())
					request.remove("city");
				if(request.get("country")!=null &&request.get("country").toString().isEmpty())
					request.remove("country");
				
				if(request.get("addressLine")!=null) {
					if(request.getJSONArray("addressLine").get(0).toString().isEmpty())
							request.remove("addressLine");
				}
				
				logger.severe("INPUT to verify update to person contact/addressLine:"+request);
				
				output = ApiAdapter.callPostAPI(url1, request.toString(), commerceToken,
						wcTrustedToken);
				logger.info("addpersonContact:"+output);
				response = new JSONObject(output);
				contactUserId = response.getString("userId");
						
				if(request.containsKey("addressLine"))
				{
					updateContact = this.updatePersonContact(storeId, nickName, request, commerceToken, wcTrustedToken, userId);

					logger.severe("updateContact o/p:" + updateContact.toString());
					if(updateContact != null && (!updateContact.isEmpty()))
						addressId = updateContact.getString("addressId");
				}
			}

			if(response.containsKey(ServiceConstants.ERRORS) || updateContact.containsKey(ServiceConstants.ERRORS)) {
				if(((JSONObject)((JSONArray)response.get("errors")).get(0)).get("errorCode").toString().equals("CWXFR0210E")) {
					result = ApiAdapter.setError(response,426,"Unauthorized!!");
				} else if(((JSONObject)((JSONArray)response.get("errors")).get(0)).get("errorCode").toString().equals("CMN1039E")) {
					result = ApiAdapter.setError(response,426,"Unauthorized!!");
				}
				
				result = ApiAdapter.setError(response,425);
				
			}  else if(response.containsKey(ServiceConstants.ERROR)) {
				if(response.get("error").toString().contains("An invalid cookie was received for the user")) {
					result = ApiAdapter.setError(response,426,"Unauthorized!!");
					
				} else {
					result = ApiAdapter.setError(response,425,"Something Went Wrong, Please try after sometime!!");
				}
			} else {
				result.put("guestResponse",guestResponse);
				finalOutput.put("addressId", addressId);
				finalOutput.put("nickname", request.getString("nickName"));
				finalOutput.put("contactUserId",contactUserId);
				result.put("data", finalOutput);
				result.put("error",new JSONArray());
			}	
			
			logger.severe("Final output from addpersonContact:" +result.toString());
			
		} catch (IOException e) {
			logger.severe("IOExc in addPersonContact:" + e);
		} catch (JSONException e) {
			logger.severe("JSONExc in addPersonContact:" + e);
		} catch (Exception e) {
			logger.severe("Exc in addPersonContact:" + e);
		}

		return result;
	}


	@PUT
	@Path("/store/{storeId}/cart/contact/{nickName}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public JSONObject updatePersonContact(@PathParam("storeId") String storeId,
			@PathParam("nickName") String nickName, JSONObject request,
			@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken,
			@HeaderParam("userId") String userId) {

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		String responseFormat = AccessProperties.configFile
				.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url1 = url + storeId + "/person/@self/contact/" + nickName
				+ "?responseFormat=" + responseFormat;
		logger.severe(url1);
		JSONObject output = new JSONObject();
		
		try {
			// phone1 from app should be mapped to phone2 in WCS
//			if (request.containsKey("phone1")) {
//				request.put("phone2", request.get("phone1"));
//				//request.remove("phone1");
//			}
			
			String input = request.toString();
			logger.severe(input);
			output = ApiAdapter
					.callPutAPI(url1, input, commerceToken, wcTrustedToken);
		//	logger.severe("updatePerson:"+output.toString());

		} catch (IOException e) {
			logger.severe("IOExc in updatePersonContact:" + e);		
		} catch (JSONException e) {
			logger.severe("JSONExc in updatePersonContact:" + e);
		} catch (Exception e) {
			logger.severe("Exc in updatePersonContact:" + e);
		}
		return output;
	}

//	@PUT
//	@Path("/store/{storeId}/cart/@self/shipping_info/checkout")
//	@Produces(MediaType.APPLICATION_JSON)
//	@OAuthSecurity(enabled = false)
//	public Response updateOrderShippingInfo(
//			@PathParam("storeId") String storeId, JSONObject request,
//			@HeaderParam("WCToken") String commerceToken,
//			@HeaderParam("WCTrustedToken") String wcTrustedToken,
//			@HeaderParam("userId") String userId) {
//
//		logger.severe("into the checkout API **********");
//		
//		String url = AccessProperties.configFile
//				.getProperty(ServiceConstants.URLSSL);
//		String responseFormat = AccessProperties.configFile
//				.getProperty(ServiceConstants.RESPONSE_FORMATE);
//		String url6 = url + storeId
//				+ "/cart/@self/shipping_info?responseFormat=" + responseFormat;
//		logger.severe("shipping_info:"+url6);
//		JSONObject output = new JSONObject();
//		//String checkoutResponse = "";
//		JSONObject preCheckOut = new JSONObject();
//		JSONObject shippingRequest = new JSONObject();
//		JSONObject finalResponse = new JSONObject();
//		JSONArray error = new JSONArray();
//		JSONObject result = new JSONObject();
//		JSONObject errors = new JSONObject();
//		
//		JSONObject updatePersonResponse = new JSONObject();
//	
//		try {
//
//			JSONObject request1 = (JSONObject) request.clone();
//			logger.severe("clone request:"+request1.toString());
//		
//
//			
//			if(request1.containsKey("addressId")) {
//				updatePersonResponse.put("addressId",request1.get("addressId"));	
//			} else {
//				
//				request1.put("addressType", "ShippingAndBilling");
//				request1.remove("shipModeId");
//				request1.remove("orderNumber");
//				request1.remove("title");
//				request1.remove("storeId");
//				request1.remove("timeAtAddress");
//				request1.remove("cartItems");
//				
//				JSONObject personDetails = getPersonContact(storeId, request1.getString("nickName"), wcTrustedToken, commerceToken);
//				
//				if(personDetails.containsKey("addressId")) {
//					updatePersonResponse.put("addressId",personDetails.get("addressId"));
//				
//				} else {
//					int randomNum = new Random().nextInt((999 - 100) + 1) + 100;
//					String nickanme = request1.getString("nickName")+randomNum;
//					nickanme = nickanme.replaceAll("\\s+","");
//					
//					request1.put("firstName",request1.getString("firstName").replaceAll("\\s+",""));
//					request1.put("lastName",request1.getString("lastName").replaceAll("\\s+",""));
//					request1.put("nickName",nickanme);
//					
//					logger.severe("after proper output:"+request1);
//					JSONObject response = addPersonContact(storeId,
//							request1, commerceToken, wcTrustedToken, userId);
//					
//					updatePersonResponse = updatePersonContact(storeId,
//							nickanme, request1, commerceToken,
//							wcTrustedToken, userId);
//					
//					updatePersonResponse = (JSONObject) updatePersonResponse.get("data");
//				}
//			}
//
//			if(updatePersonResponse.containsKey("addressId"))  {
//				shippingRequest.put("x_calculationUsage", "-1,-2,-3,-4,-5,-6,-7");
//				shippingRequest.put("shipModeId", request.get("shipModeId"));
//				shippingRequest.put("addressId",
//						updatePersonResponse.get("addressId"));
//	
//				String input = shippingRequest.toString();
//				logger.severe("shipingInfoInput:"+input);
//				output = ApiAdapter
//						.callPutAPI(url6, input, commerceToken, wcTrustedToken);
//				
//				logger.severe("shipingInfoHandleroutput:"+output.toString());
//				
//				if(output.containsKey(ServiceConstants.ORDERID)) {	
//				
//					String orderId = output.get(ServiceConstants.ORDERID).toString();
//					logger.severe("orderId before checkout:..:"+orderId);
//					
//					String url2 = url + storeId
//							+ "/cart/@self/precheckout?responseFormat="
//							+ responseFormat;
//					logger.severe("precheckout:"+url2);
//					preCheckOut.put(ServiceConstants.ORDERID, orderId);
//					preCheckOut.put("channelId", "2");
//					String preChecoutInput = preCheckOut.toString();
//					output = ApiAdapter.callPutAPIForCheckOut(url2, preChecoutInput,
//							commerceToken, wcTrustedToken);
//					logger.severe("precheckout output:"+output.toString());
//					
//					/*Need to remove this code after addressId fix*/
//					getOrderDetails(storeId, orderId, commerceToken, wcTrustedToken);
//					/*Need to remove this code after addressId fix*/
//					
////					String url3 = url + storeId + "/cart/@self/checkout";
////					logger.severe(url3);
////					checkoutResponse = ApiAdapter.callPostAPI(url3, preChecoutInput,
////							commerceToken, wcTrustedToken);
//		
//					//logger.info("checkout output:"+checkoutResponse);
////					if(output.isEmpty()) 
////						output.put(ServiceConstants.ORDERID,orderId);
//					finalResponse.put("data",output);
//					finalResponse.put("error",new JSONArray());
//					//finalResponse = output;
//					
//					if(output.containsKey("orderId")) {	
//						logger.severe("ORDER ID BEFOR ENTERING CHECOUT API"+output.get("orderId").toString());
//						finalResponse = submitOrder(request,output.get("orderId").toString());
//						
//					}
//					
//				} else {
//					if(output.containsKey(ServiceConstants.ERRORS)) {
//						if(((JSONObject)((JSONArray)output.get("errors")).get(0)).get("errorMessage").toString().
//								contains(ServiceConstants.PRECHECKOURT_ERRORMSG)) {
//							
//							result = ApiAdapter.setError(output);
//							return APIResponseBuilder.sendSuccessResponse(result);
//						} else {
//							result = ApiAdapter.setError(output);
//							return APIResponseBuilder.sendSuccessResponse(result);
//						}
//					}
//				}
//			} else {
//				errors.put("code",423);
//				errors.put("message",((JSONObject)((JSONArray)output.get("errors")).get(0)).get("errorMessage").toString());
//				error.add(errors);
//				result.put("data", new JSONObject());
//				result.put("error", error);
//				
//				return APIResponseBuilder.sendSuccessResponse(result);
//			}
//
//		} catch (IOException e) {
//			logger.severe("IOExc in updateOrderShippingInfo:" + e);
//			
//			return APIResponseBuilder.sendFailResponse(new JSONObject(),
//					e.getMessage(),
//					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e.getMessage());
//		} catch (JSONException e) {
//			logger.severe("JSONExc in updateOrderShippingInfo:"
//					+ e);
//			return APIResponseBuilder.sendFailResponse(new JSONObject(),
//					e.getMessage(),
//					ServiceConstants.ERROR_CODE_IMPROPERJSON, e
//							.getMessage());
//		} catch (Exception e) {
//			logger.severe("Exc in updateOrderShippingInfo:"
//					+ e);
//			return APIResponseBuilder.sendFailResponse(new JSONObject(),
//					e.getMessage(),
//					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
//							.getMessage());
//		} 
//		
//		return APIResponseBuilder.sendSuccessResponse(finalResponse);
//		
//	}
	
	
	@PUT
    @Path("/store/{storeId}/cart/@self/shipping_info/checkout")
    @Produces(MediaType.APPLICATION_JSON)
    @OAuthSecurity(enabled = false)
    public Response updateOrderShippingInfo(
                    @PathParam("storeId") String storeId, JSONObject request,
                    @HeaderParam("WCToken") String commerceToken,
                    @HeaderParam("WCTrustedToken") String wcTrustedToken,
                    @HeaderParam("userId") String userId) {

            logger.severe("into the checkout API **********");
            
            String url = AccessProperties.configFile
                            .getProperty(ServiceConstants.URLSSL);
            String responseFormat = AccessProperties.configFile
                            .getProperty(ServiceConstants.RESPONSE_FORMATE);
            String url6 = url + storeId + "/cart/@self/shipping_info?responseFormat=" + responseFormat;
            logger.severe("Checkout shipping_info:"+url6);
            JSONObject output = new JSONObject();
            //String checkoutResponse = "";
            JSONObject preCheckOut = new JSONObject();
            JSONObject shippingRequest = new JSONObject();
            JSONObject finalResponse = new JSONObject();
            JSONArray error = new JSONArray();
            JSONObject result = new JSONObject();
            JSONObject errors = new JSONObject();

            JSONObject updatePersonResponse = new JSONObject();
            JSONObject response = new JSONObject();
            try {

                    JSONObject request1 = (JSONObject) request.clone();
                //    logger.severe("Checkout clone request:"+request1.toString());


                    if(request1.containsKey("addressId") && !request1.get("addressId").toString().isEmpty()) {
                            updatePersonResponse.put("addressId",request1.get("addressId"));
                    } else {

                            request1.put("addressType", "ShippingAndBilling");
                            request1.remove("shipModeId");
                            request1.remove("orderNumber");
                            request1.remove("title");
                            request1.remove("storeId");
                            request1.remove("timeAtAddress");
                            request1.remove("cartItems");
                            request1.remove("orderTotalAmount");

                            JSONObject personDetails = getPersonContact(storeId,request1.getString("nickName"), wcTrustedToken, commerceToken);
                            //logger.severe("Checkout getPersonContact response:"+personDetails);

                            if(personDetails.containsKey("addressId")) {
                                    updatePersonResponse.put("addressId",personDetails.get("addressId"));

                            } else {
//                                    int randomNum = new Random().nextInt((999 - 100) + 1) + 100;
//                                    String nickanme = request1.getString("nickName")+randomNum;
//                                    nickanme = nickanme.replaceAll("\\s+","");
//                                    
//                                    request1.put("firstName",request1.getString("firstName").replaceAll("\\s+",""));
//                                    request1.put("lastName",request1.getString("lastName").replaceAll("\\s+",""));
//                                    request1.put("nickName",nickanme);

                               //     logger.severe("Checkout request for addPersonContact:"+request1);
                                    response = this.addPersonContact(storeId,
                                                    request1, commerceToken, wcTrustedToken, userId);

                                    updatePersonResponse = response.getJSONObject("data");
//                                    updatePersonResponse = updatePersonContact(storeId,
//                                                    nickanme, request1, commerceToken,
//                                                    wcTrustedToken, userId);
//
//                                    updatePersonResponse = (JSONObject) updatePersonResponse.get("data");
                            }
                    }

                    if(updatePersonResponse.containsKey("addressId"))  {
                    	
                    	
                            shippingRequest.put("x_calculationUsage", "-1,-2,-3,-4,-5,-6,-7");
                            
//                            if(request.get("shipModeId").toString().equals("10001"))
//                            	shippingRequest.put("shipModeId", "10708");
//                            else
//                            	shippingRequest.put("shipModeId", "10702");
                            
                            String shipmodeId = getShippingModes(storeId,request.getString("shipModeId"));
                            
                            shippingRequest.put("shipModeId", shipmodeId);
                            
                            shippingRequest.put("addressId",
                            		updatePersonResponse.get("addressId"));

                            String input = shippingRequest.toString();
                           // logger.severe("Checkout shipingInfoInput:"+input);
                            output = ApiAdapter
                                            .callPutAPI(url6, input, commerceToken, wcTrustedToken);

                           // logger.severe("Checkout shipingInfoHandleroutput:"+output.toString());

                            if(output.containsKey(ServiceConstants.ORDERID)) {

                                    String orderId = output.get(ServiceConstants.ORDERID).toString();
                                  //  logger.severe("Checkout orderId before checkout:..:"+orderId);

                                    String url2 = url + storeId
                                                    + "/cart/@self/precheckout?responseFormat="
                                                    + responseFormat;
                                 //   logger.severe("Checkout - precheckout:"+url2);
                                    preCheckOut.put(ServiceConstants.ORDERID, orderId);
                                    preCheckOut.put("channelId", "2");
                                    preCheckOut.put("storeNumber", request.getString("storeId"));
                                    String preChecoutInput = preCheckOut.toString();
                                 //   logger.severe("precheckout request body/input to wcs:.."+preChecoutInput);
                                    output = ApiAdapter.callPutAPIForCheckOut(url2, preChecoutInput,
                                                    commerceToken, wcTrustedToken);
                                  //  logger.severe("Checkout - precheckout output:"+output.toString());

//                                    /*Need to remove this code after addressId fix*/
//                                    getOrderDetails(storeId, orderId, commerceToken, wcTrustedToken);
//                                    /*Need to remove this code after addressId fix*/

//                                  String url3 = url + storeId + "/cart/@self/checkout";
//                                  logger.severe(url3);
//                                  checkoutResponse = ApiAdapter.callPostAPI(url3, preChecoutInput,
//                                                  commerceToken, wcTrustedToken);

                                    //logger.info("checkout output:"+checkoutResponse);
//                                  if(output.isEmpty())
//                                          output.put(ServiceConstants.ORDERID,orderId);
                                    finalResponse.put("data",output);
                                    finalResponse.put("error",new JSONArray());
                                    //finalResponse = output;

                                    if(output.containsKey("orderId")) {
                                            logger.severe("ORDER ID BEFOR ENTERING CHECOUTAPI"+output.get("orderId").toString());
                                            finalResponse = submitOrder(request,output.get("orderId").toString());

                                    }

                            } else {
                                    if(output.containsKey(ServiceConstants.ERRORS)) {
                                            if(((JSONObject)((JSONArray)output.get("errors")).get(0)).get("errorMessage").toString().
                                                            contains(ServiceConstants.PRECHECKOURT_ERRORMSG)) {

                                                    result = ApiAdapter.setError(output);
                                                    return APIResponseBuilder.sendSuccessResponse(result);
                                            } else {
                                                    result = ApiAdapter.setError(output);
                                                    return APIResponseBuilder.sendSuccessResponse(result);
                                            }
                                    }
                            }
                    } else {
                            errors.put("code",423);
                            errors.put("message",((JSONObject)((JSONArray)output.get("errors")).get(0)).get("errorMessage").toString());
                            error.add(errors);
                            result.put("data", new JSONObject());
                            result.put("error", error);

                            return APIResponseBuilder.sendSuccessResponse(result);
                    }

            } catch (IOException e) {
                    logger.severe("IOExc in updateOrderShippingInfo:" + e);

                    return APIResponseBuilder.sendFailResponse(new JSONObject(),
                                    e.getMessage(),
                                    ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e.getMessage());
            } catch (JSONException e) {
                    logger.severe("JSONExc in updateOrderShippingInfo:"
                                    + e);
                    return APIResponseBuilder.sendFailResponse(new JSONObject(),
                                    e.getMessage(),
                                    ServiceConstants.ERROR_CODE_IMPROPERJSON, e
                                                    .getMessage());
            } catch (Exception e) {
                    logger.severe("Exc in updateOrderShippingInfo:"
                                    + e);
                    return APIResponseBuilder.sendFailResponse(new JSONObject(),
                                    e.getMessage(),
                                    ServiceConstants.ERROR_CODE_INTERNALSERVERERROR, e
                                                    .getMessage());
            }

            return APIResponseBuilder.sendSuccessResponse(finalResponse);

    }

	@GET
	@Path("/store/{storeId}/addressLookup")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public String getAddressLookup(@QueryParam("PostCode") String postCode) {

		AddressLookUpService service = new AddressLookUpService();
		return service.getAddressLookup(postCode);

	}

	@GET
	@Path("/store/{storeId}/searchdata")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public String getCustomerDetails(@QueryParam("key") String key,
			@QueryParam("value") String value,
			@QueryParam("description") String description) {

		CustomerSearchService service = new CustomerSearchService();
		return service.getCustomerData(key, value, description);

	}
	

	public JSONObject submitOrder(JSONObject request,String orderId) throws JSONException{

		SubmitOrderService service = new SubmitOrderService();
		
		return service.updateOrder(request,orderId);

	}
	
	@GET
	@Path("/store/{storeId}/order/{orderId}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public JSONObject getOrderDetails(@PathParam("storeId") String storeId,
			@PathParam("orderId") String orderId,@HeaderParam("WCToken") String commerceToken,
			@HeaderParam("WCTrustedToken") String wcTrustedToken) {

		
		String url = AccessProperties.configFile
				.getProperty(ServiceConstants.URLSSL);
		
		String url1 = url + storeId + "/order/"+orderId;
		
		logger.severe(url1);
		JSONObject obj = new JSONObject();
		
		try {
			obj = ApiAdapter.callGetAPI(url1,commerceToken,wcTrustedToken);
			//logger.severe("Order Detail Output after PreCheckOut: "+obj);
		} catch (IOException e) {
			logger.severe("IOExc in getOrderDetails:" + e);
		} catch (JSONException e) {
			logger.severe("JSONExc in getOrderDetails:" + e);
		} catch (Exception e) {
			logger.severe("Exce in getOrderDetails:" + e);
		}
		return obj;

	}

}
