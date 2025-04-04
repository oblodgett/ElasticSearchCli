package net.nilosplace.ElasticSearchCli.commands.estop;

import java.io.IOException;
import java.util.Date;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import net.nilosplace.ElasticSearchCli.commands.estop.views.ViewHandler;
import net.nilosplace.ElasticSearchCli.utils.ConfigHelper;

public class ClusterDataPoller extends Thread {

	private ConfigHelper configHelper = ConfigHelper.getInstance();
	private ClusterDataManager manager;
	private int pollInterval = 10;
	private ElasticsearchClient client;
	private ViewHandler viewHandler;

	public ClusterDataPoller(ViewHandler viewHandler, ClusterDataManager manager, int pollInterval) {
		this.viewHandler = viewHandler;
		this.manager = manager;
		this.pollInterval = pollInterval;
		client = configHelper.getEsClient();
	}

	public void run() {
		while (true) {
			try {
				Date start = new Date();
				manager.setMasterResp(client.cat().master());
				manager.setHealthResp(client.cluster().health());
				manager.setNodesStatsResp(client.nodes().stats());
				manager.setIndicesStatsResp(client.indices().stats());
				manager.setTasksResp(client.cat().tasks());
				viewHandler.toggleDataUpdated();
				Date end = new Date();
				long pause = (pollInterval * 1000) - (end.getTime() - start.getTime());
				if (pause > 0) {
					Thread.sleep(pause);
				}
			} catch (InterruptedException e) {
				viewHandler.setErrorMessage(e.getMessage());
				break;
			} catch (ElasticsearchException e) {
				viewHandler.setErrorMessage(e.getMessage());
			} catch (IOException e) {
				viewHandler.setErrorMessage(e.getMessage());
			}
		}
	}

}
