package net.nilosplace.ElasticSearchCli.commands;

import net.nilosplace.ElasticSearchCli.ConfigHelper;

public abstract class ConfigCommand extends Command {
	protected ConfigHelper configHelper = ConfigHelper.getInstance();
}
