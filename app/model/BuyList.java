package model;

import java.util.List;

public class BuyList extends RefActors {
	private List<Product> products;

	public BuyList(List<Product> products) {
		this.products = products;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
