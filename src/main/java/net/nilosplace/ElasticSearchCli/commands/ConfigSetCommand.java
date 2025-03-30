package net.nilosplace.ElasticSearchCli.commands;

public class ConfigSetCommand extends ConfigCommand {

	private String name;
	private String value;

	public ConfigSetCommand(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void print() {
		System.out.println("Command: config set " + name + " " + value);
	}

	@Override
	public void execute() {
		configHelper.put(name, value);
	}
}
