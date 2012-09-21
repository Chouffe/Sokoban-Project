package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;

import model.Cell.ECell;

import exception.PathNotFoundException;
import java.io.IOException;


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
	
	
	
	public Map setCellAccessible(Map map)
	{
		
		List<Node> lNodes = new LinkedList<Node>();
		List<Node> lNodesVisited = new LinkedList<Node>();
		
		if(map.getPlayerPosition().equals(null))
		{
			// TODO : throw an exception instead
			return null;
		}
		lNodes.add(new Node(map.getPlayerPosition()));
		map.getCellFromPosition(map.getPlayerPosition()).setAccessible(true);
		//System.out.println("Nodes : "+ lNodes);
		
		Position current;
		
		while(!lNodes.isEmpty())
		{
			current = ((LinkedList<Node>)lNodes).getFirst().getPosition();
			//System.out.println(current);
			
			
			for(Position p : findEmptySpacesAround(current, map))
			{
				Node n = new Node(p);
				
				//System.out.println("visited : "+ lNodesVisited);
				if(!lNodesVisited.contains(n))
				{
					//System.out.println("test" + p);
					map.getCellFromPosition(p).setAccessible(true);
					
					lNodes.add(n);
				}	
			}
			
			lNodesVisited.add(new Node(current));
			lNodes.remove(new Node(current));
		}
		
		//map.toStringAccessible();
		return map;
	}
	
	
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
		System.out.println(map);
                // Get the position.
		Position upPosition = new Position(position.getI(), position.getJ());
		upPosition.up(map);
		Position downPosition = new Position(position.getI(), position.getJ());
		downPosition.down(map);
		Position leftPosition = new Position(position.getI(), position.getJ());
		leftPosition.left(map);
		Position rightPosition = new Position(position.getI(), position.getJ());
		rightPosition.right(map);
		
                //               ^
                //              / \
                // Move UP!      |
		//               | 
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
                        if (!(  wall.getType()==Cell.ECell.WALL || 
                                wall.getType()==Cell.ECell.BOX  ||
                                wall.getType()==Cell.ECell.BOX_ON_GOAL
                            ))

                            {
                                //if (upPosition.getI()-1>=0){ 
                                //---> Not going to check this, it will work by faith!!!
                                // Check if oneCellUp has a goal (worth moving there).
                                Cell oneCellUp = map.getCellFromPosition(new Position(upPosition.getI()-1,upPosition.getJ()));
                                
                                
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
                                        if (upPosition.getI()==i) // || upPosition.getJ()==j)
                                            positions.add(upPosition);
                                        else
                                        {
                                            System.out.println("Don't go Up!");
                                            //System.out.println(map);
                                        }
                                    }
                                else{
                                     // Add it.
                                    // Search for the player movement.
                                    Position player = map.getPlayerPosition();
                                    // Search for a valid path for the box to move.
                                    // Map map
                                    // Box position = position
                                    // Box direction position = leftPosition
                                    // Player position = player.
                                    String validPath= checkBoxDir('U',map,player,position);/*Use Joakim's function*/
                                    // Si no se puede mover a la derecha porque el jugador no puede llegar.
                                    if (validPath != null)
                                        positions.add(upPosition);
                                    else
                                        System.out.println("You shall not pass!");
                                }
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
                //              |
                // Move Down    |
                //              V
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
                            //positions.add(downPosition);
                            {
                                //if (upPosition.getI()-1>=0){ 
                                //---> Not going to check this, it will work by faith!!!
                                // Check if oneCellUp has a goal (worth moving there).
                                Cell oneCellDown = map.getCellFromPosition(new Position(downPosition.getI()+1,downPosition.getJ()));
                                
                                
                                // If this oneCellUp is a wall.... then check if there is a goal in there.
                                if (oneCellDown.getType()==Cell.ECell.WALL)
                                    // 
                                    for (int goalCount=0; goalCount<map.getNumberOfGoals();goalCount++)
                                    {
                                        // Get the goal Row
                                        int i = map.getGoals().get(goalCount).getI();
                                        // Get the goal Column
                                        //int j = map.getGoals().get(goalCount).getJ();
                                        // Check if there is a goal on the Row. 
                                        if (upPosition.getI()==i) // || upPosition.getJ()==j)
                                            positions.add(downPosition);
                                        else
                                        {
                                            System.out.println("Don't go Down!");
                                            //System.out.println(map);
                                        }
                                    }
                                else
                                {
                                     // Add it.
                                    // Search for the player movement.
                                    Position player = map.getPlayerPosition();
                                    // Search for a valid path for the box to move.
                                    // Map map
                                    // Box position = position
                                    // Box direction position = leftPosition
                                    // Player position = player.
                                    String validPath= checkBoxDir('D',map,player,position);/*Use Joakim's function*/
                                    // Si no se puede mover a la derecha porque el jugador no puede llegar.
                                    if (validPath != null)
                                        positions.add(downPosition);
                                    else
                                        System.out.println("You shall not pass!");
                                }
                                    // Add it.
                                    //positions.add(downPosition);
                            }  
                            
                         /*
                         * For Debbuging
                         */ 
                        //else{
                        //    System.out.println("Can't move down the box.");
                        //    System.out.println(map);
                        //}
                        
                    }
		}
                
                // ------->
                //
                // Moving to the right!     ----->
                //
                // ------->
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
                        if (!(  wall.getType()==Cell.ECell.WALL || 
                                wall.getType()==Cell.ECell.BOX  ||
                                wall.getType()==Cell.ECell.BOX_ON_GOAL
                            ))
                            //positions.add(rightPosition);
                            {
                                //if (upPosition.getI()-1>=0){ 
                                //---> Not going to check this, it will work by faith!!!
                                // Check if oneCellUp has a goal (worth moving there).
                                Cell oneCellRight = map.getCellFromPosition(new Position(rightPosition.getI(),rightPosition.getJ()+1));
                                
                                
                                // If this oneCellUp is a wall.... then check if there is a goal in there.
                                if (oneCellRight.getType()==Cell.ECell.WALL)
                                    // 
                                    for (int goalCount=0; goalCount<map.getNumberOfGoals();goalCount++)
                                    {
                                        // Get the goal Row
                                        int i = map.getGoals().get(goalCount).getI();
                                        // Get the goal Column
                                        //int j = map.getGoals().get(goalCount).getJ();
                                        // Check if there is a goal on the Row. 
                                        if (upPosition.getI()==i) // || upPosition.getJ()==j)
                                            positions.add(rightPosition);
                                        else
                                        {
                                            System.out.println("Don't go Right!");
                                            //System.out.println(map);
                                        }
                                    }
                                else
                                {
                                     // Add it.
                                    // Search for the player movement.
                                    Position player = map.getPlayerPosition();
                                    // Search for a valid path for the box to move.
                                    // Map map
                                    // Box position = position
                                    // Box direction position = leftPosition
                                    // Player position = player.
                                    String validPath= checkBoxDir('R',map,player,position);/*Use Joakim's function*/
                                    // Si no se puede mover a la derecha porque el jugador no puede llegar.
                                    if (validPath != null)
                                        positions.add(rightPosition);
                                    else
                                        System.out.println("You shall not pass!");
                                }
                            }  
                            
                        /*
                         * For debbuging
                         */
                        //else{
                        //    System.out.println("Can't move right the box.");
                        //    System.out.println(map);
                        //}
                    }
                    
		}
                //      <----------
                //
                // Moving to the left!    <----------
                //
                //      <----------
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
                            //positions.add(leftPosition);
                            {
                                //if (upPosition.getI()-1>=0){ 
                                //---> Not going to check this, it will work by faith!!!
                                // Check if oneCellUp has a goal (worth moving there).
                                Cell oneCellLeft = map.getCellFromPosition(new Position(leftPosition.getI(),leftPosition.getJ()-1));
                                
                                
                                // If this oneCellUp is a wall.... then check if there is a goal in there.
                                if (oneCellLeft.getType()==Cell.ECell.WALL)
                                    // 
                                    for (int goalCount=0; goalCount<map.getNumberOfGoals();goalCount++)
                                    {
                                        // Get the goal Row
                                        int i = map.getGoals().get(goalCount).getI();
                                        // Get the goal Column
                                        //int j = map.getGoals().get(goalCount).getJ();
                                        // Check if there is a goal on the Row. 
                                        if (upPosition.getI()==i) // || upPosition.getJ()==j)
                                            positions.add(leftPosition);
                                        else
                                        {
                                            System.out.println("Don't go Left!");
                                            //System.out.println(map);
                                        }
                                    }
                                else
                                {                                    
                                    // Add it.
                                    // Search for the player movement.
                                    Position player = map.getPlayerPosition();
                                    // Search for a valid path for the box to move.
                                    // Map map
                                    // Box position = position
                                    // Box direction position = leftPosition
                                    // Player position = player.
                                    String validPath= checkBoxDir('L',map,player,position);/*Use Joakim's function*/
                                    // Si no se puede mover a la derecha porque el jugador no puede llegar.
                                    if (validPath != null)
                                        positions.add(leftPosition);
                                    else
                                        System.out.println("You shall not pass!");
                                }
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
	 * 
	 * Find a path from one position to another
	 * @return 
	 * @throws CloneNotSupportedException 
	 * @throws PathNotFoundException 
	 */
	public String findPath(Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException
	{
		moves = new Moves();
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
	public String findPath(Map map, Position position1, Position position2, Cell.ECell cellType) throws CloneNotSupportedException, PathNotFoundException
	{

            try{
		clean();
		
		astar.setMap(map.clone());
		astar.setStartAndGoalNode(new Node(position1), new Node(position2));
                
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
                    return astar.search(cellType).toString();
            } catch (PathNotFoundException e) {
                    
                    System.out.println("CAN NOT FIND PATH:");
                    System.out.println("From: "+position1.toString()+"Type: "+map.getCellFromPosition(position1).getType());
                    System.out.println("To: "+position2.toString()+"Type: "+map.getCellFromPosition(position2).getType());
                    System.out.println();
                    return "";
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
	public String[] getBoxToGoalPaths(Map map) throws CloneNotSupportedException {
		String[] paths = new String[map.getNumberOfBoxes()];
		ArrayList<Box> orderedBoxes = new ArrayList<Box>();
		findBoxToGoalPaths(orderedBoxes, map, paths);
		
		return paths;
	}

	private boolean findBoxToGoalPaths(ArrayList<Box> orderedBoxes, Map map, String[] paths) throws CloneNotSupportedException {
		if (map.getNumberOfBoxes() == 0) {
			map.setBoxes(orderedBoxes);
			if (findSequentialBoxToGoalPaths(map, paths, 0)) return true;
			}
		else {
			boolean isSolved = false;
			for (int b=0; b<map.getNumberOfBoxes(); b++) {
				orderedBoxes.add(map.getBoxes().get(b));
				map.getBoxes().remove(b);
				isSolved = isSolved || findBoxToGoalPaths(orderedBoxes, map, paths); 
				if (isSolved) break;
			}
		return isSolved;
		}
		
		return false;
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
	private boolean findSequentialBoxToGoalPaths(Map map, String[] paths, int boxIndx) throws CloneNotSupportedException {
		
		if (map.getBoxes().isEmpty()) return true;
		else {
			boolean isSolved = false;
				for (int g = 0; g<map.getNumberOfGoals(); g++) {
					System.out.println("G : " + g);
					if (map.getCellFromPosition(map.getGoals().get(g)).getType() != Cell.ECell.BOX_ON_GOAL) {
						if (pathExists(map, paths, boxIndx, g, Cell.ECell.BOX)) {
							Map newMap = map.clone();
                                                         
							updateMapWithBoxOnGoal(newMap, g);

                                                        
                                                        // -------------------------

                                                                                                                // -------------------------

                                                        // These three lines are making problems
                                                        // I Rolled back the code and it seems to work.
                                                        // -------------------------
                                                        
                                                        
//                                                        newMap.putBoxOnGoal(newMap.getBoxes().get(0), newMap.getGoals().get(g), paths[boxIndx]);
//                                                        newMap.getGoals().remove(g);
//                                                        newMap.getBoxes().remove(0);




							isSolved = isSolved || findSequentialBoxToGoalPaths(newMap, paths, ++boxIndx);
							if (isSolved) break;
						}
					}
				}
				return isSolved;
			}
	
		}
        
        private void updateMapWithBoxOnGoal(Map map, int goalIndx) {
			map.set(Cell.ECell.WALL, map.getGoals().get(goalIndx));
			map.set(Cell.ECell.EMPTY_FLOOR, map.getBoxes().get(0).getPosition());
			map.getGoals().remove(goalIndx);
			map.getBoxes().remove(0);
	}

	/**
	* Finds a player path for the given box path.
	*
	*
	* @author Joakim Andrén <joaandr@kth.se>
	* @param Startmap map Should be a clone, as it is altered
	* @param Boxpath String with the given box path
	* @param BoxPos Position, initial position of the box
	* @throws CloneNotSupportedException 
	 * @throws IOException 
	*/

	//Converts a box path to the required player path.
	public String findPlayerPathFromBoxPath(String BoxPath, Map StartMap, Position PlayerPos, Position BoxPos) throws CloneNotSupportedException, IOException{
		String PlayerPath=new String();
		char lastdir=' ';
		Position newPlayerPos=new Position();
		Position initialPositionPlayer = PlayerPos;


		for(int i=0;i<BoxPath.length();i++){                    
			//System.in.read();
			//System.out.println(StartMap);
			char newdir=BoxPath.charAt(i);
			if(lastdir==newdir){ //If the box path follows the same direction, just move the player one additional step in that direction.
				PlayerPath=PlayerPath+newdir;

				if(newdir=='U'){PlayerPos.up(StartMap);}
				if(newdir=='D'){PlayerPos.down(StartMap);}
				if(newdir=='L'){PlayerPos.left(StartMap);}
				if(newdir=='R'){PlayerPos.right(StartMap);}
			}	
			else{   //Else find a path for the player to the correct side of the box.
				newPlayerPos=BoxPos.clone();
				if(newdir=='U'){newPlayerPos.down(StartMap);}
				if(newdir=='D'){newPlayerPos.up(StartMap);}
				if(newdir=='L'){newPlayerPos.right(StartMap);}
				if(newdir=='R'){newPlayerPos.left(StartMap);}
				//System.out.println(StartMap);
				try {
					PlayerPath=PlayerPath+findPath(StartMap,PlayerPos,newPlayerPos, ECell.PLAYER).toLowerCase(); 
				} catch (PathNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PlayerPos=newPlayerPos.clone();
				PlayerPath=PlayerPath+newdir;
				if(newdir=='U'){PlayerPos.up(StartMap);}
				if(newdir=='D'){PlayerPos.down(StartMap);}
				if(newdir=='L'){PlayerPos.left(StartMap);}
				if(newdir=='R'){PlayerPos.right(StartMap);}
				//System.out.println(StartMap);
			}

			StartMap.set(Cell.ECell.EMPTY_FLOOR,BoxPos);
			if(newdir=='U'){BoxPos.up(StartMap);}
			if(newdir=='D'){BoxPos.down(StartMap);}
			if(newdir=='L'){BoxPos.left(StartMap);}
			if(newdir=='R'){BoxPos.right(StartMap);}
			StartMap.set(Cell.ECell.BOX,BoxPos);
			//System.out.println(StartMap);
			lastdir=newdir;
		}
		StartMap.setPlayerPosition(PlayerPos);
		StartMap.set(ECell.EMPTY_FLOOR, initialPositionPlayer);
		StartMap.set(Cell.ECell.BOX_ON_GOAL,BoxPos);
		StartMap.set(ECell.PLAYER, PlayerPos);


		//System.out.println(StartMap);


		return PlayerPath;	
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
					PlayerPath=findPath(map,PlayerPos,newPlayerPos, ECell.PLAYER).toLowerCase(); 
				} catch (PathNotFoundException e) {
					return null;
				}
		}
		PlayerPath=PlayerPath+Boxdir;
		return PlayerPath;	
	}

	/**
	* Encapsulates findPath() in a boolean function and stores its result in paths[boxIndx].
	* @throws CloneNotSupportedException 
	*
	*
	*/
	private boolean pathExists(Map m, String[] paths, int boxIndx, int g, Cell.ECell cellType) throws CloneNotSupportedException {
		try {
			
			//System.out.println(m.getBoxes());

			//System.out.println("path:" + paths[boxIndx]);

			System.out.println(" Box Index : " +boxIndx);
			paths[boxIndx] = findPath(m, m.getBoxes().get(0).getPosition(), m.getGoals().get(g),cellType);

			
			//if (cellType == ECell.BOX)
                        //    paths[boxIndx] = paths[boxIndx].substring(0, paths[boxIndx].length()-1);
                        System.out.println("path:" + paths[boxIndx]);
			return true;
		} catch (PathNotFoundException e) {
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
        /*
         * @author: Luis
         * Prints the moves we get for an answer.
         */
        public void SolveBoardMoves(String moves, Map map) throws IOException, CloneNotSupportedException
        {            
            Position start_position = map.getPlayerPosition();
            
            System.out.println(map);
            System.in.read();
            
            // Separate the box and player moves.
            for(char a: moves.toCharArray())
            {
                System.out.print(a);
                // Only Player moves in Upper case
                    switch (a)
                    {
                        case 'u':   map.set(ECell.EMPTY_FLOOR, start_position);
                                    map.setPlayerPosition(start_position.up(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                        case 'd':   map.set(ECell.EMPTY_FLOOR, start_position);
                                    map.setPlayerPosition(start_position.down(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                        case 'l':   map.set(ECell.EMPTY_FLOOR, start_position);
                                    map.setPlayerPosition(start_position.left(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                        case 'r':   map.set(ECell.EMPTY_FLOOR, start_position);
                                    map.setPlayerPosition(start_position.right(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                        case 'U':   map.set(ECell.EMPTY_FLOOR, start_position);                                    
                                    map.setPlayerPosition(start_position.up(map));
                                    map.set(Cell.ECell.BOX,start_position.clone().up(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                        case 'D':   map.set(ECell.EMPTY_FLOOR, start_position);                                    
                                    map.setPlayerPosition(start_position.down(map));
                                    map.set(Cell.ECell.BOX,start_position.clone().down(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                        case 'L':   map.set(ECell.EMPTY_FLOOR, start_position);                                    
                                    map.setPlayerPosition(start_position.left(map));
                                    map.set(Cell.ECell.BOX,start_position.clone().left(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                        case 'R':   map.set(ECell.EMPTY_FLOOR, start_position);                                    
                                    map.setPlayerPosition(start_position.right(map));
                                    map.set(Cell.ECell.BOX,start_position.clone().right(map));
                                    map.set(ECell.PLAYER, start_position);
                                    break;
                    
                    }                    
                    System.out.println(map);
                    System.in.read();
            }
        
        
        }
        
        public String solve(Map map) throws CloneNotSupportedException, IOException
	{
		int i = 0;
		String result = "";
                Map init = map.clone();
		for(String s : getBoxToGoalPaths(map))
		{
			//System.out.println(map);
                        String r =findPlayerPathFromBoxPath(s, map, map.getPlayerPosition(), map.getBoxes().get(i).getPosition());
                        System.out.println (r);
			result +=r; 
			i++;
		}
                SolveBoardMoves(result,init);
		System.out.println(result.toUpperCase());
		return result.toUpperCase();
	}

	
	
	
}
