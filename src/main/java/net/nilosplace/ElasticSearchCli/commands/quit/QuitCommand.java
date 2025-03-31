package net.nilosplace.ElasticSearchCli.commands.quit;

import net.nilosplace.ElasticSearchCli.commands.Command;

public class QuitCommand extends Command {

	@Override
	public void execute() {
		System.exit(0);
	}
	
	@Override
	public void print() {
		System.out.println("Command: quit");
	}
}
