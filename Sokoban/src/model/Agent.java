package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.LinkedList;
import java.util.List;

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
							if (pathExists(map, paths, boxIndx, g)) {
								Map newMap = map.clone();
								newMap.applyMoves(paths[boxIndx], true);
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
	*
	*
	*/
	public boolean pathExists(Map m, String[] paths, int boxIndx, int g) throws CloneNotSupportedException, PathNotFoundException, IOException {
		try 
		{
			
			try {
				paths[boxIndx] = astar.findPath(m, m.getBoxes().get(0).getPosition(), m.getGoals().get(g), ECell.BOX);
			} catch (IllegalMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		} 
		catch (PathNotFoundException e) 
		{
			return false;
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

		return result;
	}

	public AStarSearch getAstar() {
		return astar;
	}

	public void setAstar(AStarSearch astar) {
		this.astar = astar;
	}

	
	
	
}
