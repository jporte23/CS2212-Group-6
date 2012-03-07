package ui;

/**
 * Allowable input types. Used for input validation by Regular Expression.
 * @author Coby Viner
 * @version 0.1 Coby Viner, March 3, 2012
 */
public enum AllowableInputType {
	/*
	 * LETTERS 		- Any of a-z or A-Z zero or more times
	 * ALPHANUMERIC - Any of a-z, A-Z, or 0-9 zero or more times
	 * ANY			- Any character (may or may not match line terminators)
	 */
	LETTERS("[a-zA-Z]*+"), ALPHANUMERIC("[a-zA-Z_0-9 \\t\\n\\x0B\\f\\r]*+"), ANY(".");

	/**
	 * The regex to validate against.
	 */
	private String regex;
	
	/**
	 * Constructor.
	 * @param regex	The regex with which to construct the input type
	 */
	private AllowableInputType(String regex) {
		this.regex = regex;
	}
	
	/**
	 * Gets the regex associated with this input type.
	 * @return	The regex associated with this input type
	 */
	public String getAssociatedRegex() {
		return this.regex;
	}
}
