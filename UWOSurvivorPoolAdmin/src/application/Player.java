package application;
/**
 * Class that represents a Person in a office pool for the TV show Survivor
 * @author Connor Graham, Jonathan Di Nardo, Coby Viner
 *
 */
public class Player extends Person {
		
	private String pID;					// Player's unique ID
	private int points;					// Player's current points
	private String weeklyPickID;		// Player's weekly pick on which Contestant will be eliminated
	private FinalPick finalPick;		// Player's pick on which Contestant will win the game
	private String answer;				// Player's answer to the current bonus question
	
	/**
	 * Constructor that creates a person with a unique ID
	 * @param firstName
	 * @param lastName
	 * @param pID
	 */
	public Player(String firstName, String lastName, String pID) {
		super(firstName, lastName);
		this.pID = pID;
		this.points = 0;
		this.weeklyPickID = "0";
		this.finalPick = new FinalPick(0,"0");
		this.answer = "0";
	}
	
	/**
	 * Constructor that creates a player object from a file line
	 * @param fileLine the fileLine to be interpreted as a player object
	 */
	public Player(String fileLine)
	{
		super(fileLine.split(",")[0], fileLine.split(",")[1]);
		
		String [] temp = fileLine.split(",");
		pID = temp[2];
		points = Integer.parseInt(temp[3]);
		weeklyPickID = temp[4];
		finalPick = new FinalPick (Integer.parseInt(temp[5]), temp[6]);
		answer = temp[7];
	}

	/**
	 * Method that gets the Player's unique ID
	 * @return pID the player's unique ID
	 */
	public String getpID() {
		return pID;
	}

	/**
	 * Method that sets the Player's unique ID
	 * @param pID the new pID
	 */
	public void setpID(String pID) {
		this.pID = pID;
	}

	/**
	 * Method that gets the Player's points
	 * @return The number of points the player has
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Method that sets the Player's points
	 * @param points new value of points
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Method that gets the Player's weekly pick on who will be eliminated
	 * @return weeklyPickID the ID that the player has selected as his/her choice for this week
	 * This value and method are placeholder, they will later be implemented as a Pick object
	 */
	public String getWeeklyPick() {
		return weeklyPickID;
	}

	/**
	 * Method that sets the Player's weekly pick on who will be eliminated
	 * @param weeklyPick
	 */
	public void setWeeklyPick(String weeklyPickID) {
		this.weeklyPickID = weeklyPickID;
	}

	/**
	 * Method that gets the Player's pick on who will win the game
	 * @return
	 */
	public FinalPick getFinalPick() {
		return finalPick;
	}

	/**
	 * Method that sets the Player's pick on who will win the game
	 * @param finalPick
	 */
	public void setFinalPick(int round, String pickID) {
		this.finalPick = new FinalPick(round, pickID);
	}

	/**
	 * Method that gets the Player's answer to a BonusQuestion
	 * @return
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Method that sets the Player's answer to a BonusQuestion
	 * @param answer
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	/**
	 * Method that calculates the number of points the Player has at any point in the gmame;
	 * @return
	 */
	public int calculatePoints(){
		return 1;
	}
	
	/**
	 * To string method for returning a delimited format for players
	 * @return The comma delimited string representing this objects state
	 */
	public String toString()
	{
		String theString ="";
		
		if(finalPick != null)
			theString = getFirstName() + "," + getLastName() + "," + pID + "," + points + "," + weeklyPickID + "," + finalPick.toString() + "," + answer;
		else
			theString = getFirstName() + "," + getLastName() + "," + pID + "," + points + "," + weeklyPickID + "," + 0 + "," + 0 + "," + answer;
		
		return theString;
	}
}
