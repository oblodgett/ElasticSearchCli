package net.nilosplace.ElasticSearchCli.elastic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthInfo {

	@JsonProperty("cluster_name")
	private String clusterName;

	private String status;

	@JsonProperty("timed_out")
	private boolean timedOut;

	@JsonProperty("number_of_nodes")
	private int numberOfNodes;

	@JsonProperty("number_of_data_nodes")
	private int numberOfDataNodes;

	@JsonProperty("active_primary_shards")
	private int activePrimaryShards;

	@JsonProperty("active_shards")
	private int activeShards;

	@JsonProperty("relocating_shards")
	private int relocatingShards;

	@JsonProperty("initializing_shards")
	private int initializingShards;

	@JsonProperty("unassigned_shards")
	private int unassignedShards;

	@JsonProperty("delayed_unassigned_shards")
	private int delayedUnassignedShards;

	@JsonProperty("number_of_pending_tasks")
	private int numberOfPendingTasks;

	@JsonProperty("number_of_in_flight_fetch")
	private int numberOfInFlightFetch;

	@JsonProperty("task_max_waiting_in_queue_millis")
	private long taskMaxWaitingInQueueMillis;

	@JsonProperty("active_shards_percent_as_number")
	private double activeShardsPercentAsNumber;
}
