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
					if (getCellType(map, dest) == GOAL_SQUARE) {
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
                                        if (isDeadEnd(map, position, dir))
                                            return false;
					return true;
				}
			}
		}
		return false;

	}
        
        private boolean isDeadEnd(Map map, Position position, char dir) throws CloneNotSupportedException, IOException
        {
            int depth =0;
            Position start = position.clone();
            while (isTunnel(map, position, dir))
            {
                start.unboundIncrement(dir);
                depth++;
            }
            // It is not a tunnel.
            if (depth ==0)
                return false;                        
            // Dead end with goal
            if (isGoal(map,start))
                return false;
            // Dead end
            start.unboundIncrement(dir);
            // Is it a wall in the end?
            if (hasWallsOnOrthogonals(map,start,dir))
                return true;
            else
                return false;
        }
        
        private boolean isTunnel(Map map, Position position, char dir) throws CloneNotSupportedException, IOException
        {
            Position target = position.clone();
            target.unboundIncrement(dir);
            
            if (isValidBoxSquare(map,target))
            {
                return hasWallsOnOrthogonals(map, target,dir);
            }
            else
                // Not valid square.
                return false;
        }
        
        private boolean hasWallsOnOrthogonals(Map map, Position position, char dir) throws CloneNotSupportedException
        {
            ECell [] orthogonals = getOrthogonalsCellTypes(map, position, dir);
            if (orthogonals[0]==Cell.ECell.WALL && orthogonals[1]==Cell.ECell.WALL)
                // Could be a tunnel?
                return true;
            else
                // Can not be a tunnel.
                return false;
        }
        
        ECell[] getOrthogonalsCellTypes(Map map, Position position, char dir) throws CloneNotSupportedException {
                ECell[] cells = new ECell[2];
                char[] orthos = getOrthogonals(dir);
                
		cells[0] = getCellType(map, position.unboundMove(orthos[0]));
		cells[1] = getCellType(map, position.unboundMove(orthos[1]));
		return cells;
	}

}
