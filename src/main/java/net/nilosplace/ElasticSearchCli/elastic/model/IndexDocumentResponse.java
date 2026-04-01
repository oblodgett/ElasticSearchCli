package net.nilosplace.ElasticSearchCli.elastic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexDocumentResponse {

	@JsonProperty("_index")
	private String index;

	@JsonProperty("_id")
	private String id;

	@JsonProperty("_version")
	private long version;

	private String result;
}
