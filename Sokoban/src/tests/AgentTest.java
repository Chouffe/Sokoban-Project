package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.Agent;
import model.ECell;
import model.Map;
import model.Moves;
import model.Position;

import org.junit.Before;
import org.junit.Test;


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
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/map2.txt"));
			
			Map map = new Map(br);
			Position position = new Position(1,2);
			Position position2 = new Position(1,3);
			Position position3 = new Position(2,4);
			
			assertEquals(1, agent.findEmptySpacesAround(position, map).size());
			assertEquals(2, agent.findEmptySpacesAround(position2, map).size());
			assertEquals(3, agent.findEmptySpacesAround(position3, map).size());
			
			// TODO : test the Goals!!!
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/map6.txt"));
			Map map2 = new Map(br);
			
			Position position4 = map2.getPlayerPosition();
			assertEquals(1, agent.findEmptySpacesAround(position4, map2).size());
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/map8.txt"));
			
			Map map3 = new Map(br);
			assertEquals(1, agent.findEmptySpacesAround(map3.getPlayerPosition(), map3).size());
			
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
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/map7.txt"));
			
			Map map = new Map(br);
			
			Position position = new Position(0,2);
			Position positionPlayer = new Position(1,2);
			
			assertEquals(map.getCellFromPosition(position), ECell.PLAYER);
			
			Map map2 = agent.createMapWithVisitedOnThePostion(map, position, positionPlayer);
			
			assertFalse(map.equals(map2));
			assertEquals(map.getMap().size(), map2.getMap().size());
			assertEquals(map2.getCellFromPosition(position), ECell.VISITED);
			assertEquals(map2.getCellFromPosition(positionPlayer), ECell.PLAYER);
			
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
	@Test
	public final void testfindPathToGoal() throws CloneNotSupportedException
	{
		
		try
		{
			// Test easy Pathfinding
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/map1.txt"));
			agent.setMap(br);
			assertEquals(agent.findPathToGoal(new Moves()), "DDD");
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/map2.txt"));
			agent.setMap(br);
			assertEquals(agent.findPathToGoal(new Moves()), "UUU");
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/map3.txt"));
			agent.setMap(br);
			assertEquals(agent.findPathToGoal( new Moves()), "RRRR");
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/map4.txt"));
			agent.setMap(br);
			assertEquals(agent.findPathToGoal(new Moves()), "LLLL");
			
			// Test hardPathfinding
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/map5.txt"));
			Map map5 = new Map(br);
			
			assertEquals(agent.findPathToGoal(map5, new Moves()), "DDRRRRR");
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/map6.txt"));
			Map map6 = new Map(br);
			
			assertEquals(agent.findPathToGoal(map6, new Moves()), "DDRRRRRDDD");
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/map7.txt"));
			Map map7 = new Map(br);
			
			assertEquals(agent.findPathToGoal(map7, new Moves()), "DDRRRRRUU");
			
			
			// Test on a more tricky map
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/map9.txt"));
			
			Map map8 = new Map(br);
			assertEquals(agent.findPathToGoal(map8, new Moves()), "DDRRRRR");
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/test-server1.txt"));
			
			Map map9 = new Map(br);
			System.out.println(agent.findPathToGoal(map9, new Moves()));
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/test-server2.txt"));
			
			Map map10 = new Map(br);
			assertEquals(agent.findPathToGoal(map10, new Moves()), "");
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/test-server3.txt"));
			
			Map map11 = new Map(br);
			System.out.println(agent.findPathToGoal(map11, new Moves()));
			
			br = new BufferedReader(new FileReader("/home/chouffe/workspace/Sokoban/src/tests/maps/path/test-server5.txt"));
			
			Map map12 = new Map(br);
			System.out.println(agent.findPathToGoal(map12, new Moves()));
			
		
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
