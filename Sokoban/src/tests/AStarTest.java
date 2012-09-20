package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
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
	public final void testSearch() throws PathNotFoundException
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
