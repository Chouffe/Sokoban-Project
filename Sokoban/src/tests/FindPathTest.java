package tests;

import static org.junit.Assert.*;
import AStarSearch;
import IllegalMoveException;
import Map;
import PathNotFoundException;
import SokobanChecker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.junit.Before;
import org.junit.Test;

import Cell.ECell;


public class FindPathTest {

	AStarSearch astar;
	BufferedReader br;
	Map map;
	
	@Before
	public final void setUp()
	{
		astar = new AStarSearch();
		br = null;
		map = null;
	}
	
	@Test
	public void testFindPathEasy() throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException 
	{
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/easy/map1.txt"));
		Map map = new Map(br);
		
		astar.findPath(map, map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX);
		assertEquals("RR", astar.getFinalString().toString());
		
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/easy/map2.txt"));
		map = new Map(br);
		
		astar.findPath(map, map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX);
		assertEquals("rRR", astar.getFinalString().toString());
		
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/easy/map3.txt"));
		map = new Map(br);
		
		astar.findPath(map, map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX);
		assertEquals("rRR", astar.getFinalString().toString());
		
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/easy/map4.txt"));
		map = new Map(br);
		
		
		assertEquals("rRRRRRR", astar.findPath(map, map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX));
		
	}
	
	@Test
	public void testFindPathMedium() throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException 
	{
		// Test 1
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/medium/map1.txt"));
		Map map = new Map(br);
		
		astar.findPath(map, map.getBoxes().get(2).getPosition(), map.getGoals().get(2), ECell.BOX);
		assertEquals("lllulluRRR", astar.getFinalString().toString());
		
		// Test 2
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/medium/map2.txt"));
		map = new Map(br);
		
		astar.findPath(map, map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX);
		assertEquals("llllDD", astar.getFinalString().toString());
		
		// Try to solve the map
		assertEquals(true, SokobanChecker.mapIsSolved(map, astar.getFinalString().toString()));
		
	}
	
	@Test
	public final void testFindPathDifficult() throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException
	{
		// Test 3
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/difficult/map1.txt"));
		map = new Map(br);
		
		astar.findPath(map, map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX);
		assertEquals(true, SokobanChecker.mapIsSolved(map, astar.getFinalString().toString()));
		
		br = new BufferedReader(new FileReader("src/tests/maps/findpath/difficult/map2.txt"));
		map = new Map(br);
		
		astar.findPath(map, map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX);
		assertEquals(true, SokobanChecker.mapIsSolved(map, astar.getFinalString().toString()));
				
	}
	
	

}
