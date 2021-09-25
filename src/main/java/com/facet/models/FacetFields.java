package com.facet.models;

import java.util.List;

public class FacetFields {

	private String facetId;
	private String displayName;
	private String type;
	private int sequenceId;
	private List<Options> options;
	
	public String getFacetId() {
		return facetId;
	}
	public void setFacetId(final String facetId) {
		this.facetId = facetId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}
	public String getType() {
		return type;
	}
	public void setType(final String type) {
		this.type = type;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(final int sequenceId) {
		this.sequenceId = sequenceId;
	}
	public List<Options> getOptions() {
		return options;
	}
	public void setOptions(final List<Options> options) {
		this.options = options;
	}
	public FacetFields(final String facetId, final String displayName, final String type, final int sequenceId, final List<Options> options) {
		super();
		this.facetId = facetId;
		this.displayName = displayName;
		this.type = type;
		this.sequenceId = sequenceId;
		this.options = options;
	}
	public FacetFields() {
		super();
	}
}
