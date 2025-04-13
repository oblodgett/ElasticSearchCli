package net.nilosplace.ElasticSearchCli.commands.estop.views;

import java.io.IOException;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.screen.Screen;

import co.elastic.clients.elasticsearch.cat.tasks.TasksRecord;
import net.nilosplace.ElasticSearchCli.commands.estop.ClusterDataManager;
import net.nilosplace.ElasticSearchCli.commands.estop.model.Tree;
import net.nilosplace.ElasticSearchCli.commands.estop.model.Tree.Node;

public class TaskView extends ViewBase {

	private int linePrint = 0;

	public TaskView(Screen screen, ClusterDataManager manager) {
		super(screen, manager);
		header = "Tasks View";
	}

	@Override
	public void draw(boolean clear) throws IOException {
		if (clear) {
			screen.clear();
		}

		Tree<TasksRecord> tree = manager.getTaskTree();

		linePrint = 0;
		printText(0, 0, "Tasks", ANSI.WHITE);
		for (Node<TasksRecord> node : tree.getRoot().getChildren()) {
			printTreeNode(node, 1);
		}

		screen.refresh();
	}

	public void printTreeNode(Node<TasksRecord> node, int level) {
		linePrint++;
		printChar(level * 3 - 2, linePrint, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
		printText(level * 3, linePrint, node.getData().node() + " - " + node.getData().action(), ANSI.WHITE);
		level++;
		for (Node<TasksRecord> child : node.getChildren()) {
			printTreeNode(child, level);
		}

	}

}
