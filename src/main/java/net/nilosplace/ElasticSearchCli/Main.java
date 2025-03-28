package net.nilosplace.ElasticSearchCli;

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
