package net.nilosplace.ElasticSearchCli.elastic.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodesStatsInfo {

	private Map<String, NodeStats> nodes;
}
