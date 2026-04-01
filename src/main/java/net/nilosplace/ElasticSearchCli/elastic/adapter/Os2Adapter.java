package net.nilosplace.ElasticSearchCli.elastic.adapter;

import java.io.IOException;
import java.util.List;

import net.nilosplace.ElasticSearchCli.elastic.ClusterFacade;
import net.nilosplace.ElasticSearchCli.elastic.api.OpenSearchApi;
import net.nilosplace.ElasticSearchCli.elastic.model.*;

public class Os2Adapter implements ClusterFacade {

	private final OpenSearchApi api;

	public Os2Adapter(OpenSearchApi api) {
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
		return api.catClusterManager("json");
	}

	@Override
	public List<ShardInfo> catShards() throws IOException {
		return api.catShards("json");
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
