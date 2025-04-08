package net.nilosplace.ElasticSearchCli.commands.cluster;

import java.io.IOException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;

public class ClusterInfoCommand extends ClusterCommand {

	@Override
	public void execute() {
		ElasticsearchClient client = configHelper.getEsClient();
		try {
			HealthResponse resp = client.cluster().health();
			System.out.println(resp);
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage() + " Host: " + configHelper.getConfig().get("es.host") + ":" + configHelper.getConfig().get("es.port"));
		}
	}

}
