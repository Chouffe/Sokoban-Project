package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import model.AStarSearch;
import model.Map;
import model.Node;
import model.Position;

import org.junit.Before;
import org.junit.Test;

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
	
	
}
