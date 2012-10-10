package tests;

import static org.junit.Assert.*;

import model.Box;
import model.BoxToGoalPath;
import model.Position;

import org.junit.Test;

public class BoxToGoalPathTest {

	@Test
	public void testInitialize() throws CloneNotSupportedException
	{
		BoxToGoalPath b1 = new BoxToGoalPath(new Box(new Position(1,1), false), new Position(1,2));
		
		assertEquals(new Position(1,1), b1.getBoxPosition());
		assertEquals(new Position(1,2), b1.getGoalPosition());
	}
	
	@Test
	public void testCloning() throws CloneNotSupportedException {
		BoxToGoalPath b1 = new BoxToGoalPath(new Box(new Position(1,1), false), new Position(1,2));
		BoxToGoalPath b2 = new BoxToGoalPath(new Box(new Position(1,2), false), new Position(1,3));
		BoxToGoalPath b1clone = b1.clone();
		
		assertEquals(b1.getBoxPosition(), b1clone.getBoxPosition());
		assertEquals(b1.getGoalPosition(), b1clone.getGoalPosition());
		assertEquals(b1.hashCode(), b1clone.hashCode());
		assertEquals(b1, b1clone);
		
		b1clone.setBoxPosition(new Position(3,3));
		assertFalse(b1.equals(b1clone));
		assertFalse(b1.equals(b2));
	}
	
	@Test
	public void testHashCode() throws CloneNotSupportedException {
		BoxToGoalPath b1 = new BoxToGoalPath(new Box(new Position(1,1), false), new Position(1,2));
		BoxToGoalPath b2 = new BoxToGoalPath(new Box(new Position(1,1), false), new Position(1,2));
		
		assertEquals(b1.hashCode(), b2.hashCode());
		assertEquals(b1, b2);
	}
	
	@Test
	public void testRemoveFirstPlayerMoves() throws CloneNotSupportedException {
		String playerMoves1 = "uduRlU";
		String playerMoves2 = "udlr";
		String playerMoves3 = "URL";
		
		assertEquals("RlU", BoxToGoalPath.removeFirstPlayerMoves(playerMoves1));
		assertEquals("", BoxToGoalPath.removeFirstPlayerMoves(playerMoves2));
		assertEquals("URL", BoxToGoalPath.removeFirstPlayerMoves(playerMoves3));
	}
	
	
	
	

}
