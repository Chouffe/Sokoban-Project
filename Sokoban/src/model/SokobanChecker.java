package model;


/**
 * 
 * Test if a i=given String is a solution
 * @author arthur
 *
 */
public class SokobanChecker 
{
	
	public static boolean check(Map map, String solution)
	{
		for(char c : solution.toCharArray())
		{
			switch(c)
			{
			case 'U':
				break;
			case 'D':
				break;
			case 'L':
				break;
			case 'R':
				break;
			}
		}
		return false;
	}
}
