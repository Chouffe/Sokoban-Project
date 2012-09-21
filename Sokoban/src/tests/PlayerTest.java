package tests;

import static org.junit.Assert.*;


import model.Player;
import model.Position;

import org.junit.Before;
import org.junit.Test;



public class PlayerTest 
{
	Position posPlayer1;
	Position posPlayer2;
	Player player1;
	Player player2;
	
	@Before
	public final void setUp()
	{
		posPlayer1 = new Position(1,1);
		posPlayer2 = new Position(2,2);
		
		player1 = new Player(posPlayer1, true);
		player2 = new Player(posPlayer2, false);
		
	}
	
	@Test
	public final void testCloning() throws CloneNotSupportedException
	{
		assertEquals(player1.clone().getPosition(), player1.getPosition());
		assertEquals(player1.clone().isOnGoal(), player1.isOnGoal());
	}
	
	@Test
	public final void testEquals() throws CloneNotSupportedException
	{
		assertEquals(player1.clone(), player1);
		assertEquals(new Player(posPlayer1, true), player1);
		assertFalse(new Player(posPlayer1, false).equals(player1));
		assertFalse(player1.equals(player2));
	}
	
	
}
