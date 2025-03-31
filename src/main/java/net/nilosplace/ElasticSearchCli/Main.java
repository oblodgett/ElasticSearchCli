package net.nilosplace.ElasticSearchCli;

import net.nilosplace.ElasticSearchCli.utils.CommandProcessor;

public class Main {
	public static void main( String[] args ) {
		try {
			new CommandProcessor(args);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
