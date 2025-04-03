package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.awt.Point;
import java.io.IOException;
import java.util.Date;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public abstract class ViewBase {
	public abstract void draw(boolean clear) throws IOException;

	protected int width;
	protected int height;
	protected TextColor black = ANSI.BLACK;
	protected TextColor clusterColor = ANSI.GREEN_BRIGHT;
	protected TextCharacter space = new TextCharacter(' ');
	protected Point win = new Point(3, 4);
	protected Point offset = new Point(0, 0);
	protected Screen screen;
	protected ClusterDataManager manager;
	protected String header = "";
	protected String footer = "";

	public ViewBase(Screen screen, ClusterDataManager manager) {
		this.screen = screen;
		this.manager = manager;
	}

	protected void drawHeaderAndFooter(String errorMessage) throws IOException {
		width = screen.getTerminalSize().getColumns() - 1;
		height = screen.getTerminalSize().getRows() - 1;

		screen.setCursorPosition(null);
		TextColor clusterColor = manager.getClusterColor();

		screen.newTextGraphics().drawLine(0, 0, width, 0, space.withBackgroundColor(clusterColor).withForegroundColor(black));
		screen.newTextGraphics().drawLine(0, height, width, height, space.withBackgroundColor(clusterColor).withForegroundColor(black));
		screen.newTextGraphics().setBackgroundColor(clusterColor).setForegroundColor(black).putCSIStyledString(2, 0, header + ": " + manager.getClusterName());
		screen.newTextGraphics().setBackgroundColor(clusterColor).setForegroundColor(black).putCSIStyledString((width / 2) - (offset.toString().length() / 2), 0, offset.toString());
		screen.newTextGraphics().setBackgroundColor(clusterColor).setForegroundColor(black).putCSIStyledString(2, height, footer);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(0, 1, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
		screen.newTextGraphics().drawLine(1, 1, width - 1, 1, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL).withForegroundColor(clusterColor));
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(width, 1, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(0, 2, Symbols.DOUBLE_LINE_VERTICAL);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(width, 2, Symbols.DOUBLE_LINE_VERTICAL);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(0, 2, Symbols.DOUBLE_LINE_VERTICAL);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(width, 2, Symbols.DOUBLE_LINE_VERTICAL);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(0, 3, Symbols.DOUBLE_LINE_T_RIGHT);
		screen.newTextGraphics().drawLine(1, 3, width - 1, 3, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL).withForegroundColor(clusterColor));
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(width, 3, Symbols.DOUBLE_LINE_T_LEFT);
		screen.newTextGraphics().drawLine(0, 4, 0, height - 3, new TextCharacter(Symbols.DOUBLE_LINE_VERTICAL).withForegroundColor(clusterColor));
		screen.newTextGraphics().drawLine(width, 4, width, height - 3, new TextCharacter(Symbols.DOUBLE_LINE_VERTICAL).withForegroundColor(clusterColor));
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(0, height - 2, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
		screen.newTextGraphics().drawLine(1, height - 2, width - 1, height - 2, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL).withForegroundColor(clusterColor));
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(width, height - 2, Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);
		if (errorMessage != null) {
			screen.newTextGraphics().putString(2, height - 1, "Error: " + errorMessage);
		}

		int count = 2;
		count = printBox("Cluster: " + manager.getClusterName(), count);
		count = printBox("Nodes: " + manager.getNodeTotal(), count);
		count = printBox("Indices: " + manager.getIndicesTotal(), count);
		count = printBox("Shards: " + manager.getShardsTotal(), count);
		count = printBox("Docs: " + manager.getCountTotal(), count);
		count = printBox("Total Size: " + manager.getTotalSize(), count);

		Date now = new Date();
		screen.newTextGraphics().setBackgroundColor(clusterColor).setForegroundColor(black).putCSIStyledString(width - 30, 0, now.toString());

		screen.refresh();
	}

	private int printBox(String data, int count) {
		screen.newTextGraphics().setForegroundColor(ANSI.WHITE).putString(count, 2, data);
		count += data.length();
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(count + 1, 1, Symbols.DOUBLE_LINE_T_DOWN);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(count + 1, 2, Symbols.DOUBLE_LINE_VERTICAL);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(count + 1, 3, Symbols.DOUBLE_LINE_T_UP);
		count += 3;
		return count;
	}

	public void printText(int x, int y, String text) {
		screen.newTextGraphics().setForegroundColor(ANSI.WHITE).putString(x + win.x + offset.x, y + win.y + offset.y, text);
	}

	protected void updateOffset(int x, int y) {
		offset.setLocation(offset.getX() + x, offset.getY() + y);
	}

}
