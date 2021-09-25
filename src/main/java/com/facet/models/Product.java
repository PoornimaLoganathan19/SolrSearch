package com.facet.models;

	import org.apache.solr.client.solrj.beans.Field;
	import org.springframework.data.annotation.Id;
	import org.springframework.data.solr.core.mapping.SolrDocument;
	@SolrDocument(solrCoreName = "cricutsearch")
	public class Product {
		
	  @Id
	  @Field
	  private String productId;
	  @Field
	  private String displayName;
	  
	  @Field
	  private String categoryId;
	  			
	  @Field
	  private String categoryName;
	  
	  @Field
	  private String longDescription;
	  
	  @Field
	  private String color;
	  
	  @Field
	  private double priceValue;

	  public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getProductId() {
			return productId;
		}
		
		public void setProductId(String productId) {
			this.productId = productId;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		
		public String getCategoryId() {
			return categoryId;
		}
		
		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}
		
		public String getCategoryName() {
			return categoryName;
		}
		
		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		
		public String getLongDescription() {
			return longDescription;
		}
		
		public void setLongDescription(String longDescription) {
			this.longDescription = longDescription;
		}
		
		public double getPriceValue() {
			return priceValue;
		}
		
		public void setPriceValue(double priceValue) {
			this.priceValue = priceValue;
		}
		
		public Product() {
			super();
		}

	public Product(String productId, String displayName, String categoryId, String categoryName, String longDescription,String color,
			double priceValue) {
		super();
		this.productId = productId;
		this.displayName = displayName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.longDescription = longDescription;
		this.color = color;
		this.priceValue = priceValue;
	}
	}