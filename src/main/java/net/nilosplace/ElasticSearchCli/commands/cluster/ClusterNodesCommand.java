package net.nilosplace.ElasticSearchCli.commands.cluster;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cat.NodesResponse;

public class ClusterNodesCommand extends ClusterCommand {

	@Override
	public void execute() {
		ElasticsearchClient client = configHelper.getEsClient();
		try {
			NodesResponse resp = client.cat().nodes();
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
