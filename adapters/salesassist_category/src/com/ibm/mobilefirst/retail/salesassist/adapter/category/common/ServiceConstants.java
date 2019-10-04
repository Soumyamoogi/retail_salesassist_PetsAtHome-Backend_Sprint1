package com.ibm.mobilefirst.retail.salesassist.adapter.category.common;

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
	public static final String ERROR_CODE_IMPROPERJSON = "1008";
	public static final String ERROR_CODE_UNKNOWNEXCEPTION = "1009";
	public static final String ERROR_CODE_OCCURRENCEID = "1010";
	public static final String ERROR_CODE_PRODUCTNOTFOUND = "1011";
	public static final String ERROR_CODE_INTERNALSERVERERROR = "500";
	
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
	
	
//	public static final String SYSTEM_PROP_NAME = "PETS_CONFIG";
//	public static final String PROP_FILE_NAME=System.getProperty(SYSTEM_PROP_NAME);
//
//	 static String wsdlPath;
//	 static String getXMLIO;
//	 static String urlssl;
//	 static String url;
//	 static String imageurl;
//	 static String langId;
//	 static String catalogId;
//	 static String responseFormat;
//	 static String currency;
//	 static String physicalStoreId;
//	 static String CatId;
//
//	static int HTTPCLIENT_TIMEOUT;
//	
//	static{
//		try {
//			AccessProperties.getInstance().getFile();
//			getXMLIO= AccessProperties.configFile.getProperty("xmlIO");
//			wsdlPath= AccessProperties.configFile.getProperty("STAPLES_WSDL_PATH");	
//			wsdlPath= AccessProperties.configFile.getProperty("urlssl");	
//			wsdlPath= AccessProperties.configFile.getProperty("url");	
//			wsdlPath= AccessProperties.configFile.getProperty("imageurl");	
//			wsdlPath= AccessProperties.configFile.getProperty("langId");	
//			wsdlPath= AccessProperties.configFile.getProperty("catalogId");	
//			wsdlPath= AccessProperties.configFile.getProperty("responseFormat");	
//			wsdlPath= AccessProperties.configFile.getProperty("currency");	
//			wsdlPath= AccessProperties.configFile.getProperty("physicalStoreId");	
//			wsdlPath= AccessProperties.configFile.getProperty("CatId");	
//			
//		} catch (Exception e) {
//			logger.severe("IOException : "+e);
//		}
//		
//	}
//	
//	
//	
//	public static String getWsdlPath() {
//		return wsdlPath;
//	}
//
//	
//
//	public static String getGetXMLIO() {
//		return getXMLIO;
//	}
//
//	public static String getUrlSsl() {
//		return urlssl;
//	}
//	
//	public static String getURL() {
//		return url;
//	}
//	
//	public static String getImageUrl() {
//		return imageurl;
//	}
//	
//	public static String getLandId() {
//		return langId;
//	}
//	
//	public static String getCatalogId() {
//		return catalogId;
//	}
//	
//	public static String getResponseFormat() {
//		return responseFormat;
//	}
//	
//	public static String getCurrency() {
//		return currency;
//	}
//	
//	public static String getPysicalStoreId() {
//		return physicalStoreId;
//	}
//	
//	public static String getCatId() {
//		return CatId;
//	}
//
//	/**
//	 * @return the hTTPCLIENT_TIMEOUT
//	 */
//	public static int getHTTPCLIENT_TIMEOUT() {
//		return HTTPCLIENT_TIMEOUT;
//	}
	
}
