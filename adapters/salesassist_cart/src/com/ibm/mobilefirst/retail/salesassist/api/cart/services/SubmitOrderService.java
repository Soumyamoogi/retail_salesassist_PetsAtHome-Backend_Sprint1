package com.ibm.mobilefirst.retail.salesassist.api.cart.services;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
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
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.springframework.remoting.soap.SoapFaultException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mobilefirst.bte.submitorder.messages.submitorder.SubmitOrderRequest;
import com.ibm.mobilefirst.bte.submitorder.messages.submitorder.SubmitOrderRequestPpt;
import com.ibm.mobilefirst.bte.submitorder.messages.submitorder.SubmitOrderRequestPpt_Service;
import com.ibm.mobilefirst.bte.submitorder.messages.submitorder.SubmitOrderResponse;
import com.ibm.mobilefirst.bte.submitorder.types.address.CustomerAddress;
import com.ibm.mobilefirst.bte.submitorder.types.address.CustomerAddresses;
import com.ibm.mobilefirst.bte.submitorder.types.base.AllowedType;
import com.ibm.mobilefirst.bte.submitorder.types.contacts.ContactDetails;
import com.ibm.mobilefirst.bte.submitorder.types.contacts.Telephone;
import com.ibm.mobilefirst.bte.submitorder.types.contacts.TelephoneNumbers;
import com.ibm.mobilefirst.bte.submitorder.types.customer.Customer;
import com.ibm.mobilefirst.bte.submitorder.types.customer.Customer.Marketing;
import com.ibm.mobilefirst.bte.submitorder.types.duration.Duration;
import com.ibm.mobilefirst.bte.submitorder.types.messageheader.MessageHeader;
import com.ibm.mobilefirst.bte.submitorder.types.messageheader.MessageHeader.HeaderData;
import com.ibm.mobilefirst.bte.submitorder.types.messageheader.MessageHeader.HeaderRouting;
import com.ibm.mobilefirst.bte.submitorder.types.reference.Title;
import com.ibm.mobilefirst.bte.submitorder.types.submitordertype.OrderItemExtendedType;
import com.ibm.mobilefirst.bte.submitorder.types.submitordertype.OrderItemGroupsExtended;
import com.ibm.mobilefirst.bte.submitorder.types.submitordertype.SubmitOrderType;
import com.ibm.mobilefirst.bte.submitorder.types.submitordertype.SubmitOrderType.OrderHead;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ErrorConstants;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ErrorResponse;
import com.ibm.mobilefirst.retail.salesassist.adapter.cart.common.ServiceConstants;
import com.ibm.mobilefirst.util.LoggingSoapHandler;

/***
 * Address LookUP QAS Service
 * 
 * @author Shilpa Sanganbatte
 *
 */
