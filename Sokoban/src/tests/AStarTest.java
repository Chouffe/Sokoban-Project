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
import org.omg.PortableInterceptor.SUCCESSFUL;

import exception.IllegalMoveException;
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
		Map map = new Map();
		astar.setMap(map);
		
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
	public final void testSearchWithPlayer() throws PathNotFoundException, CloneNotSupportedException, IllegalMoveException, IOException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath1.txt"));
		Map map = new Map(br);
		astar = new AStarSearch(map);
		
		Position pos1 = new Position(1,1);
		Position pos2 = new Position(3,1);
		
		astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(pos2));
		assertEquals(astar.search(ECell.PLAYER).toString(), "DD");
		
		br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath2.txt"));
		map = new Map(br);
		pos1 = new Position(1,1);
		pos2 = new Position(6,6);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(pos2));
		assertEquals(astar.search(ECell.PLAYER).toString(), "DDRRRRRDDD");
		
		br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath2.txt"));
		map = new Map(br);
		pos1 = new Position(1,1);
		pos2 = new Position(6,1);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(pos2));
		assertEquals(astar.search(ECell.PLAYER).toString(), "DDDDD");
		
		br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
		map = new Map(br);
		pos1 = new Position(1,3);
		pos2 = new Position(12,17);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(pos1), new Node(pos2));
		assertEquals(astar.search(ECell.PLAYER).toString(), "RRRRRRDDDDRRRRUURRDDRRDDDDLLDDRRD");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/map3.txt"));
		map = new Map(br);
		astar = new AStarSearch(map);
		
		astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(new Position(7,5)));
		assertEquals(astar.search(ECell.PLAYER).toString(), "UULLDD");
		astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(new Position(7,9)));
		assertEquals(astar.search(ECell.PLAYER).toString(), "UURRDD");
		
		// Large Map
		br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath4.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(map.getGoals().get(0)));
		assertEquals(astar.search(ECell.PLAYER).toString(), "RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
		
		
		// Real map
		br = new BufferedReader(new FileReader("src/tests/maps/map10.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getPlayerPosition()), new Node(map.getGoals().get(0)));
		assertEquals(astar.search(ECell.PLAYER).toString(), "RRRRRRRRRRRRRDR");
		
		
	}
	
	@Test
	public final void testSearchWithPlayerEdgeCases() throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/map3.txt"));
		Map map = new Map(br);
		Position pos = new Position(1,3);
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(pos), new Node(pos));
		assertEquals(astar.search(ECell.PLAYER).toString(), "");
		
	}
	
	@Test
	public final void testSearchWithPlayerFailure() throws CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/path/test-findPath3.txt"));
		Map map = new Map(br);
		Position pos1 = new Position(1,3);
		Position pos2 = new Position(5,3);
		
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
	
	@Test
	public final void testEasyRightSearchWithBoxWithoutShuffling() throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/right/map1.txt"));
		Map map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "RR");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/right/map2.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "RR");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/right/map3.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "RR");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/right/map4.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "RRRRRR");
		
	}
	
	@Test
	public final void testEasyLeftSearchWithBoxWithoutShuffling() throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/left/map1.txt"));
		Map map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "LL");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/left/map2.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "LL");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/left/map3.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "LL");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/left/map4.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "LLLLL");
		
	}
	
	@Test
	public final void testEasyUpSearchWithBoxWithoutShuffling() throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/up/map1.txt"));
		Map map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "UU");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/up/map2.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "UU");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/up/map3.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "UU");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/up/map4.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "UUUUU");	
	}
	
	@Test
	public final void testEasyDownSearchWithBoxWithoutShuffling() throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/down/map1.txt"));
		Map map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "DD");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/down/map2.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "DD");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/down/map3.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "DD");
		
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/easy/down/map4.txt"));
		map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "DDDDD");	
	}
	
	@Test
	public final void testSearchWithBoxesWithoutShuffling() throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/medium/map1.txt"));
		Map map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "RRR");	
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(1).getPosition()), new Node(map.getGoals().get(1)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "RRR");	
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(2).getPosition()), new Node(map.getGoals().get(2)));
		
		assertEquals(astar.search(ECell.BOX).toString(), "RRR");
		
		// Try any combination
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(1)));
		assertEquals(astar.search(ECell.BOX).toString(), "RRDR");
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(2)));
		assertEquals(astar.search(ECell.BOX).toString(), "RDRDR");
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(1).getPosition()), new Node(map.getGoals().get(0)));
		assertEquals(astar.search(ECell.BOX).toString(), "RRUR");
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(1).getPosition()), new Node(map.getGoals().get(2)));
		assertEquals(astar.search(ECell.BOX).toString(), "RRDR");
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(2).getPosition()), new Node(map.getGoals().get(0)));
		assertEquals(astar.search(ECell.BOX).toString(), "RURUR");
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(2).getPosition()), new Node(map.getGoals().get(1)));
		assertEquals(astar.search(ECell.BOX).toString(), "RRUR");
	}
	
	@Test
	public final void testSearchWithBoxEdgeCases() throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/edgecases/map1.txt"));
		Map map = new Map(br);
		
		astar = new AStarSearch(map);
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getBoxes().get(0).getPosition()));
		
		assertEquals(astar.search(ECell.BOX).toString(), "");
	}
	
	
