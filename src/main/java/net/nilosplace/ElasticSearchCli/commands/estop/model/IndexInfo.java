package net.nilosplace.ElasticSearchCli.commands.estop.model;

import lombok.Data;

@Data
public class IndexInfo {
	private String id;
	private String name;
	private Long docCount;
	private Long size;
	private Long primaryShardCount;
	private Long totalShardCount;
	private int totalSegmentCount;
}
