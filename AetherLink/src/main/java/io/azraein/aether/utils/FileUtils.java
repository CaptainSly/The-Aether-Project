package io.azraein.aether.utils;

import java.io.File;

public class FileUtils {

	// Directories
	public static String AETHER_WORKING_DIRECTORY = System.getProperty("user.dir") + "/Aether/";
	public static final String AETHER_USER_DIRECTORY = AETHER_WORKING_DIRECTORY + "users/";

	private static final String[] dirs = {
			"users", "temp", 
	};
	
	public static void createAetherFileSystem() {
		
		for (String dir : dirs) {
			File d = new File(AETHER_WORKING_DIRECTORY + dir);
			d.mkdirs();
		}
		
		
		
	}
	
}
