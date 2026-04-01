package net.nilosplace.ElasticSearchCli.commands.config;

public class ConfigPrintCommand extends ConfigCommand {

	private String name;

	public ConfigPrintCommand() {
		this.name = null;
	}

	public ConfigPrintCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		if (name == null) {
			configHelper.print();
		} else {
			configHelper.printConfig(name);
		}
	}
}
