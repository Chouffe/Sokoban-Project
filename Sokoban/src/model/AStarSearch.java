package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.Cell.ECell;

import exception.PathNotFoundException;

public class AStarSearch 
{
	
	protected Node start, goal;
	protected Map map;
	protected int maxNodes;
	protected final int MAXNODES = 1000;
	
	protected List<Node> openedList = new LinkedList<Node>();
	protected List<Node> closedList = new LinkedList<Node>();
	
	protected Moves movesResult = new Moves();
	
	public AStarSearch()
	{
		maxNodes = MAXNODES;
		map = null;
	}
	
	public AStarSearch(int maxNodes)
	{
		this();
		this.maxNodes = maxNodes;
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
		
		this.movesResult = new Moves();
		
		// We add the start node into the open List
		openedList.add(start);
		
	}
	
	public Moves search(Cell.ECell cellType) throws PathNotFoundException
	{
		while(!openedList.isEmpty())
		{
			Collections.sort(openedList);
			Node current = (Node) ((LinkedList<Node>) openedList).getFirst();
			
			if(current.isGoal(goal))
			{
//				System.out.println(goal);
//				System.out.println("FOUND");
				return reconstructPath(current);
//				return;
				//return reconstructPath(current);
			}
			
			// We remove current from openedList
			openedList.remove(current);
			
			// We add current to closedList
			closedList.add(current);
			
//			System.out.println("OPENED");
//			System.out.println(openedList);
//			System.out.println("CLOSED");
//			System.out.println(closedList);
			
			//System.out.println(getNodesFromPosition(findEmptySpacesAround(current.getPosition())));
			for(Node n : getNodesFromPosition(findEmptySpacesAround(current.getPosition(), map, cellType)))
			{
				if(closedList.contains(n))
				{
					continue;
				}
				// We compute a new g tentative
				int tentativeGScore = current.getG() + current.distance(current.getPosition(), n.getPosition());
				//System.out.println("Tentative : " + tentativeGScore);
				
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
					map.set(ECell.VISITED, n.getPosition());
					System.out.println(map);
				}
			}
		}
		
		//System.out.println("END OF THE OPENED");
		throw new PathNotFoundException();
		//return null;
	}
	
	public Moves reconstructPath(Node currentNode)
	{
		if(currentNode.getParent() != null)
		{
			//System.out.println(currentNode);
			movesResult.addMove(currentNode.getPosition(), currentNode.getParent().getPosition());
			return reconstructPath(currentNode.getParent());
			
			
			
		}
		else
		{
			if(movesResult.getMoves().size() == 0)
			{
				return new Moves();
			}
			movesResult.reverse();
			movesResult.addMove(currentNode.getPosition(), goal.getPosition());
			return movesResult;
//			System.out.println(movesResult);
//			System.out.println(currentNode.toString());
		}
	}
	
	
	/**
	 * 
	 * Finds the empty spaces around the position on the map
	 * 
	 * @param position
	 * @param map
	 * @return ArrayList<Position> list of the available positions given the previous state node
	 */
