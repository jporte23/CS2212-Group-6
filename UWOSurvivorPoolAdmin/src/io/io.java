package io;

import java.io.File;

/**
 * Not currently in use. A goal for the future is to seperate IO out of the
 * app package and into here.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public class io {
	/**
	 * The name of the configuration file.
	 */
	private static final String CONFIG_FILE_PATH = "config.txt";
	/**
	 * Specifies the delineators of the configuration file.
	 */
//	private static final String CONFIG_FILE_DELINEATORS = " ,;";
	
	public static void writePropertiesTofile() {
		// TODO Auto-generated method stub
		
	}

	public static void clearData() {
		// TODO Auto-generated method stub
		
	}

	public static void readProperties() {
		// TODO Auto-generated method stub
		
	}

	public static boolean hasPropertiesFile() {
		File properties = new File(CONFIG_FILE_PATH);
		return properties.exists();
	}
	
	
}
