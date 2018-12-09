package model;

import java.io.Serializable;
import java.util.Comparator;

public class ProductComparator implements Comparator<Product>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(Product product1, Product product2) {
		if (product1.getPrice() < product2.getPrice())
			return -1;
		if (product1.getPrice() > product2.getPrice())
			return 1;

		return 0;
	}
}
