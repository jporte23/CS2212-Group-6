package application;

//XXX MOVE IO
import java.io.*;
import java.util.ArrayList;

import ui.ColourScheme;

/**
 * Class that represents a Survivor Office Pool
 * @author Connor Graham, Jonathan Di Nardo, Coby Viner
 *
 */
public class Survivor {
	private int currentRound;						//The current round the game is on
	private int	totalRounds;						//The maximum, or final round
	private int betAmount;							//The amount each player is betting
	private ColourScheme colourScheme;				//Stores the game's current colorScheme
	private boolean isGameStarted;					//Stores whether or not the game has started
	//TODO REFEACTOR
	public ArrayList <Player> players;				//Stores a list of all of the players in the game
	public ArrayList <Contestant> contestants;		//Stores a list of all of the contestants in the game
	
	/**
	 * Calculates the size of the total pool
	 * @return totalPool the total office pool size
	 */
	public int getTotalPool(){
		return (betAmount * players.size());
	}
	
	/**
	 * Default constructor that attempts to load information from files, if files do not exist, nothing will be loaded
	 */
	public Survivor() {
		this.players		= new ArrayList <Player>();
		this.contestants	= new ArrayList <Contestant>();
		this.colourScheme	= ColourScheme.DEFAULT;
		this.isGameStarted 	= false;
		load();//XXX order...
	}
	
	/**
	 * Method that gets the current round the game is in
	 * @return
	 */
	public int getCurrentRound() {
		return currentRound;
	}
	
