package net.nilosplace.ElasticSearchCli.commands.cluster;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.nodes.NodesStatsResponse;

public class ClusterNodesCommand extends ClusterCommand {

	@Override
	public void execute() {
		ElasticsearchClient client = configHelper.getEsClient();
		try {
			NodesStatsResponse resp = client.nodes().stats();
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
