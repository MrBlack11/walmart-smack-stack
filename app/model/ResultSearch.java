package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultSearch extends RefActors implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Product> resultProducts;
	private String teste;

	public ResultSearch() {
		this.resultProducts = new ArrayList<>();
	}

	public List<Product> getResultProducts() {
		return resultProducts;
	}

	public void setResultProducts(List<Product> resultProducts) {
		this.resultProducts = resultProducts;
	}

	public String getTeste() {
		return teste;
	}

	public void setTeste(String teste) {
		this.teste = teste;
	}
}
