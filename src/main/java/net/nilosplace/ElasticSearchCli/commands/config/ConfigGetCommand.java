package net.nilosplace.ElasticSearchCli.commands.config;

public class ConfigGetCommand extends ConfigCommand {

	private String name;

	public ConfigGetCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		if (configHelper.get(name) != null) {
			System.out.println(name + ": " + configHelper.get(name));
		} else {
			System.out.println("No config entry found for: " + name);
		}
	}
}
