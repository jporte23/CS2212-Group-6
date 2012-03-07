package ui;

import java.awt.Component;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

/**
 * Checks for valid input in Strings. It does this via a regular expression
 * from {@link ui.AllowableInputType} and with a minimum and maximum length.
 * @author Coby Viner
 * @version 0.1 Coby Viner, March 3, 2012
 */
public class StringVerifier extends InputVerifier {
	private String regex, invalidMessage;
	private int min, max;
	private Component parent;

	/**
	 * Constructor.
	 * @param min				The minimum length
	 * @param max				The maximum length
	 * @param inputType			See {@link ui.AllowableInputType}
	 */
	public StringVerifier(int min, int max, AllowableInputType inputType) {
		this.regex = inputType.getAssociatedRegex();
		this.min = min;
		this.max = max;
	}

	/**
	 * Constructor. Use this to specify an error message if invalid input is given.
	 * Note that usage of this causes the {@link #verify(JComponent)} to have the
	 * side-effect of displaying an error dialogue when erroneous input is provided.
	 * @param min				The minimum length
	 * @param max				The maximum length
	 * @param inputType			See {@link ui.AllowableInputType}
	 * @param invalidMessage	The message to display when erroneous input is provided
	 * @param parent			The parent GUI component requesting input verification
	 */
	public StringVerifier(int min, int max, AllowableInputType inputType, String invalidMessage, Component parent) {
		this.regex = inputType.getAssociatedRegex();
		this.min = min;
		this.max = max;
		this.invalidMessage = invalidMessage;
		this.parent = parent;
	}

	/**
	 * Checks if the text is of the correct length.
	 * @param text	The text whose length is checked
	 * @return		True iff the text is of the correct length
	 */
	private boolean isCorrectLength(String text) {
		return text.length() >= min && text.length() <= max;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
	 */
	@Override
	public boolean verify(JComponent input) {
		JTextComponent tf = (JTextComponent) input;
		if (isCorrectLength(tf.getText()) && tf.getText().matches(regex)) {
			return true;
		} else {
			if (parent != null && invalidMessage != null) {
				JOptionPane.showMessageDialog (parent, invalidMessage, "Input Invalid", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}
	}
}
