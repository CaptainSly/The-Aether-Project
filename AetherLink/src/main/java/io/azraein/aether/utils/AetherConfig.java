package io.azraein.aether.utils;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class AetherConfig {

	public static final String AETHER_SERVER_CONFIG = "aether_server.ini";
	public static final String AETHER_CLIENT_CONFIG = "aether_client.ini";

	public static boolean doesConfigExist(String configFile) {
		File config = new File(configFile);
		return config.exists();
	}

	public static void writeStringValue(String section, String key, String value, File config)
			throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(config);
		ini.put(section, key, value);
		ini.store();
	}
	
	public static String getString(String section, String key, File config)
			throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(config);
		return ini.get(section, key);
	}
	
	public static void writeIntValue(String section, String key, int value, File config)
			throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(config);
		ini.put(section, key, value);
		ini.store();
	}
	
	public static int getInt(String section, String key, File config) throws InvalidFileFormatException, IOException {
		Ini ini = new Ini(config);
		return ini.get(section, key, Integer.class);
	}

}
