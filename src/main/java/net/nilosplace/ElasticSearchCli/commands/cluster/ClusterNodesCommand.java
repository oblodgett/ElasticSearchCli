package net.nilosplace.ElasticSearchCli.commands.cluster;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.nodes.NodesInfoResponse;

public class ClusterNodesCommand extends ClusterCommand {

	@Override
	public void print() {
	}

	@Override
	public void execute() {
		ElasticsearchClient client = configHelper.getEsClient();
		try {
			NodesInfoResponse resp = client.nodes().info();
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
