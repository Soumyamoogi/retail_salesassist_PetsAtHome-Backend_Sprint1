package com.ibm.mobilefirst.retail.salesassist.adapter.healthcheck.common;

public class ErrorConstants {

	public ErrorConstants() {
		// TODO Auto-generated constructor stub
	}

	public static final int SOAPFAULTERROR = 2002;
	public static final int RUNTIMEOUT = 2001;
	public static final int OTHERS = 2003;
	public static final String SOAPERRORMESSAGE = "SOAP Fault from server";
	public static final String READTIMEOUTMESSAGE = "Operation timed out";
	public static final String CONNECTION_REFUSED = "Connection refused";
	public static final int CONNECTION_REFUSED_ERRORCODE = 102;
	public static final String CONNECTION_REFUSED_ERRORDESCRIPTION = "Error connecting to SOMS";
	public static final String RETAILAPI_ERROR_CODE = "2004";
	public static final String RETAILSAPI_ERROR_DESCRIPTION = "Error connecting Retail Service";
	public static final String CUSTOMER_SIGNATURE_ERROR_CODE = "2005";
	public static final String CUSTOMER_SIGNATURE_ERROR_DESCRIPTION = "Error Connecting Signature Post Service";
	public static final String ORDER_ALREADY_PICKED = "Status is not a valid pipeline pickup status for the transaction";
	public static final String ORDER_ALREADY_PICKED_ERRORCODE = "110";
	public static final String SCRACH_ERROR_CODE="111";
	public static final String ERROR_DESC = "Error in processing request";
	public static final String ERROR_STRING = "Error";

}
