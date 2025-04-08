package net.nilosplace.ElasticSearchCli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.nilosplace.ElasticSearchCli.utils.ConfigHelper;

public abstract class Command implements CommandInterface {
	protected ConfigHelper configHelper = ConfigHelper.getInstance();
	protected ObjectMapper mapper = new ObjectMapper();
}