//	@Test
//	public final void testSearchWithBoxPathNotFound() throws CloneNotSupportedException, IOException, IllegalMoveException, PathNotFoundException
//	{
//		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/pathnotfound/map1.txt"));
//		Map map = new Map(br);
//		
//		astar = new AStarSearch(map);
//		System.out.println(map.getGoals());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//		
//		try
//		{
//			assertEquals(astar.search(ECell.BOX).toString(), "WHATEVER");
//			fail("Should have thrown a fuckin' exception");
//		}
//		catch(PathNotFoundException e)
//		{
//		}
//		
//		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/pathnotfound/map2.txt"));
//		map = new Map(br);
//		
//		astar = new AStarSearch(map.clone());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(new Position(4,1)));
//		
//		assertEquals(astar.search(ECell.BOX).toString(), "DD");
//		
//		astar = new AStarSearch(map.clone());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//		
//		try
//		{
//			assertEquals(astar.search(ECell.BOX).toString(), "WHATEVER");
//			fail("Should have thrown a fuckin' exception");
//		}
//		catch(PathNotFoundException e)
//		{
//		}
//		
//		astar = new AStarSearch(map.clone());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(new Position(4, 2)));
//		
//		try
//		{
//			assertEquals(astar.search(ECell.BOX).toString(), "WHATEVER");
//			fail("Should have thrown a fuckin' exception");
//		}
//		catch(PathNotFoundException e)
//		{
//		}
//		
//		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/pathnotfound/map3.txt"));
//		map = new Map(br);
//		
//		astar = new AStarSearch(map.clone());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//		
//		try
//		{
//		
//			assertEquals(astar.search(ECell.BOX).toString(), "WHATEVER");
//			fail("Should have thrown a fucking exception");
//		
//		}
//		catch(PathNotFoundException e)
//		{	
//		}
//		
//		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/pathnotfound/map4.txt"));
//		map = new Map(br);
//		
//		astar = new AStarSearch(map.clone());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//		
//		try
//		{
//			assertEquals(astar.search(ECell.BOX).toString(), "WHATEVER");
//			fail("Should have thrown a fucking exception");
//		}
//		catch(PathNotFoundException e)
//		{		
//		}
//		
//		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/pathnotfound/map6.txt"));
//		map = new Map(br);
//		
//		astar = new AStarSearch(map.clone());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//		
//		// Failure
////		try
////		{
////			assertEquals(astar.search(ECell.BOX).toString(), "WHATEVER");
////			fail("Should have thrown a fucking exception");
////		}
////		catch(PathNotFoundException e)
////		{
////		}
//		
//		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/pathnotfound/map7.txt"));
//		map = new Map(br);
//		
//		astar = new AStarSearch(map.clone());
//		astar.setStartAndGoalNode(new Node(map.getBoxes().get(2).getPosition()), new Node(map.getGoals().get(0)));
//		assertEquals("UU", astar.search(ECell.BOX).toString());
//	}
	
	@Test
	public final void testSearchWithBoxWithShuffling() throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException
	{
		br = new BufferedReader(new FileReader("src/tests/maps/astar/box/shuffling/map1.txt"));
		Map map = new Map(br);
		
		astar = new AStarSearch(map.clone());
		astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
		assertEquals("RRRLLLLLLLLDL", astar.search(ECell.BOX).toString());
	}
	
	
	// TODO : break it down in small tests
