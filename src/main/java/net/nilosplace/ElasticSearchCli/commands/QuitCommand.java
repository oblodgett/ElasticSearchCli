package net.nilosplace.ElasticSearchCli.commands;

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
