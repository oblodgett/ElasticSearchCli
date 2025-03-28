package net.nilosplace.ElasticSearchCli;

import java.util.Scanner;
import java.util.StringJoiner;

public class CommandProcessor {

	public CommandProcessor(String[] args) {
		if(args.length > 0) {
			StringJoiner joiner = new StringJoiner(" ");
			for(String arg: args) {
				joiner.add(arg);
			}
			System.out.println("Command: " + joiner.toString());
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.print("> ");
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println("Command: " + line);
				System.out.print("> ");
			}
		}
	}
}
