package tests;


import static org.junit.Assert.*;
import Map;
import Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import org.junit.Before;
import org.junit.Test;

import exception.IllegalMoveException;

public class PositionTest 
{
	Position position1;
	Position position2;
	Position position3;
	Position position4;
	
	@Before
	public void setUp() throws Exception
	{
		// We initialize some positions
		position1 = new Position();
		position2 = new Position(1,1);
		position3 = new Position(0,2);
		position4 = new Position(3,0);
	}
	
	/**
	 * Tests the creation of a Position
	 */
	@Test
	public final void testCreationPosition()
	{
				
		assertEquals(position1.getI(), 0);
		assertEquals(position1.getJ(), 0);
		assertEquals(position2.getI(), 1);
		assertEquals(position2.getJ(), 1);
		assertEquals(position3.getI(), 0);
		assertEquals(position3.getJ(), 2);
		assertEquals(position4.getI(), 3);
		assertEquals(position4.getJ(), 0);
		
		
	}
	
	/**
	 * Test the cloning
	 * @throws CloneNotSupportedException
	 */
	@Test
	public final void testClonePosition() throws CloneNotSupportedException
	{
		
		Position clone = position1.clone();
		
		clone.setI(10);
		clone.setJ(12);
		
		assertFalse(clone.getI() == position1.getI());
		assertFalse(clone.getJ() == position1.getJ());
		assertEquals(0, position1.getI());
		assertEquals(0, position1.getJ());
	}
	
	/**
	 * Tests moving positions
	 */
	@Test
	public final void testMovingPosition()
	{
		
		Position position1 = new Position();
		Position position2 = new Position(1,1);
		Position position3 = new Position(0,2);
		Position position4 = new Position(3,0);
		
		BufferedReader br = null;
		
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map3.txt"));
			
			Map map = new Map(br);
			
			// Test the up
			position1.up(map);
			position2.up(map);
			position3.up(map);
			position4.up(map);
			
			assertEquals(0, position1.getI());
			assertEquals(0, position1.getJ());
			assertEquals(0, position2.getI());
			assertEquals(1, position2.getJ());
			assertEquals(0, position3.getI());
			assertEquals(2, position3.getJ());
			assertEquals(2, position4.getI());
			assertEquals(0, position4.getJ());
			
			position4.up(map);
			
			assertEquals(1, position4.getI());
			assertEquals(0, position4.getJ());
			
			// Test the down
			position1 = new Position();
			position2 = new Position(1,1);
			position3 = new Position(0,2);
			position4 = new Position(3,0);
			
			position1.down(map);
			position2.down(map);
			position3.down(map);
			position4.down(map);
			
			assertEquals(1, position1.getI());
			assertEquals(0, position1.getJ());
			assertEquals(2, position2.getI());
			assertEquals(1, position2.getJ());
			assertEquals(1, position3.getI());
			assertEquals(2, position3.getJ());
			assertEquals(3, position4.getI());
			assertEquals(0, position4.getJ());
			
			position3.down(map);
			assertEquals(2, position3.getI());
			assertEquals(2, position3.getJ());
			
			// Test the left
			position1 = new Position();
			position2 = new Position(1,1);
			position3 = new Position(0,2);
			position4 = new Position(3,0);
			
			position1.left(map);
			position2.left(map);
			position3.left(map);
			position4.left(map);
			
			assertEquals(0, position1.getI());
			assertEquals(0, position1.getJ());
			assertEquals(1, position2.getI());
			assertEquals(0, position2.getJ());
			assertEquals(0, position3.getI());
			assertEquals(1, position3.getJ());
			assertEquals(3, position4.getI());
			assertEquals(0, position4.getJ());
			
			position3.left(map);
			
			assertEquals(0, position3.getI());
			assertEquals(0, position3.getJ());
			
			// Test the right
			position1 = new Position();
			position2 = new Position(1,1);
			position3 = new Position(0,2);
			position4 = new Position(3,0);
			
			position1.right(map);
			position2.right(map);
			position3.right(map);
			position4.right(map);
			
			assertEquals(0, position1.getI());
			assertEquals(1, position1.getJ());
			assertEquals(1, position2.getI());
			assertEquals(2, position2.getJ());
			assertEquals(0, position3.getI());
			assertEquals(3, position3.getJ());
			assertEquals(3, position4.getI());
			assertEquals(1, position4.getJ());
			
			position3.right(map);
			
			assertEquals(0, position3.getI());
			assertEquals(4, position3.getJ());
			
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	@Test
	public final void testEquals()
	{
		Position position4 = new Position(position1.getI(), position1.getJ());
		assertTrue(position4.equals(position1));
		assertEquals(position4, position1);
		
		assertFalse(position2 == position4);
	}
	
	@Test
	public final void testMovingPositionWithPlayer() throws IllegalMoveException, CloneNotSupportedException
	{
		
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/position/map1.txt"));
			
			Map map = new Map(br);
			
			System.out.println(map);
			//System.out.println(map.getPlayerPosition().right(map));
			System.out.println(map.set(map.getPlayer(), map.getPlayerPosition().clone().right(map), false));
			//System.out.println(map);
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
