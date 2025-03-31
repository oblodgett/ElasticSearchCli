package net.nilosplace.ElasticSearchCli.commands.estop;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import net.nilosplace.ElasticSearchCli.commands.Command;
import net.nilosplace.ElasticSearchCli.commands.estop.windows.ClusterWindow;

public class EsTopCommand extends Command {

	@Override
	public void execute() {

		try {
			Terminal term = new DefaultTerminalFactory().createTerminal();
			Screen screen = new TerminalScreen(term);
			WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
			screen.startScreen();

			ClusterWindow window = new ClusterWindow();
			gui.addWindow(window);
			// use GUI here until the GUI wants to exit
			Thread.sleep(2000);

			window.close();
			screen.stopScreen();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void print() {

	}
	
}
