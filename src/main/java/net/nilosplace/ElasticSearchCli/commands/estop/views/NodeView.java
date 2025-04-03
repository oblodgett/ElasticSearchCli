package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;

import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import co.elastic.clients.elasticsearch.cat.nodes.NodesRecord;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public class NodeView extends ViewBase {

	public NodeView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Node View";
		footer = "Quit: Q  Overview: O  Index View: I  Node View: N  Refresh: R";
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		int[] columns = new int[5];
		String[][] values = new String[5][manager.getNodeRecords().size() + 1];

		values[0][0] = "M";
		columns[0] = values[0][0].length();
		values[1][0] = "Name";
		columns[1] = values[1][0].length();
		values[2][0] = "IP";
		columns[2] = values[2][0].length();
		values[3][0] = "Heap Usage";
		columns[3] = values[3][0].length();
		values[4][0] = "Disk Usage";
		columns[4] = values[4][0].length();

		int c = 0;
		for (NodesRecord nodesRecord : manager.getNodeRecords()) {
			c++;
			if (nodesRecord.master().length() > columns[0]) {
				columns[0] = nodesRecord.master().length();
			}
			values[0][c] = nodesRecord.master();

			if (nodesRecord.name().length() > columns[1]) {
				columns[1] = nodesRecord.name().length();
			}
			values[1][c] = nodesRecord.name();

			if (nodesRecord.ip().length() > columns[2]) {
				columns[2] = nodesRecord.ip().length();
			}
			values[2][c] = nodesRecord.ip();

			String heap = nodesRecord.heapCurrent() + " / " + nodesRecord.heapMax() + " = " + nodesRecord.heapPercent() + "%";
			if (heap.length() > columns[3]) {
				columns[3] = heap.length();
			}
			values[3][c] = heap;

			String disk = nodesRecord.diskAvail() + " / " + nodesRecord.diskTotal() + " = " + nodesRecord.diskUsedPercent() + "%";
			if (disk.length() > columns[4]) {
				columns[4] = disk.length();
			}
			values[4][c] = disk;
		}

		for (int i = 0; i <= c; i++) {
			int colStart = 0;
			for (int k = 0; k < columns.length; k++) {
				printText(colStart, i * 2, values[k][i]);
				colStart += (columns[k] + 2);
			}
		}

		/***
		 * { "heap.percent": "17", "ram.percent": "65", "cpu": "0", "load_1m": "0.06",
		 * "load_5m": "0.03", "load_15m": "0.00", "node.role": "cdfhilmrstw", }
		 */
		screen.refresh();
	}

}
