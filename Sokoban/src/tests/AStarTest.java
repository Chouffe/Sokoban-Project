package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.AStarSearch;
import model.Cell.ECell;
import model.Map;
import model.Node;
import model.Position;

import org.junit.Before;
import org.junit.Test;

import exception.PathNotFoundException;

public class AStarTest 
{
	AStarSearch astar;
	BufferedReader br;
	
	@Before
	public void setUp()
	{
		astar = new AStarSearch();
		br = null;
	}
	
	@Test
	public final void testInit()
	{
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath1.txt"));
			
			Map map = new Map(br);
			
			astar = new AStarSearch(map);
			assertEquals(astar.getMap().toString(), map.toString());
			
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
	public final void testSetStartAndGoal()
	{
		Node goal = new Node(new Position(2,2));
		Node start = new Node(new Position(1,1));
		
		astar.setStartAndGoalNode(start, goal);
		assertEquals(start, astar.getStart());
		assertEquals(goal, astar.getGoal());
		
		assertEquals(2, astar.getStart().getF());
		assertEquals(0, astar.getStart().getG());
		assertEquals(2, astar.getStart().getH());
		
		assertEquals(1, astar.getOpenedList().size());
		assertEquals(0, astar.getClosedList().size());
		assertEquals(start, astar.getOpenedList().get(0));
	}
	
	@Test
	public final void testGetNodesFromPositions()
	{
		Position pos1 = new Position(0,0);
		Position pos2 = new Position(1,0);
		Position pos3 = new Position(0,2);
		Position pos4 = new Position(1,0);
		
		List<Position> lPositions = new ArrayList<Position>();
		lPositions.add(pos1);
		lPositions.add(pos2);
		lPositions.add(pos3);
		lPositions.add(pos4);
		
		List<Node> lNodes = astar.getNodesFromPosition(lPositions);
		assertEquals(lNodes.size(), lPositions.size());
		assertEquals(lNodes.get(0).getPosition(), lPositions.get(0));
		assertEquals(lNodes.get(1).getPosition(), lPositions.get(1));
		assertEquals(lNodes.get(2).getPosition(), lPositions.get(2));
		assertEquals(lNodes.get(3).getPosition(), lPositions.get(3));
	}
	
	@Test
	public final void testSearch() throws PathNotFoundException, CloneNotSupportedException
	{
		try
		{
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath1.txt"));
			
			Map map = new Map(br);
			
			astar = new AStarSearch(map);
			
			Position pos1 = new Position(1,1);
			Position pos2 = new Position(3,1);
			
			astar.setStartAndGoalNode(new Node(pos1), new Node(pos2));
			assertEquals(astar.search(ECell.PLAYER).toString(), "DD");
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath2.txt"));
			map = new Map(br);
			pos1 = new Position(1,1);
			pos2 = new Position(6,6);
			
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(pos1), new Node(pos2));
			assertEquals(astar.search(ECell.PLAYER).toString(), "DDRRRRRDDD");
			
			// Try some edge cases
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(pos1), new Node(pos1));
			assertEquals(astar.search(ECell.PLAYER).toString(), "");
			
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(pos2), new Node(pos2));
			assertEquals(astar.search(ECell.BOX).toString(), "");
			
			br = new BufferedReader(new FileReader("src/tests/maps/astar/map1.txt"));
			map = new Map(br);
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
			assertEquals(astar.search(ECell.BOX).toString(), "RRRRRRRRRRRRR");
			
			br = new BufferedReader(new FileReader("src/tests/maps/astar/map2.txt"));
			map = new Map(br);
			astar = new AStarSearch(map);
			
			assertFalse(map.getBoxes().get(0).getPosition() == map.getBoxes().get(1).getPosition());
			
			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
			assertEquals(new Position(2, 15), map.getGoals().get(0));
			assertEquals(astar.search(ECell.BOX).toString(), "RRRRRRRRRRRRR");
			assertFalse(map.getBoxes().get(0).getPosition() == map.getBoxes().get(1).getPosition());
			assertEquals(new Position(2, 15), map.getGoals().get(0));

			assertEquals(map.getBoxes().get(1).getPosition(), new Position(3, 3));
			astar.setStartAndGoalNode(new Node(map.getBoxes().get(1).getPosition()), new Node(map.getGoals().get(0)));
			assertEquals(astar.search(ECell.BOX).toString(), "RRRRRRRRRRRUR");
			
