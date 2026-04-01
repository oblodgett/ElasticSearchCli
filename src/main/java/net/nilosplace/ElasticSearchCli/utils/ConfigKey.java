package net.nilosplace.ElasticSearchCli.utils;

import lombok.Getter;

@Getter
public enum ConfigKey {

	ES_PROT("es.prot", "http", "Protocol (http/https)"),
	ES_HOST("es.host", "localhost", "Elasticsearch host"),
	ES_PORT("es.port", "9200", "Elasticsearch port"),
	CONFIG_DIR("config.dir", "configs", "Directory for config files");

	private final String key;
	private final String defaultValue;
	private final String description;

	ConfigKey(String key, String defaultValue, String description) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.description = description;
	}

	public static ConfigKey fromKey(String key) {
		for (ConfigKey ck : values()) {
			if (ck.key.equals(key)) {
				return ck;
			}
		}
		return null;
	}
}
