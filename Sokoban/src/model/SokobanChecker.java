package model;

import exception.IllegalMoveException;


/**
 * 
 * Test if a given String is a solution
 * @author arthur
 *
 */
public class SokobanChecker 
{
	
	public static boolean mapIsSolved(Map map, String solution) throws CloneNotSupportedException
	{
		try
		{
			map.applyMoves(solution);
			for(Box b : map.getBoxes())
			{
				if(!b.isOnGoal())
				{
					return false;
				}
			}
			return true;
		}
		catch(IllegalMoveException e)
		{
			return false;
		}
	}
	
}
