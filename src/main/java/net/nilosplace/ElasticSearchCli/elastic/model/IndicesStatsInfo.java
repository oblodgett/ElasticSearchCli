package net.nilosplace.ElasticSearchCli.elastic.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndicesStatsInfo {

	@JsonProperty("_all")
	private AllStats all;

	private Map<String, IndexStats> indices;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AllStats {
		private IndexStats.TotalStats total;
	}
}
