package net.nilosplace.ElasticSearchCli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import net.nilosplace.ElasticSearchCli.elastic.ClusterFacade;
import net.nilosplace.ElasticSearchCli.elastic.ClusterFacadeFactory;

public class ConfigHelper {

	private static final Logger log = LoggerFactory.getLogger(ConfigHelper.class);
	private static ConfigHelper instance = null;

	private ClusterFacade clusterFacade;

	@Getter private Map<String, String> config;

	private ConfigHelper() {
		config = new HashMap<>();
		for (ConfigKey ck : ConfigKey.values()) {
			config.put(ck.getKey(), ck.getDefaultValue());
		}
		autoLoadDefault();
		setupFacade();
	}

	private void autoLoadDefault() {
		Path defaultConfig = getConfigFilePath("default");
		if (Files.exists(defaultConfig)) {
			try {
				loadFromFile(defaultConfig);
				log.info("Loaded default config from: {}", defaultConfig);
				System.out.println("Loaded default config from: " + defaultConfig);
			} catch (IOException e) {
				log.warn("Could not load default config: {}", e.getMessage(), e);
			}
		}
	}

	private void setupFacade() {
		try {
			String baseUrl = config.get("es.prot") + "://" + config.get("es.host") + ":" + config.get("es.port");
			log.info("Connecting to cluster: {}", baseUrl);
			clusterFacade = ClusterFacadeFactory.create(baseUrl);
			log.info("Connected successfully");
		} catch (Exception e) {
			log.warn("Could not connect to cluster for version detection: {}", e.getMessage(), e);
		}
	}

	private Path getConfigDir() {
		return Paths.get(config.get(ConfigKey.CONFIG_DIR.getKey()));
	}

	private Path getConfigFilePath(String name) {
		return getConfigDir().resolve(name + ".properties");
	}

	public Object get(String name) {
		return config.get(name);
	}

	public void put(String name, String value) {
		ConfigKey key = ConfigKey.fromKey(name);
		if (key == null) {
			System.err.println("Unknown config key: " + name);
			System.err.println("Valid keys: ");
			for (ConfigKey ck : ConfigKey.values()) {
				System.err.println("  " + ck.getKey() + " - " + ck.getDescription());
			}
			return;
		}
		config.put(name, value);
		if (name.startsWith("es.")) {
			setupFacade();
		}
	}

	public void load(String name) throws IOException {
		Path file = getConfigFilePath(name);
		loadFromFile(file);
		setupFacade();
	}

	private void loadFromFile(Path file) throws IOException {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(file)) {
			props.load(in);
		}
		for (ConfigKey ck : ConfigKey.values()) {
			String value = props.getProperty(ck.getKey());
			if (value != null) {
				config.put(ck.getKey(), value);
			}
		}
	}

	public void save(String name) throws IOException {
		Path dir = getConfigDir();
		if (!Files.exists(dir)) {
			Files.createDirectories(dir);
		}
		Path file = getConfigFilePath(name);
		Properties props = new Properties();
		for (Entry<String, String> entry : config.entrySet()) {
			props.setProperty(entry.getKey(), entry.getValue());
		}
		try (OutputStream out = Files.newOutputStream(file)) {
			props.store(out, "ElasticSearchCli config: " + name);
		}
	}

	public void list() {
		Path dir = getConfigDir();
		if (!Files.exists(dir)) {
			System.out.println("Config directory does not exist: " + dir);
			return;
		}
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.properties")) {
			boolean found = false;
			for (Path entry : stream) {
				String filename = entry.getFileName().toString();
				System.out.println("  " + filename.replace(".properties", ""));
				found = true;
			}
			if (!found) {
				System.out.println("No config files found in: " + dir);
			}
		} catch (IOException e) {
			System.err.println("Error listing config directory: " + e.getMessage());
		}
	}

	public void printConfig(String name) {
		Path file = getConfigFilePath(name);
		if (!Files.exists(file)) {
			System.err.println("Config file not found: " + file);
			return;
		}
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(file)) {
			props.load(in);
		} catch (IOException e) {
			System.err.println("Error reading config file: " + e.getMessage());
			return;
		}
		for (String key : props.stringPropertyNames().stream().sorted().toList()) {
			System.out.println(key + ": " + props.getProperty(key));
		}
	}

	public static ConfigHelper getInstance() {
		if (instance == null) {
			instance = new ConfigHelper();
		}
		return instance;
	}

	public ClusterFacade getClusterFacade() {
		return clusterFacade;
	}

	public void print() {
		for (ConfigKey ck : ConfigKey.values()) {
			String value = config.get(ck.getKey());
			System.out.println(ck.getKey() + ": " + value + "  (" + ck.getDescription() + ")");
		}
	}
}
