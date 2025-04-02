package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.awt.Point;
import java.io.IOException;
import java.util.Date;

import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public class IndexView extends ViewBase {

	private ClusterDataManager manager;

	public IndexView(ClusterDataManager manager) {
		this.manager = manager;
	}

	@Override
	public void draw(Screen screen, Point offset) throws IOException {
		Date now = new Date();
		clusterColor = manager.getClusterColor();
		screen.newTextGraphics().setBackgroundColor(clusterColor).setForegroundColor(black).putCSIStyledString(width - 30, 0, now.toString());

		screen.refresh();
	}

	@Override
	public void fullDraw(Screen screen, Point offset) throws IOException {
		String footer = "Quit: Q  Overview: O  Index View: I  Node View: N";
		drawHeaderAndFooter(screen, offset, manager, "Index View", footer);
		draw(screen, offset);
	}

}
