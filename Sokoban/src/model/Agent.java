package model;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;


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
	
	// TODO : represent a Node for the A*
	public class Node
	{
		protected Moves moves;
		
		public Node(Moves moves)
		{
			this.moves = moves;
		}

		public Moves getMoves() {
			return moves;
		}

		public void setMoves(Moves moves) {
			this.moves = moves;
		}
		
		
	}
	
	
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
	 * Heuristic Function : sort the positionsStart according to the minimum distance of the goals
	 * 
	 * @param positionsStart
	 * @param positionsGoal
	 * @return ArrayList<Position>
	 */
	public ArrayList<Position> heuristic(ArrayList<Position> positionsStart, ArrayList<Position> positionsGoal)
	{
		TreeMap<Integer, ArrayList<Position>> hashMap = new TreeMap<Integer, ArrayList<Position>>();
		
		
		for(Position position : positionsStart)
		{
			int distance = distance(position, positionsGoal);
			if(hashMap.containsKey(distance))
			{
				ArrayList<Position> oldList = hashMap.get(distance);
				oldList.add(position);
				hashMap.put(distance, oldList);
				
			}
			else
			{
				ArrayList<Position> newList = new ArrayList<Position>();
				newList.add(position);
				hashMap.put(distance, newList);
			}
		}
		
		
		// We construct the final ArrayList
		ArrayList<Position> result = new ArrayList<Position>();
		
		Iterator<Entry<Integer, ArrayList<Position>>> it = hashMap.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer, ArrayList<Position>> entry = it.next();
			if(entry.getValue().size() > 1)
			{
				for(Position position : entry.getValue())
				{
					result.add(position);
				}
			}
			else
			{
				if(entry.getValue().size() != 0)
				{
					result.add(entry.getValue().get(0));
				}
			}
		}
		
		return result;
	}
	
	public ArrayList<Position> heuristic2(ArrayList<Position> positionsStart, ArrayList<Position> positionsGoal)
	{
		TreeMap<Integer, ArrayList<Position>> hashMap = new TreeMap<Integer, ArrayList<Position>>();
		
		
		for(Position position : positionsStart)
		{
			int distance = distance(position, positionsGoal);
			if(hashMap.containsKey(distance))
			{
				ArrayList<Position> oldList = hashMap.get(distance);
				oldList.add(position);
				hashMap.put(distance, oldList);
				
			}
			else
			{
				ArrayList<Position> newList = new ArrayList<Position>();
				newList.add(position);
				hashMap.put(distance, newList);
			}
		}
		
		
		// We construct the final ArrayList
		ArrayList<Position> result = new ArrayList<Position>();
		
		Iterator<Entry<Integer, ArrayList<Position>>> it = hashMap.descendingMap().entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer, ArrayList<Position>> entry = it.next();
			if(entry.getValue().size() > 1)
			{
				for(Position position : entry.getValue())
				{
					result.add(position);
				}
			}
			else
			{
				if(entry.getValue().size() != 0)
				{
					result.add(entry.getValue().get(0));
				}
			}
		}
		
		return result;
	}
	
	public ArrayList<Position> findEmptySpacesAround(Position position)
	{
		return findEmptySpacesAround(position, map);
	}
	
	/**
	 * 
	 * Finds the empty spaces around the position on the map
	 * 
	 * @param position
	 * @param map
	 * @return ArrayList<Position> list of the available positions given the previous state node
	 */
	public ArrayList<Position> findEmptySpacesAround(Position position, Map map)
	{
		ArrayList<Position> positions = new ArrayList<Position>();
		
		Position upPosition = new Position(position.getI(), position.getJ());
		upPosition.up(map);
		Position downPosition = new Position(position.getI(), position.getJ());
		downPosition.down(map);
		Position leftPosition = new Position(position.getI(), position.getJ());
		leftPosition.left(map);
		Position rightPosition = new Position(position.getI(), position.getJ());
		rightPosition.right(map);
		
		if(
				(upPosition.getI() != position.getI() || upPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(upPosition.getI()).get(upPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR) 
					|| 
				map.getMap().get(upPosition.getI()).get(upPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			positions.add(upPosition);
		}
		if(
				(downPosition.getI() != position.getI() || downPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			positions.add(downPosition);
		}
		if(
				(rightPosition.getI() != position.getI() || rightPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			positions.add(rightPosition);
		}
		if(
				(leftPosition.getI() != position.getI() || leftPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			positions.add(leftPosition);
		}
		
		return positions;
	}

        	public ArrayList<Position> findEmptySpacesAround(Position position, Map map, Cell myCell)
	{
		ArrayList<Position> positions = new ArrayList<Position>();
		
		Position upPosition = new Position(position.getI(), position.getJ());
		upPosition.up(map);
		Position downPosition = new Position(position.getI(), position.getJ());
		downPosition.down(map);
		Position leftPosition = new Position(position.getI(), position.getJ());
		leftPosition.left(map);
		Position rightPosition = new Position(position.getI(), position.getJ());
		rightPosition.right(map);
		
		if(
                    // It is not the same position.
                    (upPosition.getI() != position.getI() || upPosition.getJ() != position.getJ()) 
                    && 
                    // And it is an empty floor.
                    (map.getMap().get(upPosition.getI()).get(upPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR) 
                            || 
                    // Or a goal.
                    map.getMap().get(upPosition.getI()).get(upPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
                    )                     
		)
		{
                    if (myCell.getType()==Cell.ECell.PLAYER)
                        // Then add the position.
			positions.add(upPosition);
                    else if (myCell.getType()==Cell.ECell.BOX)
                    {
                        //if position.
                        System.out.println("");
                    }
                                            
                          
		}
		if(
				(downPosition.getI() != position.getI() || downPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			positions.add(downPosition);
		}
		if(
				(rightPosition.getI() != position.getI() || rightPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			positions.add(rightPosition);
		}
		if(
				(leftPosition.getI() != position.getI() || leftPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			positions.add(leftPosition);
		}
		
		return positions;
	}

        
	/**
	 * 
	 * Creates a new Map based on the previous map and simulates a move of the player
	 * Everything is cloned, no edge effects
	 * 
	 * @param positionVisited
	 * @param newPlayerPosition
	 * @return Map modified
	 * @throws CloneNotSupportedException
	 */
	public Map createMapWithVisitedOnThePostion(Position positionVisited, Position newPlayerPosition) throws CloneNotSupportedException
	{
		return createMapWithVisitedOnThePostion(map, positionVisited, newPlayerPosition);
	}
	
	public Map createMapWithVisitedOnThePostion(Map map, Position positionVisited, Position newPlayerPosition) throws CloneNotSupportedException
	{
		if(map.isPositionOnTheMap(positionVisited) 
				&&
			map.isPositionOnTheMap(newPlayerPosition)
				&&
				(map.getCellFromPosition(positionVisited).getType().equals(Cell.ECell.EMPTY_FLOOR) 
					|| 
				map.getCellFromPosition(positionVisited).getType().equals(Cell.ECell.GOAL_SQUARE)
					||
				map.getCellFromPosition(positionVisited).getType().equals(Cell.ECell.PLAYER)
				)
				&&
				(map.getCellFromPosition(newPlayerPosition).getType().equals(Cell.ECell.EMPTY_FLOOR) 
					|| 
				map.getCellFromPosition(newPlayerPosition).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
			Map newMap = map.clone();
			newMap.movePlayer(newPlayerPosition);
			
			return newMap;
		}
		else
		{
			return map.clone();
		}
	}
	
	/**
	 * 
	 * Finds a path to a goal according to the heuristic function
	 * 
	 * @param moves
	 * @return String (Solution of the Maze : UDRLUUDD...)
	 * @throws CloneNotSupportedException
	 * @see heuristic()
	 */
	public String findPathToGoal(Moves moves) throws CloneNotSupportedException
	{
		return findPathToGoal(map, moves);
	}
	
	public String findPathToGoal(Map map, Moves moves) throws CloneNotSupportedException
	{
		// End point : we've found a goal!
		if(map.getCellFromPosition(map.getPlayerPosition()).getType().equals(Cell.ECell.PLAYER_ON_GOAL_SQUARE))
		{
			System.out.println(map);
			System.out.println(nodes.getLast().getMoves());
			return moves.toString();
		}
		else
		{
			//System.out.println(map);
			//System.out.println("Node Size : " + nodes.size());
			ArrayList<Position> positions = findEmptySpacesAround(map.getPlayerPosition(), map);
			
//			for(Position position : heuristic2(findEmptySpacesAround(map.getPlayerPosition(), map), map.getGoals()))
//			{
//				//Map newMap = createMapWithVisitedOnThePostion(map, map.getPlayerPosition(), position);
//				Moves newMoves = moves.clone();
//				newMoves.addMove(map.getPlayerPosition(), position);
//				
//				nodes.add(new Node(newMoves));
//			}
			//nodes.addAll(heuristic2(findEmptySpacesAround(map.getPlayerPosition(), map), map.getGoals()));
			
			// We put the 
			
			if(positions == null || positions.isEmpty())
			{
				System.out.println("No more positions...");
				// No empty spaces around : we backtrack in the tree
				return "";
			}
			else
			{
				//Position position =  nodes.removeLast();
				//Position position = null;
				//while((position = nodes.removeLast()) != null)
				for(Position position : heuristic(findEmptySpacesAround(map.getPlayerPosition(), map), map.getGoals()))
				//for(Position position : findEmptySpacesAround(map.getPlayerPosition(), map))
				{
					moves.addMove(map.getPlayerPosition(), position);
					//Moves newMoves = moves.clone();
					//newMoves.addMove(map.getPlayerPosition(), position);
					nodes.add(new Node(moves));
					//System.out.println(moves);
					String result = findPathToGoal(createMapWithVisitedOnThePostion(map, map.getPlayerPosition(), position), 
							moves.clone());
					
					// If we are in a dead end : we backtrack!!
					if(!result.equals(""))
					{
						// No solution for the problem
						return result;
					}
					else
					{
						// We pop the last move
						moves.pop();
						
					}
				}
			}
		}
		
		// Useless : only for the errors generated by Eclipse
		String result = "";
		return result;
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
}
