package net.nilosplace.ElasticSearchCli;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.WithDefault;
import lombok.Getter;
import net.nilosplace.process_display.util.ObjectFileStorage;

public class ConfigHelper {

	private static ConfigHelper instance = null;
	private ObjectFileStorage<Map<String, Object>> storage = new ObjectFileStorage<>();

	@Getter
	private Map<String, Object> config;

	private ConfigHelper() {
		SmallRyeConfig srcConfig = new SmallRyeConfigBuilder().withMapping(Es.class).build();
		Es es = srcConfig.getConfigMapping(Es.class);
		config = new HashMap<>();
		config.put("es.prot", es.prot());
		config.put("es.host", es.host());
		config.put("es.port", es.port());
	}

	public Object get(String name) {
		return config.get(name);
	}

	public void put(String name, Object value) {
		config.put(name, value);
	}

	public void load(String filename) throws Exception {
		config = storage.readObjectFromFile(filename);
	}

	public void save(String filename) throws Exception {
		storage.writeObjectToFile(config, filename);
	}

	public static ConfigHelper getInstance() {
		if(instance == null) {
			instance = new ConfigHelper();
		}
		return instance;
	}

	public void print() {
		for(Entry<String, Object> entry: config.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	@ConfigMapping(prefix = "es")
	public interface Es {
		@WithDefault("http")
		String prot();
		@WithDefault("localhost")
		String host();
		@WithDefault("9200")
		int port();
	}
}
