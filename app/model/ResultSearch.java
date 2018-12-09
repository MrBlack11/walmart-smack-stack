package model;

import java.util.List;

public class ResultSearch extends RefActors {
	private List<Product> resultProducs;

	public ResultSearch(List<Product> resultProducs) {
		this.resultProducs = resultProducs;
	}

	public List<Product> getResultProducs() {
		return resultProducs;
	}

	public void setResultProducs(List<Product> resultProducs) {
		this.resultProducs = resultProducs;
	}
}
