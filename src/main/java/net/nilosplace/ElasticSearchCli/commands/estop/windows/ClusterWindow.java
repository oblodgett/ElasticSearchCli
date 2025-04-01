package net.nilosplace.ElasticSearchCli.commands.estop.windows;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;

public class ClusterWindow extends BasicWindow {

	public ClusterWindow() {
		super("My Cluster Window!");
		Panel horizontalPanel = new Panel();
		horizontalPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		Panel leftPanel = new Panel();
		Panel middlePanel = new Panel();
		Panel rightPanel = new Panel();

		horizontalPanel.addComponent(leftPanel);
		horizontalPanel.addComponent(middlePanel.withBorder(Borders.singleLineBevel("Panel Title")));
		horizontalPanel.addComponent(rightPanel.withBorder(Borders.doubleLineBevel()));

		// This ultimately links in the panels as the window content
		setComponent(horizontalPanel);
	}

}
