package net.nilosplace.ElasticSearchCli.commands.config;

public class ConfigListCommand extends ConfigCommand {

	public ConfigListCommand() {
	}

	@Override
	public void execute() {
		configHelper.list();
	}
}
