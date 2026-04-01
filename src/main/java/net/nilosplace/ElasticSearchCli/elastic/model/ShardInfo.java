package net.nilosplace.ElasticSearchCli.elastic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShardInfo {

	private String index;
	private String shard;
	private String prirep;
	private String state;
	private String docs;
	private String store;
	private String ip;
	private String node;
}
