package com.facet.models;

public class Options {

	private String name;
	private long quantity;
	private int sequenceId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	public Options(String name, long quantity, int sequenceId) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.sequenceId = sequenceId;
	}
	public Options() {
		super();
	}
}
