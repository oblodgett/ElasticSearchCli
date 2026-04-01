package net.nilosplace.ElasticSearchCli.elastic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexStats {

	private String uuid;
	private TotalStats total;
	private PrimariesStats primaries;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TotalStats {
		private DocsStats docs;
		private StoreStats store;

		@JsonProperty("shard_stats")
		private ShardStatsCount shardStats;

		private SegmentsStats segments;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PrimariesStats {
		@JsonProperty("shard_stats")
		private ShardStatsCount shardStats;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DocsStats {
		private long count;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StoreStats {
		@JsonProperty("size_in_bytes")
		private long sizeInBytes;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ShardStatsCount {
		@JsonProperty("total_count")
		private long totalCount;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SegmentsStats {
		private int count;
	}
}
