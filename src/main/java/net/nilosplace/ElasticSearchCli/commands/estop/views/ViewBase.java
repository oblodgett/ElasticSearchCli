package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.awt.Point;
import java.io.IOException;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;

public abstract class ViewBase {
	public abstract void draw(Screen screen, Point offset) throws IOException;

	public abstract void fullDraw(Screen screen, Point offset) throws IOException;

	protected int width;
	protected int height;
	protected TextColor black = ANSI.BLACK;
	protected TextColor clusterColor = ANSI.GREEN_BRIGHT;
	protected TextCharacter space = new TextCharacter(' ');

	protected void drawHeaderAndFooter(Screen screen, Point offset, ClusterDataManager manager, String header, String footer) throws IOException {
		width = screen.getTerminalSize().getColumns() - 1;
		height = screen.getTerminalSize().getRows() - 1;

		screen.setCursorPosition(null);
		TextColor clusterColor = manager.getClusterColor();

		screen.clear();

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

		int count = 2;
		count = printBox(screen, "Cluster: " + manager.getClusterName(), count);
		count = printBox(screen, "Nodes: " + manager.getNodeTotal(), count);
		count = printBox(screen, "Indices: " + manager.getIndicesTotal(), count);
		count = printBox(screen, "Shards: " + manager.getShardsTotal(), count);
		count = printBox(screen, "Docs: " + manager.getCountTotal(), count);
		count = printBox(screen, "Total Size: " + manager.getTotalSize(), count);

		screen.refresh();
	}

	private int printBox(Screen screen, String data, int count) {
		screen.newTextGraphics().setForegroundColor(clusterColor).putString(count, 2, data);
		count += data.length();
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(count + 1, 1, Symbols.DOUBLE_LINE_T_DOWN);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(count + 1, 2, Symbols.DOUBLE_LINE_VERTICAL);
		screen.newTextGraphics().setForegroundColor(clusterColor).setCharacter(count + 1, 3, Symbols.DOUBLE_LINE_T_UP);
		count += 3;
		return count;
	}

}