//	@Test
//	public final void testSearch() throws PathNotFoundException, CloneNotSupportedException, IllegalMoveException
//	{
//		try
//		{
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/astar/map1.txt"));
//			Map map = new Map(br);
//			astar = new AStarSearch(map);
//			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//			assertEquals(astar.search(ECell.BOX).toString(), "RRRRRRRRRRRRR");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/astar/map2.txt"));
//			map = new Map(br);
//			astar = new AStarSearch(map);
//			
//			assertFalse(map.getBoxes().get(0).getPosition() == map.getBoxes().get(1).getPosition());
//			
//			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//			assertEquals(new Position(2, 15), map.getGoals().get(0));
//			assertEquals(astar.search(ECell.BOX).toString(), "RRRRRRRRRRRRR");
//			assertFalse(map.getBoxes().get(0).getPosition() == map.getBoxes().get(1).getPosition());
//			assertEquals(new Position(2, 15), map.getGoals().get(0));
//
//			assertEquals(map.getBoxes().get(1).getPosition(), new Position(3, 3));
//			astar.setStartAndGoalNode(new Node(map.getBoxes().get(1).getPosition()), new Node(map.getGoals().get(0)));
//			assertEquals(astar.search(ECell.BOX).toString(), "RURRRRRRRRRRR");
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/astar/map3.txt"));
//			map = new Map(br);
//			astar = new AStarSearch(map);
//			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//			assertEquals(astar.findPath(map.getBoxes().get(0).getPosition(), map.getGoals().get(0), ECell.BOX), "LUUUURRUU");
//			assertEquals(astar.search(ECell.BOX).toString(), "LUUUURRUU");
//			astar.setStartAndGoalNode(new Node(map.getBoxes().get(1).getPosition()), new Node(map.getGoals().get(1)));
//			assertEquals(astar.search(ECell.BOX).toString(), "RUUUULLU");
//			
//			
//			
//			
//			
//			// Test fucking large map
//			
//			
//		//	// 1 : with a BOX
//		//	astar = new AStarSearch(map);
//		//	astar.setStartAndGoalNode(new Node(new Position(3,2)), new Node(new Position(3,4)));
//		//	assertEquals(astar.search(ECell.BOX).toString(), "URDR");
//			
//			// Test real maps
//			br = new BufferedReader(new FileReader("src/tests/maps/map10.txt"));
//			map = new Map(br);
//			
//			astar = new AStarSearch(map);
//			
//			astar.setStartAndGoalNode(new Node(map.getBoxes().get(0).getPosition()), new Node(map.getGoals().get(0)));
//			assertEquals(astar.search(ECell.PLAYER).toString(), "RRRRRRR");
//			
//			
//			br = new BufferedReader(new FileReader("src/tests/maps/map12.txt"));
//			map = new Map(br);
//			
//			
//			astar = new AStarSearch(map);
//			
//			astar.setStartAndGoalNode(new Node(new Position(2,8)), new Node(map.getGoals().get(0)));
//			assertEquals(astar.search(ECell.BOX).toString(), "RRRRRRR");
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
}
