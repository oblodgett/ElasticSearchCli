package net.nilosplace.ElasticSearchCli.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import static org.jline.builtins.Completers.TreeCompleter.node;

import org.jline.builtins.Completers.TreeCompleter;

import net.nilosplace.ElasticSearchCli.commands.Command;
import net.nilosplace.ElasticSearchCli.grammar.CommandGrammarLexer;
import net.nilosplace.ElasticSearchCli.grammar.CommandGrammarParser;

public class CommandProcessor {

	public CommandProcessor(String[] args) throws Exception {
		if (args.length > 0) {
			StringJoiner joiner = new StringJoiner(" ");
			for (String arg : args) {
				joiner.add(arg);
			}
			String line = joiner.toString();
			processCommand(line);
		} else {
			Terminal terminal = TerminalBuilder.builder()
					.system(true)
					.build();

			// Dynamic completer that scans config dir for profile names
			Completer profileCompleter = (reader1, line, candidates) -> {
				ConfigHelper ch = ConfigHelper.getInstance();
				String configDir = (String) ch.get(ConfigKey.CONFIG_DIR.getKey());
				Path dir = Paths.get(configDir);
				if (Files.exists(dir)) {
					try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.properties")) {
						for (Path entry : stream) {
							String name = entry.getFileName().toString().replace(".properties", "");
							candidates.add(new Candidate(name));
						}
					} catch (IOException ignored) {
					}
				}
			};

			// Config key completer for set/get
			StringsCompleter configKeyCompleter = new StringsCompleter(
				java.util.Arrays.stream(ConfigKey.values())
					.map(ConfigKey::getKey)
					.toArray(String[]::new)
			);

			Completer completer = new AggregateCompleter(
				// config set <key> <value>
				new ArgumentCompleter(new StringsCompleter("config"), new StringsCompleter("set"), configKeyCompleter, NullCompleter.INSTANCE),
				// config get <key>
				new ArgumentCompleter(new StringsCompleter("config"), new StringsCompleter("get"), configKeyCompleter, NullCompleter.INSTANCE),
				// config print [profile]
				new ArgumentCompleter(new StringsCompleter("config"), new StringsCompleter("print"), profileCompleter, NullCompleter.INSTANCE),
				// config load <profile>
				new ArgumentCompleter(new StringsCompleter("config"), new StringsCompleter("load"), profileCompleter, NullCompleter.INSTANCE),
				// config save <profile>
				new ArgumentCompleter(new StringsCompleter("config"), new StringsCompleter("save"), profileCompleter, NullCompleter.INSTANCE),
				// config list
				new ArgumentCompleter(new StringsCompleter("config"), new StringsCompleter("list"), NullCompleter.INSTANCE),
				// cluster commands
				new ArgumentCompleter(new StringsCompleter("cluster"), new StringsCompleter("info", "nodes"), NullCompleter.INSTANCE),
				new ArgumentCompleter(new StringsCompleter("cluster"), new StringsCompleter("generate"), NullCompleter.INSTANCE),
				new ArgumentCompleter(new StringsCompleter("cluster"), new StringsCompleter("config"), new StringsCompleter("get", "set"), NullCompleter.INSTANCE),
				// index commands
				new ArgumentCompleter(new StringsCompleter("index"), new StringsCompleter("list", "info", "switchalias", "delete"), NullCompleter.INSTANCE),
				// repo commands
				new ArgumentCompleter(new StringsCompleter("repo"), new StringsCompleter("list", "delete", "create"), NullCompleter.INSTANCE),
				// snapshot commands
				new ArgumentCompleter(new StringsCompleter("snapshot"), new StringsCompleter("list", "delete", "create"), NullCompleter.INSTANCE),
				// alias commands
				new ArgumentCompleter(new StringsCompleter("alias"), new StringsCompleter("list", "create", "remove"), NullCompleter.INSTANCE),
				// standalone commands
				new ArgumentCompleter(new StringsCompleter("estop", "quit", "exit"), NullCompleter.INSTANCE)
			);

			LineReader reader = LineReaderBuilder.builder()
					.terminal(terminal)
					.completer(completer)
					.variable(LineReader.HISTORY_FILE,
						Paths.get(System.getProperty("user.home"), ".escli_history"))
					.variable(LineReader.HISTORY_SIZE, 1000)
					.build();

			while (true) {
				try {
					String line = reader.readLine("es> ").trim();
					if (line.isEmpty()) continue;
					processCommand(line);
				} catch (UserInterruptException e) {
					terminal.writer().println("Type 'quit' or 'exit' to leave.");
					terminal.flush();
				} catch (EndOfFileException e) {
					break;
				}
			}

			terminal.close();
		}
	}

	private void processCommand(String inputCommand) {
		CharStream charStream = CharStreams.fromString(inputCommand);
		CommandGrammarLexer lexer = new CommandGrammarLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CommandGrammarParser parser = new CommandGrammarParser(tokens);
		Command command = parser.input().command;
		if (command != null) {
			command.execute();
		} else {
			System.out.println("Command failed to process: " + inputCommand);
		}
	}
}
