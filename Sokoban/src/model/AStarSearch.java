package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.Cell.ECell;

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
	 * @throws IllegalMoveException 
	 */
	public Moves search(Cell.ECell cellType) throws PathNotFoundException, CloneNotSupportedException, IOException, IllegalMoveException
	{
		
		PositionFinder pf = new PositionFinder();
		
		while(!openedList.isEmpty())
		{
			//System.out.println(map);
			//System.out.println("Opened List : " + openedList);
			//System.out.println("Closed List : " + closedList);
			
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
			closedList.add(current);
			
			// System.out.println("Initial map " + map);
			// map.set(ECell.EMPTY_FLOOR, current.getPosition());
		
			for(Node n : getNodesFromBoxMove(pf.findEmptySpacesAround(current.getPosition(), map, cellType)))
			{
				//System.out.println("Node " + n);
				
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
					
					//System.out.println(map);
					// We update the map
					Moves m = new Moves();
					m.addMove(n.parent.getPosition(), n.getPosition());
					Map mapCopy = map.clone();
					
					try
					{
						mapCopy.applyMoves(n.getBoxMove().getPlayerPath());
						mapCopy.applyMoves(m.toString());
					}
					catch(IllegalMoveException ill)
					{
						
					}
					
					// We store the map in the node
					n.setMap(mapCopy);
					
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
			System.out.println(currentNode.getMap());
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
			//map.set(ECell.BOX, start.getPosition());
			Moves result = movesResult.clone();
//			System.out.println("Final map : " + map);
//			System.out.println("Number boxes : " + map.getBoxes().size());
//			System.out.println("Box position : " + map.getBoxes().get(0));
			clean();
			return result;
		}
	}
	
	
	/**
	 * Find empty space around a given position
	 * @param position
	 * @return
	 * @throws IllegalMoveException 
	 * @see findEmptySpacesAround
	 */
	public ArrayList<Position> findEmptySpacesAround(Position position) throws CloneNotSupportedException, IOException, IllegalMoveException
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
     * @throws IllegalMoveException 
	 */
        public String findPath(Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException
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
	 * @throws IllegalMoveException 
	 */
	public String findPath(Map map, Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException, IOException, IllegalMoveException
	{

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
        {
	        try
	        {
	            return search(cellType).toString();
	        }
	        catch(PathNotFoundException e)
	        {
	        	System.out.println("Path Not Found !!! ");
	        	throw new PathNotFoundException();
	        }
	        catch(IllegalMoveException e)
	        {
	        	System.out.println("Illegal Move !!!!");
	        	throw new IllegalMoveException();
	        }
        }
                
//            } catch (PathNotFoundException e) {
//                    
//                    System.out.println("CAN NOT FIND PATH:");
//                    System.out.println("From: "+position1.toString()+"Type: "+map.getCellFromPosition(position1).getType());
//                    System.out.println("To: "+position2.toString()+"Type: "+map.getCellFromPosition(position2).getType());
//                    System.out.println();
//                    throw new
//                    return "";
//            }
                
                
		
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
	* @throws IOException 
    * @throws PathNotFoundException 
     * @throws IllegalMoveException 
	*/
        
        //Check if box can be moved in direction
	public String checkBoxDir(char Boxdir, Map map, Position PlayerPos, Position BoxPos) throws CloneNotSupportedException, IOException, PathNotFoundException, IllegalMoveException{
		String PlayerPath=new String();
		Position newPlayerPos=new Position();
		newPlayerPos=BoxPos.clone();
		if(Boxdir=='U'){newPlayerPos.down(map);}
		if(Boxdir=='D'){newPlayerPos.up(map);}
		if(Boxdir=='L'){newPlayerPos.right(map);}
		if(Boxdir=='R'){newPlayerPos.left(map);}
		if(newPlayerPos!=PlayerPos){
			PlayerPath = findPath(map.clone(),PlayerPos.clone(),newPlayerPos.clone(), ECell.PLAYER).toLowerCase();
		}
		
		if(PlayerPath != null)
		{
			PlayerPath=PlayerPath+Boxdir;
		}
		
		return PlayerPath;	
	}
        
        
        /*
         * @author: Luis F. Reina G.
         * @param:  position--> the position we want to check.         
         * @return: boolean true if we are allowed. 
         * This function should be used to test if we can move a box
         * to this position.
         * Upgrade: If the box should not move to a wall if there is no goal 
         *          in it.
         */
        public boolean isValidMove (Position tryPosition)
        {
            // Check if it is a Wall/Box or Box on Goal.
            Cell wall = map.getCellFromPosition(tryPosition);
            if (!(    wall.getType()==Cell.ECell.WALL || 
                    wall.getType()==Cell.ECell.BOX  ||
                    wall.getType()==Cell.ECell.BOX_ON_GOAL
                ))
                return true;
            else
                return false;                  
        }

        
        public boolean nearWallGoal(Map map, Position boxPosition,int offset,boolean UpDown)
        {
            // Define where are we going to sum the offset.
            int sumI = UpDown?offset:0;
            int sumJ = UpDown?0:offset;
            
            // Check if oneCellUp has a goal (worth moving there).
//            if (boxPosition.getI()+sumI>=map.height)
//                return false;
//            if (boxPosition.getJ()+sumJ>=map.width)
//                return false;
            Cell oneCellFurther = map.getCellFromPosition(new Position(boxPosition.getI()+sumI,boxPosition.getJ()+sumJ));


            // If this oneCellFurther is a wall.... then check if there is a goal in there.
            if (oneCellFurther.getType()==Cell.ECell.WALL)
                // 
                for (int goalCount=0; goalCount<map.getNumberOfGoals();goalCount++)
                {
                    // Get the goal Row and goal Column
                    if (UpDown)
                    {
                        int i = map.getGoals().get(goalCount).getI();
                        if (boxPosition.getI()==i) // || upPosition.getJ()==j)
                            return true;
                    }
                    else
                    {
                        int j = map.getGoals().get(goalCount).getJ();
                        if (boxPosition.getJ()==j)
                            return true;
                    }                    
                }
            return false;
        }
        
         /*
         * @author: Luis F. Reina G.
         * @param:  boxPosition--> the position of the box.
         *          Map --> the map
         *          moveWhre --> A char (e.g.'U' for up) where we want to move the box.
         * @return: boolean true if we are allowed. 
         * This function should be used to test if we can move a box
         * to a certain position. The player position is searched on the map.
         * 
         */    
        public boolean playerCanMoveBox (Position boxPosition, Map map, char moveWhere) throws CloneNotSupportedException, IOException, PathNotFoundException, IllegalMoveException
        {
            
            // Search for the player movement.
            Position player = map.getPlayerPosition();
            
            // Search for a valid path for the box to move.
            String validPath= checkBoxDir(moveWhere,map,player,boxPosition);/*Use Joakim's function*/
            // If there is a way then true.          
            if (validPath != null)
                return true;            
            else
                return false;          
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
        public ArrayList<Position> findEmptySpacesAround(Position position, Map map, Cell.ECell whoIsMoving) throws CloneNotSupportedException, IOException, IllegalMoveException
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
                        if (isValidMove(downPosition))
                        {
//                            Position boxPosition = position.clone();
//                            Map copy_map = map.clone();
//                            try{
//                                if (playerCanMoveBox(boxPosition,copy_map,'U'))
//                                    positions.add(upPosition);
//                            }
//                            catch (PathNotFoundException e){
//                                
//                            }      
                            // If your moving towards a wall with no goal--> Don't!
                            //if (nearWallGoal(map,upPosition,-1,true))
                                positions.add(upPosition);
                        }
                        
                            
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
                        if (isValidMove(upPosition))
                        {
                        	Position player = map.getPlayerPosition();
                        	//String validPath= checkBoxDir('D',map.clone(),player.clone(),position.clone());/*Use Joakim's function*/
                            
                            //System.out.println("validPath : "+validPath);
                            //if (validPath != null)
                        	try
                        	{
                        		String validPath= checkBoxDir('D',map.clone(),map.getPlayerPosition().clone(),position.clone());/*Use Joakim's function*/
                        		System.out.println("Player : " + map.getPlayerPosition());
                        		System.out.println("Box : " + position);
                        		System.out.println("Valid Path D : " + validPath);
                        		
                        		
                        		positions.add(downPosition);
                        	}
                        	catch(PathNotFoundException e)
                        	{
                        		System.out.println("crash");
                        	}
                        	
                            	//positions.add(downPosition);
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
                        if (isValidMove(leftPosition))
                        {

                        	Position player = map.getPlayerPosition();
                        	//String validPath= checkBoxDir('R',map.clone(),player.clone(),position.clone());/*Use Joakim's function*/
                            
                            //System.out.println("validPath : "+validPath);
                            //if (validPath != null)
                        	try
                        	{
                        		String validPath= checkBoxDir('R',map.clone(),map.getPlayerPosition().clone(),position.clone());/*Use Joakim's function*/
                        		System.out.println("Player : " + map.getPlayerPosition());
                        		System.out.println("Box : " + position);
                        		System.out.println("Valid Path R : " + validPath);
                        		
                        		
                        		positions.add(rightPosition);
                        	}
                        	catch(PathNotFoundException e)
                        	{
                        		System.out.println("crash");
                        	}
                            	//positions.add(rightPosition);

//                            Position boxPosition = position.clone();
//                            Map copy_map = map.clone();
//                            try{
//                                if (playerCanMoveBox(boxPosition,copy_map,'R'))
//                                    positions.add(rightPosition);
//                            }
//                            catch (PathNotFoundException e){
//                            }      
                            //if (nearWallGoal(map,rightPosition,+1,false))
                                positions.add(rightPosition);

                        }
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
                        if (isValidMove(rightPosition))
                        {

                        	Position player = map.getPlayerPosition();
                        	//String validPath= checkBoxDir('L',map.clone(),player.clone(),position.clone());/*Use Joakim's function*/
                            
                            //System.out.println("validPath : "+validPath);
                            //if (validPath != null)
                        	try
                        	{
                        		String validPath= checkBoxDir('L',map.clone(),map.getPlayerPosition().clone(),position.clone());/*Use Joakim's function*/
                        		System.out.println("Player : " + map.getPlayerPosition());
                        		System.out.println("Box : " + position);
                        		System.out.println("Valid Path L : " + validPath);
                        		
                        		
                        		positions.add(leftPosition);
                        	}
                        	catch(PathNotFoundException e)
                        	{
                        		System.out.println("crash");
                        	}
                            	//positions.add(leftPosition);

//                            Position boxPosition = position.clone();
//                            Map copy_map = map.clone();
//                            try{
//                                if (playerCanMoveBox(boxPosition,copy_map,'L'))
//                                    positions.add(leftPosition);
//                            }
//                            catch (PathNotFoundException e){
//                            }    
                            //if (nearWallGoal(map,leftPosition,-1,false))
                                positions.add(leftPosition);
                        }
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
