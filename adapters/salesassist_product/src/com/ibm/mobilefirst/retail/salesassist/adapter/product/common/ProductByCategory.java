package com.ibm.mobilefirst.retail.salesassist.adapter.product.common;

import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.ibm.mobilefirst.util.AccessProperties;

public class ProductByCategory {
	static Logger logger = Logger.getLogger(ProductByCategory.class.getName());
	StringBuilder productString = new StringBuilder();

	private float price;

	private String desc;

	private String name;

	private String categoryId;

	private String productId;

	private String partNumber;

	private float rating;
	private int rating_count;

	private String images;
	private String image_url;
	private String promotion_name;
	private String price_range;

	public ProductByCategory() {
		this.price = 0.0f;
		this.desc = "";
		this.name = "";
		this.categoryId = "";
		this.productId = "";
		this.partNumber = "";
		this.rating = 0.0f;
		this.rating_count = 0;
		this.images = "";
		this.image_url = "";
		this.promotion_name = "";
		this.price_range = "";
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public StringBuilder getProductString() {
		return productString;
	}

	public void setProductString(StringBuilder productString) {
		this.productString = productString;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setEntityFromJson(JSONObject obj, HashMap ratingMap, HashMap ratingCountMap) throws JSONException {

		if (obj.has("parentCategoryID")) {
			this.categoryId = obj.getString("parentCategoryID");
		}
		if (obj.has("shortDescription")) {
			this.desc = obj.getString("shortDescription");
		}
		if (obj.has("name")) {
			this.name = obj.getString("name");
		}
		if (obj.has("partNumber")) {
			this.partNumber = obj.getString("partNumber");
		}
		if (obj.has("xcatentry_promo_text_1")) {
			this.promotion_name = obj.getString("xcatentry_promo_text_1");
		}

		if (obj.has("xcatentry_displayprice")) {
			this.price_range = obj.getString("xcatentry_displayprice").replace("&pound;", "£");
		}

		if (obj.has("Price")) {
			for (int i = 0; i < obj.getJSONArray("Price").length(); i++) {
				if ((obj.getJSONArray("Price").getJSONObject(i)).has("priceValue")) {
					String strPrice = ((obj.getJSONArray("Price")).getJSONObject(i)).getString("priceValue");
					if (!strPrice.isEmpty() && strPrice.trim().length() != 0)
						this.price = Float.parseFloat(strPrice);
				}
			}
		}

		if (obj.has("parentProductID")) {
			this.productId = obj.getString("parentProductID");
		} else if (obj.has("uniqueID")) {
			this.productId = obj.getString("uniqueID");
		}
		// ratingMap != null || ratingMap.isEmpty() ||
		if (!ratingMap.isEmpty()) {
			this.rating = (Float.parseFloat(ratingMap.get(partNumber).toString()));
		} else {
			this.rating = 0;
		}

		if (!ratingCountMap.isEmpty()) {
			this.rating_count = (Integer.parseInt(ratingCountMap.get(partNumber).toString()));
		} else {
			this.rating_count = 0;
		}

		if (obj.has("thumbnail")) {
			String thumbnail = obj.getString("thumbnail");
			String imageurl = AccessProperties.configFile.getProperty("imageurl");
			this.image_url = imageurl + thumbnail;
		}
		// this.isInstock = getStockByProductId(storeId,this.productId);

	}

	@Override
	public String toString() {
		productString.append("{" + "\n");
		productString.append("\"categoryId\":\"" + this.categoryId + "\"," + "\n");
		productString.append("\"desc\":\"" + this.desc.replace("\"", "\\\"") + "\"," + "\n");
		productString.append("\"name\":\"" + this.name.replace("\"", "\\\"") + "\"," + "\n");
		// productString.append("\"desc\":\""+this.desc+"\","+"\n");
		// productString.append("\"name\":\""+this.name+"\","+"\n");
		productString.append("\"partNumber\":\"" + this.partNumber + "\"," + "\n");
		productString.append("\"price\":" + this.price + "," + "\n");
		productString.append("\"productId\":\"" + this.productId + "\"" + "," + "\n");
		productString.append("\"rating\":" + this.rating + "," + "\n");
		productString.append("\"rating_count\":" + this.rating_count + "," + "\n");
		productString.append("\"promotion_name\":\"" + this.promotion_name.replace("\"", "\\\"") + "\"," + "\n");
		productString.append("\"priceRange\":\"" + this.price_range + "\"," + "\n");
		// productString.append("\"isInstock\":"+this.isInstock+","+"\n");
		productString.append("\"image_url\":\"" + this.image_url + "\"" + "\n");
		productString.append("}");

		return productString.toString();
	}

	public void setEntityFromJsonForSuggestedProduct(JSONObject obj) throws JSONException {

		if (obj.has("name")) {
			this.name = obj.getString("name");

		}
		if (obj.has("id")) {
			this.partNumber = obj.getString("id");
		}

		if (obj.has("xcatentry_promo_text_1")) {
			this.promotion_name = obj.getString("xcatentry_promo_text_1");
		}

		if (obj.has("salePriceCents")) {
			String strPrice = obj.getString("salePriceCents");
			if (!strPrice.isEmpty() && strPrice.trim().length() != 0)
				this.price = (Float.parseFloat(strPrice)) / 100;
		}

		if (obj.has("uniqueID")) {
			this.productId = obj.getString("uniqueID");
		}

		if (obj.has("imageURL")) {
			String thumbnail = obj.getString("imageURL");
			this.image_url = "http:" + thumbnail;
		}

	}

	// @Override
	public String toStringForSuggestedProduct() {
		productString.append("{" + "\n");
		productString.append("\"name\":\"" + this.name.replace("\"", "\\\"") + "\"," + "\n");
		productString.append("\"partNumber\":\"" + this.partNumber + "\"," + "\n");
		productString.append("\"price\":" + this.price + "," + "\n");
		// productString.append("\"promotion_name\":\""+this.promotion_name+"\","+"\n");
		productString.append("\"image_url\":\"" + this.image_url + "\"" + "\n");
		productString.append("}");

		return productString.toString();
	}

}
