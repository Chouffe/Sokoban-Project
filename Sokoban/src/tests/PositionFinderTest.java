package tests;

import static org.junit.Assert.*;

import java.io.*;
import model.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import exception.IllegalMoveException;

public class PositionFinderTest {
	PositionFinder pf;
	
	@Before
	public final void Initialize() {
		pf = new PositionFinder();
	}


	@Test
	public final void testPlayerInOpenSpace() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map1.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getPlayerPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.PLAYER).get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.PLAYER).get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), pf.findEmptySpacesAround(pos, map, Cell.ECell.PLAYER).get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.PLAYER).get(3).getNewPosition());
	}
	
	@Test
	public final void testPlayerByWall() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map2.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getPlayerPosition();
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.PLAYER).get(0).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.PLAYER).get(1).getNewPosition());
	}

	@Test
	public final void testBoxAtCrossroads() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map1.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
	}

	@Test
	public final void testBlockedBox() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map6.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(1).getPosition();
		ArrayList<BoxMove> sol = new ArrayList<BoxMove>(4);
		assertEquals(sol, pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX));
	}
		
	@Test
	public final void testBoxInTunnel() throws CloneNotSupportedException, IOException, IllegalMoveException {
BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map7.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
	}

	@Test
	public final void testBoxInGoalTunnel() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map8.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('L'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
	}

	@Test
	public final void testBoxNearTwoOtherBoxes() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map10.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(3).getNewPosition());
	}

	@Test
	public final void testBoxNearThreeOtherBoxes() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map11.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('L'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(2).getNewPosition());
	}

	@Test
	public final void testBoxNearACorner() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map12.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('L'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(2).getNewPosition());
	}

	@Test
	public final void testBoxNearAStickyWall() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map13.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(2).getNewPosition());
	}

	@Test
	public final void testBoxNearTwoStickyWalls() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map14.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
	}

	@Test
	public final void testBoxNearGoalWall() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map15.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(2).getNewPosition());
	}

	@Test
	public final void testBoxNearPlayer() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map16.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(3).getNewPosition());
	
			br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map22.txt"));
		map = new Map(br);
		System.out.println(map);

		pos = map.getBoxes().get(0).getPosition();
		assertEquals(2, pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).size());
		pos = map.getBoxes().get(1).getPosition();
		assertEquals(2, pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).size());
	
	}

	@Test
	public final void testBoxInAlcove() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map17.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('L'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
	}

	@Test
	public final void testBoxNearGoalCorner() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map18.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		assertEquals(pos.unboundMove('U'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX).get(1).getNewPosition());
	}

	@Test
	public final void testCorneredBox() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map19.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		ArrayList<BoxMove> sol = new ArrayList<BoxMove>(4);
		assertEquals(sol, pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX));
	}
	
	@Test
	public final void testBoxNearWall() throws CloneNotSupportedException, IOException, IllegalMoveException
	{
		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map21.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		ArrayList<BoxMove> moves = pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX);

		assertEquals(pos.unboundMove('L'), moves.get(0).getNewPosition());
		assertEquals(pos.unboundMove('R'), moves.get(1).getNewPosition());
	}

	@Test
	public final void testBoxShuffling1() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map20.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		ArrayList<BoxMove> moves = pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX);
		System.out.println(moves.get(0));
		System.out.println(moves.get(1));
		assertEquals(pos.unboundMove('U'), moves.get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), moves.get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), moves.get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), moves.get(3).getNewPosition());
	}

	@Test
	public final void testBoxShuffling2() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map23.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		ArrayList<BoxMove> moves = pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX);
		System.out.println(moves.get(0));
		System.out.println(moves.get(1));
		assertEquals(pos.unboundMove('U'), moves.get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), moves.get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), moves.get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), moves.get(3).getNewPosition());
	}

	@Test
	public final void testBoxShuffling3() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map24.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		ArrayList<BoxMove> moves = pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX);
		for (BoxMove m : moves)
		System.out.println(m);
		assertEquals(pos.unboundMove('U'), moves.get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), moves.get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), moves.get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), moves.get(3).getNewPosition());
	}

	@Test
	public final void testBoxShuffling5() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map25.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(0).getPosition();
		ArrayList<BoxMove> moves = pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX);
		for (BoxMove m : moves)
		System.out.println(m);
		assertEquals(pos.unboundMove('U'), moves.get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), moves.get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), moves.get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), moves.get(3).getNewPosition());
	}

	@Test
	public final void testBoxShuffling10() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map26.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(5).getPosition();
		ArrayList<BoxMove> moves = pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX);
		for (BoxMove m : moves)
		System.out.println(m);
		assertEquals(pos.unboundMove('U'), moves.get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), moves.get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), moves.get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), moves.get(3).getNewPosition());
	}

	@Test
	public final void testBoxShufflingXtreme() throws CloneNotSupportedException, IOException, IllegalMoveException {

		BufferedReader br = new BufferedReader(new FileReader("src/tests/maps/posfinder/map27.txt"));
		Map map = new Map(br);
		System.out.println(map);

		Position pos = map.getBoxes().get(13).getPosition();
		ArrayList<BoxMove> moves = pf.findEmptySpacesAround(pos, map, Cell.ECell.BOX);
		for (BoxMove m : moves)
		System.out.println(m);
		assertEquals(pos.unboundMove('U'), moves.get(0).getNewPosition());
		assertEquals(pos.unboundMove('D'), moves.get(1).getNewPosition());
		assertEquals(pos.unboundMove('L'), moves.get(2).getNewPosition());
		assertEquals(pos.unboundMove('R'), moves.get(3).getNewPosition());
	}


}
