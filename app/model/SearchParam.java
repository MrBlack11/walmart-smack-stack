package model;

public class SearchParam extends RefActors {
	private String productName;
	private String category;
	private Double minPrice;
	private Double maxPrice;

	/**
	 * Construct for search by name
	 * 
	 * @param productName
	 */
	public SearchParam(String productName) {
		this.productName = productName;
		this.category = "";
		this.minPrice = 0.0;
		this.maxPrice = 0.0;
	}

	/**
	 * Construct for search by category and price range
	 * 
	 * @param category
	 * @param minPrice
	 * @param maxPrice
	 */
	public SearchParam(String category, Double minPrice, Double maxPrice) {
		this.productName = "";
		this.category = category;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	/**
	 * Construct for search by name, category and price range
	 * 
	 * @param productName
	 * @param category
	 * @param minPrice
	 * @param maxPrice
	 */
	public SearchParam(String productName, String category, Double minPrice, Double maxPrice) {
		this.productName = productName;
		this.category = category;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

}
