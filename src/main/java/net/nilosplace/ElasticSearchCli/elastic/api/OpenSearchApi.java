package net.nilosplace.ElasticSearchCli.elastic.api;

import java.io.IOException;
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import net.nilosplace.ElasticSearchCli.elastic.model.*;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface OpenSearchApi {

	@GET
	@Path("/")
	VersionInfo getVersion() throws IOException;

	@GET
	@Path("_cluster/health")
	HealthInfo clusterHealth() throws IOException;

	@GET
	@Path("_nodes/stats")
	NodesStatsInfo nodesStats() throws IOException;

	@GET
	@Path("_stats")
	IndicesStatsInfo indicesStats() throws IOException;

	@GET
	@Path("_cat/cluster_manager")
	List<MasterInfo> catClusterManager(@QueryParam("format") String format) throws IOException;

	@GET
	@Path("_cat/shards")
	List<ShardInfo> catShards(@QueryParam("format") String format) throws IOException;

	@GET
	@Path("_cat/tasks")
	List<TaskInfo> catTasks(@QueryParam("format") String format) throws IOException;

	@GET
	@Path("_cluster/settings")
	ClusterSettingsInfo clusterSettings(@QueryParam("include_defaults") boolean includeDefaults) throws IOException;

	@POST
	@Path("{index}/_doc")
	@Consumes(MediaType.APPLICATION_JSON)
	IndexDocumentResponse indexDocument(@PathParam("index") String index, Object document) throws IOException;
}
