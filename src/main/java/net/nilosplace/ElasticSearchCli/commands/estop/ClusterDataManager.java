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

import co.elastic.clients.elasticsearch._types.HealthStatus;
import co.elastic.clients.elasticsearch.cat.MasterResponse;
import co.elastic.clients.elasticsearch.cat.ShardsResponse;
import co.elastic.clients.elasticsearch.cat.TasksResponse;
import co.elastic.clients.elasticsearch.cat.shards.ShardsRecord;
import co.elastic.clients.elasticsearch.cat.tasks.TasksRecord;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import co.elastic.clients.elasticsearch.indices.IndicesStatsResponse;
import co.elastic.clients.elasticsearch.indices.stats.IndicesStats;
import co.elastic.clients.elasticsearch.nodes.NodesStatsResponse;
import co.elastic.clients.elasticsearch.nodes.Stats;
import lombok.Data;
import net.nilosplace.ElasticSearchCli.commands.estop.model.IndexInfo;
import net.nilosplace.ElasticSearchCli.commands.estop.model.NodeInfo;
import net.nilosplace.ElasticSearchCli.commands.estop.model.Tree;
import net.nilosplace.ElasticSearchCli.commands.estop.model.Tree.Node;

@Data
public class ClusterDataManager {
	private IndicesStatsResponse indicesStatsResp;
	private NodesStatsResponse nodesStatsResp;
	private HealthResponse healthResp;
	private MasterResponse masterResp;
	private ShardsResponse shardsResp;
	private TasksResponse tasksResp;

	private List<NodeInfo> nodeInfos = new ArrayList<>();
	private List<IndexInfo> indexInfos = new ArrayList<>();
	private Tree<TasksRecord> taskTree = new Tree<>(null);
	private Map<String, Map<String, List<ShardsRecord>>> shardMap;

	public void setShardsResp(ShardsResponse shardsResp) {
		this.shardsResp = shardsResp;
		List<ShardsRecord> shardList = shardsResp.valueBody();
		shardMap = new HashMap<>();

		for (NodeInfo nodeInfo : nodeInfos) {
			if (!shardMap.containsKey(nodeInfo.getName())) {
				shardMap.put(nodeInfo.getName(), new HashMap<>());
			}
			Map<String, List<ShardsRecord>> nodeMap = shardMap.get(nodeInfo.getName());
			for (IndexInfo indexInfo : indexInfos) {
				if (!nodeMap.containsKey(indexInfo.getName())) {
					nodeMap.put(indexInfo.getName(), new ArrayList<>());
				}
			}
		}

		for (ShardsRecord shardRecord : shardList) {
			String nodeName = "";
			if(shardRecord.state().equals("STARTED")) {
				nodeName = shardRecord.node();
				
				List<ShardsRecord> localShardList = shardMap.get(nodeName).get(shardRecord.index());
				if (localShardList != null) {
					localShardList.add(shardRecord);
				}
				
			} else if(shardRecord.state().equals("RELOCATING")) {
				String[] array = shardRecord.node().split(" ");
				// "node": "agr.stage.elasticsearch.server03 -> 172.31.30.2 JIsFAqL8QoiDhoY9CX1klw agr.stage.elasticsearch.server02"
				nodeName = array[0];
				
				List<ShardsRecord> localShardList = shardMap.get(nodeName).get(shardRecord.index());
				if (localShardList != null) {
					localShardList.add(shardRecord);
				}
				
				nodeName = array[4];
				
				localShardList = shardMap.get(nodeName).get(shardRecord.index());
				if (localShardList != null) {
					localShardList.add(shardRecord);
				}
			}
			
			//INITIALIZING: The shard is recovering from a peer shard or gateway.
			//RELOCATING: The shard is relocating.
			//STARTED: The shard has started.
			//UNASSIGNED
			
		}
	}

	public void setTasksResp(TasksResponse tasksResp) {
		this.tasksResp = tasksResp;

		List<TasksRecord> taskList = tasksResp.valueBody();
		Map<String, Node<TasksRecord>> taskMap = new HashMap<>();
		taskTree = new Tree<>(null);

		for (TasksRecord record : taskList) {
			Node<TasksRecord> node = new Node<>(record);
			taskMap.put(record.taskId(), node);
		}
		for (TasksRecord record : taskList) {
			Node<TasksRecord> parent = taskMap.get(record.parentTaskId());
			Node<TasksRecord> node = taskMap.get(record.taskId());
			if (parent == null) {
				parent = taskTree.getRoot();
			}
			parent.addChild(node);
		}
	}

	public void setNodesStatsResp(NodesStatsResponse nodesStatsResp) {
		this.nodesStatsResp = nodesStatsResp;
		nodeInfos.clear();
		for (Entry<String, Stats> entry : nodesStatsResp.nodes().entrySet()) {
			NodeInfo info = new NodeInfo();
			Stats stats = entry.getValue();
			info.setId(entry.getKey());
			info.setName(stats.name());
			if (masterResp != null) {
				if (masterResp.valueBody().get(0).node().equals(stats.name())) {
					info.setMaster(true);
				}
			}
			// info.setVersion(stats.v);
			info.setIp(stats.host());
			info.setHeap(stats.jvm().mem().heapUsedPercent() + "% = " + formatBytes(stats.jvm().mem().heapUsedInBytes()) + "/" + formatBytes(stats.jvm().mem().heapMaxInBytes()));
			info.setDisk((int) (100.0 * (1.0 - ((double) stats.fs().total().availableInBytes() / (double) stats.fs().total().totalInBytes()))) + "% = " + formatBytes(stats.fs().total().availableInBytes()) + "/" + formatBytes(stats.fs().total().totalInBytes()));
			info.setCpuPercent(stats.os().cpu().percent());
			info.setLoadAverage(stats.os().cpu().loadAverage());
			String uptime = DurationFormatUtils.formatDuration(stats.jvm().uptimeInMillis(), "dd:HH:mm:ss", true);
			info.setUptime(uptime);

			nodeInfos.add(info);
		}
		nodeInfos.sort(Comparator.comparing(NodeInfo::getName));
	}

	public void setIndicesStatsResp(IndicesStatsResponse indicesStatsResp) {
		this.indicesStatsResp = indicesStatsResp;
		indexInfos.clear();
		for (Entry<String, IndicesStats> entry : indicesStatsResp.indices().entrySet()) {
			IndexInfo info = new IndexInfo();
			IndicesStats stats = entry.getValue();
			info.setId(stats.uuid());
			info.setName(entry.getKey());
			info.setSize((long)stats.total().store().sizeInBytes());
			info.setDocCount(stats.total().docs().count());
			info.setPrimaryShardCount(stats.primaries().shardStats().totalCount());
			info.setTotalShardCount(stats.total().shardStats().totalCount());
			info.setTotalSegmentCount(stats.total().segments().count());
			indexInfos.add(info);
		}
		indexInfos.sort(Comparator.comparing(IndexInfo::getName));
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
