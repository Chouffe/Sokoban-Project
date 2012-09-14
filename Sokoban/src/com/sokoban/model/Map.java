package com.sokoban.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a map
 *
 */

public class Map implements Cloneable
{
	protected ArrayList<ArrayList<ECell>> map = null;
	protected ArrayList<Position> goals = null;
	
	// Todo : model with a Player, not only a position
	protected Position playerPosition = null;
	protected int height = 0;
	protected int width = 0;
	
	public Map()
	{
		map = new ArrayList<ArrayList<ECell>>();
		goals = new ArrayList<Position>();
		playerPosition = new Position();
		height = 0;
		width = 0;
	}
	
	public Map(BufferedReader bf)
	{
		this();
		map = extractBuffer(bf);
		height = map.size();
		fillTheMapWithEmptyFloor();
	}
	
	public Map(ArrayList<String> sList)
	{
		this();
		map = extractString(sList);
		height = map.size();
		fillTheMapWithEmptyFloor();
	}
	
	@Override
	public Map clone() throws CloneNotSupportedException {
		
		
		Map copie = (Map)super.clone();
		
		// Clone the goals
		copie.goals = new ArrayList<Position>();

		for(Position position : goals)
		{
			copie.goals.add(position);
		}
		
		// Clone the map
		copie.map = null;
		copie.map = new ArrayList<ArrayList<ECell>>();
		copie.map.clear();
		
		
		for(ArrayList<ECell> rowCells : map)
		{
			ArrayList<ECell> cloneRow = new ArrayList<ECell>();
			for(ECell cell : rowCells)
			{
				switch(cell)
				{
					case WALL:
						cloneRow.add(ECell.WALL);
						break;
					case PLAYER:
						cloneRow.add(ECell.PLAYER);
						break;
					case BOX:
						cloneRow.add(ECell.BOX);
						break;
					case BOX_ON_GOAL:
						cloneRow.add(ECell.BOX_ON_GOAL);
						break;
					case GOAL_SQUARE:
						cloneRow.add(ECell.GOAL_SQUARE);
						break;
					case EMPTY_FLOOR:
						cloneRow.add(ECell.EMPTY_FLOOR);
						break;
					case VISITED:
						cloneRow.add(ECell.VISITED);
						break;
				}
				
			}
			copie.map.add(cloneRow);
		}
		
		// Clone the player position
		copie.playerPosition = playerPosition.clone();
		
		return copie;
	}
	
	public void movePlayer(Position position) throws CloneNotSupportedException
	{
		
		if(getCellFromPosition(position).equals(ECell.GOAL_SQUARE))
		{
			set(ECell.PLAYER_ON_GOAL_SQUARE, position);
			set(ECell.VISITED, playerPosition);
			playerPosition = position.clone();
		}
		else if(getCellFromPosition(position).equals(ECell.EMPTY_FLOOR))
		{
			set(ECell.PLAYER, position);
			set(ECell.VISITED, playerPosition);
			playerPosition = position.clone();
		}
	}
	
	public String toString()
	{
		String result = "";
		
		for(ArrayList<ECell> rowCell : map)
		{
			for(ECell cell : rowCell)
			{
				result += cellToChar(cell);
			}
			result += "\n";
		}
		return result;
	}
	
	public void set(ECell cell, Position position)
	{
		if(isPositionOnTheMap(position))
		{
			ArrayList<ECell> row = new ArrayList<ECell>();
			row = map.get(position.getI());
			row.set(position.getJ(), cell);
			
			map.set(position.getI(), row);
		}
	}
	
	protected ArrayList<ArrayList<ECell>> extractString(ArrayList<String> sList)
	{
		
		int i = 0;
		for(String s : sList)
		{
			
			ArrayList<ECell> mapRow = new ArrayList<ECell>();
			
			if(s.length() > width)
			{
				width = s.length();
			}
			
			for(int j = 0; j < s.length(); j++)
			{
				if(charToCell(s.charAt(j)) != null)
				{
					ECell cell = charToCell(s.charAt(j));
									
					switch(cell)
					{
						case GOAL_SQUARE:
							addGoal(new Position(i,j));
							break;
						case PLAYER:
							setPlayerPosition(new Position(i,j));
							break;
						case PLAYER_ON_GOAL_SQUARE:
							setPlayerPosition(new Position(i,j));
							break;
					}
				
					mapRow.add(cell);
				}
			}
			
			map.add(mapRow);
			i++;
		}
		
		return map;
	}
	
