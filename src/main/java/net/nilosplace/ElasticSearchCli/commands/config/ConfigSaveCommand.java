package net.nilosplace.ElasticSearchCli.commands.config;

public class ConfigSaveCommand extends ConfigCommand {

	private String name;

	public ConfigSaveCommand(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		System.out.println("Saving config: " + name);
		try {
			configHelper.save(name);
		} catch (Exception e) {
			System.err.println("Failed to save config: " + name);
			System.err.println("Error: " + e.getMessage());
		}
	}
}
