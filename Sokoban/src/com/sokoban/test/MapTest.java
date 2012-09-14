package com.sokoban.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.sokoban.model.ECell;
import com.sokoban.model.Map;
import com.sokoban.model.Position;

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
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map1.txt"));
			
			Map map2 = new Map(br);
			
			assertEquals(map2.getMap().size(), 4);
			assertEquals(map2.getMap().get(0).get(0), ECell.VISITED);
			assertEquals(map2.getMap().get(1).get(0), ECell.WALL);
			assertEquals(map2.getMap().get(2).get(0), ECell.PLAYER);
			assertEquals(map2.getMap().get(2).get(1), ECell.PLAYER_ON_GOAL_SQUARE);
			assertEquals(map2.getMap().get(2).get(2), ECell.BOX);
			assertEquals(map2.getMap().get(2).get(3), ECell.BOX_ON_GOAL);
			
			assertEquals(map2.getGoals().size(), 2);
			
			// Test on a more tricky map
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map9.txt"));
			
			Map map3 = new Map(br);
			
			assertEquals(map3.isPositionOnTheMap(new Position(3, 3)), true);
			assertEquals(map3.isPositionOnTheMap(new Position(1, 4)), true);
			assertEquals(map3.getCellFromPosition(new Position(3, 3)), ECell.EMPTY_FLOOR);
			
			
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
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map2.txt"));
			Map map = new Map(br);
			
			Map map2 = map.clone();
			
			// Test player Position
			map2.getPlayerPosition().setI(2);
			map2.getPlayerPosition().setJ(2);
			
			assertFalse(map.getPlayerPosition().getI() == map2.getPlayerPosition().getI());
			assertFalse(map.getPlayerPosition().getJ() == map2.getPlayerPosition().getJ());
			
			// Test goals
			map2.getGoals().clear();
			map2.getGoals().add(map2.getPlayerPosition());
			
			assertFalse(map.getGoals().equals(map2.getGoals()));
			
			// Test Cells
			
			Position pos = new Position(1, 1);
			map2.set(ECell.VISITED, pos);
			
			assertTrue(map2.getMap().get(pos.getI()).get(pos.getJ()).equals(ECell.VISITED));
			
			pos.setI(2);
			pos.setJ(2);
			map2.set(ECell.GOAL_SQUARE, pos);
			
			assertTrue(map2.getMap().get(pos.getI()).get(pos.getJ()).equals(ECell.GOAL_SQUARE));
			
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
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map2.txt"));
			
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
			assertEquals(map.getCellFromPosition(position3), ECell.EMPTY_FLOOR);
			assertEquals(map.getCellFromPosition(position2), ECell.EMPTY_FLOOR);
			assertEquals(map.getCellFromPosition(position), ECell.EMPTY_FLOOR);
			
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
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map3.txt"));
			
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
	 */
	@Test
	public final void testPositionPlayer() throws CloneNotSupportedException
	{
				
		try
		{
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map4.txt"));
			Map map1 = new Map(br);
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map5.txt"));
			Map map2 = new Map(br);
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/com/sokoban/test/maps/map6.txt"));
			Map map3 = new Map(br);
			
			assertTrue(map1.getPlayerPosition().getI() == 0);
			assertTrue(map1.getPlayerPosition().getJ() == 2);
			
			assertTrue(map2.getPlayerPosition().getI() == 0);
			assertTrue(map2.getPlayerPosition().getJ() == 2);
			
			assertTrue(map3.getPlayerPosition().getI() == 8);
			assertTrue(map3.getPlayerPosition().getJ() == 11);
			
			Position position = new Position(7,11);
			Position visited = new Position(8,11);
			map3.movePlayer(position);
			
			assertEquals(map3.getCellFromPosition(position), ECell.PLAYER);
			assertEquals(map3.getCellFromPosition(visited), ECell.VISITED);
		
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
