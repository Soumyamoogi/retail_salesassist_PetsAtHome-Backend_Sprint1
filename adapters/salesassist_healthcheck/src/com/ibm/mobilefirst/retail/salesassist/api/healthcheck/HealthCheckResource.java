/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.healthcheck;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ibm.mobilefirst.retail.salesassist.api.healthcheck.services.AddressLookUpService;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.worklight.core.auth.OAuthSecurity;

@Path("/")
public class HealthCheckResource {
	/*
	 * For more info on JAX-RS see https://jsr311.java.net/nonav/releases/1.1/index.html
	 */
		
	//Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(HealthCheckResource.class.getName());

    //Define the server api to be able to perform server operations
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();

	/* Path for method: "<server address>/PetsAtHome/adapters/salesassist_healthcheck/users" */
	
    
    @GET
	@Path("/addressLookup")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public String getAddressLookup() {

    	String postCode = "SK9 3RN";
		AddressLookUpService service = new AddressLookUpService();
		return service.getAddressLookup(postCode);

	}
		
}
