package application;
/**
 * Class that represents a Person
 * @author Connor Graham
 *
 */
public class Person {

	private String firstName;		// First name
	private String lastName;		// Last name
	
	/**
	 * constructor that creates a person with a first and last name
	 * @param firstName
	 * @param lastName
	 */
	public Person(String firstName, String lastName){
		this.firstName=firstName;
		this.lastName=lastName;
	}

	/**
	 * Method that returns the Person's first name
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Method that sets the Person's first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Method that returns the Person's last name
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Method that sets the Person's last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
