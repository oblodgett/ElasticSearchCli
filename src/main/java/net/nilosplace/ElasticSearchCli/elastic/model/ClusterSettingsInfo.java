package net.nilosplace.ElasticSearchCli.elastic.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClusterSettingsInfo {

	private Map<String, Object> persistent;

	@JsonProperty("transient")
	private Map<String, Object> transientSettings;

	private Map<String, Object> defaults;
}
