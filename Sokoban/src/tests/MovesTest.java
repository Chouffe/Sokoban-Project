package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import model.EMove;
import model.Moves;
import model.Position;

import org.junit.Before;
import org.junit.Test;


public class MovesTest 
{
	
	Moves moves;
	
	Position position1;
	Position position2;
	Position position3;
	Position position4;
	
	@Before
	public void setUp()
	{
		moves = new Moves();
		position1 = new Position(1,1);
		position2 = new Position(1,0);
		position3 = new Position(0,1);
		position4 = new Position(0,0);
	}
	
	/**
	 * Tests the creation of Moves
	 */
	@Test
	public final void testCreationMoves()
	{
		assertFalse(moves.getMoves() == null);
		assertEquals(moves.getMoves().size(), 0);
	}
	
	/**
	 * addMove() functionality tested
	 */
	@Test
	public final void testAddMoves()
	{
		
		Moves moves2 = new Moves();
		
		moves.up();
		moves.down();
		moves.left();
		moves.right();
		
		assertEquals(moves.toString(), "UDLR");
		
		moves2.addMove(position1, position4);
		assertEquals(moves2.getMoves().size(), 0);
		
		moves2.addMove(position1, position2);
		assertEquals(moves2.getMoves().size(), 1);
		assertEquals(moves2.getMoves().get(0), EMove.LEFT);
		
		moves2.clear();
		moves2.addMove(position1, position3);
		assertEquals(moves2.getMoves().size(), 1);
		assertEquals(moves2.getMoves().get(0), EMove.UP);
		
		moves2.clear();
		moves2.addMove(position2, position1);
		assertEquals(moves2.getMoves().size(), 1);
		assertEquals(moves2.getMoves().get(0), EMove.RIGHT);
		
		moves2.clear();
		moves2.addMove(position3, position1);
		assertEquals(moves2.getMoves().size(), 1);
		assertEquals(moves2.getMoves().get(0), EMove.DOWN);
	}
}
