package net.nilosplace.ElasticSearchCli.commands.estop;

import java.io.IOException;
import java.util.Date;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.cat.CountResponse;
import co.elastic.clients.elasticsearch.cat.HealthResponse;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.cat.NodesRequest;
import co.elastic.clients.elasticsearch.cat.NodesResponse;
import co.elastic.clients.elasticsearch.cat.ShardsResponse;
import co.elastic.clients.elasticsearch.indices.stats.IndicesStats;
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
				NodesResponse nodeResp = client.cat().nodes();
				manager.setNodeRecords(nodeResp.valueBody());
				IndicesResponse indexResp = client.cat().indices();
				manager.setIndicesRecords(indexResp.valueBody());
				HealthResponse healthResp = client.cat().health();
				manager.setHealthRecords(healthResp.valueBody());
				ShardsResponse shardsResp = client.cat().shards();
				manager.setShardRecords(shardsResp.valueBody());
				CountResponse countResp = client.cat().count();
				manager.setCountRecords(countResp.valueBody());
				IndicesStats statsResp = client.indices().stats().all();
				manager.setIndicesStats(statsResp);
				viewHandler.toggleDataUpdated();
				Date end = new Date();
				long pause = (pollInterval * 1000) - (end.getTime() - start.getTime());
				if (pause > 0) {
					Thread.sleep(pause);
				}
			} catch (InterruptedException e) {
				viewHandler.setErrorMessage(e.getMessage());
			} catch (ElasticsearchException e) {
				viewHandler.setErrorMessage(e.getMessage());
			} catch (IOException e) {
				viewHandler.setErrorMessage(e.getMessage());
			}
		}
	}

}
