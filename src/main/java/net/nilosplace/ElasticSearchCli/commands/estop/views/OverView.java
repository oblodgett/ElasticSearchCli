package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.awt.Point;
import java.io.IOException;
import java.util.Date;

import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public class OverView extends ViewBase {

	private boolean indexesThanNodes;

	public OverView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Cluster Overview";
		setFooter();
	}

	public void setFooter() {
		footer = "Quit: Q  Overview: O  Index View: I  Node View: N  ";
		if (indexesThanNodes) {
			footer += "Switch Indexes/Nodes: S";
		} else {
			footer += "Switch Nodes/Indexes: S";
		}
		footer += "  Refresh: R";
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

		screen.refresh();
	}

}
