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
		
		moves = new Moves("UUDDLRLR");
		assertEquals(moves.getMoves().get(0), EMove.UP);
		assertEquals(moves.getMoves().get(1), EMove.UP);
		assertEquals(moves.getMoves().get(2), EMove.DOWN);
		assertEquals(moves.getMoves().get(3), EMove.DOWN);
		assertEquals(moves.getMoves().get(4), EMove.LEFT);
		assertEquals(moves.getMoves().get(5), EMove.RIGHT);
		assertEquals(moves.getMoves().get(6), EMove.LEFT);
		assertEquals(moves.getMoves().get(7), EMove.RIGHT);
		assertEquals(8, moves.getMoves().size());
		
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
	
	@Test
	public final void testGetPositionFromInitialPositionAndMove() throws CloneNotSupportedException
	{
		Position init = new Position(1, 2);
		
		Position expected = new Position(1, 3);
		assertEquals(expected, Moves.getPositionFromInitialPositionAndMove(init, EMove.RIGHT));
		
		expected = new Position(2,2);
		assertEquals(expected, Moves.getPositionFromInitialPositionAndMove(init, EMove.DOWN));
		
		expected = new Position(1,1);
		assertEquals(expected, Moves.getPositionFromInitialPositionAndMove(init, EMove.LEFT));
		
		expected = new Position(0,2);
		assertEquals(expected, Moves.getPositionFromInitialPositionAndMove(init, EMove.UP));
	}
	
	@Test
	public final void testAddMovesWithString()
	{
		String moves = "UUDLRU";
		Moves m = new Moves();
		m.addMoves(moves);
		
		assertEquals(6, m.getMoves().size());
		
		m = new Moves();
		m.addMoves("");
		
		assertEquals(0, m.getMoves().size());
	}
}
