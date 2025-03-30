package net.nilosplace.ElasticSearchCli.commands;

public class ConfigSaveCommand extends ConfigCommand {

	private String filename;

	public ConfigSaveCommand(String filename) {
		this.filename = filename;
	}

	@Override
	public void print() {
		System.out.println("Command: config save " + filename);
	}

	@Override
	public void execute() {
		System.out.println("Save Config to: " + filename);
		try {
			configHelper.save(filename);
		} catch (Exception e) {
			System.err.println("Failed to save config to file: " + filename);
			System.err.println("Error: " + e.getMessage());
		}
	}
	
}
