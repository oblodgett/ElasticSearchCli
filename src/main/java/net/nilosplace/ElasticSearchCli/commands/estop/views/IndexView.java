package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.awt.Point;
import java.io.IOException;
import java.util.Date;

import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public class IndexView extends ViewBase {

	public IndexView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Index View";
		footer = "Quit: Q  Overview: O  Index View: I  Node View: N";
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		screen.refresh();
	}

}
