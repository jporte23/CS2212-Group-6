package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 27, 2012
 */
public class Standings {
	//From http://jontangerine.com/silo/html/placeholder/ - CCSA License
	private static final String HTML_FILE_PATH = "Lorem Ipsum.html";

	public static URL getStandings() {
		File file = new File(HTML_FILE_PATH);
		URL helpURL = null;
		try {
			helpURL = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (helpURL != null) {
			return helpURL;
		} else {
			System.err.println("Couldn't find file: " + HTML_FILE_PATH);
			return null;
		}
	}
}