@SuppressWarnings("deprecation")
public class SubmitOrderService {
	static Logger logger = Logger.getLogger(SubmitOrderService.class
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
	 * @throws JSONException 
	 */
	public JSONObject updateOrder(JSONObject request, String orderId) throws JSONException {
		
		logger.severe("ORDER ID IN UPDATE METHOD"+orderId);
		logger.severe("Request Body from app ^^^^^^^^^^^^^^^^^^^"+request.toString());
		
		
		SubmitOrderRequestPpt_Service service = new SubmitOrderRequestPpt_Service();
		
		ErrorResponse errorResponse = new ErrorResponse();
		
		JSONObject result = new JSONObject();
		
		String 	addressResponse = "";
		JSONObject clientRef = new JSONObject();
		
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

			SubmitOrderRequestPpt port = service.getSubmitOrderRequestPptPort();

			SubmitOrderRequest submitOrder = new SubmitOrderRequest();
			
			/* start header*/
			
			MessageHeader header = new MessageHeader();
			
			HeaderData hederData = new HeaderData();
			
			hederData.setClientReference("OIS-10853");
			GregorianCalendar gcal = new GregorianCalendar();
			XMLGregorianCalendar time = DatatypeFactory.newInstance()
		            .newXMLGregorianCalendar(gcal);
			hederData.setSubmissionDateTime(time);
			hederData.setIsException(false);
			hederData.setBranch(request.get("storeId").toString()); // from front end
			header.setHeaderData(hederData);
			
			HeaderRouting hederRouting = new HeaderRouting();
			hederRouting.setChannel(ServiceConstants.ORDERINSTORE);
			hederRouting.setBrand(ServiceConstants.BRAND);
			header.setHeaderRouting(hederRouting );
			
			submitOrder.setMessageHeader(header);
			/* end header*/
			
			/* start body*/
			SubmitOrderType messageBody = new SubmitOrderType();
			
			/* start setOrderHead*/
			OrderHead orderHeader = new OrderHead();
			orderHeader.setCampaign(ServiceConstants.ORDERINSTORE);
			orderHeader.setCurrencyCode(ServiceConstants.CURRENCY);
			orderHeader.setOrderDate(time);
			orderHeader.setOrderNumber("WCS-" + request.get("orderNumber").toString());
			orderHeader.setOrderType(ServiceConstants.ORDERINSTORE); 
			
			/*start customer */
			
			Customer customer = new Customer();
	
			for (Title c : Title.values()) {
		        if (c.name().equals(request.get("title").toString())) {
		        	customer.setTitle(c);
		        	break;
		        }
		    }

			customer.setFirstName(request.get("firstName").toString());
			customer.setLastName(request.get("lastName").toString());
			
			/*customer address start*/
			
			CustomerAddresses address = new CustomerAddresses();
			
			List<CustomerAddress> addresses = address.getCustomerAddress();
				
				CustomerAddress customerAddress = new CustomerAddress();
				
				customerAddress.setCountry(request.get("country").toString());
				customerAddress.setPostCode(request.get("zipCode").toString());
				customerAddress.setType("HOME");
				customerAddress.setAddressChecked(true);
				customerAddress.setIsBusinessAddress(false);
				Duration timeAddress = new Duration();
				timeAddress.setMonths(Integer.parseInt(((JSONObject)request.get("timeAtAddress")).getString("months")));
				timeAddress.setYears(Integer.parseInt(((JSONObject)request.get("timeAtAddress")).getString("years")));
				customerAddress.setTimeAtAddress(timeAddress);
				
			addresses.add(customerAddress);
			customer.setAddresses(address);
			/*customer address end*/
			
			/*customer contact start*/
			ContactDetails contact = new ContactDetails();
			TelephoneNumbers telephoneNos = new TelephoneNumbers();
			List<Telephone> telephonoNo = telephoneNos.getTelephoneNumber();
			Telephone telephoneNumber = new Telephone();
			
			telephoneNumber.setContactType("HOME");//HOME
			telephoneNumber.setNumber(request.get("phone2").toString()); // from screen , not VIP
			
			telephonoNo.add(telephoneNumber );
			contact.setTelephonesNumbers(telephoneNos);
			customer.setContacts(contact);
			/*customer contact end*/
			
			/*customer marketing end*/
			Marketing marketing = new Marketing();
			List<AllowedType> petsMarketing = marketing.getPetsAtHomeMarketing();
			
			AllowedType petsAtMarketing = new AllowedType();
			petsAtMarketing.setAllowed(false); // need to discuss
			petsAtMarketing.setType("MAIL"); // need to talk on this as well
			
			petsMarketing.add(petsAtMarketing );
			customer.setMarketing(marketing);
			/*customer marketing end*/
			
			orderHeader.setCustomer(customer);
			/*end customer */
			
			messageBody.setOrderHead(orderHeader);
			/* end setOrderHead*/
			
			
			/* start orderItemGroup*/
			List<OrderItemGroupsExtended> orderItemsGroups = messageBody.getOrderItemGroups();
			
			OrderItemGroupsExtended orderItemsGroup = new OrderItemGroupsExtended();
			
			orderItemsGroup.setItemCode("7500565"); // is it going to be orderNumber/orderId
			orderItemsGroup.setQuantity(1); // need to discuss
			orderItemsGroup.setDescription("Order in store");
			orderItemsGroup.setValue(0); // need to discuss
			orderItemsGroup.setItemLineId(1);
			orderItemsGroup.setUsageCode("OIS");
			orderItemsGroup.setItemNetAmount(new BigDecimal(request.getString("orderTotalAmount"))); // total amout of number of items, need to talk
			orderItemsGroup.setItemVatCode("STANDARD");
			orderItemsGroup.setProductGroupId("OIS");			
		
			List<OrderItemExtendedType> orderItemList = orderItemsGroup.getOrderItems();
			
			JSONArray cartItems = (JSONArray) request.get("cartItems");
			
			int lintItemId=0;
			
			//for(int i=0;i<cartItems.size();i++) {
				
				OrderItemExtendedType orderItemExteType = new OrderItemExtendedType();
				lintItemId ++;
				JSONObject cartItem = (JSONObject) cartItems.get(0);
				logger.severe("Order Id from cart item --------------"+cartItem.getString("orderItemId"));
				//orderItemExteType.setItemCode(cartItem.getString("orderItemId"));// indiviual orderItemId
				orderItemExteType.setItemCode("7500565");// indiviual orderItemId alway set to dummy value
				orderItemExteType.setQuantity(cartItem.getInt("orderItemQuantity")); // number of qunatity for that orderItemId
//				orderItemExteType.setItemCode("7500565");// indiviual orderItemId
//				orderItemExteType.setQuantity(1); // number of qunatity for that orderItemId
				orderItemExteType.setValue(Float.parseFloat(cartItem.getString("OrderItemUnitPrice"))); // total orderItemId price/Quantity
				orderItemExteType.setDescription(cartItem.getString("orderItemDescription"));
				orderItemExteType.setIsSubscriptionProduct(false);
				orderItemExteType.setMandatoryOrderItem(false);
				orderItemExteType.setItemLineId(lintItemId);
				orderItemExteType.setItemNetAmount(new BigDecimal(request.getString("orderTotalAmount")));
				
				orderItemExteType.setItemVatCode("STANDARD");
				orderItemExteType.setProductGroupId("OIS");
			
				orderItemList.add(orderItemExteType);
			//}
			orderItemsGroups.add(orderItemsGroup); // need to discuss , how many they can send at a time
			
			/* end orderItemGroup*/
			
			submitOrder.setMessageBody(messageBody);
			
			/* end body */
		logger.severe("BTE Request..............."+submitOrder.toString());
			logger.severe("Another log1 ###### "+submitOrder.getMessageBody().toString());
				//logger.severe("Another log 2###### "+submitOrder.getMessageBody().getOrderNumber().toString());
			if (submitOrder.getMessageBody().getOrderItemGroups() != null && submitOrder.getMessageBody().getOrderItemGroups().getClass().isInstance(OrderItemGroupsExtended.class)){
				logger.severe("Another log3 ###### "+((OrderItemGroupsExtended)submitOrder.getMessageBody().getOrderItemGroups()).getItemType());
				logger.severe("Another log3 ###### "+((OrderItemGroupsExtended)submitOrder.getMessageBody().getOrderItemGroups()).getQuantity());
				//logger.severe("Another log3 ###### "+submitOrder.getMessageBody());
			}
//			logger.severe("Another log4 ###### "+submitOrder.getMessageBody().getShippingDetails().toString());
//			logger.severe("Another log5 ###### "+submitOrder.getMessageBody().getOrderHead().toString());
			
			SubmitOrderResponse response = port.submitOrder(submitOrder);
			
			if(response.getMessageHeader()!=null && response.getMessageHeader().getHeaderData().getClientReference()!=null) {
				clientRef.put("clientReference", response.getMessageHeader().getHeaderData().getClientReference());
				clientRef.put("orderId",orderId);
				
				logger.severe("ORDER ID AFTER RESPONSE"+orderId);
				result.put("data", clientRef);
				result.put("error", new JSONArray());
			} else if(response.getMessageHeader()!=null && response.getMessageHeader().getHeaderData().isIsException() && 
					response.getMessageHeader().getHeaderData().getException()!=null) {
				
				JSONArray error = new JSONArray();
				JSONObject errors = new JSONObject();
				errors.put("code", 2003);
				errors.put("message", response.getMessageHeader().getHeaderData().getException().getBaseException().getDescription());
				error.add(errors);
				result.put("data", clientRef);
				result.put("error", error);
			}
			
			addressResponse = objectToString(response);
			logger.severe("BTE Response:"+addressResponse);
			
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
			} else if (message.contains(ErrorConstants.CONNECTION_REFUSED)) {
				
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
			return new JSONObject(response);
		}

		return result;
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
