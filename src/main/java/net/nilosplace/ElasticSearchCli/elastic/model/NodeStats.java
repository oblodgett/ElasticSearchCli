package net.nilosplace.ElasticSearchCli.elastic.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeStats {

	private String name;
	private String host;
	private JvmStats jvm;
	private OsStats os;
	private FsStats fs;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class JvmStats {
		private MemStats mem;

		@JsonProperty("uptime_in_millis")
		private long uptimeInMillis;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MemStats {
		@JsonProperty("heap_used_in_bytes")
		private long heapUsedInBytes;

		@JsonProperty("heap_max_in_bytes")
		private long heapMaxInBytes;

		@JsonProperty("heap_used_percent")
		private int heapUsedPercent;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OsStats {
		private CpuStats cpu;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CpuStats {
		private Integer percent;

		@JsonProperty("load_average")
		private Map<String, Double> loadAverage;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FsStats {
		private FsTotalStats total;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FsTotalStats {
		@JsonProperty("available_in_bytes")
		private long availableInBytes;

		@JsonProperty("total_in_bytes")
		private long totalInBytes;
	}
}
