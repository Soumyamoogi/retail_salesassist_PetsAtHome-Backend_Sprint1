
package com.ibm.mobilefirst.retail.salesassist.api.healthcheck.javabeans.addresslookup;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "flat",
    "houseName",
    "houseNumber",
    "streetLine1",
    "streetLine2",
    "locality1",
    "locality2",
    "town",
    "county",
    "country",
    "postCode",
    "type"
})
public class Address {

    @JsonProperty("flat")
    private String flat;
    @JsonProperty("houseName")
    private String houseName;
    @JsonProperty("houseNumber")
    private String houseNumber;
    @JsonProperty("streetLine1")
    private String streetLine1;
    @JsonProperty("streetLine2")
    private Object streetLine2;
    @JsonProperty("locality1")
    private String locality1;
    @JsonProperty("locality2")
    private String locality2;
    @JsonProperty("town")
    private String town;
    @JsonProperty("county")
    private String county;
    @JsonProperty("country")
    private String country;
    @JsonProperty("postCode")
    private String postCode;
    @JsonProperty("type")
    private String type;

    public Address() {
    	
       this.flat="";  
       this.houseName="";
       this.houseNumber="";
       this.streetLine1="";
       this.streetLine2="";
       this.locality1="";
       this.locality2="";
       this.town="";
       this.county="";
       this.country="";
       this.postCode="";
       this.type="";
    }
    /**
     * 
     * @return
     *     The flat
     */
    @JsonProperty("flat")
    public String getFlat() {
        return flat;
    }

    /**
     * 
     * @param flat
     *     The flat
     */
    @JsonProperty("flat")
    public void setFlat(String flat) {
        this.flat = flat;
    }

    /**
     * 
     * @return
     *     The houseName
     */
    @JsonProperty("houseName")
    public String getHouseName() {
        return houseName;
    }

    /**
     * 
     * @param houseName
     *     The houseName
     */
    @JsonProperty("houseName")
    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    /**
     * 
     * @return
     *     The houseNumber
     */
    @JsonProperty("houseNumber")
    public String getHouseNumber() {
        return houseNumber;
    }

    /**
     * 
     * @param houseNumber
     *     The houseNumber
     */
    @JsonProperty("houseNumber")
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    /**
     * 
     * @return
     *     The streetLine1
     */
    @JsonProperty("streetLine1")
    public String getStreetLine1() {
        return streetLine1;
    }

    /**
     * 
     * @param streetLine1
     *     The streetLine1
     */
    @JsonProperty("streetLine1")
    public void setStreetLine1(String streetLine1) {
        this.streetLine1 = streetLine1;
    }

    /**
     * 
     * @return
     *     The streetLine2
     */
    @JsonProperty("streetLine2")
    public Object getStreetLine2() {
        return streetLine2;
    }

    /**
     * 
     * @param streetLine2
     *     The streetLine2
     */
    @JsonProperty("streetLine2")
    public void setStreetLine2(Object streetLine2) {
        this.streetLine2 = streetLine2;
    }

    /**
     * 
     * @return
     *     The locality1
     */
    @JsonProperty("locality1")
    public String getLocality1() {
        return locality1;
    }

    /**
     * 
     * @param locality1
     *     The locality1
     */
    @JsonProperty("locality1")
    public void setLocality1(String locality1) {
        this.locality1 = locality1;
    }

    /**
     * 
     * @return
     *     The locality2
     */
    @JsonProperty("locality2")
    public String getLocality2() {
        return locality2;
    }

    /**
     * 
     * @param locality2
     *     The locality2
     */
    @JsonProperty("locality2")
    public void setLocality2(String locality2) {
        this.locality2 = locality2;
    }

    /**
     * 
     * @return
     *     The town
     */
    @JsonProperty("town")
    public String getTown() {
        return town;
    }

    /**
     * 
     * @param town
     *     The town
     */
    @JsonProperty("town")
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 
     * @return
     *     The county
     */
    @JsonProperty("county")
    public String getCounty() {
        return county;
    }

    /**
     * 
     * @param county
     *     The county
     */
    @JsonProperty("county")
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * 
     * @return
     *     The country
     */
    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 
     * @return
     *     The postCode
     */
    @JsonProperty("postCode")
    public String getPostCode() {
        return postCode;
    }

    /**
     * 
     * @param postCode
     *     The postCode
     */
    @JsonProperty("postCode")
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

}
