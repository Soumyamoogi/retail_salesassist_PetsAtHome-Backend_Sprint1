package com.ibm.mobilefirst.retail.salesassist.adapter.cart.common;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"code",
"ErrorDescription",
"ErrorTime",
"message"
})
public class ErrorResponse {

@JsonProperty("code")
private int code;
@JsonProperty("ErrorDescription")
private String errorDescription;
@JsonProperty("ErrorTime")
private String errorTime;
@JsonProperty("message")
private String message;

/**
* 
* @return
* The code
*/
@JsonProperty("code")
public int getcode() {
return code;
}

/**
* 
* @param code
* The code
*/
@JsonProperty("code")
public void setcode(int code) {
this.code = code;
}

/**
* 
* @return
* The errorDescription
*/
@JsonProperty("ErrorDescription")
public String getErrorDescription() {
return errorDescription;
}

/**
* 
* @param errorDescription
* The ErrorDescription
*/
@JsonProperty("ErrorDescription")
public void setErrorDescription(String errorDescription) {
this.errorDescription = errorDescription;
}

/**
* 
* @return
* The errorTime
*/
@JsonProperty("ErrorTime")
public String getErrorTime() {
return errorTime;
}

/**
* 
* @param errorTime
* The ErrorTime
*/
@JsonProperty("ErrorTime")
public void setErrorTime(String errorTime) {
this.errorTime = errorTime;
}

/**
* 
* @return
* The errorTime
*/
@JsonProperty("message")
public String getMessage() {
return message;
}

/**
* 
* @param errorTime
* The ErrorTime
*/
@JsonProperty("message")
public void setMessage(String message) {
this.message = message;
}


}