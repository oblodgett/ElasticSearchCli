package net.nilosplace.ElasticSearchCli.commands.estop;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nilosplace.ElasticSearchCli.elastic.ClusterFacade;
import net.nilosplace.ElasticSearchCli.commands.estop.views.ViewHandler;
import net.nilosplace.ElasticSearchCli.utils.ConfigHelper;

public class ClusterDataPoller extends Thread {

	private static final Logger log = LoggerFactory.getLogger(ClusterDataPoller.class);
	private ConfigHelper configHelper = ConfigHelper.getInstance();
	private ClusterDataManager manager;
	private int pollInterval = 10;
	private ClusterFacade facade;
	private ViewHandler viewHandler;

	public ClusterDataPoller(ViewHandler viewHandler, ClusterDataManager manager, int pollInterval) {
		this.viewHandler = viewHandler;
		this.manager = manager;
		this.pollInterval = pollInterval;
		facade = configHelper.getClusterFacade();
	}

	public void run() {
		while (true) {
			try {
				Date start = new Date();
				manager.setMasterInfo(facade.catMaster());
				manager.setHealthInfo(facade.clusterHealth());
				manager.setNodesStatsInfo(facade.nodesStats());
				manager.setIndicesStatsInfo(facade.indicesStats());
				manager.setTaskList(facade.catTasks());
				manager.setShardList(facade.catShards());
				viewHandler.toggleDataUpdated();
				Date end = new Date();
				long pause = (pollInterval * 1000) - (end.getTime() - start.getTime());
				if (pause > 0) {
					Thread.sleep(pause);
				}
			} catch (InterruptedException e) {
				log.warn("Poller interrupted", e);
				viewHandler.setErrorMessage(e.getMessage());
				break;
			} catch (IOException e) {
				log.error("Polling error: {}", e.getMessage(), e);
				viewHandler.setErrorMessage(e.getMessage());
			} catch (Exception e) {
				log.error("Unexpected polling error: {}", e.getMessage(), e);
				viewHandler.setErrorMessage(e.getMessage());
			}
		}
	}

}
