package model;

import java.util.ArrayList;

import static model.Cell.ECell.*;
import model.Cell.ECell;

import exception.PathNotFoundException;
import java.io.IOException;

public class PositionFinder {

	public PositionFinder() {
	}

	public ArrayList<BoxMove> findEmptySpacesAround(Position position, Map map, Cell.ECell what) throws CloneNotSupportedException, IOException {
		ArrayList<BoxMove> spaces = new ArrayList<BoxMove>(4);
		char[] dirs = {'U', 'D', 'L', 'R'};
		for (int i=0; i<4; i++) {
			String playerPushPath = new String();
			if (isValidMove(map, position, what, dirs[i], playerPushPath)) {
				spaces.add(new BoxMove(position.unboundMove(dirs[i]), playerPushPath));
			}
		}
		return spaces;
	}

	private boolean isPlayerAccessible(Map map, Position position) {
		ECell cellType = getCellType(map, position);	
		return (!(cellType == WALL || cellType == BOX || cellType == BOX_ON_GOAL));
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
		ECell type = getCellType(map,pos);
		return (type == GOAL_SQUARE || type == PLAYER_ON_GOAL_SQUARE);
	}

	private boolean isValidBoxSquare(Map map, Position pos) {
		ECell type = getCellType(map, pos);
		return (!(type == BOX || type == WALL || type == BOX_ON_GOAL));
	}

	private boolean boxWillStickOnWall(Map map, Position pos, char dir) throws CloneNotSupportedException {
		Position target = pos.unboundMove(dir);
		Position twoAway = target.unboundMove(dir);
		if (map.isPositionOnTheMap(twoAway)) {
			ECell twoAwayType = getCellType(map, twoAway);
			if (twoAwayType == WALL) {
				char[] orthos = getOrthogonals(dir);
				Position spreader1 = target.unboundMove(orthos[0]);
				Position spreader2 = target.unboundMove(orthos[1]);
				while (isValidBoxSquare(map, spreader1)) {
					if (isGoal(map, spreader1) || isValidBoxSquare(map, spreader1.unboundMove(dir)))
						return false;
					spreader1.unboundIncrement(orthos[0]);
				}
				while (isValidBoxSquare(map, spreader2)) {
					if (isGoal(map, spreader2) || isValidBoxSquare(map, spreader2.unboundMove(dir)))
						return false;
					spreader2.unboundIncrement(orthos[1]);
				}
				return true;
			}
		}
		return false;
	}

	public static char getOppositeDirection(char dir) {
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

	private boolean playerCanPush(Map map, Position position, char dir, String path) throws CloneNotSupportedException, IOException {
		AStarSearch searcher = new AStarSearch();
		Position dest = position.unboundMove(getOppositeDirection(dir));
		try {
			path = searcher.findPath(map, map.getPlayerPosition(), dest, Cell.ECell.PLAYER);
			path.toLowerCase();
			return true;
		}
		catch (PathNotFoundException e) {
			return false;
		}
	}

	private boolean isValidMove(Map map, Position position, Cell.ECell what, char dir, String playerPushPath) throws CloneNotSupportedException, IOException {
		Position dest = position.unboundMove(dir);
		if (!map.isPositionOnTheMap(dest))
			return false;
		boolean isPlayer = (what == PLAYER || what == PLAYER_ON_GOAL_SQUARE);

		if (isPlayer) {
			return (isPlayerAccessible(map, dest));
		}
		
		else {
			Position pushSource = position.unboundMove(getOppositeDirection(dir));
			if (isValidBoxSquare(map, dest) && isPlayerAccessible(map, pushSource)) {
				if (playerCanPush(map, position, dir, playerPushPath)) {
					if (isGoal(map, dest)) {
						return true;
					}
					if (isCorner(map, dest)) {
					   	return false;
					}
					if (boxWillDeadlock(map, dest)) {
						return false;
					}
					if (boxWillStickOnWall(map, position, dir)) {
					   	return false;
					}
					return true;
				}
			}
		}
		return false;
	}
        
}
