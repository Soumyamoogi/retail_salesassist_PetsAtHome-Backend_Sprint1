package com.ibm.mobilefirst.retail.salesassist.adapter.cart.common;

import java.util.logging.Logger;

public class ServiceConstants {
	
	static Logger logger = Logger.getLogger(ServiceConstants.class.getName());

	public static final int ERROR_CODE_OK = 200;
	public static final int ERROR_CODE_OK1 = 500;

	public static final String SUCCESS_CODE = "200";
	public static final String ERROR_CODE_1 = "501";
	public static final String ERROR_CODE_2 = "500";
	public static final String KEYPREFIX_STORES = "store:";
	public static final String KEYPREFIX_ORDER = "order:";

	public static final String SUCCESS_MESSAGE = "Success";

	// Error codes
	public static final String ERROR_CODE_INVALIDSEARCHTERM = "1007";
	public static final int ERROR_CODE_IMPROPERJSON = 1008;
	public static final String ERROR_CODE_UNKNOWNEXCEPTION = "1009";
	public static final String ERROR_CODE_OCCURRENCEID = "1010";
	public static final String ERROR_CODE_PRODUCTNOTFOUND = "1010";
	public static final int ERROR_CODE_INTERNALSERVERERROR = 500;
	public static final int ERROR_CODE_COMMON = 422;
	public static final String PRECHECKOURT_ERRORMSG = "Your request cannot be completed, as one or more of the products you wish to purchase are not available in the quantity you requested";
	//property file elements 
	
	public static final String XMLIO = "xmlIO";
	public static final String URLSSL = "urlssl";
	public static final String URL = "url";
	public static final String IMAGEURL = "imageurl";
	public static final String LANGID = "langId";
	public static final String CATALOGID = "catalogId";
	public static final String RESPONSE_FORMATE = "responseFormat";
	public static final String CURRENTCY = "currency";
	public static final String PHYSICALID = "physicalStoreId";
	public static final String CATEGORYID = "CatId";
	public static final String RATINGANDREVIEWURL = "ratingAndReviewUrl";
	public static final String MERCHANTID = "merchantId";
	public static final String MERCHANTGROUPID = "merchantGroupId";
	public static final String APIKEY = "apiKey";
	public static final String ORDERINSTORE = "ORDERINSTORE";
	public static final String BRAND = "PetsAtHome";
	public static final String CURRENCY = "GBP";

	public static final String EMPTY_VALUE = "";
	
	public static final String ORDERID = "orderId";
	public static final String CARTID = "cartId";
	public static final String QUANTITY = "quantity";
	public static final String USERID = "userId";
	public static final String PRODUCTID = "productId";
	public static final String ORDERITEM = "orderItem";
	public static final String ERRORS = "errors";
	public static final String ORDERITEMID = "orderItemId";
	public static final String WCTOKEN = "WCToken";
	public static final String WCTRUSTEDTOKEN = "WCTrustedToken";
	public static final String ERROR = "error";
	public static final String GUEST = "guest";
	public static final String CARTSELF = "/cart/@self?responseFormat=";
	public static final String ORDERSTATUS = "orderStatus";
	
}
