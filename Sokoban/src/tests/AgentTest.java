package tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.AStarSearch;
import model.Agent;
import model.Cell;
import model.Map;
import model.Position;
import model.Cell.ECell;

import org.junit.Before;
import org.junit.Test;

import exception.PathNotFoundException;


public class AgentTest {
	
	Agent agent;
	BufferedReader br;
	
	@Before
	public void setUp()
	{
		agent = new Agent();
		br = null;
	}
	
	/**
	 * Tests the distance function
	 * @throws CloneNotSupportedException
	 */
	@Test
	public final void testDistance() throws CloneNotSupportedException
	{
		// Test the distance(Position, Position)
		Position position1 = new Position();
		Position position2 = new Position(1,0);
		Position position3 = new Position(0,1);
		Position position4 = new Position(1,1);
		Position position5 = new Position(2,2);
		
		assertEquals(agent.distance(position1, position2), 1);
		assertEquals(agent.distance(position1, position3), 1);
		assertEquals(agent.distance(position1, position4), 2);
		assertEquals(agent.distance(position1, position5), 8);
		
		// Test the distance(Position, ArrayList<Position>)
		ArrayList<Position> positions = new ArrayList<Position>();
		positions.add(position2.clone());
		positions.add(position5.clone());
		
		assertEquals(agent.distance(position1, positions), 1);
		
		positions = new ArrayList<Position>();
		positions.add(position4.clone());
		positions.add(position5.clone());
		
		assertEquals(agent.distance(position1, positions), 2);
		
		positions = new ArrayList<Position>();
		positions.add(position2.clone());
		positions.add(position3.clone());
		
		assertEquals(agent.distance(position1, positions), 1);
		
		positions = new ArrayList<Position>();
		positions.add(position1.clone());
		
		assertEquals(agent.distance(position1, positions), 0);
		
		positions = new ArrayList<Position>();
		
		assertEquals(agent.distance(position1, positions), -1);
		
	}
	
//	@Test
//	public final void testEmptySpaces() throws CloneNotSupportedException
//	{
//		
//		try
//		{
//			br = new BufferedReader(new FileReader("src/tests/maps/map2.txt"));
//			
//			Map map = new Map(br);
//			Position position = new Position(1,2);
//			Position position2 = new Position(1,3);
//			Position position3 = new Position(2,4);
//			
//			// test with a Player
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(position, map).size());
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(position, map, ECell.PLAYER).size());
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(position2, map).size());
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(position2, map, ECell.PLAYER).size());
//			assertEquals(3, agent.getAstar().findEmptySpacesAround(position3, map).size());
//			assertEquals(3, agent.getAstar().findEmptySpacesAround(position3, map, ECell.PLAYER).size());
//			
//			// Test with a Box
//			Position positionBox1 = new Position(1,2);
//			Position positionBox2 = new Position(1,3);
//			Position positionBox3 = new Position(2,4);
//			Position positionBox4 = new Position(3,4);
//			Position positionBox5 = new Position(4,4);
//			Position positionBox6 = new Position(4,2);
//			
//			assertEquals(0, agent.getAstar().findEmptySpacesAround(positionBox1, map, ECell.BOX).size());
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(positionBox2, map, ECell.BOX).size());
//			assertEquals(2, agent.getAstar().findEmptySpacesAround(positionBox3, map, ECell.BOX).size());
//			assertEquals(2, agent.getAstar().findEmptySpacesAround(positionBox4, map, ECell.BOX).size());
//			assertEquals(0, agent.getAstar().findEmptySpacesAround(positionBox5, map, ECell.BOX).size());
//			assertEquals(0, agent.getAstar().findEmptySpacesAround(positionBox6, map, ECell.BOX).size());
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/map6.txt"));
//			Map map2 = new Map(br);
//			
//			Position position4 = map2.getPlayerPosition();
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(position4, map2).size());
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(map2.getPlayerPosition(), map2, ECell.PLAYER).size());
//			
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/map8.txt"));
//			
//			Map map3 = new Map(br);
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(map3.getPlayerPosition(), map3).size());
//			assertEquals(1, agent.getAstar().findEmptySpacesAround(map3.getPlayerPosition(), map3, ECell.PLAYER).size());
//			
//			
//			
//			
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (br != null)br.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//		
//	}

	
		
	@Test
	public final void testFindPath() throws CloneNotSupportedException, PathNotFoundException
	{
		try
		{
			// Test easy Pathfinding
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath1.txt"));
			Map map = new Map(br);
			
			assertEquals(agent.getAstar().findPath(map, new Position(1,1), new Position(4,1), Cell.ECell.PLAYER).toString(), "DDD");
			assertEquals(agent.getAstar().findPath(map, new Position(1,2), new Position(1,2), Cell.ECell.PLAYER).toString(), "");
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath2.txt"));
		
			map = new Map(br);
			
			assertEquals(agent.getAstar().findPath(map, new Position(1,1), new Position(6,6), Cell.ECell.PLAYER).toString(), "DDRRRRRDDD");
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
		
			agent.setMap(br);
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
		
			map = new Map(br);
			assertEquals(agent.getAstar().findPath(map, new Position(1,3), new Position(12,17), Cell.ECell.PLAYER).toString(), "RRRRRRDDDDRRRRUURRDDRRDDDDLLDDRRD");
		
			
			br = new BufferedReader(new FileReader("src/tests/maps/map11.txt"));
		
			map = new Map(br);
			assertEquals(agent.getAstar().findPath(map, map.getPlayerPosition(), new Position(1,14), Cell.ECell.PLAYER).toString(), "RRRRRRRRRRRRR");
			
			assertEquals(agent.getAstar().findPath(map, new Position(5,1), new Position(1,14), Cell.ECell.PLAYER).toString(), "RRRRRRRRRURURURUR");
			assertEquals(agent.getAstar().findPath(map, new Position(4,2), new Position(1,14), Cell.ECell.BOX).toString(), "RRRRRRRRRURURUR");
			
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
	public final void testSetCellAccessible()
	{
		try
		{
			
			// TODO : add some assert
			br = new BufferedReader(new FileReader("src/tests/maps/path/map1.txt"));
			Map map = new Map(br);
			//System.out.println(map);
			
			agent.setCellAccessible(map).toStringAccessible();
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/map7.txt"));
			map = new Map(br);
			//System.out.println(map);
			
			//agent.setCellAccessible(map).toStringAccessible();
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-server5.txt"));
			map = new Map(br);
			//System.out.println(map);
			
			//agent.setCellAccessible(map).toStringAccessible();
			
			
			//assertEquals("", "");
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
	public final void testPathExist() throws CloneNotSupportedException, PathNotFoundException, IOException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/pathExist/map1.txt"));
		Map map = new Map(br);
		String[] paths = new String[2];
	
		assertEquals(false, agent.pathExists(map, paths, 0, 0));
		assertEquals(true, agent.pathExists(map, paths, 1, 1));
		
		assertTrue(paths[1].length() > 0);
		assertEquals(null, paths[0]);
	}
	
	@Test
	public final void testSolving() throws CloneNotSupportedException, PathNotFoundException
	{
		try
		{
			
			br = new BufferedReader(new FileReader("src/tests/maps/solve/map1.txt"));
			Map map = new Map(br);
			assertEquals("DDLDLURULLULDD", agent.solve(map));
			
			br = new BufferedReader(new FileReader("src/tests/maps/solve/map2.txt"));
			map = new Map(br);
			// Failure...
			//agent.solve(map);
			
			br = new BufferedReader(new FileReader("src/tests/maps/solve/map3.txt"));
			map = new Map(br);
			//agent.solve(map);
			
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/solve.txt"));
			map = new Map(br);
			//System.out.println(map);

			
			assertEquals("RRRRDRRRRRRRR", agent.solve(map));
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/solve2.txt"));
			map = new Map(br);
			//System.out.println(map);
			
			assertEquals("RRRRRRRRRRRRLLLLLLLDRRRRRRR", agent.solve(map));
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/solve3.txt"));
			map = new Map(br);
			//System.out.println(map);
			
			agent.solve(map);
			//agent.SolveBoardMoves(agent.solve(map), map);
			
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
