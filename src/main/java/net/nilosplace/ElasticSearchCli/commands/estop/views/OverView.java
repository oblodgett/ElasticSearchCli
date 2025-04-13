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

	private boolean indexesThanNodes;

	public OverView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Cluster Overview";
		setFooter();
	}

	public void setFooter() {
		if (indexesThanNodes) {
			footer = "Indexes/Nodes: H";
		} else {
			footer = "Nodes/Indexes: H";
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
				
			int colStart = columns[0] + 2;
			int rowStart = rows[0] + 1;
			c = 1;
			for (NodeInfo nodeInfo : nodes) {
				int r = 1;
				rowStart = rows[0] + 1;
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
					rowStart += (rows[r] + 1);
					r++;
				}
				colStart += (columns[c] + 2);
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
				if (indexInfo.getName().length() > columns[c]) {
					columns[c] = indexInfo.getName().length();
				}
				c++;
			}

			int colStart = columns[0] + 2;
			int rowStart = rows[0] + 1;
			c = 1;
			for (IndexInfo indexInfo : indexes) {
				int r = 1;
				rowStart = rows[0] + 1;
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
					rowStart += (rows[r] + 1);
					r++;
				}
				colStart += (columns[c] + 2);
				c++;
			}
		}

//		for (int r = 0; r < rows.length; r++) {
//			int colStart = 0;
//			int rowStart = 0;
//			clearLine(r * 2);
//			for (int c = 0; c < columns.length; c++) {
//				System.out.println("R: " + r + " C: " + c + " V: " + values[c][r]);
//				printText(colStart, rowStart, values[c][r], ANSI.WHITE);
//				colStart += (columns[c] + 2);
//				rowStart += (rows[r] + 1);
//			}
//		}

		screen.refresh();
	}

}