	/**
	 * Method that sets the current round the game is in
	 * @param currentRound
	 */
	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}
	
	/**
	 * Sets the colour scheme.
	 * @param colourScheme	The colour scheme to set
	 */
	public void setColourScheme(ColourScheme colourScheme) {
		this.colourScheme = colourScheme;
	}
	
	/**
	 * Method that gets the total number of rounds the game will have.
	 * @return
	 */
	public int getTotalRounds() {
		return totalRounds;
	}
	
	/**
	 * Method that returns the current colour scheme.
	 * @return	The current colour scheme
	 */
	public ColourScheme getCurrentColourScheme() {
		return this.colourScheme;
	}
	
	/**
	 * Method that sets the total number of rounds the game will have.
	 * @param totalRounds
	 */
	public void setTotalRounds(int totalRounds) {
		this.totalRounds = totalRounds;
	}
	
	/**
	 * Method that gets the bet amount for the pool.
	 * @return
	 */
	public int getBetAmount() {
		return betAmount;
	}
	
	/**
	 * Method that sets the bet amount for the pool.
	 * @param betAmount
	 */
	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}
	
	/**
	 * Adds a new player to the arrayList, constructs it from scratch.
	 * It overwrites any existing player with the same pID
	 * @param firstName the first name of the new player
	 * @param lastName the last name of the new player
	 * @param cID the cID of the new player
	 */
	public void addPlayer(String firstName, String lastName, String pID) {
		if(locationOfPlayer(pID) == -1)
			players.add(new Player(firstName, lastName, pID));
		else
			players.set(locationOfPlayer(pID), new Player(firstName, lastName, pID));
	}
	
	/**
	 * Adds a player object to arrayList, or overwrites that player if its unique ID is already found in the arrayList
	 * @param player the player object to be added to the list
	 */
	public void addPlayer(Player player) {
		if(locationOfPlayer(player.getpID()) == -1)
			players.add(player);
		else
			players.set(locationOfPlayer(player.getpID()), player);
	}
	
	/**
	 * Method to return the location of a player based on his or her pID within the arrayList
	 * @param iD the ID to be searched for
	 * @return the location of the player, returns -1 if the player is not in the arrayList
	 */
	private int locationOfPlayer(String iD){
		int location = -1;
		
		for(int x = 0; x < players.size(); x++)
		{
			if(iD.equalsIgnoreCase(players.get(x).getpID()) == true)
				location = x;
		}//End of for
		
		return location;
	}//end of locationOfPlayer method
	
	/**
	 * Method to return the location of a contestant based on his or her cID within the arrayList
	 * @param iD the ID to be searched for
	 * @return the location of the contestant, returns -1 if the contestant is not in the arrayList
	 */
	private int locationOfContestant(String iD) {
		int location = -1;
		
		for(int x = 0; x < contestants.size(); x++)
		{
			if(iD.equalsIgnoreCase(contestants.get(x).getcID()) == true)
				location = x;
		}//End of for
		
		return location;
	}//end of locationOfContestant method
	
	/**
	 * Adds a new contestants to the arrayList, constructs it from scratch.
	 * It overwrites any existing contestants with the same cID
	 * @param firstName the first name of the new contestant
	 * @param lastName the last name of the new contestant
	 * @param cID the cID of the new contestant
	 * @param tribe the tribe of the new contestant
	 */
	public void addContestant(String firstName, String lastName, String cID, String tribe) {
		if(locationOfContestant(cID) == -1)
			contestants.add(new Contestant(firstName, lastName, cID,tribe));
		else
			contestants.set(locationOfContestant(cID), new Contestant(firstName, lastName, cID,tribe));
	}
	
	/**
	 * Adds a Contestant object to arrayList, or overwrites that Contestant if its unique ID is already found in the arrayList
	 * @param aContestant the contestant object to be added to the list
	 */
	public void addContestant(Contestant aContestant) {
		if(locationOfContestant(aContestant.getcID()) == -1)
			contestants.add(aContestant);
		else
			contestants.set(locationOfContestant(aContestant.getcID()), aContestant);
	}
	
	/**
	 * Starts the game.
	 * @param betAmount the bet amount for the game
	 */
	public void startGame(int betAmount) {
		this.betAmount = betAmount;
		this.isGameStarted = true;
	}
	
	/**
	 * Returns if the game has started.
	 * @return	True iff the game has started
	 */
	public boolean isGameStarted() {
		return this.isGameStarted;
	}	
	
	/**
	 * Returns the number of contestants
	 * @return the number of contestants in the array list
	 */
	public int getNumContestants() {
		return contestants.size();
	}
	
	/**
	 *Saves the game of survivor to disk
	 */
	public void save() {
		//Overwrite the current contestant file, or create one
		try{
				FileWriter mystream = new FileWriter("contestants");
				BufferedWriter writer = new BufferedWriter(mystream);
				
				//Write the contestants, one per line
				for(int x = 0; x < contestants.size(); x++)
					writer.write(contestants.get(x).toString()+ "\n");
				
				//Close the file
				writer.close();
			}catch(Exception error){System.out.println("Fatal Error: " + error.getMessage());}
		
		//Overwrite the current player file, or create one
		try {
				FileWriter mystream = new FileWriter("players");
				BufferedWriter writer = new BufferedWriter(mystream);
				
				//Write the player, one per line
				for(int x = 0; x < players.size(); x++)
					writer.write(players.get(x).toString() + "\n");
				
				//Close the file
				writer.close();
			}catch(Exception error){System.out.println("Fatal Error: " + error.getMessage());}
		
		//Overwrite the current game file, or create one
		try{
				FileWriter mystream = new FileWriter("game");
				BufferedWriter writer = new BufferedWriter(mystream);
				writer.write(currentRound + "," + totalRounds + "," + betAmount + "," + this.getCurrentColourScheme() + "," + isGameStarted);
				
				//Close the file
				writer.close();
			}catch(Exception error){System.out.println("Fatal Error: " + error.getMessage());}
	}//End of save method
	
	/**
	 *Loads the game of survivor from disk
	 */
	private void load()
	{
		File playerFile = new File("players");
		File contestantFile = new File("contestants");
		File gameFile = new File("game");
		
		//If the players file exists, load the last saved players
		if (playerFile.exists())
		{
			//Load the 
			try{
				FileInputStream mystream = new FileInputStream("players");
				DataInputStream input = new DataInputStream(mystream);
				BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
				
				String line = buffer.readLine();
				while(line != null)
				{
					players.add(new Player(line));
					line = buffer.readLine();
				}
				input.close();
			}catch(Exception error){System.out.println("Fatal Error:" + error.getMessage() );}
		}
		
		//If the contestants file exists, load the last saved contestants
		if (contestantFile.exists())
		{
			
			//Load the 
			try{
				FileInputStream mystream = new FileInputStream("contestants");
				DataInputStream input = new DataInputStream(mystream);
				BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
				
				String line = buffer.readLine();
				
				while(line != null)
				{
					contestants.add(new Contestant(line));
					line = buffer.readLine();
				}
				input.close();
			}catch(Exception error){System.out.println("Fatal Error:" + error.getMessage() );}
		}	
		
		//If the game file exists, load the last saved game settings
		if (gameFile.exists()) {
			//Load the game file and information
			try {
				FileInputStream mystream = new FileInputStream("game");
				DataInputStream input = new DataInputStream(mystream);
				BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
					
				String line = buffer.readLine();
				
				if(line != null) {
					currentRound = Integer.parseInt(line.split(",")[0]);
					totalRounds = Integer.parseInt(line.split(",")[1]);
					betAmount = Integer.parseInt(line.split(",")[2]);
					colourScheme = ColourScheme.valueOf(line.split(",")[3]);
					isGameStarted = Boolean.parseBoolean((line.split(",")[4]));
				}

				input.close();
			} catch(Exception error){System.out.println("Fatal Error:" + error.getMessage() );}
		}//End of if
	}//End of load method
	
	/**
	 *For debugging only, prints the state of the game when called - will be deleted in final release
	 */
	protected void debug() {
		System.out.println("---------------------STATUS OF SURVIVOR GAME----------------------");
		System.out.println("Current Round: \t" + currentRound);
		System.out.println("Total Rounds: \t" + totalRounds);
		System.out.println("Bet Amounts: \t" + betAmount);
		System.out.println("Colour Scheme: \t" + colourScheme);
		
		System.out.println("\n*****Players:");
		for(int x = 0; x < players.size(); x++)
			System.out.println(players.get(x).toString());
		
		System.out.println("\n*****Contestants:");
		for(int x = 0; x < contestants.size(); x++)
			System.out.println(contestants.get(x).toString());
		
		System.out.println("\n---------------------END OF STATUS----------------------");
	}
}//End of class