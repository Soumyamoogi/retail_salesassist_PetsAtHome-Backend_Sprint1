/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mobilefirst.retail.salesassist.api.product;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.google.common.base.Stopwatch;
import com.ibm.mobilefirst.retail.salesassist.adapter.product.common.APIResponseBuilder;
import com.ibm.mobilefirst.retail.salesassist.adapter.product.common.ApiAdapter;
import com.ibm.mobilefirst.retail.salesassist.adapter.product.common.ProductByCategory;
import com.ibm.mobilefirst.retail.salesassist.adapter.product.common.ProductById;
import com.ibm.mobilefirst.retail.salesassist.adapter.product.common.ServiceConstants;
import com.ibm.mobilefirst.retail.salesassist.adapter.product.common.SortList;
import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class ProductResource {

	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(ProductResource.class.getName());

	//Define the server api to be able to perform server operations
	WLServerAPI api = WLServerAPIProvider.getWLServerAPI();

	@GET
	@Path("/store/{storeId}/products/bycategory/{categoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getProductsByCategory(@PathParam("storeId") String storeId,@PathParam("categoryId") String catId,@QueryParam("pageNumber") int pageNumber,@QueryParam("pageSize") int pageSize){

		logger.severe("================Start of getProductsByCategory API================");
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		
		StringBuilder productBuilder = new StringBuilder();
		StringBuilder productIds = new StringBuilder();
		StringBuilder ratingUrl1 = new StringBuilder();
		StringBuilder ratingCountUrl = new StringBuilder();
		HashMap<String, String> ratingMap = new HashMap<String, String>();
		HashMap<String, String> ratingCountMap = new HashMap<String, String>();
		
		String langId = AccessProperties.configFile.getProperty(ServiceConstants.LANGID);
		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		String ratingUrl = AccessProperties.configFile.getProperty(ServiceConstants.RATINGANDREVIEWURL);
		String merchantId = AccessProperties.configFile.getProperty(ServiceConstants.MERCHANTID);
		String apiKey = AccessProperties.configFile.getProperty(ServiceConstants.APIKEY);
		String url1 = url+storeId+"/productview/byCategory/"+catId  + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&langId=" + langId + "&orderBy=0";
		//logger.severe(url1);

		try {
			Stopwatch stopwatchurl1 = new Stopwatch();
			stopwatchurl1.start();
			
			logger.severe("------------------------------------------");
			logger.severe("URL1 : " + url1);
			logger.severe("StoreId : " + storeId);
			
			JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
			
			stopwatchurl1.stop();
			long stopurl1 = stopwatchurl1.elapsed(TimeUnit.MILLISECONDS);
			long secondsurl1 = (stopurl1 / 1000) % 60;
			long minutesurl1 = (stopurl1 / 1000) / 60;
			logger.severe("Milliseconds : " + stopurl1 + "\tseconds : " + secondsurl1 + "\tMinutes : "
					+ minutesurl1);
			logger.severe("------------------------------------------");
			
			//logger.severe("");
			//logger.severe(jsonObj.toString());
			JSONArray arr = jsonObj.getJSONArray("CatalogEntryView");
			if(arr != null) {				
				productBuilder.append("{"+"\n"+"\"recordSetTotal\""+":"+jsonObj.getString("recordSetTotal")+","+"\n");
				productBuilder.append("\"recordSetCount\""+":"+jsonObj.getString("recordSetCount")+","+"\n"+"\"data\""+":"+"\n");
				productBuilder.append("[");
				for(int i=0;i<arr.length();i++)
				{
					if(i==0) {
						productIds.append(arr.getJSONObject(i).getString("partNumber"));
					} else {
						productIds.append(",").append(arr.getJSONObject(i).getString("partNumber"));					
					}
				}
				
				Stopwatch stopwatchratingUrl1 = new Stopwatch();
				stopwatchratingUrl1.start();
				
				logger.severe("------------------------------------------");
				
				
				ratingUrl1.append(ratingUrl).append("products/rating?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
				//logger.severe(ratingUrl1.toString());
				logger.severe("ratingUrl1 : " + ratingUrl1);
				logger.severe("StoreId : " + storeId);
				
				JSONArray ratingArr = ApiAdapter.callGetAPI(ratingUrl1);
				
				stopwatchratingUrl1.stop();
				long stopratingurl1 = stopwatchratingUrl1.elapsed(TimeUnit.MILLISECONDS);
				long secondsratingurl1 = (stopratingurl1 / 1000) % 60;
				long minutesratingurl1 = (stopratingurl1 / 1000) / 60;
				logger.severe("Milliseconds : " + stopratingurl1 + "\tseconds : " + secondsratingurl1 + "\tMinutes : "
						+ minutesratingurl1);
				logger.severe("------------------------------------------");
				
				for(int j=0; j<ratingArr.length();j++) {
					ratingMap.put(ratingArr.getJSONObject(j).getString("page_id"), ratingArr.getJSONObject(j).getString("average_rating"));
				}

				Stopwatch stopwatchratingcountUrl1 = new Stopwatch();
				stopwatchratingcountUrl1.start();
				
				logger.severe("------------------------------------------");
				
				
				ratingCountUrl.append(ratingUrl).append("products/review_count?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
				
				//logger.severe(ratingCountUrl.toString());
				logger.severe("ratingCountUrl : " + ratingUrl1);
				logger.severe("StoreId : " + storeId);
				
				JSONArray ratingCountArr = ApiAdapter.callGetAPI(ratingCountUrl);
				
				stopwatchratingcountUrl1.stop();
				long stopratingcounturl1 = stopwatchratingcountUrl1.elapsed(TimeUnit.MILLISECONDS);
				long secondsratingcounturl1 = (stopratingcounturl1 / 1000) % 60;
				long minutesratingcounturl1 = (stopratingcounturl1 / 1000) / 60;
				logger.severe("Milliseconds : " + stopratingcounturl1 + "\tseconds : " + secondsratingcounturl1 + "\tMinutes : "
						+ minutesratingcounturl1);
				logger.severe("------------------------------------------");
				
				for(int j=0; j<ratingCountArr.length();j++) {
					ratingCountMap.put(ratingCountArr.getJSONObject(j).getString("page_id"), ratingCountArr.getJSONObject(j).getString("num_reviews"));
				}

				for(int i=0;i<arr.length();i++)
				{
					ProductByCategory prod = new ProductByCategory();
					prod.setEntityFromJson(arr.getJSONObject(i), ratingMap, ratingCountMap);
					String resp = prod.toString();
					productBuilder.append(resp);
					if(i!=arr.length()-1)
						productBuilder.append(","+"\n");				
					resp = "";
				}
				productBuilder.append("]");
				productBuilder.append("}");
				logger.info(productBuilder.toString());
			}
			stopwatch.stop();
			long stop = stopwatch.elapsed(TimeUnit.MILLISECONDS);
			logger.severe("================ Response time for getProductsByCategory API================");
			long seconds = (stop / 1000) % 60;
			long minutes = (stop / 1000) / 60;
			logger.severe("Milliseconds : " + stop + "\tSeconds : " + seconds + "\tMinutes : " + minutes);

			logger.severe("================ Stop of getProductsByCategory API==========================");
			
			return APIResponseBuilder.sendSuccessResponseStd1(new JSONObject(productBuilder.toString()));


		} catch (IOException e) {
			logger.severe("IOExc in getProductsByCategory:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);

		} catch (JSONException e) {
			logger.severe("JSONExc in getProductsByCategory:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);

		} catch (Exception e) {
			logger.severe("Exc in getProductsByCategory:" + e);	
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);
		}
	}


	@GET
	@Path("/store/{storeId}/featuredProducts")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getFeaturedProducts(@PathParam("storeId") String storeId,@QueryParam("pageNumber") int pageNumber,@QueryParam("pageSize") int pageSize, @QueryParam("categoryId") String categoryId){

		StringBuilder productBuilder = new StringBuilder();
		StringBuilder productIds = new StringBuilder();
		StringBuilder ratingUrl1 = new StringBuilder();
		StringBuilder ratingCountUrl = new StringBuilder();
		HashMap<String, String> ratingMap = new HashMap<String, String>();
		HashMap<String, String> ratingCountMap = new HashMap<String, String>();

		
		String langId = AccessProperties.configFile.getProperty(ServiceConstants.LANGID);
		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		String ratingUrl = AccessProperties.configFile.getProperty(ServiceConstants.RATINGANDREVIEWURL);
		String merchantId = AccessProperties.configFile.getProperty(ServiceConstants.MERCHANTID);
		String apiKey = AccessProperties.configFile.getProperty(ServiceConstants.APIKEY);
		String CatId = null;
		if(categoryId != null && (!categoryId.equalsIgnoreCase(""))) {
			CatId = AccessProperties.configFile.getProperty(categoryId);
			if(CatId==null || CatId.isEmpty())
				CatId=categoryId;
		} else {
			CatId = AccessProperties.configFile.getProperty(ServiceConstants.CATEGORYID);   		
		}
		String url1 = url+storeId+"/productview/byCategory/"+CatId  + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&langId=" + langId;
		logger.severe(url1);

		if(pageNumber==0)
			pageNumber=1;

		if(pageSize==0)
			pageSize=10;

		try {
			JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
			logger.severe(jsonObj.toString());
			JSONArray arr = jsonObj.getJSONArray("CatalogEntryView");
			if(arr != null) {				
				productBuilder.append("{"+"\n"+"\"recordSetTotal\""+":"+jsonObj.getString("recordSetTotal")+","+"\n");
				productBuilder.append("\"recordSetCount\""+":"+jsonObj.getString("recordSetCount")+","+"\n"+"\"data\""+":"+"\n");
				productBuilder.append("[");

				for(int i=0;i<arr.length();i++)
				{
					if(i==0) {
						productIds.append(arr.getJSONObject(i).getString("partNumber"));
					} else {
						productIds.append(",").append(arr.getJSONObject(i).getString("partNumber"));					
					}
				}
				ratingUrl1.append(ratingUrl).append("products/rating?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
				//logger.severe(ratingUrl1.toString());
				JSONArray ratingArr = ApiAdapter.callGetAPI(ratingUrl1);

				for(int j=0; j<ratingArr.length();j++) {
					ratingMap.put(ratingArr.getJSONObject(j).getString("page_id"), ratingArr.getJSONObject(j).getString("average_rating"));
				}

				ratingCountUrl.append(ratingUrl).append("products/review_count?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
				//logger.severe(ratingCountUrl.toString());
				JSONArray ratingCountArr = ApiAdapter.callGetAPI(ratingCountUrl);
				for(int j=0; j<ratingCountArr.length();j++) {
					ratingCountMap.put(ratingCountArr.getJSONObject(j).getString("page_id"), ratingCountArr.getJSONObject(j).getString("num_reviews"));
				}

				for(int i=0;i<arr.length();i++)
				{
					ProductByCategory prod = new ProductByCategory();
					prod.setEntityFromJson(arr.getJSONObject(i), ratingMap, ratingCountMap);
					String resp = prod.toString();
					productBuilder.append(resp);
					if(i!=arr.length()-1)
						productBuilder.append(","+"\n");				
					resp = "";
				}
				productBuilder.append("]");
				productBuilder.append("}");
				logger.info(productBuilder.toString());
			}
			return APIResponseBuilder.sendSuccessResponseStd1(new JSONObject(productBuilder.toString()));

		} catch (IOException e) {
			logger.severe("IOExc in getFeaturedProducts:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);
		} catch (JSONException e) {
			logger.severe("JSONExc in getFeaturedProducts:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);

		} catch (Exception e) {
			logger.severe("Exc in getFeaturedProducts:" + e);	
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);
		}
	}




	@GET
	@Path("/store/{storeId}/productview/bySearchTerm/{searchTerm}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getProductsBySearchTerm(@PathParam("storeId") String storeId,@PathParam("searchTerm") String searchTerm, 
			@QueryParam("pageSize") String pageSize, @QueryParam("pageNumber") String pageNumber,
			@QueryParam("price") String price, @QueryParam("facet") String facetvalue,
			@QueryParam("orderby") String orderby) {

		logger.severe("================Start of getProductsBySearchTerm API================");
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();

		StringBuilder productIds = new StringBuilder();
		StringBuilder ratingUrl1 = new StringBuilder();
		StringBuilder ratingCountUrl = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();

		HashMap<String, String> ratingMap = new HashMap<>();
		HashMap<String, String> ratingCountMap = new HashMap<>();

		String langId = AccessProperties.configFile.getProperty(ServiceConstants.LANGID);
		String catalogId = AccessProperties.configFile.getProperty(ServiceConstants.CATALOGID);
		String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String currency = AccessProperties.configFile.getProperty(ServiceConstants.CURRENTCY);
		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		String ratingUrl = AccessProperties.configFile.getProperty(ServiceConstants.RATINGANDREVIEWURL);
		String merchantId = AccessProperties.configFile.getProperty(ServiceConstants.MERCHANTID);
		String apiKey = AccessProperties.configFile.getProperty(ServiceConstants.APIKEY);

		String profileName = "X_findProductsBySearchTerm";
		JSONObject jsonObj = null;
		boolean isPartNumberSearch = false;

		if (pageNumber == null || pageNumber.isEmpty())
			pageNumber = "1";

		if (pageSize == null || pageSize.isEmpty())
			pageSize = "10";

		try {

			if (orderby != null && (!orderby.isEmpty())) {
				switch (orderby) {
				case "0":
					orderby = "0";
					break;
				case "1":
					orderby = "4";
					break;
				case "2":
					orderby = "3";
					break;
				case "3":
					orderby = "2";
					break;
				case "4":
					orderby = "5";
					break;
				default:
					orderby = "";
					break;
				}
			}

			String minPrice = "";
			String maxPrice = "";

			if (price != null && !price.isEmpty()) {
				String[] priceSplit = price.split("to");
				minPrice = priceSplit[0].trim();
				maxPrice = priceSplit[1].trim();
			}

			if ((facetvalue != null && (!facetvalue.isEmpty()))) {
				if (orderby != null && (!orderby.isEmpty())) {
					String url1 = url + storeId + "/productview/bySearchTerm/" + searchTerm + "?pageNumber="
							+ pageNumber + "&pageSize=" + pageSize + "&langId=" + langId + "&searchType=1002"
							+ "&facet=" + URLEncoder.encode(facetvalue, "UTF-8") + "&orderBy=" + orderby + "&minPrice="
							+ minPrice + "&maxPrice=" + maxPrice;
					logger.severe(url1);
					jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));
				} else {
					String url1 = url + storeId + "/productview/bySearchTerm/" + searchTerm + "?pageNumber="
							+ pageNumber + "&pageSize=" + pageSize + "&langId=" + langId + "&searchType=1002"
							+ "&facet=" + URLEncoder.encode(facetvalue, "UTF-8") + "&minPrice=" + minPrice
							+ "&maxPrice=" + maxPrice;
					logger.severe(url1);
					jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));
				}

			} else if (((price != null && (!price.isEmpty())))) {
				if (orderby != null && (!orderby.isEmpty())) {
					String url1 = url + storeId + "/productview/bySearchTerm/" + searchTerm + "?pageNumber="
							+ pageNumber + "&pageSize=" + pageSize + "&langId=" + langId + "&searchType=1002"
							+ "&orderBy=" + orderby + "&minPrice=" + minPrice + "&maxPrice=" + maxPrice;
					logger.severe(url1);
					jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));
				} else {
					String url1 = url + storeId + "/productview/bySearchTerm/" + searchTerm + "?pageNumber="
							+ pageNumber + "&pageSize=" + pageSize + "&langId=" + langId + "&searchType=1002"
							+ "&minPrice=" + minPrice + "&maxPrice=" + maxPrice;
					logger.severe(url1);
					jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));
				}
			} else {
				/*
				 * Commented by A.George 
				 * 04May2017.
				 * try {
					if (orderby != null && (!orderby.isEmpty())) {
						Stopwatch stopwatchurl1 = new Stopwatch();
						stopwatchurl1.start();

						String url1 = url + storeId + "/productview/" + searchTerm + "?langId=" + langId + "&currency="
								+ currency + "&orderBy=" + orderby + "&responseFormat=" + responseFormat;

						logger.severe("------------------------------------------");
						logger.severe("URL1 : " + url1);
						logger.severe("StoreId : " + storeId);

						jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));

						stopwatchurl1.stop();
						long stopurl1 = stopwatchurl1.elapsed(TimeUnit.MILLISECONDS);
						long secondsurl1 = (stopurl1 / 1000) % 60;
						long minutesurl1 = (stopurl1 / 1000) / 60;
						logger.severe("Milliseconds : " + stopurl1 + "\tseconds : " + secondsurl1 + "\tMinutes : "
								+ minutesurl1);
						logger.severe("------------------------------------------");

						isPartNumberSearch = true;
					}

					else {
						Stopwatch stopwatchurl1 = new Stopwatch();
						stopwatchurl1.start();

						String url1 = url + storeId + "/productview/" + searchTerm + "?langId=" + langId + "&currency="
								+ currency + "&responseFormat=" + responseFormat;

						logger.severe("------------------------------------------");
						logger.severe("URL1 : " + url1);
						logger.severe("StoreId : " + storeId);

						jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));

						stopwatchurl1.stop();
						long stopurl1 = stopwatchurl1.elapsed(TimeUnit.MILLISECONDS);
						long secondsurl1 = (stopurl1 / 1000) % 60;
						long minutesurl1 = (stopurl1 / 1000) / 60;
						logger.severe("Milliseconds : " + stopurl1 + "\tseconds : " + secondsurl1 + "\tMinutes : "
								+ minutesurl1);
						logger.severe("------------------------------------------");
					}
					isPartNumberSearch = true;

				} catch (IOException e) {
					logger.severe("IOExc in getProductsByPartNumberSearch:" + e);
				} catch (JSONException e) {
					logger.severe("JSONExc in getProductsByPartNumberSearch:" + e);
				} catch (Exception e) {
					logger.severe("Exc in getProductsByPartNumberSearch:" + e);
				}
*/
				if (isPartNumberSearch == false) {
					if (orderby != null && (!orderby.isEmpty())) {
						Stopwatch stopwatchurl2 = new Stopwatch();
						stopwatchurl2.start();

						String url1 = url + storeId + "/productview/bySearchTerm/" + searchTerm + "?pageNumber="
								+ pageNumber + "&pageSize=" + pageSize + "&langId=" + langId + "&catalogId=" + catalogId
								+ "&currency=" + currency + "&responseFormat=" + responseFormat + "&orderBy=" + orderby
								+ "&profileName=" + profileName + "&searchType=1002";

						logger.severe("------------------------------------------");
						logger.severe("URL2 : " + url1);
						logger.severe("StoreId : " + storeId);

						jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));

						stopwatchurl2.stop();
						long stopurl2 = stopwatchurl2.elapsed(TimeUnit.MILLISECONDS);
						long secondsurl2 = (stopurl2 / 1000) % 60;
						long minutesurl2 = (stopurl2 / 1000) / 60;
						logger.severe("Milliseconds : " + stopurl2 + "\tseconds : " + secondsurl2 + "\tMinutes : "
								+ minutesurl2);
						logger.severe("------------------------------------------");
					} else {
						Stopwatch stopwatchurl2 = new Stopwatch();
						stopwatchurl2.start();

						String url1 = url + storeId + "/productview/bySearchTerm/" + searchTerm + "?pageNumber="
								+ pageNumber + "&pageSize=" + pageSize + "&langId=" + langId + "&catalogId=" + catalogId
								+ "&currency=" + currency + "&responseFormat=" + responseFormat + "&profileName="
								+ profileName + "&searchType=1002";

						logger.severe("------------------------------------------");
						logger.severe("URL2 : " + url1);
						logger.severe("StoreId : " + storeId);

						jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));

						stopwatchurl2.stop();
						long stopurl2 = stopwatchurl2.elapsed(TimeUnit.MILLISECONDS);
						long secondsurl2 = (stopurl2 / 1000) % 60;
						long minutesurl2 = (stopurl2 / 1000) / 60;
						logger.severe("Milliseconds : " + stopurl2 + "\tseconds : " + secondsurl2 + "\tMinutes : "
								+ minutesurl2);
						logger.severe("------------------------------------------");

					}
				}
			}

			JSONArray arr = jsonObj.getJSONArray("CatalogEntryView");
			productBuilder
					.append("{" + "\n" + "\"recordSetTotal\"" + ":" + jsonObj.getString("recordSetTotal") + "," + "\n");
			productBuilder.append("\"recordSetCount\"" + ":" + jsonObj.getString("recordSetCount") + "," + "\n"
					+ "\"data\"" + ":" + "\n");
			productBuilder.append("[");

			for (int i = 0; i < arr.length(); i++) {
				if (i == 0) {
					productIds.append(arr.getJSONObject(i).getString("partNumber"));
				} else {
					productIds.append(",").append(arr.getJSONObject(i).getString("partNumber"));
				}
			}

			if (orderby != null && !orderby.isEmpty()) {
				Stopwatch ratingurl = new Stopwatch();
				ratingurl.start();

				ratingUrl1.append(ratingUrl).append("products/rating?apikey=").append(apiKey).append("&page_id=")
						.append(productIds).append("&merchant_id=").append(merchantId);

				logger.severe("------------------------------------------");
				logger.severe("Rating URL : " + ratingUrl1);
				logger.severe("StoreId : " + storeId);

				JSONArray ratingArr = ApiAdapter.callGetAPI(ratingUrl1);
				for (int j = 0; j < ratingArr.length(); j++) {
					ratingMap.put(ratingArr.getJSONObject(j).getString("page_id"),
							ratingArr.getJSONObject(j).getString("average_rating"));

				}
				ratingurl.stop();
				long stoprating = ratingurl.elapsed(TimeUnit.MILLISECONDS);
				long secondsratingurl = (stoprating / 1000) % 60;
				long minutesratingurl = (stoprating / 1000) / 60;
				logger.severe("Milliseconds : " + stoprating + "\tseconds : " + secondsratingurl + "\tMinutes : "
						+ minutesratingurl);
				logger.severe("------------------------------------------");

				Stopwatch ratingcounturl = new Stopwatch();
				ratingcounturl.start();

				ratingCountUrl.append(ratingUrl).append("products/review_count?apikey=").append(apiKey)
						.append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);

				logger.severe("------------------------------------------");
				logger.severe("Rating Count URL : " + ratingCountUrl);
				logger.severe("StoreId : " + storeId);

				JSONArray ratingCountArr = ApiAdapter.callGetAPI(ratingCountUrl);
				for (int j = 0; j < ratingCountArr.length(); j++) {
					ratingCountMap.put(ratingCountArr.getJSONObject(j).getString("page_id"),
							ratingCountArr.getJSONObject(j).getString("num_reviews"));

				}

				ratingcounturl.stop();
				long stopratingcount = ratingcounturl.elapsed(TimeUnit.MILLISECONDS);
				long secondsratingcounturl = (stopratingcount / 1000) % 60;
				long minutesratingcounturl = (stopratingcount / 1000) / 60;
				logger.severe("Milliseconds : " + stopratingcount + "\tseconds : " + secondsratingcounturl
						+ "\tMinutes : " + minutesratingcounturl);
				logger.severe("------------------------------------------");

			}

			for (int i = 0; i < arr.length(); i++) {
				ProductByCategory prod = new ProductByCategory();
				prod.setEntityFromJson(arr.getJSONObject(i), ratingMap, ratingCountMap);
				String resp = prod.toString();
				productBuilder.append(resp);
				if (i != arr.length() - 1)
					productBuilder.append("," + "\n");

				resp = "";
			}
			productBuilder.append("]");
			productBuilder.append("}");

			stopwatch.stop();
			long stop = stopwatch.elapsed(TimeUnit.MILLISECONDS);
			logger.severe("================ Response time for getProductsBySearchTerm API================");
			long seconds = (stop / 1000) % 60;
			long minutes = (stop / 1000) / 60;
			logger.severe("Milliseconds : " + stop + "\tSeconds : " + seconds + "\tMinutes : " + minutes);

			logger.severe("================ Stop of getProductsBySearchTerm API==========================");
			return APIResponseBuilder.sendSuccessResponseStd1(new JSONObject(productBuilder.toString()));

		} catch (IOException e) {
			logger.severe("IOExc in getProductsBySearchTerm:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(), ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);
		} catch (JSONException e) {
			logger.severe("JSONExc in getProductsBySearchTerm:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(), ServiceConstants.ERROR_CODE_IMPROPERJSON);
		} catch (Exception e) {
			logger.severe("Exc in getProductsBySearchTerm:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),
					ServiceConstants.ERROR_CODE_INTERNALSERVERERROR);
		}

	} 

	@GET
	@Path("/store/{storeId}/productview/bySearchTerm/{searchTerm}/filterlist/all")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getFilterListBySearchTerm(@PathParam("storeId") String storeId,@PathParam("searchTerm") String searchTerm, @QueryParam("pageSize") String pageSize, @QueryParam("pageNumber") String pageNumber, @QueryParam("facet") String facetvalue) {
		

		String langId = AccessProperties.configFile.getProperty(ServiceConstants.LANGID);
		String catalogId = AccessProperties.configFile.getProperty(ServiceConstants.CATALOGID);
		String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String currency = AccessProperties.configFile.getProperty(ServiceConstants.CURRENTCY);
		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);

		JSONArray arr = new JSONArray();
		JSONArray finalarr = new JSONArray();
		JSONObject finalobj = new JSONObject();
		List<JSONObject> filtersList = new ArrayList<JSONObject>();
		JSONObject tempobj = new JSONObject();
		JSONObject resjobj = new JSONObject();

		String profileName = "X_findProductsBySearchTerm";
		JSONObject jsonObj = null;
		boolean isPartNumberSearch = false;

		if(pageNumber==null || pageNumber.isEmpty())
			pageNumber="1";

		if(pageSize==null || pageSize.isEmpty())
			pageSize="10";

		try {
			if(facetvalue != null && (!facetvalue.isEmpty()))
			{

					String url1 = url+storeId+"/productview/bySearchTerm/"+searchTerm + "?pageNumber=" + pageNumber +"&pageSize=" + pageSize +"&langId=" + langId + "&searchType=1002" +"&facet="+URLEncoder.encode(facetvalue,"UTF-8");
					logger.severe(url1);
					jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));

			} else{
					try {
					String url1 = url+storeId+"/productview/"+searchTerm + "?langId=" + langId + "&currency=" + currency +  "&responseFormat=" + responseFormat;
					logger.severe(url1);
					jsonObj = ApiAdapter.callGetAPI(url1.replaceAll(" ", "%20"));
					logger.severe("the new JSON is here"+jsonObj.toString());
					isPartNumberSearch = true;
	
				} catch (IOException e) {
					logger.severe("IOExc in getProductsByPartNumberSearch:" + e);
				} catch (JSONException e) {
					logger.severe("JSONExc in getProductsByPartNumberSearch:" + e);
				} catch (Exception e) {
					logger.severe("Exc in getProductsByPartNumberSearch:" + e);	
				}
	
				if(isPartNumberSearch == false) {
					String url2 = url+storeId+"/productview/bySearchTerm/"+searchTerm + "?pageNumber=" + pageNumber +"&pageSize=" + pageSize +"&langId=" + langId + "&catalogId=" + catalogId + "&currency=" + currency +  "&responseFormat=" + responseFormat + "&profileName=" + profileName + "&searchType=1002";
					logger.severe(url2);
					jsonObj = ApiAdapter.callGetAPI(url2.replaceAll(" ", "%20")); 
				}		
			}
			if(jsonObj.containsKey("FacetView")){
				arr = jsonObj.getJSONArray("FacetView");
				for( int i=0; i<arr.length() ;i++)
				{
					JSONObject jobj = arr.getJSONObject(i);

					if(jobj.get("value").toString().equals("price_GBP"))
					{
						if(!finalarr.isEmpty())
						{
							for( int k=0 ; k<finalarr.length(); k++)
							{
								if(finalarr.getJSONObject(k).getString("name").equals("Price") )
								{
									logger.severe("we already have price here");
								}

								else{
									finalobj.put("name", "Price");
									for( int j=0; j<jobj.getJSONArray("Entry").length(); j++)
									{
										tempobj = (JSONObject) jobj.getJSONArray("Entry").get(j);
										String str1 = tempobj.getString("label");
										int indexOfOpenBracket = str1.indexOf("{");
										int indexOfLastBracket = str1.lastIndexOf("}");

										//logger.severe(str1.substring(indexOfOpenBracket+1, indexOfLastBracket));
										str1=str1.substring(indexOfOpenBracket+1, indexOfLastBracket);
										String strn = str1.replaceAll(" ", " to £");
										String strn1 ="£"+strn;
										JSONObject temp = new JSONObject();
										temp.put("label", strn1);
										temp.put("entryValue", tempobj.getString("entryValue").toString());

										filtersList.add(temp);

									}
									finalobj.put("values", filtersList);
									//logger.severe(finalarr.toString());
									finalarr.add(finalobj);
									//logger.severe(finalarr.toString());
								}
							}

						}else{
							finalobj.put("name", "Price");
							for( int j=0; j<jobj.getJSONArray("Entry").length(); j++)
							{
								tempobj = (JSONObject) jobj.getJSONArray("Entry").get(j);
								String str1 = tempobj.getString("label");
								int indexOfOpenBracket = str1.indexOf("{");
								int indexOfLastBracket = str1.lastIndexOf("}");

								//logger.severe(str1.substring(indexOfOpenBracket+1, indexOfLastBracket));
								str1=str1.substring(indexOfOpenBracket+1, indexOfLastBracket);
								JSONObject temp = new JSONObject();
								temp.put("label", str1);
								String strn = "";
								String strn1="";
								
								if(j != jobj.getJSONArray("Entry").length()-1)
								{
								strn = str1.replaceAll(" ", " to £");
								strn ="£"+strn;
								}
								
								if(j==0)
								{
									strn = strn.replaceAll("\\*", "0");
								}
								if(j == jobj.getJSONArray("Entry").length()-1 )
								{
									
									String strn2 = str1.replaceAll("\\*", "");
									strn = "Above £"+strn2;
									
								}
								if(!checkIfEmpty(strn)){
									temp.put("label", strn);
									temp.put("entryValue", tempobj.getString("entryValue").toString());
									filtersList.add(temp);
								}

							}
							finalobj.put("values", filtersList);
							logger.severe(finalarr.toString());
							finalarr.add(finalobj);
							logger.severe(finalarr.toString());
						}
					}


					else if(jobj.containsKey("name") && !jobj.get("name").toString().equals("ParentCatalogGroup")){

						logger.severe("we will solve here");
						String string1 = jobj.getString("name");
						string1 = string1.replaceAll("[\\-()]", "");
						finalobj.put("name",string1);
						for( int j=0; j<jobj.getJSONArray("Entry").length(); j++)
						{
							tempobj = (JSONObject) jobj.getJSONArray("Entry").get(j);
							String str1 = (tempobj.getString("label")).toString();
							JSONObject temp = new JSONObject();
							if(!checkIfEmpty(str1)){
								temp.put("label", str1);
								temp.put("entryValue", tempobj.getString("entryValue").toString());
								filtersList.add(temp);
							}

						}
						finalobj.put("values", filtersList);
						logger.severe(finalarr.toString());
						finalarr.add(finalobj);
						logger.severe(finalarr.toString());
					}
					filtersList.clear();
					finalobj = new JSONObject();

				}

				resjobj.put("data",finalarr);

				return APIResponseBuilder.sendSuccessResponseStd(JSON.parse(finalarr.toString()));
			} 
			else{
				return APIResponseBuilder.sendFailResponseStd("IOExc in getSortingList:!",ServiceConstants.ERROR_CODE_INVALIDSEARCHTERM);

			}
		}
		catch (IOException e) {
			logger.severe("IOExc in getFilterListBySearchTerm:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND);

		} catch (JSONException e) {
			logger.severe("JSONExc in getFilterListBySearchTerm:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_IMPROPERJSON);

		} catch (Exception e) {
			logger.severe("Exc in getFilterListBySearchTerm:" + e);	
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION);

		}
	} 

	private boolean checkIfEmpty(String str1) {
		if(str1!=null && str1.equalsIgnoreCase(" "))
			return true;
		return false;
	}

	@GET
	@Path("/store/{storeId}/products/byId/{productId}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getProductById(@PathParam("storeId") String storeId,@PathParam("productId") String productId) {

		logger.severe("================Start of getProductById API================");
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		
		StringBuilder ratingUrl1 = new StringBuilder();
		StringBuilder reviewUrl = new StringBuilder();
		
		JSONArray reviewArr = null;
		String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String ratingUrl = AccessProperties.configFile.getProperty(ServiceConstants.RATINGANDREVIEWURL);
		String merchantId = AccessProperties.configFile.getProperty(ServiceConstants.MERCHANTID);
		String apiKey = AccessProperties.configFile.getProperty(ServiceConstants.APIKEY);
		String reviewpagesize = AccessProperties.configFile.getProperty(ServiceConstants.REVIEWPAGESIZE);

		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		String url1 = url+storeId+"/productview/byId/"+productId + "?responseFormat=" + responseFormat;

		//logger.severe("ProductById Url:"+url1);
		String sort = "created_date";
		String resp = ServiceConstants.EMPTY;
		try {   			

			Stopwatch stopwatchurl1 = new Stopwatch();
			stopwatchurl1.start();
			
			logger.severe("------------------------------------------");
			logger.severe("URL1 : " + url1);
			logger.severe("StoreId : " + storeId);
			
			JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
			
			stopwatchurl1.stop();
			long stopurl1 = stopwatchurl1.elapsed(TimeUnit.MILLISECONDS);
			long secondsurl1 = (stopurl1 / 1000) % 60;
			long minutesurl1 = (stopurl1 / 1000) / 60;
			logger.severe("Milliseconds : " + stopurl1 + "\tseconds : " + secondsurl1 + "\tMinutes : "
					+ minutesurl1);
			logger.severe("------------------------------------------");
			
			JSONArray arr = jsonObj.getJSONArray("CatalogEntryView");

			if(arr !=null) {   				
				ratingUrl1.append(ratingUrl).append("products/snapshot?apikey=").append(apiKey).append("&page_id=").append(arr.getJSONObject(0).getString("partNumber")).append("&merchant_id=").append(merchantId);
				//logger.severe("Rating URL:" + ratingUrl1.toString());
				reviewUrl.append(ratingUrl).append("reviews?apikey=").append(apiKey).append("&page_id=").append(arr.getJSONObject(0).getString("partNumber")).append("&sort=").append(sort).append("&merchant_id=").append(merchantId).append("&page_size=").append(reviewpagesize);  
				//logger.severe("Review URL ******************"+reviewUrl.toString());
				
				Stopwatch stopwatchratingurl1 = new Stopwatch();
				stopwatchratingurl1.start();
				
				logger.severe("------------------------------------------");
				logger.severe("ratingUrl1 : " + ratingUrl1);
				logger.severe("StoreId : " + storeId);
				
				JSONObject snapshot = ApiAdapter.callGetAPI(ratingUrl1.toString());
				
				stopwatchratingurl1.stop();
				long stopratingurl1 = stopwatchratingurl1.elapsed(TimeUnit.MILLISECONDS);
				long secondsratingurl1 = (stopratingurl1 / 1000) % 60;
				long minutesratingurl1 = (stopratingurl1 / 1000) / 60;
				logger.severe("Milliseconds : " + stopratingurl1 + "\tseconds : " + secondsratingurl1 + "\tMinutes : "
						+ minutesratingurl1);
				logger.severe("------------------------------------------");
				
				
				Stopwatch stopwatchreviewurl1 = new Stopwatch();
				stopwatchreviewurl1.start();
				
				logger.severe("------------------------------------------");
				logger.severe("reviewUrl : " + reviewUrl);
				logger.severe("StoreId : " + storeId);
				
				JSONObject review = ApiAdapter.callGetAPI(reviewUrl.toString());
				
				stopwatchreviewurl1.stop();
				long stopreviewurl1 = stopwatchreviewurl1.elapsed(TimeUnit.MILLISECONDS);
				long secondsreviewurl1 = (stopreviewurl1 / 1000) % 60;
				long minutesreviewurl1 = (stopreviewurl1 / 1000) / 60;
				logger.severe("Milliseconds : " + stopreviewurl1 + "\tseconds : " + secondsreviewurl1 + "\tMinutes : "
						+ minutesreviewurl1);
				logger.severe("------------------------------------------");
				
				if(review != null) {
					reviewArr = review.getJSONArray("data");
				}

				String suggProducts = getSuggestedProducts(storeId, (arr.getJSONObject(0).getString("partNumber")));
				for(int i=0;i<arr.length();i++)
				{
					ProductById prod = new ProductById();
					if(suggProducts!=null && !suggProducts.isEmpty())
						prod.setSuggestedProducts(new JSONArray(suggProducts));
					prod.setEntityFromJson(arr.getJSONObject(i), snapshot, reviewArr, storeId);
					resp = prod.toStringPID();
				}
				resp = ProductById.checkDisplayType(resp, jsonObj);
			}
			
			stopwatch.stop();
			long stop = stopwatch.elapsed(TimeUnit.MILLISECONDS);
			logger.severe("================ Response time for getProductById API================");
			long seconds = (stop / 1000) % 60;
			long minutes = (stop / 1000) / 60;
			logger.severe("Milliseconds : " + stop + "\tSeconds : " + seconds + "\tMinutes : " + minutes);

			logger.severe("================ Stop of getProductById API==========================");
			
			return APIResponseBuilder.sendSuccessResponseStd1(new JSONObject(resp));

		} catch (IOException e) {
			logger.severe("IOExc in getProductById:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND, e.toString());

		}catch (JSONException e) {
			logger.severe("JSONExc in getProductById:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(), ServiceConstants.ERROR_CODE_IMPROPERJSON ,e.toString());
		}
		catch (Exception e) {
			logger.severe("Exc in getProductById:" + e);	
			return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION ,e.getMessage());
		}
	}


//	@GET
//	@Path("/store/{storeId}/products/bysearchterm/{searchterm}/orderby/{sortorder}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@OAuthSecurity(enabled=false)
//	public Response getProductsBySorting(@PathParam("storeId") String storeId,@PathParam("searchterm") String searchterm,@PathParam("sortorder") String sortorder,@QueryParam("pageSize") String pageSize, @QueryParam("pageNumber") String pageNumber)
//	{
//
//		StringBuilder productBuilder = new StringBuilder();
//		StringBuilder productIds = new StringBuilder();
//		StringBuilder ratingUrl1 = new StringBuilder();
//		StringBuilder ratingCountUrl = new StringBuilder();
//		HashMap<String, String> ratingMap = new HashMap<String, String>();
//		HashMap<String, String> ratingCountMap = new HashMap<String, String>();
//
//		
//
//		String langId = AccessProperties.configFile.getProperty(ServiceConstants.LANGID);
//		String catalogId = AccessProperties.configFile.getProperty(ServiceConstants.CATALOGID);
//		String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);
//		String currency = AccessProperties.configFile.getProperty(ServiceConstants.CURRENTCY);
//		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
//		String ratingUrl = AccessProperties.configFile.getProperty(ServiceConstants.RATINGANDREVIEWURL);
//		String merchantId = AccessProperties.configFile.getProperty(ServiceConstants.MERCHANTID);
//		String apiKey = AccessProperties.configFile.getProperty(ServiceConstants.APIKEY);
//
//		String profileName = "X_findProductsBySearchTerm";
//		//    	JSONObject jsonObj = null;
//		boolean isPartNumberSearch = false;
//
//		if(pageNumber==null || pageNumber.isEmpty())
//			pageNumber="1";
//
//		if(pageSize==null || pageSize.isEmpty())
//			pageSize="10";
//		//	String url1 = url+storeId+"/productview/byCategory/"+CatId  + "?pageNumber=1&pageSize=10&langId=" + langId+"&orderBy="+sortorder;
//		String url1 = url+storeId+"/productview/bySearchTerm/"+searchterm + "?pageNumber=" + pageNumber +"&pageSize=" + pageSize +"&langId=" + langId + "&catalogId=" + catalogId + "&currency=" + currency +  "&responseFormat=" + responseFormat + "&profileName=" + profileName+"&orderBy="+sortorder;
//		logger.severe(url1);
//
//		try {
//			JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
//			logger.severe(jsonObj.toString());
//			JSONArray arr = jsonObj.getJSONArray("CatalogEntryView");
//			logger.severe("This JDONObject"+jsonObj.toString());
//			if(arr != null) {				
//				//				productBuilder.append("{"+"\n"+"\"recordSetTotal\""+":"+jsonObj.getString("recordSetTotal")+","+"\n");
//				//				productBuilder.append("\"recordSetCount\""+":"+jsonObj.getString("recordSetCount")+","+"\n"+"\"data\""+":"+"\n");
//
//				productBuilder.append("[");
//
//				for(int i=0;i<arr.length();i++)
//				{
//					if(i==0) {
//						productIds.append(arr.getJSONObject(i).getString("partNumber"));
//					} else {
//						productIds.append(",").append(arr.getJSONObject(i).getString("partNumber"));					
//					}
//				}
//				ratingUrl1.append(ratingUrl).append("products/rating?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
//				logger.severe(ratingUrl1.toString());
//				JSONArray ratingArr = ApiAdapter.callGetAPI(ratingUrl1);
//				for(int j=0; j<ratingArr.length();j++) {
//					ratingMap.put(ratingArr.getJSONObject(j).getString("page_id"), ratingArr.getJSONObject(j).getString("average_rating"));
//				}
//
//				ratingCountUrl.append(ratingUrl).append("products/review_count?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
//				logger.severe(ratingCountUrl.toString());
//				JSONArray ratingCountArr = ApiAdapter.callGetAPI(ratingCountUrl);
//				for(int j=0; j<ratingCountArr.length();j++) {
//					ratingCountMap.put(ratingCountArr.getJSONObject(j).getString("page_id"), ratingCountArr.getJSONObject(j).getString("num_reviews"));
//				}
//
//				for(int i=0;i<arr.length();i++)
//				{
//					ProductByCategory prod = new ProductByCategory();
//					prod.setEntityFromJson(arr.getJSONObject(i), ratingMap, ratingCountMap);
//					String resp = prod.toString();
//					productBuilder.append(resp);
//					if(i!=arr.length()-1)
//						productBuilder.append(","+"\n");				
//					resp = "";
//				}
//				productBuilder.append("]");
//				productBuilder.append("}");
//				logger.info(productBuilder.toString());
//			}
//
//			return APIResponseBuilder.sendSuccessResponseStd(JSON.parse(productBuilder.toString()));
//
//		} catch (IOException e) {
//			logger.severe("IOExc in getProductsBySorting:" + e.getMessage());
//			return APIResponseBuilder.sendFailResponseStd(e.toString(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND);
//		} catch (JSONException e) {
//			logger.severe("JSONExc in getProductsBySorting:" + e.toString());
//			return APIResponseBuilder.sendFailResponseStd(e.toString(), ServiceConstants.ERROR_CODE_IMPROPERJSON);
//		} catch (Exception e) {
//			logger.severe("Exc in getProductsBySorting:" + e.getMessage());	
//			return APIResponseBuilder.sendFailResponseStd(e.toString(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION);
//		}
//	}

	@GET
	@Path("/store/products/details/list")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getSortingList()
	{
		try {
			SortList prod = new SortList();
			String prod1 = prod.getSortEntities();
			String prod2 = prod.getSort1Entities();

			String[] newList = prod1.split(",");
			logger.severe("newlist"+ newList); 

			String[] newList1 = prod2.split(",");
			logger.severe("newlist"+ newList1);

			JSONArray mJSONArray = new JSONArray(Arrays.asList(newList));
			JSONArray mJSONArray1 = new JSONArray(Arrays.asList(newList1));

			JSONObject jobj2 = prod.getShippingValues();

			JSONObject obj1= new JSONObject();
			JSONObject obj2= new JSONObject();

			obj2.put("ProductListing", mJSONArray1);
			obj2.put("Reviews", mJSONArray);
			obj1.put("Sorting", obj2);
			obj1.put("Delivery", jobj2);

			return APIResponseBuilder.sendSuccessResponseStd(JSON.parse(obj1.toString()));
		} catch (NullPointerException | JSONException e) {
			logger.severe("Exc in getSortingList:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND);
		} catch (Exception e) {
			logger.severe("Exc in getSortingList:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION );
		}

	}

	public String getSuggestedProducts(@PathParam("storeId") String storeId, @PathParam("productId") String productId) {

		StringBuilder productBuilder = new StringBuilder();
		StringBuilder productIds = new StringBuilder();
		StringBuilder ratingUrl1 = new StringBuilder();
		StringBuilder ratingCountUrl = new StringBuilder();
		HashMap<String, String> ratingMap = new HashMap<String, String>();
		HashMap<String, String> ratingCountMap = new HashMap<String, String>();

		

		String url = AccessProperties.configFile.getProperty(ServiceConstants.RICHRELEVANCEURL);
		String ratingUrl = AccessProperties.configFile.getProperty(ServiceConstants.RATINGANDREVIEWURL);
		String merchantId = AccessProperties.configFile.getProperty(ServiceConstants.MERCHANTID);
		String apiKey = AccessProperties.configFile.getProperty(ServiceConstants.APIKEY);
		String url1 = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		String suggetsedProductPlacement = AccessProperties.configFile.getProperty(ServiceConstants.SUGGPROD_PLACEMENT);

		String producturl = ServiceConstants.EMPTY;
		String partNumber = ServiceConstants.EMPTY;
		String resp = ServiceConstants.EMPTY;
		JSONObject productJson = null;

		try {
			double sessionId =  Math.random();
			String url2 = url+sessionId+"&placements=" + suggetsedProductPlacement + productId;
			logger.severe("SuggestedProduct url:" + url2);
			JSONObject jsonObj = ApiAdapter.callGetAPI(url2);
			JSONArray arr = jsonObj.getJSONArray("placements").getJSONObject(0).getJSONArray("recommendedProducts");
			for(int i=0;i<arr.length();i++) {
				if(i==0) {
					productIds.append(arr.getJSONObject(i).getString("id"));
				} else {
					productIds.append(",").append(arr.getJSONObject(i).getString("id"));					
				}
			}
			ratingUrl1.append(ratingUrl).append("products/rating?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
			logger.severe("rating url for suggested product:" + ratingUrl1.toString());
			JSONArray ratingArr = ApiAdapter.callGetAPI(ratingUrl1);
			for(int j=0; j<ratingArr.length();j++) {
				ratingMap.put(ratingArr.getJSONObject(j).getString("page_id"), ratingArr.getJSONObject(j).getString("average_rating"));
			}
			ratingCountUrl.append(ratingUrl).append("products/review_count?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
			logger.severe("rating count url for suggested product:" +ratingCountUrl.toString());
			JSONArray ratingCountArr = ApiAdapter.callGetAPI(ratingCountUrl);
			for(int j=0; j<ratingCountArr.length();j++) {
				ratingCountMap.put(ratingCountArr.getJSONObject(j).getString("page_id"), ratingCountArr.getJSONObject(j).getString("num_reviews"));
			}

			productBuilder.append("["+"\n");
			for(int i=0;i<arr.length();i++)
			{
				partNumber = arr.getJSONObject(i).getString("id");
				producturl = url1 + storeId + "/productview/" + partNumber;
				logger.severe("part number url in suggProd:" + producturl.toString());
				try {
					productJson = ApiAdapter.callGetAPI(producturl.toString());					
				} catch (IOException e) {
					logger.severe("IOExc in getSuggestedProducts:" + e);
				} catch (Exception e) {
					logger.severe("Exc in getSuggestedProducts:" + e);	
				}

				if(productJson != null && (!productJson.isEmpty())) {
					JSONArray productArr = productJson.getJSONArray("CatalogEntryView");

					if(productArr !=null) {   				

						for(int j=0;j<productArr.length();j++)
						{
							ProductByCategory prod = new ProductByCategory();
							prod.setEntityFromJson(productArr.getJSONObject(j), ratingMap, ratingCountMap);
							resp = prod.toString();
							productBuilder.append(resp+"\n");				
						}
						if(i!=arr.length()-1)
							productBuilder.append(","+"\n");				
						resp = ServiceConstants.EMPTY;
						producturl = ServiceConstants.EMPTY;
						partNumber = ServiceConstants.EMPTY;
					}
				}
				productJson = null;
			}
			productBuilder.append("]");
			return productBuilder.toString();

		} catch (IOException e) {
			logger.severe("IOExc in getSuggestedProducts:" + e);
			return productBuilder.toString();
		} catch (JSONException e) {
			logger.severe("JSONExc in getSuggestedProducts:" + e);
			return productBuilder.toString();
		}
		catch (Exception e) {
			logger.severe("Exc in getSuggestedProducts:" + e);	
			return productBuilder.toString();
		}
	}   





	@GET
	@Path("/store/{storeId}/bestSellers")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getBestSellers(@PathParam("storeId") String storeId, @QueryParam("categoryId") String categoryId){

		StringBuilder productBuilder = new StringBuilder();
		StringBuilder productIds = new StringBuilder();
		StringBuilder ratingUrl1 = new StringBuilder();
		StringBuilder ratingCountUrl = new StringBuilder();
		HashMap<String, String> ratingMap = new HashMap<String, String>();
		HashMap<String, String> ratingCountMap = new HashMap<String, String>();

		
		String url = AccessProperties.configFile.getProperty(ServiceConstants.RICHRELEVANCEURL);
		String url2 = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		String ratingUrl = AccessProperties.configFile.getProperty(ServiceConstants.RATINGANDREVIEWURL);
		String merchantId = AccessProperties.configFile.getProperty(ServiceConstants.MERCHANTID);
		String apiKey = AccessProperties.configFile.getProperty(ServiceConstants.APIKEY);
		String bestSellersPlacement = AccessProperties.configFile.getProperty(ServiceConstants.BESTSELLER_PLACEMENT);

		String producturl = ServiceConstants.EMPTY;
		String partNumber = ServiceConstants.EMPTY;
		String resp = ServiceConstants.EMPTY;
		JSONObject productJson = null;
		String CatId = null;
		
		if(categoryId != null && (!categoryId.equalsIgnoreCase(""))) {
//			CatId = AccessProperties.configFile.getProperty(categoryId);
//			if(CatId==null || CatId.isEmpty())
				CatId=categoryId;
		} else {
			bestSellersPlacement = "home_page.instore";
			CatId = "";
		}
		
		double sessionId =  Math.random();
		String url1 = url+sessionId+"&placements=" + bestSellersPlacement + CatId;
		logger.severe(url1);

		try {
			JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
			logger.severe(jsonObj.toString());
			JSONArray arr = jsonObj.getJSONArray("placements").getJSONObject(0).getJSONArray("recommendedProducts");
			for(int i=0;i<arr.length();i++) {
				if(i==0) {
					productIds.append(arr.getJSONObject(i).getString("id"));
				} else {
					productIds.append(",").append(arr.getJSONObject(i).getString("id"));					
				}
			}
			ratingUrl1.append(ratingUrl).append("products/rating?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
			logger.severe(ratingUrl1.toString());
			JSONArray ratingArr = ApiAdapter.callGetAPI(ratingUrl1);
			for(int j=0; j<ratingArr.length();j++) {
				ratingMap.put(ratingArr.getJSONObject(j).getString("page_id"), ratingArr.getJSONObject(j).getString("average_rating"));
			}			
			ratingCountUrl.append(ratingUrl).append("products/review_count?apikey=").append(apiKey).append("&page_id=").append(productIds).append("&merchant_id=").append(merchantId);
			logger.severe(ratingCountUrl.toString());
			JSONArray ratingCountArr = ApiAdapter.callGetAPI(ratingCountUrl);
			for(int j=0; j<ratingCountArr.length();j++) {
				ratingCountMap.put(ratingCountArr.getJSONObject(j).getString("page_id"), ratingCountArr.getJSONObject(j).getString("num_reviews"));
			}

			productBuilder.append("{"+"\n");
			productBuilder.append("\"data\""+":"+"\n");
			productBuilder.append("["+"\n");
			for(int i=0;i<arr.length();i++)
			{
				partNumber = arr.getJSONObject(i).getString("id");
				producturl = url2 + storeId + "/productview/" + partNumber;
				logger.severe(producturl.toString());
				try {
					productJson = ApiAdapter.callGetAPI(producturl.toString());					
				} catch (IOException e) {
					logger.severe("IOExc in getSuggestedProducts:" + e);
				} catch (Exception e) {
					logger.severe("Exc in getSuggestedProducts:" + e);	
				}

				if(productJson != null && (!productJson.isEmpty())) {
					logger.severe(productJson.toString());
					JSONArray productArr = productJson.getJSONArray("CatalogEntryView");
	
					if(productArr !=null) {   				
	
						for(int j=0;j<productArr.length();j++)
						{
							ProductByCategory prod = new ProductByCategory();
							prod.setEntityFromJson(productArr.getJSONObject(j), ratingMap, ratingCountMap);
							resp = prod.toString();
							productBuilder.append(resp+"\n");				
						}
						if(i!=arr.length()-1)
							productBuilder.append(","+"\n");				
						resp = ServiceConstants.EMPTY;
						producturl = ServiceConstants.EMPTY;
						partNumber = ServiceConstants.EMPTY;
					}
				}
				productJson = null;
			}
			productBuilder.append("]");
			productBuilder.append("}");
			logger.severe(productBuilder.toString());
			return APIResponseBuilder.sendSuccessResponseStd1(new JSONObject(productBuilder.toString()));

		} catch (IOException e) {
			logger.severe("IOExc in getBestSellers:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);
		}catch (JSONException e) {
			logger.severe("JSONExc in getBestSellers:" + e);
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);
		}
		catch (Exception e) {
			logger.severe("Exc in getBestSellers:" + e);	
			return APIResponseBuilder.sendFailResponseStd(e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND1);
		}
	}

	
	
	
	@GET
	@Path("/store/{storeId}/deliveryInfo/bySkuId/{skuId}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getInfoBySkuId(@PathParam("storeId") String storeId,@PathParam("skuId") String productId) {


		String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);

		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		String url1 = url+storeId+"/productview/byId/"+productId + "?responseFormat=" + responseFormat;

		logger.severe(""+url1);
		JSONObject resp = new JSONObject();
		JSONObject finalResp = new JSONObject();
		try {   			

			JSONObject jsonObj = ApiAdapter.callGetAPI(url1);
			logger.severe(jsonObj.toString());
			JSONArray arr = jsonObj.getJSONArray("CatalogEntryView");

			if(arr !=null && arr.getJSONObject(0)!= null) {
				JSONObject obj = arr.getJSONObject(0);
				if(obj.has("Attributes"))
				{	     	
					JSONArray attrArr = obj.getJSONArray("Attributes");
					for(int j=0;j<attrArr.length();j++)
					{
						if(attrArr.getJSONObject(j).has("name") ) {

							if(attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("PickupInStore")){
								String pickupInStore = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0).getString("values");				

								if(pickupInStore.equalsIgnoreCase("1")) {
									resp.put("isPickUpInStore", true);    					
								} else {
									resp.put("isPickUpInStore", false);				
								}
							} else if(attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("AvailableForHomeDelivery")){
								String homeDelivery = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0).getString("values");				

								if(homeDelivery.equalsIgnoreCase("1")) {
									resp.put("isHomeDelivery", true);    					
								} else {
									resp.put("isHomeDelivery", false);				
								}
							}
						}
					}
				}
				resp.put("partNumber", obj.getString("partNumber"));
				String imageurl = AccessProperties.configFile.getProperty("imageurl");
				String image_url ="";
				ArrayList<String> image_array = new ArrayList<String>();
				if(obj.has("fullImage") && !obj.has("Attachments"))
				{
					String thumbnail = obj.getString("fullImage");
					
					if(thumbnail.contains("L")) {
						int index = thumbnail.indexOf(".");
						thumbnail = thumbnail.substring(0,index-1) + thumbnail.substring(index,thumbnail.length());
					}
					image_url = imageurl+thumbnail;
					image_array.add(image_url);
				}

				if(obj.has("Attachments"))
				{
					JSONArray obj1= obj.getJSONArray("Attachments");
					for(Object obje: obj1) {
						if(((JSONObject)obje).has("usage")) {		
							if(((JSONObject)obje).getString("usage").equals("ANGLEIMAGES_FULLIMAGE"))
							{
								String path = ((JSONObject)obje).getString("path").replace("L", "");
								image_url = imageurl+"/wcsstore/"+path;
								image_array.add(image_url);
							}
						}
					}
				}
				resp.put("image_url", image_array);				
			}
			finalResp.put("data", resp);
			return APIResponseBuilder.sendSuccessResponseStd1(finalResp);

		} catch (IOException e) {
			logger.severe("IOExc in getDeliveryInfoById:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(),ServiceConstants.ERROR_CODE_PRODUCTNOTFOUND, e.toString());

		}catch (JSONException e) {
			logger.severe("JSONExc in getDeliveryInfoById:" + e);
			return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(), ServiceConstants.ERROR_CODE_IMPROPERJSON ,e.toString());
		}
		catch (Exception e) {
			logger.severe("Exc in getDeliveryInfoById:" + e);	
			return APIResponseBuilder.sendFailResponse(new JSONObject(),e.getMessage(),ServiceConstants.ERROR_CODE_UNKNOWNEXCEPTION ,e.getMessage());
		}
	}
    
}

