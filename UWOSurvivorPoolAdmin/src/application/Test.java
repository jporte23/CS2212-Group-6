package application;
/**
 * Tests TODO
 * @author Jonathan Di Nardo
 */
class Test {
	/**
	 * Test harness.
	 * @param args	Command line arguments.
	 */
	public static void main (String [] args)
	{
		Survivor testSurvivor = new Survivor();
		
		for(int x = 0; x < 26; x++)
			testSurvivor.addContestant(x+"",x+"",x+"",x+"");
		
		for(int x = 0; x < 26; x++)
			testSurvivor.addPlayer(x+"",x+"",x+"");
		
		//testSurvivor.setTheme("test");
		
		testSurvivor.addPlayer(1+"",2+"",1+"");
		
		testSurvivor.save();
		
		testSurvivor.debug();
	}
	
}
