package application;
/**
 * Class that represents a bonus question created by an admin in a Survivor office pool
 * @author Connor Graham, Jonathan Di Nardo
 *
 */
public class BonusQuestion {

	private boolean isBonusQuestion;	//Stores if the question was or was not a bonus question
	private String question;			// question asked by admin
	private String answer;				// answer to the question asked by admin
	private String[] options;			// solutions to choose from if question is multiple choice
	private int round;					// round in which the question was asked
	
	/**
	 * Constructor that creates a bonus question with a short answer solution
	 * @param question
	 * @param answer
	 * @param round
	 */
	public BonusQuestion(String question, String answer, int round) {
		super();
		this.question = question;
		this.answer = answer;
		this.round = round;
		this.isBonusQuestion = true;
	}

	/**
	 * Constructor that creates a multiple choice bonus question 
	 * @param question
	 * @param answer
	 * @param options
	 * @param round
	 */
	public BonusQuestion(String question, String answer, String[] options,
			int round) {
		super();
		this.question = question;
		this.answer = answer;
		this.options = options;
		this.round = round;
	}
	
	/**
	 * Method that checks a player's solution to a bonus question with the correct answer
	 * @param solution 
	 * @return true if player's guess was correct
	 */
	public boolean isValid(String solution){
		if(solution.compareTo(answer) == 0){
			return true;
		}
		return false;
	}

	
	/**
	 * Method that returns if the Bonus Question is a BonusQuestion
	 * @return true if it is a bonus question, false otherwise
	 */
	public boolean isBonusQuestion() {
		return isBonusQuestion;
	}

	/**
	 * Method that sets if the Bonus Question is a BonusQuestion
	 * @param isBonusQuestion
	 */
	public void setBonusQuestion(boolean isBonusQuestion) {
		this.isBonusQuestion = isBonusQuestion;
	}

	/**
	 * Method that returns the question asked
	 * @return question the state of the question field
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Method that sets the question asked
	 * @param question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Method that returns the correct answer
	 * @return answer the current state of the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Method that sets the correct answer
	 * @param answer
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * Method that returns the options set for a multiple choice question
	 * @return a String array containing the options for answers
	 */
	public String[] getOptions() {
		return options;
	}

	/**
	 * Method that set the options for a multiple choice question
	 * @param options
	 */
	public void setOptions(String[] options) {
		this.options = options;
	}

	/**
	 * Method that returns the round the question was asked
	 * @return round the round that the question was asked
	 */
	public int getRound() {
		return round;
	}

	/**
	 * Method that sets the round the question was asked
	 * @param round
	 */
	public void setRound(int round) {
		this.round = round;
	}
}
