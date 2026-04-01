package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;
import java.util.List;

import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.elastic.model.ShardInfo;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public class ShardView extends ViewBase {

	public ShardView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Shard View";
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		List<ShardInfo> shards = manager.getShardList();
		if (shards == null || shards.isEmpty()) {
			printText(0, 0, "No shard data available", ANSI.WHITE);
			screen.refresh();
			return;
		}

		int colCount = 7;
		int[] columns = new int[colCount];
		String[][] values = new String[colCount][shards.size() + 1];

		values[0][0] = "Index";
		values[1][0] = "Shard";
		values[2][0] = "P/R";
		values[3][0] = "State";
		values[4][0] = "Docs";
		values[5][0] = "Store";
		values[6][0] = "Node";

		for (int i = 0; i < colCount; i++) {
			columns[i] = values[i][0].length();
		}

		int c = 0;
		for (ShardInfo shard : shards) {
			c++;
			values[0][c] = shard.getIndex() != null ? shard.getIndex() : "";
			values[1][c] = shard.getShard() != null ? shard.getShard() : "";
			values[2][c] = shard.getPrirep() != null ? shard.getPrirep() : "";
			values[3][c] = shard.getState() != null ? shard.getState() : "";
			values[4][c] = shard.getDocs() != null ? shard.getDocs() : "";
			values[5][c] = shard.getStore() != null ? shard.getStore() : "";
			values[6][c] = shard.getNode() != null ? shard.getNode() : "";

			for (int i = 0; i < colCount; i++) {
				if (values[i][c].length() > columns[i]) {
					columns[i] = values[i][c].length();
				}
			}
		}

		for (int i = 0; i <= c; i++) {
			int colStart = 0;
			clearLine(i);
			for (int k = 0; k < columns.length; k++) {
				ANSI color = ANSI.WHITE;
				if (i > 0 && k == 3) {
					String state = values[k][i];
					if ("STARTED".equals(state)) color = ANSI.GREEN;
					else if ("RELOCATING".equals(state)) color = ANSI.YELLOW;
					else if ("INITIALIZING".equals(state)) color = ANSI.CYAN;
					else if ("UNASSIGNED".equals(state)) color = ANSI.RED;
				}
				printText(colStart, i, values[k][i], color);
				colStart += (columns[k] + 2);
			}
		}

		screen.refresh();
	}
}
