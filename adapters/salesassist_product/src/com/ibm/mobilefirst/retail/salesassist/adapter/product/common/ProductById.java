package com.ibm.mobilefirst.retail.salesassist.adapter.product.common;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import com.ibm.mobilefirst.util.AccessProperties;

public class ProductById {

	static Logger logger = Logger.getLogger(ProductById.class.getName());
	StringBuilder productString = new StringBuilder();

	private float price;

	private String desc;

	private String name;

	private String categoryId;

	private String appDimension;

	private String partNumber;

	private String brand;

	private String numberOfSKUs;

	private String image_url;

	private ArrayList<String> image_array = new ArrayList<String>();

	private Boolean hasSingleSKU;
	private int grossWeight;
	private int grossHeight;
	private int grossWidth;
	private int grossDepth;
	private float productAverageRating;
	private String uOM;
	private String singleSKUUniqueID;

	private String dropshipProduct;
	private String promotion_name;

	private String mfPartNumber_ntk;

	private int ppuMultiplier;
	private String ageRestriction;
	private int netContents;
	private int ppuMeasure;
	private String guaranteeInMonths;
	private String vatMsg;
	private String pmeds;
	private String pricePerUnit;
	private float pricePer;
	private String productId;
	private String loyaltyCardUsage;
	private int price1;
	private String summaryIndications;
	private String ingredients;
	private String usageInstructions;
	private String warnings;
	private String age;
	// private String productId;
	private String patientInfoLeaflet;
	private String activeIngredients;
	private String productEditorialCopy;
	private String patientInfoLeafletPdfPath;
	JSONArray arr = new JSONArray();
	JSONArray arr1 = new JSONArray();
	JSONArray arr2 = new JSONArray();
	JSONArray data = new JSONArray();
	JSONObject priceBase = new JSONObject();
	JSONObject ppuMeasureDict = new JSONObject();
	JSONObject rating1 = new JSONObject();
	JSONArray suggestedProducts = new JSONArray();
	JSONArray reviews = new JSONArray();
	private int rating;
	private int recomm_percent;
	private String longDesc;
	private boolean isDeliveredInStoreOnly;
	private boolean isPickUpInStore;
	private boolean isHomeDelivery;
	private boolean isSQPSubProduct;

	public ProductById() {
		this.categoryId = "";
		this.price = 0.0f;
		// this.images = "";
		this.name = "";
		this.productId = "";
		this.partNumber = "";
		this.brand = "";
		this.numberOfSKUs = "";
		this.singleSKUUniqueID = "";
		this.desc = "";
		this.longDesc = "";
		this.appDimension = "";
		this.mfPartNumber_ntk = "";
		this.promotion_name = "";
		this.hasSingleSKU = false;
		this.productAverageRating = 0.0f;
		this.ppuMeasure = 0;
		this.ppuMultiplier = 0;
		this.dropshipProduct = "";
		this.loyaltyCardUsage = "";
		this.guaranteeInMonths = "";
		this.pmeds = "";
		this.ageRestriction = "";
		this.vatMsg = "";
		this.summaryIndications = "";
		this.ingredients = "";
		this.usageInstructions = "";
		this.warnings = "";
		this.productId = "";
		this.patientInfoLeaflet = "";
		this.activeIngredients = "";
		this.productEditorialCopy = "";
		this.patientInfoLeafletPdfPath = "";
		this.image_url = "";
		this.recomm_percent = 0;
		this.isDeliveredInStoreOnly = false;
		this.isPickUpInStore = false;
		this.isHomeDelivery = false;
		this.isSQPSubProduct = false;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	// public String getImage ()
	// {
	// return images;
	// }
	//
	// public void setImage (String images)
	// {
	// this.images = images;
	// }

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getpartNumber() {
		return partNumber;
	}

	public void setpartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public Boolean hasSingleSKU() {
		return hasSingleSKU;
	}

	public void hasSingleSKU(Boolean hasSingleSKU) {
		this.hasSingleSKU = hasSingleSKU;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// public Skus[] getSkus ()
	// {
	// return skus;
	// }
	//
	// public void setSkus (Skus[] skus)
	// {
	// this.skus = skus;
	// }

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getnumberOfSKUs(String numberOfSKUs) {
		return numberOfSKUs;
	}

	public void setnumberOfSKUs(String numberOfSKUs) {
		this.numberOfSKUs = numberOfSKUs;
	}

	//
	public String getsingleSKUCatalogEntryID(String singleSKUCatalogEntryID) {
		return singleSKUCatalogEntryID;
	}

	public void setsingleSKUCatalogEntryID(String singleSKUUniqueID) {
		this.singleSKUUniqueID = singleSKUUniqueID;
	}

	// public void gethasSingleSKU (Boolean hasSingleSKU)
	// {
	// this.hasSingleSKU = hasSingleSKU;
	// }
	//
	// public void sethasSingleSKU (Boolean hasSingleSKU)
	// {
	// this.hasSingleSKU = hasSingleSKU;
	// }

	public String getpromotion_name(String promotion_name) {
		return promotion_name;
	}

	public void setpromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}

	public String getmfPartNumber_ntk(String mfPartNumber_ntk) {
		return mfPartNumber_ntk;
	}

	public void mfPartNumber_ntk(String mfPartNumber_ntk) {
		this.mfPartNumber_ntk = mfPartNumber_ntk;
	}

	public float getproductAverageRating() {
		return productAverageRating;
	}

	public void setproductAverageRating(int productAverageRating) {
		this.productAverageRating = productAverageRating;
	}

	public int getproductRating() {
		return rating;
	}

	public void setproductRating(int rating) {
		this.rating = rating;
	}

	public String getsummaryIndications() {
		return summaryIndications;
	}

	public void setsummaryIndications(String summaryIndications) {
		this.summaryIndications = summaryIndications;
	}

	// public String activeIngredients()
	// {
	// return activeIngredients;
	// }

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ProductById.logger = logger;
	}

