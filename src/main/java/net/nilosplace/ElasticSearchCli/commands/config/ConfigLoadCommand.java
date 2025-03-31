package net.nilosplace.ElasticSearchCli.commands.config;

public class ConfigLoadCommand extends ConfigCommand {

	private String filename;

	public ConfigLoadCommand(String filename) {
		this.filename = filename;
	}

	@Override
	public void print() {
		System.out.println("Command: config load " + filename);
	}
	
	@Override
	public void execute() {
		System.out.println("Loading Config from: " + filename);
		try {
			configHelper.load(filename);
		} catch (Exception e) {
			System.err.println("Failed to load config from file: " + filename);
			System.err.println("Error: " + e.getMessage());
		}
	}
}
