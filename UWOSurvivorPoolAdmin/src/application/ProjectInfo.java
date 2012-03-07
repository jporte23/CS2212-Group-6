package application;

/**
 * 
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public class ProjectInfo {
	public static final int YEAR			= 2012;
	public static final float VERSION		= 0.03f;
	public static final String TITLE		= "UWO Survivor Pool Admin " + VERSION;
	public static final String[] AUTHORS	= {
		"Moammer Frayjoun", 
		"Connor Graham",
		"Jonathan Di Nardo",
		"Jordan Porter",
		"Coby Viner"
	};
	
	public static String getCopyWriteInfo() {
		return "\u00A9 " + YEAR + " " + getAuthorList();
	}
	
	public static String getAuthorList() {
		return AUTHORS[0] + ", " + AUTHORS[1] + ", " + AUTHORS[2] + ", " + AUTHORS[3] + ", " + AUTHORS[4] + " ";
	}
}