	public StringBuilder getProductString() {
		return productString;
	}

	public void setProductString(StringBuilder productString) {
		this.productString = productString;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getNumberOfSKUs() {
		return numberOfSKUs;
	}

	public void setNumberOfSKUs(String numberOfSKUs) {
		this.numberOfSKUs = numberOfSKUs;
	}

	public Boolean getHasSingleSKU() {
		return hasSingleSKU;
	}

	public void setHasSingleSKU(Boolean hasSingleSKU) {
		this.hasSingleSKU = hasSingleSKU;
	}

	public int getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(int grossWeight) {
		this.grossWeight = grossWeight;
	}

	public int getGrossHeight() {
		return grossHeight;
	}

	public void setGrossHeight(int grossHeight) {
		this.grossHeight = grossHeight;
	}

	public int getGrossWidth() {
		return grossWidth;
	}

	public void setGrossWidth(int grossWidth) {
		this.grossWidth = grossWidth;
	}

	public int getGrossDepth() {
		return grossDepth;
	}

	public void setGrossDepth(int grossDepth) {
		this.grossDepth = grossDepth;
	}

	public float getProductAverageRating() {
		return productAverageRating;
	}

	public void setProductAverageRating(int productAverageRating) {
		this.productAverageRating = productAverageRating;
	}

	public String getuOM() {
		return uOM;
	}

	public void setuOM(String uOM) {
		this.uOM = uOM;
	}

	public String getSingleSKUCatalogEntryID() {
		return singleSKUUniqueID;
	}

	public void setSingleSKUCatalogEntryID(String singleSKUUniqueID) {
		this.singleSKUUniqueID = singleSKUUniqueID;
	}

	public String getDropshipProduct() {
		return dropshipProduct;
	}

	public void setDropshipProduct(String dropshipProduct) {
		this.dropshipProduct = dropshipProduct;
	}

	public String getPromotion_name() {
		return promotion_name;
	}

	public void setPromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}

	public String getMfPartNumber_ntk() {
		return mfPartNumber_ntk;
	}

	public void setMfPartNumber_ntk(String mfPartNumber_ntk) {
		this.mfPartNumber_ntk = mfPartNumber_ntk;
	}

	public int getPpuMultiplier() {
		return ppuMultiplier;
	}

	public void setPpuMultiplier(int ppuMultiplier) {
		this.ppuMultiplier = ppuMultiplier;
	}

	public String getAgeRestriction() {
		return ageRestriction;
	}

	public void setAgeRestriction(String ageRestriction) {
		this.ageRestriction = ageRestriction;
	}

	public int getNetContents() {
		return netContents;
	}

	public void setNetContents(int netContents) {
		this.netContents = netContents;
	}

	public int getPpuMeasure() {
		return ppuMeasure;
	}

	public void setPpuMeasure(int ppuMeasure) {
		this.ppuMeasure = ppuMeasure;
	}

	public String getGuaranteeInMonths() {
		return guaranteeInMonths;
	}

	public void setGuaranteeInMonths(String guaranteeInMonths) {
		this.guaranteeInMonths = guaranteeInMonths;
	}

	public String getVatMsg() {
		return vatMsg;
	}

	public void setVatMsg(String vatMsg) {
		this.vatMsg = vatMsg;
	}

	public String getPmeds() {
		return pmeds;
	}

	public void setPmeds(String pmeds) {
		this.pmeds = pmeds;
	}

	public String getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public float getPricePer() {
		return pricePer;
	}

	public void setPricePer(float pricePer) {
		this.pricePer = pricePer;
	}

	public String getLoyaltyCardUsage() {
		return loyaltyCardUsage;
	}

	public void setLoyaltyCardUsage(String loyaltyCardUsage) {
		this.loyaltyCardUsage = loyaltyCardUsage;
	}

	public int getPrice1() {
		return price1;
	}

