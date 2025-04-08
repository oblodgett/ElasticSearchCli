package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;

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
		footer = "Overview: O  Index View: I  Node View: N  Tasks View: T  ";
		if (indexesThanNodes) {
			footer += "Switch Indexes/Nodes: S";
		} else {
			footer += "Switch Nodes/Indexes: S";
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

		screen.refresh();
	}

}
