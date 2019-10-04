package com.ibm.mobilefirst.response;

import org.json.simple.JSONArray;



public class ServiceResponse {

	
	private Object data;
	private JSONArray error = new JSONArray();


	
	public Object getError() {
		return error;
	}
	public void setError(JSONArray error) {
		this.error = error;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (String) data + error;
	}
}
