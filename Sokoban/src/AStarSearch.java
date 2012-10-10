

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


import exception.IllegalMoveException;
import exception.PathNotFoundException;
import java.io.IOException;


/**
 * Class to handle the AStar Search on map
 * @author arthur
 *
 */
public class AStarSearch 
{
	
	protected Node start, goal;
	protected Map map;	
	protected Hashtable<Integer, Node> closedTable = new Hashtable<Integer, Node>();
	protected List<Node> openedList = new LinkedList<Node>();
	protected Moves movesResult = new Moves();
	protected StringBuffer finalString = new StringBuffer("");
	
	public AStarSearch()
	{
		map = null;
	}
	
	public AStarSearch(Map map)
	{
		this();
		this.map = map;
	}
	
	public void setStartAndGoalNode(Node start, Node goal)
	{
		// Initialization
		this.start = start;
		this.goal = goal;
		
		this.start.setG(0);
		this.start.setH(this.start.goalDistanceEstimate(goal));
		this.start.setF(this.start.getG() + this.start.getH());
		
		try {
			this.start.setMap(map.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		this.movesResult = new Moves();
		
		// We add the start node into the open List
		openedList.add(start);
		
	}
	
	/**
	 * Implementation of the AStar Search
	 * @param cellType
	 * The cellType should be either a PLAYER or a BOX
	 * @return
	 * @throws PathNotFoundException
	 * @throws CloneNotSupportedException 
	 * @throws IllegalMoveException 
	 */
	public Moves search(Cell.ECell cellType) throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		PositionFinder pf = new PositionFinder();
		
		while(!openedList.isEmpty())
		{
			Collections.sort(openedList);
			Node current = (Node) ((LinkedList<Node>) openedList).getFirst();
			
			// If we have reached the goal we construct the path
			if(current.isGoal(goal))
			{
				return reconstructPath(current);
			}
			
			// We remove current from openedList
			openedList.remove(current);
			
			// We add current to closedList
			closedTable.put(current.hashCode(), current);
			
			for(Node n : getNodesFromBoxMove(pf.findEmptySpacesAround(current.getPosition().clone(), current.getMap(), cellType)))
			{
				n.parent = current;
				
				// We update the map
				Map mapCopy = n.parent.getMap().clone();
				
				try
				{	
					mapCopy.applyMoves(n.getBoxMove().getPlayerPath());
					n.setMap(mapCopy);
				}
				catch(IllegalMoveException ill)
				{
					System.out.println("FAILLLLLL");
					continue;
				}
				
				if(closedTable.containsKey(n.hashCode()))
				{
					continue;
				}
				
				// We compute a new g tentative
				int tentativeGScore = current.getG() + current.distance(current.getPosition(), n.getPosition());
				
				if(!openedList.contains(n) || tentativeGScore < n.getG())
				{
					if(!openedList.contains(n))
					{
						openedList.add(n);
					}
					
					n.parent = current;
					n.setG(tentativeGScore);
					n.setH(n.goalDistanceEstimate(goal));
					n.setF(n.getG() + n.getH());
					
					// We update the map
					mapCopy = n.parent.getMap().clone();
					
					try
					{
						mapCopy.applyMoves(n.getBoxMove().getPlayerPath());	
						// We store the map in the node
						n.setMap(mapCopy);
					}
					catch(IllegalMoveException ill)
					{
						System.out.println("FAILLLLLL");
						continue;
					}
				}
			}
		}
		
		// No path found : we throw an exception
		throw new PathNotFoundException();
	}
	
	/**
	 * Auxiliary function for the AStar search
	 * Reconstruct the path from a child node to its parent recursively
	 * @author arthur
	 * @param currentNode
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	public Moves reconstructPath(Node currentNode) throws CloneNotSupportedException
	{
		if(currentNode.getParent() != null)
		{
			movesResult.addMove(currentNode.getPosition(), currentNode.getParent().getPosition());
			
			StringBuffer path = new StringBuffer(currentNode.getBoxMove().getPlayerPath());
			path.append(finalString);
			finalString = path;

//			System.out.println("Path : " + currentNode.getBoxMove().getPlayerPath());
//			System.out.println("Reconstruction \n" + currentNode.getMap());
//			System.out.println("Current final String : " + finalString.toString());
			
			return reconstructPath(currentNode.getParent());	
		}
		else
		{
			if(movesResult.getMoves().size() == 0)
			{
				clean();
				return new Moves();
			}
			// We need to reverse the way to get 
			movesResult.reverse();
			movesResult.addMove(currentNode.getPosition(), goal.getPosition());
			Moves result = movesResult.clone();
			clean();
	
			return result;
		}
	}
	
	
     /**
	 * 
	 * Find a path from one position to another
	 * 
	 * @return 
	 * @throws CloneNotSupportedException 
	 * @throws PathNotFoundException 
     * @throws IllegalMoveException 
	 */
    public String findPath(Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException
	{	
		return findPath(map, position1, position2, cellType);
	}
	
	/**
	 * Finds a path between 2 positions on a given map for a given ECell type
	 * 
	 * @author arthur
	 * @param map
	 * @param position1
	 * @param position2
	 * @return The moves that the player has to do to complete the path
	 * @throws CloneNotSupportedException
	 * @throws PathNotFoundException
	 * @throws IllegalMoveException 
	 */
	public String findPath(Map map, Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException
	{
		// We clean all the variables before doing the search
		clean();
		finalString = new StringBuffer();
		
		// We bound the map and the positions to the AstarSearch
		setMap(map.clone());
		setStartAndGoalNode(new Node(position1), new Node(position2));         

        try
        {
        	search(cellType).toString();
			return finalString.toString();
        }
        catch(IllegalMoveException e)
        {
        	System.out.println("Illegal Move !!!!");
        	throw new IllegalMoveException();
        }
	}
        
	
	/**
	 * Convert List of Positions to List of Nodes
	 * @param positionList
	 * @return List of Nodes
	 */
	public List<Node> getNodesFromPosition(List<Position> positionList)
	{
		List<Node> nodeList = new ArrayList<Node>();
		
		for(Position p : positionList)
		{
			nodeList.add(new Node(p));
		}
		
		return nodeList;
	}
	
	/**
	 * Convert List of Positions to List of Nodes
	 * @param positionList
	 * @return List of Nodes
	 */
	public List<Node> getNodesFromBoxMove(List<BoxMove> bmList)
	{
		List<Node> nodeList = new ArrayList<Node>();
		
		for(BoxMove bm : bmList)
		{
			Node n = new Node(bm.getNewPosition());
			n.setBoxMove(bm);
			nodeList.add(n);
		}
		
		return nodeList;
	}

	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Node getStart() {
		return start;
	}

	public void setStart(Node start) {
		this.start = start;
	}

	public Node getGoal() {
		return goal;
	}

	public void setGoal(Node goal) {
		this.goal = goal;
	}

	public List<Node> getOpenedList() {
		return openedList;
	}

	public void setOpenedList(List<Node> openedList) {
		this.openedList = openedList;
	}
	
	/**
	 * Cleans the openedList and the movesResult
	 * @author arthur
	 */
	protected void clean()
	{
		
		openedList = new LinkedList<Node>();
		closedTable.clear();
		
		movesResult = new Moves();
	}

	public Moves getMovesResult() {
		return movesResult;
	}

	public void setMovesResult(Moves movesResult) {
		this.movesResult = movesResult;
	}

	public StringBuffer getFinalString() {
		return finalString;
	}

	public void setFinalString(StringBuffer finalString) {
		this.finalString = finalString;
	}
}
