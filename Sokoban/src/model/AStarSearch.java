package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AStarSearch 
{
	
	protected Node start, goal;
	protected Map map;
	protected int maxNodes;
	protected final int MAXNODES = 1000;
	
	protected List<Node> openedList = new LinkedList<Node>();
	protected List<Node> closedList = new LinkedList<Node>();
	
	
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
		
		// We add the start node into the open List
		openedList.add(start);
		
	}
	
	public void search()
	{
		while(!openedList.isEmpty())
		{
			Collections.sort(openedList);
			Node current = (Node) ((LinkedList<Node>) openedList).getLast();
			
			if(current.isGoal(goal))
			{
				//return reconstructPath(current);
			}
			
			// We remove current from openedList
			openedList.remove(current);
			
			// We add current to closedList
			closedList.add(current);
			
			for(Node n : getNodesFromPosition(findEmptySpacesAround(current.getPosition())))
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
				}
			}
		}
	}
	
	public void reconstructPath(Node currentNode)
	{
		if(currentNode.getParent() != null)
		{
			reconstructPath(currentNode.getParent());
			System.out.println(currentNode);
		}
		else
		{
			System.out.println(currentNode.toString());
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
	public ArrayList<Position> findEmptySpacesAround(Position position)
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
