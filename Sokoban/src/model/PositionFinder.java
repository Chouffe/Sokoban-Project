package model;

import java.util.ArrayList;

import static model.Cell.ECell.*;
import model.Cell.ECell;

import exception.PathNotFoundException;
import java.io.IOException;

public class PositionFinder {

	public PositionFinder() {
	}

	public ArrayList<Position> findEmptySpacesAround(Position position, Map map, Cell.ECell what) throws CloneNotSupportedException, IOException {
		ArrayList<Position> spaces = new ArrayList<Position>(4);
		char[] dirs = {'U', 'D', 'L', 'R'};
		for (int i=0; i<4; i++) {
			if (isValidMove(map, position, what, dirs[i])) {
				spaces.add(position.unboundMove(dirs[i]));
			}
		}
		return spaces;
	}

	private boolean isPlayerAccessible(Map map, Position position) {
		ECell cellType = getCellType(map, position);	
		return (cellType == VISITED || cellType == EMPTY_FLOOR || cellType == GOAL_SQUARE);
	}

	private ECell getCellType(Map map, Position pos) {
		return map.getCellFromPosition(pos).getType();
	}
	
	//[up down left right]
	private ECell[] getAdjacentCellTypes(Map map, Position position) throws CloneNotSupportedException {
		ECell[] cells = new ECell[4];
		cells[0] = getCellType(map, position.unboundMove('U'));
		cells[1] = getCellType(map, position.unboundMove('D'));
		cells[2] = getCellType(map, position.unboundMove('L'));
		cells[3] = getCellType(map, position.unboundMove('R'));
		return cells;
	}

	private ECell[] getSurroundingCellTypes(Map map, Position position) throws CloneNotSupportedException {
		char[] dirs = {'U', 'R', 'D', 'D', 'L', 'L', 'U', 'U'};
		ECell[] surr = new ECell[8];
		for (int i=0; i<8; i++) {
			surr[i] = getCellType(map, position.unboundIncrement(dirs[i]));
		}
		return surr;
	}

	private boolean isCorner(Map map, Position position) throws CloneNotSupportedException {
		ECell[] cells = getAdjacentCellTypes(map, position); //[up down left right]
		return ((cells[0] == WALL || cells[1] == WALL) && (cells[2] == WALL || cells[3] == WALL));
	}

	private boolean boxWillDeadlock(Map map, Position pos) throws CloneNotSupportedException {
		int numAdjBoxes = -1;
		for (ECell c : getSurroundingCellTypes(map, pos)) {
			if (c == BOX || c == BOX_ON_GOAL)
				numAdjBoxes++;
		}
		return (numAdjBoxes > 2);
	}

	char[] getOrthogonals(char dir) {
		char[] orthos = new char[2];
		if (dir == 'U' || dir == 'D') {
			char[] lr = {'L', 'R'};
			return lr;
			}
		else {
			char[] ud = {'U', 'D'};
			return ud;
			}
	}

	private boolean isGoal(Map map, Position pos) {
		return (getCellType(map,pos) == GOAL_SQUARE);
	}

	private boolean isValidBoxSquare(Map map, Position pos) {
		ECell type = getCellType(map, pos);
		return (type == EMPTY_FLOOR || type == VISITED || type == GOAL_SQUARE);
	}

	private boolean boxWillStickOnWall(Map map, Position pos, char dir) throws CloneNotSupportedException {
		Position target = pos.unboundMove(dir);
		System.out.println("Target " + target);
		Position twoAway = target.unboundMove(dir);
		System.out.println("Two away " + twoAway);
		if (map.isPositionOnTheMap(twoAway)) {
			ECell twoAwayType = getCellType(map, twoAway);
			System.out.println("Two away type: " + twoAwayType);
			if (twoAwayType == WALL) {
				System.out.println("Two spots away is a wall?");
				char[] orthos = getOrthogonals(dir);
				Position spreader1 = target.unboundMove(orthos[0]);
				Position spreader2 = target.unboundMove(orthos[1]);
				while (isValidBoxSquare(map, spreader1)) {
					if (isValidBoxSquare(map, spreader1.unboundMove(dir)))
						return false;
					spreader1.unboundIncrement(orthos[0]);
				}
				while (isValidBoxSquare(map, spreader2)) {
					if (isValidBoxSquare(map, spreader2.unboundMove(dir)))
						return false;
					spreader2.unboundIncrement(orthos[1]);
				}
				return true;
			}
		}
		return false;
	}

	private char getOppositeDirection(char dir) {
		switch (dir) {
			case 'U':
				return 'D';
			case 'D':
				return 'U';
			case 'L':
				return 'R';
			case 'R':
				return 'L';
		}
		return 'E';
	}

	private boolean playerCanPush(Map map, Position position, char dir) throws CloneNotSupportedException, IOException {
		AStarSearch searcher = new AStarSearch();
		Position dest = position.unboundMove(getOppositeDirection(dir));
		try {
			searcher.findPath(map, map.getPlayerPosition(), dest, Cell.ECell.PLAYER);
			return true;
		}
		catch (PathNotFoundException e) {
			return false;
		}
	}

	private boolean isValidMove(Map map, Position position, Cell.ECell what, char dir) throws CloneNotSupportedException, IOException {
		Position dest = position.unboundMove(dir);
		if (!map.isPositionOnTheMap(dest))
			return false;
		boolean isPlayer = (what == PLAYER || what == PLAYER_ON_GOAL_SQUARE);

		if (isPlayer) {
			return (isPlayerAccessible(map, dest));
		}
		
		else {
			if (isValidBoxSquare(map, dest)) {
				if (playerCanPush(map, position, dir)) {
					System.out.println("Player can push");
					if (getCellType(map, dest) == GOAL_SQUARE) {
						System.out.println("Destination square is a goal");
						return true;
					}
					if (isCorner(map, dest)) {
						System.out.println("Can't push into a corner");
					   	return false;
					}
					if (boxWillDeadlock(map, dest)) {
						System.out.println("This would cause a deadlock");
						return false;
					}
					if (boxWillStickOnWall(map, position, dir)) {
						System.out.println("Box will stick on this wall");
					   	return false;
					}
					return true;
				}
			}
		}
		return false;

	}

}
