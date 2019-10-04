package com.ibm.mobilefirst.retail.salesassist.api.cart.services;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import org.springframework.remoting.soap.SoapFaultException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ErrorConstants;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ErrorResponse;
import com.ibm.mobilefirst.util.LoggingSoapHandler;
import com.ibm.mobilefirst.vip.customersearch.messages.searchdata.SearchDataPort;
import com.ibm.mobilefirst.vip.customersearch.messages.searchdata.SearchDataPort_Service;
import com.ibm.mobilefirst.vip.customersearch.messages.searchdata.SearchDataRequest;
import com.ibm.mobilefirst.vip.customersearch.messages.searchdata.SearchDataRequestType;
import com.ibm.mobilefirst.vip.customersearch.messages.searchdata.SearchDataResponse;
import com.ibm.mobilefirst.vip.customersearch.types.base.KeyValuePair;
import com.ibm.mobilefirst.vip.customersearch.types.messageheader.MessageHeader;
import com.ibm.mobilefirst.vip.customersearch.types.messageheader.MessageHeader.HeaderData;
import com.ibm.mobilefirst.vip.customersearch.types.messageheader.MessageHeader.HeaderRouting;

/***
 * Customer Search VIP Service
 * 
 * @author Shilpa Sanganbatte
 *
 */
@SuppressWarnings("deprecation")
public class CustomerSearchService {
	static Logger logger = Logger.getLogger(CustomerSearchService.class
			.getName());

	HttpClient client = new DefaultHttpClient();
	HttpPost postReq = null;

