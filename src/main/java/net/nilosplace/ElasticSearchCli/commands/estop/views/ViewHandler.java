package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;
import java.util.Date;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import java.awt.Point;

import lombok.Data;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataPoller;

@Data
public class ViewHandler extends Thread {

	private Screen screen;
	private ClusterDataManager manager = new ClusterDataManager();
	private ClusterDataPoller poller = new ClusterDataPoller(manager, 10);

	private NodeView nodeView = new NodeView(manager);
	private IndexView indexView = new IndexView(manager);
	private OverView overView = new OverView(manager);
	private ViewBase currentView;
	private Point offset = new Point();

	public ViewHandler(Screen screen) {
		this.screen = screen;
	}

	public void show() {
		poller.start();

		try {
			screen.startScreen();
			currentView = overView;
			currentView.fullDraw(screen, offset);
			Date lastDraw = null;
			while (true) {
				if (handleKeyStroke()) {
					break;
				}
				TerminalSize newSize = screen.doResizeIfNecessary();
				if (newSize != null) {
					currentView.fullDraw(screen, offset);
				}
				Thread.sleep(20);
				Date now = new Date();
				if (lastDraw == null || now.getTime() - lastDraw.getTime() > 2000) {
					currentView.draw(screen, offset);
					lastDraw = now;
				}
			}
			screen.stopScreen();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean handleKeyStroke() throws IOException {
		KeyStroke keyStroke = screen.pollInput();
		if (keyStroke != null) {
			switch (keyStroke.getKeyType()) {
				case KeyType.Character -> {
					switch (keyStroke.getCharacter()) {
						case 'Q', 'q' -> {
							return true;
						}
						case 'O', 'o' -> {
							currentView = overView;
							currentView.fullDraw(screen, offset);
						}
						case 'N', 'n' -> {
							currentView = nodeView;
							currentView.fullDraw(screen, offset);
						}
						case 'I', 'i' -> {
							currentView = indexView;
							currentView.fullDraw(screen, offset);
						}
						case 'S', 's' -> {
							overView.toggleIndexesNodes();
							currentView.fullDraw(screen, offset);
						}
						case 'R', 'r' -> {
							currentView.fullDraw(screen, offset);
						}
						default -> {
							System.out.println(keyStroke);
						}
					}
				}
				case KeyType.ArrowUp -> {
					if (offset.getY() > 0) {
						offset.setLocation(offset.getX(), offset.getY() - 1);
					}
				}
				case KeyType.ArrowDown -> {
					offset.setLocation(offset.getX(), offset.getY() + 1);
				}
				case KeyType.ArrowLeft -> {
					if (offset.getX() > 0) {
						offset.setLocation(offset.getX() - 1, offset.getY());
					}
				}
				case KeyType.ArrowRight -> {
					offset.setLocation(offset.getX() + 1, offset.getY());
				}
				default -> {
					System.out.println(keyStroke);
				}
			}
		}
		return false;
	}

}
