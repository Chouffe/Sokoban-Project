package model;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

import model.Cell.ECell;

import exception.IllegalMoveException;
import exception.PathNotFoundException;


/**
 * 
 * Represents the Agent which is able to solve a given problem
 * The agent is the AI
 *
 */
public class Agent {
	
	protected Map map;
	protected Moves moves;
	protected LinkedList<Node> nodes = new LinkedList<Node>();
	protected AStarSearch astar = new AStarSearch();
	protected HashMap<BoxToGoalPath, String> pathMap = new HashMap<BoxToGoalPath, String>();
	int hashedUsed = 0;
		
	
	public Agent()
	{
		map = new Map();
		moves = new Moves();
		nodes = new LinkedList<Node>();
	}
	
	public Agent(ArrayList<String> stringMap)
	{
		this();
		map = new Map(stringMap);
	}
	
	/**
	 * Computes the distance between two positions
	 * @author arthur
	 * @param position1
	 * @param position2
	 * @return int : distance
	 */
	public int distance(Position position1, Position position2)
	{
		return (position1.getI() - position2.getI())*(position1.getI() - position2.getI()) + (position1.getJ() - position2.getJ())*(position1.getJ() - position2.getJ());
	}
	
	/**
	 * Computes the minimum distance between on position and a list of positions
	 * @author arthur
	 * @param position
	 * @param positions
	 * @return the minimum distance
	 *          if the list is empty, return -1
	 */
	public int distance(Position position, ArrayList<Position> positions)
	{
		if(positions != null && positions.size() > 0)
		{
			int result = distance(position, positions.get(0));
			
			for(Position pos : positions)
			{
				int dist = distance(position, pos);
				if(result > dist)
				{
					result = dist;
				}
			}
			
			return result;
		}
		else
		{
			return -1;
		}
	}
	
	
	
	/**
	* Finds a box-to-goal path for each box.
	*
	* Result is only guaranteed to be accurate if the strings are accessed in the 
	* order in which they are stored in the String array.
	*
	* @author Alden Coots <ialden.coots@gmail.com>
	* @throws CloneNotSupportedException 
	*/
	public String[] getBoxToGoalPaths(Map map) throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException {
		String[] paths = new String[map.getNumberOfBoxes()];
		ArrayList<Box> orderedBoxes = new ArrayList<Box>();
		findBoxToGoalPaths(orderedBoxes, map, paths);
		
		return paths;
	}

	public boolean findBoxToGoalPaths(ArrayList<Box> orderedBoxes, Map map, String[] paths) throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException {
		if (map.getNumberOfBoxes() == 0) {
			map.setBoxes(orderedBoxes);
			return (findSequentialBoxToGoalPaths(map, paths, 0));
			}
		else {
			boolean isSolved = false;
			for (int b=0; b<map.getNumberOfBoxes(); b++) {
				ArrayList<Box> newOrder = new ArrayList<Box>();
				newOrder.addAll(orderedBoxes);
				Map newMap = map.clone();
				newOrder.add(map.getBoxes().get(b));
				newMap.getBoxes().remove(b);
				isSolved = isSolved || findBoxToGoalPaths(newOrder, newMap, paths); 
				if (isSolved) break;
			}
		return isSolved;
		}
	}

