
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
    "address"
})
public class Data {

    @JsonProperty("address")
    private List<Address> address = new ArrayList<Address>();

    /**
     * 
     * @return
     *     The address
     */
    @JsonProperty("address")
    public List<Address> getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    @JsonProperty("address")
    public void setAddress(List<Address> address) {
        this.address = address;
    }

}
