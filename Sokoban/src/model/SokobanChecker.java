package model;

import java.io.IOException;

import exception.IllegalMoveException;
import exception.OffOfMapException;


/**
 * 
 * Test if a given String is a solution
 * @author arthur
 *
 */
public class SokobanChecker 
{
	
	public static boolean mapIsSolved(Map map, String solution) throws CloneNotSupportedException, OffOfMapException
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
	
	public static void showMapSolving(Map map, String solution) throws IllegalMoveException, CloneNotSupportedException, IOException, OffOfMapException
	{
		map = map.clone();
		solution = solution.toUpperCase();
		Moves m = new Moves(solution);
		int M = m.getMoves().size();

		for(int i=0; i<M-1; i++) {
			map.applyOneMove(m.getMoves().get(i), false);
			System.out.println(map);
			System.in.read();
		}
		map.applyOneMove(m.getMoves().get(M-1), true);
	}
	
}
