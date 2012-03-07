package application;
/**
 * Class that represents a contestant from the popular TV show, Survivor
 * @author Connor Graham
 *
 */
public class Contestant extends Person{
		
	private String cID;				//Stores the contestant's unique ID
	private String tribe;			//Stores the contestant's tribe
	private int roundEliminated;	//Stores the round that this contestant was eliminated
	private String photoPath;		//Stores the contestant's photoPath
	
	/**
	 * Constructor that creates a Person with a tribe and unique ID
	 * @param firstName
	 * @param lastName
	 * @param cID
	 * @param tribe
	 */
	public Contestant(String firstName, String lastName, String cID,
			String tribe) {
		super(firstName, lastName);
		this.cID = cID;
		this.tribe = tribe;
		this.roundEliminated = 0;
		this.photoPath = "default";
	}
	/**
	 * Constructor that creates a player object from a file line
	 * @param fileLine the fileLine to be interpreted as a contestant object
	 */
	public Contestant(String fileLine) {
		super(fileLine.split(",")[0], fileLine.split(",")[1]);
		
		String [] temp = fileLine.split(",");
		this.cID = temp[2];
		this.tribe = temp[3];
		this.roundEliminated = Integer.parseInt(temp[4]);
		this.photoPath = temp[5];
	}
	
	/**
	 * Method that gets the Contestant's ID
	 * @return cID the Contestant's unique ID
	 */
	public String getcID() {
		return cID;
	}
	
	/**
	 * Method that sets the Contestant's ID
	 * @param cID the Contestant's new cID
	 */
	public void setcID(String cID) {
		this.cID = cID;
	}

	/**
	 * Method that gets the Contestant's tribe name
	 * @return tribe the Contestant's current tribe
	 */
	public String getTribe() {
		return tribe;
	}

	/**
	 * Method that sets the Contestant's tribe name
	 * @param tribe the Contestant's new tribe
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	/**
	 * Method that gets when the Contestant was eliminated. Equal to 0 iff still in the game.
	 * @return roundElminated the round the Contestant was eliminated
	 */
	public int getRoundEliminated() {
		return roundEliminated;
	}

	/**
	 * Method that sets when the contestant was eliminated
	 * @param weeksInGame
	 */
	public void setRoundEliminated(int roundEliminated) {
		this.roundEliminated = roundEliminated;
	}
	
	/**
	 * To string method for returning a delimited format for contestants
	 * @return The comma delimited string representing this objects state
	 */
	public String toString()
	{
		return getFirstName() + "," + getLastName() + "," + cID + "," + tribe + "," + roundEliminated + "," + photoPath;
	}
}