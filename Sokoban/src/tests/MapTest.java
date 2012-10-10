package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.Box;
import model.Cell;
import model.EMove;
import model.Map;
import model.Player;
import model.Position;
import model.Cell.ECell;

import org.junit.Before;
import org.junit.Test;

import exception.IllegalMoveException;


public class MapTest 
{
	BufferedReader br;
	
	@Before
	public void setUp()
	{
		br = null;
	}
	
	@Test
	public final void testCreationMap()
	{
		
		// Empty Map
		Map map1 = new Map();
		assertEquals(0, map1.getMap().size());
		assertFalse(null == map1.getMap());
		
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map1.txt"));
			
			Map map2 = new Map(br);
			
			assertEquals(map2.getMap().size(), 4);
			assertEquals(map2.getMap().get(0).get(0).getType(), Cell.ECell.VISITED);
			assertEquals(map2.getMap().get(1).get(0).getType(), Cell.ECell.WALL);
			assertEquals(map2.getMap().get(2).get(0).getType(), Cell.ECell.PLAYER);
			assertEquals(map2.getCellFromPosition(map2.getPlayerPosition()).getType(), Cell.ECell.PLAYER_ON_GOAL_SQUARE);
			assertEquals(map2.getMap().get(2).get(1).getType(), Cell.ECell.PLAYER_ON_GOAL_SQUARE);
			assertEquals(map2.getMap().get(2).get(2).getType(), Cell.ECell.BOX);
			assertEquals(map2.getMap().get(2).get(3).getType(), Cell.ECell.BOX_ON_GOAL);
			
			assertEquals(map2.getGoals().size(), 2);
			
			// Test on a more tricky map
			br = new BufferedReader(new FileReader("src/tests/maps/map9.txt"));
			
			Map map3 = new Map(br);
			
			assertEquals(map3.isPositionOnTheMap(new Position(3, 3)), true);
			assertEquals(map3.isPositionOnTheMap(new Position(1, 4)), true);
			assertEquals(map3.getCellFromPosition(new Position(3, 3)).getType(), Cell.ECell.EMPTY_FLOOR);
			
			
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
	
	/**
	 * Tests the cloning
	 */
	@Test
	public final void testClone()
	{	
		
		try {
			
			br = new BufferedReader(new FileReader("src/tests/maps/map2.txt"));
			Map map = new Map(br);
			
			Map map2 = map.clone();
			
			assertEquals(map.getPlayer(), map2.getPlayer());
			
			// Test player Position
			map2.getPlayerPosition().setI(2);
			map2.getPlayerPosition().setJ(3);
			
			assertFalse(map.getPlayer() == map2.getPlayer());
			assertFalse(map.getPlayerPosition().getI() == map2.getPlayerPosition().getI());
			assertFalse(map.getPlayerPosition().getJ() == map2.getPlayerPosition().getJ());
			
			// Test goals
			map2.getGoals().clear();
			map2.getGoals().add(map2.getPlayerPosition());
			
			assertFalse(map.getGoals().equals(map2.getGoals()));
			
			// Test Cells
			Position pos = new Position(1, 1);
			map2.set(Cell.ECell.VISITED, pos);
			
			assertTrue(map2.getMap().get(pos.getI()).get(pos.getJ()).getType().equals(Cell.ECell.VISITED));
			
			pos.setI(2);
			pos.setJ(2);
			map2.set(Cell.ECell.GOAL_SQUARE, pos);
			
			assertTrue(map2.getMap().get(pos.getI()).get(pos.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE));
			
		} 
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public final void testMap()
	{
		
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map2.txt"));
			
			Map map = new Map(br);
			Position position = new Position(1,2);
			Position position2 = new Position(1,3);
			Position position3 = new Position(2,4);
			Position position4 = new Position(10,0);
			
			assertEquals(true, map.isPositionOnTheMap(position));
			assertEquals(true, map.isPositionOnTheMap(position2));
			assertEquals(true, map.isPositionOnTheMap(position3));
			assertEquals(false, map.isPositionOnTheMap(position4));
				
			assertEquals(map.getCellFromPosition(position4), null);
			assertEquals(map.getCellFromPosition(position3).getType(), Cell.ECell.EMPTY_FLOOR);
			assertEquals(map.getCellFromPosition(position2).getType(), Cell.ECell.EMPTY_FLOOR);
			assertEquals(map.getCellFromPosition(position).getType(), Cell.ECell.PLAYER);
			
			// TODO : test the others cells
			
			
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
	
	/**
	 * Test if a position is located on the map
	 */
	@Test
	public final void testIsPositionOnTheMap()
	{
		
		BufferedReader br = null;
		
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map3.txt"));
			
			Map map = new Map(br);
			
			Position position = new Position(0,2);
			Position position2 = new Position(1,0);
			Position position3 = new Position(2,2);
			Position position4 = new Position(3,3);
			Position position5 = new Position(3,5);
			Position position6 = new Position(3,6);
			Position position7 = new Position(4,0);
			
			assertEquals(map.isPositionOnTheMap(position), true);
			assertEquals(map.isPositionOnTheMap(position2), true);
			assertEquals(map.isPositionOnTheMap(position3), true);
			assertEquals(map.isPositionOnTheMap(position4), true);
			assertEquals(map.isPositionOnTheMap(position5), true);
			assertEquals(map.isPositionOnTheMap(position6), false);
			assertEquals(map.isPositionOnTheMap(position7), false);
			
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
	
	/**
	 * Tests if the player Position is correctly handled
	 * @throws CloneNotSupportedException
	 * @throws IllegalMoveException 
	 */
	@Test
	public final void testPositionPlayer() throws CloneNotSupportedException, IllegalMoveException
	{
				
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map4.txt"));
			Map map1 = new Map(br);
			
			br = new BufferedReader(new FileReader("src/tests/maps/map5.txt"));
			Map map2 = new Map(br);
			
			br = new BufferedReader(new FileReader("src/tests/maps/map6.txt"));
			Map map3 = new Map(br);
			
			assertTrue(map1.getPlayerPosition().getI() == 0);
			assertTrue(map1.getPlayerPosition().getJ() == 2);
			
			assertTrue(map2.getPlayerPosition().getI() == 0);
			assertTrue(map2.getPlayerPosition().getJ() == 2);
			
			assertTrue(map3.getPlayerPosition().getI() == 8);
			assertTrue(map3.getPlayerPosition().getJ() == 11);
		
		
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
	public final void testSettingMap() throws IllegalMoveException, CloneNotSupportedException
	{
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/setting/map1.txt"));
			Map map1 = new Map(br);
			
			assertEquals(map1.getPlayer().getPosition(), new Position(1,3));
			assertEquals(map1.getBoxes().get(0).getPosition(), new Position(2,3));
			assertEquals(map1.getPlayer().isOnGoal(), false);
			
			
			// We make the move : it updates the map
			map1.set(map1.getPlayer(), map1.getGoals().get(0), false);
			
			assertEquals(map1.getPlayer().isOnGoal(), true);
			assertEquals(map1.getCellFromPosition(map1.getPlayer().getPosition()).getType(), ECell.PLAYER_ON_GOAL_SQUARE);
			
			// Empty floor
			Position emptyFloor = new Position(1,3);
			assertEquals(map1.getCellFromPosition(emptyFloor).getType(), ECell.EMPTY_FLOOR);
			
			// We make the move : it updates the map
			map1.set(map1.getPlayer(), map1.getGoals().get(1), false);
			assertEquals(map1.getCellFromPosition(map1.getPlayer().getPosition()).getType(), ECell.PLAYER_ON_GOAL_SQUARE);
			assertEquals(map1.getCellFromPosition(map1.getGoals().get(0)).getType(), ECell.GOAL_SQUARE);
			
			// We make the same move
			map1.set(map1.getPlayer(), map1.getGoals().get(1),false);
			assertEquals(map1.getCellFromPosition(map1.getPlayer().getPosition()).getType(), ECell.PLAYER_ON_GOAL_SQUARE);
			assertEquals(map1.getCellFromPosition(map1.getGoals().get(0)).getType(), ECell.GOAL_SQUARE);
			
			map1.set(map1.getPlayer(), emptyFloor, false);
			assertEquals(map1.getPlayer().isOnGoal(), false);
			
			// We move the box now
			Position oldBoxPosition = new Position(1,4);
			assertEquals(map1.getBoxes().get(0).isOnGoal(), false);
			System.out.println(map1);
			map1.set(map1.getBoxes().get(0), map1.getGoals().get(0), false);
			System.out.print(map1);
			assertEquals(map1.getBoxes().get(0).isOnGoal(), true);
			assertEquals(ECell.EMPTY_FLOOR, map1.getCellFromPosition(oldBoxPosition).getType());
			assertEquals(ECell.BOX_ON_GOAL, map1.getCellFromPosition(map1.getBoxes().get(0).getPosition()).getType());
			
			// Test illegal moves
			try
			{
				map1.set(map1.getPlayer(), new Position(0,0), false);
				map1.set(map1.getBoxes().get(0), new Position(0,1), false);
			}
			catch(IllegalMoveException e)
			{
				
			}
			
			map1.set(map1.getBoxes().get(0), oldBoxPosition, false);
			assertEquals(map1.getBoxes().get(0).isOnGoal(), false);
			
			// We test moves to reach goals
			br = new BufferedReader(new FileReader("src/tests/maps/setting/map3.txt"));
			Map map3 = new Map(br);
			
			map3.set(map3.getPlayer(), map3.getGoals().get(1), false);
			map3.set(map3.getBoxes().get(0), map3.getGoals().get(0), false);
			System.out.println(map3);
			
//			assertTrue(map1.setPlayer(player).getI() == 0);
//			assertTrue(map1.getPlayerPosition().getJ() == 2);
//			
//			assertTrue(map2.getPlayerPosition().getI() == 0);
//			assertTrue(map2.getPlayerPosition().getJ() == 2);
//			
//			assertTrue(map3.getPlayerPosition().getI() == 8);
//			assertTrue(map3.getPlayerPosition().getJ() == 11);
//			
//			Position position = new Position(7,11);
//			Position visited = new Position(8,11);
//			map3.movePlayer(position);
//			
//			assertEquals(map3.getCellFromPosition(position).getType(), Cell.ECell.PLAYER);
//			assertEquals(map3.getCellFromPosition(visited).getType(), Cell.ECell.VISITED);
		
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
	public final void testBoxEquality() throws FileNotFoundException, IllegalMoveException, CloneNotSupportedException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map27.txt"));
		Map map = new Map(br);
		Map clone = map.clone();

		for (Box b : map.getBoxes()) {
			assertEquals(b, map.getBox(b.getPosition()));
			assertEquals(b, clone.getBox(b.getPosition()));
			System.out.println(clone.getBox(b.getPosition()));
		}
	}
		
	
	@Test
	public final void testSet() throws FileNotFoundException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/setting/map2.txt"));
		Map map1 = new Map(br);
		
		Player p = map1.getPlayer();
		Box box1 = map1.getBoxes().get(0);
		System.out.println(box1);
		
		map1.set(box1, new Position(1,3), false);
		
		assertEquals(map1.getBoxes().get(0).getPosition(), new Position(1,3));
		
		System.out.println(map1);
	}
	
	@Test
	public final void testSetFailure() throws FileNotFoundException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/setting/map2.txt"));
		Map map1 = new Map(br);
		
		Player p = map1.getPlayer();
		Box box1 = map1.getBoxes().get(0);
		System.out.println(box1);
		
		try
		{
			map1.set(box1, new Position(2,2), false);
			fail();
		}
		catch(IllegalMoveException e)
		{
			
		}
		
		try
		{
			map1.set(box1, new Position(1,1), false);
			fail();
		}
		catch(IllegalMoveException e)
		{
			
		}
		try
		{
			map1.set(box1, new Position(0,2), false);
			fail();
		}
		catch(IllegalMoveException e)
		{
			
		}
		
		map1.set(box1, new Position(1,2), false);
		
		
		assertEquals(map1.getBoxes().get(0).getPosition(), new Position(1,2));
		
		System.out.println(map1);
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/map2.txt"));
		Map map2 = new Map(br);
		
		map2.set(map2.getBoxes().get(0), new Position(2,3), false);
		map2.set(map2.getPlayer(), new Position(2,2), false);
		
		System.out.println(map2);
	}
	
	@Test
	public final void testApplyOneMoveSuccess() throws FileNotFoundException, IllegalMoveException, CloneNotSupportedException
	{
		
		// Test LEFT && RIGHT
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map1.txt"));
		Map map = new Map(br);
		
		map.applyOneMove(EMove.RIGHT, false);
		map.applyOneMove(EMove.RIGHT, false);
		map.applyOneMove(EMove.RIGHT, false);
		map.applyOneMove(EMove.RIGHT, false);
		map.applyOneMove(EMove.RIGHT, false);
		
		assertEquals(new Position(1,6), map.getPlayerPosition());
		
		map.applyOneMove(EMove.LEFT, false);
		map.applyOneMove(EMove.LEFT, false);
		
		assertEquals(new Position(1,4), map.getPlayerPosition());
		
		// Test UP && DOWN
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map2.txt"));
		map = new Map(br);
		
		map.applyOneMove(EMove.DOWN, false);
		map.applyOneMove(EMove.DOWN, false);
		
		assertEquals(new Position(3,1), map.getPlayerPosition());
		
		map.applyOneMove(EMove.DOWN, false);
		assertEquals(new Position(4,1), map.getPlayerPosition());
		
		map.applyOneMove(EMove.UP, false);
		map.applyOneMove(EMove.UP, false);
		map.applyOneMove(EMove.UP, false);
		
		assertEquals(new Position(1,1), map.getPlayerPosition());
		
		// Test with boxes on the path
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map4.txt"));
		map = new Map(br);
		
		map.applyOneMove(EMove.DOWN, false);
		map.applyOneMove(EMove.DOWN, false);
		
		assertEquals(new Position(3, 5), map.getPlayerPosition());
		
		map.applyOneMove(EMove.LEFT, false);
		map.applyOneMove(EMove.DOWN, false);
		map.applyOneMove(EMove.RIGHT, false);
		
		assertEquals(new Box(new Position(4,6), true), map.getBoxes().get(0));
		
		map.applyOneMove(EMove.RIGHT, false);
		
		assertEquals(true, map.getPlayer().isOnGoal());
		assertEquals(new Box(new Position(4,7), false), map.getBoxes().get(0));
		
		//System.out.println(map.getBoxes());
		
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map6.txt"));
		map = new Map(br);
		
		map.applyOneMove(EMove.UP, false);
		
		
	}
	
	@Test
	public final void testApplyOneMoveFailure() throws FileNotFoundException, IllegalMoveException, CloneNotSupportedException
	{
		// Test LEFT && RIGHT
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map1.txt"));
		Map map = new Map(br);
		
		try
		{
			map.applyOneMove(EMove.UP, false);
			fail("");
		}
		catch(IllegalMoveException e)
		{
		}
		
		try
		{
			map.applyOneMove(EMove.DOWN, false);
			fail("");
		}
		catch(IllegalMoveException e)
		{
		}
			
			
			// Test UP && DOWN
			br = new BufferedReader(new FileReader("src/tests/maps/applymove/map2.txt"));
			map = new Map(br);
			
			try
			{
				map.applyOneMove(EMove.LEFT, false);
				fail("");
			}
			catch(IllegalMoveException e)
			{
			}
			
			try
			{
				map.applyOneMove(EMove.RIGHT, false);
				fail("");
			}
			catch(IllegalMoveException e)
			{
			}
			
//			
//			map.applyOneMove(EMove.RIGHT);
//			
//			assertEquals(new Position(3,1), map.getPlayerPosition());
//			
//			map.applyOneMove(EMove.DOWN);
//			assertEquals(new Position(4,1), map.getPlayerPosition());
//			
//			map.applyOneMove(EMove.UP);
//			map.applyOneMove(EMove.UP);
//			map.applyOneMove(EMove.UP);
//			
//			assertEquals(new Position(1,1), map.getPlayerPosition());
	}
	
	@Test
	public final void testGetBox() throws FileNotFoundException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map4.txt"));
		Map map = new Map(br);
		
		assertEquals(3, map.getBoxes().size());
		Box b = new Box(new Position(2,5), false);
		//System.out.println();
		assertEquals(map.getBox(new Position(2,5)), b);
	}
	
	@Test
	public final void testApplyMoves() throws FileNotFoundException, IllegalMoveException, CloneNotSupportedException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map4.txt"));
		Map map = new Map(br);
		
		assertEquals(3, map.getBoxes().size());
		map.applyMoves("DUDDRRRUULLDDUURRDLLLULDD");
		//System.out.println(map.getBoxes());
		//System.out.println(map.getPlayer());
	}
	
	@Test
	public final void testHashCode() throws FileNotFoundException, CloneNotSupportedException 
	{
		br = new BufferedReader(new FileReader("src/tests/maps/applymove/map4.txt"));
		Map map = new Map(br);
		
		Map map2 = map.clone();
		assertEquals(map2.hashCode(), map.hashCode());
		
		map2.setPlayer(new Player(new Position(1, 1), false));
		assertFalse(map2.hashCode() == map.hashCode());
	}

}
