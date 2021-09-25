package com.facet.models;

import java.util.List;

public class SearchTerms {

	private String searchText;
	private String categoryId;
	private List<String> colors;
	private int minPrice;
	private int maxPrice;
	private int start;
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public List<String> getColors() {
		return colors;
	}
	public void setColors(List<String> colors) {
		this.colors = colors;
	}
	public int getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}
	public int getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public SearchTerms(String searchText, String categoryId, List<String> colors, int minPrice, int maxPrice, int start) {
		super();
		this.searchText = searchText;
		this.categoryId = categoryId;
		this.colors = colors;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.start = start;
	}
	public SearchTerms() {
		super();
	}
}