			br = new BufferedReader(new FileReader("src/tests/maps/astar/map3.txt"));
			map = new Map(br);
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
			assertEquals(astar.search(ECell.BOX).toString(), "LUUUURRUU");
			astar.setStartAndGoalNode(new Node(map.getBoxes().get(1).getPosition()), new Node(map.getGoals().get(1)));
			assertEquals(astar.search(ECell.BOX).toString(), "RUUUULLU");
			
			astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(new Position(7,5)));
			assertEquals(astar.search(ECell.PLAYER).toString(), "UULLDD");
			astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(new Position(7,9)));
			assertEquals(astar.search(ECell.PLAYER).toString(), "UURRDD");
			
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
			map = new Map(br);
			pos1 = new Position(1,3);
			pos2 = new Position(12,17);
			
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(pos1), new Node(pos2));
			assertEquals(astar.search(ECell.PLAYER).toString(), "RRRRRRDDDDRRRRUURRDDRRDDDDLLDDRRD");
			
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
			map = new Map(br);
			pos1 = new Position(1,3);
			pos2 = new Position(5,3);
			
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(pos1), new Node(pos2));
			try
			{
				assertEquals(astar.search(ECell.PLAYER).toString(), "");
				fail("Should have thrown an exception");
			}
			catch(PathNotFoundException e)
			{
			}
			
			// Test fucking large map
			
			// 1 : with a PLAYER
			br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath4.txt"));
			map = new Map(br);
			
			astar = new AStarSearch(map);
			astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(map.getGoals().get(0)));
			assertEquals(astar.search(ECell.PLAYER).toString(), "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
			
		//	// 1 : with a BOX
		//	astar = new AStarSearch(map);
		//	astar.setStartAndGoalNode(new Node(new Position(3,2)), new Node(new Position(3,4)));
		//	assertEquals(astar.search(ECell.BOX).toString(), "URDR");
			
			// Test real maps
			br = new BufferedReader(new FileReader("src/tests/maps/map10.txt"));
			map = new Map(br);
			
			astar = new AStarSearch(map);
			
			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
			assertEquals(astar.search(ECell.PLAYER).toString(), "RRRRRRR");
			
			
			br = new BufferedReader(new FileReader("src/tests/maps/map12.txt"));
			map = new Map(br);
			
			
			astar = new AStarSearch(map);
			
			astar.setStartAndGoalNode(new Node(new Position(2,8)), new Node(map.getGoals().get(0)));
			assertEquals(astar.search(ECell.BOX).toString(), "RRRRRRR");
			
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
	public final void testCheckBoxDir() throws CloneNotSupportedException, IOException, PathNotFoundException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/map4.txt"));
		Map map = new Map(br);
		AStarSearch astar = new AStarSearch(); 
		
		assertEquals("rururrddlU", astar.checkBoxDir('U', map, map.getPlayerPosition(), map.getBoxes().get(1).getPosition()));
		assertEquals("rururD", astar.checkBoxDir('D', map, map.getPlayerPosition(), map.getBoxes().get(1).getPosition()));
		assertEquals("rururrdL", astar.checkBoxDir('L', map, map.getPlayerPosition(), map.getBoxes().get(1).getPosition()));
		assertEquals("rurR", astar.checkBoxDir('R', map, map.getPlayerPosition(), map.getBoxes().get(1).getPosition()));
		
		
		assertEquals("uU", astar.checkBoxDir('U', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
		
		try
		{
			assertEquals(null, astar.checkBoxDir('L', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
			
		}
		catch(PathNotFoundException e)
		{
		}
		
		try
		{
			assertEquals(null, astar.checkBoxDir('R', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
			
		}
		catch(PathNotFoundException e)
		{
		}
		
		try
		{
			assertEquals(null, astar.checkBoxDir('D', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
			
		}
		catch(PathNotFoundException e)
		{
		}
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/map5.txt"));
		map = new Map(br);
		astar = new AStarSearch(); 
		
		assertEquals("rR", astar.checkBoxDir('R', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
		
		try
		{
			assertEquals(null, astar.checkBoxDir('L', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
			
		}
		catch(PathNotFoundException e)
		{
		}
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/map6.txt"));
		map = new Map(br);
		Map clonedMap = map.clone();
		astar = new AStarSearch(); 
		
		assertEquals("R", astar.checkBoxDir('R', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
		
		try
		{
			assertEquals(null, astar.checkBoxDir('U', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
			
		}
		catch(PathNotFoundException e)
		{
		}
		
		try
		{
			assertEquals(null, astar.checkBoxDir('D', map, map.getPlayerPosition(), map.getBoxes().get(0).getPosition()));
			
		}
		catch(PathNotFoundException e)
		{
		}
		
		// Test cloning
		assertEquals(clonedMap.toString(), map.clone().toString());
		
	}
	
}
