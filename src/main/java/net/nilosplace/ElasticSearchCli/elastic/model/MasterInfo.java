package net.nilosplace.ElasticSearchCli.elastic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MasterInfo {

	private String id;
	private String host;
	private String ip;
	private String node;
}
