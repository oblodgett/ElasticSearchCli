package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataPoller;

@Data
@EqualsAndHashCode(callSuper = false)
public class ViewHandler extends Thread {

	private Screen screen;
	private ClusterDataManager manager = new ClusterDataManager();
	private ClusterDataPoller poller = new ClusterDataPoller(this, manager, 10);

	private NodeView nodeView;
	private IndexView indexView;
	private OverView overView;
	private ShardView shardView;
	private TaskView taskView;
	private ViewBase currentView;
	private String errorMessage;

	private Lock lock = new ReentrantLock();

	private boolean dataUpdated = false;
	private boolean screenResized = false;
	private boolean viewChanged = false;
	private boolean screenChanged = false;

	public ViewHandler(Screen screen) {
		this.screen = screen;
		nodeView = new NodeView(screen, manager);
		indexView = new IndexView(screen, manager);
		overView = new OverView(screen, manager);
		shardView = new ShardView(screen, manager);
		taskView = new TaskView(screen, manager);
	}

	public void show() {
		poller.start();

		try {
			screen.startScreen();
			screen.setCursorPosition(null);
			currentView = overView;
			currentView.draw(true);
			currentView.drawHeaderAndFooter(errorMessage);
			boolean timeToQuit = false;
			while (true) {
				lock.lock();
				timeToQuit = handleKeyStroke();
				if (timeToQuit) {
					lock.unlock();
					break;
				}
				screenResized = screen.doResizeIfNecessary() != null;
				try {
					if (dataUpdated || screenResized || screenChanged || viewChanged) {
						currentView.draw(dataUpdated || screenResized || screenChanged || viewChanged);
						currentView.drawHeaderAndFooter(errorMessage);
						dataUpdated = screenResized = screenChanged = viewChanged = false;
					}
				} finally {
					lock.unlock();
				}
				Thread.sleep(20);
			}
			screen.stopScreen();
			screen.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		poller.interrupt();
	}

	public void toggleDataUpdated() {
		lock.lock();
		try {
			dataUpdated = true;
		} finally {
			lock.unlock();
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
							viewChanged = true;
						}
						case 'N', 'n' -> {
							currentView = nodeView;
							viewChanged = true;
						}
						case 'I', 'i' -> {
							currentView = indexView;
							viewChanged = true;
						}
						case 'T', 't' -> {
							currentView = taskView;
							viewChanged = true;
						}
						case 'S', 's' -> {
							currentView = shardView;
							viewChanged = true;
						}
						case 'H', 'h' -> {
							currentView = overView;
							overView.toggleIndexesNodes();
							viewChanged = true;
						}
						case 'R', 'r', ' ' -> {
							viewChanged = true;
							currentView.reset();
							errorMessage = null;
						}
						default -> {
							System.out.println("Unknown key: " + keyStroke);
						}
					}
				}
				case KeyType.ArrowUp -> {
					currentView.updateOffset(0, 1);
					screenChanged = true;
				}
				case KeyType.ArrowDown -> {
					currentView.updateOffset(0, -1);
					screenChanged = true;
				}
				case KeyType.ArrowLeft -> {
					currentView.updateOffset(1, 0);
					screenChanged = true;
				}
				case KeyType.ArrowRight -> {
					currentView.updateOffset(-1, 0);
					screenChanged = true;
				}
				default -> {
					System.out.println("Unknown key: " + keyStroke);
				}
			}
		}
		return false;
	}

	public void setErrorMessage(String message) {
		lock.lock();
		try {
			errorMessage = message;
		} finally {
			lock.unlock();
		}
	}

}