	/***
	 * Get Order Details by calling order details API
	 * 
	 * @param orderNo
	 * @param shipmentNo
	 * @param shipNode
	 * @param sellerOrgCode
	 * @return JSON Object containing details of an order
	 */
	public String getCustomerData(String key,String value,String description) {
		
		
		SearchDataPort_Service service = new SearchDataPort_Service();
		
		ErrorResponse errorResponse = new ErrorResponse();
		
		String 	addressResponse = "";
		
		JSONObject finalResponse = new JSONObject();
		
		try {
			if (true) {
				service.setHandlerResolver(new HandlerResolver() {
					@SuppressWarnings("rawtypes")
					@Override
					public List<Handler> getHandlerChain(PortInfo portInfo) {
						List<Handler> handlerChain = new ArrayList<Handler>();
						handlerChain.add(new LoggingSoapHandler());
						return handlerChain;
					}
				});

			}

			 SearchDataPort port = service.getSearchDataPortPort();
			 
			 SearchDataRequest searchDataRequestRequest = new SearchDataRequest();
			 
			 //JSONArray requestSearchItmes = (JSONArray) request.get("searchItems");
			
			/*start header*/
			 MessageHeader header = new MessageHeader();
			
			 HeaderData headerData = new HeaderData();
			 headerData.setClientReference("OIS-00100229");
			 headerData.setIsException(false);
			 GregorianCalendar gcal = new GregorianCalendar();
				XMLGregorianCalendar time = DatatypeFactory.newInstance()
			            .newXMLGregorianCalendar(gcal);
			 headerData.setSubmissionDateTime(time);
			 header.setHeaderData(headerData );
			 
			 HeaderRouting headerRouting = new HeaderRouting();
			 headerRouting.setBrand("PetsAtHome");
			 headerRouting.setChannel("ORDERINSTORE");
			header.setHeaderRouting(headerRouting );
			 
			 searchDataRequestRequest.setMessageHeader(header);
			 
			 /*end header*/
			 
			 /*start body*/
			 SearchDataRequestType searchBody = new SearchDataRequestType();
			 
			 List<KeyValuePair> searchItems = searchBody.getSearchItem();
			 
				 KeyValuePair searchItem = new KeyValuePair();
				 searchItem.setKey(key);
				 searchItem.setValue(value);
				 searchItem.setDescription(description);
				 searchItems.add(searchItem);

			
			 searchBody.setSearchDepth("SUMMARY");
			 searchBody.setType("VIPCUSTOMER");
			  
			 searchDataRequestRequest.setMessageBody(searchBody);
			 /*end body*/
			 
			SearchDataResponse response = port.searchData(searchDataRequestRequest);
			
			String addressResponses= objectToString(response);
			logger.severe(addressResponses);
			
			if (response.getMessageBody()!=null && response.getMessageBody().getLoyalty().get(0).getMember().isEmpty())
			{
				JSONArray err = new JSONArray();
				JSONObject e = new JSONObject();
				e.put("code", 2001);
				e.put("message", "No customer records found");
				err.add(e);
				finalResponse.put("error",err);
			} else if (response.getMessageHeader()!=null &&
					response.getMessageHeader().getHeaderData().isIsException() && 
					response.getMessageHeader().getHeaderData().getException()!=null){
				
				JSONArray err = new JSONArray();
				JSONObject e = new JSONObject();
				e.put("code", 2004);
				logger.severe(response.getMessageHeader().getHeaderData().getException().getBaseException().getDescription());
				e.put("message", "Internal Server Error occurred!! Please try again after some time");
				err.add(e);
				finalResponse.put("error",err);
			} else {
				JSONObject result =new JSONObject(addressResponses);
				result.remove("messageHeader");
				JSONObject body = result.getJSONObject("messageBody");
				JSONArray loyality = body.getJSONArray("loyalty");
				
				JSONArray memebers = new JSONArray();
				JSONArray customerAddress = new JSONArray();
				
				for(int i=0;i<loyality.size();i++) {
					JSONObject loyalityObject = (JSONObject) loyality.get(i);
					
					memebers = loyalityObject.getJSONArray("member");
					
					for(int j=0;j<memebers.size();j++) {
						JSONObject member = ((JSONObject) memebers.get(j)).getJSONObject("addresses");
						customerAddress = member.getJSONArray("customerAddress");
						
						for(int k=0;k<customerAddress.size();k++) {
							
							JSONObject customerAddre= ((JSONObject) customerAddress.get(k)).getJSONObject("timeAtAddress");
							
							customerAddre.put("months", customerAddre.getString("months"));
							customerAddre.put("years", customerAddre.getString("years"));
						}
						
					}
					
				}
				
				finalResponse.put("data",result.get("messageBody"));
			}
			
			addressResponse= objectToString(finalResponse);
			
			logger.severe(addressResponse);
			
		} catch (Exception e) {

			String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS z").format(Calendar.getInstance().getTime());
			String message = e.getMessage();
			if (message.contains(ErrorConstants.SOAPERRORMESSAGE)) {
				SoapFaultException ex = (SoapFaultException) e;
				errorResponse.setcode(ErrorConstants.SOAPFAULTERROR);
				errorResponse.setErrorDescription(ex.getFaultString());
				errorResponse.setErrorTime(timeStamp);
			}
			else if (message.contains(ErrorConstants.READTIMEOUTMESSAGE)) {
				
				errorResponse.setcode(ErrorConstants.RUNTIMEOUT);
				errorResponse.setErrorDescription(message);
				errorResponse.setErrorTime(timeStamp);
			}else if (message.contains(ErrorConstants.CONNECTION_REFUSED)) {
				
				errorResponse.setcode(ErrorConstants.CONNECTION_REFUSED_ERRORCODE);
				errorResponse.setErrorDescription(ErrorConstants.CONNECTION_REFUSED_ERRORDESCRIPTION);
				errorResponse.setErrorTime(timeStamp);
			}
			else {
				errorResponse.setcode(ErrorConstants.OTHERS);
				errorResponse.setErrorDescription(message);
				errorResponse.setErrorTime(timeStamp);
			}
			
			logger.severe("Exception : "+e);
			String response = objectToString(errorResponse);
			logger.severe(response);
			return response;
		}

		return addressResponse;
	}
	
	public String objectToString(Object object){
		String result = "";
		ObjectMapper mapper = new ObjectMapper();
		StringWriter str = new StringWriter();
		try {
			mapper.writeValue(str, object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = str.toString();
		return result;
	}
	
}
