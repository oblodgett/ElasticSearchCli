package net.nilosplace.ElasticSearchCli.commands.cluster;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.InfoResponse;
import java.io.IOException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.InfoResponse;
import co.elastic.clients.elasticsearch.nodes.ElasticsearchNodesClient;
import co.elastic.clients.elasticsearch.nodes.NodesInfoResponse;

public class ClusterInfoCommand extends ClusterCommand {

	@Override
	public void print() {

	}

	@Override
	public void execute() {
		ElasticsearchClient client = configHelper.getEsClient();
		try {
			InfoResponse resp = client.info();
			System.out.println(resp);
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
			System.err.println("Error: " + e.getMessage() + " Host: " + configHelper.getConfig().get("es.host") + ":" + configHelper.getConfig().get("es.port"));
		}
	}

}
