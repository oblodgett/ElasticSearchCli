package net.nilosplace.ElasticSearchCli.elastic;

import java.io.IOException;
import java.util.List;

import net.nilosplace.ElasticSearchCli.elastic.model.*;

public interface ClusterFacade {

	String SHARD_COLUMNS = "index,shard,prirep,state,docs,store,ip,node,segments.count,segments.memory,search.query_total,unassigned.reason";

	VersionInfo getVersion() throws IOException;

	HealthInfo clusterHealth() throws IOException;

	NodesStatsInfo nodesStats() throws IOException;

	IndicesStatsInfo indicesStats() throws IOException;

	List<MasterInfo> catMaster() throws IOException;

	List<ShardInfo> catShards() throws IOException;

	List<TaskInfo> catTasks() throws IOException;

	ClusterSettingsInfo clusterSettings(boolean includeDefaults) throws IOException;

	IndexDocumentResponse indexDocument(String index, Object document) throws IOException;
}
