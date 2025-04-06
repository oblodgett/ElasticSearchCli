package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;

import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;
import net.nilosplace.ElasticSearchCli.commands.estop.model.NodeInfo;

public class NodeView extends ViewBase {

	public NodeView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Node View";
		footer = "Overview: O  Index View: I  Node View: N  Tasks View: T";
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		int colCount = 10;
		int[] columns = new int[colCount];
		String[][] values = new String[colCount][manager.getNodeTotal() + 1];

		values[0][0] = "M";
		values[1][0] = "Name";
		// values[2][0] = "Version";
		values[2][0] = "IP";
		values[3][0] = "CPU%";
		values[4][0] = "1m";
		values[5][0] = "5m";
		values[6][0] = "15m";
		values[7][0] = "Heap Usage";
		values[8][0] = "Disk Usage";
		values[9][0] = "Uptime";

		for (int i = 0; i < colCount; i++) {
			columns[i] = values[i][0].length();
		}

		int c = 0;
		for (NodeInfo nodeInfo : manager.getNodeInfos()) {
			c++;
			values[0][c] = nodeInfo.isMaster() ? "*" : "-";
			values[1][c] = nodeInfo.getName();
			// values[2][c] = nodeInfo.getVersion();
			values[2][c] = nodeInfo.getIp();
			values[3][c] = nodeInfo.getCpuPercent() + "%";
			values[4][c] = nodeInfo.getLoadAverage().get("1m") + "";
			values[5][c] = nodeInfo.getLoadAverage().get("5m") + "";
			values[6][c] = nodeInfo.getLoadAverage().get("15m") + "";
			values[7][c] = nodeInfo.getHeap();
			values[8][c] = nodeInfo.getDisk();
			values[9][c] = nodeInfo.getUptime();

			for (int i = 0; i < colCount; i++) {
				if (values[i][c].length() > columns[i]) {
					columns[i] = values[i][c].length();
				}
			}
		}

		for (int i = 0; i <= c; i++) {
			int colStart = 0;
			clearLine(i * 2);
			for (int k = 0; k < columns.length; k++) {
				printText(colStart, i * 2, values[k][i]);
				colStart += (columns[k] + 2);
			}
		}

		screen.refresh();
	}

}
