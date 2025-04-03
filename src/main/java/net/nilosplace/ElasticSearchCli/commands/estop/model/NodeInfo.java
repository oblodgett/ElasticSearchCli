package net.nilosplace.ElasticSearchCli.commands.estop.model;

import java.util.Map;

import lombok.Data;

@Data
public class NodeInfo {

	private boolean master;
	private String name;
	private String version;
	private String ip;
	private String heap;
	private String disk;
	private Map<String, Double> loadAverage;
	private Integer cpuPercent;
	private String uptime;
}
