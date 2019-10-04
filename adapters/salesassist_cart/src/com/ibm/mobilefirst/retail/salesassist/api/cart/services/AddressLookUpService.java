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
import org.springframework.remoting.soap.SoapFaultException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.json.java.JSONArray;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ErrorConstants;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ErrorResponse;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ServiceConstants;
import com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup.AddresslookupResponse;
import com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup.Data;
import com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup.Error;
import com.ibm.mofilefirst.qas.addresslookup.messages.addresslookup.AddressLookupRequest;
import com.ibm.mofilefirst.qas.addresslookup.messages.addresslookup.AddressLookupRequestType;
import com.ibm.mofilefirst.qas.addresslookup.messages.addresslookup.AddressLookupResponse;
import com.ibm.mofilefirst.qas.addresslookup.messages.addresslookup.AddressValidation;
import com.ibm.mofilefirst.qas.addresslookup.messages.addresslookup.AddressValidation11;
import com.ibm.mofilefirst.qas.addresslookup.types.address.Address;
import com.ibm.mofilefirst.qas.addresslookup.types.messageheader.MessageHeader;
import com.ibm.mofilefirst.qas.addresslookup.types.messageheader.MessageHeader.HeaderData;
import com.ibm.mofilefirst.qas.addresslookup.types.messageheader.MessageHeader.HeaderRouting;
import com.ibm.mobilefirst.util.LoggingSoapHandler;

/***
 * Address LookUP QAS Service
 * 
 * @author Shilpa Sanganbatte
 *
 */
@SuppressWarnings("deprecation")
public class AddressLookUpService {
	static Logger logger = Logger.getLogger(AddressLookUpService.class
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
	public String getAddressLookup(String postCode) {
		
		AddressValidation11 service = new AddressValidation11();
		
		ErrorResponse errorResponse = new ErrorResponse();
		
		AddresslookupResponse finalresponse = new AddresslookupResponse();
		
		String 	addressResponse = "";
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

			AddressValidation port = service.getAddressValidationPort11();

			AddressLookupRequest addReq = new AddressLookupRequest();

			/*header start*/
			MessageHeader header = new MessageHeader();
			
			HeaderData headerData = new HeaderData();
			headerData.setClientReference("my reference number");
			GregorianCalendar gcal = new GregorianCalendar();
			XMLGregorianCalendar time = DatatypeFactory.newInstance()
		            .newXMLGregorianCalendar(gcal);
			headerData.setSubmissionDateTime(time);
			headerData.setIsException(false);
			header.setHeaderData(headerData);
			
			HeaderRouting headerRouting = new HeaderRouting();
			headerRouting.setBrand(ServiceConstants.BRAND);
			headerRouting.setChannel("OrderInStore");
			header.setHeaderRouting(headerRouting);
			
			addReq.setMessageHeader(header);
			/*header end*/
			
			AddressLookupRequestType messageBody = new AddressLookupRequestType();
			
			messageBody.setAddressChecked(false);
			messageBody.setIsBusinessAddress(false);
			messageBody.setType("Home");
			messageBody.setPostCode(postCode);
			addReq.setMessageBody(messageBody);
			

			AddressLookupResponse response = port.lookupAddress(addReq);
			String s = objectToString(response);
			logger.severe(s);
			
			if(response.getMessageBody()!= null && 
					!response.getMessageBody().getAddress().toString().isEmpty()) {
			
				List<Address> addresses = response.getMessageBody().getAddress();
				
				ArrayList<com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup.Address> addressArray = 
						new ArrayList<com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup.Address>();
				
				for(Address address : addresses) {
					com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup.Address currentAddress = 
							new  com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup.Address();
					
					currentAddress.setCountry(address.getCountry()!=null?address.getCountry():ServiceConstants.EMPTY_VALUE);
					currentAddress.setCounty(address.getCounty()!=null?address.getCounty():ServiceConstants.EMPTY_VALUE);
					currentAddress.setFlat(address.getFlat()!=null?address.getFlat():ServiceConstants.EMPTY_VALUE);
					currentAddress.setHouseName(address.getHouseName()!=null?address.getHouseName():ServiceConstants.EMPTY_VALUE);
					currentAddress.setHouseNumber(address.getHouseNumber()!=null?address.getHouseNumber():ServiceConstants.EMPTY_VALUE);
					currentAddress.setLocality1(address.getLocality1()!=null?address.getLocality1():ServiceConstants.EMPTY_VALUE);
					currentAddress.setLocality2(address.getLocality2()!=null?address.getLocality2():ServiceConstants.EMPTY_VALUE);
					currentAddress.setPostCode(address.getPostCode()!=null?address.getPostCode():ServiceConstants.EMPTY_VALUE);
					currentAddress.setStreetLine1(address.getStreetLine1()!=null?address.getStreetLine1():ServiceConstants.EMPTY_VALUE);
					currentAddress.setStreetLine2(address.getStreetLine2()!=null?address.getStreetLine2():ServiceConstants.EMPTY_VALUE);
					currentAddress.setTown(address.getTown()!=null?address.getTown():ServiceConstants.EMPTY_VALUE);
					currentAddress.setType(address.getType()!=null?address.getType():ServiceConstants.EMPTY_VALUE);
					
					addressArray.add(currentAddress);
				}
				
				Data data = new Data();
				data.setAddress(addressArray);
				finalresponse.setData(data);
				finalresponse.setError(new JSONArray());
				
				addressResponse = objectToString(finalresponse);
				logger.severe(addressResponse);
				
			} else if(response.getMessageHeader()!=null && response.getMessageHeader().getHeaderData().isIsException() &&
					response.getMessageHeader().getHeaderData().getException()!=null) {
				
				List<Error> error = new ArrayList<Error>();
				Error errors = new Error();
				errors.setCode(1000);
				errors.setMessage(response.getMessageHeader().getHeaderData().getException().getBaseException().getDescription());
				error.add(errors);
				finalresponse.setError(error );
				
				Data data = null;
				finalresponse.setData(data );
				addressResponse = objectToString(finalresponse);
				logger.severe(addressResponse);
			}
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