	public void setPrice1(int price1) {
		this.price1 = price1;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getUsageInstructions() {
		return usageInstructions;
	}

	public void setUsageInstructions(String usageInstructions) {
		this.usageInstructions = usageInstructions;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public String getPatientInfoLeaflet() {
		return patientInfoLeaflet;
	}

	public void setPatientInfoLeaflet(String patientInfoLeaflet) {
		this.patientInfoLeaflet = patientInfoLeaflet;
	}

	public String getActiveIngredients() {
		return activeIngredients;
	}

	public void setActiveIngredients(String activeIngredients) {
		this.activeIngredients = activeIngredients;
	}

	public String getProductEditorialCopy() {
		return productEditorialCopy;
	}

	public void setProductEditorialCopy(String productEditorialCopy) {
		this.productEditorialCopy = productEditorialCopy;
	}

	public String getPatientInfoLeafletPdfPath() {
		return patientInfoLeafletPdfPath;
	}

	public void setPatientInfoLeafletPdfPath(String patientInfoLeafletPdfPath) {
		this.patientInfoLeafletPdfPath = patientInfoLeafletPdfPath;
	}

	public JSONArray getArr() {
		return arr;
	}

	public void setArr(JSONArray arr) {
		this.arr = arr;
	}

	public JSONArray getArr1() {
		return arr1;
	}

	public void setArr1(JSONArray arr1) {
		this.arr1 = arr1;
	}

	public JSONArray getArr2() {
		return arr2;
	}

	public void setArr2(JSONArray arr2) {
		this.arr2 = arr2;
	}

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

	public JSONObject getPriceBase() {
		return priceBase;
	}

	public void setPriceBase(JSONObject priceBase) {
		this.priceBase = priceBase;
	}

	public JSONObject getPpuMeasureDict() {
		return ppuMeasureDict;
	}

	public void setPpuMeasureDict(JSONObject ppuMeasureDict) {
		this.ppuMeasureDict = ppuMeasureDict;
	}

	public JSONObject getRating() {
		return rating1;
	}

	public void setRating(JSONObject rating) {
		this.rating1 = rating;
	}

	public void setactiveIngredients(String activeIngredients) {
		this.activeIngredients = activeIngredients;
	}

	public String productEditorialCopy() {
		return productEditorialCopy;
	}

	public void setproductEditorialCopy(String productEditorialCopy) {
		this.productEditorialCopy = productEditorialCopy;
	}

	public JSONArray getSuggestedProducts() {
		return suggestedProducts;
	}

	public void setSuggestedProducts(JSONArray suggestedProducts) {
		this.suggestedProducts = suggestedProducts;
	}

	public void setEntityFromJson(JSONObject obj, JSONObject snapshot, JSONArray reviewArr, String storeId)
			throws JSONException {
		StringBuilder skus = new StringBuilder();
		String colour = "";
		if (snapshot.has("average_rating")) {
			this.productAverageRating = Float.parseFloat(snapshot.getString("average_rating"));
		}

		if (snapshot.has("percent_recommended")) {
			this.recomm_percent = Integer.parseInt(snapshot.getString("percent_recommended"));
		}

		if (obj.has("parentCategoryID")) {
			this.categoryId = obj.getString("parentCategoryID");
		}
		// if(obj.has("fullImage"))
		// {
		// String thumbnail = obj.getString("fullImage");
		// String imageurl =
		// AccessProperties.configFile.getProperty("imageurl");
		// this.image_url=imageurl+thumbnail;
		//// this.image_array.add(this.image_url);
		// }
		String imageurl = AccessProperties.configFile.getProperty("imageurl");
		if (obj.has("fullImage") && !obj.has("Attachments")) {
			String thumbnail = obj.getString("fullImage");

			if (thumbnail.contains("L")) {
				int index = thumbnail.indexOf(".");
				thumbnail = thumbnail.substring(0, index - 1) + thumbnail.substring(index, thumbnail.length());
			}

			this.image_url = imageurl + thumbnail;

			this.image_array.add("\"" + this.image_url + "\"");
		}

		if (obj.has("Attachments")) {
			JSONArray obj1 = obj.getJSONArray("Attachments");
			for (Object obje : obj1) {

				if (((JSONObject) obje).has("usage")) {
					if (((JSONObject) obje).getString("usage").equals("ANGLEIMAGES_FULLIMAGE")) {
						String path = ((JSONObject) obje).getString("path").replace("L", "");
						this.image_url = imageurl + "/wcsstore/" + path;
						this.image_array.add("\"" + this.image_url + "\"");
					}
				}
			}
			// String path = obj.getJSONArray("Attachments")getString("path");

			// this.image_url=imageurl+thumbnail;
		}

		if (obj.has("xcatentry_promo_text_1")) {
			this.promotion_name = obj.getString("xcatentry_promo_text_1");
		}

		if (obj.has("Price")) {

			JSONArray prices = obj.getJSONArray("Price");

			for (int i = 0; i < prices.size(); i++) {
				JSONObject priceObj = prices.getJSONObject(i);
				if (priceObj.has("priceValue")) {
					this.price = Float.parseFloat(priceObj.getString("priceValue"));
				}
			}
		}

		if (obj.has("partNumber")) {
			this.partNumber = obj.getString("partNumber");
		}

		if (obj.has("shortDescription")) {

			String target = obj.getString("shortDescription");
			this.desc = target;
		}
		if (obj.has("longDescription")) {
			String target = obj.getString("longDescription");

			int index = target.indexOf("$Nutrition$");
			if (index > 0) {
				String subStr1 = target.substring(0, index);
				String subStr2 = target.substring(index).replace("$Nutrition$", "");
				String subStr3 = subStr1.concat("<BR\\/><BR\\/><b>Nutrition:<\\/b><BR\\/><BR\\/>").concat(subStr2);
				int index1 = subStr3.indexOf("$DeliveryInformation$");
				String str1 = subStr3.substring(0, index1);
				String str2 = subStr3.substring(index1).replace("$DeliveryInformation$", "");
				String str3 = str1.concat("<BR\\/><BR\\/><b>Delivery Information:<\\/b><BR\\/><BR\\/>").concat(str2);

				logger.severe("subStr1:" + subStr1 + "subStr2:" + subStr2 + "subStr3:" + subStr3);
				this.longDesc = str3;
			} else {
				int index1 = target.indexOf("$DeliveryInformation$");
				if (index1 > 0) {
					String str1 = target.substring(0, index1);
					String str2 = target.substring(index1).replace("$DeliveryInformation$", "");
					String str3 = str1.concat("<BR\\/><BR\\/><b>Delivery Information:<\\/b><BR\\/><BR\\/>")
							.concat(str2);

					this.longDesc = str3;

				} else {
					this.longDesc = target;
				}
			}

			// String target =
			// obj.getString("longDescription").replace(".$Nutrition$",
			// "<BR\\/><BR\\/>").replace(".$DeliveryInformation$", ".<BR\\/>");
			// target = target.replace("$Nutrition$",
			// "<BR\\/><BR\\/><b>Nutrition:<\\/b><BR\\/>").replace("$DeliveryInformation$",
			// "<BR\\/><b>DeliveryInformation:<\\/b><BR\\/>");
			// logger.severe("longDesc:" + obj.getString("longDescription"));
		}

		if (obj.has("name")) {
			String target = obj.getString("name");
			this.name = target;
		}

		if (obj.has("rating")) {
			this.rating = Integer.parseInt(obj.getString("rating"));
		}
		if (obj.has("uniqueID")) {
			this.productId = obj.getString("uniqueID");
		}
		if (obj.has("numberOfSKUs")) {
			this.numberOfSKUs = obj.getString("numberOfSKUs");
		}
		if (obj.has("singleSKUUniqueID")) {
			this.singleSKUUniqueID = obj.getString("singleSKUUniqueID");
		}

		if (obj.has("hasSingleSKU")) {
			this.hasSingleSKU = obj.getBoolean("hasSingleSKU");
		}

		if (obj.has("mfPartNumber_ntk")) {
			this.mfPartNumber_ntk = obj.getString("mfPartNumber_ntk");
		}

		if (obj.has("Attributes")) {
			JSONArray attrArr = obj.getJSONArray("Attributes");
			for (int j = 0; j < attrArr.length(); j++) {

				if (attrArr.getJSONObject(j).has("name")) {

					if (attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("Brand")) {
						String brand = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0)
								.getString("values");
						this.brand = brand;

					} else if (attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("Colour-")) {
						colour = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0).getString("values");
					} else if (attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("ApproxDimProduct")) {
						this.appDimension = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0)
								.getString("values");
					} else if (attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("Delivery Information")) {
						String deliveryOptions = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0)
								.getString("values").replaceAll("<[^>]*>", "");
						if (deliveryOptions.equalsIgnoreCase("Delivery OptionsIn-Store only")) {
							this.isDeliveredInStoreOnly = true;
						}
					} else if (attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("PickupInStore")) {
						String pickupInStore = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0)
								.getString("values");

						if (pickupInStore.equalsIgnoreCase("1")) {
							this.isPickUpInStore = true;
						}
					} else if (attrArr.getJSONObject(j).getString("name")
							.equalsIgnoreCase("AvailableForHomeDelivery")) {
						String homeDelivery = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0)
								.getString("values");

						if (homeDelivery.equalsIgnoreCase("1")) {
							this.isHomeDelivery = true;
						}
					} else if (attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("SQPProduct")
							|| attrArr.getJSONObject(j).getString("name").equalsIgnoreCase("SubscriptionAvailable")) {
						String sqpProduct = attrArr.getJSONObject(j).getJSONArray("Values").getJSONObject(0)
								.getString("values");

						if (sqpProduct.equalsIgnoreCase("1")) {
							this.isSQPSubProduct = true;
						}
					}
				}
			}
		}

		if (obj.has("SKUs")) {
			Double pricePerUnit = null;
			DecimalFormat df = new DecimalFormat("#.##");

			DecimalFormat df1 = new DecimalFormat("0.00");

			String pricePerKg = "";

			JSONArray skusArr = obj.getJSONArray("SKUs");
			for (int j = 0; j < skusArr.length(); j++) {
				if (hasSingleSKU) {
					skus.append(singleSKUUniqueID);

				} else {
					if (j == 0) {
						skus.append(skusArr.getJSONObject(j).getString("SKUUniqueID"));
					} else {
						skus.append(",");
						skus.append(skusArr.getJSONObject(j).getString("SKUUniqueID"));
					}
				}
			}
			HashMap map = getStockByProductId(storeId, skus);
			for (int j = 0; j < skusArr.length(); j++) {
				if (skusArr.getJSONObject(j).has("Attributes")) {
					JSONArray attributeArr = skusArr.getJSONObject(j).getJSONArray("Attributes");
					JSONObject sku = new JSONObject();
					JSONArray attr = new JSONArray();

					if (skusArr.getJSONObject(j).has("Price")) {
						String strPrice = skusArr.getJSONObject(j).getJSONArray("Price").getJSONObject(0)
								.getString("SKUPriceValue");
						sku.put("price", Double.valueOf(df.format(Double.parseDouble(strPrice))));
						if (hasSingleSKU) {
							price = Float.valueOf(df.format(Float.parseFloat(strPrice)));
						}
					}

					for (int k = 0; k < attributeArr.length(); k++) {
						JSONObject colorOrSize = new JSONObject();
						if (attributeArr.getJSONObject(k).has("name")) {
							int valLength = attributeArr.getJSONObject(k).getJSONArray("Values").length();
							for (int value = 0; value < valLength; value++) {
								String colorOrSizeValue = attributeArr.getJSONObject(k).getJSONArray("Values")
										.getJSONObject(value).getString("values");
								if (attributeArr.getJSONObject(k).getString("name").equalsIgnoreCase("Colour")
										|| attributeArr.getJSONObject(k).getString("name").equalsIgnoreCase("Weight")) {
									colorOrSize.put("name", attributeArr.getJSONObject(k).getString("name"));

									Double totalPrice = (Double) sku.get("price");
									if (colorOrSizeValue.substring(colorOrSizeValue.length() - 2).toString()
											.contains("kg")) {
										String numbers = colorOrSizeValue.substring(0, colorOrSizeValue.length() - 2);
										pricePerUnit = totalPrice / Double.parseDouble(numbers);
										pricePerKg = "£" + df1.format(pricePerUnit) + "/kg";
									} else if (colorOrSizeValue.substring(colorOrSizeValue.length() - 2).toString()
											.contains("ml")) {
										String numbers = colorOrSizeValue.substring(0, colorOrSizeValue.length() - 2);
										pricePerUnit = totalPrice / Double.parseDouble(numbers);
										pricePerKg = "£" + df1.format(pricePerUnit) + "/ml";
									}

									JSONArray colororSizeArr = new JSONArray();
									colororSizeArr.add(colorOrSizeValue);
									colorOrSize.put("values", colororSizeArr);
								} else if (attributeArr.getJSONObject(k).getString("name").equalsIgnoreCase("Size")) {
									colorOrSize.put("name", attributeArr.getJSONObject(k).getString("name"));
									JSONArray colororSizeArr = new JSONArray();
									colororSizeArr.add(colorOrSizeValue);
									colorOrSize.put("values", colororSizeArr);
									if (colour.length() > 0) {
										JSONObject colourJson = new JSONObject();
										colourJson.put("name", "Colour");
										JSONArray colororArr = new JSONArray();
										colororArr.add(colour);
										colourJson.put("values", colororArr);
										attr.add(colourJson);
									}
								}
							}
						}
						attr.add(colorOrSize);
					}

					sku.put("attributes", attr);
					sku.put("skuId", skusArr.getJSONObject(j).getString("SKUUniqueID"));
					sku.put("count", map.get(skusArr.getJSONObject(j).getString("SKUUniqueID")));
					sku.put("pricePerUnit", pricePerKg);
					arr.add(sku);
				} else if (hasSingleSKU) {
					JSONObject sku = new JSONObject();
					sku.put("price", price);
					sku.put("skuId", singleSKUUniqueID);
					skus.append(singleSKUUniqueID);
					sku.put("pricePerUnit", pricePerKg); // for single sku,
															// price/Unit is
															// empty..
					sku.put("count", map.get(singleSKUUniqueID).toString());
					if (colour.length() > 0) {
						JSONObject colourJson = new JSONObject();
						colourJson.put("name", "Colour");
						JSONArray colororArr = new JSONArray();
						colororArr.add(colour);
						colourJson.put("values", colororArr);
						JSONArray attr = new JSONArray();
						attr.add(colourJson);
						sku.put("attributes", attr);
					} else {
						/**
						 * sku.put("attributes", "[]"); Commented by A.George.
						 * 05May2017
						 */
						sku.put("attributes", new JSONArray());
					}
					arr.add(sku);
				}
			}
		}

		JSONArray reviews = new JSONArray();
		String date = "";
		if (reviewArr != null && reviewArr.length() > 0) {
			for (int i = 0; i < reviewArr.length(); i++) {
				if (reviewArr.getJSONObject(i).has("created_date")) {
					try {
						date = reviewArr.getJSONObject(i).getString("created_date").replaceAll("/", "-");
					} catch (Exception e) {
						logger.severe("Exc in dateConversion in ProductById:" + e.toString());
					}
				}

				JSONObject review1 = new JSONObject();
				review1.put("date", date);
				if (reviewArr.getJSONObject(i).has("rating")) {
					review1.put("rating", reviewArr.getJSONObject(i).getInt("rating"));
				} else {
					review1.put("rating", 0);
				}
				if (reviewArr.getJSONObject(i).has("name")) {
					review1.put("name", reviewArr.getJSONObject(i).getString("name"));
				} else {
					review1.put("name", "");
				}
				if (reviewArr.getJSONObject(i).has("comments")) {
					review1.put("comments", reviewArr.getJSONObject(i).getString("comments"));
				} else {
					review1.put("comments", "");
				}
				if (reviewArr.getJSONObject(i).has("headline")) {
					review1.put("headline", reviewArr.getJSONObject(i).getString("headline"));
				} else {
					review1.put("headline", "");
				}
				if (reviewArr.getJSONObject(i).has("helpful_score")) {
					review1.put("helpful_score", reviewArr.getJSONObject(i).getInt("helpful_score"));
				} else {
					review1.put("helpful_score", 0);
				}
				if (reviewArr.getJSONObject(i).has("location")) {
					review1.put("location", reviewArr.getJSONObject(i).getString("location"));
				} else {
					review1.put("location", "");
				}
				reviews.add(review1);
			}
			this.reviews = reviews;
		}
	}

	// @Override
	public String toStringPID() {

		productString.append("{" + "\n");
		productString.append("\"data\":" + "\n");
		productString.append("{" + "\n");

		productString.append("\"categoryId\":\"" + this.categoryId + "\"," + "\n");

		productString.append("\"price\":" + this.price + "," + "\n");
		// productString.append("\"images\":\""+this.images+"\","+"\n");
		productString.append("\"name\":\"" + this.name.replace("\"", "\\\"") + "\"," + "\n");
		productString.append("\"productId\":\"" + this.productId + "\"" + "," + "\n");
		productString.append("\"partNumber\":\"" + this.partNumber + "\"," + "\n");
		productString.append("\"brand\":\"" + this.brand + "\"," + "\n");
		productString.append("\"desc\":\"" + this.desc.replace("\"", "\\\"") + "\"," + "\n");
		productString.append("\"longDesc\":\"" + this.longDesc.replace("\"", "\\\"") + "\"," + "\n");
		productString.append("\"appDimension\":\"" + this.appDimension + "\"," + "\n");
		productString.append("\"numberOfSKUs\":\"" + this.numberOfSKUs + "\" ," + "\n");
		productString.append("\"image_url\":" + this.image_array + "," + "\n");
		// productString.append("\"image_url\":"+this.image_url+","+ "\n");

		// productString.append("\"singleSKUUniqueID\":\""+this.singleSKUUniqueID+"\","+"\n");
		productString.append("\"promotion_name\":\"" + this.promotion_name.replace("\"", "\\\"") + "\"," + "\n");
		productString.append("\"hasSingleSKU\"" + ":" + this.hasSingleSKU + " ,\n");
		productString.append("\"rating\"" + ":" + this.rating + " ,\n");
		productString.append("\"recomm_percent\"" + ":" + this.recomm_percent + " ,\n");
		productString.append("\"productAverageRating\"" + ":" + this.productAverageRating + " ,\n");
		productString.append("\"isDeliveredInStoreOnly\"" + ":" + this.isDeliveredInStoreOnly + " ,\n");
		productString.append("\"isPickUpInStore\"" + ":" + this.isPickUpInStore + " ,\n");
		productString.append("\"isHomeDelivery\"" + ":" + this.isHomeDelivery + " ,\n");
		productString.append("\"isSQPSubProduct\"" + ":" + this.isSQPSubProduct + " ,\n");

		productString.append("\"suggestedProducts\"" + ":" + this.suggestedProducts + " ,\n");
		productString.append("\"reviews\"" + ":" + this.reviews + " ,\n");
		productString.append("\"skus\"" + ":" + arr);
		productString.append("}");
		productString.append("}");

		return productString.toString();
	}

	private HashMap getStockByProductId(String storeId, StringBuilder skus) {
		HashMap map = new HashMap();

		String responseFormat = AccessProperties.configFile.getProperty(ServiceConstants.RESPONSE_FORMATE);
		String url = AccessProperties.configFile.getProperty(ServiceConstants.URL);
		JSONObject inventoryJson = new JSONObject();

		try {
			String url3 = url + storeId + "/inventoryavailability/" + skus.toString() + "?responseFormat="
					+ responseFormat;
			logger.severe("Inventory url:" + url3);
			inventoryJson = ApiAdapter.callGetAPI(url3);
			logger.severe("Inventory Output for each Product" + inventoryJson);
			logger.severe("Inventory SKU's for each Product" + skus.toString());
			JSONArray inventoryArr = inventoryJson.getJSONArray("InventoryAvailability");
			for (int k = 0; k < inventoryArr.length(); k++) {
				if (inventoryArr.getJSONObject(k).has("availableQuantity")
						&& (inventoryArr.getJSONObject(k).has("onlineStoreName")
								|| inventoryArr.getJSONObject(k).has("onlineStoreId"))) {
					map.put(inventoryArr.getJSONObject(k).getString("productId"),
							inventoryArr.getJSONObject(k).getString("availableQuantity"));
				}
			}

			logger.severe("Inventory Map:" + map.toString());

		} catch (IOException e) {
			logger.severe("IOExc in getStockByProductId:" + e.getMessage());
		} catch (JSONException e) {
			logger.severe("JSONExc in getStockByProductId:" + e.getMessage());
		}
		return map;
	}

	public ArrayList<String> getImage_array() {
		return image_array;
	}

	public void setImage_array(ArrayList<String> image_array) {
		this.image_array = image_array;
	}

	/**
	 * soumya moogi
	 * 
	 * @param resp
	 * @param originaljsonObj
	 * @return
	 */
	public static String checkDisplayType(String resp, JSONObject originaljsonObj) {
		String response = "";
		JSONObject jsonObj = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray skuarray = new JSONArray();
		JSONArray attributesarray = new JSONArray();
		HashMap<Integer, String> map = new HashMap<>();
		JSONObject skuarrayobj = new JSONObject();

		try {

			if (originaljsonObj.has("CatalogEntryView")) {
				JSONArray catalogEntityView = new JSONArray();
				JSONArray originalAttribute = new JSONArray();
				JSONArray originalValues = new JSONArray();

				catalogEntityView = originaljsonObj.getJSONArray("CatalogEntryView");
				originalAttribute = catalogEntityView.getJSONObject(0).getJSONArray("Attributes");
				originalValues = originalAttribute.getJSONObject(0).getJSONArray("Values");

				for (int i = 0; i < originalValues.size(); i++) {
					String value = originalValues.getJSONObject(i).getString("values");
					String rep = value.replaceAll(" ", "");
					if (!isAlpha(rep)) {

						value = value.replaceAll("[^0-9\\.]", "");
						map.put(i, value);
					} else {

						map.put(i, rep);
					}
				}
			}

			jsonObj = new JSONObject(resp);
			if (jsonObj.has("data")) {
				data = jsonObj.getJSONObject("data");
			}

			if (data.has("skus")) {
				skuarray = data.getJSONArray("skus");
				int skuarraysize = skuarray.size();

				if (skuarraysize > 1) {
					String priceValue = "";
					Double priceValueDouble = 0.0;
					Double big = 0.0;
					Double small = 0.0;
					Double[] price = new Double[skuarraysize];
					for (int l = 0; l < skuarraysize; l++) {

						priceValue = skuarray.getJSONObject(l).getString("price");
						priceValueDouble = Double.parseDouble(priceValue);
						price[l] = priceValueDouble;
					}
					big = price[0];
					small = price[0];
					for (int m = 1; m < price.length; m++) {

						if (price[m] > big)
							big = price[m];
						else if (price[m] < small)
							small = price[m];
					}
					String smallValue = String.valueOf(small);
					String bigValue = String.valueOf(big);
					int smallLength = smallValue.substring(smallValue.indexOf(".")).length();
					int bigLength = bigValue.substring(bigValue.indexOf(".")).length();
					smallLength--;
					bigLength--;
					if (smallLength == 1) {
						smallValue = String.valueOf(small) + "0";

					}
					if (bigLength == 1) {
						bigValue = String.valueOf(big) + "0";
					}
					if (smallValue.equalsIgnoreCase(bigValue))

					{
						data.put("skuPriceRange", "£" + smallValue);
					} else {
						data.put("skuPriceRange", "From £" + smallValue + " to £" + bigValue);
					}

				} else {
					data.put("skuPriceRange", "");
				}

				for (int i = 0; i < skuarray.size(); i++) {
					skuarrayobj = skuarray.getJSONObject(i);

					/**
					 * Added by A.George 05May2017
					 * 
					 */

					if (skuarray.getJSONObject(i).has("attributes")) {
						attributesarray = skuarray.getJSONObject(i).getJSONArray("attributes");
					}

					if (!attributesarray.isEmpty()) {

						/**
						 * 
						 * The below two lines commented.
						 * 
						 * if (skuarray.getJSONObject(i).has("attributes")) {
						 * attributesarray =
						 * skuarray.getJSONObject(i).getJSONArray("attributes");
						 * 
						 * Code Change Ends.
						 */

						int valLength = attributesarray.size();
						String name = "";
						String sizeValue = "";
						if (valLength > 1) {
							for (int j = 0; j < attributesarray.size(); j++) {
								JSONObject attributeObj = attributesarray.getJSONObject(j);
								if (attributeObj.has("name")) {
									name = attributeObj.getString("name");
									if (name.equalsIgnoreCase("size")) {
										sizeValue = attributesarray.getJSONObject(j).getJSONArray("values")
												.getString(0);
										if (sizeValue.equalsIgnoreCase("xx small")) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", "XXS");
											data.put("displayType", "swatches");
										} else if (sizeValue.equalsIgnoreCase("x small")) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", "XS");
										} else if (sizeValue.equalsIgnoreCase("small")) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", "S");
										} else if (sizeValue.equalsIgnoreCase("medium")) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", "M");
										} else if (sizeValue.equalsIgnoreCase("large")) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", "L");
										} else if (sizeValue.equalsIgnoreCase("x large")
												|| sizeValue.equalsIgnoreCase("jumbo")) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", "XL");
										} else if (sizeValue.equalsIgnoreCase("xx large")
												|| sizeValue.equalsIgnoreCase("Giant")) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", "XXL");
										} else if (!Character.isDigit(sizeValue.trim().charAt(0))) {
											sizeValue = sizeValue.replaceAll(" ", "");
											checkDisplayOrder(map, sizeValue, skuarrayobj);
											attributeObj.put("displayValue", sizeValue);

										} else {
											String checkInt = sizeValue.replaceAll("[^0-9\\.]", "");
											if (checkInt.matches("\\d+")) {
												Integer formattedValue = Integer
														.valueOf(sizeValue.replaceAll("[^0-9\\.]", ""));
												String formattedStringValue = Integer.toString(formattedValue);
												attributeObj.put("displayValue", formattedStringValue);
												checkDisplayOrder(map, formattedStringValue, skuarrayobj);
												data.put("displayType", "text");
											} else {
												double formattedValue = Double
														.valueOf(sizeValue.replaceAll("[^0-9\\.]", ""));
												String formattedStringValue = Double.toString(formattedValue);
												attributeObj.put("displayValue", formattedStringValue);
												checkDisplayOrder(map, formattedStringValue, skuarrayobj);
												data.put("displayType", "text");
											}

										}
									} else if (name.equalsIgnoreCase("weight")) {
										String weightValue = attributesarray.getJSONObject(0).getJSONArray("values")
												.getString(0);

										String checkInt = weightValue.replaceAll("[^0-9\\.]", "");
										if (checkInt.matches("\\d+")) {
											Integer formattedValue = Integer
													.valueOf(weightValue.replaceAll("[^0-9\\.]", ""));
											String formattedStringValue = Integer.toString(formattedValue);
											attributeObj.put("displayValue", formattedStringValue);
											checkDisplayOrder(map, formattedStringValue, skuarrayobj);
											data.put("displayType", "swatches");
										} else {
											double formattedValue = Double
													.valueOf(weightValue.replaceAll("[^0-9\\.]", ""));
											String formattedStringValue = Double.toString(formattedValue);
											checkDisplayOrder(map, formattedStringValue, skuarrayobj);
											attributeObj.put("displayValue", formattedStringValue);
										}
									} else if (name.equalsIgnoreCase("colour")) {
										String colourValue = attributesarray.getJSONObject(0).getJSONArray("values")
												.getString(0);
										attributeObj.put("displayValue", colourValue);
									} else {
										String otherValue = attributesarray.getJSONObject(0).getJSONArray("values")
												.getString(0);
										attributeObj.put("displayValue", otherValue);
									}
								}
							}

							if (name.equalsIgnoreCase("size") && (sizeValue.equalsIgnoreCase("xxsmall")
									|| sizeValue.equalsIgnoreCase("xsmall") || sizeValue.equalsIgnoreCase("small")
									|| sizeValue.equalsIgnoreCase("medium") || sizeValue.equalsIgnoreCase("large")
									|| sizeValue.equalsIgnoreCase("xlarge") || sizeValue.equalsIgnoreCase("xxlarge"))) {
								data.put("displayType", "swatches");
							} else {

								data.put("displayType", "chevron");
							}

						} else {
							JSONObject attributeObj = attributesarray.getJSONObject(0);
							if (attributeObj.has("name")) {
								name = attributeObj.getString("name");
								if (name.equalsIgnoreCase("size")) {
									sizeValue = attributesarray.getJSONObject(0).getJSONArray("values").getString(0);
									if (sizeValue.equalsIgnoreCase("xx small")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", "XXS");
										data.put("displayType", "swatches");
									} else if (sizeValue.equalsIgnoreCase("x small")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", "XS");
										data.put("displayType", "swatches");
									} else if (sizeValue.equalsIgnoreCase("small")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", "S");
										data.put("displayType", "swatches");
									} else if (sizeValue.equalsIgnoreCase("medium")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", "M");
										data.put("displayType", "swatches");
									} else if (sizeValue.equalsIgnoreCase("large")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", "L");
										data.put("displayType", "swatches");
									} else if (sizeValue.equalsIgnoreCase("x large")
											|| sizeValue.equalsIgnoreCase("jumbo")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", "XL");
										data.put("displayType", "swatches");
									} else if (sizeValue.equalsIgnoreCase("xx large")
											|| sizeValue.equalsIgnoreCase("Giant")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", "XXL");
										data.put("displayType", "swatches");
									} else if (sizeValue.matches(".*[/\\\\].*")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										attributeObj.put("displayValue", sizeValue);
										data.put("displayType", "chevron");
									} else if (sizeValue.contains("cm")) {
										sizeValue = sizeValue.replaceAll(" ", "");
										String checkInt = sizeValue.replaceAll("[^0-9\\.]", "");
										checkDisplayOrder(map, checkInt, skuarrayobj);
										attributeObj.put("displayValue", sizeValue);
										data.put("displayType", "chevron");
									} else if (sizeValue.contains("(") || sizeValue.contains("{")
											|| sizeValue.contains("[")) {
										String sizeValuenew = "";
										String[] parts = sizeValue.split("[\\{\\(\\)}]");

										for (int m = 0; m < parts.length - 1; m++) {
											sizeValuenew = parts[m];
											String checkInt = sizeValuenew.replaceAll("[^0-9\\.]", "");
											if (checkInt.matches("\\d+")) {
												Integer formattedValue = Integer
														.valueOf(sizeValuenew.replaceAll("[^0-9\\.]", ""));
												String formattedStringValue = Integer.toString(formattedValue);
												attributeObj.put("displayValue", formattedStringValue);

												checkDisplayOrder(map, formattedStringValue, skuarrayobj);

												data.put("displayType", "swatches");
											} else {

												double formattedValue = Double
														.valueOf(sizeValuenew.replaceAll("[^0-9\\.]", ""));
												String formattedStringValue = Double.toString(formattedValue);
												attributeObj.put("displayValue", formattedStringValue);

												checkDisplayOrder(map, formattedStringValue, skuarrayobj);

												data.put("displayType", "swatches");
											}
										}
									} else if (isAlpha(sizeValue)) {
										attributeObj.put("displayValue", sizeValue);
										checkDisplayOrder(map, sizeValue, skuarrayobj);
										data.put("displayType", "swatches");
									} else {
										String checkInt = sizeValue.replaceAll("[^0-9\\.]", "");
										if (checkInt.matches("\\d+")) {
											Integer formattedValue = Integer
													.valueOf(sizeValue.replaceAll("[^0-9\\.]", ""));
											String formattedStringValue = Integer.toString(formattedValue);
											attributeObj.put("displayValue", formattedStringValue);
											checkDisplayOrder(map, formattedStringValue, skuarrayobj);
											data.put("displayType", "swatches");
										} else {
											double formattedValue = Double
													.valueOf(sizeValue.replaceAll("[^0-9\\.]", ""));
											String formattedStringValue = Double.toString(formattedValue);
											attributeObj.put("displayValue", formattedStringValue);
											checkDisplayOrder(map, formattedStringValue, skuarrayobj);
											data.put("displayType", "swatches");
										}
									}
								} else if (name.equalsIgnoreCase("weight")) {
									String weightValue = attributesarray.getJSONObject(0).getJSONArray("values")
											.getString(0);

									String checkInt = weightValue.replaceAll("[^0-9\\.]", "");
									if (checkInt.matches("\\d+")) {
										Integer formattedValue = Integer
												.valueOf(weightValue.replaceAll("[^0-9\\.]", ""));
										String formattedStringValue = Integer.toString(formattedValue);
										attributeObj.put("displayValue", formattedStringValue);

										checkDisplayOrder(map, formattedStringValue, skuarrayobj);

										data.put("displayType", "swatches");
									} else {

										double formattedValue = Double.valueOf(weightValue.replaceAll("[^0-9\\.]", ""));
										String formattedStringValue = Double.toString(formattedValue);
										attributeObj.put("displayValue", formattedStringValue);

										checkDisplayOrder(map, formattedStringValue, skuarrayobj);

										data.put("displayType", "swatches");
									}
								} else if (name.equalsIgnoreCase("colour")) {
									String colourValue = attributesarray.getJSONObject(0).getJSONArray("values")
											.getString(0);
									attributeObj.put("displayValue", colourValue);
									data.put("displayType", "chevron");
								} else {
									String otherValue = attributesarray.getJSONObject(0).getJSONArray("values")
											.getString(0);
									attributeObj.put("displayValue", otherValue);
									data.put("displayType", "text");
								}
							}
						}
					} else {

						data.put("displayType", "chevron");
					}
				}
			}

			jsonObj.put("data", data);
			response = jsonObj.toString();
		} catch (Exception e) {
			logger.severe("Error in checkCondition Method. " + e.getMessage());
		}
		return response;
	}

	public static boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}

	public static void checkDisplayOrder(HashMap<Integer, String> map, String sizeValue, JSONObject skuarrayobj) {
		try {
			int displayOrder = 0;
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				int index = entry.getKey();
				String value = entry.getValue();
				if (value.equalsIgnoreCase(sizeValue)) {
					displayOrder = index + 1;
				}
			}
			skuarrayobj.put("displayOrder", displayOrder);
		} catch (Exception e) {
			logger.severe("Error in Check Display Order Method. " + e.getMessage());
		}
	}

}
