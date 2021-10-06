package com.campaign.model;

public class Promotion {

	private String productName;
	private int qty;
	private int cost;

	public Promotion(int qty, String productName, int cost) {
		this.productName = productName;
		this.cost = cost;
		this.qty = qty;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
}
