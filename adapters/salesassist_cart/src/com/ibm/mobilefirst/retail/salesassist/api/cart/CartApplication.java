/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.ibm.mobilefirst.retail.salesassist.api.cart;

import java.util.logging.Logger;

import com.ibm.mobilefirst.util.AccessProperties;
import com.worklight.wink.extensions.MFPJAXRSApplication;

public class CartApplication extends MFPJAXRSApplication{

	static Logger logger = Logger.getLogger(CartApplication.class.getName());
	
	@Override
	protected void init() throws Exception {
		logger.info("Adapter initialized!");
		AccessProperties.getInstance();
	}
	
	@Override
	protected void destroy() throws Exception {
		logger.info("Adapter destroyed!");
	}
	
	@Override
	protected String getPackageToScan() {
		//The package of this class will be scanned (recursively) to find JAX-RS resources. 
		//It is also possible to override "getPackagesToScan" method in order to return more than one package for scanning
		return getClass().getPackage().getName();
	}
}
