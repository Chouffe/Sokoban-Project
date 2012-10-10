

/**
 * Represents a cell on the board
 *
 */
public class Cell 
{
	protected Cell.ECell type;
	protected boolean isAccessible;
	
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
		VISITED,
		FINAL_BOX_ON_GOAL;
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
		case FINAL_BOX_ON_GOAL:
			return 'X';
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
		else if(c.equals('X'))
		{
			return Cell.ECell.FINAL_BOX_ON_GOAL;
		}
		else
		{
			return null;
		}
	}
	

	public Cell.ECell getType() {
		return type;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public void setAccessible(boolean accessible) {
		isAccessible = accessible;
	}

	public void setType(Cell.ECell type) {
		this.type = type;
	}
	
	
}
