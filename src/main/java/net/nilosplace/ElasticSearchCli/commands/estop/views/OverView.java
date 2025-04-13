package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import co.elastic.clients.elasticsearch.cat.shards.ShardsRecord;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;
import net.nilosplace.ElasticSearchCli.commands.estop.model.IndexInfo;
import net.nilosplace.ElasticSearchCli.commands.estop.model.NodeInfo;

public class OverView extends ViewBase {

	private boolean indexesThanNodes = true;

	public OverView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Cluster Overview";
		setFooter();
	}

	public void setFooter() {
		if (indexesThanNodes) {
			footer = "Nodes/Indexes: H";
		} else {
			footer = "Indexes/Nodes: H";
		}
	}

	public void toggleIndexesNodes() {
		indexesThanNodes = !indexesThanNodes;
		setFooter();
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		List<NodeInfo> nodes = manager.getNodeInfos();
		List<IndexInfo> indexes = manager.getIndexInfos();
		// Nodes by Indexes
		Map<String, Map<String, List<ShardsRecord>>> shardMap = manager.getShardMap();

		if (shardMap == null) {
			return;
		}

		int rowBuffer = 1;
		int columnBuffer = 3;
		
		int[] columns;
		int[] rows;

		if (indexesThanNodes) {
			columns = new int[nodes.size() + 1];
			rows = new int[indexes.size() + 1];

			// Pass 1 calc sizes
			int c = 1;
			for (NodeInfo nodeInfo : nodes) {
				int r = 1;
				for (IndexInfo indexInfo : indexes) {
					if(indexInfo.getName().length() > columns[0]) {
						columns[0] = indexInfo.getName().length();
					}
					List<ShardsRecord> shardList = shardMap.get(nodeInfo.getName()).get(indexInfo.getName());
					if (shardList != null) {
						int rowCount = 0;
						for (int i = 0; i < shardList.size(); i += 5) {
							String text = "";
							for (ShardsRecord shardsRecord :shardList.subList(i, Math.min(i + 5, shardList.size()))) {
								text += "[" + shardsRecord.shard() + "]";
							}
							if (text.length() > columns[c]) {
								columns[c] = text.length();
							}
							rowCount++;
				        }
						if(rowCount > rows[r]) {
							rows[r] = rowCount;
						}
					}
					r++;
				}
				if(1 > rows[0]) {
					rows[0] = 1;
				}
				if (nodeInfo.getName().length() > columns[c]) {
					columns[c] = nodeInfo.getName().length();
				}
				c++;
			}
				
			int colStart = columns[0] + columnBuffer;
			int rowStart = rows[0] + rowBuffer;
			c = 1;
			for (NodeInfo nodeInfo : nodes) {
				int r = 1;
				rowStart = rows[0] + rowBuffer;
				printText(colStart, 0, nodeInfo.getName(), ANSI.WHITE);
				for (IndexInfo indexInfo : indexes) {
					if(c == 1) printText(0, rowStart, indexInfo.getName(), ANSI.WHITE);
					
					List<ShardsRecord> shardList = shardMap.get(nodeInfo.getName()).get(indexInfo.getName());
					if (shardList != null) {
						int rl = 0;
						for (int i = 0; i < shardList.size(); i += 5) {
							String text = "";
							int cl = 0;
							for (ShardsRecord shardsRecord: shardList.subList(i, Math.min(i + 5, shardList.size()))) {
								text = "[" + shardsRecord.shard() + "]";
								printText(colStart + cl, rowStart + rl, text, ANSI.WHITE);
								cl += text.length();
							}
							rl++;
				        }
					}
					rowStart += (rows[r] + rowBuffer);
					r++;
				}
				colStart += (columns[c] + columnBuffer);
				c++;
			}

		} else {
			
			columns = new int[indexes.size() + 1];
			rows = new int[nodes.size() + 1];

			// Pass 1 calc sizes
			int c = 1;
			for (IndexInfo indexInfo : indexes) {
				int r = 1;
				for (NodeInfo nodeInfo : nodes) {
					if(nodeInfo.getName().length() > columns[0]) {
						columns[0] = nodeInfo.getName().length();
					}
					List<ShardsRecord> shardList = shardMap.get(nodeInfo.getName()).get(indexInfo.getName());
					if (shardList != null) {
						int rowCount = 0;
						for (int i = 0; i < shardList.size(); i += 5) {
							String text = "";
							for (ShardsRecord shardsRecord :shardList.subList(i, Math.min(i + 5, shardList.size()))) {
								text += "[" + shardsRecord.shard() + "]";
							}
							if (text.length() > columns[c]) {
								columns[c] = text.length();
							}
							rowCount++;
				        }
						if(rowCount > rows[r]) {
							rows[r] = rowCount;
						}
					}
					r++;
				}
				if(1 > rows[0]) {
					rows[0] = 1;
				}
				if (indexInfo.getName().length() > columns[c]) {
					columns[c] = indexInfo.getName().length();
				}
				c++;
			}

			int colStart = columns[0] + columnBuffer;
			int rowStart = rows[0] + rowBuffer;
			c = 1;
			for (IndexInfo indexInfo : indexes) {
				int r = 1;
				rowStart = rows[0] + rowBuffer;
				printText(colStart, 0, indexInfo.getName(), ANSI.WHITE);
				for (NodeInfo nodeInfo : nodes) {
					if(c == 1) printText(0, rowStart, nodeInfo.getName(), ANSI.WHITE);
					
					List<ShardsRecord> shardList = shardMap.get(nodeInfo.getName()).get(indexInfo.getName());
					if (shardList != null) {
						int rl = 0;
						for (int i = 0; i < shardList.size(); i += 5) {
							String text = "";
							int cl = 0;
							for (ShardsRecord shardsRecord: shardList.subList(i, Math.min(i + 5, shardList.size()))) {
								text = "[" + shardsRecord.shard() + "]";
								printText(colStart + cl, rowStart + rl, text, ANSI.WHITE);
								cl += text.length();
							}
							rl++;
				        }
					}
					rowStart += (rows[r] + rowBuffer);
					r++;
				}
				colStart += (columns[c] + columnBuffer);
				c++;
			}
		}

		screen.refresh();
	}

}
