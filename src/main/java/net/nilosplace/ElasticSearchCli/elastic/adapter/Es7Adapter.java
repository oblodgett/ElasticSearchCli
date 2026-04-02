package net.nilosplace.ElasticSearchCli.elastic.adapter;

import java.io.IOException;
import java.util.List;

import net.nilosplace.ElasticSearchCli.elastic.ClusterFacade;
import net.nilosplace.ElasticSearchCli.elastic.api.ElasticSearchApi;
import net.nilosplace.ElasticSearchCli.elastic.model.*;

public class Es7Adapter implements ClusterFacade {

	private final ElasticSearchApi api;

	public Es7Adapter(ElasticSearchApi api) {
		this.api = api;
	}

	@Override
	public VersionInfo getVersion() throws IOException {
		return api.getVersion();
	}

	@Override
	public HealthInfo clusterHealth() throws IOException {
		return api.clusterHealth();
	}

	@Override
	public NodesStatsInfo nodesStats() throws IOException {
		return api.nodesStats();
	}

	@Override
	public IndicesStatsInfo indicesStats() throws IOException {
		return api.indicesStats();
	}

	@Override
	public List<MasterInfo> catMaster() throws IOException {
		return api.catMaster("json");
	}

	@Override
	public List<ShardInfo> catShards() throws IOException {
		return api.catShards("json", SHARD_COLUMNS);
	}

	@Override
	public List<TaskInfo> catTasks() throws IOException {
		return api.catTasks("json");
	}

	@Override
	public ClusterSettingsInfo clusterSettings(boolean includeDefaults) throws IOException {
		return api.clusterSettings(includeDefaults);
	}

	@Override
	public IndexDocumentResponse indexDocument(String index, Object document) throws IOException {
		return api.indexDocument(index, document);
	}
}
