package net.nilosplace.ElasticSearchCli.commands.estop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import net.nilosplace.ElasticSearchCli.elastic.model.*;
import net.nilosplace.ElasticSearchCli.commands.estop.model.IndexInfo;
import net.nilosplace.ElasticSearchCli.commands.estop.model.NodeInfo;
import net.nilosplace.ElasticSearchCli.commands.estop.model.Tree;
import net.nilosplace.ElasticSearchCli.commands.estop.model.Tree.Node;

@Data
public class ClusterDataManager {
	private static final Logger log = LoggerFactory.getLogger(ClusterDataManager.class);
	private IndicesStatsInfo indicesStatsInfo;
	private NodesStatsInfo nodesStatsInfo;
	private HealthInfo healthInfo;
	private List<MasterInfo> masterInfo;
	private List<ShardInfo> shardList;
	private List<TaskInfo> taskList;

	private List<NodeInfo> nodeInfos = new ArrayList<>();
	private List<IndexInfo> indexInfos = new ArrayList<>();
	private Tree<TaskInfo> taskTree = new Tree<>(null);
	private Map<String, Map<String, List<ShardInfo>>> shardMap;

	public void setShardList(List<ShardInfo> shardList) {
		this.shardList = shardList;
		log.debug("Received {} shards, {} nodes, {} indexes for shard map", shardList.size(), nodeInfos.size(), indexInfos.size());
		shardMap = new HashMap<>();

		for (NodeInfo nodeInfo : nodeInfos) {
			if (!shardMap.containsKey(nodeInfo.getName())) {
				shardMap.put(nodeInfo.getName(), new HashMap<>());
			}
			Map<String, List<ShardInfo>> nodeMap = shardMap.get(nodeInfo.getName());
			for (IndexInfo indexInfo : indexInfos) {
				if (!nodeMap.containsKey(indexInfo.getName())) {
					nodeMap.put(indexInfo.getName(), new ArrayList<>());
				}
			}
		}

		// Count shards per index from the actual shard list
		Map<String, long[]> shardCounts = new HashMap<>();
		for (ShardInfo shardInfo : shardList) {
			String idx = shardInfo.getIndex();
			if (idx != null) {
				shardCounts.computeIfAbsent(idx, k -> new long[2]);
				shardCounts.get(idx)[1]++; // total
				if ("p".equals(shardInfo.getPrirep())) {
					shardCounts.get(idx)[0]++; // primary
				}
			}
		}
		for (IndexInfo indexInfo : indexInfos) {
			long[] counts = shardCounts.get(indexInfo.getName());
			if (counts != null) {
				indexInfo.setPrimaryShardCount(counts[0]);
				indexInfo.setTotalShardCount(counts[1]);
			}
		}

		for (ShardInfo shardInfo : shardList) {
			String nodeName = "";
			if ("STARTED".equals(shardInfo.getState())) {
				nodeName = shardInfo.getNode();

				Map<String, List<ShardInfo>> nodeMap = shardMap.get(nodeName);
				if (nodeMap != null) {
					List<ShardInfo> localShardList = nodeMap.get(shardInfo.getIndex());
					if (localShardList != null) {
						localShardList.add(shardInfo);
					}
				}

			} else if ("RELOCATING".equals(shardInfo.getState())) {
				String[] array = shardInfo.getNode().split(" ");
				nodeName = array[0];

				Map<String, List<ShardInfo>> nodeMap = shardMap.get(nodeName);
				if (nodeMap != null) {
					List<ShardInfo> localShardList = nodeMap.get(shardInfo.getIndex());
					if (localShardList != null) {
						localShardList.add(shardInfo);
					}
				}

				nodeName = array[4];

				nodeMap = shardMap.get(nodeName);
				if (nodeMap != null) {
					List<ShardInfo> localShardList = nodeMap.get(shardInfo.getIndex());
					if (localShardList != null) {
						localShardList.add(shardInfo);
					}
				}
			}
		}
	}

	public void setTaskList(List<TaskInfo> taskList) {
		this.taskList = taskList;

		Map<String, Node<TaskInfo>> taskMap = new HashMap<>();
		taskTree = new Tree<>(null);

		for (TaskInfo record : taskList) {
			Node<TaskInfo> node = new Node<>(record);
			taskMap.put(record.getId(), node);
		}
		for (TaskInfo record : taskList) {
			Node<TaskInfo> parent = taskMap.get(record.getParentTaskId());
			Node<TaskInfo> node = taskMap.get(record.getId());
			if (parent == null) {
				parent = taskTree.getRoot();
			}
			parent.addChild(node);
		}
	}

