/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.promocode;

import java.util.*;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

class Soriting implements Comparator{  
	 
	@Override
 	public int compare(Object o1,Object o2){  
 		
 		String s1="";
 		String s2="";
 		try {
 			
 			JSONObject oo1 = new JSONObject(o1);
 			JSONObject oo2 = new JSONObject(o2);
 			
 			s1 = oo1.getString("displayStoreName");
 			s2=oo2.getString("displayStoreName");  
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}  
 
 		return s1.compareTo(s2);  
 
 	}
 
 } 