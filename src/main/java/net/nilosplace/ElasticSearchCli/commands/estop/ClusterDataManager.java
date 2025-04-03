package net.nilosplace.ElasticSearchCli.commands.estop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

import co.elastic.clients.elasticsearch._types.HealthStatus;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import co.elastic.clients.elasticsearch.indices.IndicesStatsResponse;
import co.elastic.clients.elasticsearch.nodes.NodesStatsResponse;
import co.elastic.clients.elasticsearch.nodes.Stats;
import lombok.Data;
import net.nilosplace.ElasticSearchCli.commands.estop.model.NodeInfo;

@Data
public class ClusterDataManager {
	private IndicesStatsResponse indicesStatsResp;
	private NodesStatsResponse nodesStatsResp;
	private HealthResponse healthResp;

	private List<NodeInfo> nodeInfos = new ArrayList<>();

	public void setNodesStatsResp(NodesStatsResponse nodesStatsResp) {
		this.nodesStatsResp = nodesStatsResp;
		nodeInfos.clear();
		for (Entry<String, Stats> entry : nodesStatsResp.nodes().entrySet()) {
			NodeInfo info = new NodeInfo();
			Stats stats = entry.getValue();
			info.setName(stats.name());
			// info.setVersion(stats.v);
			info.setIp(stats.host());
			info.setHeap(stats.jvm().mem().heapUsedPercent() + "% = " + formatBytes(stats.jvm().mem().heapUsedInBytes()) + "/" + formatBytes(stats.jvm().mem().heapMaxInBytes()));
			info.setDisk((int) (100 * stats.fs().total().availableInBytes() / stats.fs().total().totalInBytes()) + "% = " + formatBytes(stats.fs().total().availableInBytes()) + "/" + formatBytes(stats.fs().total().totalInBytes()));
			info.setCpuPercent(stats.os().cpu().percent());
			info.setLoadAverage(stats.os().cpu().loadAverage());
			String uptime = DurationFormatUtils.formatDuration(stats.jvm().uptimeInMillis(), "dd:HH:mm:ss", true);
			info.setUptime(uptime);

			nodeInfos.add(info);
		}
	}

	public String getClusterName() {
		if (healthResp != null) {
			return healthResp.clusterName();
		}
		return "Cluster Unknown";
	}

	public TextColor getClusterColor() {
		if (healthResp != null) {
			if (healthResp.status() == HealthStatus.Green) {
				return ANSI.GREEN_BRIGHT;
			}
			if (healthResp.status() == HealthStatus.Yellow) {
				return ANSI.YELLOW_BRIGHT;
			}
			if (healthResp.status() == HealthStatus.Red) {
				return ANSI.RED_BRIGHT;
			}
		}
		return ANSI.GREEN_BRIGHT;
	}

	public int getNodeTotal() {
		if (healthResp != null) {
			return healthResp.numberOfNodes();
		}
		return 0;
	}

	public int getIndicesTotal() {
		if (indicesStatsResp != null) {
			return indicesStatsResp.indices().size();
		}
		return 0;
	}

	public int getShardsTotal() {
		if (healthResp != null) {
			return healthResp.activeShards();
		}
		return 0;
	}

	public long getCountTotal() {
		if (indicesStatsResp != null) {
			return indicesStatsResp.all().total().docs().count();
		}
		return 0;
	}

	public long getTotalSize() {
		if (indicesStatsResp != null) {
			return indicesStatsResp.all().total().store().sizeInBytes();
		}
		return 0;
	}

	public static String formatBytes(long bytes) {
		if (bytes < 1024) {
			return bytes + "B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(1024));
		char unit = "KMGTPE".charAt(exp - 1);
		return String.format("%d%c", (long) (bytes / Math.pow(1024, exp)), unit);
	}

}
