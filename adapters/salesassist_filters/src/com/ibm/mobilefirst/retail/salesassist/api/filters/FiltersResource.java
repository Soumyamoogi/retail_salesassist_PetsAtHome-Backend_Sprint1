/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.filters;

import java.io.IOException;
import java.util.ArrayList;
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

import com.ibm.mobilefirst.retail.salesassist.adapter.filters.common.APIResponseBuilder;
import com.ibm.mobilefirst.retail.salesassist.adapter.filters.common.FilterLists;
import com.ibm.mobilefirst.retail.salesassist.adapter.filters.common.ServiceConstants;
import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.adapters.rest.api.MFPServerOperationException;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class FiltersResource {

	static Logger logger = Logger.getLogger(FiltersResource.class.getName());

    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();


	@GET
	@Path("/store/{storeId}/filterlist/all/search/{searchterm}")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled=false)
	public Response getFiltersListByCategory(@PathParam("storeId") String storeId, @PathParam("searchTerm") String searchTerm){
		StringBuilder productBuilder = new StringBuilder();
		
		String langId = AccessProperties.configFile.getProperty("langId");
		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		
		String url1 = url+storeId+"/productview/bySearchTerm/"+searchTerm + "?pageNumber=1&pageSize=10&langId=" + langId;
		logger.severe(url1);
		JSONArray arr = new JSONArray();
		JSONArray finalarr = new JSONArray();
		JSONObject finalobj = new JSONObject();
		JSONArray arr1 = new JSONArray();
		List<String> filtersList = new ArrayList<String>();
		JSONObject tempobj = new JSONObject();


		JSONObject jsonObj;
		try {
			jsonObj = FilterLists.callGetAPI(url1);
			logger.severe(jsonObj.toString());
			arr = jsonObj.getJSONArray("FacetView");

			for( int i=0; i<arr.length() ;i++)
			{
				logger.severe(""+i);
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

										logger.severe(str1.substring(indexOfOpenBracket+1, indexOfLastBracket));
										str1=str1.substring(indexOfOpenBracket+1, indexOfLastBracket);
										String x = str1.substring(0, 3) + "-" + str1.substring(3, str1.length());
										filtersList.add(x);

									}
									finalobj.put("values", filtersList);
									logger.severe(finalarr.toString());
									finalarr.add(finalobj);
									logger.severe(finalarr.toString());
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

									logger.severe(str1.substring(indexOfOpenBracket+1, indexOfLastBracket));
									str1=str1.substring(indexOfOpenBracket+1, indexOfLastBracket);
									String x = str1.substring(0, 3) + "-" + str1.substring(3, str1.length());
									
									filtersList.add(x);

								}
								finalobj.put("values", filtersList);
								logger.severe(finalarr.toString());
								finalarr.add(finalobj);
								logger.severe(finalarr.toString());
							}
						}

	
				else if(jobj.containsKey("name")){
					
	//					finalobj.put("name", jobj.get("name").toString());
					logger.severe("we will solve here");
						finalobj.put("name",jobj.getString("name"));
						for( int j=0; j<jobj.getJSONArray("Entry").length(); j++)
						{
							tempobj = (JSONObject) jobj.getJSONArray("Entry").get(j);
							filtersList.add(tempobj.getString("label"));

						}
						finalobj.put("values", filtersList);
						logger.severe(finalarr.toString());
						finalarr.add(finalobj);
						logger.severe(finalarr.toString());
					}
				filtersList.clear();
				finalobj = new JSONObject();

			}
    		return APIResponseBuilder.sendSuccessResponse(JSON.parse(finalarr.toString()));

		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
	        return APIResponseBuilder.sendFailResponse(new JSONObject(),"JSONExeption occured in getFiltersListByCategory", ServiceConstants.ERROR_CODE_IMPROPERJSON ,e.getMessage().toString());

		}
//		logger.severe("we are outside new adapter");
//		return finalarr;

	}
		
}
