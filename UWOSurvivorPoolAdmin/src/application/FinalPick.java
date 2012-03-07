package application;
/**
 * Class that represents the final pick of a Contestant in Survivor
 * @author Connor Graham, Jonathan Di Nardo
 *
 */
public class FinalPick {
	private int roundPicked;	// round that the choice was made
	private String winnerID;	// Contestant that the Player believes will win, matches cID
	
	/**
	 * Contructor that creates a FinalPick 
	 * @param roundPicked
	 * @param winner
	 */
	public FinalPick(int roundPicked, String winnerID) {
		this.roundPicked = roundPicked;
		this.winnerID = winnerID;
	}


	/**
	 * Method that gets the round that the pick was made
	 * @return
	 */
	public int getRoundPicked() {
		return roundPicked;
	}


	/**
	 * Method that sets the round the pick was made
	 * @param roundPicked
	 */
	public void setRoundPicked(int roundPicked) {
		this.roundPicked = roundPicked;
	}


	/**
	 * Method that gets the Player's choice in winner
	 * @return
	 */
	public String getWinner() {
		return winnerID;
	}


	/**
	 * Method that sets the Player's choice in winner
	 * @param winner
	 */
	public void setWinner(String winner) {
		this.winnerID = winner;
	}
	
	/**
	 * To string method for returning a delimited format for finalPick
	 * @return The comma delimited string representing this objects state
	 */
	public String toString()
	{
		String theString;
		
		theString = winnerID + "," + roundPicked;
		
		return theString;
	}
}
