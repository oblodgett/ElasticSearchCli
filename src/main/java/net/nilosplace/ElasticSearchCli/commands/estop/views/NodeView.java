package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.awt.Point;
import java.io.IOException;
import java.util.Date;

import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import co.elastic.clients.elasticsearch.cat.nodes.NodesRecord;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public class NodeView extends ViewBase {

	private ClusterDataManager manager;

	public NodeView(ClusterDataManager manager) {
		this.manager = manager;
	}

	@Override
	public void draw(Screen screen, Point offset) throws IOException {
		Date now = new Date();
		clusterColor = manager.getClusterColor();
		screen.newTextGraphics().setBackgroundColor(clusterColor).setForegroundColor(black).putCSIStyledString(width - 30, 0, now.toString());

		Point win = new Point(3, 5);
		int col = 0;
		for (NodesRecord nodesRecord : manager.getNodeRecords()) {
			screen.newTextGraphics().setForegroundColor(ANSI.WHITE).putString(win.x + offset.x, col + win.y + offset.y, nodesRecord.master() + "  " + nodesRecord.name() + " (" + nodesRecord.ip() + ")");
			col += 2;
		}
		/***
		 * { "ip": "172.18.0.2", "heap.percent": "17", "ram.percent": "65", "cpu": "0",
		 * "load_1m": "0.06", "load_5m": "0.03", "load_15m": "0.00", "node.role":
		 * "cdfhilmrstw", "master": "*", "name": "es04" }
		 */
		screen.refresh();
	}

	@Override
	public void fullDraw(Screen screen, Point offset) throws IOException {
		String footer = "Quit: Q  Overview: O  Index View: I  Node View: N  Refresh: R";
		drawHeaderAndFooter(screen, offset, manager, "Node View", footer);
		draw(screen, offset);
	}

}
