package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.Agent;
import model.Cell;
import model.Map;
import model.Moves;
import model.Position;
import model.Cell.ECell;
import model.Box;

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
	
	@Test
	public final void heuristic()
	{
		Position start1 = new Position(1,0);
		Position start2 = new Position(0,1);
		Position start3 = new Position(0,2);
		Position start4 = new Position(1,2);
		
		ArrayList<Position> listStarts = new ArrayList<Position>();
		
		listStarts.add(start1);
		listStarts.add(start2);
		listStarts.add(start3);
		listStarts.add(start4);
		
		Position goal = new Position(1,1);
		
		ArrayList<Position> listGoals = new ArrayList<Position>();
		listGoals.add(goal);
		
		
		assertEquals(agent.heuristic(listStarts, listGoals).size(), listStarts.size());
	}
	
	@Test
	public final void testEmptySpaces() throws CloneNotSupportedException
	{
		
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map2.txt"));
			
			Map map = new Map(br);
			Position position = new Position(1,2);
			Position position2 = new Position(1,3);
			Position position3 = new Position(2,4);
			
			// test with a Player
			assertEquals(1, agent.findEmptySpacesAround(position, map).size());
			assertEquals(1, agent.findEmptySpacesAround(position, map, ECell.PLAYER).size());
			assertEquals(2, agent.findEmptySpacesAround(position2, map).size());
			assertEquals(2, agent.findEmptySpacesAround(position2, map, ECell.PLAYER).size());
			assertEquals(3, agent.findEmptySpacesAround(position3, map).size());
			assertEquals(3, agent.findEmptySpacesAround(position3, map, ECell.PLAYER).size());
			
			// Test with a Box
			Position positionBox1 = new Position(1,2);
			Position positionBox2 = new Position(1,3);
			Position positionBox3 = new Position(2,4);
			Position positionBox4 = new Position(3,4);
			Position positionBox5 = new Position(4,4);
			Position positionBox6 = new Position(4,2);
			
			assertEquals(0, agent.findEmptySpacesAround(positionBox1, map, ECell.BOX).size());
			assertEquals(2, agent.findEmptySpacesAround(positionBox2, map, ECell.BOX).size());
			assertEquals(2, agent.findEmptySpacesAround(positionBox3, map, ECell.BOX).size());
			assertEquals(2, agent.findEmptySpacesAround(positionBox4, map, ECell.BOX).size());
			assertEquals(0, agent.findEmptySpacesAround(positionBox5, map, ECell.BOX).size());
			assertEquals(0, agent.findEmptySpacesAround(positionBox6, map, ECell.BOX).size());
			
			br = new BufferedReader(new FileReader("src/tests/maps/map6.txt"));
			Map map2 = new Map(br);
			
			Position position4 = map2.getPlayerPosition();
			assertEquals(1, agent.findEmptySpacesAround(position4, map2).size());
			assertEquals(1, agent.findEmptySpacesAround(map2.getPlayerPosition(), map2, ECell.PLAYER).size());
			
			
			br = new BufferedReader(new FileReader("src/tests/maps/map8.txt"));
			
			Map map3 = new Map(br);
			assertEquals(1, agent.findEmptySpacesAround(map3.getPlayerPosition(), map3).size());
			assertEquals(1, agent.findEmptySpacesAround(map3.getPlayerPosition(), map3, ECell.PLAYER).size());
			
			
			
			
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
	public final void testCreateMapWithVisited() throws CloneNotSupportedException
	{
		
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map7.txt"));
			
			Map map = new Map(br);
			
			Position position = new Position(0,2);
			Position positionPlayer = new Position(1,2);
			
			assertEquals(map.getCellFromPosition(position).getType(), Cell.ECell.PLAYER);
			
			Map map2 = agent.createMapWithVisitedOnThePostion(map, position, positionPlayer);
			
			assertFalse(map.equals(map2));
			assertEquals(map.getMap().size(), map2.getMap().size());
			assertEquals(map2.getCellFromPosition(position).getType(), Cell.ECell.VISITED);
			assertEquals(map2.getCellFromPosition(positionPlayer).getType(), Cell.ECell.PLAYER);
			
			Position position2 = new Position(1,1);
			
			Map map3 = agent.createMapWithVisitedOnThePostion(map2, map2.getPlayerPosition(), position2);
			
			assertEquals(map.getMap().size(), map3.getMap().size());
			assertEquals(map.getMap().get(0).size(), map3.getMap().get(0).size());
			assertFalse(map.equals(map3));
		
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
	 * Tests the pathfinding of the Agent
	 * @throws CloneNotSupportedException
	 */
//	@Test
//	public final void testfindPathToGoal() throws CloneNotSupportedException
//	{
//		
//		try
//		{
//			// Test easy Pathfinding
//			br = new BufferedReader(new FileReader("src/tests/maps/path/map1.txt"));
//			agent.setMap(br);
//			assertEquals(agent.findPathToGoal(new Moves()), "DDD");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/map2.txt"));
//			agent.setMap(br);
//			assertEquals(agent.findPathToGoal(new Moves()), "UUU");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/map3.txt"));
//			agent.setMap(br);
//			assertEquals(agent.findPathToGoal( new Moves()), "RRRR");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/map4.txt"));
//			agent.setMap(br);
//			assertEquals(agent.findPathToGoal(new Moves()), "LLLL");
//			
//			// Test hardPathfinding
//			br = new BufferedReader(new FileReader("src/tests/maps/path/map5.txt"));
//			agent.setMap(br);
//			//Map map5 = new Map(br);
//			
//			assertEquals(agent.findPathToGoal(new Moves()), "DDRRRRR");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/map6.txt"));
//			agent.setMap(br);
//			//Map map6 = new Map(br);
//			
//			assertEquals(agent.findPathToGoal(new Moves()), "DDRRRRRDDD");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/map7.txt"));
//			agent.setMap(br);
//			//Map map7 = new Map(br);
//			
//			assertEquals(agent.findPathToGoal(new Moves()), "DDRRRRRUU");
//			
//			
//			// Test on a more tricky map
//			br = new BufferedReader(new FileReader("src/tests/maps/map9.txt"));
//			agent.setMap(br);
////			Map map8 = new Map(br);
//			assertEquals(agent.findPathToGoal(new Moves()), "DDRRRRR");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/test-server1.txt"));
//			agent.setMap(br);
//	//		Map map9 = new Map(br);
//			System.out.println(agent.findPathToGoal(new Moves()));
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/test-server2.txt"));
//			agent.setMap(br);
//		//	Map map10 = new Map(br);
//			assertEquals(agent.findPathToGoal(new Moves()), "");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/test-server3.txt"));
//			agent.setMap(br);
//			//Map map11 = new Map(br);
//			System.out.println(agent.findPathToGoal(new Moves()));
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/path/test-server5.txt"));
//			agent.setMap(br);
//			//Map map12 = new Map(br);
//			System.out.println(agent.findPathToGoal(new Moves()));
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
//	}
	
	@Test
	public final void testFindPath() throws CloneNotSupportedException, PathNotFoundException
	{
		try
		{
			// Test easy Pathfinding
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath1.txt"));
			Map map = new Map(br);
			
			assertEquals(agent.findPath(map, new Position(1,1), new Position(4,1), Cell.ECell.PLAYER).toString(), "DDD");
			assertEquals(agent.findPath(map, new Position(1,2), new Position(1,2), Cell.ECell.PLAYER).toString(), "");
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath2.txt"));
		
			map = new Map(br);
			
			assertEquals(agent.findPath(map, new Position(1,1), new Position(6,6), Cell.ECell.PLAYER).toString(), "DDRRRRRDDD");
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
		
			agent.setMap(br);
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
		
			map = new Map(br);
			assertEquals(agent.findPath(map, new Position(1,3), new Position(12,17), Cell.ECell.PLAYER).toString(), "RRRRRRDDDDRRRRUURRDDRRDDDDLLDDRRD");
		
			
			br = new BufferedReader(new FileReader("src/tests/maps/map11.txt"));
		
			map = new Map(br);
			assertEquals(agent.findPath(map, map.getPlayerPosition(), new Position(1,14), Cell.ECell.PLAYER).toString(), "RRRRRRRRRRRRR");
			
			assertEquals(agent.findPath(map, new Position(5,1), new Position(1,14), Cell.ECell.PLAYER).toString(), "RRRRRRRRRURURURUR");
			assertEquals(agent.findPath(map, new Position(4,2), new Position(1,14), Cell.ECell.BOX).toString(), "RRRRRRRRRURURUR");
			
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
			
			agent.setCellAccessible(map).toStringAccessible();
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-server5.txt"));
			map = new Map(br);
			//System.out.println(map);
			
			agent.setCellAccessible(map).toStringAccessible();
			
			
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
	public final void testFindPathToGoals() throws CloneNotSupportedException
	{
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/map10.txt"));
			Map map = new Map(br);
			
			System.out.println(map);
			System.out.println("Number of goals : " + map.getNumberOfGoals());
			System.out.println("Number of boxes : " + map.getNumberOfBoxes());
			int i=0;
			for(String s : agent.getBoxToGoalPaths(map))
			{
				Map newmap=map.clone();
				Box b=map.getBoxes().get(i);
				System.out.println("Player path:" + agent.findPlayerPathFromBoxPath(s, newmap,map.getPlayerPosition() ,b.getPosition()));
				System.out.println("box path"+s);
				System.out.println("Box pos:"+b.getPosition());
				System.out.println("Player pos:"+map.getPlayerPosition());
				i++;
				System.out.println(newmap);
				System.out.println(map);
			}
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("tezst2");
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("tezst");
			}
		}
	}
}
