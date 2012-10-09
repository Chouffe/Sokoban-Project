package model;

import java.io.IOException;
import java.util.ArrayList;

import exception.IllegalMoveException;
import exception.PathNotFoundException;

import static model.Cell.ECell.*;
import model.Cell.ECell;


public class PositionFinder {

	public PositionFinder() {
	}

	public ArrayList<BoxMove> findEmptySpacesAround(Position position, Map map, Cell.ECell what) throws CloneNotSupportedException, IOException, IllegalMoveException {
		ArrayList<BoxMove> spaces = new ArrayList<BoxMove>(4);
		char[] dirs = {'U', 'D', 'L', 'R'};
		for (int i=0; i<4; i++) {
			StringBuffer playerPushPath = new StringBuffer();
			Map maap = map.clone();
			if (isValidMove(maap, position, what, dirs[i], playerPushPath)) {
				spaces.add(new BoxMove(position.unboundMove(dirs[i]), playerPushPath.toString()));
			}
		}
		return spaces;
	}

	private boolean isPlayerAccessible(Map map, Position position) {
		ECell cellType = getCellType(map, position);	
		return (!(cellType == WALL || cellType == BOX || cellType == BOX_ON_GOAL || cellType == FINAL_BOX_ON_GOAL));
	}

	public static ECell getCellType(Map map, Position pos) {
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
		Position scanner = position.unboundMove(dirs[0]);
		surr[0] = getCellType(map, scanner);
		for (int i=1; i<8; i++) {
			surr[i] = getCellType(map, scanner.unboundIncrement(dirs[i]));
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

	private boolean boxWillDeadlock(Map map, Position dest, char dir) throws CloneNotSupportedException {
		Position twoAway = dest.unboundMove(dir);
		if (!isBox(map, twoAway))
			return false;
		else {
		char[] orthos = getOrthogonals(dir);
		return ((isBox(map, twoAway.unboundMove(orthos[0])) && isBox(map, dest.unboundMove(orthos[0])))
			|| (isBox(map, twoAway.unboundMove(orthos[1])) && isBox(map, dest.unboundMove(orthos[1]))));
		}
	}

	public static char[] getOrthogonals(char dir) {
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

	private boolean isBox(Map map, Position pos) {
		ECell type = getCellType(map, pos);
		return (type == BOX || type == BOX_ON_GOAL);
	}

	private boolean isGoal(Map map, Position pos) {
		ECell type = getCellType(map,pos);
		return (type == GOAL_SQUARE || type == PLAYER_ON_GOAL_SQUARE);
	}

	private boolean isValidBoxSquare(Map map, Position pos) {
		ECell type = getCellType(map, pos);
		return (!(type == BOX || type == WALL || type == BOX_ON_GOAL || type == FINAL_BOX_ON_GOAL));
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

	private boolean playerCanPush(Map map, Position dest, char dir, StringBuffer path) throws CloneNotSupportedException, IOException, IllegalMoveException {
		AStarSearch searcher = new AStarSearch();
		if (dest.equals(map.getPlayerPosition())) {
			path.append(dir);
			map.applyMoves(""+dir);
			System.out.println(map);
			return true;
		}
		try {
			String newPath = searcher.findPath(map, map.getPlayerPosition(), dest, Cell.ECell.PLAYER).toLowerCase() + dir;
			path.append(newPath);
			map.applyMoves(newPath);
			System.out.println(map);
			return true;
		}
		catch (PathNotFoundException e) {
			return false;
		}
	}

	private boolean sidesClear(Map map, Position source, Position dest) throws CloneNotSupportedException {
		if (!map.isPositionOnTheMap(source) || !map.isPositionOnTheMap(dest))
			return false;
		return (isValidBoxSquare(map, dest) && isPlayerAccessible(map, source));
	}

	private boolean isWall(Map map, Position pos) {
		return getCellType(map, pos) == WALL;
	}

	private boolean sourceIsWall(Map map, Position pos, char dir) throws CloneNotSupportedException {
		return (isWall(map, pos.unboundMove(getOppositeDirection(dir))));
	}

	private boolean isValidBoxMove(Map map, Position source, Position dest, char dir, StringBuffer playerPushPath) throws CloneNotSupportedException, IOException, IllegalMoveException {

		if (boxWillDeadlock(map, dest, dir))
			return false;

		if (!isGoal(map, dest)) {
			if (isCorner(map, dest))
			   	return false;
			if (boxWillStickOnWall(map, source.unboundMove(dir), dir))
			   	return false;
		}

		return (playerCanPush(map, source, dir, playerPushPath));
	}

	private boolean clearSides(Map map, Position boxPos, char dir, StringBuffer path) throws CloneNotSupportedException, IOException, IllegalMoveException {
		ECell type = getCellType(map, boxPos);
		if (type == WALL || type == FINAL_BOX_ON_GOAL)
			return false;
		if (type != BOX && type != BOX_ON_GOAL)
			return true;

		Position dest = boxPos.unboundMove(dir);
		Position pushSource = boxPos.unboundMove(getOppositeDirection(dir));

		if (sidesClear(map, pushSource, dest)) {
			return isValidBoxMove(map, pushSource, dest, dir, path);
		}
		else {
			char[] orthos = getOrthogonals(dir);
			Position newBoxPos1 = boxPos.unboundMove(dir);
			Position newBoxPos2 = boxPos.unboundMove(getOppositeDirection(dir));

			for (int i=0; i<orthos.length; i++) {
				for (int j=0; j<orthos.length; j++) {
					Map maap = map.clone();
					StringBuffer paath = new StringBuffer(path.toString());
					if (clearAndPush(maap, orthos, newBoxPos1, newBoxPos2, dest, pushSource, dir, paath, i, j)) {
						int oldMove = path.length();
						path.delete(0,path.length());
						path.append(paath.toString());
						map.applyMoves(paath.delete(0, oldMove).toString());
						return true;
					}
				}
			}

//			return
//				((clearSides(map, newBoxPos1, orthos[0], path) || clearSides(map, newBoxPos1, orthos[1], path))
//				&& (clearSides(map, newBoxPos2, orthos[0], path) || clearSides(map, newBoxPos2, orthos[1], path))
//				&& isValidBoxMove(map, pushSource, dest, dir, path));
		}
		return false;
	}

	private boolean clearAndPush(Map map, char[] orthos, Position pos1, Position pos2, Position dest, Position source, char dir, StringBuffer path, int i, int j) throws CloneNotSupportedException, IOException, IllegalMoveException {
		return
			((clearSides(map, pos1, orthos[i], path) || clearSides(map, pos1, orthos[1>>i], path))
			&& (clearSides(map, pos2, orthos[j], path) || clearSides(map, pos2, orthos[1>>j], path))
			&& isValidBoxMove(map, source, dest, dir, path));
	}

	private boolean isValidMove(Map map, Position position, Cell.ECell what, char dir, StringBuffer playerPushPath) throws CloneNotSupportedException, IOException, IllegalMoveException {
		Position dest = position.unboundMove(dir);
		if (!map.isPositionOnTheMap(dest))
			return false;
		boolean isPlayer = (what == PLAYER || what == PLAYER_ON_GOAL_SQUARE);

		if (isPlayer) {
			playerPushPath.append(dir);
			return (isPlayerAccessible(map, dest));
		}
		
		if (sourceIsWall(map, position, dir) || isWall(map, dest))
			return false;

		else {
			return clearSides(map, position, dir, playerPushPath);
		}
	}
        
}
