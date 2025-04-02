package net.nilosplace.ElasticSearchCli.commands.estop;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import net.nilosplace.ElasticSearchCli.commands.Command;
import net.nilosplace.ElasticSearchCli.commands.estop.views.ViewHandler;

public class EsTopCommand extends Command {

	@Override
	public void execute() {
		try {
			Screen screen = new DefaultTerminalFactory().createScreen();
			ViewHandler vh = new ViewHandler(screen);
			vh.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
