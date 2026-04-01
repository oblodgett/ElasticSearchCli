package net.nilosplace.ElasticSearchCli;

import java.nio.file.Files;
import java.nio.file.Paths;

import net.nilosplace.ElasticSearchCli.utils.CommandProcessor;

public class Main {

	static {
		try {
			Files.createDirectories(Paths.get("logs"));
		} catch (Exception e) {
			// ignore
		}
	}

	public static void main(String[] args) {
		try {
			new CommandProcessor(args);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
