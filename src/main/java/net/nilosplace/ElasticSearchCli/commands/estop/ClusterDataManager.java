package net.nilosplace.ElasticSearchCli.commands.estop;

import java.util.List;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

import co.elastic.clients.elasticsearch.cat.count.CountRecord;
import co.elastic.clients.elasticsearch.cat.health.HealthRecord;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.cat.nodes.NodesRecord;
import co.elastic.clients.elasticsearch.cat.shards.ShardsRecord;
import co.elastic.clients.elasticsearch.indices.stats.IndicesStats;
import lombok.Data;

@Data
public class ClusterDataManager {
	private TextColor clusterColor = ANSI.GREEN_BRIGHT;
	private List<IndicesRecord> indicesRecords;
	private List<NodesRecord> nodeRecords;
	private List<HealthRecord> healthRecords;
	private List<ShardsRecord> shardRecords;
	private List<CountRecord> countRecords;
	private IndicesStats indicesStats;

	public String getClusterName() {
		if (healthRecords != null && healthRecords.size() > 0) {
			return healthRecords.get(0).cluster();
		}
		return "Cluster Unknown";
	}

	public TextColor getClusterColor() {
		if (healthRecords != null && healthRecords.size() > 0) {
			if (healthRecords.get(0).status().equals("green")) {
				return ANSI.GREEN_BRIGHT;
			}
			if (healthRecords.get(0).status().equals("yellow")) {
				return ANSI.YELLOW_BRIGHT;
			}
			if (healthRecords.get(0).status().equals("red")) {
				return ANSI.RED_BRIGHT;
			}
		}
		return ANSI.GREEN_BRIGHT;
	}

	public String getNodeTotal() {
		if (healthRecords != null && healthRecords.size() > 0) {
			return healthRecords.get(0).nodeData();
		}
		return "0";

	}

	public int getIndicesTotal() {
		if (indicesRecords != null && indicesRecords.size() > 0) {
			return indicesRecords.size();
		}
		return 0;
	}

	public int getShardsTotal() {
		if (shardRecords != null && shardRecords.size() > 0) {
			return shardRecords.size();
		}
		return 0;
	}

	public long getCountTotal() {
		if (indicesStats != null) {
			return indicesStats.total().docs().count();
		}
		return 0;
	}

	public long getTotalSize() {
		if (indicesStats != null) {
			return indicesStats.total().store().sizeInBytes();
		}
		return 0;
	}

}
