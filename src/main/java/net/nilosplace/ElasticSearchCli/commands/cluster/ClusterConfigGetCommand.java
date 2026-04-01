package net.nilosplace.ElasticSearchCli.commands.cluster;

import net.nilosplace.ElasticSearchCli.elastic.ClusterFacade;
import net.nilosplace.ElasticSearchCli.elastic.model.ClusterSettingsInfo;

public class ClusterConfigGetCommand extends ClusterCommand {

	private String configName;

	public ClusterConfigGetCommand(String configName) {
		this.configName = configName;
		configName = this.configName;
	}

	@Override
	public void execute() {
		ClusterFacade facade = configHelper.getClusterFacade();
		try {
			ClusterSettingsInfo resp = facade.clusterSettings(true);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
