package net.nilosplace.ElasticSearchCli.commands.estop;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.cat.CountResponse;
import co.elastic.clients.elasticsearch.cat.HealthResponse;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.cat.NodesResponse;
import co.elastic.clients.elasticsearch.cat.ShardsResponse;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.nodes.NodesInfoResponse;
import co.elastic.clients.elasticsearch.nodes.info.NodeInfo;
import net.nilosplace.ElasticSearchCli.utils.ConfigHelper;

public class ClusterDataPoller extends Thread {

	private ConfigHelper configHelper = ConfigHelper.getInstance();
	private ClusterDataManager manager;
	private int pollInterval = 10;
	private ElasticsearchClient client;

	public ClusterDataPoller(ClusterDataManager manager, int pollInterval) {
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
				Date end = new Date();
				Thread.sleep((pollInterval * 1000) - (end.getTime() - start.getTime()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ElasticsearchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(pollInterval * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