	public void setNodesStatsInfo(NodesStatsInfo nodesStatsInfo) {
		this.nodesStatsInfo = nodesStatsInfo;
		nodeInfos.clear();
		for (Entry<String, NodeStats> entry : nodesStatsInfo.getNodes().entrySet()) {
			NodeInfo info = new NodeInfo();
			NodeStats stats = entry.getValue();
			info.setId(entry.getKey());
			info.setName(stats.getName());
			if (masterInfo != null && !masterInfo.isEmpty()) {
				if (masterInfo.get(0).getNode().equals(stats.getName())) {
					info.setMaster(true);
				}
			}
			info.setIp(stats.getHost());
			if (stats.getJvm() != null && stats.getJvm().getMem() != null) {
				info.setHeap(stats.getJvm().getMem().getHeapUsedPercent() + "% = " + formatBytes(stats.getJvm().getMem().getHeapUsedInBytes()) + "/" + formatBytes(stats.getJvm().getMem().getHeapMaxInBytes()));
			}
			if (stats.getFs() != null && stats.getFs().getTotal() != null && stats.getFs().getTotal().getTotalInBytes() > 0) {
				info.setDisk((int) (100.0 * (1.0 - ((double) stats.getFs().getTotal().getAvailableInBytes() / (double) stats.getFs().getTotal().getTotalInBytes()))) + "% = " + formatBytes(stats.getFs().getTotal().getAvailableInBytes()) + "/" + formatBytes(stats.getFs().getTotal().getTotalInBytes()));
			}
			if (stats.getOs() != null && stats.getOs().getCpu() != null) {
				info.setCpuPercent(stats.getOs().getCpu().getPercent());
				info.setLoadAverage(stats.getOs().getCpu().getLoadAverage());
			}
			if (stats.getJvm() != null) {
				String uptime = DurationFormatUtils.formatDuration(stats.getJvm().getUptimeInMillis(), "dd:HH:mm:ss", true);
				info.setUptime(uptime);
			}

			nodeInfos.add(info);
		}
		nodeInfos.sort(Comparator.comparing(NodeInfo::getName));
	}

	public void setIndicesStatsInfo(IndicesStatsInfo indicesStatsInfo) {
		this.indicesStatsInfo = indicesStatsInfo;
		indexInfos.clear();
		if (indicesStatsInfo.getIndices() == null) {
			log.warn("IndicesStatsInfo.indices is null");
			return;
		}
		log.debug("Received {} indices from /_stats", indicesStatsInfo.getIndices().size());
		for (Entry<String, IndexStats> entry : indicesStatsInfo.getIndices().entrySet()) {
			IndexInfo info = new IndexInfo();
			IndexStats stats = entry.getValue();
			info.setId(stats.getUuid());
			info.setName(entry.getKey());
			info.setSize(stats.getTotal() != null && stats.getTotal().getStore() != null ? stats.getTotal().getStore().getSizeInBytes() : 0L);
			info.setDocCount(stats.getTotal() != null && stats.getTotal().getDocs() != null ? stats.getTotal().getDocs().getCount() : 0L);
			info.setPrimaryShardCount(stats.getPrimaries() != null && stats.getPrimaries().getShardStats() != null ? stats.getPrimaries().getShardStats().getTotalCount() : 0L);
			info.setTotalShardCount(stats.getTotal() != null && stats.getTotal().getShardStats() != null ? stats.getTotal().getShardStats().getTotalCount() : 0L);
			info.setTotalSegmentCount(stats.getTotal() != null && stats.getTotal().getSegments() != null ? stats.getTotal().getSegments().getCount() : 0);
			indexInfos.add(info);
		}
		indexInfos.sort(Comparator.comparing(IndexInfo::getName));
	}

	public String getClusterName() {
		if (healthInfo != null) {
			return healthInfo.getClusterName();
		}
		return "Cluster Unknown";
	}

	public TextColor getClusterColor() {
		if (healthInfo != null) {
			String status = healthInfo.getStatus();
			if ("green".equalsIgnoreCase(status)) {
				return ANSI.GREEN_BRIGHT;
			}
			if ("yellow".equalsIgnoreCase(status)) {
				return ANSI.YELLOW_BRIGHT;
			}
			if ("red".equalsIgnoreCase(status)) {
				return ANSI.RED_BRIGHT;
			}
		}
		return ANSI.GREEN_BRIGHT;
	}

	public int getNodeTotal() {
		if (healthInfo != null) {
			return healthInfo.getNumberOfNodes();
		}
		return 0;
	}

	public int getIndicesTotal() {
		if (indicesStatsInfo != null) {
			return indicesStatsInfo.getIndices().size();
		}
		return 0;
	}

	public int getShardsTotal() {
		if (healthInfo != null) {
			return healthInfo.getActiveShards();
		}
		return 0;
	}

	public long getCountTotal() {
		if (indicesStatsInfo != null && indicesStatsInfo.getAll() != null && indicesStatsInfo.getAll().getTotal() != null && indicesStatsInfo.getAll().getTotal().getDocs() != null) {
			return indicesStatsInfo.getAll().getTotal().getDocs().getCount();
		}
		return 0;
	}

	public long getTotalSize() {
		if (indicesStatsInfo != null && indicesStatsInfo.getAll() != null && indicesStatsInfo.getAll().getTotal() != null && indicesStatsInfo.getAll().getTotal().getStore() != null) {
			return indicesStatsInfo.getAll().getTotal().getStore().getSizeInBytes();
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