	protected ArrayList<ArrayList<ECell>> extractBuffer(BufferedReader bf)
	{
		try
		{
			map = new ArrayList<ArrayList<ECell>>();
			
			int i = 0;
			String currentLine;
			
			while ((currentLine = bf.readLine()) != null) {
				
				ArrayList<ECell> mapRow = new ArrayList<ECell>();
				
				if(currentLine.length() > width)
				{
					width = currentLine.length();
				}
				
				for(int j = 0; j < currentLine.length(); j++)
				{
					if(charToCell(currentLine.charAt(j)) != null)
					{
						ECell cell = charToCell(currentLine.charAt(j));
										
						switch(cell)
						{
							case GOAL_SQUARE:
								addGoal(new Position(i,j));
								break;
							case PLAYER:
								setPlayerPosition(new Position(i,j));
								break;
							case PLAYER_ON_GOAL_SQUARE:
								setPlayerPosition(new Position(i,j));
								break;
						}
					
						mapRow.add(cell);
					}
				}
				
				map.add(mapRow);
				i++;
			}
			
			return map;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				if (bf != null)bf.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return null;
	}
	
	protected void fillTheMapWithEmptyFloor()
	{
		ArrayList<ArrayList<ECell>> filledMap = new ArrayList<ArrayList<ECell>>();			
		
		for(ArrayList<ECell> rowCellsInit : map)
		{
			ArrayList<ECell> row = new ArrayList<ECell>();
			for(int j = 0; j < width; j++)
			{
				row.add(ECell.EMPTY_FLOOR);
			}
			int k = 0;
			for(ECell cell : rowCellsInit)
			{
				switch(cell)
				{
				case WALL:
					row.set(k, ECell.WALL);
					break;
				case PLAYER:
					row.set(k, ECell.PLAYER);
					break;
				case PLAYER_ON_GOAL_SQUARE:
					row.set(k, ECell.PLAYER_ON_GOAL_SQUARE);
					break;
				case BOX:
					row.set(k, ECell.BOX);
					break;
				case BOX_ON_GOAL:
					row.set(k, ECell.BOX_ON_GOAL);
					break;
				case GOAL_SQUARE:
					row.set(k, ECell.GOAL_SQUARE);
					break;
				case EMPTY_FLOOR:
					row.set(k, ECell.EMPTY_FLOOR);
					break;
				case VISITED:
					row.set(k, ECell.VISITED);
					break;
				}
				k++;
			}
			
			filledMap.add(row);
		}
		
		map = filledMap;
	}
	
	public boolean isPositionOnTheMap(Position position)
	{
		return (position.getI() >= 0 && position.getJ() >= 0 
				&& map != null 
				&& position.getI() < map.size() 
				&& position.getJ() < width);
	}
	
	public ECell getCellFromPosition(Position position)
	{
		if(isPositionOnTheMap(position))
		{
			return map.get(position.getI()).get(position.getJ());
		}
		else
		{
			return null;
		}
	}
	
	public Character cellToChar(ECell cell)
	{
		switch(cell)
		{
		case WALL:
			return '#';
		case PLAYER:
			return '@';
		case PLAYER_ON_GOAL_SQUARE:
			return '+';
		case BOX:
			return '$';
		case BOX_ON_GOAL:
			return '*';
		case EMPTY_FLOOR:
			return ' ';
		case GOAL_SQUARE:
			return '.';
		case VISITED:
			return ':';
		default:
			return null;
		}
		
	}
	
	public ECell charToCell(Character c)
	{
		if(c.equals('#'))
		{
			return ECell.WALL;
		}
		else if(c.equals('@'))
		{
			return ECell.PLAYER;
		}
		else if(c.equals('+'))
		{
			return ECell.PLAYER_ON_GOAL_SQUARE;
		}
		else if(c.equals('$'))
		{
			return ECell.BOX;
		}
		else if(c.equals('*'))
		{
			return ECell.BOX_ON_GOAL;
		}
		else if(c.equals('.'))
		{
			return ECell.GOAL_SQUARE;
		}
		else if(c.equals(' '))
		{
			return ECell.EMPTY_FLOOR;
		}
		else if(c.equals(':'))
		{
			return ECell.VISITED;
		}
		else
		{
			return null;
		}
	}

	public ArrayList<Position> getGoals() {
		return goals;
	}

	public void setGoals(ArrayList<Position> goals) {
		this.goals = goals;
	}
	
	protected void addGoal(Position position)
	{
		goals.add(position);
	}

	public ArrayList<ArrayList<ECell>> getMap() {
		return map;
	}

	public void setMap(ArrayList<ArrayList<ECell>> map) {
		this.map = map;
	}

	public Position getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Position playerPosition) {
		this.playerPosition = playerPosition;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	
	
}