	/**
	* Finds a box-to-goal path for each box.
	*
	* Populates a String array with box-to-goal paths and returns true if a valid
	* solution is found for all boxes.
	*
	* Result is only guaranteed to be accurate if the strings are accessed in the 
	* order in which they are stored in the String array.
	*
	* @author Alden Coots <ialden.coots@gmail.com>
	* @param map Should be a clone, as it is altered
	* @param paths String array where box-to-goal paths are stored
	* @param boxIndx index of initial box in map's box array (should be 0 initially)
	* @throws CloneNotSupportedException 
	*/
	public boolean findSequentialBoxToGoalPaths(Map map, String[] paths, int boxIndx) throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException {
		if (map.getBoxes().isEmpty()) return true;
		else {
			boolean isSolved = false;
				for (int g = 0; g<map.getNumberOfGoals(); g++) {
					Cell.ECell type = PositionFinder.getCellType(map, map.getGoals().get(g));
					if (type != Cell.ECell.BOX_ON_GOAL && type != Cell.ECell.FINAL_BOX_ON_GOAL) {
							Box box = map.getBoxes().get(0).clone();
							Position goal = map.getGoals().get(g).clone();
							BoxToGoalPath boxToGoalPath = new BoxToGoalPath(box, goal);
							if (playerPathExistsToPreviouslyExploredBox(map, paths, boxIndx, boxToGoalPath)
								|| boxPathExists(map, paths, boxIndx, g)) {
									Map newMap = map.clone();
									newMap.applyMoves(paths[boxIndx], true);
									pathMap.put(boxToGoalPath, BoxToGoalPath.removeFirstPlayerMoves(paths[boxIndx]));
									isSolved = isSolved || findSequentialBoxToGoalPaths(newMap, paths, boxIndx+1);
									if (isSolved) break;
							}
					}
				}
				return isSolved;
			}
		}



	/**
	* Encapsulates findPath() in a boolean function and stores its result in paths[boxIndx].
	* @throws CloneNotSupportedException 
	* @throws IllegalMoveException 
	*
	*
	*/
	public boolean boxPathExists(Map m, String[] paths, int boxIndx, int g) throws CloneNotSupportedException, IOException, IllegalMoveException {
			try {
				paths[boxIndx] = astar.findPath(m, m.getBoxes().get(0).getPosition(), m.getGoals().get(g), ECell.BOX);
				return true;
			} 
			catch (PathNotFoundException e) {
				return false;
			}
	}

	public boolean playerPathExistsToPreviouslyExploredBox(Map m, String[] paths, int boxIndx, BoxToGoalPath boxToGoalPath) throws CloneNotSupportedException, IOException {
		if (!pathMap.containsKey(boxToGoalPath))
			return false;
		else {
			String boxToGoalString = pathMap.get(boxToGoalPath);
			char firstPushDir = boxToGoalString.charAt(0);
			Position playerPushStart = boxToGoalPath.getBoxPosition().unboundMove(PositionFinder.getOppositeDirection(firstPushDir));
			try {
				paths[boxIndx] = astar.findPath(m, m.getPlayerPosition(), playerPushStart, ECell.PLAYER).toLowerCase() + boxToGoalString;
				Map illegalMoveTestClone = m.clone();
				illegalMoveTestClone.applyMoves(paths[boxIndx]);
				hashedUsed++;
				return true;
			}
			catch (PathNotFoundException e) {
				System.out.println("Path not found");
				return false;
			}
			catch (IllegalMoveException il) {
				System.out.println("String no longer valid");
				return false;
			}
			
		}
	}
	
	/**
	 * Clean the agent variables
	 * 
	 * @author arthur
	 */
	protected void clean()
	{
		map = null;
		moves = new Moves();
		nodes = new LinkedList<Node>();
		astar = new AStarSearch();
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public void setMap(ArrayList<String> sList) {
		this.map = new Map(sList);
	}
	
	public void setMap(BufferedReader br) {
		this.map = new Map(br);
	}

	public Moves getMoves() {
		return moves;
	}

	public void setMoves(Moves moves) {
		this.moves = moves;
	}
        
        
    /**
     * 
     * Solve a given Map by returning the right string sequence
     * 
     * @author arthur
     * @param map
     * @return
     * @throws CloneNotSupportedException
     * @throws IOException
     */
    public String solve(Map map) throws CloneNotSupportedException, IOException, PathNotFoundException, IllegalMoveException {
    
		String result = "";
	    
		for(String s : getBoxToGoalPaths(map)) {
			result += s;
		}
		System.out.println("Number strings hashed: " + pathMap.size());
		System.out.println("Times string used from hash map: " + hashedUsed);

		return result;
	}

	public AStarSearch getAstar() {
		return astar;
	}

	public void setAstar(AStarSearch astar) {
		this.astar = astar;
	}

	
	
	
}
