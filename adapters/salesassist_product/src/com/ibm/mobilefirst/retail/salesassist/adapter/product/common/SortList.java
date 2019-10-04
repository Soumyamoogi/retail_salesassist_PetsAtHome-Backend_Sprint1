package com.ibm.mobilefirst.retail.salesassist.adapter.product.common;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.ibm.mobilefirst.util.AccessProperties;

public class SortList {
	
	
	 public String getSortEntities()
	    {
		
	   String detailspage1 = AccessProperties.configFile.getProperty("detailsfield1");
	 
	   return detailspage1;
	   
	    }
	 
	 public String getSort1Entities()
	    {
		
	   String detailsfield2 = AccessProperties.configFile.getProperty("detailsfield2");
	 
	   return detailsfield2;
	   
	    }
	 
	 public JSONObject getShippingValues()
	    {
		
	     String basevalue = AccessProperties.configFile.getProperty("basevalue");
	     String deliverycharges = AccessProperties.configFile.getProperty("deliverycharges");
	     
	     JSONObject jobject = new JSONObject();
	     try {
			jobject.put("BaseValue", basevalue);
	
	     jobject.put("DeliveryCharges", deliverycharges);
	     return jobject;
	     }
	   
	     catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new JSONObject();
			}
	    }

}
