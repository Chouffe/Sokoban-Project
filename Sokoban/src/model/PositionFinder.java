package model;


import java.io.IOException;
import java.util.ArrayList;

import model.Cell.ECell;
import static model.Cell.ECell.*;


import exception.IllegalMoveException;
import exception.OffOfMapException;
import exception.PathNotFoundException;


public class PositionFinder {

	public PositionFinder() {
	}

	public ArrayList<BoxMove> findEmptySpacesAround(Position position, Map map, Cell.ECell what) throws CloneNotSupportedException, IOException, IllegalMoveException, OffOfMapException {
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

	public boolean isPlayerAccessible(Map map, Position position) throws OffOfMapException {
		ECell cellType = getCellType(map, position);	
		return (!(cellType == WALL || cellType == BOX || cellType == BOX_ON_GOAL || cellType == FINAL_BOX_ON_GOAL));
	}

	public static Cell.ECell getCellType(Map map, Position pos) throws OffOfMapException {
		return map.getCellFromPosition(pos).getType();
	}
	
	//[up down left right]
	private Cell.ECell[] getAdjacentCellTypes(Map map, Position position) throws CloneNotSupportedException, OffOfMapException {
		Cell.ECell[] cells = new Cell.ECell[4];
		cells[0] = getCellType(map, position.unboundMove('U'));
		cells[1] = getCellType(map, position.unboundMove('D'));
		cells[2] = getCellType(map, position.unboundMove('L'));
		cells[3] = getCellType(map, position.unboundMove('R'));
		return cells;
	}

	// Not used yet
	private Cell.ECell[] getSurroundingCellTypes(Map map, Position position) throws CloneNotSupportedException, OffOfMapException {
		char[] dirs = {'U', 'R', 'D', 'D', 'L', 'L', 'U', 'U'};
		Cell.ECell[] surr = new Cell.ECell[8];
		Position scanner = position.unboundMove(dirs[0]);
		surr[0] = getCellType(map, scanner);
		for (int i=1; i<8; i++) {
			surr[i] = getCellType(map, scanner.unboundIncrement(dirs[i]));
		}
		return surr;
	}

	private boolean isSquareDeadlock(Map m, Position box, char dir) throws CloneNotSupportedException {
		try {
			Position dest = box.unboundMove(dir);
			Map map = m.clone();
			map.set(ECell.BOX, dest);
			map.set(ECell.EMPTY_FLOOR, box);
			char[] orthos = getOrthogonals(dir);
			if (isValidBoxSquare(map, dest.unboundMove(dir))) {
				if (isGoal(map, dest.unboundMove(dir)))
					return false;
				return isUnsafeCenter(map, dest.unboundMove(dir));
			}
			Position side1 = dest.unboundMove(orthos[0]);
			Position side2 = dest.unboundMove(orthos[1]);
			if (!isValidBoxSquare(map, side1))
				return isUnsafeCenter(map, side1.unboundIncrement(dir));
			if (!isValidBoxSquare(map, side2))
				return isUnsafeCenter(map, side2.unboundIncrement(dir));
			return false;
		}
		catch (OffOfMapException e) {
			return false;
		}
	}

	private boolean isUnsafeCenter(Map map, Position center) throws CloneNotSupportedException, OffOfMapException {
		return (diagonalsAreUnsafe(map, center) && adjacentsAreUnsafe(map, center));
	}

	private boolean diagonalsAreUnsafe(Map map, Position center) throws CloneNotSupportedException, OffOfMapException {
		return (!isValidBoxSquare(map, center.unboundMove('U').unboundIncrement('L')) && !isValidBoxSquare(map, center.unboundMove('D').unboundIncrement('R')))
			|| (!isValidBoxSquare(map, center.unboundMove('U').unboundIncrement('R')) && !isValidBoxSquare(map, center.unboundMove('D').unboundIncrement('L')));
	}

	private boolean adjacentsAreUnsafe(Map map, Position center) throws CloneNotSupportedException, OffOfMapException {
		return (!isValidBoxSquare(map, center.unboundMove('U')) && !isValidBoxSquare(map, center.unboundMove('D')) && !isValidBoxSquare(map, center.unboundMove('L')) && !isValidBoxSquare(map, center.unboundMove('R')));
	}

	private boolean isCorner(Map map, Position position) throws CloneNotSupportedException, OffOfMapException {
		Cell.ECell[] cells = getAdjacentCellTypes(map, position); //[up down left right]
		return ((cells[0] == Cell.ECell.WALL || cells[1] == Cell.ECell.WALL) && (cells[2] == Cell.ECell.WALL || cells[3] == Cell.ECell.WALL));
	}

	private boolean boxWillDeadlock(Map map, Position dest, char dir) throws CloneNotSupportedException, OffOfMapException {
		Position twoAway = dest.unboundMove(dir);
		Position boxPos = dest.unboundMove(getOppositeDirection(dir));
		if (isSquareDeadlock(map, boxPos, dir))
			return true;
		if (!isBox(map, twoAway))
			return false;
		else {
		char[] orthos = getOrthogonals(dir);
		return ((isBox(map, twoAway.unboundMove(orthos[0])) && isBox(map, dest.unboundMove(orthos[0])))
			|| (isBox(map, twoAway.unboundMove(orthos[1])) && isBox(map, dest.unboundMove(orthos[1]))));
		}
	}

	public static char[] getOrthogonals(char direction) {
			char dir = Character.toUpperCase(direction);
			if (dir == 'U' || dir == 'D') {
				char[] lr = {'L', 'R'};
				return lr;
				}
			else {
				char[] ud = {'U', 'D'};
				return ud;
			}
		}

	public boolean isBox(Map map, Position pos) throws OffOfMapException {
		ECell type = getCellType(map, pos);
		return (type == BOX || type == BOX_ON_GOAL);
	}

	private boolean isGoal(Map map, Position pos) throws OffOfMapException {
		Cell.ECell type = getCellType(map,pos);
		return (type == Cell.ECell.GOAL_SQUARE || type == Cell.ECell.PLAYER_ON_GOAL_SQUARE);
	}

	private boolean isValidBoxSquare(Map map, Position pos) throws OffOfMapException {
		Cell.ECell type = getCellType(map, pos);
		return (!(type == Cell.ECell.BOX || type == Cell.ECell.WALL || type == Cell.ECell.BOX_ON_GOAL || type == Cell.ECell.FINAL_BOX_ON_GOAL));
	}
	
	private boolean isValidBoxAccessibleType(ECell type) {
		return (!(type == BOX || type == WALL || type == BOX_ON_GOAL || type == FINAL_BOX_ON_GOAL));
	}

	private boolean boxWillStickOnWall(Map map, Position pos, char dir) throws CloneNotSupportedException, OffOfMapException {
		Position target = pos.unboundMove(dir);
		Position twoAway = target.unboundMove(dir);
		if (map.isPositionOnTheMap(twoAway)) {
			Cell.ECell twoAwayType = getCellType(map, twoAway);
			if (twoAwayType == Cell.ECell.WALL) {
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

	//If possible appends path from current player position to player position after single box move to StringBuffer path.
	//dest is position next to box before the push
	private boolean playerCanPush(Map map, Position dest, char dir, StringBuffer path) throws CloneNotSupportedException, IOException, IllegalMoveException, OffOfMapException {
		AStarSearch searcher = new AStarSearch();
		if (dest.equals(map.getPlayerPosition())) {
			path.append(Character.toUpperCase(dir));
			map.applyMoves(""+dir);
			//System.out.println(map);
			return true;
		}
		try {
			String newPath = searcher.findPath(map, map.getPlayerPosition(), dest, Cell.ECell.PLAYER).toLowerCase() + dir;
			path.append(newPath);
			map.applyMoves(newPath);
			//System.out.println(map);
			return true;
		}
		catch (PathNotFoundException e) {
			return false;
		}
	}

	private boolean sidesClear(Map map, Position source, Position dest) throws CloneNotSupportedException, OffOfMapException {
		if (!map.isPositionOnTheMap(source) || !map.isPositionOnTheMap(dest))
			return false;
		return (isValidBoxSquare(map, dest) && isPlayerAccessible(map, source));
	}

	private boolean isWall(Map map, Position pos) throws OffOfMapException {
		return getCellType(map, pos) == Cell.ECell.WALL;
	}

	private boolean sourceIsWall(Map map, Position pos, char dir) throws CloneNotSupportedException, OffOfMapException {
		return (isWall(map, pos.unboundMove(getOppositeDirection(dir))));
	}

	public boolean isValidBoxMove(Map map, Position source, Position dest, char dir, StringBuffer playerPushPath) throws CloneNotSupportedException, IOException, IllegalMoveException, OffOfMapException {
	
		if (!isValidBoxSquare(map, source) || !isValidBoxSquare(map, dest))
			return false;

		if (!isGoal(map, dest)) {
			if (isCorner(map, dest))
			   	return false;
			if (boxWillStickOnWall(map, source.unboundMove(dir), dir))
			   	return false;

		if (boxWillDeadlock(map, dest, dir))
			return false;

		}

		return (playerCanPush(map, source, dir, playerPushPath));
	}

	private boolean clearSides(Map map, Position boxPos, char dir, StringBuffer path) throws CloneNotSupportedException, IOException, IllegalMoveException, OffOfMapException {
		Cell.ECell type = getCellType(map, boxPos);
		if (type == Cell.ECell.WALL || type == Cell.ECell.FINAL_BOX_ON_GOAL)
			return false;
		if (type != Cell.ECell.BOX && type != Cell.ECell.BOX_ON_GOAL)
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
		}
		return false;
	}

	private boolean clearAndPush(Map map, char[] orthos, Position pos1, Position pos2, Position dest, Position source, char dir, StringBuffer path, int i, int j) throws CloneNotSupportedException, IOException, IllegalMoveException, OffOfMapException {
		return
			((clearSides(map, pos1, orthos[i], path) || clearSides(map, pos1, orthos[1>>i], path))
			&& (clearSides(map, pos2, orthos[j], path) || clearSides(map, pos2, orthos[1>>j], path))
			&& isValidBoxMove(map, source, dest, dir, path));
	}

	private boolean isValidMove(Map map, Position position, Cell.ECell what, char dir, StringBuffer playerPushPath) throws CloneNotSupportedException, IOException, IllegalMoveException, OffOfMapException {
		Position dest = position.unboundMove(dir);
		if (!map.isPositionOnTheMap(dest))
			return false;
		boolean isPlayer = (what == Cell.ECell.PLAYER || what == Cell.ECell.PLAYER_ON_GOAL_SQUARE);

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
