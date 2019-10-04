package com.ibm.mobilefirst.retail.salesassist.adapter.category.common;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.ibm.mobilefirst.util.AccessProperties;


public class Category {
	StringBuilder bestSellerobj = new StringBuilder();
	StringBuilder categoryString = new StringBuilder();
	StringBuilder topcategoryString = new StringBuilder();
	private String categoryId;
	private String categoryName;
	private String partNumber;
	private String parentId;
	private String name;

	private String productId;

	private String desc;
	private String price;
	private String productAverageRating;

	private String promotion_name;
	private String image_url;

	public Category()
	{
		this.categoryId="";
		this.categoryName = "";
		this.parentId = "";
		this.partNumber ="";
		this.name ="";
		this.price ="";
		this.productId ="";
		this.desc ="";
		this.productAverageRating = "";
		this.promotion_name = "";
		this.image_url = "";
	}






	public StringBuilder getBestSellerobj() {
		return bestSellerobj;
	}






	public void setBestSellerobj(StringBuilder bestSellerobj) {
		this.bestSellerobj = bestSellerobj;
	}






	public String getPromotion_name() {
		return promotion_name;
	}






	public void setPromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}






	public String getCategoryId() {
		return categoryId;
	}






	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}







	public String getProductAverageRating() {
		return productAverageRating;
	}






	public void setProductAverageRating(String productAverageRating) {
		this.productAverageRating = productAverageRating;
	}






	public String getPartNumber() {
		return partNumber;
	}






	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}






	public String getName() {
		return name;
	}






	public void setName(String name) {
		this.name = name;
	}






	public String getProductId() {
		return productId;
	}






	public void setProductId(String productId) {
		this.productId = productId;
	}






	public String getDesc() {
		return desc;
	}






	public void setDesc(String desc) {
		this.desc = desc;
	}






	public String getPrice() {
		return price;
	}






	public void setPrice(String price) {
		this.price = price;
	}






//	public void setEntityFromJsonBestsellers(JSONObject obj) throws JSONException
//	{
//		if(obj.has("name"))
//		{
//			String target = obj.getString("name").replaceAll("<[^>]*>", "");
//			this.name=target;
//
//		}
//		//	 	  if((obj.has("attributes"))&&(!(name.contains("Atkins")))&&(obj.has("buyable")))
//		//	 			  {
//
//		if(obj.has("categoryId"))
//		{
//			this.categoryId=obj.getString("categoryId");
//		}
//		if(obj.has("partNumber"))
//		{
//			this.partNumber= obj.getString("partNumber");
//		}
//
//		if(obj.has("price"))
//		{
//			this.price=obj.getJSONArray("price").getJSONObject(1).getString("value");
//		}
//		if(obj.has("uniqueID"))
//		{
//			this.productId = obj.getString("uniqueID");
//		}
//		if(obj.has("shortDescription"))
//		{
//			String target = obj.getString("shortDescription");
//			this.desc=target;
//		}
//		if(obj.has("rating"))
//		{
//			this.productAverageRating=obj.getString("rating");
//		}
//		if(obj.has("promotion_name"))
//		{
//			String target = obj.getString("promotion_name").replaceAll("<[^>]*>", "");
//			this.promotion_name=target;
//		}
//	}
//
//	// }
//
//	//  @Override
//	public String toStringBestsellers()
//	{
//
//
//		bestSellerobj.append("{"+"\n");
//
//
//		bestSellerobj.append("\"categoryId\":\""+this.categoryId+"\","+"\n");
//		bestSellerobj.append("\"partNumber\":\""+this.partNumber+"\","+"\n");
//		bestSellerobj.append("\"name\":\""+this.name+"\","+"\n");
//		bestSellerobj.append("\"price\":\""+this.price+"\","+"\n");
//		bestSellerobj.append("\"desc\":\""+this.desc+"\","+"\n");
//		bestSellerobj.append("\"productId\":\""+this.productId+"\""+","+"\n");
//		bestSellerobj.append("\"rating\":"+"{");
//		bestSellerobj.append("\"productAverageRating\":\""+this.productAverageRating+"\""+"\n");
//		bestSellerobj.append("}"+",");
//		bestSellerobj.append("\"promotion_name\":\""+this.promotion_name+"\""+"\n");
//		bestSellerobj.append("}");
//
//
//		return bestSellerobj.toString();
//	}




	public void setEntityFromJson(JSONObject obj, String parentCategoryId) throws JSONException
	{
		if(obj.has("name"))
		{
			this.categoryName=obj.getString("name");

		}


		if(obj.has("uniqueID"))
		{
			this.categoryId=obj.getString("uniqueID");
		}

		if(obj.has("parentCatalogGroupID"))
		{
			int size = obj.getJSONArray("parentCatalogGroupID").size();
			this.parentId=obj.getJSONArray("parentCatalogGroupID").getString(size-1);
		}
	}




	public void setEntityFromJsonByParent(JSONObject obj, String parentCategoryId) throws JSONException
	{
		if(obj.has("name"))
		{
			this.categoryName=obj.getString("name");

		}


		if(obj.has("uniqueID"))
		{
			this.categoryId=obj.getString("uniqueID");
		}

		this.parentId=parentCategoryId;
	}




	public JSONObject toObjectByParent() throws JSONException
	{

		JSONObject finalResult = new JSONObject();

		finalResult.put("categoryName", this.categoryName);
		finalResult.put("parentId", this.parentId);
		finalResult.put("categoryId", this.categoryId);

		return finalResult;
	}

	public void setEntityFromJsonTopCategories(JSONObject obj) throws JSONException
	{


		if(obj.has("name"))
		{
			this.categoryName=obj.getString("name");
			this.parentId=" ";
		}

		if(obj.has("uniqueID"))
		{
			this.categoryId=obj.getString("uniqueID");
		}

		if(obj.has("thumbnail")) {
			String identifier = obj.getString("identifier");
			String imageurl = AccessProperties.configFile.getProperty("imageurl");
			this.image_url=imageurl+"/wcsstore/pah-as01/images/mega-menu/"+identifier+"-bg.png";
		}

	}



	public JSONObject toObjectTopCategories() throws JSONException
	{

		JSONObject finalResult = new JSONObject();

		finalResult.put("categoryName", this.categoryName);
		finalResult.put("parentId", this.parentId);
		finalResult.put("image_url", this.image_url);
		finalResult.put("categoryId", this.categoryId);

		return finalResult;
	}	  


}
