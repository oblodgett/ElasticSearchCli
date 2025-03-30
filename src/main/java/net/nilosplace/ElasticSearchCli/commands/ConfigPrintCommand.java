package net.nilosplace.ElasticSearchCli.commands;

public class ConfigPrintCommand extends ConfigCommand {

	public ConfigPrintCommand() {

	}

	@Override
	public void print() {
		System.out.println("Command: config print");
	}

	@Override
	public void execute() {
		configHelper.print();
	}
	
}
