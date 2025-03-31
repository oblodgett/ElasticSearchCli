package net.nilosplace.ElasticSearchCli;

import java.util.Scanner;
import java.util.StringJoiner;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import net.nilosplace.ElasticSearchCli.commands.Command;
import net.nilosplace.ElasticSearchCli.grammar.CommandGrammarLexer;
import net.nilosplace.ElasticSearchCli.grammar.CommandGrammarParser;

public class CommandProcessor {

	public CommandProcessor(String[] args) {
		if(args.length > 0) {
			StringJoiner joiner = new StringJoiner(" ");
			for(String arg: args) {
				joiner.add(arg);
			}
			String line = joiner.toString();
			processCommand(line);
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.print("> ");
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				processCommand(line);
				System.out.print("> ");
			}
		}
	}

	private void processCommand(String inputCommand) {
		CharStream charStream = CharStreams.fromString(inputCommand);
		CommandGrammarLexer lexer = new CommandGrammarLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CommandGrammarParser parser = new CommandGrammarParser(tokens);
		Command command = parser.input().command;
		if(command != null) {
			command.execute();
		} else {
			System.out.println("Command failed to process: " + inputCommand);
		}
	}
}
