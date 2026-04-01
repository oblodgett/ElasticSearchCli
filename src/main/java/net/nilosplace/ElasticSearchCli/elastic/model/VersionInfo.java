package net.nilosplace.ElasticSearchCli.elastic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionInfo {

	private String name;

	@JsonProperty("cluster_name")
	private String clusterName;

	@JsonProperty("cluster_uuid")
	private String clusterUuid;

	private Version version;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Version {
		private String number;
		private String distribution;

		@JsonProperty("build_type")
		private String buildType;

		@JsonProperty("build_hash")
		private String buildHash;

		@JsonProperty("lucene_version")
		private String luceneVersion;
	}
}
