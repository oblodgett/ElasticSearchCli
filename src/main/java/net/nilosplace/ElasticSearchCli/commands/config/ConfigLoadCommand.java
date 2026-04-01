package net.nilosplace.ElasticSearchCli.commands.config;

public class ConfigLoadCommand extends ConfigCommand {

	private String name;

	public ConfigLoadCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		System.out.println("Loading config: " + name);
		try {
			configHelper.load(name);
		} catch (Exception e) {
			System.err.println("Failed to load config: " + name);
			System.err.println("Error: " + e.getMessage());
		}
	}
}
