package net.nilosplace.ElasticSearchCli.commands;

import net.nilosplace.ElasticSearchCli.utils.ConfigHelper;

public abstract class Command implements CommandInterface {
	protected ConfigHelper configHelper = ConfigHelper.getInstance();
}
