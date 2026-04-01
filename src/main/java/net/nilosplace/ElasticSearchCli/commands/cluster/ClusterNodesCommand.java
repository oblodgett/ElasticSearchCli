package net.nilosplace.ElasticSearchCli.commands.cluster;

import net.nilosplace.ElasticSearchCli.elastic.ClusterFacade;
import net.nilosplace.ElasticSearchCli.elastic.model.NodesStatsInfo;

public class ClusterNodesCommand extends ClusterCommand {

	@Override
	public void execute() {
		ClusterFacade facade = configHelper.getClusterFacade();
		try {
			NodesStatsInfo resp = facade.nodesStats();
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
