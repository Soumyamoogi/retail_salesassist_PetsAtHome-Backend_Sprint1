
package com.ibm.mobilefirst.retail.salesassist.api.cart.javabeans.addresslookup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "data",
    "error"
})
public class AddresslookupResponse {

    @JsonProperty("data")
    private Data data;
    
    @JsonProperty("error")
    private List<Error> error = new ArrayList<Error>();


    /**
     * 
     * @return
     *     The data
     */
    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }
    
    /**
     * 
     * @return
     *     The error
     */
    @JsonProperty("error")
    public List<Error> getError() {
        return error;
    }

    /**
     * 
     * @param error
     *     The error
     */
    @JsonProperty("error")
    public void setError(List<Error> error) {
        this.error = error;
    }


}
