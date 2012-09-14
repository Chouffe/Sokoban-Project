package com.sokoban.model;

import java.util.ArrayList;

/**
 * 
 * Represents the moves of the player
 * UP, DOWN, RIGHT, LEFT
 * @see EMoves
 *
 */
public class Moves implements Cloneable
{
	ArrayList<EMove> moves;
	
	public Moves()
	{
		moves = new ArrayList<EMove>();
	}
	
	public Moves(ArrayList<EMove> moves)
	{
		for(EMove move : moves)
		{
			this.moves.add(move);
		}
	}
	
	public void up()
	{
		moves.add(EMove.UP);
	}
	public void down()
	{
		moves.add(EMove.DOWN);
	}
	public void right()
	{
		moves.add(EMove.RIGHT);
	}
	public void left()
	{
		moves.add(EMove.LEFT);
	}
	
	public Moves pop()
	{
		if(moves.size() != 0)
		{
			moves.remove(moves.size()-1);
		}
		return this;
	}
	
	public Moves addMove(Position position1, Position position2)
	{
		int deltaI = position2.getI() - position1.getI();
		int deltaJ = position2.getJ() - position1.getJ();
		
		if(deltaI == -1 && deltaJ == 0)
		{
			moves.add(EMove.UP);
		}
		else if(deltaI == 1 && deltaJ == 0)
		{
			moves.add(EMove.DOWN);
		}
		else if(deltaI == 0 && deltaJ == 1)
		{
			moves.add(EMove.RIGHT);
		}
		else if(deltaI == 0 && deltaJ == -1)
		{
			moves.add(EMove.LEFT);
		}
		
		return this;
	}
	
	public String toString()
	{
		String result = "";
		
		for(EMove move : moves)
		{
			switch(move)
			{
				case UP:
					result += "U";
					break;
					
				case DOWN:
					result += "D";
					break;
					
				case RIGHT:
					result += "R";
					break;
					
				case LEFT:
					result += "L";
					break;
			}
		}
		
		return result;
	}

	public ArrayList<EMove> getMoves() {
		return moves;
	}

	public void setMoves(ArrayList<EMove> moves) {
		this.moves = moves;
	}
	
	public void clear()
	{
		moves.clear();
	}

	@Override
	public Moves clone() throws CloneNotSupportedException {

		Moves clone = (Moves)super.clone();
		ArrayList<EMove> movesClone = new ArrayList<EMove>();
		
		for(EMove move : moves)
		{
			switch(move)
			{
			case UP:
				movesClone.add(EMove.UP);
				break;
			case DOWN:
				movesClone.add(EMove.DOWN);
				break;
			case RIGHT:
				movesClone.add(EMove.RIGHT);
				break;
			case LEFT:
				movesClone.add(EMove.LEFT);
				break;
			}
		}
		clone.moves = movesClone;
		return clone;
	}
	
	
}
