package net.nilosplace.ElasticSearchCli.commands.cluster;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.GetClusterSettingsRequest;
import co.elastic.clients.elasticsearch.cluster.GetClusterSettingsResponse;

public class ClusterConfigGetCommand extends ClusterCommand {

	private String configName;

	public ClusterConfigGetCommand(String configName) {
		this.configName = configName;
	}

	@Override
	public void print() {

	}

	@Override
	public void execute() {
		ElasticsearchClient client = configHelper.getEsClient();
		try {
			GetClusterSettingsRequest request = new GetClusterSettingsRequest.Builder().includeDefaults(true).build();
			GetClusterSettingsResponse resp = client.cluster().getSettings(request);
			System.out.println(mapper.readTree(jsonUtil.toString(resp).replace("GetClusterSettingsResponse: ", "")).toPrettyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
