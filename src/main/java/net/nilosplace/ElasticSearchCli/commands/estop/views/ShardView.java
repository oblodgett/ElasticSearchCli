package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;

import com.googlecode.lanterna.screen.Screen;

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

		screen.refresh();
	}
}
