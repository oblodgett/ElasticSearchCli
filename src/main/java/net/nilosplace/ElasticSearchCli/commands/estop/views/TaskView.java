package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;

import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public class TaskView extends ViewBase {

	public TaskView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Tasks View";
		footer = "Overview: O  Index View: I  Node View: N  Tasks View: T";
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		printText(0, 0, "Tasks");

		screen.refresh();
	}

}
