package net.nilosplace.ElasticSearchCli.commands.cluster;

import java.io.IOException;

import net.nilosplace.ElasticSearchCli.elastic.ClusterFacade;
import net.nilosplace.ElasticSearchCli.elastic.model.HealthInfo;

public class ClusterInfoCommand extends ClusterCommand {

	@Override
	public void execute() {
		ClusterFacade facade = configHelper.getClusterFacade();
		try {
			HealthInfo resp = facade.clusterHealth();
			System.out.println(resp);
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage() + " Host: " + configHelper.getConfig().get("es.host") + ":" + configHelper.getConfig().get("es.port"));
		}
	}

}
