package ui;

import java.awt.Font;

/**
 * A storage class for the myriads of constants used across this package.
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 * @version 0.2 Coby Viner, March 3, 2012
 */
public class Constants {
	protected static final Font 	DEFAULT_FONT 			= new Font("SansSerif", Font.PLAIN, 18);
	protected static final Font		BOLD_FONT 				= new Font("SansSerif", Font.BOLD, 18);
	protected static final String	ADD						= "Add";
	protected static final String	MODIFY					= "Modify";
	protected static final String	DELETE					= "Delete";
	protected static final String	NONE_SELECTED_TITLE		= "No Items Selected";
	protected static final String	NONE_SELECTED_MESSAGE	= "You must select an item before performing this operation.";
	protected static final String 	IMAGE_PATH 				= "img/";
	protected static final String 	ICON_PATH 				= IMAGE_PATH + "icons/";
}
