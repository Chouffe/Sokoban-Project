package model;

/**
 * Represents a cell on the board
 *
 */
public class Cell 
{
	protected Cell.ECell type;
	
	/**
	 * Represents the Sokoban's cell type
	 */
	public enum ECell
	{
		WALL, 
		PLAYER, 
		PLAYER_ON_GOAL_SQUARE, 
		BOX, 
		BOX_ON_GOAL, 
		GOAL_SQUARE, 
		EMPTY_FLOOR, 
		VISITED;
	}
	
	public Cell(Cell.ECell type)
	{
		this.type = type;
	}
	
	public static Character cellToChar(Cell.ECell cell)
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
	
	public static Cell.ECell charToCell(Character c)
	{
		if(c.equals('#'))
		{
			return Cell.ECell.WALL;
		}
		else if(c.equals('@'))
		{
			return Cell.ECell.PLAYER;
		}
		else if(c.equals('+'))
		{
			return Cell.ECell.PLAYER_ON_GOAL_SQUARE;
		}
		else if(c.equals('$'))
		{
			return Cell.ECell.BOX;
		}
		else if(c.equals('*'))
		{
			return Cell.ECell.BOX_ON_GOAL;
		}
		else if(c.equals('.'))
		{
			return Cell.ECell.GOAL_SQUARE;
		}
		else if(c.equals(' '))
		{
			return Cell.ECell.EMPTY_FLOOR;
		}
		else if(c.equals(':'))
		{
			return Cell.ECell.VISITED;
		}
		else
		{
			return null;
		}
	}
	

	public Cell.ECell getType() {
		return type;
	}

	public void setType(Cell.ECell type) {
		this.type = type;
	}
	
	
}