//	public ArrayList<Position> findEmptySpacesAround(Position position)
//	{
//		ArrayList<Position> positions = new ArrayList<Position>();
//		
//		Position upPosition = new Position(position.getI(), position.getJ());
//		upPosition.up(map);
//		Position downPosition = new Position(position.getI(), position.getJ());
//		downPosition.down(map);
//		Position leftPosition = new Position(position.getI(), position.getJ());
//		leftPosition.left(map);
//		Position rightPosition = new Position(position.getI(), position.getJ());
//		rightPosition.right(map);
//		
//		if(
//				(upPosition.getI() != position.getI() || upPosition.getJ() != position.getJ()) 
//				&& 
//				(map.getMap().get(upPosition.getI()).get(upPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR) 
//					|| 
//				map.getMap().get(upPosition.getI()).get(upPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
//				)
//		)
//		{
//			positions.add(upPosition);
//		}
//		if(
//				(downPosition.getI() != position.getI() || downPosition.getJ() != position.getJ()) 
//				&& 
//				(map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
//					||
//				map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
//				)
//		)
//		{
//			positions.add(downPosition);
//		}
//		if(
//				(rightPosition.getI() != position.getI() || rightPosition.getJ() != position.getJ()) 
//				&& 
//				(map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
//					||
//				map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
//				)
//		)
//		{
//			positions.add(rightPosition);
//		}
//		if(
//				(leftPosition.getI() != position.getI() || leftPosition.getJ() != position.getJ()) 
//				&& 
//				(map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
//					||
//				map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
//				)
//		)
//		{
//			positions.add(leftPosition);
//		}
//		
//		return positions;
//	}
	
	public ArrayList<Position> findEmptySpacesAround(Position position)
	{
		return findEmptySpacesAround(position, map, Cell.ECell.BOX);
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
                    //System.out.println("FAIL.");
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

        /*
         * @author: Luis
         * This function should be used to move either the player or a box.
         */
        public ArrayList<Position> findEmptySpacesAround(Position position, Map map, Cell.ECell whoIsMoving)
	{
		ArrayList<Position> positions = new ArrayList<Position>();
		
                // Get the position.
		Position upPosition = new Position(position.getI(), position.getJ());
		upPosition.up(map);
		Position downPosition = new Position(position.getI(), position.getJ());
		downPosition.down(map);
		Position leftPosition = new Position(position.getI(), position.getJ());
		leftPosition.left(map);
		Position rightPosition = new Position(position.getI(), position.getJ());
		rightPosition.right(map);
		
                // Move UP!
		if(
                    // It is not the same position. There was a wall.
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
                    if (whoIsMoving==Cell.ECell.PLAYER){
                        // Then add the position.
			positions.add(upPosition);
                    }
                    else if (whoIsMoving==Cell.ECell.BOX)
                    {
                        // -------------------------------------
                        // If the box wants to move up --> Don't.
                        // -------------------------------------
                        // Check if the cell down is a wall, a box or a box on goal.
                        Cell wall = map.getCellFromPosition(downPosition);
                        if (!(    wall.getType()==Cell.ECell.WALL || 
                                wall.getType()==Cell.ECell.BOX  ||
                                wall.getType()==Cell.ECell.BOX_ON_GOAL
                            ))
                            positions.add(upPosition);
                        /*
                         * For Debbuging
                         */ 
                        //else{                             
                        //    System.out.println("Can't move up the box.");
                        //    System.out.println(map);
                        //}
                            
                    }
                                            
                          
		}
                // Move Down
		if(
                    (downPosition.getI() != position.getI() || downPosition.getJ() != position.getJ()) 
                    && 
                    (map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
                            ||
                    map.getMap().get(downPosition.getI()).get(downPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
                    )
		)
		{
                    // If the cell is a player--> He can move.
                    if (whoIsMoving==Cell.ECell.PLAYER){                        
			positions.add(downPosition);
                    }
                    // If the cell is a box...
                    else if (whoIsMoving==Cell.ECell.BOX)
                    {
                        // If the box wants to move down --> Don't.
                        // -------------------------------------
                        // Check if the cell up is a wall.                        
                        Cell wall = map.getCellFromPosition(upPosition);
                        if (!(    wall.getType()==Cell.ECell.WALL || 
                                wall.getType()==Cell.ECell.BOX  ||
                                wall.getType()==Cell.ECell.BOX_ON_GOAL
                            ))
                            positions.add(downPosition);
                            
                         /*
                         * For Debbuging
                         */ 
                        //else{
                        //    System.out.println("Can't move down the box.");
                        //    System.out.println(map);
                        //}
                        
                    }
		}
                
                // Moving to the right!
		if(
				(rightPosition.getI() != position.getI() || rightPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(rightPosition.getI()).get(rightPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
                    // -------------------------------------
                    // Again the same .... Check if there is
                    // a wall to the left and do not let it
                    // move if this is a box.
                    // -------------------------------------
                    if (whoIsMoving==Cell.ECell.PLAYER){
                        positions.add(rightPosition);
                    }
                    else if (whoIsMoving==Cell.ECell.BOX)
                    {   
                        Cell wall = map.getCellFromPosition(leftPosition);
                        if (!(    wall.getType()==Cell.ECell.WALL || 
                                wall.getType()==Cell.ECell.BOX  ||
                                wall.getType()==Cell.ECell.BOX_ON_GOAL
                            ))
                            positions.add(rightPosition);
                            
                        /*
                         * For debbuging
                         */
                        //else{
                        //    System.out.println("Can't move right the box.");
                        //    System.out.println(map);
                        //}
                    }
                    
		}
                // Moving to the left!
		if(
				(leftPosition.getI() != position.getI() || leftPosition.getJ() != position.getJ()) 
				&& 
				(map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.EMPTY_FLOOR)
					||
				map.getMap().get(leftPosition.getI()).get(leftPosition.getJ()).getType().equals(Cell.ECell.GOAL_SQUARE)
				)
		)
		{
                    // -------------------------------------
                    // Again the same .... Check if there is
                    // a wall to the left and do not let it
                    // move if this is a box.
                    // -------------------------------------
                    if (whoIsMoving==Cell.ECell.PLAYER){
                        positions.add(leftPosition);
                    }
                    else if (whoIsMoving==Cell.ECell.BOX)
                    {   
                        Cell wall = map.getCellFromPosition(rightPosition);
                        if (!(  wall.getType()==Cell.ECell.WALL || 
                                wall.getType()==Cell.ECell.BOX  ||
                                wall.getType()==Cell.ECell.BOX_ON_GOAL
                            ))
                            positions.add(leftPosition);
                        /*else{                            
                            System.out.println("Can't move left the box.");
                            System.out.println(map);
                        }*/
                    }
		
		}
		
		return positions;
	}
	
	
	public List<Node> getNodesFromPosition(List<Position> positionList)
	{
		List<Node> nodeList = new ArrayList<Node>();
		
		for(Position p : positionList)
		{
			nodeList.add(new Node(p));
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

	public List<Node> getClosedList() {
		return closedList;
	}

	public void setClosedList(List<Node> closedList) {
		this.closedList = closedList;
	}
	
	
	
}
