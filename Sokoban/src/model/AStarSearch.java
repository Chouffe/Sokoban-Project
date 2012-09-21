package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.Cell.ECell;

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
	
	protected List<Node> openedList = new LinkedList<Node>();
	protected List<Node> closedList = new LinkedList<Node>();
	
	protected Moves movesResult = new Moves();
	
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
	 */
	public Moves search(Cell.ECell cellType) throws PathNotFoundException, CloneNotSupportedException, IOException
	{
		while(!openedList.isEmpty())
		{
			Collections.sort(openedList);
			Node current = (Node) ((LinkedList<Node>) openedList).getFirst();
			
			if(current.isGoal(goal))
			{
				return reconstructPath(current);
			}
			
			// We remove current from openedList
			openedList.remove(current);
			
			// We add current to closedList
			closedList.add(current);
			map.set(ECell.EMPTY_FLOOR, current.getPosition());
		
			for(Node n : getNodesFromPosition(findEmptySpacesAround(current.getPosition(), map, cellType)))
			{
				if(closedList.contains(n))
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
					
					//map.set(ECell.VISITED, n.getPosition());
					//System.out.println(map);
				}
			}
		}
		
		// No path found : we throw an exception
		throw new PathNotFoundException();
	}
	
	/**
	 * Auxiliary function for the AStar search
	 * Reconstruct the path from a child node to its parent recursively
	 * @param currentNode
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	public Moves reconstructPath(Node currentNode) throws CloneNotSupportedException
	{
		if(currentNode.getParent() != null)
		{
			movesResult.addMove(currentNode.getPosition(), currentNode.getParent().getPosition());
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
			map.set(ECell.BOX, start.getPosition());
			Moves result = movesResult.clone();
			clean();
			return result;
		}
	}
	
	
	/**
	 * Find empty space around a given position
	 * @param position
	 * @return
	 * @see findEmptySpacesAround
	 */
	public ArrayList<Position> findEmptySpacesAround(Position position) throws CloneNotSupportedException, IOException
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
        	/**
	 * 
	 * Find a path from one position to another
	 * @return 
	 * @throws CloneNotSupportedException 
	 * @throws PathNotFoundException 
	 */
        public String findPath(Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException, IOException
	{
		
		return findPath(map, position1, position2, cellType);
	}
	
	/**
	 * 
	 * @author arthur
	 * @param map
	 * @param position1
	 * @param position2
	 * @return The moves that the player has to do to complete the path
	 * @throws CloneNotSupportedException
	 * @throws PathNotFoundException
	 */
	public String findPath(Map map, Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException, PathNotFoundException, PathNotFoundException, PathNotFoundException, IOException
	{

            try{
		clean();
		
		setMap(map.clone());
		setStartAndGoalNode(new Node(position1), new Node(position2));
                
                int position1I = position1.getI();
                int position1J = position1.getJ();
                int position2I = position2.getI();
                int position2J = position2.getJ();
                
                // There was a problem with single moving boxes. 
                if (position1I-position2I==1 && position1J-position2J==0)
                    return "U";
                else if (position1I-position2I==-1  && position1J-position2J ==0)
                    return "D";
                else if (position1J-position2J==1   && position1I-position2I ==0)
                    return "L";
                else if (position1J-position2J==-1  && position1I-position2I ==0)
                    return "R";
                else
                    return search(cellType).toString();
            } catch (PathNotFoundException e) {
                    
                    System.out.println("CAN NOT FIND PATH:");
                    System.out.println("From: "+position1.toString()+"Type: "+map.getCellFromPosition(position1).getType());
                    System.out.println("To: "+position2.toString()+"Type: "+map.getCellFromPosition(position2).getType());
                    System.out.println();
                    return "";
            }                
		
		
	}

        	/**
	* Checks if a a player can move the box in the given direction
	*
	*
	* @author Joakim Andrén <joaandr@kth.se>
	* @param Map map
	* @param Boxdir is the given box diretion
	* @param BoxPos Position, position of the box
	* @param PlayerPos Position, position of the player
	* @throws CloneNotSupportedException 
	 * @throws IOException 
	*/
        
        //Check if box can be moved in direction
	public String checkBoxDir(char Boxdir, Map map, Position PlayerPos, Position BoxPos) throws CloneNotSupportedException, IOException{
		String PlayerPath=new String();
		Position newPlayerPos=new Position();
		newPlayerPos=BoxPos.clone();
		if(Boxdir=='U'){newPlayerPos.down(map);}
		if(Boxdir=='D'){newPlayerPos.up(map);}
		if(Boxdir=='L'){newPlayerPos.right(map);}
		if(Boxdir=='R'){newPlayerPos.left(map);}
		if(newPlayerPos!=PlayerPos){
			try {
					PlayerPath=findPath(map.clone(),PlayerPos.clone(),newPlayerPos.clone(), ECell.PLAYER).toLowerCase(); 
				} catch (PathNotFoundException e) {
					return null;
				}
		}
		PlayerPath=PlayerPath+Boxdir;
		return PlayerPath;	
	}
        
      public boolean GoalNearWall (Position actualPosition, int movement, boolean UpDown)
        {
            //if (upPosition.getI()-1>=0){ 
                                //---> Not going to check this, it will work by faith!!!
                                // Check if oneCellUp has a goal (worth moving there).
                                int sumI = UpDown?movement:0;
                                int sumJ = UpDown?0:movement;
                                Cell oneCellUp = map.getCellFromPosition(new Position(actualPosition.getI()+ sumI,actualPosition.getJ()+sumJ));
                                
                                
                                // If this oneCellUp is a wall.... then check if there is a goal in there.
                                if (oneCellUp.getType()==Cell.ECell.WALL)
                                    // 
                                    for (int goalCount=0; goalCount<map.getNumberOfGoals();goalCount++)
                                    {
                                        // Get the goal Row
                                        int i = map.getGoals().get(goalCount).getI();
                                        // Get the goal Column
                                        //int j = map.getGoals().get(goalCount).getJ();
                                        // Check if there is a goal on the Row. 
                                        if (actualPosition.getI()==i) // || upPosition.getJ()==j)
                                            return true;                                        
                                    }
                                    return false;
//                                else{
//                                     // Add it.
//                                    // Search for the player movement.
//                                    Position player = map.getPlayerPosition();
//                                    // Search for a valid path for the box to move.
//                                    // Map map
//                                    // Box position = position
//                                    // Box direction position = leftPosition
//                                    // Player position = player.
//                                    String validPath= checkBoxDir('U',map,player,position);/*Use Joakim's function*/
//                                    // Si no se puede mover a la derecha porque el jugador no puede llegar.
//                                    if (validPath != null)
//                                        positions.add(upPosition);
//                                    else
//                                        System.out.println("You shall not pass!");
//                                }
                              
        }

      
      
      
        public Position BoxCanMove (Position whereToMove, Position whereToCheck, Position whereAmI, Map map) throws CloneNotSupportedException, IOException
        {
            // -------------------------------------
            // If the box wants to move up --> Don't.
            // -------------------------------------
            // Check if the cell down is a wall, a box or a box on goal.
            Cell wall = map.getCellFromPosition(whereToCheck);
            if (!(  wall.getType()==Cell.ECell.WALL || 
                    wall.getType()==Cell.ECell.BOX  ||
                    wall.getType()==Cell.ECell.BOX_ON_GOAL
                ))

                {
                    // If you are going to a wall with no goal --> Don't go there.
                    if (GoalNearWall(whereToMove,-1,true)) {
                        return whereToMove;
                    }                                
                    
                         // Add it.
                        // Search for the player movement.
                        Position player = map.getPlayerPosition();
                        // Search for a valid path for the box to move.
                        // Map map
                        // Box position = position
                        // Box direction position = leftPosition
                        // Player position = player.
                        String validPath= checkBoxDir('U',map,player,whereAmI);/*Use Joakim's function*/
                        // Si no se puede mover a la derecha porque el jugador no puede llegar.
                        System.out.println("validPath : "+validPath);
                        if (validPath != null)
                            return whereToMove;
                        else
                            System.out.println("You shall not pass!");
                    
                                            /*
                         * For Debbuging
                         */ 
                        //else{                             
                        //    System.out.println("Can't move away the box.");
                        //    System.out.println(map);
                        //}
                }
            return null;

            /*
             * For Debbuging
             */ 
            //else{                             
            //    System.out.println("Can't move up the box.");
            //    System.out.println(map);
            //}            
        }
        /*
         * @author: Luis F. Reina G.
         * @param:  position--> where the player/box is
         *          map --> The actual map.
         *          whoIsMoving --> If it is a player or a box.
         * @return: ArrayList with the positions the player/box is allowed
         *          to move.
         * This function should be used to move either the player or a box.
         * The box can not move out of a wall or move if there is an obstacle
         * that is going to block the movement.
         * Upgrade: If the box should not move to a wall if there is no goal 
         *          in it.
         */
        public ArrayList<Position> findEmptySpacesAround(Position position, Map map, Cell.ECell whoIsMoving) throws CloneNotSupportedException, IOException
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
                        {
                        	Position player = map.getPlayerPosition();
                        	String validPath= checkBoxDir('U',map.clone(),player.clone(),position.clone());/*Use Joakim's function*/
                            
                            //System.out.println("validPath : "+validPath);
                            //if (validPath != null)
                            positions.add(upPosition);
                        }
                            
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
                        {
                        	Position player = map.getPlayerPosition();
                        	//String validPath= checkBoxDir('D',map.clone(),player.clone(),position.clone());/*Use Joakim's function*/
                            
                            //System.out.println("validPath : "+validPath);
                            //if (validPath != null)
                            	positions.add(downPosition);
                        }
                        	//positions.add(downPosition);
                            
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
                        {
                        	Position player = map.getPlayerPosition();
                        	//String validPath= checkBoxDir('R',map.clone(),player.clone(),position.clone());/*Use Joakim's function*/
                            
                            //System.out.println("validPath : "+validPath);
                            //if (validPath != null)
                            	positions.add(rightPosition);
                        }
                            //positions.add(rightPosition);
                            
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
                        {
                        	Position player = map.getPlayerPosition();
                        	//String validPath= checkBoxDir('L',map.clone(),player.clone(),position.clone());/*Use Joakim's function*/
                            
                            //System.out.println("validPath : "+validPath);
                            //if (validPath != null)
                            	positions.add(leftPosition);
                        }
                        /*else{                            
                            System.out.println("Can't move left the box.");
                            System.out.println(map);
                        }*/
                    }

		}

		return positions;
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
	
	protected void clean()
	{
		
		openedList = new LinkedList<Node>();
		closedList = new LinkedList<Node>();
		
		movesResult = new Moves();
	}
	
}
