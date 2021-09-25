package com.facet;

import static com.facet.constants.FacetFields.SOLR_URL;

import java.util.List;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class FacetQuery {
	public String getFacetQuery() throws SolrServerException {
		
		final HttpSolrServer solr = new HttpSolrServer(SOLR_URL);
	
		final SolrQuery query = new SolrQuery().setFacet(true);
		// set facet field and facet query for solr
		query.setQuery("color:raspberry AND priceValue:[* TO 300]");
		query.setRows(15);
		query.addFacetField("color");
		query.addFacetQuery("priceValue:[* TO 500]");
		
		// Query Request for solr
		final QueryRequest request = new QueryRequest(query);
		
		// Query Response for solr
		final QueryResponse response = request.process(solr);
		
		// Overall query results containing the records related query and facet properties
       //System.out.println("RESPONSE" +response.getResults());
		
		final SolrDocumentList docs = response.getResults();
		final long numFound = docs.getNumFound();
		System.out.println("NumberFOund: " +numFound);
		int docsCounter = 0;
		for(final SolrDocument doc: docs) {
			docsCounter++;
			System.out.println("doccounter" +docsCounter);
			for(final Entry<String, Object> fields : doc.entrySet()) {
				final String name = fields.getKey();
				final Object value = fields.getValue();
				System.out.println("\t" + name + "=" + value  );
			}
		}
		// To query out the facet output
		System.out.println("Response: " +response.getFacetFields());
		
		final List<FacetField> facetFields = response.getFacetFields();
		for(int i = 0; i< facetFields.size(); i++) {
			final FacetField facetField = facetFields.get(i);
			System.out.println("facetFields: " +facetField);
			final List<Count> facetInfo = facetField.getValues();
			System.out.println("facetInfo: " +facetInfo);
			for(final FacetField.Count facetInstance : facetInfo) {
				System.out.println(facetInstance.getName() + ":" + 
			facetInstance.getCount());
			}
		}	
		// Output for facet query
		System.out.println("FacetQuery: " +response.getFacetQuery());
		// Overall Query
		System.out.println("SOLR QUERY:");
		System.out.println(SOLR_URL + "/select?indent=on&" + query + "&wt=json" );
		
	return "success";
	}
}
