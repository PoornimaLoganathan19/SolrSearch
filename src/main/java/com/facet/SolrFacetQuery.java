package com.facet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.facet.models.FacetFields;
import com.facet.models.Options;
import com.facet.models.SearchTerms;
import static com.facet.constants.FacetFields.*;
import static com.facet.constants.OptionTypes.*;
/**
 * 
 * @author PL00557272
 * 
 */
@RestController
@RequestMapping("/cricut")
public class SolrFacetQuery {
	
//	@Autowired
//	private ProductRepository productRepository;
	@RequestMapping(method=RequestMethod.GET, value="/getAllDocs")
	public Map<String, List<Map<String, Object>>> getAllIndexedDocuments() throws SolrServerException {
		
		List<Map<String, Object>> documents = new ArrayList<>();
		Map<String,List<Map<String, Object>>> product = new HashMap<>();
		
		HttpSolrServer solr = new HttpSolrServer(SOLR_URL);
		
		SolrQuery query = new SolrQuery();
		
		query.setQuery(DEFAULT_QUERY);
		query.setRows(ROWS);
		QueryRequest request = new QueryRequest(query);
		QueryResponse response = request.process(solr);
		
		for(SolrDocument solrDocument : response.getResults()) {
			documents.add(solrDocument);
       }
		product.put("products", documents);
	    return product;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getOneDoc/{productId}")
	public Map<String, List<Map<String, Object>>> getOneIndexedDocuments(@PathVariable String productId) throws SolrServerException {
		
		List<Map<String, Object>> documents = new ArrayList<>();
		Map<String,List<Map<String, Object>>> product = new HashMap<>();
		
		HttpSolrServer solr = new HttpSolrServer(SOLR_URL);
		
		SolrQuery query = new SolrQuery();
		
		query.setQuery(productId);
		query.setRows(ROWS);
		QueryRequest request = new QueryRequest(query);
		QueryResponse response = request.process(solr);
		
		for(SolrDocument solrDocument : response.getResults()) {
			documents.add(solrDocument);
		}
		product.put("products", documents);
	    return product;
	}

	@RequestMapping(method=RequestMethod.GET, value="/getFacetFields")
	public Map<String, List<FacetFields>> getAllFacets() throws SolrServerException {
       
		HttpSolrServer solr = new HttpSolrServer(SOLR_URL);
		
		SolrQuery query = new SolrQuery().setFacet(true);
		
		query.setQuery(DEFAULT_QUERY);
		query.setRows(FACET_ROWS);
		query.addFacetField(COLOR);
		
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append(PRICE_VALUE).append(":[").append(DEFAULT_MIN_PRICE).append(" TO ")
		.append(DEFAULT_MAX_PRICE).append("]");
		
		query.addFacetQuery(sbQuery.toString());
		QueryRequest request = new QueryRequest(query);
		QueryResponse response = request.process(solr);
		
		Map<String, List<FacetFields>> facet = new HashMap<>();
		
		List<FacetFields> facetList = new ArrayList<FacetFields>();
		final List<Options> options = new ArrayList<Options>();
		List<FacetField> facetFields = response.getFacetFields();
		int count = 1;
//		int count1 = 1;
//		for(FacetField facetField : facetFields) {
//			int counter = 1;
//				for(FacetField.Count count : facetField.getValues()) {
//					options.add(new Options(count.getName(),count.getCount(),counter));
//					counter++;
//				}
//			facetList.add(new FacetFields(facetField.getName(), facetField.getName(), RADIO, count1, options));	
//			count1++;
//		}
		for(int i=0; i < facetFields.size(); i++) {
			
			System.out.println("Facet Size" +facetFields.size());
			
			FacetField facetField = facetFields.get(i);
			System.out.println(facetField.getName());
			int counter = 1;
			List<Count> facetInfos = facetField.getValues();
			for(FacetField.Count facetInstance : facetInfos) {
				
				String name = facetInstance.getName();
				long count1 = facetInstance.getCount();
				
				System.out.println("Name:" + name + "/t Count:" +count1);
				options.add(new Options(facetInstance.getName(),facetInstance.getCount(),counter));
				counter++;
			}
			if(facetField.getName().contains(COLOR)) {
				facetList.add(new FacetFields(facetField.getName(), facetField.getName(), RADIO, count, options));
				count++;	
			}
		}
		facet.put("facets", facetList);
		return facet;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method=RequestMethod.POST, value="/addFacetFields")
	public Map addFacetFields(@RequestBody SearchTerms searchTerms) throws SolrServerException {
		
		HttpSolrServer solr = new HttpSolrServer(SOLR_URL);
		
		SolrQuery query = new SolrQuery();
		
		// For  COLOR
		StringBuilder facetQuery = new StringBuilder();
		StringBuilder colorsQuery = new StringBuilder();
		if(searchTerms.getColors()!= null && searchTerms.getColors().size() > 0) {
			String prefix = "";
			for (String color : searchTerms.getColors()) {
				facetQuery.append(prefix);
				facetQuery.append(COLOR).append(":");
				prefix = " OR ";
				facetQuery.append(color);
			}
			colorsQuery.append("(").append(facetQuery.toString()).append(")");
			System.out.println("Faceted Query" + colorsQuery);
		} else {
		    colorsQuery.append("(").append(COLOR).append(": ")
		    		   .append("*:*").append(")");
		}
		
		// For PRICE_VALUE
		StringBuilder priceQuery = new StringBuilder();
		if((searchTerms.getMinPrice())==0 && (searchTerms.getMaxPrice()) > 0) {
			priceQuery.append(PRICE_VALUE).append(":[")
			.append(searchTerms.getMinPrice()).append(" TO ").append(searchTerms.getMaxPrice()).append("]");
		} else {
			priceQuery.append(PRICE_VALUE).append(":[").append("*")
					  .append(" TO ").append("*").append("]");
		}
		
		// For SEARCH_TEXT
		StringBuilder searchTextQuery = new StringBuilder();
		if(searchTerms.getSearchText()!=null) {
			searchTextQuery.append(searchTerms.getSearchText());
		} else {
			searchTerms.setSearchText("*");
			searchTextQuery.append("(")
			               .append(searchTerms.getSearchText())
			               .append(")");
		}
		
		//  For CATEGORY_ID
		StringBuilder categoryIdQuery = new StringBuilder();
		if(searchTerms.getCategoryId()!=null) {
			categoryIdQuery.append("(").append(CATEGORY_NAME)
						   .append(": ").append(searchTerms.getCategoryId())
						   .append(")");
		} else {
			searchTerms.setCategoryId("*");
			categoryIdQuery.append("(").append(CATEGORY_NAME)
			   .append(": ").append(searchTerms.getCategoryId())
			   .append(")");
		}
		// Overall SOLR QUERY
		StringBuilder solrQuery = new StringBuilder();
		solrQuery.append(searchTextQuery.toString()).append(" AND ")
		 		 .append(categoryIdQuery.toString()).append(" AND ")
		 		 .append(colorsQuery.toString()).append(" AND ")
		         .append(priceQuery.toString());
		System.out.println("SOLR QUERY:" + solrQuery.toString());
		
		query.setFacet(true);
		query.setQuery(solrQuery.toString());
//		query.setQuery(searchTextQuery.toString() + " AND " + categoryIdQuery.toString() + " AND " + colorsQuery.toString() + " AND " + priceQuery.toString());
		query.setStart(searchTerms.getStart());
		query.setRows(ROWS);
		query.addFacetField(COLOR);
		query.setSort(PRICE_VALUE, ORDER.asc);
//		query.addFacetQuery(priceQuery.toString());
		
		QueryRequest request = new QueryRequest(query);
		QueryResponse response = request.process(solr);
		
	// Get Products	
		List<Map<String, Object>> documents = new ArrayList<>();
		Map<String,List<Map<String, Object>>> product = new HashMap<>();
		
		for(SolrDocument solrDocument : response.getResults()) {
			documents.add(solrDocument);
		}
		product.put("products", documents);
		
	// Get Facets
        Map<String, List<FacetFields>> facet = new HashMap<>();
		
		List<FacetFields> facetList = new ArrayList<>();
		final List<Options> options = new ArrayList<Options>();
		
		List<FacetField> facetFields = response.getFacetFields();
		int count = 1;
		for(int i=0; i < facetFields.size(); i++) {
			FacetField facetField = facetFields.get(i);
			int counter = 1;
			List<Count> facetInfos = facetField.getValues();
			for(FacetField.Count facetInstance : facetInfos) {
				options.add(new Options(facetInstance.getName(),facetInstance.getCount(),counter));
				counter++;
			}
			if(facetField.getName().contains(COLOR)) {
				facetList.add(new FacetFields(facetField.getName(), facetField.getName(), RADIO, count, options));
				count++;	
			}	
		}
		facet.put("facets", facetList);	
		
		Map finalMap = new HashMap<>();
		finalMap.putAll(product);
		finalMap.putAll(facet);
		
		return finalMap;
	}
}
