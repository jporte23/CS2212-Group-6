package ui;
import java.awt.Color;

/**
 * The various colour schemes that are present in the GUI.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public enum ColourScheme {
	SUNSET(Color.ORANGE, Color.YELLOW, Color.RED, Color.YELLOW, getComplement(Color.YELLOW)), 
	DAWN(Color.BLUE, Color.BLUE, Color.CYAN, getMauve(), getComplement(Color.BLUE)), 
	DEFAULT(new Color(51,98,140), new Color(169,176,190), new Color(214, 217, 223), Color.WHITE, Color.BLACK);
	
	private final Color c1;
	private final Color c2;
	private final Color c3;
	private final Color c4;
	private final Color readableText;
	
	/**
	 * Sets this colour scheme.
	 * @param c1			Colour one
	 * @param c2			Colour two
	 * @param c3			Colour three - sets most backgrounds
	 * @param c4			Colour four  - sets the text area background
	 * @param readableText	Colour for text. It is recommended that this be black or the backgrounds complement.
	 */
	private ColourScheme(Color c1, Color c2, Color c3, Color c4, Color readableText) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.readableText = readableText;
	}

	/**
	 * Gets colour 1.
	 * @return Colour 1
	 */
	public Color getC1() {
		return c1;
	}

	/**
	 * Gets colour 2.
	 * @return Colour 2
	 */
	public Color getC2() {
		return c2;
	}

	/**
	 * Gets colour 3, the colour of most backgrounds.
	 * @return Colour 3, the colour of most backgrounds
	 */
	public Color getC3() {
		return c3;
	}

	/**
	 * Gets colour 4, the text area background.
	 * @return Colour 4, the text area background
	 */
	public Color getC4() {
		return c4;
	}

	/**
	 * Gets readable text colour. This is usually black or the backgrounds complement.
	 * @return Colour readable text colour
	 */
	public Color getReadableText() {
		return readableText;
	}
	
	/**
	 * Checks if the given string is an element of this enumeration (ignoring case).
	 * @param s	The string to check
	 * @return	True iff the given string is an element of this enumeration (ignoring case)
	 */
	public static boolean isColourScheme(String s) {
		for (ColourScheme c : ColourScheme.values()) {
			if (s.equalsIgnoreCase(c.toString()))	return true;
		}
		return false;
	}
	
	/**
	 * Gets the given colour's complement.
	 * @param colour	The colour whose complement is to be returned
	 * @return			The given colour's complement
	 */
	public static Color getComplement(Color colour) {
		//convert to HSB
		float[] HSB = Color.RGBtoHSB(colour.getRed(), colour.getGreen(), colour.getBlue(), null);

		//calculate the opposite hue
		HSB[0] += 0.5f;
		if (HSB[0] > 1) HSB[0] -= 1;

		//return the HSB color
		return Color.getHSBColor(HSB[0], HSB[1], HSB[2]);
	}
	
	/**
	 * Gets the colour mauve.
	 * @return	The colour mauve
	 */
	public static Color getMauve() {
		return new Color(224, 176, 255);
	}
}